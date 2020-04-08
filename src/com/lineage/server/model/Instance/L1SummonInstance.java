package com.lineage.server.model.Instance;

import static com.lineage.server.model.skill.L1SkillId.FOG_OF_SLEEPING;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.server.ActionCodes;
import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.drop.SetDrop;
import com.lineage.server.model.drop.SetDropExecutor;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_HPMeter;
import com.lineage.server.serverpackets.S_PetMenuPacket;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_NPCPack_Summon;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.world.World;

/**
 * 召唤兽控制项
 * 
 * @author daien
 * 
 */
public class L1SummonInstance extends L1NpcInstance {

    private static final long serialVersionUID = 1L;

    private static final Log _log = LogFactory.getLog(L1SummonInstance.class);

    private static final int _summonTime = 3600;

    private int _currentPetStatus;

    private int _checkMove = 0;

    private boolean _tamed;

    private boolean _isReturnToNature = false;

    private static Random _random = new Random();

    public boolean tamed() {
        return this._tamed;
    }

    // ターゲットがいない场合の处理
    @Override
    public boolean noTarget() {
        switch (this._currentPetStatus) {
            case 3:// 休息
                return true;

            case 4:// 散开
                if ((_master != null)
                        && (_master.getMapId() == getMapId())
                        && (getLocation().getTileLineDistance(
                                _master.getLocation()) < 5)) {
                    if (_npcMove != null) {
                        int dir = _npcMove.targetReverseDirection(
                                _master.getX(), _master.getY());
                        dir = _npcMove.checkObject(dir);

                        _npcMove.setDirectionMove(dir);
                        setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
                    }

                } else {
                    _currentPetStatus = 3;
                    return true;
                }
                break;

            case 5:// 警戒
                if ((Math.abs(getHomeX() - getX()) > 1)
                        || (Math.abs(getHomeY() - getY()) > 1)) {
                    if (_npcMove != null) {
                        final int dir = _npcMove.moveDirection(getHomeX(),
                                getHomeY());
                        if (dir == -1) {
                            // ホームが离れすぎてたら现在地がホーム
                            setHomeX(getX());
                            setHomeY(getY());

                        } else {
                            _npcMove.setDirectionMove(dir);
                            setSleepTime(calcSleepTime(getPassispeed(),
                                    MOVE_SPEED));
                        }
                    }
                }
                break;

            default:
                if ((_master != null) && (_master.getMapId() == getMapId())) {
                    // 主人跟随
                    final int location = getLocation().getTileLineDistance(
                            _master.getLocation());
                    // System.out.println("主人跟随 距离: "+location);
                    if (location > 2) {
                        if (_npcMove != null) {
                            final int dir = _npcMove.moveDirection(
                                    _master.getX(), _master.getY());
                            // System.out.println("控制者遗失次数: "+_checkMove);
                            if (dir == -1) {
                                _checkMove++;
                                if (_checkMove >= 10) {
                                    // 控制者遗失
                                    // System.out.println("控制者遗失次数: "+_checkMove);
                                    _checkMove = 0;
                                    _currentPetStatus = 3;
                                    return true;
                                }

                            } else {
                                _checkMove = 0;
                                _npcMove.setDirectionMove(dir);
                                setSleepTime(calcSleepTime(getPassispeed(),
                                        MOVE_SPEED));
                            }
                        }
                    }

                } else {
                    // 控制者遗失
                    // System.out.println("控制者遗失else");
                    _currentPetStatus = 3;
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * 召唤兽
     * 
     * @param template
     * @param master
     */
    public L1SummonInstance(final L1Npc template, final L1Character master) {
        super(template);
        this.setId(IdFactoryNpc.get().nextId());
        // 副本编号
        this.set_showId(master.get_showId());

        this.set_time(_summonTime);
        /*
         * if (SummonTimer.MAP.get(this) == null) { SummonTimer.MAP.put(this,
         * _summonTime); }
         */

        this.setMaster(master);
        this.setX(master.getX() + _random.nextInt(5) - 2);
        this.setY(master.getY() + _random.nextInt(5) - 2);
        this.setMap(master.getMapId());
        this.setHeading(5);
        this.setLightSize(template.getLightSize());

        _currentPetStatus = 3;
        _tamed = false;

        World.get().storeObject(this);
        World.get().addVisibleObject(this);

        for (final L1PcInstance pc : World.get().getRecognizePlayer(this)) {
            this.onPerceive(pc);
        }
        master.addPet(this);
        if (master instanceof L1PcInstance) {
            // 增加物件组人
            addMaster((L1PcInstance) master);

        } else if (master instanceof L1NpcInstance) {
            // L1NpcInstance npc = (L1NpcInstance) master;
            // 增加物件组人
            // npc.broadcastPacketX10(new S_NewMaster(npc.getNameId(), this));
            _currentPetStatus = 1;
            // System.out.println("增加物件组人:"+npc.getNameId());
        }
    }

    /**
     * 造尸术
     * 
     * @param target
     * @param master
     * @param isCreateZombie
     */
    public L1SummonInstance(final L1NpcInstance target,
            final L1Character master, final boolean isCreateZombie) {
        super(null);
        this.setId(IdFactoryNpc.get().nextId());
        // 副本编号
        this.set_showId(master.get_showId());

        if (isCreateZombie) { // クリエイトゾンビ
            int npcId = 45065;
            final L1PcInstance pc = (L1PcInstance) master;
            final int level = pc.getLevel();
            if (pc.isWizard()) {
                if ((level >= 24) && (level <= 31)) {
                    npcId = 81183;
                } else if ((level >= 32) && (level <= 39)) {
                    npcId = 81184;
                } else if ((level >= 40) && (level <= 43)) {
                    npcId = 81185;
                } else if ((level >= 44) && (level <= 47)) {
                    npcId = 81186;
                } else if ((level >= 48) && (level <= 51)) {
                    npcId = 81187;
                } else if (level >= 52) {
                    npcId = 81188;
                }
            } else if (pc.isElf()) {
                if (level >= 48) {
                    npcId = 81183;
                }
            }
            final L1Npc template = NpcTable.get().getTemplate(npcId).clone();
            this.setting_template(template);

        } else { // テイミングモンスター
            this.setting_template(target.getNpcTemplate());
            this.setCurrentHpDirect(target.getCurrentHp());
            this.setCurrentMpDirect(target.getCurrentMp());
        }

        this.set_time(_summonTime);
        /*
         * if (SummonTimer.MAP.get(this) == null) { SummonTimer.MAP.put(this,
         * _summonTime); }
         */

        this.setMaster(master);
        this.setX(target.getX());
        this.setY(target.getY());
        this.setMap(target.getMapId());
        this.setHeading(target.getHeading());
        this.setLightSize(target.getLightSize());
        this.setPetcost(6);

        if ((target instanceof L1MonsterInstance)
                && !((L1MonsterInstance) target).is_storeDroped()) {
            // XXX
            final SetDropExecutor setDropExecutor = new SetDrop();
            setDropExecutor.setDrop(target, target.getInventory());
        }

        this.setInventory(target.getInventory());
        target.setInventory(null);

        this._currentPetStatus = 3;
        this._tamed = true;

        // ペットが攻击中だった场合止めさせる
        for (final L1NpcInstance each : master.getPetList().values()) {
            each.targetRemove(target);
        }

        target.deleteMe();
        World.get().storeObject(this);
        World.get().addVisibleObject(this);
        for (final L1PcInstance pc : World.get().getRecognizePlayer(this)) {
            this.onPerceive(pc);
        }
        master.addPet(this);
        if (master instanceof L1PcInstance) {
            // 增加物件组人
            addMaster((L1PcInstance) master);
        }
    }

    @Override
    public void receiveDamage(final L1Character attacker, final int damage) { // 攻击でＨＰを减らすときはここを使用
        ISASCAPE = false;
        if (this.getCurrentHp() > 0) {
            if (damage > 0) {
                this.setHate(attacker, 0); // サモンはヘイト无し
                this.removeSkillEffect(FOG_OF_SLEEPING);
                if (!this.isExsistMaster()) {
                    this._currentPetStatus = 1;
                    this.setTarget(attacker);
                }
            }

            if ((attacker instanceof L1PcInstance) && (damage > 0)) {
                final L1PcInstance player = (L1PcInstance) attacker;
                player.setPetTarget(this);
            }

            final int newHp = this.getCurrentHp() - damage;
            if (newHp <= 0) {
                this.Death(attacker);
            } else {
                this.setCurrentHp(newHp);
            }
        } else if (!this.isDead()) {// 念のため
            _log.error("NPC hp减少处理失败 可能原因: 初始hp为0(" + this.getNpcId() + ")");
            this.Death(attacker);
        }
    }

    /**
     * 死亡
     * 
     * @param lastAttacker
     */
    public synchronized void Death(final L1Character lastAttacker) {
        if (!this.isDead()) {
            this.setDead(true);
            this.setCurrentHp(0);
            this.setStatus(ActionCodes.ACTION_Die);

            this.getMap().setPassable(this.getLocation(), true);

            // 怪物解散处理
            L1Inventory targetInventory = null;// 主人的背包
            if (this._master != null) {
                if (this._master.getInventory() != null) {// 主人存在并且背包不为空
                    targetInventory = this._master.getInventory();
                }
            }

            final List<L1ItemInstance> items = this._inventory.getItems();
            for (final L1ItemInstance item : items) {
                if (targetInventory != null) {
                    // 容量重量确认及びメッセージ送信
                    if (this._master.getInventory().checkAddItem(item,
                            item.getCount()) == L1Inventory.OK) {
                        this._inventory.tradeItem(item, item.getCount(),
                                targetInventory);
                        // 143:\f1%0%s 给你 %1%o 。
                        ((L1PcInstance) this._master)
                                .sendPackets(new S_ServerMessage(143, this
                                        .getName(), item.getLogName()));

                    } else { // 超过持有物件数量(掉落地面)
                        item.set_showId(this.get_showId());
                        targetInventory = World.get().getInventory(this.getX(),
                                this.getY(), this.getMapId());
                        this._inventory.tradeItem(item, item.getCount(),
                                targetInventory);
                    }
                } else { // 主人遗失(掉落地面)
                    item.set_showId(this.get_showId());
                    targetInventory = World.get().getInventory(this.getX(),
                            this.getY(), this.getMapId());
                    this._inventory.tradeItem(item, item.getCount(),
                            targetInventory);
                }
            }

            if (this._tamed) {
                this.broadcastPacketAll(new S_DoActionGFX(this.getId(),
                        ActionCodes.ACTION_Die));
                this.startDeleteTimer(ConfigAlt.NPC_DELETION_TIME * 2);

            } else {
                this.deleteMe();
            }
        }
    }

    public synchronized void returnToNature() {
        this._isReturnToNature = true;
        if (!this._tamed) {
            this.getMap().setPassable(this.getLocation(), true);
            // 怪物解散处理
            L1Inventory targetInventory = null;// 主人的背包
            if (this._master != null) {
                if (this._master.getInventory() != null) {// 主人存在并且背包不为空
                    targetInventory = this._master.getInventory();
                }
            }
            final List<L1ItemInstance> items = this._inventory.getItems();
            for (final L1ItemInstance item : items) {
                if (targetInventory != null) {
                    // 容量重量确认及びメッセージ送信
                    if (this._master.getInventory().checkAddItem(item,
                            item.getCount()) == L1Inventory.OK) {
                        this._inventory.tradeItem(item, item.getCount(),
                                targetInventory);
                        // 143:\f1%0%s 给你 %1%o 。
                        ((L1PcInstance) this._master)
                                .sendPackets(new S_ServerMessage(143, this
                                        .getName(), item.getLogName()));

                    } else { // 超过持有物件数量(掉落地面)
                        item.set_showId(this.get_showId());
                        targetInventory = World.get().getInventory(this.getX(),
                                this.getY(), this.getMapId());
                        this._inventory.tradeItem(item, item.getCount(),
                                targetInventory);
                    }

                } else { // 主人遗失(掉落地面)
                    item.set_showId(this.get_showId());
                    targetInventory = World.get().getInventory(this.getX(),
                            this.getY(), this.getMapId());
                    this._inventory.tradeItem(item, item.getCount(),
                            targetInventory);
                }
            }
            this.deleteMe();

        } else {
            this.liberate();
        }
    }

    // オブジェクト消去处理
    @Override
    public synchronized void deleteMe() {
        if (_master != null) {
            _master.removePet(this);
        }
        if (_destroyed) {
            return;
        }
        if (!_tamed && !_isReturnToNature) {
            broadcastPacketX8(new S_SkillSound(getId(), 169));
        }
        // _master.getPetList().remove(getId());
        super.deleteMe();
    }

    // テイミングモンスター、クリエイトゾンビの时の解放处理
    public void liberate() {
//        final L1MonsterInstance monster = new L1MonsterInstance(
//                this.getNpcTemplate());
//        monster.setId(IdFactoryNpc.get().nextId());
//
//        monster.setX(this.getX());
//        monster.setY(this.getY());
//        monster.setMap(this.getMapId());
//        monster.setHeading(this.getHeading());
//        monster.set_storeDroped(true);
//        monster.setInventory(this.getInventory());
//        this.setInventory(null);
//        monster.setCurrentHpDirect(this.getCurrentHp());
//        monster.setCurrentMpDirect(this.getCurrentMp());
//        monster.setExp(0);
//
//        this.deleteMe();
//        World.get().storeObject(monster);
//        World.get().addVisibleObject(monster);
    	//迷魅怪物解散改为删除 //要恢复请删掉以下项恢复以上项 hjx1000
        this.getMap().setPassable(this.getLocation(), true);
        // 怪物解散处理
        L1Inventory targetInventory = null;// 主人的背包
        if (this._master != null) {
            if (this._master.getInventory() != null) {// 主人存在并且背包不为空
                targetInventory = this._master.getInventory();
            }
        }
        final List<L1ItemInstance> items = this._inventory.getItems();
        for (final L1ItemInstance item : items) {
            if (targetInventory != null) {
                // 容量重量确认及びメッセージ送信
                if (this._master.getInventory().checkAddItem(item,
                        item.getCount()) == L1Inventory.OK) {
                    this._inventory.tradeItem(item, item.getCount(),
                            targetInventory);
                    // 143:\f1%0%s 给你 %1%o 。
                    ((L1PcInstance) this._master)
                            .sendPackets(new S_ServerMessage(143, this
                                    .getName(), item.getLogName()));

                } else { // 超过持有物件数量(掉落地面)
                    item.set_showId(this.get_showId());
                    targetInventory = World.get().getInventory(this.getX(),
                            this.getY(), this.getMapId());
                    this._inventory.tradeItem(item, item.getCount(),
                            targetInventory);
                }

            } else { // 主人遗失(掉落地面)
                item.set_showId(this.get_showId());
                targetInventory = World.get().getInventory(this.getX(),
                        this.getY(), this.getMapId());
                this._inventory.tradeItem(item, item.getCount(),
                        targetInventory);
            }
        }
        this.deleteMe();
    }

    public void setTarget(final L1Character target) {
        if ((target != null)
                && ((this._currentPetStatus == 1)
                        || (this._currentPetStatus == 2) || (this._currentPetStatus == 5))) {
            this.setHate(target, 0);
            if (!this.isAiRunning()) {
                this.startAI();
            }
        }
    }

    /**
     * 设置主人目标
     * 
     * @param target
     */
    public void setMasterTarget(final L1Character target) {
        // System.out.println("设置主人目标");
        if ((target != null) && ((_currentPetStatus == 1) || // 攻击
                (_currentPetStatus == 5)// 警戒
                )) {
        	if (target instanceof L1PcInstance) { //修正安全区还会跟随主人攻击 hjx1000
        		if (target.isSafetyZone()) {
        			return;
        		}
        	}
            setHate(target, 10);
            if (!isAiRunning()) {
                startAI();
            }
        }
    }

    /**
     * 对该物件攻击的调用
     */
    @Override
    public void onAction(final L1PcInstance player) {
        if (player == null) {
            return;
        }
        final L1Character cha = this.getMaster();
        if (cha == null) {
            return;
        }
        final L1PcInstance master = (L1PcInstance) cha;
        if (master.isTeleport()) { // 传送处理中
            return;
        }
        if (master.equals(player)) {// 攻击者是主人
            final L1AttackMode attack_mortion = new L1AttackPc(player, this); // 攻击判断
            attack_mortion.action();
            return;
        }
        if ((this.isSafetyZone() || player.isSafetyZone())
                && this.isExsistMaster()) {// 安全区域中
            final L1AttackMode attack_mortion = new L1AttackPc(player, this); // 攻击判断
            attack_mortion.action();
            return;
        }

        if (player.checkNonPvP(player, this)) {
            return;
        }

        final L1AttackMode attack = new L1AttackPc(player, this);
        if (attack.calcHit()) {
            attack.calcDamage();
        }
        attack.action();
        attack.commit();
    }

    @Override
    public void onTalkAction(final L1PcInstance player) {
        if (this.isDead()) {
            return;
        }
        if (this._master.equals(player)) {
            player.sendPackets(new S_PetMenuPacket(this, 0));
        }
    }

    @Override
    public void onFinalAction(final L1PcInstance player, final String action) {
        final int status = Integer.parseInt(action);
        // int status = ActionType(action);
        switch (status) {
            case 0:
                return;

            case 6:// 解散
                if (this._tamed) {
                    // テイミングモンスター、クリエイトゾンビの解放
                    this.liberate();
                } else {
                    // サモンの解散
                    this.Death(null);
                }           	
                break;

            default:
                // 同じ主人のペットの状态をすべて更新
                final Object[] petList = this._master.getPetList().values()
                        .toArray();
                for (final Object petObject : petList) {
                    if (petObject instanceof L1SummonInstance) {
                        // サモンモンスター
                        final L1SummonInstance summon = (L1SummonInstance) petObject;
                        summon.set_currentPetStatus(status);

                    } else {
                        // ペット
                    }
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
            if (perceivedFrom.get_showId() != get_showId()) {
                return;
            }
            perceivedFrom.addKnownObject(this);
            perceivedFrom
                    .sendPackets(new S_NPCPack_Summon(this, perceivedFrom));
            if (getMaster() != null) {
                if (perceivedFrom.getId() == getMaster().getId()) {
                    perceivedFrom.sendPackets(new S_HPMeter(getId(), 100
                            * getCurrentHp() / getMaxHp()));
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void onItemUse() {
        if (!this.isActived()) {
            // １００％の确率でヘイストポーション使用
            this.useItem(USEITEM_HASTE, 100);
        }
        if (this.getCurrentHp() * 100 / this.getMaxHp() < 40) {
            // ＨＰが４０％きったら
            // １００％の确率で回复ポーション使用
            this.useItem(USEITEM_HEAL, 100);
        }
    }

    @Override
    public void onGetItem(final L1ItemInstance item) {
        if (this.getNpcTemplate().get_digestitem() > 0) {
            this.setDigestItem(item);
        }
        Arrays.sort(healPotions);
        Arrays.sort(haestPotions);
        if (Arrays.binarySearch(healPotions, item.getItem().getItemId()) >= 0) {
            if (this.getCurrentHp() != this.getMaxHp()) {
                this.useItem(USEITEM_HEAL, 100);
            }
        } else if (Arrays
                .binarySearch(haestPotions, item.getItem().getItemId()) >= 0) {
            this.useItem(USEITEM_HASTE, 100);
        }
    }

    @Override
    public void setCurrentHp(final int i) {
        final int currentHp = Math.min(i, this.getMaxHp());

        if (this.getCurrentHp() == currentHp) {
            return;
        }

        this.setCurrentHpDirect(currentHp);

        // 宠物血条更新
        if (this._master instanceof L1PcInstance) {
            final int hpRatio = 100 * currentHp / this.getMaxHp();
            final L1PcInstance master = (L1PcInstance) this._master;
            master.sendPackets(new S_HPMeter(this.getId(), hpRatio));
        }
    }

    @Override
    public void setCurrentMp(final int i) {
        final int currentMp = Math.min(i, this.getMaxMp());

        if (this.getCurrentMp() == currentMp) {
            return;
        }

        this.setCurrentMpDirect(currentMp);
    }

    public void set_currentPetStatus(final int i) {
        this._currentPetStatus = i;
        set_tempModel();
        switch (this._currentPetStatus) {
            case 5:
                this.setHomeX(this.getX());
                this.setHomeY(this.getY());
                break;

            case 3:
                this.allTargetClear();
                break;

            default:
                if (!this.isAiRunning()) {
                    this.startAI();
                }
                break;
        }
    }

    public int get_currentPetStatus() {
        return this._currentPetStatus;
    }

    /**
     * 是否具有主人
     * 
     * @return
     */
    public boolean isExsistMaster() {
        boolean isExsistMaster = true;
        if (this.getMaster() != null) {
            final String masterName = this.getMaster().getName();
            if (World.get().getPlayer(masterName) == null) {
                isExsistMaster = false;
            }
        }
        return isExsistMaster;
    }

    private int _time = 0;

    /**
     * 设置剩余使用时间
     * 
     * @return
     */
    public int get_time() {
        return _time;
    }

    /**
     * 剩余使用时间
     * 
     * @param time
     */
    public void set_time(final int time) {
        this._time = time;
    }

    private int _tempModel = 3;

    public void set_tempModel() {
        _tempModel = _currentPetStatus;
    }

    public void get_tempModel() {
        _currentPetStatus = _tempModel;
    }
}
