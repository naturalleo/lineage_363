package com.lineage.server.model.skill;

import static com.lineage.server.model.skill.L1SkillId.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ChangeShape;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillBrave;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.serverpackets.S_SkillIconBloodstain;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 冲突技能抵销
 * 
 * @author dexc
 * 
 */
public class L1BuffUtil {

    private static final Log _log = LogFactory.getLog(L1BuffUtil.class);

    /**
     * 无法使用药水
     * 
     * @param pc
     * @return true:可以使用 false:无法使用
     */
    public static boolean stopPotion(final L1PcInstance pc) {
        if (pc.hasSkillEffect(L1SkillId.DECAY_POTION) == true) { // 药水霜化术
            // 698 喉咙灼热，无法喝东西。
            pc.sendPackets(new S_ServerMessage(698));
            return false;
        }
        return true;
    }

    /**
     * 解除魔法技能绝对屏障
     * 
     * @param pc
     */
    public static void cancelAbsoluteBarrier(final L1PcInstance pc) { // 解除魔法技能绝对屏障
        if (pc.hasSkillEffect(ABSOLUTE_BARRIER)) {
            pc.killSkillEffectTimer(ABSOLUTE_BARRIER);
            pc.startHpRegeneration();
            pc.startMpRegeneration();
        }
    }

