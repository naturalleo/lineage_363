package com.lineage.server.model.Instance;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.ActionCodes;
import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1War;
import com.lineage.server.model.L1WarSpawn;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_NPCPack;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import com.lineage.server.world.WorldNpc;
import com.lineage.server.world.WorldWar;

/**
 * 守护者之塔控制项
 * 
 * @author daien
 * 
 */
public class L1TowerTDInstance extends L1NpcInstance {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    private static final Log _log = LogFactory.getLog(L1TowerTDInstance.class);
    
    private int _crackStatus;// 损坏状态
    /**
     * 守护者之塔
     * 
     * @param template
     */
    public L1TowerTDInstance(final L1Npc template) {
        super(template);
    }



    @Override
    public void onPerceive(final L1PcInstance perceivedFrom) {
        try {
            perceivedFrom.addKnownObject(this);
            perceivedFrom.sendPackets(new S_NPCPack(this));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void onAction(final L1PcInstance player) {
        try {
            if ((this.getCurrentHp() > 0) && !this.isDead()) {
                final L1AttackMode attack = new L1AttackPc(player, this);
                if (attack.calcHit()) {
                    attack.calcDamage();
                    // attack.addChaserAttack();
                }
                attack.action();
                attack.commit();
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void receiveDamage(final L1Character attacker, final int damage) {
        boolean isDamage = true;

        // 塔可以攻击
        if (isDamage) {
            // 判断主要攻击者
            L1PcInstance pc = null;
            if (attacker instanceof L1PcInstance) {// 攻击者是玩家
                return;
            }


            if ((getCurrentHp() > 0) && !isDead()) {
                final int newHp = getCurrentHp() - damage;
                if ((newHp <= 0) && !isDead()) {
                    setCurrentHpDirect(0);
                    setDead(true);
                    setStatus(ActionCodes.ACTION_TowerDie);
                    _crackStatus = 0;
                    final Death death = new Death(this);
                    GeneralThreadPool.get().execute(death);
                }
                if (newHp > 0) {
                    setCurrentHp(newHp);
                    if ((getMaxHp() * 1 / 4) > getCurrentHp()) {
                        if (_crackStatus != 3) {
                            broadcastPacketAll(new S_DoActionGFX(getId(),
                                    ActionCodes.ACTION_TowerCrack3));
                            setStatus(ActionCodes.ACTION_TowerCrack3);
                            _crackStatus = 3;
                        }

                    } else if ((getMaxHp() * 2 / 4) > getCurrentHp()) {
                        if (_crackStatus != 2) {
                            broadcastPacketAll(new S_DoActionGFX(getId(),
                                    ActionCodes.ACTION_TowerCrack2));
                            setStatus(ActionCodes.ACTION_TowerCrack2);
                            _crackStatus = 2;
                        }

                    } else if ((getMaxHp() * 3 / 4) > getCurrentHp()) {
                        if (_crackStatus != 1) {
                            broadcastPacketAll(new S_DoActionGFX(getId(),
                                    ActionCodes.ACTION_TowerCrack1));
                            setStatus(ActionCodes.ACTION_TowerCrack1);
                            _crackStatus = 1;
                        }
                    }
                }

            } else if (!this.isDead()) { // 念のため
                setDead(true);
                setStatus(ActionCodes.ACTION_TowerDie);
                final Death death = new Death(this);
                GeneralThreadPool.get().execute(death);
            }
        }
    }

    @Override
    public void setCurrentHp(final int i) {
        final int currentHp = Math.min(i, getMaxHp());

        if (getCurrentHp() == currentHp) {
            return;
        }

        setCurrentHpDirect(currentHp);
    }

    /**
     * 塔死亡
     * 
     * @author daien
     * 
     */
    private class Death implements Runnable {
        private L1TowerTDInstance _tower = null;// 塔

        public Death(L1TowerTDInstance tower) {
            _tower = tower;
        }

        @Override
        public void run() {
            _tower.setCurrentHpDirect(0);
            _tower.setDead(true);
            _tower.setStatus(ActionCodes.ACTION_TowerDie);

            _tower.getMap().setPassable(_tower.getLocation(), true);

            _tower.broadcastPacketAll(new S_DoActionGFX(_tower.getId(),
                    ActionCodes.ACTION_TowerDie));
        }
    }

    @Override
    public void deleteMe() {
        _destroyed = true;
        if (getInventory() != null) {
            getInventory().clearItems();
        }
        allTargetClear();
        _master = null;
        World.get().removeVisibleObject(this);
        World.get().removeObject(this);
        for (final L1PcInstance pc : World.get().getRecognizePlayer(this)) {
            pc.removeKnownObject(this);
            pc.sendPackets(new S_RemoveObject(this));
        }
        removeAllKnownObjects();
    }

    /**
     * 是亚丁守护子塔
     * 
     * @return
     */
    public boolean isSubTower() {
        return ((getNpcTemplate().get_npcId() == 81190)// 守护塔:伊娃
                || (getNpcTemplate().get_npcId() == 81191)// 守护塔:帕格里奥
                || (getNpcTemplate().get_npcId() == 81192)// 守护塔:马普勒
        || (getNpcTemplate().get_npcId() == 81193));// 守护塔:沙哈
    }
}
