package com.lineage.data.item_etcitem.doll;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigOther;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.DollPowerTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.ItemTimeTable;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.lock.CharItemsTimeReading;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.L1War;
import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ItemName;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Doll;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.WorldWar;

/**
 * 魔法娃娃
 */
public class Magic_Doll extends ItemExecutor {
//	private static boolean isSure = false;//修改魔法娃娃为一次性道具 hjx1000
    /**
	 *
	 */
    private Magic_Doll() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Magic_Doll();
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
        final int itemId = item.getItemId();
        final int itemobj = item.getId();
        this.useMagicDoll(pc, itemId, itemobj);
//        if (isSure) {
//        	pc.getInventory().removeItem(item, 1); //修改魔法娃娃为一次性道具 hjx1000
//        	isSure = false;
//        }
        set_time_item(item, pc);
    }

    private void useMagicDoll(final L1PcInstance pc, final int itemId,
            final int itemObjectId) {
        if (pc.getDoll(itemObjectId) != null) {
            // 娃娃收回
            pc.getDoll(itemObjectId).deleteDoll();
            return;
        }

        if (pc.getDolls().size() >= ConfigAlt.MAX_DOLL_COUNT) {
            pc.sendPackets(new S_ServerMessage(319));
            return;
        }

        if (!pc.getDolls().isEmpty()) {
            for (final Iterator<L1DollInstance> iter = pc.getDolls().values()
                    .iterator(); iter.hasNext();) {
                final L1DollInstance doll = iter.next();
                // for (L1DollInstance doll : pc.getDolls().values()) {
                if (pc.getInventory().getItem(doll.getItemObjId()).getItemId() == itemId) {
                    pc.sendPackets(new S_ServerMessage("\\fY不能携带相同的娃娃"));
                    return;
                }
            }
        }

        if (!ConfigOther.WAR_DOLL) {
            // 战争
            if (pc.getClan() != null) {
                boolean inWar = false;
                if (pc.getClan().getCastleId() != 0) {
                    if (ServerWarExecutor.get().isNowWar(
                            pc.getClan().getCastleId())) { // 战争时间内
                        inWar = true;
                    }

                } else {
                    final List<L1War> warList = WorldWar.get().getWarList(); // 全部战争清单
                    for (final Iterator<L1War> iter = warList.iterator(); iter
                            .hasNext();) {
                        final L1War war = iter.next();
                        if (war.checkClanInWar(pc.getClan().getClanName())) { // 战争中
                            inWar = true;
                            break;
                        }
                    }
                }

                if (inWar) {
                    // 1531：加入血盟中或战斗中时，无法进行召唤魔法娃娃。
                    pc.sendPackets(new S_ServerMessage(1531));
                    return;
                }
            }
        }

        boolean iserror = false;
        final L1Doll type = DollPowerTable.get().get_type(itemId);
        if (type != null) {
            if (type.get_need() != null) {
                final int[] itemids = type.get_need();
                final int[] counts = type.get_counts();

                for (int i = 0; i < itemids.length; i++) {
                    if (!pc.getInventory().checkItem(itemids[i], counts[i])) {
                        final L1Item temp = ItemTable.get().getTemplate(
                                itemids[i]);
                        pc.sendPackets(new S_ServerMessage(337, temp
                                .getNameId()));
                        iserror = true;
                    }
                }

                if (!iserror) {
                    for (int i = 0; i < itemids.length; i++) {
                        pc.getInventory().consumeItem(itemids[i], counts[i]);
                    }
                }
            }

            if (!iserror) {
                final L1Npc template = NpcTable.get().getTemplate(71082);
                L1DollInstance doll = new L1DollInstance(template, pc,
                        itemObjectId, type);
//                if (itemId < 58011) { //ID大于58011 为永久性魔法娃娃 hjx1000
//                    isSure = true;//修改魔法娃娃为一次性道具 hjx1000
//                }
            }
        }
    }
    
    /**
     * 给予时间限制物品
     * 
     * @param item
     */
    private void set_time_item(final L1ItemInstance item, final L1PcInstance pc) {
        if (item.get_time() == null) {
            int date = -1;
            if (ItemTimeTable.TIME.get(item.getItemId()) != null) {
                date = ItemTimeTable.TIME.get(item.getItemId()).intValue();
            }

            if (date != -1) {
                long time = System.currentTimeMillis();// 目前时间豪秒
                long x1 = date * 60 * 60;// 指定小时耗用秒数
                long x2 = x1 * 1000;// 转为豪秒
                long upTime = x2 + time;// 目前时间 加上指定天数耗用秒数

                // 时间数据
                final Timestamp ts = new Timestamp(upTime);
                item.set_time(ts);

                // 人物背包物品使用期限资料
                CharItemsTimeReading.get().addTime(item.getId(), ts);
                //pc.sendPackets(new S_ItemName(item));
                pc.getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
            }
        }
    }
}
