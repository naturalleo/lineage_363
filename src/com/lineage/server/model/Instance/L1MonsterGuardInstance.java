package com.lineage.server.model.Instance;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Character;
import com.lineage.server.serverpackets.S_NPCPack;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.world.World;

/**
 * 对象:警卫(妖堡箭塔) 控制项
 * 
 * @author daien
 * 
 */
public class L1MonsterGuardInstance extends L1NpcInstance {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    private static final Log _log = LogFactory
            .getLog(L1MonsterGuardInstance.class);

    /**
     * TODO 接触资讯
     */
    @Override
    public void onPerceive(final L1PcInstance perceivedFrom) {
        try {
            // 副本ID不相等 不相护显示
            if (perceivedFrom.get_showId() != get_showId()) {
                return;
            }
            perceivedFrom.addKnownObject(this);
            perceivedFrom.sendPackets(new S_NPCPack(this));
            if (0 < getCurrentHp()) {
                onNpcAI(); // 启动AI
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void searchTarget() {
        // 攻击目标搜寻
        L1PcInstance targetPlayer = searchTarget(this);
        if (targetPlayer != null) {
            _hateList.add(targetPlayer, 0);
            _target = targetPlayer;
        } else {
            ISASCAPE = false;
        }
    }

    private L1PcInstance searchTarget(L1MonsterGuardInstance npc) {
        // 攻击目标搜寻
        L1PcInstance targetPlayer = null;
        for (final L1PcInstance pc : World.get().getVisiblePlayer(npc)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                _log.error(e.getLocalizedMessage(), e);
            }
            if (pc.getCurrentHp() <= 0) {
                continue;
            }
            if (pc.isDead()) {
                continue;
            }
            if (pc.isGhost()) {
                continue;
            }
            if (pc.isGm()) {
                continue;
            }

            // 副本ID不相等
            if (npc.get_showId() != pc.get_showId()) {
                continue;
            }

            if (pc.getClan() != null) {
                if (pc.getClan().getCastleId() == L1CastleLocation.OT_CASTLE_ID) {
                    continue;
                }
            }

            boolean isCheck = false;
            if (!pc.isInvisble()) {
                isCheck = true;
            }

            if (npc.getNpcTemplate().is_agrocoi()) {
                isCheck = true;
            }

            if (isCheck) { // 检查
                // 变形探知
                if (pc.hasSkillEffect(67)) { // 变形术
                    if (npc.getNpcTemplate().is_agrososc()) {
                        targetPlayer = pc;
                        return targetPlayer;
                    }
                }

                // 主动攻击
                if (npc.getNpcTemplate().is_agro()) {
                    targetPlayer = pc;
                    return targetPlayer;
                }

                // 特定外型搜寻
                if (npc.getNpcTemplate().is_agrogfxid1() >= 0) {
                    if (pc.getGfxId() == npc.getNpcTemplate().is_agrogfxid1()) {
                        targetPlayer = pc;
                        return targetPlayer;
                    }
                }
                if (npc.getNpcTemplate().is_agrogfxid2() >= 0) {
                    if (pc.getGfxId() == npc.getNpcTemplate().is_agrogfxid2()) {
                        targetPlayer = pc;
                        return targetPlayer;
                    }
                }
            }
        }
        return targetPlayer;
    }

    /**
     * 攻击目标设置
     */
    @Override
    public void setLink(final L1Character cha) {
        // 副本ID不相等
        if (get_showId() != cha.get_showId()) {
            return;
        }
        if ((cha != null) && _hateList.isEmpty()) {
            _hateList.add(cha, 0);
            checkTarget();
        }
    }

    public L1MonsterGuardInstance(final L1Npc template) {
        super(template);
    }

    @Override
    public void onNpcAI() {
        if (isAiRunning()) {
            return;
        }

        setActived(false);
        startAI();
    }

    @Override
    public void onAction(final L1PcInstance pc) {
        if (ATTACK != null) {
            ATTACK.attack(pc, this);
        }
        if ((getCurrentHp() > 0) && !isDead()) {
            final L1AttackMode attack = new L1AttackPc(pc, this);
            if (attack.calcHit()) {
                attack.calcDamage();
                attack.calcStaffOfMana();
                attack.addChaserAttack();
            }
            attack.action();
            attack.commit();
        }
    }

    /**
     * 受攻击mp减少计算
     */
    @Override
    public void ReceiveManaDamage(final L1Character attacker, final int mpDamage) {
    }

    /**
     * 受攻击hp减少计算
     */
    @Override
    public void receiveDamage(L1Character attacker, final int damage) {
    }

    @Override
    public void setCurrentHp(final int i) {
    }

    @Override
    public void setCurrentMp(final int i) {
    }
}
