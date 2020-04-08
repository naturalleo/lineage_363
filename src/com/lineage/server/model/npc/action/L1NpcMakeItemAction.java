package com.lineage.server.model.npc.action;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1ObjectAmount;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.npc.L1NpcHtml;
import com.lineage.server.serverpackets.S_HowManyMake;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.IterableElementList;

public class L1NpcMakeItemAction extends L1NpcXmlAction {

    private final List<L1ObjectAmount<Integer>> _materials = new ArrayList<L1ObjectAmount<Integer>>();
    private final List<L1ObjectAmount<Integer>> _items = new ArrayList<L1ObjectAmount<Integer>>();
    private final boolean _isAmountInputable;
    private final L1NpcAction _actionOnSucceed;
    private final L1NpcAction _actionOnFail;

    public L1NpcMakeItemAction(final Element element) {
        super(element);

        this._isAmountInputable = L1NpcXmlParser.getBoolAttribute(element,
                "AmountInputable", true);
        final NodeList list = element.getChildNodes();
        for (final Element elem : new IterableElementList(list)) {
            if (elem.getNodeName().equalsIgnoreCase("Material")) {
                final int id = Integer.valueOf(elem.getAttribute("ItemId"));
                final long amount = Long.valueOf(elem.getAttribute("Amount"));
                this._materials.add(new L1ObjectAmount<Integer>(id, amount));
                continue;
            }
            if (elem.getNodeName().equalsIgnoreCase("Item")) {
                final int id = Integer.valueOf(elem.getAttribute("ItemId"));
                final long amount = Long.valueOf(elem.getAttribute("Amount"));
                this._items.add(new L1ObjectAmount<Integer>(id, amount));
                continue;
            }
        }

        if (this._items.isEmpty() || this._materials.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Element elem = L1NpcXmlParser.getFirstChildElementByTagName(element,
                "Succeed");
        this._actionOnSucceed = elem == null ? null : new L1NpcListedAction(
                elem);
        elem = L1NpcXmlParser.getFirstChildElementByTagName(element, "Fail");
        this._actionOnFail = elem == null ? null : new L1NpcListedAction(elem);
    }

    private boolean makeItems(final L1PcInstance pc, final String npcName,
            final long amount) {
        if (amount <= 0) {
            return false;
        }

        boolean isEnoughMaterials = true;
        for (final L1ObjectAmount<Integer> material : this._materials) {
            if (!pc.getInventory().checkItemNotEquipped(material.getObject(),
                    material.getAmount() * amount)) {
                final L1Item temp = ItemTable.get().getTemplate(
                        material.getObject());
                pc.sendPackets(new S_ServerMessage(337, temp.getNameId()
                        + "("
                        + ((material.getAmount() * amount) - pc.getInventory()
                                .countItems(temp.getItemId())) + ")"));
                isEnoughMaterials = false;
            }
        }
        if (!isEnoughMaterials) {
            return false;
        }

        // 容量と重量の计算
        int countToCreate = 0; // アイテムの个数（缠まる物は1个）
        int weight = 0;

        for (final L1ObjectAmount<Integer> makingItem : this._items) {
            final L1Item temp = ItemTable.get().getTemplate(
                    makingItem.getObject());
            if (temp.isStackable()) {
                if (!pc.getInventory().checkItem(makingItem.getObject())) {
                    countToCreate += 1;
                }
            } else {
                countToCreate += makingItem.getAmount() * amount;
            }
            weight += temp.getWeight() * (makingItem.getAmount() * amount)
                    / 1000;
        }
        // 容量确认
        if (pc.getInventory().getSize() + countToCreate > 180) {
            pc.sendPackets(new S_ServerMessage(263)); // 263 \f1一个角色最多可携带180个道具。
            return false;
        }
        // 重量确认
        if (pc.getMaxWeight() < pc.getInventory().getWeight() + weight) {
            pc.sendPackets(new S_ServerMessage(82)); // 82 此物品太重了，所以你无法携带。
            return false;
        }

        for (final L1ObjectAmount<Integer> material : this._materials) {
            // 材料消费
            pc.getInventory().consumeItem(material.getObject(),
                    material.getAmount() * amount);
        }

        for (final L1ObjectAmount<Integer> makingItem : this._items) {
            final L1ItemInstance item = pc.getInventory().storeItem(
                    makingItem.getObject(), makingItem.getAmount() * amount);
            if (item != null) {
                String itemName = ItemTable.get()
                        .getTemplate(makingItem.getObject()).getNameId();
                if (makingItem.getAmount() * amount > 1) {
                    itemName = itemName + " (" + makingItem.getAmount()
                            * amount + ")";
                }
                pc.sendPackets(new S_ServerMessage(143, npcName, itemName)); // \f1%0が%1をくれました。
            }
        }
        return true;
    }

    /**
     * 指定されたインベントリ内に、素材が何セットあるか数える
     */
    private long countNumOfMaterials(final L1PcInventory inv) {
        long count = Long.MAX_VALUE;
        for (final L1ObjectAmount<Integer> material : this._materials) {
            final long numOfSet = inv.countItems(material.getObject())
                    / material.getAmount();
            count = Math.min(count, numOfSet);
        }
        return count;
    }

    @Override
    public L1NpcHtml execute(final String actionName, final L1PcInstance pc,
            final L1Object obj, final byte[] args) {
        final long numOfMaterials = this.countNumOfMaterials(pc.getInventory());
        if ((1 < numOfMaterials) && this._isAmountInputable) {
            pc.sendPackets(new S_HowManyMake(obj.getId(), (int) numOfMaterials,
                    actionName));
            return null;
        }
        return this.executeWithAmount(actionName, pc, obj, 1);
    }

    @Override
    public L1NpcHtml executeWithAmount(final String actionName,
            final L1PcInstance pc, final L1Object obj, final long amount) {
        final L1NpcInstance npc = (L1NpcInstance) obj;
        L1NpcHtml result = null;
        if (this.makeItems(pc, npc.getNameId(), amount)) {
            if (this._actionOnSucceed != null) {
                result = this._actionOnSucceed.execute(actionName, pc, obj,
                        new byte[0]);
            }
        } else {
            if (this._actionOnFail != null) {
                result = this._actionOnFail.execute(actionName, pc, obj,
                        new byte[0]);
            }
        }
        return result == null ? L1NpcHtml.HTML_CLOSE : result;
    }

    @Override
    public void execute(String actionName, String npcid) {
        // TODO Auto-generated method stub

    }

}
