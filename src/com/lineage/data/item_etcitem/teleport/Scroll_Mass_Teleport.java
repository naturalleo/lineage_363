package com.lineage.data.item_etcitem.teleport;

import java.util.ArrayList;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.lock.CharBookReading;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1BookMark;
import com.lineage.server.world.World;

/**
 * <font color=#00800>全体传送术的卷轴</font><BR>
 * Scroll of Mass Teleport
 * 
 * @author dexc
 * 
 */
public class Scroll_Mass_Teleport extends ItemExecutor {

    /**
	 *
	 */
    private Scroll_Mass_Teleport() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Scroll_Mass_Teleport();
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
        // 接收到的地图编号
        final Short mapID = (short) data[0];
        // 接收到的坐标点
        int mapX = data[1]; 
        int mapY = data[2];
        // 所在位置 是否允许传送
        final boolean isTeleport = pc.getMap().isTeleportable();//修正集体传送卷可以在非传送地图上传送 hjx1000
        if (!isTeleport) {
            // 647 这附近的能量影响到瞬间移动。在此地无法使用瞬间移动。
            pc.sendPackets(new S_ServerMessage(647));
            // 解除传送锁定
            pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK,
                    false));

        } else {
        	boolean flag = false; // 传送模式
            final ArrayList<L1BookMark> bookm = CharBookReading.get()
                    .getBookMarks(pc);
            // 取出记忆座标
            if (bookm != null) {
                for (final L1BookMark book : bookm) {
                    if (book.getMapId() == mapID && book.getLocX() == mapX
                            && book.getLocY() == mapY) {
                    	flag = true;
                    } 
                } 
            } else {
                // 删除道具
                pc.getInventory().removeItem(item, 1);

                // 取得座标值
                final L1Location newLocation = pc.getLocation().randomLocation(
                        200, true);

                final int newX = newLocation.getX();
                final int newY = newLocation.getY();
                final short newMapId = (short) newLocation.getMapId();

                // 取得3格范围内物件
                final ArrayList<L1Object> objList = World.get()
                        .getVisiblePoint(pc.getLocation(), 3, pc.get_showId());

                for (final L1Object tgObj : objList) {
                    if (tgObj instanceof L1PcInstance) {
                        final L1PcInstance tgPc = (L1PcInstance) tgObj;
                        if (tgPc.isDead()) {
                            continue;
                        }
                        if (tgPc.getClanid() == 0) {
                            continue;
                        }
                        // 血盟成员传送
                        if (tgPc.getClanid() == pc.getClanid()) {
                            // 商店村模式
                            if (!tgPc.isPrivateShop()) {
                                // 解除魔法技能绝对屏障
                                L1BuffUtil.cancelAbsoluteBarrier(tgPc);
                                tgPc.setTeleportX(newX);
                                tgPc.setTeleportY(newY);
                                tgPc.setTeleportMapId(newMapId);
                                // 你的血盟成员想要传送你。你答应吗？(Y/N)
                                pc.sendPackets(new S_Message_YN(748));
                            }
                        }
                    }
                }
                // 自身的传送
                L1Teleport.teleport(pc, newX, newY, newMapId, 5, true);
            }

            if (flag) {
                // 删除道具
                pc.getInventory().removeItem(item, 1);
                // 取得3格范围内物件相同副本ID物件
                final ArrayList<L1Object> objList = World.get()
                        .getVisiblePoint(pc.getLocation(), 3, pc.get_showId());

                for (final L1Object tgObj : objList) {
                    if (tgObj instanceof L1PcInstance) {
                        final L1PcInstance tgPc = (L1PcInstance) tgObj;
                        if (tgPc.isDead()) {
                            continue;
                        }
                        if (tgPc.getClanid() == 0) {
                            continue;
                        }
                        // 血盟成员传送
                        if (tgPc.getClanid() == pc.getClanid()) {
                            // 商店村模式
                            if (!tgPc.isPrivateShop()) {

                                // 解除魔法技能绝对屏障
                                L1BuffUtil.cancelAbsoluteBarrier(tgPc);
                                tgPc.setTeleportX(mapX);
                                tgPc.setTeleportY(mapY);
                                tgPc.setTeleportMapId(mapID);
                                // 你的血盟成员想要传送你。你答应吗？(Y/N)
                                tgPc.sendPackets(new S_Message_YN(748));
                            }
                        }
                    }
                }
                // 自身的传送
                L1Teleport.teleport(pc, mapX, mapY,
                		mapID, 5, true);
            } else {
                // 删除道具
                pc.getInventory().removeItem(item, 1);

                // 取得座标值
                final L1Location newLocation = pc.getLocation().randomLocation(
                        200, true);

                final int newX = newLocation.getX();
                final int newY = newLocation.getY();
                final short newMapId = (short) newLocation.getMapId();

                // 取得3格范围内物件
                final ArrayList<L1Object> objList = World.get()
                        .getVisiblePoint(pc.getLocation(), 3, pc.get_showId());

                for (final L1Object tgObj : objList) {
                    if (tgObj instanceof L1PcInstance) {
                        final L1PcInstance tgPc = (L1PcInstance) tgObj;
                        if (tgPc.isDead()) {
                            continue;
                        }
                        if (tgPc.getClanid() == 0) {
                            continue;
                        }
                        // 血盟成员传送
                        if (tgPc.getClanid() == pc.getClanid()) {
                            // 商店村模式
                            if (!tgPc.isPrivateShop()) {
                                // 解除魔法技能绝对屏障
                                L1BuffUtil.cancelAbsoluteBarrier(tgPc);
                                tgPc.setTeleportX(newX);
                                tgPc.setTeleportY(newY);
                                tgPc.setTeleportMapId(newMapId);
                                // 你的血盟成员想要传送你。你答应吗？(Y/N)
                                pc.sendPackets(new S_Message_YN(748));
                            }
                        }
                    }
                }
                // 自身的传送
                L1Teleport.teleport(pc, newX, newY, newMapId, 5, true);
            }
            // 解除魔法技能绝对屏障
            L1BuffUtil.cancelAbsoluteBarrier(pc);
        }
    }
}
