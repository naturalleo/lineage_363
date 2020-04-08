package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.SHOCK_STUN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.ActionCodes;
import com.lineage.server.datatables.MobSkillTable;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1MobSkill;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.World;

public class L1MobSkillUse {

    private static final Log _log = LogFactory.getLog(L1MobSkillUse.class);

    private L1MobSkill _mobSkillTemplate = null;

    private L1NpcInstance _attacker = null;

    private L1Character _target = null;

    private static final Random _rnd = new Random();

    private int _sleepTime = 0;

    private int _skillUseCount[];

    public L1MobSkillUse(final L1NpcInstance npc) {
        try {
            _sleepTime = 0;

            _mobSkillTemplate = MobSkillTable.getInstance().getTemplate(
                    npc.getNpcTemplate().get_npcId());
            if (_mobSkillTemplate == null) {
                return;
            }
            _attacker = npc;
            _skillUseCount = new int[getMobSkillTemplate().getSkillSize()];

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private int getSkillUseCount(final int idx) {
        return _skillUseCount[idx];
    }

    private void skillUseCountUp(final int idx) {
        _skillUseCount[idx]++;
    }

    public void resetAllSkillUseCount() {
        if (getMobSkillTemplate() == null) {
            return;
        }

        for (int i = 0; i < getMobSkillTemplate().getSkillSize(); i++) {
            _skillUseCount[i] = 0;// 使用次数归0
        }
    }

    public int getSleepTime() {
        return _sleepTime;
    }

    public void setSleepTime(final int i) {
        _sleepTime = i;
    }

    public L1MobSkill getMobSkillTemplate() {
        return _mobSkillTemplate;
    }

    /*
     * トリガーの条件のみチェック。
     */
    public boolean isSkillTrigger(final L1Character tg) {
        if (_mobSkillTemplate == null) {
            return false;
        }
        _target = tg;

        int type;
        type = getMobSkillTemplate().getType(0);

        if (type == L1MobSkill.TYPE_NONE) {
            return false;
        }

        int i = 0;
        for (i = 0; (i < getMobSkillTemplate().getSkillSize())
                && (this.getMobSkillTemplate().getType(i) != L1MobSkill.TYPE_NONE); i++) {

            // changeTargetが设定されている场合、ターゲットの入れ替え
            final int changeType = getMobSkillTemplate().getChangeTarget(i);
            if (changeType > 0) {
                _target = changeTarget(changeType, i);

            } else {
                // 设定されてない场合は本来のターゲットにする
                _target = tg;
            }

            if (isSkillUseble(i, false)) {
                return true;
            }
        }
        return false;
    }

    /*
     * スキル攻击 スキル攻击可能ならばtrueを返す。 攻击できなければfalseを返す。
     */
    public boolean skillUse(final L1Character tg, final boolean isTriRnd) {
        if (_mobSkillTemplate == null) {
            return false;
        }
        _target = tg;

        int type;
        type = getMobSkillTemplate().getType(0);

        if (type == L1MobSkill.TYPE_NONE) {
            return false;
        }

        int[] skills = null;
        int skillSizeCounter = 0;
        final int skillSize = getMobSkillTemplate().getSkillSize();
        if (skillSize >= 0) {
            skills = new int[skillSize];
        }

        int i = 0;
        for (i = 0; (i < getMobSkillTemplate().getSkillSize())
                && (getMobSkillTemplate().getType(i) != L1MobSkill.TYPE_NONE); i++) {

            // changeTargetが设定されている场合、ターゲットの入れ替え
            final int changeType = getMobSkillTemplate().getChangeTarget(i);
            if (changeType > 0) {
                _target = this.changeTarget(changeType, i);
            } else {
                // 设定されてない场合は本来のターゲットにする
                _target = tg;
            }

            if (isSkillUseble(i, isTriRnd) == false) {
                continue;

            } else { // 条件にあうスキルが存在する
                skills[skillSizeCounter] = i;
                skillSizeCounter++;
            }
        }

        if (skillSizeCounter != 0) {
            final int num = _rnd.nextInt(skillSizeCounter);
            if (useSkill(skills[num])) { // スキル使用
                return true;
            }
        }

        return false;
    }

    private boolean useSkill(final int i) {
        boolean isUseSkill = false;
        final int type = getMobSkillTemplate().getType(i);
        switch (type) {
            case L1MobSkill.TYPE_PHYSICAL_ATTACK: // 物理攻击
                if (physicalAttack(i) == true) {
                    skillUseCountUp(i);
                    isUseSkill = true;
                }
                break;

            case L1MobSkill.TYPE_MAGIC_ATTACK: // 魔法攻击
                if (magicAttack(i) == true) {
                    skillUseCountUp(i);
                    isUseSkill = true;
                }
                break;

            case L1MobSkill.TYPE_SUMMON: // サモンする
                if (summon(i) == true) {
                    skillUseCountUp(i);
                    isUseSkill = true;
                }
                break;

            case L1MobSkill.TYPE_POLY: // 强制变身させる
                if (poly(i) == true) {
                    skillUseCountUp(i);
                    isUseSkill = true;
                }
                break;

            case L1MobSkill.AHTHARAS_1: // 群体冲晕
                // System.out.println("群体冲晕");
                if (shock_stun(i) == true) {
                    skillUseCountUp(i);
                    isUseSkill = true;
                }
                break;

            case L1MobSkill.AHTHARAS_2: // 群体相消
                // System.out.println("群体相消");
                if (cancellation(i) == true) {
                    skillUseCountUp(i);
                    isUseSkill = true;
                }
                break;

            case L1MobSkill.AHTHARAS_3: // 群体坏物术
                // System.out.println("群体坏物");
                if (weapon_break(i) == true) {
                    skillUseCountUp(i);
                    isUseSkill = true;
                }
                break;
        }
        return isUseSkill;
    }

    /**
     * 群体坏物
     * 
     * @param idx
     * @return
     */
    private boolean weapon_break(final int idx) {
        final int actId = getMobSkillTemplate().getActid(idx);
        for (final L1PcInstance pc : World.get().getVisiblePlayer(_attacker)) {
            final L1ItemInstance weapon = pc.getWeapon();
            if (weapon != null) {
                final int weaponDamage = _rnd.nextInt(_attacker.getInt() / 3) + 1;
                // \f1あなたの%0が损伤しました。
                pc.sendPackets(new S_ServerMessage(268, weapon.getLogName()));
                pc.getInventory().receiveDamage(weapon, weaponDamage);
            }
        }

        final S_DoActionGFX gfx = new S_DoActionGFX(_attacker.getId(), actId);
        _attacker.broadcastPacketX10(gfx);
        _sleepTime = _attacker.getNpcTemplate().getSubMagicSpeed();
        return true;
    }

    /**
     * 群体相消
     * 
     * @param idx
     * @return
     */
    private boolean cancellation(final int idx) {
        final int actId = getMobSkillTemplate().getActid(idx);
        for (final L1PcInstance pc : World.get().getVisiblePlayer(_attacker)) {
            // final L1Skills skill =
            // SkillsTable.get().getTemplate(L1SkillId.CANCELLATION);
            new L1SkillUse()
                    .handleCommands(pc, L1SkillId.CANCELLATION, pc.getId(),
                            pc.getX(), pc.getY(), 0, L1SkillUse.TYPE_GMBUFF);
        }

        final S_DoActionGFX gfx = new S_DoActionGFX(_attacker.getId(), actId);
        _attacker.broadcastPacketX10(gfx);
        _sleepTime = _attacker.getNpcTemplate().getSubMagicSpeed();
        return true;
    }

    /**
     * 群体冲晕
     * 
     * @param idx
     * @return
     */
    private boolean shock_stun(final int idx) {
        final int actId = this.getMobSkillTemplate().getActid(idx);
        for (final L1PcInstance pc : World.get().getVisiblePlayer(_attacker)) {

            final int[] stunTimeArray = { 2, 3, 4 };

            final int rnd = _rnd.nextInt(stunTimeArray.length);

            int shock = stunTimeArray[rnd];
            shock += pc.getSkillEffectTimeSec(SHOCK_STUN);

            if (shock > 6) {// 最大冲晕时间6秒
                shock = 6;
            }

            pc.setSkillEffect(L1SkillId.SHOCK_STUN, shock * 1000);

            // 骑士技能(冲击之晕)
            L1SpawnUtil.spawnEffect(81162, shock, pc.getX(), pc.getY(),
                    pc.getMapId(), pc, 0);

            pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
        }

        final S_DoActionGFX gfx = new S_DoActionGFX(_attacker.getId(), actId);
        _attacker.broadcastPacketX10(gfx);
        _sleepTime = _attacker.getNpcTemplate().getSubMagicSpeed();
        return true;
    }

    private boolean summon(final int idx) {
        final int summonId = getMobSkillTemplate().getSummon(idx);
        final int min = getMobSkillTemplate().getSummonMin(idx);
        final int max = getMobSkillTemplate().getSummonMax(idx);
        int count = 0;

        if (summonId == 0) {
            return false;
        }

        count = _rnd.nextInt(max) + min;
        L1MobSkillUseSpawn skillUseSpawn = new L1MobSkillUseSpawn();
        skillUseSpawn.mobspawn(_attacker, _target, summonId, count);

        // 魔方阵の表示
        _attacker.broadcastPacketX8(new S_SkillSound(_attacker.getId(), 761));

        // 魔法を使う动作のエフェクト
        final S_DoActionGFX gfx = new S_DoActionGFX(_attacker.getId(),
                ActionCodes.ACTION_SkillBuff);

        _attacker.broadcastPacketX10(gfx);
        _sleepTime = _attacker.getNpcTemplate().getSubMagicSpeed();

        return true;
    }

    /*
     * 15セル以内で射线が通るPCを指定したモンスターに强制变身させる。 对PCしか使えない。
     */
    private boolean poly(final int idx) {
        final int polyId = getMobSkillTemplate().getPolyId(idx);
        boolean usePoly = false;

        if (polyId == 0) {
            return false;
        }

        for (final L1PcInstance pc : World.get().getVisiblePlayer(_attacker)) {
            if (pc.isDead()) { // 死亡している
                continue;
            }
            if (pc.isGhost()) {
                continue;
            }
            if (pc.isGmInvis()) {
                continue;
            }
            if (_attacker.glanceCheck(pc.getX(), pc.getY()) == false) {
                continue; // 射线が通らない
            }

            final int npcId = _attacker.getNpcTemplate().get_npcId();
            switch (npcId) {
                case 81082: // ヤヒの场合
                    pc.getInventory().takeoffEquip(945); // 牛のpolyIdで装备を全部外す。
                    break;

                default:
                    break;
            }
            L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_NPC);

            usePoly = true;
        }
        if (usePoly) {
            // 变身させた场合、オレンジの柱を表示する。
            for (final L1PcInstance pc : World.get()
                    .getVisiblePlayer(_attacker)) {
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 230));
                break;
            }
            // 魔法を使う动作のエフェクト
            final S_DoActionGFX gfx = new S_DoActionGFX(_attacker.getId(),
                    ActionCodes.ACTION_SkillBuff);
            _attacker.broadcastPacketX10(gfx);

            _sleepTime = _attacker.getNpcTemplate().getSubMagicSpeed();
        }

