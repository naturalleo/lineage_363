package com.lineage.server.model.drop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigBoxMsg;
import com.lineage.data.event.PowerItemSet;
import com.lineage.server.datatables.ItemMsgTable;
import com.lineage.server.datatables.lock.CharItemPowerReading;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1ItemPower_name;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.ListMapUtil;
import com.lineage.server.world.World;

/**
 * NPC掉落物品的分配
 * 
 * @author dexc
 * 
 */
public class DropShare implements DropShareExecutor {

    private static final Log _log = LogFactory.getLog(DropShare.class);

    private static final Random _random = new Random();

    // 正向
    private static final byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };

    private static final byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

    /**
     * 掉落物品的分配
     * 
     * @param npc
     *            死亡的NPC
     * @param acquisitorList
     * @param hateList
     */
    @Override
    public void dropShare(final L1NpcInstance npc,
            final ArrayList<L1Character> acquisitorList,
            final ArrayList<Integer> hateList) {
        DropShareR dropShareR = new DropShareR(npc, acquisitorList, hateList);
        GeneralThreadPool.get().schedule(dropShareR, 0);
    }

    private class DropShareR implements Runnable {

        final L1NpcInstance _npc;
        final ArrayList<L1Character> _acquisitorList;
        final ArrayList<Integer> _hateList;

        private DropShareR(L1NpcInstance npc,
                ArrayList<L1Character> acquisitorList,
                ArrayList<Integer> hateList) {
            _npc = npc;
            _acquisitorList = acquisitorList;
            _hateList = hateList;
        }

        @Override
        public void run() {
            try {
                // _log.info("NPC掉落物品的分配: " + _npc.getName());
                final L1Inventory inventory = _npc.getInventory();
                if (inventory == null) {
                    return;
                }
                if (inventory.getSize() <= 0) {
                    return;
                }
                if (_acquisitorList.size() != _hateList.size()) {
                    // _log.info("acquisitorList.size() != hateList.size()");
                    return;
                }
                // ヘイトの合计を取得
                int totalHate = 0;
                L1Character acquisitor;
                for (int i = _hateList.size() - 1; i >= 0; i--) {
                    acquisitor = _acquisitorList.get(i);

                    if ((ConfigAlt.AUTO_LOOT == 2) // オートルーティング２の场合はサモン及びペットは省く
                            && ((acquisitor instanceof L1SummonInstance) || (acquisitor instanceof L1PetInstance))) {
                        _acquisitorList.remove(i);
                        _hateList.remove(i);

                    } else if ((acquisitor != null)
                            && (acquisitor.getMapId() == _npc.getMapId())
                            && (acquisitor.getLocation().getTileLineDistance(
                                    _npc.getLocation()) <= ConfigAlt.LOOTING_RANGE)) {
                        totalHate += _hateList.get(i);

                    } else {
                        // _log.info("NPC掉落物品分配无对象 删除掉落物: " + npc.getName());
                        _acquisitorList.remove(i);
                        _hateList.remove(i);
                    }
                }

                // 掉落物品的分配
                L1Inventory targetInventory = null;
                L1PcInstance player;
                final Random random = new Random();
                int randomInt;
                int chanceHate;
                int itemId;
                final List<L1ItemInstance> list = inventory.getItems();

                if (list.isEmpty()) {
                    return;
                }

                if (list.size() <= 0) {
                    return;
                }
                for (L1ItemInstance item : list) {
                    itemId = item.getItemId();

                    if ((item.getItem().getType2() == 0)
                            && (item.getItem().getType() == 2)) { // 照明道具
                        item.setNowLighting(false);
                    }

                    if (((ConfigAlt.AUTO_LOOT != 0) || (itemId == L1ItemId.ADENA))
                            && (totalHate > 0)) {
                        randomInt = random.nextInt(totalHate);
                        chanceHate = 0;
                        for (int j = _hateList.size() - 1; j >= 0; j--) {
                            Thread.sleep(1);
                            chanceHate += _hateList.get(j);
                            if (chanceHate > randomInt) {
                                acquisitor = _acquisitorList.get(j);

                                if (acquisitor.getInventory().checkAddItem(
                                        item, item.getCount()) == L1Inventory.OK) {
                                    targetInventory = acquisitor.getInventory();
                                    if (acquisitor instanceof L1PcInstance) {
                                        player = (L1PcInstance) acquisitor;
//                                        if (player.isActived()) {// 挂机得不到物品
//                                        	inventory.deleteItem(item);
//                                        	return;
//                                        }// 挂机得不到物品
                                        //set_power(player, item); 关闭狩猎取得凹槽 hjx1000

                                        // 具有队伍
                                        if (player.isInParty()) {
                                            final Object[] pcs = player
                                                    .getParty().partyUsers()
                                                    .values().toArray();
                                            if (pcs.length <= 0) {
                                                return;
                                            }
                                            for (Object obj : pcs) {
                                                if (obj instanceof L1PcInstance) {
                                                    final L1PcInstance tgpc = (L1PcInstance) obj;
                                                    // 813 队员%2%s 从%0 取得 %1%o
                                                    tgpc.sendPackets(new S_ServerMessage(
                                                            813,
                                                            _npc.getNameId(),
                                                            item.getLogName(),
                                                            player.getName()));
                                                }

                                            }

                                        } else {
                                            // 143 \f1%0%s 给你 %1%o 。
                                            player.sendPackets(new S_ServerMessage(
                                                    143, _npc.getNameId(), item
                                                            .getLogName()));
                                        }

                                        if (ConfigBoxMsg.ISMSG) {
                                            if (ItemMsgTable.get().contains(
                                                    item.getItemId())) {
                                                ConfigBoxMsg.msg(
                                                        player.getName(),
                                                        _npc.getNameId(),
                                                        item.getLogName());
                                            }
                                        }
                                    }
                                } else {
                                    item.set_showId(_npc.get_showId());
                                    targetInventory = World.get().getInventory(
                                            acquisitor.getX(),
                                            acquisitor.getY(),
                                            acquisitor.getMapId()); // 持てないので足元に落とす
                                }
                                break;
                            }
                        }

                    } else {
                        final List<Integer> dirList = new ArrayList<Integer>();
                        for (int j = 0; j < 8; j++) {
                            dirList.add(j);
                        }
                        int x = 0;
                        int y = 0;
                        int dir = 0;
                        do {
                            if (dirList.size() == 0) {
                                x = 0;
                                y = 0;
                                break;
                            }
                            randomInt = random.nextInt(dirList.size());
                            dir = dirList.get(randomInt);
                            dirList.remove(randomInt);

                            x = HEADING_TABLE_X[dir];
                            y = HEADING_TABLE_Y[dir];
                            Thread.sleep(1);

                        } while (!_npc.getMap().isPassable(_npc.getX(),
                                _npc.getY(), dir, null));
                        item.set_showId(_npc.get_showId());
                        targetInventory = World.get().getInventory(
                                _npc.getX() + x, _npc.getY() + y,
                                _npc.getMapId());
                        ListMapUtil.clear(dirList);
                    }

                    inventory.tradeItem(item, item.getCount(), targetInventory);
                }
                ListMapUtil.clear(list);
                // _npc.turnOnOffLight();

            } catch (final Exception e) {
                // _log.error(e.getLocalizedMessage(), e);

            } finally {
                // 移除此 ArrayList 中的所有元素
                ListMapUtil.clear(_acquisitorList);
                ListMapUtil.clear(_hateList);
            }
        }

        private void set_power(L1PcInstance tgpc, L1ItemInstance item) {
            if (PowerItemSet.START) {
                if (item.get_power_name() != null) {
                    return;
                }
                int r = 5;
                if (tgpc.getInventory().checkEquipped(37001)) {// 野外打怪道具 增设 5%
                                                               // 成功凹槽
                    r = 50;
                } else if (tgpc.getInventory().checkEquipped(37002)) {// 野外打怪道具
                                                                      // 增设
                                                                      // 10%成功凹槽
                    r = 100;
                } else if (tgpc.getInventory().checkEquipped(37003)) {// 野外打怪道具
                                                                      // 增设
                                                                      // 15%成功凹槽
                    r = 150;
                } else if (tgpc.getInventory().checkEquipped(37004)) {// 野外打怪道具
                                                                      // 增设
                                                                      // 20%成功凹槽
                                                                      // (boss取得)
                    r = 200;
                } else if (tgpc.getInventory().checkEquipped(37005)) {// 野外打怪道具
                                                                      // 增设
                                                                      // 100%成功凹槽
                                                                      // (商城)
                    r = 1000;
                }

                if (_random.nextInt(1000) <= r) {
                    // 凹槽诞生
                    switch (item.getItem().getUseType()) {
                        case 1:// 武器
                        case 2:// 盔甲
                        case 18:// T恤
                        case 19:// 斗篷
                        case 20:// 手套
                        case 21:// 靴
                        case 22:// 头盔
                        case 25:// 盾牌
                            final L1ItemPower_name power = new L1ItemPower_name();
                            power.set_item_obj_id(item.getId());
                            power.set_hole_count(1);
                            power.set_hole_1(0);
                            power.set_hole_2(0);
                            power.set_hole_3(0);
                            power.set_hole_4(0);
                            power.set_hole_5(0);
                            item.set_power_name(power);
                            break;
                    }
                }
                if (item.get_power_name() != null) {
                    CharItemPowerReading.get().storeItem(item.getId(),
                            item.get_power_name());
                }
            }
        }
    }
}
