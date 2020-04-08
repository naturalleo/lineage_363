package com.lineage.server.model.Instance;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.ActionCodes;
import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1PcQuest;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_FollowerPack;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.World;

/**
 * 跟随者
 * 
 * @author dexc
 * 
 */
public class L1FollowerInstance extends L1NpcInstance {

    private static final long serialVersionUID = 1L;

    private static final Log _log = LogFactory.getLog(L1FollowerInstance.class);

    /**
     * 目标检查
     * 
     */
    @Override
    public boolean noTarget() {
        // final L1NpcInstance targetNpc = null;
        if (ATTACK != null) {
            L1PcInstance pc = null;
            if (this._master instanceof L1PcInstance) {
                pc = (L1PcInstance) _master;
            }
            ATTACK.attack(pc, this);

        } else {
            for (final L1Object object : World.get().getVisibleObjects(this)) {
                if (object instanceof L1NpcInstance) {
                    final L1NpcInstance tgnpc = (L1NpcInstance) object;
                    if ((tgnpc.getNpcTemplate().get_npcId() == 71061 // 卡得穆斯
                            )
                            && (getNpcTemplate().get_npcId() == 71062)) { // 卡米特
                        if (getLocation().getTileLineDistance(
                                _master.getLocation()) < 3) {
                            final L1PcInstance pc = (L1PcInstance) _master;
                            if (((pc.getX() >= 32448) && (pc.getX() <= 32452)) // カドモス周辺座标
                                    && ((pc.getY() >= 33048) && (pc.getY() <= 33052))
                                    && (pc.getMapId() == 440)) {
                                setParalyzed(true);
                                if (!pc.getInventory().checkItem(40711)) {
                                    createNewItem(pc, 40711, 1);
                                    pc.getQuest().set_step(
                                            L1PcQuest.QUEST_CADMUS, 3);
                                }
                                deleteMe();
                                return true;
                            }
                        }

                    } else if ((tgnpc.getNpcTemplate().get_npcId() == 71074 // 蜥蜴人长老
                            )
                            && (getNpcTemplate().get_npcId() == 71075)) {// 疲惫的蜥蜴人战士
                        // 疲れ果てたリザードマンファイター
                        if (getLocation().getTileLineDistance(
                                _master.getLocation()) < 3) {
                            final L1PcInstance pc = (L1PcInstance) _master;
                            if (((pc.getX() >= 32731) && (pc.getX() <= 32735)) // リザードマン长老周辺座标
                                    && ((pc.getY() >= 32854) && (pc.getY() <= 32858))
                                    && (pc.getMapId() == 480)) {
                                setParalyzed(true);
                                if (!pc.getInventory().checkItem(40633)) {
                                    createNewItem(pc, 40633, 1);
                                    pc.getQuest().set_step(
                                            L1PcQuest.QUEST_LIZARD, 2);
                                }
                                deleteMe();
                                return true;
                            }
                        }
                    }
                }
            }
        }

        // 进行删除
        if (destroyed()) {
            return true;
        }
        // 主人为空
        if (_master == null) {
            return true;
        }
        if (_master.isDead()
                || (getLocation().getTileLineDistance(_master.getLocation()) > 13)) {
            setParalyzed(true);
            // 跟随者主人遗失 重新召唤原始NPC
            spawn(getNpcTemplate().get_npcId(), getX(), getY(), getHeading(),
                    getMapId());

            deleteMe();
            return true;

        } else if ((_master != null) && (_master.getMapId() == getMapId())) {
            // 跟随主人移动
            if (getLocation().getTileLineDistance(_master.getLocation()) > 2) {
                if (_npcMove != null) {
                    _npcMove.setDirectionMove(_npcMove.moveDirection(
                            _master.getX(), _master.getY()));
                    setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
                }
            }
        }
        return false;
    }

    /**
     * 跟随者
     * 
     * @param template
     *            召唤NPC数据
     * @param target
     *            原始对话对象
     * @param master
     *            执行者
     */
    public L1FollowerInstance(final L1Npc template, final L1NpcInstance target,
            final L1Character master) {
        super(template);

        // 对话对象是否已经成为跟随者NPC主人
        if (is(master)) {
            return;
        }

        this._master = master;
        this.setId(IdFactoryNpc.get().nextId());
        // 副本编号
        this.set_showId(master.get_showId());

        this.setMaster(master);
        this.setX(target.getX());
        this.setY(target.getY());
        this.setMap(target.getMapId());
        this.setHeading(target.getHeading());
        this.setLightSize(target.getLightSize());

        // 删除原始对话对象(这可让原始NPC重新召唤)
        target.setParalyzed(true);
        target.deleteMe();

        World.get().storeObject(this);
        World.get().addVisibleObject(this);
        for (final L1PcInstance pc : World.get().getRecognizePlayer(this)) {
            this.onPerceive(pc);
        }

        this.onNpcAI();
        master.addFollower(this);
    }

