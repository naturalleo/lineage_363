package com.lineage.server.model.Instance;

import static com.lineage.server.model.skill.L1SkillId.FOG_OF_SLEEPING;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigOther;
import com.lineage.config.ConfigRate;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.ActionCodes;
import com.lineage.server.datatables.NpcScoreTable;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.UBTable;
import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1DragonSlayer;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.L1UltimateBattle;
import com.lineage.server.model.drop.DropShare;
import com.lineage.server.model.drop.DropShareExecutor;
import com.lineage.server.model.drop.SetDrop;
import com.lineage.server.model.drop.SetDropExecutor;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_NPCPack;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillBrave;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.timecontroller.npc.NpcWorkTimer;
import com.lineage.server.utils.CalcExp;
import com.lineage.server.utils.CheckUtil;
import com.lineage.server.world.World;

/**
 * 对象:mob 控制项
 * 
 * @author daien
 * 
 */
public class L1TDInstance extends L1NpcInstance {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    private static final Log _log = LogFactory.getLog(L1TDInstance.class);

    private static final Random _random = new Random();

    // private static final boolean _tkpc = false; // 追杀邪恶玩家

    private boolean _storeDroped; // 背包是否禁止加入掉落物品

    // アイテム使用处理
    @Override
    public void onItemUse() {
        if (!isActived() && (_target != null)) {
            useItem(USEITEM_HASTE, 40); // ４０％の确率でヘイストポーション使用

            // 变形怪 变身数据处理
            if (getNpcTemplate().is_doppel()
                    && (_target instanceof L1PcInstance)) {
                final L1PcInstance targetPc = (L1PcInstance) _target;
                setName(_target.getName());
                setNameId(_target.getName());
                setTitle(_target.getTitle());
                setTempLawful(_target.getLawful());
                setTempCharGfx(targetPc.getClassId());
                setGfxId(targetPc.getClassId());
                setPassispeed(640);
                setAtkspeed(900); // 正确な值がわからん
                for (final L1PcInstance pc : World.get().getRecognizePlayer(
                        this)) {
                    pc.sendPackets(new S_RemoveObject(this));
                    pc.removeKnownObject(this);
                    pc.updateObject();
                }
            }
        }
        if (getCurrentHp() * 100 / getMaxHp() < 40) { // ＨＰが４０％きったら
            useItem(USEITEM_HEAL, 50); // ５０％の确率で回复ポーション使用
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
            if (0 < getCurrentHp()) {
                if ((getHiddenStatus() == HIDDEN_STATUS_SINK)
                        || (getHiddenStatus() == HIDDEN_STATUS_ICE)) {
                    perceivedFrom.sendPackets(new S_DoActionGFX(getId(),
                            ActionCodes.ACTION_Hide));

                } else if (getHiddenStatus() == HIDDEN_STATUS_FLY) {
                    perceivedFrom.sendPackets(new S_DoActionGFX(getId(),
                            ActionCodes.ACTION_Moveup));
                }
                perceivedFrom.sendPackets(new S_NPCPack(this));
                onNpcAI(); // 启动AI
                if (this.getBraveSpeed() == 1) {// 具有勇水状态
                    perceivedFrom.sendPackets(new S_SkillBrave(getId(), 1,
                            600000));
                }

            } else {
                perceivedFrom.sendPackets(new S_NPCPack(this));
            }
//            if (!perceivedFrom.isAiRunning()) {//PC AI启动 hjx1000
//            	perceivedFrom.startAI();
//            	System.out.println("AI启动");
//            }


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

    private L1PcInstance searchTarget(L1TDInstance npc) {
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

            if (npc.getMapId() == 410) {// 魔族神殿的MOB
                // 忽略收到调职命令的小恶魔
                if (pc.getTempCharGfx() == 4261) {
                    continue;
                }
            }

            if (npc.getNpcTemplate().get_family() == NpcTable.ORC) {
                if (pc.getClan() != null) {
                    if (pc.getClan().getCastleId() == L1CastleLocation.OT_CASTLE_ID) {
                        continue;
                    }
                }
            }

            final L1PcInstance tgpc1 = npc.attackPc1(pc);
            if (tgpc1 != null) {
                targetPlayer = tgpc1;
                return targetPlayer;
            }

            final L1PcInstance tgpc2 = npc.attackPc2(pc);
            if (tgpc2 != null) {
                targetPlayer = tgpc2;
                return targetPlayer;
            }

            // どちらかの条件を满たす场合、友好と见なされ先制攻击されない。
            // ?モンスターのカルマがマイナス值（バルログ侧モンスター）でPCのカルマレベルが1以上（バルログ友好）
            // ?モンスターのカルマがプラス值（ヤヒ侧モンスター）でPCのカルマレベルが-1以下（ヤヒ友好）
            if (npc.getNpcTemplate().getKarma() < 0) {
                if (pc.getKarmaLevel() >= 1) {
                    continue;
                }
            }
            if (npc.getNpcTemplate().getKarma() > 0) {
                if (pc.getKarmaLevel() <= -1) {
                    continue;
                }
            }

            // 见弃てられた者たちの地 カルマクエストの变身中は、各阵営のモンスターから先制攻击されない
            if (pc.getTempCharGfx() == 6034) {
                if (npc.getNpcTemplate().getKarma() < 0) {
                    continue;
                }
            }
            if (pc.getTempCharGfx() == 6035) {
                if (npc.getNpcTemplate().getKarma() > 0) {
                    continue;
                }
                if (npc.getNpcTemplate().get_npcId() == 46070) {// 被抛弃的魔族
                    continue;
                }
                if (npc.getNpcTemplate().get_npcId() == 46072) {// 被抛弃的魔族
                    continue;
                }

            }

            // 邪恶玩家追杀
            final L1PcInstance tgpc = npc.targetPlayer1000(pc);
            if (tgpc != null) {
                targetPlayer = tgpc;
                return targetPlayer;
            }

            boolean isCheck = false;
            if (!pc.isInvisble()) {
                isCheck = true;
            }

            if (npc.getNpcTemplate().is_agrocoi()) {
                isCheck = true;
            }
            if (isCheck) { // インビジチェック
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
     * 攻击虚拟玩家
     */
    /*
     * private void tkDe() { for (final L1Object tg :
     * World.get().getVisibleObjects(this)) { try { Thread.sleep(2); } catch
     * (InterruptedException e) { _log.error(e.getLocalizedMessage(), e); } if
     * (tg instanceof L1DeInstance) { L1DeInstance tgDe = (L1DeInstance) tg; if
     * (tgDe.isDead()) { continue; } if (tgDe.getCurrentHp() <= 0) { continue; }
     * if (_random.nextBoolean()) { this._hateList.add(tgDe, 0); this._target =
     * tgDe; } } } }
     */

    /**
     * 克特
     * 
     * @param pc
     * @return
     */
    private L1PcInstance attackPc2(final L1PcInstance pc) {
        if (this.getNpcId() == 45600) { // 克特
            if (pc.isCrown()) {// 王族
                if (pc.getTempCharGfx() == pc.getClassId()) {
                    return pc;
                }
            }
            if (pc.isDarkelf()) {// 黑妖
                return pc;
            }
        }
        return null;
    }

    /**
     * 竞技场
     * 
     * @param pc
     * @return
     */
    private L1PcInstance attackPc1(final L1PcInstance pc) {
        final int mapId = this.getMapId();
        boolean isCheck = false;
        if (mapId == 88) {
            isCheck = true;
        }
        if (mapId == 98) {
            isCheck = true;
        }
        if (mapId == 92) {
            isCheck = true;
        }
        if (mapId == 91) {
            isCheck = true;
        }
        if (mapId == 95) {
            isCheck = true;
        }
        if (isCheck) {
            if (!pc.isInvisble() || this.getNpcTemplate().is_agrocoi()) { // インビジチェック
                return pc;
            }
        }
        return null;
    }

    /**
     * 邪恶玩家追杀
     * 
     * @param pc
     * @return
     */
    private L1PcInstance targetPlayer1000(final L1PcInstance pc) {
        if (ConfigOther.KILLRED) {
            if (!this.getNpcTemplate().is_agro()
                    && !this.getNpcTemplate().is_agrososc()
                    && (this.getNpcTemplate().is_agrogfxid1() < 0)
                    && (this.getNpcTemplate().is_agrogfxid2() < 0)) { // 完全なノンアクティブモンスター

                if (pc.getLawful() < -1000) { // プレイヤーがカオティック
                    return pc;
                }
            }
        }
        return null;
    }

    /**
     * 攻击目标设置
     */
    @Override
    public void setLink(final L1Character cha) {
        // 副本ID不相等
        if (this.get_showId() != cha.get_showId()) {
            return;
        }
        if ((cha != null) && this._hateList.isEmpty()) {
            this._hateList.add(cha, 0);
            this.checkTarget();
        }
    }

    public L1TDInstance(final L1Npc template) {
        super(template);
        this._storeDroped = false;
        this.setRest(true);
        try {
                final Class<?> cls = Class.forName("com.lineage.data.npc.other.Npc_TD");
                WORK = (NpcExecutor) cls.getMethod("get").invoke(null);
                if (WORK.workTime() != 0) {
                    // 加入NPC工作列队
                    NpcWorkTimer.put(this, WORK.workTime());

                } else {// 工作时间设置为0
                    // 执行一次
                    WORK.work(this);
                }
            }catch (final ClassNotFoundException e) {
            String error = "发生[TD NPC档案]错误, 检查档案是否存在: Npc_TD";
            _log.error(error);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }

    }

    @Override
    public void onNpcAI() {
        //System.out.println("===isAiRunning==="+this.isAiRunning());
    	synchronized(this) {
            if (this.isAiRunning()) {
                return;
            }
//            if (this.getNpcTemplate().get_hpr() == 100000) { //每次回血量为10W的怪不启动AI hjx1000
//            	return;
//            }
            if (!this._storeDroped) {// 背包是否加入掉落物品
                final SetDropExecutor setdrop = new SetDrop();
                setdrop.setDrop(this, this.getInventory());
                
                this.getInventory().shuffle();
                this._storeDroped = true;
            }
            this.setActived(false);
    		this.startAI();
    		//this.setAiRunning(true);
//    		try {
//    			Thread.sleep(1);
//    		} catch (InterruptedException e) {
//    			// TODO Auto-generated catch block
//    			e.printStackTrace();
//    		}
    	}
    }

    /**
     * 对话
     */
    @Override
    public void onTalkAction(final L1PcInstance pc) {
        // 改变面向
        this.setHeading(this.targetDirection(pc.getX(), pc.getY()));
        this.broadcastPacketAll(new S_ChangeHeading(this));

        // 动作暂停
        set_stop_time(REST_MILLISEC);
        this.setRest(true);
    }

    @Override
    public void onAction(final L1PcInstance pc) {
        if (ATTACK != null) {
            ATTACK.attack(pc, this);
        }
        if ((this.getCurrentHp() > 0) && !this.isDead()) {
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
        if ((mpDamage > 0) && !this.isDead()) {
            this.setHate(attacker, mpDamage);

            this.onNpcAI();

            // NPC互相帮助的判断
            if (attacker instanceof L1PcInstance) {
                this.serchLink((L1PcInstance) attacker, this.getNpcTemplate()
                        .get_family());
            }

            int newMp = this.getCurrentMp() - mpDamage;
            if (newMp < 0) {
                newMp = 0;
            }
            this.setCurrentMp(newMp);
        }
    }

    /**
     * 魔法具有属性伤害使用 (魔法抗性处理) attr:0.无属性魔法,1.地魔法,2.火魔法,4.水魔法,8.风魔法 (武器技能使用)
     * 
     * @param attacker
     * @param damage
     * @param attr
     */
    public void receiveDamage(final L1Character attacker, double damage,
            final int attr) {
        final int player_mr = this.getMr();
        final int rnd = _random.nextInt(300) + 1;
        if (player_mr >= rnd) {
            damage /= 2.01;
        }

        int resist = 0;
        switch (attr) {
            case L1Skills.ATTR_EARTH:
                resist = this.getEarth();
                break;

            case L1Skills.ATTR_FIRE:
                resist = this.getFire();
                break;

            case L1Skills.ATTR_WATER:
                resist = this.getWater();
                break;

            case L1Skills.ATTR_WIND:
                resist = this.getWind();
                break;
        }

        int resistFloor = (int) (0.32 * Math.abs(resist));
        if (resist >= 0) {
            resistFloor *= 1;

        } else {
            resistFloor *= -1;
        }

        final double attrDeffence = resistFloor / 32.0;

        double coefficient = (1.0 - attrDeffence + 3.0 / 32.0);

        if (coefficient > 0) {
            damage *= coefficient;
        }
        this.receiveDamage(attacker, (int) damage);
    }

    /**
     * 受攻击hp减少计算
     */
    @Override
    public void receiveDamage(L1Character attacker, int damage) {
        ISASCAPE = false;
        if ((this.getCurrentHp() > 0) && !this.isDead()) {
            if ((this.getHiddenStatus() == HIDDEN_STATUS_SINK)
                    || (this.getHiddenStatus() == HIDDEN_STATUS_FLY)) {
                return;
            }
            if (damage >= 0) {
                if (attacker instanceof L1EffectInstance) { // 效果不列入目标(设置主人为目标)
                    final L1EffectInstance effect = (L1EffectInstance) attacker;
                    attacker = effect.getMaster();
                    if (attacker != null) {
                        this.setHate(attacker, damage);
                    }

                } else if (attacker instanceof L1IllusoryInstance) { // 攻击者是分身不列入目标(设置主人为目标)
                    final L1IllusoryInstance ill = (L1IllusoryInstance) attacker;
                    attacker = ill.getMaster();
                    if (attacker != null) {
                        this.setHate(attacker, damage);
                    }
                } else if (attacker instanceof L1MonsterInstance) {// 魔法师．哈汀(故事)
                    switch (getNpcTemplate().get_npcId()) {
                        case 91290: // 镰刀死神的使者
                        case 91294: // 巴风特
                        case 91295: // 黑翼赛尼斯
                        case 91296: // 赛尼斯
                            this.setHate(attacker, damage);
                            damage = 0;
                            break;
                    }

                } else {
                    this.setHate(attacker, damage);
                }
            }

            if (damage > 0) {
                this.removeSkillEffect(FOG_OF_SLEEPING);
            }

            this.onNpcAI();

            L1PcInstance atkpc = null;
            // 攻击者昰PC
            if (attacker instanceof L1PcInstance) {
                atkpc = (L1PcInstance) attacker;
                if (damage > 0) {
                    atkpc.setPetTarget(this);
                    switch (getNpcTemplate().get_npcId()) {
                        case 45681: // 林德拜尔
                        case 45682: // 安塔瑞斯
                        case 45683: // 法利昂
                        case 45684: // 巴拉卡斯
                        case 81163: // 吉尔塔斯
                        case 65498: // 沙虫
                        case 92000: // 杰弗雷库(雌)
                        case 92001: // 杰弗雷库(雄)	
                            recall(atkpc);
                            break;
                    }
                }
                // NPC互相帮助的判断
                this.serchLink(atkpc, this.getNpcTemplate().get_family());
            }

            final int newHp = this.getCurrentHp() - damage;
            if ((newHp <= 0) && !this.isDead()) {
                final int transformId = this.getNpcTemplate().getTransformId();
                // 变身しないモンスター
                if (transformId == -1) {
                    if (this.getPortalNumber() != -1) {
                        if ((this.getNpcTemplate().get_npcId() == 71014)
                                || (this.getNpcTemplate().get_npcId() == 71026)) {
                            // 准备阶段二
                            L1DragonSlayer.getInstance().startDragonSlayer2rd(
                                    this.getPortalNumber());
                        } else if ((this.getNpcTemplate().get_npcId() == 71015)
                                || (this.getNpcTemplate().get_npcId() == 71027)) {
                            // 准备阶段三
                            L1DragonSlayer.getInstance().startDragonSlayer3rd(
                                    this.getPortalNumber());
                        } else if ((this.getNpcTemplate().get_npcId() == 71016)
                                || (this.getNpcTemplate().get_npcId() == 71028)) {
                            this.bloodstain();
                            // 结束屠龙副本
                            L1DragonSlayer.getInstance().endDragonSlayer(
                                    this.getPortalNumber());
                        }
                    }
                    this.setCurrentHpDirect(0);
                    this.setDead(true);
                    this.setStatus(ActionCodes.ACTION_Die);
                    openDoorWhenNpcDied(this);
                    final Death death = new Death(attacker);
                    GeneralThreadPool.get().execute(death);

                } else { // 变身するモンスター
                    // distributeExpDropKarma(attacker);
                    this.transform(transformId);
                }
            }
            if (newHp > 0) {
                this.setCurrentHp(newHp);
                this.hide();
            }
            // HP 显示设置
            if (ConfigOther.HPBAR && atkpc != null) {
                this.broadcastPacketHP(atkpc);
            }

        } else if (!this.isDead()) { // 念のため
            this.setDead(true);
            this.setStatus(ActionCodes.ACTION_Die);
            final Death death = new Death(attacker);
            GeneralThreadPool.get().execute(death);
            // Death(attacker);
        }
    }

    /**
     * NPC死亡开门的处理
     * 
     * @param npc
     */
    private static void openDoorWhenNpcDied(final L1NpcInstance npc) {
        final int[] npcId = { 46143, 46144, 46145, 46146, 46147, 46148, 46149,
                46150, 46151, 46152 };
        final int[] doorId = { 5001, 5002, 5003, 5004, 5005, 5006, 5007, 5008,
                5009, 5010 };

        for (int i = 0; i < npcId.length; i++) {
            if (npc.getNpcTemplate().get_npcId() == npcId[i]) {
                openDoorInCrystalCave(doorId[i]);
            }
        }
    }

    /**
     * 开门的处理
     * 
     * @param doorId
     */
    private static void openDoorInCrystalCave(final int doorId) {
        for (final L1Object object : World.get().getObject()) {
            if (object instanceof L1DoorInstance) {
                final L1DoorInstance door = (L1DoorInstance) object;
                if (door.getDoorId() == doorId) {
                    door.open();
                }
            }
        }
    }

    /**
     * 召回PC的处理(PC距离自身过远)
     * 
     * @param pc
     */
    private void recall(final L1PcInstance pc) {
        if (this.getMapId() != pc.getMapId()) {
            return;
        }
        if (this.getLocation().getTileLineDistance(pc.getLocation()) > 4) {
            for (int count = 0; count < 10; count++) {
                final L1Location newLoc = this.getLocation().randomLocation(3,
                        4, false);
                if (this.glanceCheck(newLoc.getX(), newLoc.getY())) {
                    L1Teleport.teleport(pc, newLoc.getX(), newLoc.getY(),
                            this.getMapId(), 5, true);
                    break;
                }
            }
        }
    }

    @Override
    public void setCurrentHp(final int i) {
        final int currentHp = Math.min(i, this.getMaxHp());

        if (this.getCurrentHp() == currentHp) {
            return;
        }

        this.setCurrentHpDirect(currentHp);
    }

    @Override
    public void setCurrentMp(final int i) {
        final int currentMp = Math.min(i, this.getMaxMp());

        if (this.getCurrentMp() == currentMp) {
            return;
        }

        this.setCurrentMpDirect(currentMp);
    }

    /**
     * 死亡判断
     * 
     * @author daien
     * 
     */
    class Death implements Runnable {

        L1Character _lastAttacker;// 攻击者

        /**
         * 死亡判断
         * 
         * @param lastAttacker
         *            攻击者
         */
        public Death(final L1Character lastAttacker) {
            this._lastAttacker = lastAttacker;
        }

        @Override
        public void run() {
            if (_lastAttacker instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) _lastAttacker;
                // 玩家杀怪数量
                pc.setKillMonstersNumber(pc.getKillMonstersNumber() + 1);
                pc.sendPackets(new S_OwnCharStatus(pc));
            }
            L1TDInstance mob = L1TDInstance.this;

            mob.setDeathProcessing(true);
            mob.setCurrentHpDirect(0);
            mob.setDead(true);
            mob.setStatus(ActionCodes.ACTION_Die);

            mob.broadcastPacketAll(new S_DoActionGFX(mob.getId(),
                    ActionCodes.ACTION_Die));

            // 解除旧座标障碍宣告
            mob.getMap().setPassable(mob.getLocation(), true);

            mob.startChat(CHAT_TIMING_DEAD);

            mob.distributeExpDropKarma(this._lastAttacker);
            mob.giveUbSeal();

            mob.setDeathProcessing(false);

            mob.setExp(0);
            mob.setKarma(0);
            mob.allTargetClear();
            mob.setPortalNumber(L1TDInstance.this.getPortalNumber());

            int deltime = 0;
            // 特定NPC死亡时间设置
            switch (mob.getNpcId()) {
                case 92000:// 杰弗雷库(雌)
                case 92001:// 杰弗雷库(雄)
                    deltime = 60;
                    break;

                default:
                    deltime = ConfigAlt.NPC_DELETION_TIME;
                	//mob.deleteMe();//怪死立刻删除
                    break;
            }
            mob.startDeleteTimer(deltime);
        }
    }

    /**
     * 判断主要攻击者(最后杀死NPC的人)
     * 
     * @param lastAttacker
     */
    private void distributeExpDropKarma(final L1Character lastAttacker) {
        if (lastAttacker == null) {
            return;
        }

        // 判断主要攻击者
        L1PcInstance pc = null;

        // NPC具有死亡判断设置
        if (DEATH != null) {
            pc = DEATH.death(lastAttacker, this);

        } else {
            // 判断主要攻击者
            pc = CheckUtil.checkAtkPc(lastAttacker);
        }

        if (pc != null) {
            final ArrayList<L1Character> targetList = _hateList
                    .toTargetArrayList();
            final ArrayList<Integer> hateList = _hateList.toHateArrayList();
            // 取回经验值
            final long exp = getExp();

            // 加入经验值与积分
            CalcExp.calcExp(pc, this.getId(), targetList, hateList, exp);
            final int score = NpcScoreTable.get().get_score(getNpcId());
            if (score > 0 && !isResurrect()) {
                // 3032 你得到了 %0 积分。
                pc.sendPackets(new S_ServerMessage("\\fX你得到了" + score + "积分"));
                pc.get_other().add_score(score);
            }

            // 死亡后续处理
            if (isDead()) {
                // 掉落物品分配
                distributeDrop();
                // 阵营
                giveKarma(pc);
            }
        }
    }

    /**
     * 掉落物品分配
     */
    private void distributeDrop() {
        final ArrayList<L1Character> dropTargetList = this._dropHateList
                .toTargetArrayList();
        final ArrayList<Integer> dropHateList = this._dropHateList
                .toHateArrayList();
        try {
            // 设置掉落物品
            final DropShareExecutor dropShareExecutor = new DropShare();
            dropShareExecutor.dropShare(L1TDInstance.this, dropTargetList,
                    dropHateList);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 阵营
     * 
     * @param pc
     */
    private void giveKarma(final L1PcInstance pc) {
        int karma = this.getKarma();
        if (karma != 0) {
            final int karmaSign = Integer.signum(karma);
            final int pcKarmaLevel = pc.getKarmaLevel();
            final int pcKarmaLevelSign = Integer.signum(pcKarmaLevel);
            // カルマ背信行为は5倍
            if ((pcKarmaLevelSign != 0) && (karmaSign != pcKarmaLevelSign)) {
                karma *= 5;
            }
            // カルマは止めを刺したプレイヤーに设定。ペットorサモンで倒した场合も入る。
            pc.addKarma((int) (karma * ConfigRate.RATE_KARMA));
        }
    }

    private void giveUbSeal() {
        if (this.getUbSealCount() != 0) { // UBの勇者の证
            final L1UltimateBattle ub = UBTable.getInstance().getUb(
                    this.getUbId());
            if (ub != null) {
                for (final L1PcInstance pc : ub.getMembersArray()) {
                    if ((pc != null) && !pc.isDead() && !pc.isGhost()) {
                        final L1ItemInstance item =
                        // 勇者的勋章(41402)
                        pc.getInventory().storeItem(41402,
                                this.getUbSealCount());
                        // 403 获得%0%o 。
                        pc.sendPackets(new S_ServerMessage(403, item
                                .getLogName()));
                    }
                }
            }
        }
    }

    /**
     * 背包是否禁止加入掉落物品
     * 
     * @return true:不加入 false:加入
     */
    public boolean is_storeDroped() {
        return this._storeDroped;
    }

    /**
     * 设置背包是否禁止加入掉落物品
     * 
     * @param flag
     *            true:不加入 false:加入
     */
    public void set_storeDroped(final boolean flag) {
        this._storeDroped = flag;
    }

    private int _ubSealCount = 0; // 无限大赛可获得的勇气之证数量

    /**
     * 给予勇气之证数量
     * 
     * @return
     */
    public int getUbSealCount() {
        return this._ubSealCount;
    }

    /**
     * 设置给予勇气之证数量
     * 
     * @param i
     */
    public void setUbSealCount(final int i) {
        this._ubSealCount = i;
    }

    private int _ubId = 0; // UBID

    /**
     * UBID
     * 
     * @return
     */
    public int getUbId() {
        return this._ubId;
    }

    /**
     * UBID
     * 
     * @param i
     */
    public void setUbId(final int i) {
        this._ubId = i;
    }

    /**
     * 一定机率躲藏
     */
    private void hide() {
        final int npcid = this.getNpcTemplate().get_npcId();
        switch (npcid) {
            case 45061: // 弱化史巴托
            case 45161: // 史巴托
            case 45181: // 史巴托
            case 45455: // 残暴的史巴托
                if (this.getMaxHp() / 3 > this.getCurrentHp()) {
                    final int rnd = _random.nextInt(10);
                    if (1 > rnd) {
                        this.allTargetClear();
                        this.setHiddenStatus(HIDDEN_STATUS_SINK);
                        this.broadcastPacketAll(new S_DoActionGFX(this.getId(),
                                ActionCodes.ACTION_Hide));
                        this.setStatus(13);
                        this.broadcastPacketAll(new S_NPCPack(this));
                    }
                }
                break;

            case 45682: // 安塔瑞斯
                if (this.getMaxHp() / 3 > this.getCurrentHp()) {
                    final int rnd = _random.nextInt(50);
                    if (1 > rnd) {
                        this.allTargetClear();
                        this.setHiddenStatus(HIDDEN_STATUS_SINK);
                        this.broadcastPacketAll(new S_DoActionGFX(this.getId(),
                                ActionCodes.ACTION_AntharasHide));
                        this.setStatus(20);
                        this.broadcastPacketAll(new S_NPCPack(this));
                    }
                }
                break;

            case 45067: // 弱化哈维
            case 45264: // 哈维
            case 45452: // 哈维
            case 45090: // 弱化格利芬
            case 45321: // 格利芬
            case 45445: // 格利芬
                if (this.getMaxHp() / 3 > this.getCurrentHp()) {
                    final int rnd = _random.nextInt(10);
                    if (1 > rnd) {
                        this.allTargetClear();
                        this.setHiddenStatus(HIDDEN_STATUS_FLY);
                        this.broadcastPacketAll(new S_DoActionGFX(this.getId(),
                                ActionCodes.ACTION_Moveup));
                        this.setStatus(4);
                        this.broadcastPacketAll(new S_NPCPack(this));
                    }
                }
                break;
            /*
             * case 45681: // 林德拜尔 XXX 暂时移除躲藏 if (this.getMaxHp() / 3 >
             * this.getCurrentHp()) { final int rnd = _random.nextInt(50); if (1
             * > rnd) { this.allTargetClear();
             * this.setHiddenStatus(HIDDEN_STATUS_FLY); this.broadcastPacket(new
             * S_DoActionGFX(this.getId(), ActionCodes.ACTION_Moveup));
             * this.setStatus(11); this.broadcastPacket(new S_NPCPack(this)); }
             * }
             */

            case 46107: // 底比斯 曼陀罗草(白)
            case 46108: // 底比斯 曼陀罗草(黑)
                if (this.getMaxHp() / 4 > this.getCurrentHp()) {
                    final int rnd = _random.nextInt(10);
                    if (1 > rnd) {
                        this.allTargetClear();
                        this.setHiddenStatus(HIDDEN_STATUS_SINK);
                        this.broadcastPacketAll(new S_DoActionGFX(this.getId(),
                                ActionCodes.ACTION_Hide));
                        this.setStatus(13);
                        this.broadcastPacketAll(new S_NPCPack(this));
                    }
                }
                break;
        }
    }

    /**
     * 召唤后隐藏
     */
    public void initHide() {
        // 出现直后の隐れる动作
        // 潜るMOBは一定の确率で地中に潜った状态に、
        // 飞ぶMOBは飞んだ状态にしておく
        final int npcid = this.getNpcTemplate().get_npcId();
        final int rnd = _random.nextInt(3);
        switch (npcid) {
            case 45061: // 弱化史巴托
            case 45161: // 史巴托
            case 45181: // 史巴托
            case 45455: // 残暴的史巴托
                if (1 > rnd) {
                    this.setHiddenStatus(HIDDEN_STATUS_SINK);
                    this.setStatus(13);
                }
                break;

            case 45045: // 弱化高仑石头怪
            case 45126: // 高仑石头怪
            case 45134: // 高仑石头怪
            case 45281: // 奇岩 高仑石头怪
                if (1 > rnd) {
                    this.setHiddenStatus(HIDDEN_STATUS_SINK);
                    this.setStatus(4);
                }
                break;

            case 45067: // 弱化哈维
            case 45264: // 哈维
            case 45452: // 哈维
            case 45090: // 弱化格利芬
            case 45321: // 格利芬
            case 45445: // 格利芬
                this.setHiddenStatus(HIDDEN_STATUS_FLY);
                this.setStatus(4);
                break;

            case 45681: // 林德拜尔
                this.setHiddenStatus(HIDDEN_STATUS_FLY);
                this.setStatus(11);
                break;

            case 46107: // 底比斯 曼陀罗草(白)
            case 46108: // 底比斯 曼陀罗草(黑)
                if (1 > rnd) {
                    this.setHiddenStatus(HIDDEN_STATUS_SINK);
                    this.setStatus(13);
                }
                break;

            case 46125:// 高仑钢铁怪
            case 46126:// 莱肯
            case 46127:// 欧熊
            case 46128:// 冰原老虎
                this.setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_ICE);
                this.setStatus(4);
                break;
        }
    }

    public void initHideForMinion(final L1NpcInstance leader) {
        // グループに属するモンスターの出现直后の隐れる动作（リーダーと同じ动作にする）
        final int npcid = this.getNpcTemplate().get_npcId();
        if (leader.getHiddenStatus() == HIDDEN_STATUS_SINK) {
            switch (npcid) {
                case 45061: // カーズドスパルトイ
                case 45161: // スパルトイ
                case 45181: // スパルトイ
                case 45455: // デッドリースパルトイ
                    this.setHiddenStatus(HIDDEN_STATUS_SINK);
                    this.setStatus(13);
                    break;
                case 45045: // クレイゴーレム
                case 45126: // ストーンゴーレム
                case 45134: // ストーンゴーレム
                case 45281: // ギランストーンゴーレム
                    this.setHiddenStatus(HIDDEN_STATUS_SINK);
                    this.setStatus(4);
                    break;
                case 46107: // テーベ マンドラゴラ(白)
                case 46108: // テーベ マンドラゴラ(黑)
                    this.setHiddenStatus(HIDDEN_STATUS_SINK);
                    this.setStatus(13);
                    break;
            }
        } else if (leader.getHiddenStatus() == HIDDEN_STATUS_FLY) {
            switch (npcid) {
                case 45067: // バレーハーピー
                case 45264: // ハーピー
                case 45452: // ハーピー
                case 45090: // バレーグリフォン
                case 45321: // グリフォン
                case 45445: // グリフォン
                    this.setHiddenStatus(HIDDEN_STATUS_FLY);
                    this.setStatus(4);
                    break;
                case 45681: // 林德拜尔
                    this.setHiddenStatus(HIDDEN_STATUS_FLY);
                    this.setStatus(11);
                    break;
                case 46125:
                case 46126:
                case 46127:
                case 46128:
                    this.setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_ICE);
                    this.setStatus(4);
                    break;
            }
        }
    }

    @Override
    protected void transform(final int transformId) {
        super.transform(transformId);
        // DROPの再设定
        this.getInventory().clearItems();
        // XXX
        final SetDropExecutor setDropExecutor = new SetDrop();
        setDropExecutor.setDrop(this, this.getInventory());
        // DropTable.getInstance().setDrop(this, getInventory());
        this.getInventory().shuffle();
    }
    /** 龙之血痕 */
    private void bloodstain() {
        for (final L1PcInstance pc : World.get().getVisiblePlayer(
                this, 50)) {
            if (this.getNpcTemplate().get_npcId() == 71016) { // 安塔瑞斯 第三阶段
                pc.sendPackets(new S_ServerMessage(1580)); // 安塔瑞斯：黑暗的诅咒将会降临到你们身上！席琳，
                                                           // 我的母亲，请让我安息吧...
                L1BuffUtil.bloodstain(pc, (byte) 0, 259200, true);
            } else if (this.getNpcTemplate().get_npcId() == 71028) { // 法利昂 第三阶段
                pc.sendPackets(new S_ServerMessage(1668)); // 法利昂：莎尔...你这个家伙...怎么...对得起我的母亲...席琳啊...请拿走...我的生命吧...
                L1BuffUtil.bloodstain(pc, (byte) 1, 259200, true);
            }
        }
    }
}
