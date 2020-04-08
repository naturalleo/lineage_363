package com.lineage.data.item_etcitem.teleport;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.ItemTeleportTable;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.timecontroller.server.ServerUseMapTimer;

/**
 * <font color=#00800>自定义传送卷轴</font><BR>
 * 
 * @author dexc
 * 
 */
public class SOR_UserSet extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(SOR_UserSet.class);

    /**
	 *
	 */
    private SOR_UserSet() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new SOR_UserSet();
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
    	//添加限制时间段使用的卷轴  id 42101 hjx1000
    	final int itemid = item.getItemId();
    	if (itemid == 42101) {
    		final Calendar now = new GregorianCalendar();
    		final int h = now.get(Calendar.HOUR_OF_DAY);
    		final int w = now.get(Calendar.DAY_OF_WEEK);
    		if (h < 20 || h > 22 || (w != 1 && w != 4)) {
    			pc.sendPackets(new S_ServerMessage("\\aD只有周三和周日晚上20:00-21:59可以进入"));
    			return;
    		}
    		final long time1 = now.getTimeInMillis();
    		final int YY = now.get(Calendar.YEAR);
    		final int MM = now.get(Calendar.MONTH);
    		final int DD = now.get(Calendar.DAY_OF_MONTH);
    		final int HH = 22;
    		final int mm = 0;
    		now.set(YY, MM, DD, HH, mm);
    		final long time2 = now.getTimeInMillis();
    		final int time3 = (int) ((time2 - time1) / 1000);
    		if (time3 >= 1800) {
                pc.get_other().set_usemap(5167);
                ServerUseMapTimer.put(pc, 1800);
                pc.sendPackets(new S_ServerMessage("使用时间限制:" + 1800 + "秒"));
    		} else {
                pc.get_other().set_usemap(5167);
                ServerUseMapTimer.put(pc, time3);
                pc.sendPackets(new S_ServerMessage("使用时间限制:" + time3 + "秒"));
    		}
    	}
    	if (itemid == 42102) {
    		Calendar now = new GregorianCalendar();
    		final int h = now.get(Calendar.HOUR_OF_DAY);
    		if (h != 22) {
    			pc.sendPackets(new S_ServerMessage("只有晚上10点可以使用"));
    			return;
    		}
    	}
    	//添加限制时间段使用的卷轴  id 42101 hjx1000
        final int[] loc = ItemTeleportTable.get().getLoc(item.getItemId());

        if (loc != null) {
            final int locX = loc[0];
            final int locY = loc[1];
            final short mapId = (short) loc[2];

            if (pc.getMap().isEscapable()) {
                // 删除道具
                pc.getInventory().removeItem(item, 1);

                // 解除魔法技能绝对屏障
                L1BuffUtil.cancelAbsoluteBarrier(pc);

                final TeleportRunnable runnable = new TeleportRunnable(pc,
                        locX, locY, mapId);
                pc.setHomeX(locX);
                pc.setHomeY(locY);
                GeneralThreadPool.get().schedule(runnable, 0);

                // 该地图不允许使用回卷
            } else {
                // 276 \f1在此无法使用传送。
                pc.sendPackets(new S_ServerMessage(276));
                // 解除传送锁定
                pc.sendPackets(new S_Paralysis(
                        S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
            }
            final int time = loc[3];
            if (time > 0) {
                pc.get_other().set_usemap(mapId);
                ServerUseMapTimer.put(pc, time);
                pc.sendPackets(new S_ServerMessage("使用时间限制:" + time + "秒"));
            }
        }
    }

    private class TeleportRunnable implements Runnable {

        private final L1PcInstance _pc;
        private int _locX = 0;
        private int _locY = 0;
        private int _mapid = 0;

        public TeleportRunnable(final L1PcInstance pc, final int x,
                final int y, final int mapid) {
            _pc = pc;
            _locX = x;
            _locY = y;
            _mapid = mapid;
        }

        @Override
        public void run() {
            try {
                final L1Map map = L1WorldMap.get().getMap((short) _mapid);
                int r = 10;
                int tryCount = 0;
                int newX = _locX;
                int newY = _locY;
                do {
                    tryCount++;
                    newX = _locX + (int) (Math.random() * r)
                            - (int) (Math.random() * r);
                    newY = _locY + (int) (Math.random() * r)
                            - (int) (Math.random() * r);
                    if (map.isPassable(newX, newY, _pc)) {
                        break;
                    }
                    Thread.sleep(1);
                } while (tryCount < 5);

                if (tryCount >= 5) {
                    L1Teleport.teleport(_pc, _locX, _locY, (short) _mapid,
                            _pc.getHeading(), true);

                } else {
                    L1Teleport.teleport(_pc, newX, newY, (short) _mapid,
                            _pc.getHeading(), true);
                }

            } catch (InterruptedException e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
