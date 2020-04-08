package com.lineage.data.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.lock.GamblingReading;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.templates.L1Event;
import com.lineage.server.templates.L1Item;
import com.lineage.server.timecontroller.event.GamblingTime;
import com.lineage.server.world.World;

/**
 * 奇岩赌场<BR>
 * 
 * @author dexc
 * 
 */
public class GamblingSet extends EventExecutor {

    private static final Log _log = LogFactory.getLog(GamblingSet.class);

    public static int GAMADENATIME;// 每场比赛间隔时间(分钟)

    public static int ADENAITEM = 40308;// 奇岩赌场 下注使用物品编号(预设金币40308)

    public static int GAMADENA;// 奇岩赌场 下注额(每张赌票售价)

    public static String GAMADENANAME;// 奇岩赌场 下注物品名称

    /**
	 *
	 */
    private GamblingSet() {
        // TODO Auto-generated constructor stub
    }

    public static EventExecutor get() {
        return new GamblingSet();
    }

    @Override
    public void execute(final L1Event event) {
        try {
            final String[] set = event.get_eventother().split(",");

            try {
                GAMADENATIME = Integer.parseInt(set[0]);

            } catch (Exception e) {
                GAMADENATIME = 30;
                _log.error("未设定每场比赛间隔时间(分钟)(使用预设30分钟)");
            }

            try {
                GAMADENA = Integer.parseInt(set[1]);

            } catch (Exception e) {
                GAMADENA = 5000;
                _log.error("未设定奇岩赌场 下注额(每张赌票售价)(使用预设5000)");
            }

            try {
                ADENAITEM = Integer.parseInt(set[2]);
                L1Item item = ItemTable.get().getTemplate(ADENAITEM);
                if (item != null) {
                    GAMADENANAME = item.getNameId();
                }

            } catch (Exception e) {
                ADENAITEM = 40308;
                GAMADENANAME = "$4";
                _log.error("未设定奇岩赌场 下注物品编号(使用预设40308)");
            }

            // 赌场纪录
            GamblingReading.get().load();

            // 赌场计时
            final GamblingTime gamblingTimeController = new GamblingTime();
            gamblingTimeController.start();

            spawnDoor();

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 召唤门
     */
    private void spawnDoor() {
        int[][] gamDoors = new int[][] {
                // 门的编号 / 图形编号 / X座标 / Y座标 / 地图编号 / 门的左端 / 门的右端 / HP / 管理者代号
                new int[] { 51, 1487, 33521, 32861, 4, 1, 33523, 33523, 0, -1 },
                new int[] { 52, 1487, 33519, 32863, 4, 1, 33523, 33523, 0, -1 },
                new int[] { 53, 1487, 33517, 32865, 4, 1, 33523, 33523, 0, -1 },
                new int[] { 54, 1487, 33515, 32867, 4, 1, 33523, 33523, 0, -1 },
                new int[] { 55, 1487, 33513, 32869, 4, 1, 33523, 33523, 0, -1 }, };

        for (int[] doorInfo : gamDoors) {
            final L1DoorInstance door = (L1DoorInstance) NpcTable.get()
                    .newNpcInstance(81158);

            if (door != null) {
                // NPC OBJID
                door.setId(IdFactoryNpc.get().nextId());

                int id = doorInfo[0];
                int gfxid = doorInfo[1];
                int locx = doorInfo[2];
                int locy = doorInfo[3];
                int mapid = doorInfo[4];
                int direction = doorInfo[5];
                int left_edge_location = doorInfo[6];
                int right_edge_location = doorInfo[7];
                int hp = doorInfo[8];
                int keeper = doorInfo[9];

                door.setDoorId(id);
                door.setGfxId(gfxid);
                door.setX(locx);
                door.setY(locy);
                door.setMap((short) mapid);
                door.setHomeX(locx);
                door.setHomeY(locy);
                door.setDirection(direction);
                door.setLeftEdgeLocation(left_edge_location);
                door.setRightEdgeLocation(right_edge_location);
                door.setMaxHp(hp);
                door.setCurrentHp(hp);
                door.setKeeperId(keeper);

                World.get().storeObject(door);
                World.get().addVisibleObject(door);
            }
        }
    }
}
