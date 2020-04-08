package com.lineage.server.model.drop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigRate;
import com.lineage.server.datatables.DropItemTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.MapsTable;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.templates.L1Drop;
import com.lineage.server.templates.L1DropMap;
import com.lineage.server.templates.L1Item;

/**
 * NPC持有物品取回
 * 
 * @author dexc
 * 
 */
public class SetDrop implements SetDropExecutor {

    private static final Log _log = LogFactory.getLog(SetDrop.class);

    private static Map<Integer, ArrayList<L1Drop>> _droplist;

    private static Map<Integer, HashMap<Integer, ArrayList<L1DropMap>>> _droplistX;

    private static final Random _random = new Random();

    /**
     * 设置掉落资料
     * 
     * @param droplists
     */
    @Override
    public void addDropMap(final Map<Integer, ArrayList<L1Drop>> droplists) {
        if (_droplist != null) {
            _droplist.clear();
        }
        _droplist = droplists;
    }

    /**
     * 设置指定MAP掉落资料
     * 
     * @param droplists
     */
    @Override
    public void addDropMapX(
            Map<Integer, HashMap<Integer, ArrayList<L1DropMap>>> droplists) {
        if (_droplistX != null) {
            _droplistX.clear();
        }
        _droplistX = droplists;
    }

    /**
     * NPC持有物品资料取回
     * 
     * @param npc
     * @param inventory
     */
    @Override
    public void setDrop(final L1NpcInstance npc, final L1Inventory inventory) {
        setDrop(npc, inventory, 0.0);
    }

    /**
     * NPC持有物品资料取回
     * 
     * @param npc
     * @param inventory
     * @param random
     */
    @Override
    public void setDrop(final L1NpcInstance npc, final L1Inventory inventory,
            final double random) {
        // NPC掉落资料取回
        final int mobId = npc.getNpcTemplate().get_npcId();
        // NPC位置
        final int mapid = npc.getMapId();
        HashMap<Integer, ArrayList<L1DropMap>> droplistX = _droplistX
                .get(mapid);
        if (droplistX != null) {
            final ArrayList<L1DropMap> list = droplistX.get(mobId);
            if (list != null) {
                setDrop(npc, inventory, list);
            }
        }

        final ArrayList<L1Drop> dropList = _droplist.get(mobId);
        if (dropList == null) {
            return;
        }

        // 取回增加倍率
        double droprate = ConfigRate.RATE_DROP_ITEMS;
        if (droprate <= 0) {
            droprate = 0;
        }
        droprate += random;

        double adenarate = ConfigRate.RATE_DROP_ADENA;
        if (adenarate <= 0) {
            adenarate = 0;
        }

        if ((droprate <= 0) && (adenarate <= 0)) {
            return;
        }

        for (final L1Drop drop : dropList) {
            // 掉落物品编号
            final int itemId = drop.getItemid();
            // 物品为金币掉落数量为0
            if ((adenarate == 0) && (itemId == L1ItemId.ADENA)) {
                continue;
            }

            // 取回随机机率
            final int randomChance = _random.nextInt(0xf4240) + 1;
            // 地图增加掉率
            final double rateOfMapId = MapsTable.get().getDropRate(
                    npc.getMapId());
            // 指定物品增加掉率
            final double rateOfItem = DropItemTable.get().getDropRate(itemId);

            if ((droprate == 0)
                    || (drop.getChance() * droprate * rateOfMapId * rateOfItem < randomChance)) {
                continue;
            }

            // ドロップ个数を设定
            final double amount = DropItemTable.get().getDropAmount(itemId);
            final long min = (long) (drop.getMin() * amount);
            final long max = (long) (drop.getMax() * amount);

            long itemCount = min;
            final long addCount = max - min + 1;
            if (addCount > 1) {
                itemCount += _random.nextInt((int) addCount);
            }
            // 物件为金币 加入倍率
            if (itemId == L1ItemId.ADENA) {
                itemCount *= adenarate;
            }
            // 数量为0
            if (itemCount < 0) {
                itemCount = 0;
            }
            // 限制持有数量
            if (itemCount > 2000000000) {
                itemCount = 2000000000;
            }
            if (itemCount > 0) {
                additem(inventory, itemId, itemCount);

            } else {
                _log.error("NPC加入背包物件数量为0(" + mobId + " itemId: " + itemId
                        + ")");
            }
        }
    }

