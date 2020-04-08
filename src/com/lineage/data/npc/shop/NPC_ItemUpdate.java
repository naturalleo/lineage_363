package com.lineage.data.npc.shop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.ItemUpdateTable;
import com.lineage.server.datatables.lock.CharItemsReading;
import com.lineage.server.datatables.lock.CharShiftingReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_AddItem;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_DeleteInventoryItem;
import com.lineage.server.serverpackets.S_HelpMessage;
import com.lineage.server.serverpackets.S_ItemCount;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_PowerItemList;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1ItemUpdate;
import com.lineage.server.world.World;

/**
 * 物品升级专员
 * 
 * 
 * DELETE FROM `npc` WHERE `npcid`='91141'; DELETE FROM `npc` WHERE
 * `npcid`='91142'; DELETE FROM `npc` WHERE `npcid`='91143'; INSERT INTO `npc`
 * VALUES ('91141', '物品升级专员', '物品升级专员', 'shop.NPC_ItemUpdate', '', 'L1Merchant',
 * '6757', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '', '0',
 * '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '', '1', '-1',
 * '-1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
 * '0', '0', '0', '0', '1', '0', '0', '-1', '0', '0', '0', '0', '0'); INSERT
 * INTO `npc` VALUES ('91142', '物品升级专员', '物品升级专员', 'shop.NPC_ItemUpdate', '',
 * 'L1Merchant', '6757', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
 * '0', '', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '',
 * '1', '-1', '-1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
 * '0', '0', '0', '0', '0', '0', '1', '0', '0', '-1', '0', '0', '0', '0', '0');
 * INSERT INTO `npc` VALUES ('91143', '物品升级专员', '物品升级专员', 'shop.NPC_ItemUpdate',
 * '', 'L1Merchant', '6757', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
 * '0', '0', '', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
 * '0', '', '1', '-1', '-1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
 * '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '-1', '0', '0', '0',
 * '0', '0');
 * 
 * @author daien
 * 
 */
