package com.lineage.data.item_etcitem.wand;

import java.util.ArrayList;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.ActionCodes;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.L1TownLocation;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.world.World;

/**
 * <font color=#00800>强化驱逐魔杖</font><BR>
 * Wand of Banishment<BR>
 * <font color=#00800>驱逐魔杖</font><BR>
 * Wand of Blink<BR>
 * 
 * @author dexc
 * 
 */
public class Wand_Blink extends ItemExecutor {

    /**
	 *
	 */
    private Wand_Blink() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Wand_Blink();
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
        // 对象OBJID
        final int targObjId = data[0];

        // 解除魔法技能绝对屏障
        L1BuffUtil.cancelAbsoluteBarrier(pc);

        if (pc.isInvisble()) {// 隐身状态
            pc.delInvis(); // 解除隐身状态
        }

        // 送出封包
        pc.sendPacketsX8(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Wand));

        // 可用次数
        final int chargeCount = item.getChargeCount();

        if (chargeCount <= 0) {
            // 79 没有任何事情发生
            pc.sendPackets(new S_ServerMessage(79));

        } else {
            final L1Object target = World.get().findObject(targObjId);
            if (target != null) {

                final int rnd = (int) (Math.random() * 100);// 随机数字范围0~99

                if ((target != null) && (rnd >= 50)) {// 目标不是空的 并且 1/2成功机率
                    this.wandAction(pc, target);

                } else {
                    // 280 \f1施咒失败。
                    pc.sendPackets(new S_ServerMessage(280));
                }
                // 建立新次数(可用次数-1)
                item.setChargeCount(item.getChargeCount() - 1);
                // 更新可用次数
                pc.getInventory().updateItem(item,
                        L1PcInventory.COL_CHARGE_COUNT);

            } else {
                // 79 没有任何事情发生
                pc.sendPackets(new S_ServerMessage(79));
            }
        }
    }

    /**
     * 魔杖的使用对象处理 / 驱逐魔杖的行为判断
     * 
     * @param pc
     *            使用物品的人物
     * @param ta
     *            目标对象
     * @return
     */
    private void wandAction(final L1PcInstance pc, final L1Object ta) {

        int target_x = 0;// 与目标的X距离
        int target_y = 0;// 与目标的Y距离
        int newX = 0;
        int newY = 0;
        final short xy = (short) ((Math.random() * 3) + 1);// 随机数字范围1~3(随机取得推出距离)

        // 目标是玩家的状态
        if (ta instanceof L1PcInstance) {
            final L1PcInstance targetPc = (L1PcInstance) ta;

            target_x = pc.getX() - targetPc.getX();// 计算X距离
            target_y = pc.getY() - targetPc.getY();// 计算Y距离

            // 取消对自己攻击
            if (pc.getId() == targetPc.getId()) {
                // 280 \f1施咒失败。
                pc.sendPackets(new S_ServerMessage(280));

                // 目标距离过远的状态 或 施展对象是GM
            } else if (((target_x > 4) || (target_x < -4) || (target_y > 4) || (target_y < -4))
                    || targetPc.isGm()) {
                // 280 \f1施咒失败。
                pc.sendPackets(new S_ServerMessage(280));

            } else {
                switch (target_x) {// 计算X推出距离
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        newX = targetPc.getX() - xy;
                        break;

                    case -1:
                    case -2:
                    case -3:
                    case -4:
                        newX = targetPc.getX() + xy;
                        break;

                    default:
                        newX = targetPc.getX();
                        break;
                }

                switch (target_y) {// 计算Y推出距离
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        newY = targetPc.getY() - xy;
                        break;

                    case -1:
                    case -2:
                    case -3:
                    case -4:
                        newY = targetPc.getY() + xy;
                        break;

                    default:
                        newY = targetPc.getY();
                        break;
                }

                final L1Map map = L1WorldMap.get().getMap(targetPc.getMapId());
                final short mapId = targetPc.getMapId();
                final int head = targetPc.getHeading();
                if (L1TownLocation.isGambling(newX, newY, mapId)) {
                    // 280 \f1施咒失败。
                    pc.sendPackets(new S_ServerMessage(280));
                    return;
                }
                // 判断位置是否可通行 可通行继续传送 不可通行留置原地并扣除道具
                if (map.isInMap(newX, newY) && map.isPassable(newX, newY, null)) {
                    // 执行移动
                    L1Teleport
                            .teleport(targetPc, newX, newY, mapId, head, true);

                } else {
                    // 280 \f1施咒失败。
                    pc.sendPackets(new S_ServerMessage(280));
                }
            }

            // 目标是怪物的状态
        } else if (ta instanceof L1MonsterInstance) {
            final L1MonsterInstance targetNpc = (L1MonsterInstance) ta;

            target_x = pc.getX() - targetNpc.getX();// 计算X距离
            target_y = pc.getY() - targetNpc.getY();// 计算Y距离

            // 目标距离过远的状态 或 怪物等级 >= 40
            if (((target_x > 4) || (target_x < -4) || (target_y > 4) || (target_y < -4))
                    || (targetNpc.getLevel() >= 40)) {
                // 280 \f1施咒失败。
                pc.sendPackets(new S_ServerMessage(280));

            } else {
                switch (target_x) {// 计算X推出距离
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        newX = targetNpc.getX() - xy;
                        break;

                    case -1:
                    case -2:
                    case -3:
                    case -4:
                        newX = targetNpc.getX() + xy;
                        break;

                    default:
                        newX = targetNpc.getX();
                        break;
                }
                switch (target_y) {// 计算Y推出距离
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        newY = targetNpc.getY() - xy;
                        break;

                    case -1:
                    case -2:
                    case -3:
                    case -4:
                        newY = targetNpc.getY() + xy;
                        break;

                    default:
                        newY = targetNpc.getY();
                        break;
                }

                final L1Map map = L1WorldMap.get().getMap(targetNpc.getMapId());
                final short mapId = targetNpc.getMapId();
                final int head = targetNpc.getHeading();

                // 判断位置是否可通行 可通行继续传送 不可通行留置原地并扣除道具
                if (map.isInMap(newX, newY)
                        && map.isPassable(newX, newY, targetNpc)) {
                    this.teleport(targetNpc, newX, newY, mapId, head);

                } else {
                    // 280 \f1施咒失败。
                    pc.sendPackets(new S_ServerMessage(280));
                }
            }

        } else {// 目标非 玩家 怪物 取消效果(避免玩家任意移动NPC)
            // 280 \f1施咒失败。
            pc.sendPackets(new S_ServerMessage(280));
        }
    }

    /**
     * 怪物传送的处理
     * 
     * @param targetNpc
     *            被传送的NPC
     * @param x
     *            新的X座标点
     * @param y
     *            新的Y座标点
     * @param map
     *            新的MAP编号
     * @param head
     *            新的面向点
     * @return
     */
    private void teleport(final L1MonsterInstance targetNpc, final int x,
            final int y, final short map, final int head) {
        World.get().moveVisibleObject(targetNpc, map);
        // 解除原座标障碍宣告
        targetNpc.getMap().setPassable(targetNpc.getX(), targetNpc.getY(),
                true, 0x02);

        targetNpc.setX(x);// 设定新的X座标点
        targetNpc.setY(y);// 设定新的Y座标点
        targetNpc.setMap(map);// 设定新的MAP编号
        targetNpc.setHeading(head);// 设定新的面向点

        // 建立新障碍宣告
        targetNpc.getMap().setPassable(x, y, false, 0x02);

        // 更新周边玩家视野
        final ArrayList<L1PcInstance> tgPcs = World.get().getVisiblePlayer(
                targetNpc, 15);

        for (final L1PcInstance tgPc : tgPcs) {
            if (tgPc != null) {
                tgPc.sendPackets(new S_SkillSound(targetNpc.getId(), 169));
                // 送出封包
                tgPc.sendPackets(new S_RemoveObject(targetNpc));
                tgPc.removeKnownObject(targetNpc);
                tgPc.updateObject();
            }
        }
    }
}
