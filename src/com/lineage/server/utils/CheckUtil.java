package com.lineage.server.utils;

import static com.lineage.server.model.skill.L1SkillId.DECAY_POTION;
import static com.lineage.server.model.skill.L1SkillId.EARTH_BIND;
import static com.lineage.server.model.skill.L1SkillId.FOG_OF_SLEEPING;
import static com.lineage.server.model.skill.L1SkillId.FREEZING_BREATH;
import static com.lineage.server.model.skill.L1SkillId.ICE_LANCE;
import static com.lineage.server.model.skill.L1SkillId.SHOCK_SKIN;
import static com.lineage.server.model.skill.L1SkillId.SHOCK_STUN;
import static com.lineage.server.model.skill.L1SkillId.STATUS_CURSE_PARALYZED;
import static com.lineage.server.model.skill.L1SkillId.STATUS_POISON_PARALYZED;
import static com.lineage.server.model.skill.L1SkillId.THUNDER_GRAB;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.NpcTeleportTable;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1IllusoryInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.timecontroller.server.ServerUseMapTimer;
import com.lineage.server.world.World;

/**
 * 对象检查器
 * 
 * @author daien
 * 
 */
public class CheckUtil {

    private static final Log _log = LogFactory.getLog(CheckUtil.class);

    private CheckUtil() {
    }