public class NPC_ItemUpdate extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(NPC_ItemUpdate.class);
    private static final Random _random = new Random();

    /**
	 *
	 */
    private NPC_ItemUpdate() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new NPC_ItemUpdate();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        pc.set_mode_id(0);
        pc.set_check_item(false);
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), L1ItemUpdate._html_01));
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        if (cmd.matches("[0-9]+")) {
            if (pc.get_mode_id() != 0 && !pc.get_check_item()) {
                check_item(pc, npc, cmd);
            }

        } else if (cmd.equalsIgnoreCase("up")) {// 选择我要升级的装备
            pc.set_mode_id(0);
            show_item(pc, npc);

        } else if (cmd.startsWith("ut")) {// 选择我要升级的装备
            if (pc.get_check_item()) {
                get_item(pc, npc, cmd);
            }
        }
    }

    private void get_item(L1PcInstance pc, L1NpcInstance npc, String cmd) {
        try {
            final int index = Integer.parseInt(cmd.substring(2));

            final L1ItemInstance tgitem = pc.getInventory().getItem(
                    pc.get_mode_id());
            if (tgitem == null) {
                _log.error("升级道具 对象 OBJID异常 背包中无该道具:" + pc.get_mode_id());
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }
            final ArrayList<L1ItemUpdate> list = ItemUpdateTable.get().get(
                    tgitem.getItemId());

            final L1ItemUpdate newitem = list.get(index);
            if (newitem == null) {
                _log.error("升级道具 对象 ITEMID 找不到升级模组:" + tgitem.getItemId());
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }

            final int toid = newitem.get_toid();
            final int[] needids = newitem.get_needids();
            final int[] needcounts = newitem.get_needcounts();
            final int probability = newitem.get_probability();

            // 需要物件不足
            if (CreateNewItem.checkNewItem(pc, needids, needcounts) >= 1) {// 传回可交换道具数小于1(需要物件不足)
                // 收回任务需要物件 给予任务完成物件
                if (CreateNewItem.delItems(pc, needids, needcounts, 1)) {// 删除需要物品
                	final int rnd = _random.nextInt(100)+1;
                	if (rnd <= probability) {
                        final L1Item l1item = ItemTable.get().getTemplate(toid);
                        final String src_name = tgitem.getItem().getName();
                        // 移除物品显示
                        pc.sendPackets(new S_DeleteInventoryItem(tgitem.getId()));
                        tgitem.setItemId(toid);// 变更ITEMID
                        tgitem.setItem(l1item);
                        tgitem.setBless(l1item.getBless());
                        // 更新数据
                        CharItemsReading.get().updateItemId_Name(tgitem);

                        // 还原显示
                        pc.sendPackets(new S_AddItem(tgitem));
                        pc.sendPackets(new S_ServerMessage(403, tgitem.getLogName())); // 获得0%。

                        // 建立纪录
                        CharShiftingReading.get().newShifting(pc, 0, src_name,
                                tgitem.getId(), tgitem.getItem(), tgitem, 0);
        				final StringBuilder s = new StringBuilder();
        				s.append("玩家" + pc.getName() + " 成功合成  " + tgitem.getLogName());
                        // 产生讯息封包 (强化成功)
                        World.get()
                                .broadcastPacketToAll(
                                        new S_HelpMessage(s.toString()));
                        pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4541));//打造成功魔法效果
                	} else {
                		pc.sendPackets(new S_SystemMessage("装备打造失败。"));
                		pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4581));//打造失败魔法效果
                	}
                }

            } else {
                // 关闭对话窗
                pc.sendPackets(new S_CloseList(pc.getId()));
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 显示升级资讯
     * 
     * @param pc
     * @param npc
     * @param cmd
     */
    private void check_item(L1PcInstance pc, L1NpcInstance npc, String cmd) {
        try {
            final int index = Integer.parseInt(cmd);

            final L1ItemInstance tgitem = pc.getInventory().getItem(
                    pc.get_mode_id());
            if (tgitem == null) {
                _log.error("升级道具 对象 OBJID异常 背包中无该道具:" + pc.get_mode_id());
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }
            final ArrayList<L1ItemUpdate> list = ItemUpdateTable.get().get(
                    tgitem.getItemId());
            final L1ItemUpdate newitem = list.get(index);
            if (newitem == null) {
                _log.error("升级道具 对象 ITEMID 找不到升级模组:" + tgitem.getItemId());
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }

            final int toids = newitem.get_toid();
            final int[] needids = newitem.get_needids();
            final int[] needcounts = newitem.get_needcounts();

            final L1Item getitem = ItemTable.get().getTemplate(toids);
            String need = "";
            for (int i = 0; i < needids.length; i++) {
                final L1Item l1item = ItemTable.get().getTemplate(needids[i]);
                need += l1item.getName() + "(" + needcounts[i] + ")  ";
            }

            final String[] date = new String[] { tgitem.getItem().getName(),
                    getitem.getName(), need };
            pc.set_check_item(true);
            pc.sendPackets(new S_ItemCount(npc.getId(), 1, 1,
                    L1ItemUpdate._html_03, "ut" + index, date));

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 显示可升级装备清单
     * 
     * @param pc
     * @param npc
     */
    private void show_item(L1PcInstance pc, L1NpcInstance npc) {
        try {
            final ArrayList<L1ItemInstance> list = new ArrayList<L1ItemInstance>();
            final List<L1ItemInstance> items = pc.getInventory().getItems();
            for (L1ItemInstance item : items) {
                if (item.getItem().isStackable()) {
                    continue;
                }
                if (item.isEquipped()) {// 使用中物件
                    continue;
                }
                if (ItemUpdateTable.get().get(item.getItemId()) == null) {
                    continue;
                }
                list.add(item);
            }

            if (list.size() <= 0) {
                pc.sendPackets(new S_ServerMessage("\\fR找不到可以升级的装备。"));
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }

            pc.sendPackets(new S_PowerItemList(pc, npc.getId(), list));

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
