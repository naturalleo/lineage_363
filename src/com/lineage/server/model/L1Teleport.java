package com.lineage.server.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.utils.Teleportation;

/**
 * 传送控制项
 * 
 * @author dexc
 * 
 */
public class L1Teleport {

    private static final Log _log = LogFactory.getLog(L1Teleport.class);

    // テレポートスキルの种类
    public static final int TELEPORT = 0;

    public static final int CHANGE_POSITION = 1;

    public static final int ADVANCED_MASS_TELEPORT = 2;

    public static final int CALL_CLAN = 3;

    // 顺番にteleport(白), change position e(青), ad mass teleport e(赤), call clan(绿)
    public static final int[] EFFECT_SPR = { 169, 2235, 2236, 2281 };

    public static final int[] EFFECT_TIME = { 280, 440, 440, 1120 };

    private L1Teleport() {
    }

    /**
     * 传送控制项
     * 
     * @param pc
     *            执行的人物
     * @param loc
     *            座标
     * @param head
     *            面向
     * @param effectable
     *            是否产生动画
     */
    public static void teleport(final L1PcInstance pc, final L1Location loc,
            final int head, final boolean effectable) {
        teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), head,
                effectable, TELEPORT);
    }

    /**
     * 传送控制项
     * 
     * @param pc
     *            执行的人物
     * @param loc
     *            座标
     * @param head
     *            面向
     * @param effectable
     *            是否产生动画
     * @param skillType
     *            动画的光 (0:白),(1:青),(2:赤),(3:绿)
     */
    public static void teleport(final L1PcInstance pc, final L1Location loc,
            final int head, final boolean effectable, final int skillType) {
        teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), head,
                effectable, skillType);
    }

    /**
     * 传送控制项
     * 
     * @param pc
     *            执行的人物
     * @param x
     *            X座标
     * @param y
     *            Y座标
     * @param mapId
     *            地图编号
     * @param head
     *            面向
     * @param effectable
     *            是否产生动画
     */
    public static void teleport(final L1PcInstance pc, final int x,
            final int y, final short mapid, final int head,
            final boolean effectable) {
        teleport(pc, x, y, mapid, head, effectable, TELEPORT);
    }

    /**
     * 传送控制项
     * 
     * @param pc
     *            执行的人物
     * @param x
     *            X座标
     * @param y
     *            Y座标
     * @param mapId
     *            地图编号
     * @param head
     *            面向
     * @param effectable
     *            是否产生动画
     * @param skillType
     *            动画的光 (0:白),(1:青),(2:赤),(3:绿)
     */
    public static void teleport(final L1PcInstance pc, final int x,
            final int y, final short mapId, final int head,
            final boolean effectable, final int skillType) {
        // 保存宠物目前模式
        pc.setPetModel();

        // 传送锁定解除
        // pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK,
        // false));

        // 动画显示
        if (effectable
                && ((skillType >= 0) && (skillType <= EFFECT_SPR.length))) {
            // 改变显示(复原正常)
            // pc.sendPackets(new S_ChangeName(pc, false));
            // 传送动画
            pc.sendPacketsX8(new S_SkillSound(pc.getId(), EFFECT_SPR[skillType]));

            try {
                Thread.sleep((int) (EFFECT_TIME[skillType] * 0.7));

            } catch (final Exception e) {
            }
        }

        pc.setTeleportX(x);
        pc.setTeleportY(y);
        pc.setTeleportMapId(mapId);
        pc.setTeleportHeading(head);

        Teleportation.teleportation(pc);
    }

    /*
     * targetキャラクターのdistanceで指定したマス分前にテレポートする。指定されたマスがマップでない场合何もしない。
     */
    public static void teleportToTargetFront(final L1Character cha,
            final L1Character target, final int distance) {
        int locX = target.getX();
        int locY = target.getY();
        final int heading = target.getHeading();
        final L1Map map = target.getMap();
        final short mapId = target.getMapId();

        // ターゲットの向きからテレポート先の座标を决める。
        switch (heading) {
            case 1:
                locX += distance;
                locY -= distance;
                break;

            case 2:
                locX += distance;
                break;

            case 3:
                locX += distance;
                locY += distance;
                break;

            case 4:
                locY += distance;
                break;

            case 5:
                locX -= distance;
                locY += distance;
                break;

            case 6:
                locX -= distance;
                break;

            case 7:
                locX -= distance;
                locY -= distance;
                break;

            case 0:
                locY -= distance;
                break;

            default:
                break;

        }

        if (map.isPassable(locX, locY, null)) {
            if (cha instanceof L1PcInstance) {
                teleport((L1PcInstance) cha, locX, locY, mapId,
                        cha.getHeading(), true);

            } else if (cha instanceof L1NpcInstance) {
                ((L1NpcInstance) cha).teleport(locX, locY, cha.getHeading());
            }
        }
    }

    /**
     * 随机执行移动
     * 
     * @param pc
     * @param effectable
     */
    public static void randomTeleport(final L1PcInstance pc,
            final boolean effectable) {
        try {
            // まだ本サーバのランテレ处理と违うところが结构あるような???
            final L1Location newLocation = pc.getLocation().randomLocation(200,
                    true);
            final int newX = newLocation.getX();
            final int newY = newLocation.getY();
            final short mapId = (short) newLocation.getMapId();

            L1Teleport.teleport(pc, newX, newY, mapId, 5, effectable);
            pc.setHomeX(newX);
            pc.setHomeY(newY);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
