package com.lineage.server.model.Instance;

import static com.lineage.server.model.skill.L1SkillId.FOG_OF_SLEEPING;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigRate;
import com.lineage.server.ActionCodes;
import com.lineage.server.datatables.NPCTalkDataTable;
import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1NpcTalkData;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.drop.DropShare;
import com.lineage.server.model.drop.DropShareExecutor;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_NpcChat;
import com.lineage.server.serverpackets.S_NpcChatShouting;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.types.Point;
import com.lineage.server.utils.CalcExp;
import com.lineage.server.world.World;

/**
 * 对象:精灵守护神 控制项
 * 
 * @author daien
 * 
 */
public class L1GuardianInstance extends L1NpcInstance {
    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    private static final Log _log = LogFactory.getLog(L1GuardianInstance.class);

    private Random _random = new Random();
    private L1GuardianInstance _npc = this;

    /**
     * @param template
     */
    public L1GuardianInstance(final L1Npc template) {
        super(template);
    }

    /**
     * 目标搜寻
     */
    @Override
    public void searchTarget() {
        // 目标搜寻
        L1PcInstance targetPlayer = searchTarget(this);

        if (targetPlayer != null) {
            this._hateList.add(targetPlayer, 0);
            this._target = targetPlayer;
        }
    }

    private static L1PcInstance searchTarget(L1GuardianInstance npc) {
        L1PcInstance targetPlayer = null;

        for (final L1PcInstance pc : World.get().getVisiblePlayer(npc)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                _log.error(e.getLocalizedMessage(), e);
            }
            if ((pc.getCurrentHp() <= 0) || pc.isDead() || pc.isGm()
                    || pc.isGhost()) {
                continue;
            }

            // 副本ID不相等
            if (npc.get_showId() != pc.get_showId()) {
                continue;
            }
            if (!pc.isInvisble() || npc.getNpcTemplate().is_agrocoi()) { // インビジチェック
                if (!pc.isElf()) { // エルフ以外
                    targetPlayer = pc;
                    // $804 人类，如果你重视你的生命现在就快离开这神圣的地方。
                    npc.wideBroadcastPacket(new S_NpcChatShouting(npc, "$804"));
                    break;

                } else if (pc.isElf() && pc.isWantedForElf()) {
                    targetPlayer = pc;
                    // $815 若杀害同族，必须以自己的生命赎罪。
                    npc.wideBroadcastPacket(new S_NpcChat(npc, "$815"));
                    break;
                }
            }
        }
        return targetPlayer;
    }
    
    /**
     * 添加妖精森林NPC无目标处理防卡定安特挂水果树枝 hjx1000
     */
    @Override
    public boolean noTarget() {
        if (this.getLocation().getTileLineDistance(
                new Point(getHomeX(), getHomeY())) > 0) {
            if (_npcMove != null) {
                final int dir = _npcMove.moveDirection(getHomeX(), getHomeY());
                if (dir != -1) {
                    _npcMove.setDirectionMove(dir);
                    setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));

                } else {
                    teleport(getHomeX(), getHomeY(), 1);
                }
            }

        } else {
            if (World.get().getRecognizePlayer(this).size() == 0) {
                return true; // 周りにプレイヤーがいなくなったらＡＩ处理终了
            }
        }
        return false;
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

    @Override
    public void onNpcAI() {
        if (this.isAiRunning()) {
            return;
        }
        this.setActived(false);
        this.startAI();
    }

    @Override
    public void onAction(final L1PcInstance player) {
        try {
            if ((player.getType() == 2) && (player.getCurrentWeapon() == 0)
                    && player.isElf()) {
                final L1AttackMode attack = new L1AttackPc(player, this);

                if (attack.calcHit()) {
                    if (this.getNpcTemplate().get_npcId() == 70848) { // エント
                        final int chance = this._random.nextInt(100) + 1;
                        if (chance <= 5) {
                            player.getInventory().storeItem(40506, 1);
                            player.sendPackets(new S_ServerMessage(143, "$755",
                                    "$794")); // \f1%0が%1をくれました。
                        } else if ((chance <= 60) && (chance > 10)) {
                            player.getInventory().storeItem(40507, 1);
                            player.sendPackets(new S_ServerMessage(143, "$755",
                                    "$763")); // \f1%0が%1をくれました。
                        } else if ((chance <= 70) && (chance > 60)) {
                            player.getInventory().storeItem(40505, 1);
                            player.sendPackets(new S_ServerMessage(143, "$755",
                                    "$770")); // \f1%0が%1をくれました。
                        }
                    }
                    if (this.getNpcTemplate().get_npcId() == 70850) { // パン
                        final int chance = this._random.nextInt(100) + 1;
                        if (chance <= 30) {
                            player.getInventory().storeItem(40519, 5);
                            player.sendPackets(new S_ServerMessage(143, "$753",
                                    "$760" + " (" + 5 + ")")); // \f1%0が%1をくれました。
                        }
                    }
                    if (this.getNpcTemplate().get_npcId() == 70846) { // アラクネ
                        final int chance = this._random.nextInt(100) + 1;
                        if (chance <= 30) {
                            player.getInventory().storeItem(40503, 1);
                            player.sendPackets(new S_ServerMessage(143, "$752",
                                    "$769")); // \f1%0が%1をくれました。
                        }
                    }
                    attack.calcDamage();
                    attack.calcStaffOfMana();
                    // attack.addPcPoisonAttack(player, this);
                    attack.addChaserAttack();
                }
                attack.action();
                attack.commit();
            } else if ((this.getCurrentHp() > 0) && !this.isDead()) {
                final L1AttackMode attack = new L1AttackPc(player, this);
                if (attack.calcHit()) {
                    attack.calcDamage();
                    attack.calcStaffOfMana();
                    attack.addChaserAttack();
                }
                attack.action();
                attack.commit();
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void onTalkAction(final L1PcInstance player) {
        final int objid = this.getId();
        final L1NpcTalkData talking = NPCTalkDataTable.get().getTemplate(
                this.getNpcTemplate().get_npcId());
        final L1Object object = World.get().findObject(this.getId());
        final L1NpcInstance target = (L1NpcInstance) object;
        // final String htmlid = null;
        // final String[] htmldata = null;

        if (talking != null) {
            final int pcx = player.getX(); // PCのX座标
            final int pcy = player.getY(); // PCのY座标
            final int npcx = target.getX(); // NPCのX座标
            final int npcy = target.getY(); // NPCのY座标

            if ((pcx == npcx) && (pcy < npcy)) {
                this.setHeading(0);

            } else if ((pcx > npcx) && (pcy < npcy)) {
                this.setHeading(1);

            } else if ((pcx > npcx) && (pcy == npcy)) {
                this.setHeading(2);

            } else if ((pcx > npcx) && (pcy > npcy)) {
                this.setHeading(3);

            } else if ((pcx == npcx) && (pcy > npcy)) {
                this.setHeading(4);

            } else if ((pcx < npcx) && (pcy > npcy)) {
                this.setHeading(5);

            } else if ((pcx < npcx) && (pcy == npcy)) {
                this.setHeading(6);

            } else if ((pcx < npcx) && (pcy < npcy)) {
                this.setHeading(7);
            }
            this.broadcastPacketAll(new S_ChangeHeading(this));

            if (player.getLawful() < -1000) { // プレイヤーがカオティック
                player.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
            } else {
                player.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
            }
            // html表示パケット送信
            /*
             * if (htmlid != null) { // htmlidが指定されている场合 if (htmldata != null) {
             * // html指定がある场合は表示 player.sendPackets(new S_NPCTalkReturn(objid,
             * htmlid, htmldata)); } else { player.sendPackets(new
             * S_NPCTalkReturn(objid, htmlid)); }
             * 
             * } else { if (player.getLawful() < -1000) { // プレイヤーがカオティック
             * player.sendPackets(new S_NPCTalkReturn(talking, objid, 2)); }
             * else { player.sendPackets(new S_NPCTalkReturn(talking, objid,
             * 1)); } }
             */

            // 动作暂停
            set_stop_time(REST_MILLISEC);
            this.setRest(true);
        }
    }

    /**
     * 受攻击hp减少计算
     */
    @Override
    public void receiveDamage(L1Character attacker, final int damage) { // 攻击でＨＰを减らすときはここを使用
        ISASCAPE = false;
        if ((attacker instanceof L1PcInstance) && (damage > 0)) {
            final L1PcInstance pc = (L1PcInstance) attacker;
            if ((pc.getType() == 2) && // 素手ならダメージなし
                    (pc.getCurrentWeapon() == 0)) {
            } else {
                if ((this.getCurrentHp() > 0) && !this.isDead()) {
                    if (damage >= 0) {
                        if (attacker instanceof L1EffectInstance) { // 效果不列入目标
                            // this.setHate(attacker, damage);

                        } else if (attacker instanceof L1IllusoryInstance) { // 攻击者是分身不列入目标(设置主人为目标)
                            L1IllusoryInstance ill = (L1IllusoryInstance) attacker;
                            attacker = ill.getMaster();
                            this.setHate(attacker, damage);

                        } else {
                            this.setHate(attacker, damage);
                        }
                        // this.setHate(attacker, damage);
                    }
                    if (damage > 0) {
                        this.removeSkillEffect(FOG_OF_SLEEPING);
                    }
                    this.onNpcAI();
                    // 互相帮助的判断
                    this.serchLink(pc, this.getNpcTemplate().get_family());
                    if (damage > 0) {
                        pc.setPetTarget(this);
                    }

                    final int newHp = this.getCurrentHp() - damage;
                    if ((newHp <= 0) && !this.isDead()) {
                        this.setCurrentHpDirect(0);
                        this.setDead(true);
                        this.setStatus(ActionCodes.ACTION_Die);

                        final Death death = new Death(attacker);
                        GeneralThreadPool.get().execute(death);
                    }
                    if (newHp > 0) {
                        this.setCurrentHp(newHp);
                    }
                } else if (!this.isDead()) { // 念のため
                    this.setDead(true);
                    this.setStatus(ActionCodes.ACTION_Die);

                    final Death death = new Death(attacker);
                    GeneralThreadPool.get().execute(death);
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

        /*
         * if (this.getMaxHp() > this.getCurrentHp()) {
         * this.startHpRegeneration(); }
         */
    }

    @Override
    public void setCurrentMp(final int i) {
        final int currentMp = Math.min(i, this.getMaxMp());

        if (this.getCurrentMp() == currentMp) {
            return;
        }

        this.setCurrentMpDirect(currentMp);

        /*
         * if (this.getMaxMp() > this.getCurrentMp()) {
         * this.startMpRegeneration(); }
         */
    }

    /**
     * 死亡判断
     * 
     * @author daien
     * 
     */
    class Death implements Runnable {

        L1Character _lastAttacker;

        /**
         * 死亡判断
         * 
         * @param lastAttacker
         *            攻击者
         */
        public Death(L1Character lastAttacker) {
            _lastAttacker = lastAttacker;
        }

        @Override
        public void run() {
            L1GuardianInstance.this.setDeathProcessing(true);
            L1GuardianInstance.this.setCurrentHpDirect(0);
            L1GuardianInstance.this.setDead(true);
            L1GuardianInstance.this.setStatus(ActionCodes.ACTION_Die);
            final int targetobjid = L1GuardianInstance.this.getId();
            L1GuardianInstance.this.getMap().setPassable(
                    L1GuardianInstance.this.getLocation(), true);
            L1GuardianInstance.this.broadcastPacketAll(new S_DoActionGFX(
                    targetobjid, ActionCodes.ACTION_Die));

            L1PcInstance player = null;

            // 判断主要攻击者
            if (_lastAttacker instanceof L1PcInstance) {// 攻击者是玩家
                player = (L1PcInstance) _lastAttacker;

            } else if (_lastAttacker instanceof L1PetInstance) {// 攻击者是宠物
                player = (L1PcInstance) ((L1PetInstance) _lastAttacker)
                        .getMaster();

            } else if (_lastAttacker instanceof L1SummonInstance) {// 攻击者是 召换兽
                player = (L1PcInstance) ((L1SummonInstance) _lastAttacker)
                        .getMaster();

            } else if (_lastAttacker instanceof L1IllusoryInstance) {// 攻击者是 分身
                player = (L1PcInstance) ((L1IllusoryInstance) _lastAttacker)
                        .getMaster();

            } else if (_lastAttacker instanceof L1EffectInstance) {// 攻击者是 技能物件
                player = (L1PcInstance) ((L1EffectInstance) _lastAttacker)
                        .getMaster();
            }

            if (player != null) {
                final ArrayList<L1Character> targetList = L1GuardianInstance.this._hateList
                        .toTargetArrayList();
                final ArrayList<Integer> hateList = L1GuardianInstance.this._hateList
                        .toHateArrayList();
                final long exp = L1GuardianInstance.this.getExp();
                CalcExp.calcExp(player, targetobjid, targetList, hateList, exp);

                final ArrayList<L1Character> dropTargetList = L1GuardianInstance.this._dropHateList
                        .toTargetArrayList();
                final ArrayList<Integer> dropHateList = L1GuardianInstance.this._dropHateList
                        .toHateArrayList();
                try {
                    // XXX
                    final DropShareExecutor dropShareExecutor = new DropShare();
                    dropShareExecutor.dropShare(L1GuardianInstance.this._npc,
                            dropTargetList, dropHateList);

                } catch (final Exception e) {
                    _log.error(e.getLocalizedMessage(), e);
                }
                // カルマは止めを刺したプレイヤーに设定。ペットorサモンで倒した场合も入る。
                player.addKarma((int) (L1GuardianInstance.this.getKarma() * ConfigRate.RATE_KARMA));
            }
            L1GuardianInstance.this.setDeathProcessing(false);

            L1GuardianInstance.this.setKarma(0);
            L1GuardianInstance.this.setExp(0);
            L1GuardianInstance.this.allTargetClear();

            L1GuardianInstance.this
                    .startDeleteTimer(ConfigAlt.NPC_DELETION_TIME);
        }
    }

    @Override
    public void onFinalAction(final L1PcInstance player, final String action) {
    }

    public void doFinalAction(final L1PcInstance player) {
    }
}
