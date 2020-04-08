package com.lineage.data.npc.shop;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.event.VIPSet;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.VIPReading;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Item;

/**
 * VIP管理员<BR>
 * 91128
 * 
 * @author dexc
 * 
 */
public class Npc_VIP extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_VIP.class);

    /**
	 *
	 */
    private Npc_VIP() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_VIP();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        final Timestamp time = VIPReading.get().getOther(pc);

        if (time != null) {
            final SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            final String key = sdf.format(time);
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_v2",
                    new String[] { key }));

        } else {
            L1Item item = ItemTable.get().getTemplate(VIPSet.ITEMID);
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_v1",
                    new String[] { String.valueOf(VIPSet.ADENA),
                            item.getNameId(), String.valueOf(VIPSet.DATETIME) }));
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        int mapid = -1;
        int x = -1;
        int y = -1;

        if (cmd.equalsIgnoreCase("1")) {// 买1个月VIP(需要 1千个YiWei币)
            final Timestamp time = VIPReading.get().getOther(pc);

            if (time != null) {
                final SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                final String key = sdf.format(time);
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_v2",
                        new String[] { key }));
                return;
            }

            // 取回天宝数量
            final L1ItemInstance itemT = pc.getInventory().checkItemX(
                    VIPSet.ITEMID, VIPSet.ADENA);
            if (itemT == null) {
                // 337：\f1%0不足%s。 0_o"
                pc.sendPackets(new S_ServerMessage(337, "天宝"));

            } else {
                pc.getInventory().removeItem(itemT, VIPSet.ADENA);// 移除天宝

                long timeNow = System.currentTimeMillis();// 目前时间豪秒

                long x1 = VIPSet.DATETIME * 24 * 60 * 60;// 30天耗用秒数
                long x2 = x1 * 1000;// 转为豪秒
                long upTime = x2 + timeNow;// 目前时间 加上 30天

                // 到期时间
                final Timestamp value = new Timestamp(upTime);

                VIPReading.get().storeOther(pc.getId(), value);

                final SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                final String key = sdf.format(value);
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_v2",
                        new String[] { key }));
                return;
            }

        } else if (cmd.equalsIgnoreCase("2")) {// 我要进入 6转以上练功地图
            mapid = 1002;
            x = 32756;
            y = 32679;

        } else if (cmd.equalsIgnoreCase("3")) {// 我要进入 6转以上打宝地图
            mapid = 200;
            x = 32756;
            y = 32942;

        } else if (cmd.equalsIgnoreCase("4")) {// 我要进入 3到5转练功地图
            mapid = 10022;
            x = 32756;
            y = 32679;

        } else if (cmd.equalsIgnoreCase("5")) {// 我要进入 3到5转打宝地图
            mapid = 6661;
            x = 32800;
            y = 32751;

        } else if (cmd.equalsIgnoreCase("6")) {// 我要进入 0到2转练功地图
            mapid = 10021;
            x = 32756;
            y = 32679;

        } else if (cmd.equalsIgnoreCase("7")) {// 我要进入 0到2转打宝地图
            mapid = 7781;
            x = 32739;
            y = 32686;
        }

        if (mapid != -1) {
            // System.out.println("mapid:"+mapid);
            final Timestamp time = VIPReading.get().getOther(pc);
            if (time != null) {
                // System.out.println("time:"+time.toString());
                // 目前时间
                final Timestamp ts = new Timestamp(System.currentTimeMillis());
                // System.out.println("ts:"+ts.toString());
                if (time.after(ts)) {
                    teleport(pc, x, y, mapid);

                } else {
                    // 移出清单
                    VIPReading.get().delOther(pc.getId());
                }
            }
        } else {
            // 3052:你的转生次数(%0)已经超过这个地图的限制。
            pc.sendPackets(new S_ServerMessage("等级(" + pc.getLevel()
                    + ")已经超过限制"));
        }

        // 关闭对话窗
        pc.sendPackets(new S_CloseList(pc.getId()));
    }

    /**
     * 传送
     * 
     * @param pc
     * @param x
     * @param y
     * @param mapid
     */
    private void teleport(final L1PcInstance pc, final int x, final int y,
            final int mapid) {
        try {
            L1Location location = new L1Location(x, y, mapid);
            final L1Location newLocation = location.randomLocation(200, false);

            final int newX = newLocation.getX();
            final int newY = newLocation.getY();
            final short mapId = (short) newLocation.getMapId();

            L1Teleport.teleport(pc, newX, newY, mapId, 5, true);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