        return usePoly;
    }

    /**
     * 魔法攻击
     * 
     * @param idx
     * @return
     */
    private boolean magicAttack(final int idx) {
        final L1SkillUse skillUse = new L1SkillUse();
        final int skillid = getMobSkillTemplate().getSkillId(idx);
        boolean canUseSkill = false;

        if (skillid > 0) {
            canUseSkill = skillUse.checkUseSkill(null, skillid,
                    _target.getId(), _target.getX(), _target.getY(), 0,
                    L1SkillUse.TYPE_NORMAL, _attacker);
        }

        if (canUseSkill == true) {
            if (getMobSkillTemplate().getLeverage(idx) > 0) {
                skillUse.setLeverage(getMobSkillTemplate().getLeverage(idx));
            }
            skillUse.handleCommands(null, skillid, _target.getId(),
                    _target.getX(), _target.getX(), 0, L1SkillUse.TYPE_NORMAL,
                    _attacker);

            // 使用スキルによるsleepTimeの设定
            final L1Skills skill = SkillsTable.get().getTemplate(skillid);
            if (skill.getTarget().equals("attack") && (skillid != 18)) { // 有方向魔法
                _sleepTime = _attacker.getNpcTemplate().getAtkMagicSpeed();

            } else { // 无方向魔法
                _sleepTime = _attacker.getNpcTemplate().getSubMagicSpeed();
            }

            return true;
        }
        return false;
    }

    /**
     * 物理攻击
     */
    private boolean physicalAttack(final int idx) {
        final Map<Integer, Integer> targetList = new HashMap<Integer, Integer>();
        final int areaWidth = getMobSkillTemplate().getAreaWidth(idx);
        final int areaHeight = getMobSkillTemplate().getAreaHeight(idx);
        final int range = getMobSkillTemplate().getRange(idx);
        final int actId = getMobSkillTemplate().getActid(idx);
        final int gfxId = getMobSkillTemplate().getGfxid(idx);

        // レンジ外
        if (_attacker.getLocation().getTileLineDistance(_target.getLocation()) > range) {
            return false;
        }

        // 障害物がある场合攻击不可能
        if (!_attacker.glanceCheck(_target.getX(), _target.getY())) {
            return false;
        }

        _attacker.setHeading(_attacker.targetDirection(_target.getX(),
                _target.getY())); // 向きのセット

        if (areaHeight > 0) {
            // 范围攻击
            final ArrayList<L1Object> objs = World.get().getVisibleBoxObjects(
                    _attacker, _attacker.getHeading(), areaWidth, areaHeight);

            for (final L1Object obj : objs) {
                if (!(obj instanceof L1Character)) { // ターゲットがキャラクター以外の场合何もしない。
                    continue;
                }

                final L1Character cha = (L1Character) obj;
                if (cha.isDead()) { // 死んでるキャラクターは对象外
                    continue;
                }

                // ゴースト状态は对象外
                if (cha instanceof L1PcInstance) {
                    if (((L1PcInstance) cha).isGhost()) {
                        continue;
                    }
                }

                // 障害物がある场合は对象外
                if (!_attacker.glanceCheck(cha.getX(), cha.getY())) {
                    continue;
                }

                if ((_target instanceof L1PcInstance)
                        || (_target instanceof L1SummonInstance)
                        || (_target instanceof L1PetInstance)) {
                    // 对PC
                    if (((obj instanceof L1PcInstance)
                            && !((L1PcInstance) obj).isGhost() && !((L1PcInstance) obj)
                                .isGmInvis())
                            || (obj instanceof L1SummonInstance)
                            || (obj instanceof L1PetInstance)) {
                        targetList.put(obj.getId(), 0);
                    }
                } else {
                    // 对NPC
                    if (obj instanceof L1MonsterInstance) {
                        targetList.put(obj.getId(), 0);
                    }
                }
            }
        } else {
            // 单体攻击
            targetList.put(_target.getId(), 0); // ターゲットのみ追加
        }

        if (targetList.size() == 0) {
            return false;
        }

        final Iterator<Integer> ite = targetList.keySet().iterator();
        while (ite.hasNext()) {
            final int targetId = ite.next();
            L1Object object = World.get().findObject(targetId);
            final L1AttackMode attack = new L1AttackNpc(_attacker,
                    (L1Character) object);

            if (attack.calcHit()) {
                if (getMobSkillTemplate().getLeverage(idx) > 0) {
                    attack.setLeverage(getMobSkillTemplate().getLeverage(idx));
                }
                attack.calcDamage();
            }
            if (actId > 0) {
                attack.setActId(actId);
            }
            // 攻击モーションは实际のターゲットに对してのみ行う
            if (targetId == this._target.getId()) {
                if (gfxId > 0) {
                    _attacker.broadcastPacketX8(new S_SkillSound(_attacker
                            .getId(), gfxId));
                }
                attack.action();
            }
            attack.commit();
        }

        _sleepTime = _attacker.getAtkspeed();
        return true;
    }

    /**
     * トリガーの条件のみチェック
     */
    private boolean isSkillUseble(final int skillIdx, final boolean isTriRnd) {
        boolean useble = false;
        final int type = getMobSkillTemplate().getType(skillIdx);

        if (isTriRnd || (type == L1MobSkill.TYPE_SUMMON)
                || (type == L1MobSkill.TYPE_POLY)) {
            if (getMobSkillTemplate().getTriggerRandom(skillIdx) > 0) {
                final int chance = _rnd.nextInt(100) + 1;
                if (chance < getMobSkillTemplate().getTriggerRandom(skillIdx)) {
                    useble = true;
                } else {
                    return false;
                }
            }
        }

        if (getMobSkillTemplate().getTriggerHp(skillIdx) > 0) {
            final int hpRatio = (_attacker.getCurrentHp() * 100)
                    / _attacker.getMaxHp();
            if (hpRatio <= getMobSkillTemplate().getTriggerHp(skillIdx)) {
                useble = true;
            } else {
                return false;
            }
        }

        if (getMobSkillTemplate().getTriggerCompanionHp(skillIdx) > 0) {
            final L1NpcInstance companionNpc = searchMinCompanionHp();
            if (companionNpc == null) {
                return false;
            }

            final int hpRatio = (companionNpc.getCurrentHp() * 100)
                    / companionNpc.getMaxHp();
            if (hpRatio <= getMobSkillTemplate()
                    .getTriggerCompanionHp(skillIdx)) {
                useble = true;
                _target = companionNpc; // ターゲットの入れ替え

            } else {
                return false;
            }
        }

        if (getMobSkillTemplate().getTriggerRange(skillIdx) != 0) {
            final int distance = _attacker.getLocation().getTileLineDistance(
                    _target.getLocation());

            if (getMobSkillTemplate().isTriggerDistance(skillIdx, distance)) {
                useble = true;

            } else {
                return false;
            }
        }

        if (getMobSkillTemplate().getTriggerCount(skillIdx) > 0) {
            if (getSkillUseCount(skillIdx) < getMobSkillTemplate()
                    .getTriggerCount(skillIdx)) {
                useble = true;

            } else {
                return false;
            }
        }
        return useble;
    }

    private L1NpcInstance searchMinCompanionHp() {
        L1NpcInstance npc;
        L1NpcInstance minHpNpc = null;
        int hpRatio = 100;
        int companionHpRatio;
        final int family = _attacker.getNpcTemplate().get_family();

        for (final L1Object object : World.get().getVisibleObjects(_attacker)) {
            if (object instanceof L1NpcInstance) {
                npc = (L1NpcInstance) object;
                if (npc.getNpcTemplate().get_family() == family) {
                    companionHpRatio = (npc.getCurrentHp() * 100)
                            / npc.getMaxHp();
                    if (companionHpRatio < hpRatio) {
                        hpRatio = companionHpRatio;
                        minHpNpc = npc;
                    }
                }
            }
        }
        return minHpNpc;
    }

    /**
     * 现在ChangeTargetで有效な值は2,3のみ
     * 
     * @param type
     * @param idx
     * @return
     */
    private L1Character changeTarget(final int type, final int idx) {
        L1Character target;

        switch (type) {
            case L1MobSkill.CHANGE_TARGET_ME:
                target = _attacker;
                break;

            case L1MobSkill.CHANGE_TARGET_RANDOM:
                // System.out.println("L1MobSkill.CHANGE_TARGET_RANDOM:");
                // ターゲット候补の选定
                final List<L1Character> targetList = new ArrayList<L1Character>();
                for (final L1Object obj : World.get().getVisibleObjects(
                        _attacker)) {
                    if ((obj instanceof L1PcInstance)
                            || (obj instanceof L1PetInstance)
                            || (obj instanceof L1SummonInstance)) {

                        final L1Character cha = (L1Character) obj;

                        final int distance = _attacker.getLocation()
                                .getTileLineDistance(cha.getLocation());

                        // 発动范围外のキャラクターは对象外
                        if (!getMobSkillTemplate().isTriggerDistance(idx,
                                distance)) {
                            continue;
                        }

                        // 障害物がある场合は对象外
                        if (!_attacker.glanceCheck(cha.getX(), cha.getY())) {
                            continue;
                        }

                        if (!_attacker.getHateList().containsKey(cha)) { // ヘイトがない场合对象外
                            continue;
                        }

                        if (cha.isDead()) { // 死んでるキャラクターは对象外
                            continue;
                        }

                        // ゴースト状态は对象外
                        if (cha instanceof L1PcInstance) {
                            if (((L1PcInstance) cha).isGhost()) {
                                continue;
                            }
                        }
                        targetList.add((L1Character) obj);
                    }
                }

                if (targetList.size() == 0) {
                    target = _target;

                } else {
                    final int randomSize = targetList.size() * 100;
                    final int targetIndex = _rnd.nextInt(randomSize) / 100;
                    target = targetList.get(targetIndex);
                }
                break;

            default:
                target = _target;
                break;
        }
        return target;
    }
}