    /**
     * 对话对象是否已经成为跟随者NPC主人
     * 
     * @param master
     * @param npcId
     * @return
     */
    private boolean is(final L1Character master) {
        if (master.getFollowerList().size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void onNpcAI() {
        if (this.isAiRunning()) {
            return;
        }
        this.startAI();
    }

    @Override
    public synchronized void deleteMe() {
        // 移出跟随者
        this._master.removeFollower(this);
        this.setMaster(null);
        this.getMap().setPassable(this.getLocation(), true);
        // 执行L1NpcInstance删除动作
        super.deleteMe();
    }

    @Override
    public void onAction(final L1PcInstance pc) {
        try {
            final L1AttackMode attack = new L1AttackPc(pc, this);
            if (attack.calcHit()) {
                attack.calcDamage();
                // attack.calcStaffOfMana(); XXX
                // attack.addChaserAttack();
            }
            attack.action();
            attack.commit();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void onTalkAction(final L1PcInstance player) {
        if (this.isDead()) {
            return;
        }
        switch (this.getNpcTemplate().get_npcId()) {
            case 71062:// 卡米特
                if (this._master.equals(player)) {
                    player.sendPackets(new S_NPCTalkReturn(this.getId(),
                            "kamit2"));
                } else {
                    player.sendPackets(new S_NPCTalkReturn(this.getId(),
                            "kamit1"));
                }
                break;

            case 71075:// 疲惫的蜥蜴人战士
                if (this._master.equals(player)) {
                    player.sendPackets(new S_NPCTalkReturn(this.getId(),
                            "llizard2"));
                } else {
                    player.sendPackets(new S_NPCTalkReturn(this.getId(),
                            "llizard1a"));
                }
                break;
        }
    }

    /**
     * TODO 接触资讯
     */
    @Override
    public void onPerceive(final L1PcInstance perceivedFrom) {
        try {
            // 副本ID不相等 不相护显示
            if (perceivedFrom.get_showId() != this.get_showId()) {
                return;
            }
            perceivedFrom.addKnownObject(this);
            perceivedFrom.sendPackets(new S_FollowerPack(this, perceivedFrom));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 给予任务道具
     * 
     * @param pc
     * @param item_id
     * @param count
     */
    private void createNewItem(final L1PcInstance pc, final int item_id,
            final long count) {
        final L1ItemInstance item = ItemTable.get().createItem(item_id);
        item.setCount(count);
        if (item != null) {
            if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
                pc.getInventory().storeItem(item);
            } else {
                item.set_showId(pc.get_showId());
                World.get().getInventory(pc.getX(), pc.getY(), pc.getMapId())
                        .storeItem(item);
            }
            pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
        }
    }

    /**
     * 跟随者主人遗失 重新召唤原始NPC
     * 
     * @param npcId
     *            编号
     * @param x
     *            X座标
     * @param y
     *            Y座标
     * @param h
     *            面向
     * @param m
     *            地图编号
     */
    public void spawn(final int npcId, final int x, final int y, final int h,
            final short m) {
        try {
            final L1NpcInstance mob = L1SpawnUtil.spawn(npcId, x, y, m, h);
            final L1QuestInstance newnpc = (L1QuestInstance) mob;
            newnpc.onNpcAI();
            // newnpc.turnOnOffLight();
            newnpc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // チャット开始

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 受攻击hp减少计算 XXX
     */
    @Override
    public void receiveDamage(L1Character attacker, final int damage) {
        if (this.getMaxHp() > 100) {
            if ((this.getCurrentHp() > 0) && !this.isDead()) {
                final int newHp = this.getCurrentHp() - damage;
                if ((newHp <= 0) && !this.isDead()) {
                    this.setCurrentHpDirect(0);
                    this.setDead(true);
                    this.setStatus(ActionCodes.ACTION_Die);
                    final Death death = new Death();
                    GeneralThreadPool.get().execute(death);
                }
                if (newHp > 0) {
                    this.setCurrentHp(newHp);
                }

            } else if (!this.isDead()) {
                this.setDead(true);
                this.setStatus(ActionCodes.ACTION_Die);
                final Death death = new Death();
                GeneralThreadPool.get().execute(death);
            }
        }
    }

    /**
     * 死亡
     * 
     * @author daien
     * 
     */
    private class Death implements Runnable {

        @Override
        public void run() {
            try {
                // 解除旧座标障碍宣告
                getMap().setPassable(getLocation(), true);

                setDeathProcessing(true);

                broadcastPacketAll(new S_DoActionGFX(getId(),
                        ActionCodes.ACTION_Die));

                setDeathProcessing(false);
                // 加入死亡处理清单
                startDeleteTimer(10);

            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