    /**
     * 加速效果 抵销对应技能
     * 
     * @param pc
     */
    public static void hasteStart(final L1PcInstance pc) {
        try {
            // 解除加速术
            if (pc.hasSkillEffect(HASTE)) {
                pc.killSkillEffectTimer(HASTE);
                pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
                pc.setMoveSpeed(0);

            }

            // 解除强力加速术
            if (pc.hasSkillEffect(GREATER_HASTE)) {
                pc.killSkillEffectTimer(GREATER_HASTE);
                pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
                pc.setMoveSpeed(0);

            }

            // 解除加速药水
            if (pc.hasSkillEffect(STATUS_HASTE)) {
                pc.killSkillEffectTimer(STATUS_HASTE);
                pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
                pc.setMoveSpeed(0);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 加速药水效果
     * 
     * @param pc
     * @param timeMillis
     */
    public static void haste(final L1PcInstance pc, final int timeMillis) {
        try {
            hasteStart(pc);

            // 加速药水效果
            pc.setSkillEffect(STATUS_HASTE, timeMillis);

            final int objId = pc.getId();
            pc.sendPackets(new S_SkillHaste(objId, 1, timeMillis / 1000));
            pc.broadcastPacketAll(new S_SkillHaste(objId, 1, 0));

            pc.sendPacketsX8(new S_SkillSound(objId, 191));
            pc.setMoveSpeed(1);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 勇敢效果 抵销对应技能
     * 
     * @param pc
     */
    public static void braveStart(final L1PcInstance pc) {
        try {
            /*
             * { HOLY_WALK, MOVING_ACCELERATION, WIND_WALK, STATUS_BRAVE,
             * STATUS_BRAVE2, STATUS_ELFBRAVE, STATUS_RIBRAVE, BLOODLUST },
             */

            // 解除神圣疾走
            if (pc.hasSkillEffect(HOLY_WALK)) {
                pc.killSkillEffectTimer(HOLY_WALK);
                pc.sendPacketsAll(new S_SkillBrave(pc.getId(), 0, 0));
                pc.setBraveSpeed(0);
            }

            // 解除行走加速
            if (pc.hasSkillEffect(MOVING_ACCELERATION)) {
                pc.killSkillEffectTimer(MOVING_ACCELERATION);
                pc.sendPacketsAll(new S_SkillBrave(pc.getId(), 0, 0));
                pc.setBraveSpeed(0);
            }

            // 解除风之疾走
            if (pc.hasSkillEffect(WIND_WALK)) {
                pc.killSkillEffectTimer(WIND_WALK);
                pc.sendPacketsAll(new S_SkillBrave(pc.getId(), 0, 0));
                pc.setBraveSpeed(0);
            }

            // 解除勇敢药水效果
            if (pc.hasSkillEffect(STATUS_BRAVE)) {
                pc.killSkillEffectTimer(STATUS_BRAVE);
                pc.sendPacketsAll(new S_SkillBrave(pc.getId(), 0, 0));
                pc.setBraveSpeed(0);
            }

            // 解除精灵饼干效果
            if (pc.hasSkillEffect(STATUS_ELFBRAVE)) {
                pc.killSkillEffectTimer(STATUS_ELFBRAVE);
                pc.sendPacketsAll(new S_SkillBrave(pc.getId(), 0, 0));
                pc.setBraveSpeed(0);
            }

            // 解除生命之树果实效果
            if (pc.hasSkillEffect(STATUS_RIBRAVE)) {
                pc.killSkillEffectTimer(STATUS_RIBRAVE);
                // XXX ユグドラの实のアイコンを消す方法が不明
                pc.setBraveSpeed(0);
            }

            // 解除血之渴望
            if (pc.hasSkillEffect(BLOODLUST)) {
                pc.killSkillEffectTimer(BLOODLUST);
                pc.sendPacketsAll(new S_SkillBrave(pc.getId(), 0, 0));
                pc.setBraveSpeed(0);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 勇敢药水效果
     * 
     * @param pc
     *            对象
     * @param timeMillis
     *            TIME
     */
    public static void brave(final L1PcInstance pc, final int timeMillis) {
        try {
            braveStart(pc);

            // 勇敢药水效果
            pc.setSkillEffect(STATUS_BRAVE, timeMillis);

            final int objId = pc.getId();
            pc.sendPackets(new S_SkillBrave(objId, 1, timeMillis / 1000));
            pc.broadcastPacketAll(new S_SkillBrave(objId, 1, 0));

            pc.sendPacketsX8(new S_SkillSound(objId, 751));
            pc.setBraveSpeed(1);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 龙骑士变身
     * 
     * @param pc
     */
    public static void doPoly(final L1PcInstance pc) {
        try {
            final int skillId = pc.getAwakeSkillId();
            int polyId = 0;
            switch (skillId) {
                case AWAKEN_ANTHARAS:
                    polyId = 9362;
                    break;
                case AWAKEN_FAFURION:
                    polyId = 9364;
                    break;
                case AWAKEN_VALAKAS:
                    polyId = 9363;
                    break;
            }
            if (pc.hasSkillEffect(SHAPE_CHANGE)) {
                pc.killSkillEffectTimer(SHAPE_CHANGE);
            }
            pc.setTempCharGfx(polyId);

            pc.sendPacketsAll(new S_ChangeShape(pc, polyId));

            final L1ItemInstance weapon = pc.getWeapon();
            if (weapon != null) {
                pc.sendPacketsAll(new S_CharVisualUpdate(pc));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 解除龙骑士变身
     * 
     * @param pc
     */
    public static void undoPoly(final L1PcInstance pc) {
        try {
            final int classId = pc.getClassId();
            pc.setTempCharGfx(classId);

            pc.sendPacketsAll(new S_ChangeShape(pc, classId));

            final L1ItemInstance weapon = pc.getWeapon();
            if (weapon != null) {
                pc.sendPacketsAll(new S_CharVisualUpdate(pc));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 经验加倍技能判断(第一段)
     * 
     * @param pc
     * @return
     */
    public static boolean cancelExpSkill(final L1PcInstance pc) {
        // 停止初段技能
        if (pc.hasSkillEffect(COOKING_1_7_N)) {
            pc.removeSkillEffect(COOKING_1_7_N);
        }
        if (pc.hasSkillEffect(COOKING_1_7_S)) {
            pc.removeSkillEffect(COOKING_1_7_S);
        }
        if (pc.hasSkillEffect(COOKING_2_7_N)) {
            pc.removeSkillEffect(COOKING_2_7_N);
        }
        if (pc.hasSkillEffect(COOKING_2_7_S)) {
            pc.removeSkillEffect(COOKING_2_7_S);
        }
        if (pc.hasSkillEffect(COOKING_3_7_N)) {
            pc.removeSkillEffect(COOKING_3_7_N);
        }
        if (pc.hasSkillEffect(COOKING_3_7_S)) {
            pc.removeSkillEffect(COOKING_3_7_S);
        }

        // 返回已有技能
        if (pc.hasSkillEffect(EXP13)) {
            final int time = pc.getSkillEffectTimeSec(EXP13);
            // 3021 目前正在享受 %0 倍经验.【剩余时间: %1 秒】
            pc.sendPackets(new S_ServerMessage("\\fX第一段130%经验 剩余时间(秒):" + time));
            return false;
        }
        if (pc.hasSkillEffect(EXP15)) {
            final int time = pc.getSkillEffectTimeSec(EXP15);
            // 3021 目前正在享受 %0 倍经验.【剩余时间: %1 秒】
            pc.sendPackets(new S_ServerMessage("\\fX第一段150%经验 剩余时间(秒):" + time));
            return false;
        }
        if (pc.hasSkillEffect(EXP17)) {
            final int time = pc.getSkillEffectTimeSec(EXP17);
            // 3021 目前正在享受 %0 倍经验.【剩余时间: %1 秒】
            pc.sendPackets(new S_ServerMessage("\\fX第一段170%经验 剩余时间(秒):" + time));
            return false;
        }
        if (pc.hasSkillEffect(EXP20)) {
            final int time = pc.getSkillEffectTimeSec(EXP20);
            // 3021 目前正在享受 %0 倍经验.【剩余时间: %1 秒】
            pc.sendPackets(new S_ServerMessage("\\fX第一段200%经验 剩余时间(秒):" + time));
            return false;
        }
        return true;
    }

    /**
     * 经验加倍技能判断(第二段)
     * 
     * @param pc
     * @return
     */
    public static boolean cancelExpSkill_2(final L1PcInstance pc) {
        // 返回已有技能
        if (pc.hasSkillEffect(SEXP11)) {
            final int time = pc.getSkillEffectTimeSec(SEXP11);
            // 3021 目前正在享受 %0 倍经验.【剩余时间: %1 秒】
            pc.sendPackets(new S_ServerMessage("第二段110%经验 剩余时间(秒):" + time));
            return false;
        }
        // 返回已有技能
        if (pc.hasSkillEffect(SEXP13)) {
            final int time = pc.getSkillEffectTimeSec(SEXP13);
            // 3083 第二段经验1.3倍效果时间尚有 %0 秒。
            pc.sendPackets(new S_ServerMessage("第二段130%经验 剩余时间(秒):" + time));
            return false;
        }
        if (pc.hasSkillEffect(SEXP15)) {
            final int time = pc.getSkillEffectTimeSec(SEXP15);
            // 3084 第二段经验1.5倍效果时间尚有 %0 秒。
            pc.sendPackets(new S_ServerMessage("第二段150%经验 剩余时间(秒):" + time));
            return false;
        }
        if (pc.hasSkillEffect(SEXP17)) {
            final int time = pc.getSkillEffectTimeSec(SEXP17);
            // 3085 第二段经验1.7倍效果时间尚有 %0 秒。
            pc.sendPackets(new S_ServerMessage("第二段170%经验 剩余时间(秒):" + time));
            return false;
        }
        if (pc.hasSkillEffect(SEXP20)) {
            final int time = pc.getSkillEffectTimeSec(SEXP20);
            // 3082 第二段经验2.0倍效果时间尚有 %0 秒。
            pc.sendPackets(new S_ServerMessage("第二段200%经验 剩余时间(秒):" + time));
            return false;
        }
        return true;
    }

    /**
     * 四大龙物品
     * 
     * @param pc
     * @return
     */
    public static int cancelDragon(final L1PcInstance pc) {
        if (pc.hasSkillEffect(DRAGON1)) {
            return pc.getSkillEffectTimeSec(DRAGON1);
        }
        if (pc.hasSkillEffect(DRAGON2)) {
            return pc.getSkillEffectTimeSec(DRAGON2);
        }
        if (pc.hasSkillEffect(DRAGON3)) {
            return pc.getSkillEffectTimeSec(DRAGON3);
        }
        if (pc.hasSkillEffect(DRAGON4)) {
            return pc.getSkillEffectTimeSec(DRAGON4);
        }
        if (pc.hasSkillEffect(DRAGON5)) {
            return pc.getSkillEffectTimeSec(DRAGON5);
        }
        if (pc.hasSkillEffect(DRAGON6)) {
            return pc.getSkillEffectTimeSec(DRAGON6);
        }
        if (pc.hasSkillEffect(DRAGON7)) {
            return pc.getSkillEffectTimeSec(DRAGON7);
        }
        return -1;
    }
    /** 
     * 龙之血痕-
     * -pc-
     * -byte -
     * time时间 秒-
     * showGfx-
     *  */
    public static void bloodstain(final L1PcInstance pc, final byte type,
            final int time, final boolean showGfx) {

        if (showGfx) {
            pc.sendPackets(new S_SkillSound(pc.getId(), 7783));
            pc.broadcastPacketX10(new S_SkillSound(pc.getId(), 7783));
        }

        int skillId = EFFECT_BLOODSTAIN_OF_ANTHARAS;
        int iconType = 0;
        if (type == 0) { // 安塔瑞斯
            if (!pc.hasSkillEffect(skillId)) {
                pc.addAc(-2); // 防御 -2
                pc.addWater(50); // 水属性 +50
            }
            iconType = S_SkillIconBloodstain.ANTHARAS; // 安塔瑞斯的血痕
        } else if (type == 1) { // 法利昂
            skillId = EFFECT_BLOODSTAIN_OF_FAFURION;
            if (!pc.hasSkillEffect(skillId)) {
                pc.addWind(50); // 风属性 +50
            }
            iconType = S_SkillIconBloodstain.FAFURION; // 法利昂的血痕
        }
        pc.sendPackets(new S_OwnCharAttrDef(pc));
        pc.sendPackets(new S_SkillIconBloodstain(iconType, time / 60));
        pc.setSkillEffect(skillId, (time * 1000));
    }
}
