package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Skills;

/**
 * <font color=#00800>空的魔法卷轴(等级1)40090</font><BR>
 * Blank Scroll (Level 1)<BR>
 * <font color=#00800>空的魔法卷轴(等级2)40091</font><BR>
 * Blank Scroll (Level 2)<BR>
 * <font color=#00800>空的魔法卷轴(等级3)40092</font><BR>
 * Blank Scroll (Level 3)<BR>
 * <font color=#00800>空的魔法卷轴(等级4)40093</font><BR>
 * Blank Scroll (Level 4)<BR>
 * <font color=#00800>空的魔法卷轴(等级5)40094</font><BR>
 * Blank Scroll (Level 5)<BR>
 * 
 * @author dexc
 * 
 */
public class Blank_MagicReel extends ItemExecutor {

    /**
	 *
	 */
    private Blank_MagicReel() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Blank_MagicReel();
    }

    /**
     * 道具物件执行
     * 
     * @param data
     *            参数
     * @param pc
     *            执行者
     * @param item
     *            物件
     */
    @Override
    public void execute(final int[] data, final L1PcInstance pc,
            final L1ItemInstance item) {
        final int blanksc_skillid = data[0];
        final int itemId = item.getItemId();
        if (pc.isWizard()) { // 法师才可使用
            // 魔法书与学习的技能要等级匹配
            if (((itemId == 40090) && (blanksc_skillid <= 7))
                    || ((itemId == 40091) && (blanksc_skillid <= 15))
                    || ((itemId == 40092) && (blanksc_skillid <= 22))
                    || ((itemId == 40093) && (blanksc_skillid <= 31))
                    || ((itemId == 40094) && (blanksc_skillid <= 39))) {
                final L1ItemInstance spellsc = ItemTable.get().createItem(
                        40859 + blanksc_skillid);
                if (spellsc != null) {
                    if (pc.getInventory().checkAddItem(spellsc, 1) == L1Inventory.OK) {
                        final L1Skills l1skills = SkillsTable.get()
                                .getTemplate(blanksc_skillid + 1);
                        if (pc.getCurrentHp() <= l1skills.getHpConsume()) {
                            pc.sendPackets(new S_ServerMessage(279)); // \f1因体力不足而无法使用魔法。
                            return;
                        }

                        if (pc.getCurrentMp() < l1skills.getMpConsume()) {
                            pc.sendPackets(new S_ServerMessage(278)); // \f1施咒失败。
                            return;
                        }

                        if (l1skills.getItemConsumeId() != 0) { // 检查技能所需材料
                            if (!pc.getInventory().checkItem(
                                    // 检查技能所需材料的数量
                                    l1skills.getItemConsumeId(),
                                    l1skills.getItemConsumeCount())) {
                                pc.sendPackets(new S_ServerMessage(299)); // \f1施放魔法所需材料不足。
                                return;
                            }
                        }

                        pc.setCurrentHp(pc.getCurrentHp()
                                - l1skills.getHpConsume());
                        pc.setCurrentMp(pc.getCurrentMp()
                                - l1skills.getMpConsume());

                        int lawful = pc.getLawful() + l1skills.getLawful();
                        if (lawful > 32767) {
                            lawful = 32767;
                        }
                        if (lawful < -32767) {
                            lawful = -32767;
                        }

                        pc.setLawful(lawful);

                        if (l1skills.getItemConsumeId() != 0) { // 检查技能所需材料
                            pc.getInventory().consumeItem(
                                    l1skills.getItemConsumeId(),
                                    l1skills.getItemConsumeCount());
                        }
                        pc.getInventory().removeItem(item, 1);
                        pc.getInventory().storeItem(spellsc);
                    }
                }
            } else {
                pc.sendPackets(new S_ServerMessage(591)); // 这张卷轴太易碎所以无法纪录魔法。
            }
        } else {
            pc.sendPackets(new S_ServerMessage(264)); // \f1你的职业无法使用此装备。
        }
    }
}
