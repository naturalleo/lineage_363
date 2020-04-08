package com.lineage.data.item_etcitem.teleport;

import static com.lineage.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;

import java.util.ArrayList;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.lock.CharBookReading;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1BookMark;

/**
 * 魔法卷轴(指定传送)40863<br>
 * 瞬间移动卷轴 40100 瞬间移动卷轴（祝福）140100
 */
public class Move_Reel extends ItemExecutor {

    /**
	 *
	 */
    private Move_Reel() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Move_Reel();
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
        // 所在地图编号
        Short mapID = (short) data[0];
        int mapX = data[1];
        int mapY = data[2];
        // 所在位置 是否允许传送
        final boolean isTeleport = pc.getMap().isTeleportable();
        if (!isTeleport) {
            // 647 这附近的能量影响到瞬间移动。在此地无法使用瞬间移动。
            pc.sendPackets(new S_ServerMessage(647));
            pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK,
                    false));
        } else {
            boolean flag = false; // 传送模式
            final ArrayList<L1BookMark> bookList = CharBookReading.get()
                    .getBookMarks(pc);
            // 检查是否有此坐标
            if (bookList != null) {
                for (final L1BookMark book : bookList) {
                    if (book.getMapId() == mapID && book.getLocX() == mapX
                            && book.getLocY() == mapY) {
                        flag = true;
                    }
                }
//            } else {
//                pc.getInventory().removeItem(item, 1);
//                L1Teleport.randomTeleport(pc, true);
            }
            if (flag) {
                pc.getInventory().removeItem(item, 1);
                L1Teleport.teleport(pc, mapX, mapY, mapID, 5, true);
            } else { // 随机传送
                pc.getInventory().removeItem(item, 1);
                L1Teleport.randomTeleport(pc, true);
            }
            // 绝对屏障解除
            if (pc.hasSkillEffect(ABSOLUTE_BARRIER)) {
                pc.killSkillEffectTimer(ABSOLUTE_BARRIER);
                pc.startHpRegeneration();
                pc.startMpRegeneration();
            }
            // pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK,
            // false));
        }
    }
}