    /**
     * 指定地图NPC持有物品资料取回
     * 
     * @param npc
     * @param inventory
     * @param dropList
     */
    private void setDrop(final L1NpcInstance npc, final L1Inventory inventory,
            final ArrayList<L1DropMap> dropList) {
        // 取回增加倍率
        double droprate = ConfigRate.RATE_DROP_ITEMS;
        if (droprate <= 0) {
            droprate = 0;
        }

        double adenarate = ConfigRate.RATE_DROP_ADENA;
        if (adenarate <= 0) {
            adenarate = 0;
        }

        if ((droprate <= 0) && (adenarate <= 0)) {
            return;
        }

        for (final L1DropMap drop : dropList) {
            // 掉落物品编号
            final int itemId = drop.getItemid();
            // 物品为金币掉落数量为0
            if ((adenarate == 0) && (itemId == L1ItemId.ADENA)) {
                continue;
            }

            // 取回随机机率
            final int randomChance = _random.nextInt(0xf4240) + 1;
            final double rateOfMapId = MapsTable.get().getDropRate(
                    npc.getMapId());
            final double rateOfItem = DropItemTable.get().getDropRate(itemId);

            boolean noadd = ((drop.getChance() * droprate * rateOfMapId * rateOfItem) < randomChance);
            if ((droprate == 0) || noadd) {
                continue;
            }
            // 指定的物件提高掉落数量
            final double amount = DropItemTable.get().getDropAmount(itemId);
            final long min = (long) (drop.getMin() * amount);
            final long max = (long) (drop.getMax() * amount);

            long itemCount = min;
            final long addCount = max - min + 1;
            if (addCount > 1) {
                itemCount += _random.nextInt((int) addCount);
            }
            // 物件为金币 加入倍率
            if (itemId == L1ItemId.ADENA) {
                itemCount *= adenarate;
            }
            // 数量为0
            if (itemCount < 0) {
                itemCount = 0;
            }
            // 限制持有数量
            if (itemCount > 2000000000) {
                itemCount = 2000000000;
            }
            if (itemCount > 0) {
                // System.out.println("add:"+npc.getName() + " droprate:" +
                // droprate +" itemId:"+itemId + " itemCount:"+itemCount);
                additem(inventory, itemId, itemCount);

            } else {
                _log.error("NPC加入背包物件数量为0(" + npc.getNpcId() + " itemId: "
                        + itemId + ") 指定地图");
            }
        }
    }

    /**
     * 对指定背包加入物件
     * 
     * @param inventory
     * @param itemId
     * @param itemCount
     */
    private void additem(L1Inventory inventory, int itemId, long itemCount) {
        try {
            final L1Item tmp = ItemTable.get().getTemplate(itemId);
            if (tmp == null) {
                _log.error("掉落物品设置错误(无这编号物品): " + itemId);
                return;
            }
            if (tmp.isStackable()) {// 可以堆叠
                // 生除物品
                final L1ItemInstance item = ItemTable.get().createItem(itemId);
                if (item != null) {
                    item.setCount(itemCount);
                    // 加入背包
                    inventory.storeItem(item);
                }

            } else {
                for (int i = 0; i < itemCount; i++) {
                    // 生除物品
                    final L1ItemInstance item = ItemTable.get().createItem(
                            itemId);
                    if (item != null) {
                        item.setCount(1);
                        // 加入背包
                        inventory.storeItem(item);
                    }
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
