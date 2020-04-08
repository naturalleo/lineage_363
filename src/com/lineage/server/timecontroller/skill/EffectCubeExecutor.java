package com.lineage.server.timecontroller.skill;

import static com.lineage.server.model.skill.L1SkillId.STATUS_CUBE_IGNITION_TO_ALLY;
import static com.lineage.server.model.skill.L1SkillId.STATUS_CUBE_IGNITION_TO_ENEMY;
import static com.lineage.server.model.skill.L1SkillId.STATUS_CUBE_QUAKE_TO_ALLY;
import static com.lineage.server.model.skill.L1SkillId.STATUS_CUBE_QUAKE_TO_ENEMY;
import static com.lineage.server.model.skill.L1SkillId.STATUS_CUBE_BALANCE;
import static com.lineage.server.model.skill.L1SkillId.STATUS_CUBE_SHOCK_TO_ALLY;
import static com.lineage.server.model.skill.L1SkillId.STATUS_CUBE_SHOCK_TO_ENEMY;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Cube;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1EffectType;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;

/**
 * 幻术师技能状态送出执行
 * 
 * @author dexc
 * 
 */
public class EffectCubeExecutor {

    private static final Log _log = LogFactory.getLog(EffectCubeExecutor.class);

    private static EffectCubeExecutor _instance;

    protected static EffectCubeExecutor get() {
        if (_instance == null) {
            _instance = new EffectCubeExecutor();
        }
        return _instance;
    }

    /**
     * 幻术师技能状态送出的判断
     * 
     * @param effect
     */
    protected void cubeBurn(final L1EffectInstance effect) {
        try {
            for (final L1Object objects : World.get().getVisibleObjects(effect,
                    3)) {

                if (objects == null) {// 对象为空
                    continue;
                }
                if (objects instanceof L1PcInstance) {
                    final L1PcInstance pc = (L1PcInstance) objects;

                    if (pc.isDead()) {
                        continue;
                    }
                    // 副本ID不相等
                    if (effect.get_showId() != pc.get_showId()) {
                        continue;
                    }
                    if (effect.getMaster() == null) {
                        continue;
                    }
                    final L1PcInstance user = (L1PcInstance) effect.getMaster(); // Cube使用者
                    if (pc.getId() == user.getId()) {
                        cubeToAlly(pc, effect);
                        continue;
                    }
                    if ((pc.getClanid() != 0)
                            && (user.getClanid() == pc.getClanid())) {// 相同血盟
                        cubeToAlly(pc, effect);
                        continue;
                    }
                    if (pc.isInParty() && pc.getParty().isMember(user)) {// 队伍成员
                        cubeToAlly(pc, effect);
                        continue;
                    }

                    if (pc.isSafetyZone()) { // 安全区域的状态
                        boolean isNowWar = false;
                        final int castleId = L1CastleLocation
                                .getCastleIdByArea(pc);
                        if (castleId > 0) {
                            isNowWar = ServerWarExecutor.get().isNowWar(
                                    castleId);
                        }
                        if (!isNowWar) {
                            continue;
                        }
                        cubeToEnemy(pc, effect);

                    } else {
                        cubeToEnemy(pc, effect);
                    }

                } else if (objects instanceof L1MonsterInstance) {
                    final L1MonsterInstance mob = (L1MonsterInstance) objects;
                    if (mob.isDead()) {
                        continue;
                    }
                    cubeToEnemy(mob, effect);
                }
            }

        } catch (final Exception e) {
            _log.error("Npc L1Effect幻术师技能(立方)状态送出时间轴发生异常", e);
            effect.deleteMe();
        }
    }

    /**
     * 幻术师技能状态送出的执行(对成员)
     * 
     * @param cha
     * @param effect
     */
    private static void cubeToAlly(final L1Character cha,
            final L1EffectInstance effect) {
        final int castGfx = SkillsTable.get().getTemplate(effect.getSkillId())
                .getCastGfx();
        L1PcInstance pc = null;
        final int skillid = cubeToAllyId(effect.effectType());

        if (!cha.hasSkillEffect(skillid)) {
            switch (effect.effectType()) {
                case isCubeBurn:
                    cha.addFire(30);
                    break;
                case isCubeEruption:
                    cha.addEarth(30);
                    break;
                case isCubeShock:
                    cha.addWind(30);
                    break;
            }
            if (cha instanceof L1PcInstance) {
                pc = (L1PcInstance) cha;
                pc.sendPackets(new S_OwnCharAttrDef(pc));
                pc.sendPackets(new S_SkillSound(pc.getId(), castGfx));
            }
            cha.broadcastPacketX10(new S_SkillSound(cha.getId(), castGfx));
            cha.setSkillEffect(skillid, L1EffectInstance.CUBE_TIME);
            if (effect.effectType() == L1EffectType.isCubeHarmonize) {
                final L1Cube cube = new L1Cube(effect, cha, STATUS_CUBE_BALANCE);
                cube.begin();
            }
        }
    }

    /**
     * 幻术师技能状态送出的执行(对敌人)
     * 
     * @param cha
     * @param effect
     */
    private static void cubeToEnemy(final L1Character cha,
            final L1EffectInstance effect) {
        final int castGfx2 = SkillsTable.get().getTemplate(effect.getSkillId())
                .getCastGfx2();
        L1PcInstance pc = null;
        final int skillid = cubeToEnemyId(effect.effectType());

        if (!cha.hasSkillEffect(skillid)) {
            if (cha instanceof L1PcInstance) {
                pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillSound(pc.getId(), castGfx2));
            }
            cha.broadcastPacketX10(new S_SkillSound(cha.getId(), castGfx2));
            cha.setSkillEffect(skillid, L1EffectInstance.CUBE_TIME);
            final L1Cube cube = new L1Cube(effect, cha, skillid);
            cube.begin();
        }
    }

    /**
     * 取回技能编号
     * 
     * @param effectType
     * @return
     */
    private static int cubeToAllyId(final L1EffectType effectType) {
        int cubeToAllyId = 0;
        switch (effectType) {
            case isCubeBurn:
                cubeToAllyId = STATUS_CUBE_IGNITION_TO_ALLY;
                break;

            case isCubeEruption:
                cubeToAllyId = STATUS_CUBE_QUAKE_TO_ALLY;
                break;

            case isCubeHarmonize:
                cubeToAllyId = STATUS_CUBE_BALANCE;
                break;

            case isCubeShock:
                cubeToAllyId = STATUS_CUBE_SHOCK_TO_ALLY;
                break;
        }
        return cubeToAllyId;
    }

    /**
     * 取回技能编号
     * 
     * @param effectType
     * @return
     */
    private static int cubeToEnemyId(final L1EffectType effectType) {
        int cubeToEnemyId = 0;
        switch (effectType) {
            case isCubeBurn:
                cubeToEnemyId = STATUS_CUBE_IGNITION_TO_ENEMY;
                break;

            case isCubeEruption:
                cubeToEnemyId = STATUS_CUBE_QUAKE_TO_ENEMY;
                break;

            case isCubeHarmonize:
                cubeToEnemyId = STATUS_CUBE_BALANCE;
                break;

            case isCubeShock:
                cubeToEnemyId = STATUS_CUBE_SHOCK_TO_ENEMY;
                break;
        }
        return cubeToEnemyId;
    }
}