    /**
     * 检查攻击致死成立的PC
     * 
     * @param lastAttacker
     * 
     * @return 攻击致死成立的PC
     */
    public static L1PcInstance checkAtkPc(final L1Character lastAttacker) {
        try {
            // 判断主要攻击者
            L1PcInstance pc = null;

            if (lastAttacker instanceof L1PcInstance) {// 攻击者是玩家
                pc = (L1PcInstance) lastAttacker;

            } else if (lastAttacker instanceof L1PetInstance) {// 攻击者是宠物
                final L1PetInstance atk = (L1PetInstance) lastAttacker;
                if (atk.getMaster() != null) {
                    if (atk.getMaster() instanceof L1PcInstance) {
                        pc = (L1PcInstance) atk.getMaster();
                    }
                }

            } else if (lastAttacker instanceof L1SummonInstance) {// 攻击者是 召换兽
                final L1SummonInstance atk = (L1SummonInstance) lastAttacker;
                if (atk.getMaster() != null) {
                    if (atk.getMaster() instanceof L1PcInstance) {
                        pc = (L1PcInstance) atk.getMaster();
                    }
                }

            } else if (lastAttacker instanceof L1IllusoryInstance) {// 攻击者是 分身
                final L1IllusoryInstance atk = (L1IllusoryInstance) lastAttacker;
                if (atk.getMaster() != null) {
                    if (atk.getMaster() instanceof L1PcInstance) {
                        pc = (L1PcInstance) atk.getMaster();
                    }
                }

            } else if (lastAttacker instanceof L1EffectInstance) {// 攻击者是 技能物件
                final L1EffectInstance atk = (L1EffectInstance) lastAttacker;
                if (atk.getMaster() != null) {
                    if (atk.getMaster() instanceof L1PcInstance) {
                        pc = (L1PcInstance) atk.getMaster();
                    }
                }

            } else {
                return null;
            }
            return pc;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 检查地图使用权
     * 
     * @param pc
     *            检查对象
     */
    public static void isUserMap(final L1PcInstance pc) {
        try {
            if (!pc.isGm()) {
                // 检查 时间/VIP 地图使用权
                isTimeMap(pc);

                // 检查团队地图使用权
                isPartyMap(pc);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 检查团队地图使用权
     * 
     * @param pc
     *            检查对象
     */
    private static void isPartyMap(final L1PcInstance pc) {
        try {
            short mapid = pc.getMapId();
            Integer userCount = NpcTeleportTable.get().isPartyMap(mapid);

            if (userCount != null) {
                if (!pc.isInParty()) {
                    // 425：您并没有参加任何队伍。
                    pc.sendPackets(new S_ServerMessage(425));
                    L1Teleport.teleport(pc, 33080, 33392, (short) 4, 5, true);
                    return;
                }

                int partyCount = pc.getParty().partyUserInMap(mapid);
                if (partyCount < userCount.intValue()) {
                    L1Teleport.teleport(pc, 33080, 33392, (short) 4, 5, true);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 检查 时间/VIP 地图使用权
     * 
     * @param pc
     *            检查对象
     */
    private static void isTimeMap(final L1PcInstance pc) {
        try {
            short mapid = pc.getMapId();
            // 时效地图判断
            if (NpcTeleportTable.get().isTimeMap(mapid)) {
                // 该人物 是否在使用者清单中
                Integer time = ServerUseMapTimer.MAP.get(pc);
                if (time == null) {
                    L1Teleport.teleport(pc, 33080, 33392, (short) 4, 5, true);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 全物件无法使用
     * 
     * @param pc
     *            检查对象
     * @return
     */
    public static boolean getUseItemAll(final L1PcInstance pc) {
        /*
         * 33 木乃伊的诅咒 50 冰矛围篱 66 沉睡之雾 71 药水霜化术 87 冲击之晕 157 大地屏障 189 冲击之肤 192 夺命之雷
         */
        if (pc.hasSkillEffect(STATUS_CURSE_PARALYZED)) {
            return false;
        }

        if (pc.hasSkillEffect(STATUS_POISON_PARALYZED)) {
            return false;
        }

        if (pc.hasSkillEffect(FOG_OF_SLEEPING)) {
            return false;
        }

        if (pc.hasSkillEffect(SHOCK_STUN)) {
            return false;
        }

        return true;
    }

    /**
     * 无法使用指定类型道具
     * 
     * @param pc
     *            检查对象
     * @return
     */
    public static boolean getUseItem(final L1PcInstance pc) {
        /*
         * 33 木乃伊的诅咒 50 冰矛围篱 66 沉睡之雾 71 药水霜化术 87 冲击之晕 157 大地屏障 189 冲击之肤 192 夺命之雷
         */

        if (pc.hasSkillEffect(DECAY_POTION)) {
            pc.sendPackets(new S_ServerMessage(698)); // 喉咙灼热，无法喝东西。
            return false;
        }

        if (pc.hasSkillEffect(EARTH_BIND)) {
            return false;
        }

        if (pc.hasSkillEffect(SHOCK_SKIN)) {
            return false;
        }

        if (pc.hasSkillEffect(THUNDER_GRAB)) {
            return false;
        }

        if (pc.hasSkillEffect(ICE_LANCE)) {
            return false;
        }

        if (pc.hasSkillEffect(FREEZING_BREATH)) {
            return false;
        }
        return true;
    }

    /**
     * 该座标点上有物件(不可穿透目标)
     * 
     * @param pc
     *            检查对象
     * @param locx
     * @param locy
     * @param mapid
     * @return true有 false没有
     */
    public static boolean checkPassable(final L1PcInstance pc, final int locx,
            final int locy, final short mapid) {
        if (QuestMapTable.get().isQuestMap(pc.getMapId())) {
            // 是副本专用地图(不列入判断的地形)
            return false;
        }
        // 1格范围内物件
        final Collection<L1Object> allObj = World.get()
                .getVisibleObjects(pc, 1);
        for (final Iterator<L1Object> iter = allObj.iterator(); iter.hasNext();) {
            final L1Object obj = iter.next();
            // 道具
            if (obj instanceof L1ItemInstance) {
                continue;
            }
            if (!(obj instanceof L1Character)) {
                continue;
            }
            final L1Character character = (L1Character) obj;
            // 忽略隐身
//            if (character.isInvisble()) {
//                continue;
//            }
//            if (character instanceof L1NpcInstance) {
//                continue;
//            }
            if (character instanceof L1PcInstance) {
            	final L1PcInstance tgpc = (L1PcInstance) character;
            	if (tgpc.isGmInvis()) {// 忽略GM隐身
            		continue;
            	}
            	if (tgpc.isGhost()) {// 忽略鬼魂模式
            		continue;
            	}
            }
            if ((character.getX() == locx) && (character.getY() == locy)
            		&& (character.getMapId() == mapid)) {// 其他
            	return true;
            }
//            if (character instanceof L1PetInstance ||
//            	character instanceof L1MonsterInstance||
//            	character instanceof L1SummonInstance) {
//                if ((character.getX() == locx) && (character.getY() == locy)) {// 其他
//                	return true;
//                }
//            }
        }
        return false;
    }

    /**
     * 检查该物件是否允许攻击(技能)
     * 
     * @param cha
     * @return
     */
    public static boolean checkAttackSkill(final L1Character cha) {
        try {
            if (cha instanceof L1EffectInstance) { // 效果不列入目标
                return false;

            } else if (cha instanceof L1IllusoryInstance) { // 攻击者是分身不列入目标(设置主人为目标)
                return false;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return true;
    }
}
