package com.lineage.server.model.skill;

import static com.lineage.server.model.skill.L1SkillId.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.ActionCodes;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.L1War;
import com.lineage.server.model.Instance.L1CrownInstance;
import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.Instance.L1DwarfInstance;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1FieldObjectInstance;
import com.lineage.server.model.Instance.L1FurnitureInstance;
import com.lineage.server.model.Instance.L1GuardInstance;
import com.lineage.server.model.Instance.L1HousekeeperInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MerchantInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.Instance.L1TeleporterInstance;
import com.lineage.server.model.Instance.L1TowerInstance;
import com.lineage.server.model.poison.L1DamagePoison;
import com.lineage.server.model.skill.skillmode.SkillMode;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_Chat;
import com.lineage.server.serverpackets.S_Dexup;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_Invis;
import com.lineage.server.serverpackets.S_NpcChat;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_PacketBoxIconAura;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_Poison;
import com.lineage.server.serverpackets.S_RangeSkill;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_ShowPolyList;
import com.lineage.server.serverpackets.S_SkillBrave;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.serverpackets.S_SkillIconShield;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_Sound;
import com.lineage.server.serverpackets.S_Strup;
import com.lineage.server.serverpackets.S_UseAttackSkill;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.utils.CheckUtil;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldTrap;
import com.lineage.server.world.WorldWar;

/**
 * 技能施放判断
 * 
 * @author dexc
 * 
 */
public class L1SkillUse {

    private static final Log _log = LogFactory.getLog(L1SkillUse.class);

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_LOGIN = 1;
    public static final int TYPE_SPELLSC = 2;
    public static final int TYPE_NPCBUFF = 3;
    public static final int TYPE_GMBUFF = 4;

    private L1Skills _skill;
    private int _skillId;
    private int _getBuffDuration;// 技能时间
    private int _shockStunDuration;
    private int _getBuffIconDuration;// 技能图示时间
    private int _targetID;
    private int _mpConsume = 0;
    private int _hpConsume = 0;
    private int _targetX = 0;
    private int _targetY = 0;
    private int _dmg = 0;// 伤害
    private int _skillTime = 0;
    private int _type = 0;
    private boolean _isPK = false;
    private int _bookmarkId = 0;
    private int _itemobjid = 0;
    private boolean _checkedUseSkill = false; // 事前チェック济みか
    private int _leverage = 10; // 1/10倍なので10で1倍
    private boolean _isFreeze = false;
    private boolean _isCounterMagic = true;
    private boolean _isGlanceCheckFail = false;

    /** 执行者 */
    private L1Character _user = null;

    /** 执行者为pc */
    private L1PcInstance _player = null;

    /** 执行者为npc */
    private L1NpcInstance _npc = null;

    /** 目标 */
    private L1Character _target = null;

    /** 目标为NPC */
    private L1NpcInstance _targetNpc = null;

    private int _calcType;
    private static final int PC_PC = 1;
    private static final int PC_NPC = 2;
    private static final int NPC_PC = 3;
    private static final int NPC_NPC = 4;

    private ArrayList<TargetStatus> _targetList;

    private static final int[] EXCEPT_COUNTER_MAGIC = { 1, 2, 3, 5, 8, 9, 12,
            13, 14, 19, 21, 26, 31, 32, 35, 37, 42, 43, 44, 48, 49, 52, 54, 55,
            57, 60, 61, 63, 67, 68, 69, 72, 73, 75, 78, 79, SHOCK_STUN,
            REDUCTION_ARMOR, BOUNCE_ATTACK, SOLID_CARRIAGE, COUNTER_BARRIER,
            97, 98, 99, 100, 101, 102, 104, 105, 106, 107, 109, 110, 111, 113,
            114, 115, 116, 117, 118, 129, 130, 131, 132, 134, 137, 138, 146,
            147, 148, 149, 150, 151, 155, 156, 158, 159, 161, 163, 164, 165,
            166, 168, 169, 170, 171, SOUL_OF_FLAME, ADDITIONAL_FIRE,
            DRAGON_SKIN, AWAKEN_ANTHARAS, AWAKEN_FAFURION, AWAKEN_VALAKAS,
            MIRROR_IMAGE, ILLUSION_OGRE, ILLUSION_LICH, PATIENCE, 10026, 10027,
            ILLUSION_DIA_GOLEM, INSIGHT, ILLUSION_AVATAR, 10028, 10029, DRESS_HALZ };

    /**
     * 攻击倍率(1/10)
     */
    public void setLeverage(final int i) {
        this._leverage = i;
    }

    /**
     * 攻击倍率(1/10)
     * 
     * @return
     */
    public int getLeverage() {
        return this._leverage;
    }

    private boolean isCheckedUseSkill() {
        return this._checkedUseSkill;
    }

    private void setCheckedUseSkill(final boolean flg) {
        this._checkedUseSkill = flg;
    }

    /**
     * 
     * @param player
     *            攻击者为PC
     * @param skillid
     *            技能编号
     * @param target_id
     *            目标OBJID
     * @param x
     *            X座标
     * @param y
     *            Y座标
     * @param time
     *            时间
     * @param type
     *            类型
     * @param attacker
     *            攻击者为NPC
     * @return
     */
    public boolean checkUseSkill(final L1PcInstance player, final int skillid,
            final int target_id, final int x, final int y, final int time,
            final int type, final L1Character attacker) {
        // 初期设定ここから
        setCheckedUseSkill(true);
        _targetList = new ArrayList<TargetStatus>(); // ターゲットリストの初期化

        _skill = SkillsTable.get().getTemplate(skillid);
        if (_skill == null) {
            return false;
        }
        _skillId = skillid;
        _targetX = x;
        _targetY = y;
        _skillTime = time;
        _type = type;
        boolean checkedResult = true;

        if (attacker == null) {
            // pc
            _player = player;
            _user = _player;

        } else {
            // npc
            _npc = (L1NpcInstance) attacker;
            _user = _npc;
        }

        if (_skill.getTarget().equals("none")) {
            _targetID = _user.getId();
            _targetX = _user.getX();
            _targetY = _user.getY();

        } else {
            _targetID = target_id;
        }

        switch (type) {
            case TYPE_NORMAL: // 通常の魔法使用时
                checkedResult = this.isNormalSkillUsable();
                break;

            case TYPE_SPELLSC: // スペルスクロール使用时
                /*
                 * checkedResult = this.isSpellScrollUsable(); break;
                 */

            case TYPE_NPCBUFF:
                checkedResult = true;
                break;
        }

        if (!checkedResult) {
            return false;
        }

        // ファイアーウォール、ライフストリームは咏唱对象が座标
        // キューブは咏唱者の座标に配置されるため例外
        // id58火牢 id63治愈能量风暴
        if ((_skillId == FIRE_WALL) || (_skillId == LIFE_STREAM)) {
            return true;
        }

        final L1Object object = World.get().findObject(_targetID);
        if (object instanceof L1ItemInstance) {
            return false;
        }
        if (_user instanceof L1PcInstance) {
            if (object instanceof L1PcInstance) {
                _calcType = PC_PC;

            } else {
                _calcType = PC_NPC;
                _targetNpc = (L1NpcInstance) object;
            }

        } else if (_user instanceof L1NpcInstance) {
            if (object instanceof L1PcInstance) {
                _calcType = NPC_PC;

            } else if (_skill.getTarget().equals("none")) {
                _calcType = NPC_PC;

            } else {
                _calcType = NPC_NPC;
                _targetNpc = (L1NpcInstance) object;
            }
        }

        switch (_skillId) {
        // 可使用传送戒指技能
            case TELEPORT:
            case MASS_TELEPORT:
                _bookmarkId = target_id;
                break;

            // 技能对象为道具
            case CREATE_MAGICAL_WEAPON:
            case BRING_STONE:
            case BLESSED_ARMOR:
            case ENCHANT_WEAPON:
            case SHADOW_FANG:
                _itemobjid = target_id;
                break;
        }

        _target = (L1Character) object;

        if (!(_target instanceof L1MonsterInstance)
                && _skill.getTarget().equals("attack")
                && (_user.getId() != target_id)) {
            _isPK = true; // ターゲットがモンスター以外で攻击系スキルで、自分以外の场合PKモードとする。
        }

        // 初期设定ここまで

        // 事前チェック
        if (!(object instanceof L1Character)) { // ターゲットがキャラクター以外の场合何もしない。
            checkedResult = false;
        }

        // 技能发动 目标清单判定
        makeTargetList();

        if ((_targetList.size() == 0) && (_user instanceof L1NpcInstance)) {
            checkedResult = false;
        }
        // 事前チェックここまで
        return checkedResult;
    }

    /**
     * 通常のスキル使用时に使用者の状态からスキルが使用可能であるか判断する
     * 
     * @return false スキルが使用不可能な状态である场合 在以下情况下不可使用技能
     */
    private boolean isNormalSkillUsable() {
        // スキル使用者がPCの场合のチェック
        if (this._user instanceof L1PcInstance) {
            final L1PcInstance pc = (L1PcInstance) this._user;

            if (!this.isAttrAgrees()) { // 精灵魔法で、属性が一致しなければ何もしない。
                return false;
            }

            if ((this._skillId == ELEMENTAL_PROTECTION)
                    && (pc.getElfAttr() == 0)) {
                pc.sendPackets(new S_ServerMessage(280)); // \f1魔法が失败しました。
                return false;
            }

            // DIGはロウフルでのみ使用可
            //修改究极邪恶状态一样可以使用
//            if ((this._skillId == DISINTEGRATE) && (pc.getLawful() < 500)) {
//                // このメッセージであってるか未确认
//                pc.sendPackets(new S_ServerMessage(352, "$967")); // この魔法を利用するには性向值が%0でなければなりません。
//                return false;
//            }

            // 同じキューブは效果范围外であれば配置可能
            if ((this._skillId == CUBE_IGNITION)
                    || (this._skillId == CUBE_QUAKE)
                    || (this._skillId == CUBE_SHOCK)
                    || (this._skillId == CUBE_BALANCE)) {
                boolean isNearSameCube = false;
                int gfxId = 0;
                for (final L1Object obj : World.get().getVisibleObjects(pc, 3)) {
                    if (obj instanceof L1EffectInstance) {
                        final L1EffectInstance effect = (L1EffectInstance) obj;
                        gfxId = effect.getGfxId();
                        if (((this._skillId == CUBE_IGNITION) && (gfxId == 6706))
                                || ((this._skillId == CUBE_QUAKE) && (gfxId == 6712))
                                || ((this._skillId == CUBE_SHOCK) && (gfxId == 6718))
                                || ((this._skillId == CUBE_BALANCE) && (gfxId == 6724))) {
                            isNearSameCube = true;
                            break;
                        }
                    }
                }
                if (isNearSameCube) {
                    pc.sendPackets(new S_ServerMessage(1412)); // 已在地板上召唤了魔法立方块。
                    return false;
                }
            }

            // 觉醒状态では觉醒スキル以外使用不可
            if (((pc.getAwakeSkillId() == AWAKEN_ANTHARAS)
                    && (this._skillId != AWAKEN_ANTHARAS)
                    && (this._skillId != MAGMA_BREATH)
                    && (this._skillId != SHOCK_SKIN) && (this._skillId != FREEZING_BREATH))
                    || ((pc.getAwakeSkillId() == AWAKEN_FAFURION)
                            && (this._skillId != AWAKEN_FAFURION)
                            && (this._skillId != MAGMA_BREATH)
                            && (this._skillId != SHOCK_SKIN) && (this._skillId != FREEZING_BREATH))
                    || ((pc.getAwakeSkillId() == AWAKEN_VALAKAS)
                            && (this._skillId != AWAKEN_VALAKAS)
                            && (this._skillId != MAGMA_BREATH)
                            && (this._skillId != SHOCK_SKIN) && (this._skillId != FREEZING_BREATH))) {
                pc.sendPackets(new S_ServerMessage(1385)); // 目前状态中无法使用觉醒魔法。
                return false;
            }

            if ((this.isItemConsume() == false) && !this._player.isGm()) { // 消费アイテムはあるか
                this._player.sendPackets(new S_ServerMessage(299)); // \f1施放魔法所需材料不足。
                return false;
            }
        }
        // スキル使用者がNPCの场合のチェック
        else if (this._user instanceof L1NpcInstance) {

            // サイレンス状态では使用不可
            if (this._user.hasSkillEffect(SILENCE)) {
                // NPCにサイレンスが挂かっている场合は1回だけ使用をキャンセルさせる效果。
                this._user.removeSkillEffect(SILENCE);
                return false;
            }
        }

        // PC、NPC共通のチェック
        if (!this.isHPMPConsume()) { // 消费HP、MPはあるか
            return false;
        }
        return true;
    }

    /**
     * pc 用技能施放判断
     * 
     * @param player
     * @param skillId
     * @param targetId
     * @param x
     * @param y
     * @param timeSecs
     *            秒
     * @param type
     */
    public void handleCommands(final L1PcInstance player, final int skillId,
            final int targetId, final int x, final int y, final int timeSecs,
            final int type) {
        this.handleCommands(player, skillId, targetId, x, y, timeSecs, type,
                null);
    }

    /**
     * 通用技能施放判断
     * 
     * @param player
     * @param skillId
     * @param targetId
     * @param x
     * @param y
     * @param timeSecs
     * @param type
     * @param attacker
     */
    public void handleCommands(final L1PcInstance player, final int skillId,
            final int targetId, final int x, final int y, final int timeSecs,
            final int type, final L1Character attacker) {
        try {
            // 事前チェックをしているか？
            if (!isCheckedUseSkill()) {
                final boolean isUseSkill = checkUseSkill(player, skillId,
                        targetId, x, y, timeSecs, type, attacker);

                if (!isUseSkill) {
                    failSkill();
                    return;
                }
            }
            switch (type) {
                case TYPE_NORMAL: // 魔法咏唱时
                    if (!_isGlanceCheckFail || (_skill.getArea() > 0)
                            || _skill.getTarget().equals("none")) {
                        runSkill();
                        useConsume();
                        sendGrfx(true);
                        sendFailMessageHandle();
                        setDelay();
                    }
                    break;

                case TYPE_LOGIN: // ログイン时（HPMP材料消费なし、グラフィックなし）
                    runSkill();
                    break;

                case TYPE_SPELLSC: // スペルスクロール使用时（HPMP材料消费なし）
                    runSkill();
                    sendGrfx(true);
                    break;

                case TYPE_GMBUFF: // GMBUFF使用时（HPMP材料消费なし、魔法モーションなし）
                    runSkill();
                    sendGrfx(false);
                    break;

                case TYPE_NPCBUFF: // NPCBUFF使用时（HPMP材料消费なし）
                    runSkill();
                    sendGrfx(true);
                    break;
            }
            setCheckedUseSkill(false);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 法术施展失败的处理
     */
    private void failSkill() {
        setCheckedUseSkill(false);
        switch (_skillId) {
            case TELEPORT:
            case MASS_TELEPORT:
            case TELEPORT_TO_MATHER:
                // 解除传送锁定
                _player.sendPackets(new S_Paralysis(
                        S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
                break;
        }
    }

    /**
     * 可加入设置目标的判断
     * 
     * @param cha
     *            加入判断的目标物件
     * @return true:可加入目标 false:不可加入目标
     * @throws Exception
     */
    private boolean isTarget(final L1Character cha) throws Exception {
        if (cha == null) {
            return false;
        }

        // 副本ID不相等
        if (_user.get_showId() != cha.get_showId()) {
            return false;
        }

        if (_npc != null) {
            // 在目标清单中
            if (_npc.isHate(cha)) {
                return true;
            }
            // 施展者是宠物 XXX
            if (_npc instanceof L1PetInstance) {
                if (cha instanceof L1MonsterInstance) {
                    return true;
                }
            }
            // 施展者是召唤兽
            if (_npc instanceof L1SummonInstance) {
                if (cha instanceof L1MonsterInstance) {
                    return true;
                }
            }
        }

        // 该物件是否允许攻击
        if (!CheckUtil.checkAttackSkill(cha)) {
            return false;
        }

        boolean flg = false;

        // 目标是门
        if (cha instanceof L1DoorInstance) {
            // 目标不可破坏设置
            if ((cha.getMaxHp() == 0) || (cha.getMaxHp() == 1)) {
                return false;
            }
        }

        // 目标是魔法娃娃 拒绝所有技能
        if (cha instanceof L1DollInstance) {
            return false;
        }

        // 目标是人物
        if (cha instanceof L1PcInstance) {
            final L1PcInstance pc = (L1PcInstance) cha;
            // 鬼魂模式 以及 GM隐身
            if (pc.isGhost() || pc.isGmInvis()) {
                return false;
            }
        }

        // NPC 对 PC
        if (_calcType == NPC_PC) {
            if ((cha instanceof L1PcInstance) || (cha instanceof L1PetInstance)
                    || (cha instanceof L1SummonInstance)) {
                flg = true;
            }
        }

        // PC 对 NPC
        if (_calcType == PC_NPC) {
            // 判断目标为人物
            if (cha instanceof L1PcInstance) {
                // 位于安全区域中
                if (cha.isSafetyZone()) {
                    return false;
                }
            }
        }

        // 元のターゲットがPet、Summon以外のNPCの场合、PC、Pet、Summonは对象外
        if ((_calcType == PC_NPC)
                // 目标是NPC
                && (_target instanceof L1NpcInstance)
                // 不能是宠物
                && !(_target instanceof L1PetInstance)
                // 不能是召唤兽
                && !(_target instanceof L1SummonInstance)
                && ((cha instanceof L1PetInstance)
                        || (cha instanceof L1SummonInstance) || (cha instanceof L1PcInstance))) {
            return false;
        }

        // 元のターゲットがガード以外のNPCの场合、ガードは对象外
        if ((_calcType == PC_NPC) && (_target instanceof L1NpcInstance)
                && !(_target instanceof L1GuardInstance)
                && (cha instanceof L1GuardInstance)) {
            return false;
        }

        // NPC对PCでターゲットがモンスターの场合ターゲットではない。
        if ((_skill.getTarget().equals("attack") || (_skill.getType() == L1Skills.TYPE_ATTACK))
                && (_calcType == NPC_PC)
                && !(cha instanceof L1PetInstance)
                && !(cha instanceof L1SummonInstance)
                && !(cha instanceof L1PcInstance)) {
            return false;
        }

        // NPC对NPCで使用者がMOBで、ターゲットがMOBの场合ターゲットではない。
        if ((_skill.getTarget().equals("attack") || (_skill.getType() == L1Skills.TYPE_ATTACK))
                && (_calcType == NPC_NPC)
                && (_user instanceof L1MonsterInstance)
                && (cha instanceof L1MonsterInstance)) {
            return false;
        }

        // 无方向范围攻击魔法で攻击できないNPCは对象外
        if (_skill.getTarget().equals("none")
                && (_skill.getType() == L1Skills.TYPE_ATTACK)
                && ((cha instanceof L1CrownInstance)
                        || (cha instanceof L1DwarfInstance)
                        || (cha instanceof L1EffectInstance)
                        || (cha instanceof L1FieldObjectInstance)
                        || (cha instanceof L1FurnitureInstance)
                        || (cha instanceof L1HousekeeperInstance)
                        || (cha instanceof L1MerchantInstance) || (cha instanceof L1TeleporterInstance))) {
            return false;
        }

        // 攻击系スキルで对象が自分は对象外
        if ((_skill.getType() == L1Skills.TYPE_ATTACK)
                && (cha.getId() == _user.getId())) {
            return false;
        }

        // ターゲットが自分でH-Aの场合效果无し
        if ((cha.getId() == _user.getId()) && (_skillId == HEAL_ALL)) {
            return false;
        }

        if ((((_skill.getTargetTo() & L1Skills.TARGET_TO_PC) == L1Skills.TARGET_TO_PC)
                || ((_skill.getTargetTo() & L1Skills.TARGET_TO_CLAN) == L1Skills.TARGET_TO_CLAN) || ((_skill
                .getTargetTo() & L1Skills.TARGET_TO_PARTY) == L1Skills.TARGET_TO_PARTY))
                && (cha.getId() == _user.getId()) && (_skillId != HEAL_ALL)) {
            return true; // ターゲットがパーティーかクラン员のものは自分に效果がある。（ただし、ヒールオールは除外）
        }

        // 攻击者是PC
        if ((_user instanceof L1PcInstance)
                && (_skill.getTarget().equals("attack") || (_skill.getType() == L1Skills.TYPE_ATTACK))
                && (_isPK == false)) {

            // 目标是宠物
            if (cha instanceof L1SummonInstance) {
                final L1SummonInstance summon = (L1SummonInstance) cha;
                // 自己的宠物
                if (_player.getId() == summon.getMaster().getId()) {
                    return false;
                }

            } else if (cha instanceof L1PetInstance) {
                final L1PetInstance pet = (L1PetInstance) cha;
                // 自己的宠物
                if (_player.getId() == pet.getMaster().getId()) {
                    return false;
                }
            }
        }

        if ((_skill.getTarget().equals("attack") || (_skill.getType() == L1Skills.TYPE_ATTACK))
                // 目标不是怪物
                && !(cha instanceof L1MonsterInstance)
                // 不是PK状态
                && (_isPK == false)
                // 目标是人物
                && (_target instanceof L1PcInstance)) {

            L1PcInstance enemy = null;

            try {
                enemy = (L1PcInstance) cha;

            } catch (final Exception e) {
                return false;
            }

            // カウンター无所遁形术
            if ((_skillId == COUNTER_DETECTION)
                    && (enemy.getZoneType() != 1)
                    && (cha.hasSkillEffect(INVISIBILITY) || cha
                            .hasSkillEffect(BLIND_HIDING))) {
                return true; // インビジかブラインドハイディング中
            }
            if ((_player.getClanid() != 0) && (enemy.getClanid() != 0)) { // クラン所属中
                // 取回全部战争清单
                for (final L1War war : WorldWar.get().getWarList()) {
                    if (war.checkClanInWar(_player.getClanname())) { // 自クランが战争に参加中
                        if (war.checkClanInSameWar( // 同じ战争に参加中
                                _player.getClanname(), enemy.getClanname())) {
                            if (L1CastleLocation.checkInAllWarArea(
                                    enemy.getX(), enemy.getY(),
                                    enemy.getMapId())) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false; // 攻击スキルでPKモードじゃない场合
        }

        if ((_user.glanceCheck(cha.getX(), cha.getY()) == false)
                && (_skill.isThrough() == false)) {
            // エンチャント、复活スキルは障害物の判定をしない
            if (!((_skill.getType() == L1Skills.TYPE_CHANGE) || (_skill
                    .getType() == L1Skills.TYPE_RESTORE))) {
                _isGlanceCheckFail = true;
                return false; // 直线上に障害物がある
            }
        }

        if (cha.hasSkillEffect(ICE_LANCE)
                && ((_skillId == ICE_LANCE) || (_skillId == FREEZING_BLIZZARD) || (_skillId == FREEZING_BREATH))) {
            return false; // アイスランス中にアイスランス、フリージングブリザード、フリージングブレス
        }

        if (cha.hasSkillEffect(FREEZING_BLIZZARD)
                && ((_skillId == ICE_LANCE) || (_skillId == FREEZING_BLIZZARD) || (_skillId == FREEZING_BREATH))) {
            return false; // フリージングブリザード中にアイスランス、フリージングブリザード、フリージングブレス
        }

        if (cha.hasSkillEffect(FREEZING_BREATH)
                && ((_skillId == ICE_LANCE) || (_skillId == FREEZING_BLIZZARD) || (_skillId == FREEZING_BREATH))) {
            return false; // フリージングブレス中にアイスランス、フリージングブリザード、フリージングブレス
        }

        if (cha.hasSkillEffect(EARTH_BIND) && (_skillId == EARTH_BIND)) {
            return false; // アース バインド中にアース バインド
        }

        if (!(cha instanceof L1MonsterInstance)
                && ((_skillId == TAMING_MONSTER) || (_skillId == CREATE_ZOMBIE))) {
            return false; // ターゲットがモンスターじゃない（テイミングモンスター）
        }
        if (cha.isDead()
                && ((_skillId != CREATE_ZOMBIE) && (_skillId != RESURRECTION)
                        && (_skillId != GREATER_RESURRECTION) && (_skillId != CALL_OF_NATURE))) {
            return false; // ターゲットが死亡している
        }

        if ((cha.isDead() == false)
                && ((_skillId == CREATE_ZOMBIE) || (_skillId == RESURRECTION)
                        || (_skillId == GREATER_RESURRECTION) || (_skillId == CALL_OF_NATURE))) {
            return false; // ターゲットが死亡していない
        }

        if (((cha instanceof L1TowerInstance) || (cha instanceof L1DoorInstance))
                && ((_skillId == CREATE_ZOMBIE) || (_skillId == RESURRECTION)
                        || (_skillId == GREATER_RESURRECTION) || (_skillId == CALL_OF_NATURE))) {
            return false; // ターゲットがガーディアンタワー、ドア
        }

        if (cha instanceof L1PcInstance) {
            final L1PcInstance pc = (L1PcInstance) cha;
            //
            if (pc.hasSkillEffect(ABSOLUTE_BARRIER)) {
                switch (_skillId) {
                    case CURSE_BLIND:
                    case WEAPON_BREAK:
                    case DARKNESS:
                    case WEAKNESS:
                    case DISEASE:
                    case FOG_OF_SLEEPING:
                    case MASS_SLOW:
                    case SLOW:
                    case CANCELLATION:
                    case SILENCE:
                    case DECAY_POTION:
                    case MASS_TELEPORT:
                    case DETECTION:
                    case COUNTER_DETECTION:
                    case ERASE_MAGIC:
                    case ENTANGLE:
                    case PHYSICAL_ENCHANT_DEX:
                    case PHYSICAL_ENCHANT_STR:
                    case BLESS_WEAPON:
                    case EARTH_SKIN:
                    case IMMUNE_TO_HARM:
                    case REMOVE_CURSE:
                        return true;

                    default:
                        return false;
                }
            }
        }

        // 目标在隐身状态(地下)
        if (cha instanceof L1NpcInstance) {
            final int hiddenStatus = ((L1NpcInstance) cha).getHiddenStatus();
            switch (hiddenStatus) {
                case L1NpcInstance.HIDDEN_STATUS_SINK:
                    switch (_skillId) {
                        case DETECTION:
                        case COUNTER_DETECTION:
                            return true;
                    }
                    return false;

                case L1NpcInstance.HIDDEN_STATUS_FLY:
                    return false;
            }
        }

        if (((_skill.getTargetTo() & L1Skills.TARGET_TO_PC) == L1Skills.TARGET_TO_PC // ターゲットがPC
        )
                && (cha instanceof L1PcInstance)) {
            flg = true;

        } else if (((_skill.getTargetTo() & L1Skills.TARGET_TO_NPC) == L1Skills.TARGET_TO_NPC // ターゲットがNPC
        )

                && ((cha instanceof L1MonsterInstance)
                        || (cha instanceof L1NpcInstance)
                        || (cha instanceof L1SummonInstance) || (cha instanceof L1PetInstance))) {
            flg = true;

        } else if (((_skill.getTargetTo() & L1Skills.TARGET_TO_PET) == L1Skills.TARGET_TO_PET)
                && (_user instanceof L1PcInstance)) { // ターゲットがSummon,Pet
            if (cha instanceof L1SummonInstance) {
                final L1SummonInstance summon = (L1SummonInstance) cha;
                if (summon.getMaster() != null) {
                    if (_player.getId() == summon.getMaster().getId()) {
                        flg = true;
                    }
                }
            }

            if (cha instanceof L1PetInstance) {
                final L1PetInstance pet = (L1PetInstance) cha;
                if (pet.getMaster() != null) {
                    if (_player.getId() == pet.getMaster().getId()) {
                        flg = true;
                    }
                }
            }
        }

        if ((_calcType == PC_PC) && (cha instanceof L1PcInstance)) {

            final L1PcInstance xpc = (L1PcInstance) cha;
            if (((_skill.getTargetTo() & L1Skills.TARGET_TO_CLAN) == L1Skills.TARGET_TO_CLAN)
                    && (((_player.getClanid() != 0 // ターゲットがクラン员
                    ) && (_player.getClanid() == xpc.getClanid())) || _player
                            .isGm())) {
                return true;
            }

            if (((_skill.getTargetTo() & L1Skills.TARGET_TO_PARTY) == L1Skills.TARGET_TO_PARTY)
                    && (_player.getParty().isMember(xpc) || _player.isGm())) {
                return true;
            }
        }

        return flg;
    }

    /**
     * 是否为同组
     * 
     * @param npc
     * @param cha
     * @return
     */
    private boolean isParty(final L1NpcInstance npc, final L1Character cha) {
        if (npc.getMaster() == null) {
            return false;
        }
        // 在目标清单中
        if (npc.isHate(cha)) {
            return false;
        }

        final int masterId = npc.getMaster().getId();

        // 目标是人物
        if (cha instanceof L1PcInstance) {
            if (cha.getId() == masterId) {
                return true;
            }
            return false;
        }

        // 目标是宠物
        if (cha instanceof L1PetInstance) {
            final L1PetInstance tgPet = (L1PetInstance) cha;
            if (tgPet.getMaster() != null
                    && tgPet.getMaster().getId() == masterId) {
                return true;
            }
            return false;
        }

        // 目标是召唤兽
        if (cha instanceof L1SummonInstance) {
            final L1SummonInstance tgSu = (L1SummonInstance) cha;
            if (tgSu.getMaster() != null
                    && tgSu.getMaster().getId() == masterId) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 技能发动 目标清单判定
     */
    private void makeTargetList() {
        try {
            if (this._type == TYPE_LOGIN) { // ログイン时(死亡时、お化け屋敷のキャンセレーション含む)は使用者のみ
                this._targetList.add(new TargetStatus(this._user));
                return;
            }

            if ((this._skill.getTargetTo() == L1Skills.TARGET_TO_ME)
                    && ((this._skill.getType() & L1Skills.TYPE_ATTACK) != L1Skills.TYPE_ATTACK)) {
                this._targetList.add(new TargetStatus(this._user)); // ターゲットは使用者のみ
                return;
            }

            // 具有攻击范围设置
            if (this._skill.getRanged() != -1) {
                if (this._user.getLocation().getTileLineDistance(
                        this._target.getLocation()) > this._skill.getRanged()) {
                    return; // 射程范围外
                }

            } else {
                // 距离不可见
                if (!this._user.getLocation().isInScreen(
                        this._target.getLocation())) {
                    return; // 射程范围外
                }
            }

            if ((this.isTarget(this._target) == false)
                    && !(this._skill.getTarget().equals("none"))) {
                // 对象が违うのでスキルが発动しない。
                return;
            }

            // 直线上目标列举
            switch (this._calcType) {
                case LIGHTNING:
                case FREEZING_BREATH:
                    final ArrayList<L1Object> al1object = World.get()
                            .getVisibleLineObjects(this._user, this._target);
                    for (final L1Object tgobj : al1object) {
                        if (tgobj == null) {
                            continue;
                        }

                        if (!(tgobj instanceof L1Character)) { // ターゲットがキャラクター以外の场合何もしない。
                            continue;
                        }

                        final L1Character cha = (L1Character) tgobj;
                        if (this.isTarget(cha) == false) {
                            continue;
                        }
                        // 技能发动 目标清单判定:直线上目标列举
                        this._targetList.add(new TargetStatus(cha));
                    }
                    al1object.clear();
                    return;
            }

            // 单一目标攻击
            if (this._skill.getArea() == 0) {
                if (!this._user.glanceCheck(this._target.getX(),
                        this._target.getY())) { // 直线上に障害物があるか
                    if (((this._skill.getType() & L1Skills.TYPE_ATTACK) == L1Skills.TYPE_ATTACK)
                            && (this._skillId != 10026)
                            && (this._skillId != 10027)
                            && (this._skillId != 10028)
                            && (this._skillId != 10029)) { // 安息攻击以外の攻击スキル
                        // ダメージも発生しないし、ダメージモーションも発生しないが、スキルは発动
                        this._targetList.add(new TargetStatus(this._target,
                                false));
                        return;
                    }
                }
                this._targetList.add(new TargetStatus(this._target));

                // 范围攻击
            } else {
                if (!this._skill.getTarget().equals("none")) {
                    this._targetList.add(new TargetStatus(this._target));
                }

                if ((this._skillId != HEAL_ALL)
                        && !(this._skill.getTarget().equals("attack") || (this._skill
                                .getType() == L1Skills.TYPE_ATTACK))) {
                    // 攻击系以外のスキルとH-A以外はターゲット自身を含める
                    this._targetList.add(new TargetStatus(this._user));
                }

                List<L1Object> objects;
                // 全画面物件
                if (this._skill.getArea() == -1) {
                    objects = World.get().getVisibleObjects(this._user);

                    // 指定范围物件
                } else {
                    objects = World.get().getVisibleObjects(this._target,
                            this._skill.getArea());
                }
                // System.out.println("攻击范围物件数量:"+objects.size());
                for (final L1Object tgobj : objects) {
                    if (tgobj == null) {
                        continue;
                    }

                    if (!(tgobj instanceof L1Character)) {
                        continue;
                    }

                    if (tgobj instanceof L1MonsterInstance) {
                        L1MonsterInstance mob = (L1MonsterInstance) tgobj;
                        if (mob.getNpcId() == 45166) {// 胆小的南瓜怪
                            continue;
                        }
                        if (mob.getNpcId() == 45167) {// 残暴的南瓜怪
                            continue;
                        }
                    }

                    final L1Character cha = (L1Character) tgobj;

                    if (!this.isTarget(cha)) {
                        continue;
                    }

                    // 技能发动 目标清单判定:加入目标清单 - 回圈
                    this._targetList.add(new TargetStatus(cha));
                }
                return;
            }

        } catch (final Exception e) {
            // _log.error("SkillId:" + this._skillId + " UserName:" +
            // this._player.getName());
        }
    }

    /**
     * 讯息发送
     * 
     * @param pc
     */
    private void sendHappenMessage(final L1PcInstance pc) {
        final int msgID = this._skill.getSysmsgIdHappen();
        if (msgID > 0) {
            pc.sendPackets(new S_ServerMessage(msgID));
        }
    }

    // 失败メッセージ表示のハンドル
    private void sendFailMessageHandle() {
        // 攻击スキル以外で对象を指定するスキルが失败した场合は失败したメッセージをクライアントに送信
        // ※攻击スキルは障害物があっても成功时と同じアクションであるべき。
        if ((_skill.getType() != L1Skills.TYPE_ATTACK)
                && !_skill.getTarget().equals("none")
                && (_targetList.size() == 0)) {
            sendFailMessage();
        }
    }

    // メッセージの表示（失败したとき）
    private void sendFailMessage() {
        final int msgID = _skill.getSysmsgIdFail();
        if ((msgID > 0) && (_user instanceof L1PcInstance)) {
            _player.sendPackets(new S_ServerMessage(msgID));
        }
    }

    // 精灵魔法の属性と使用者の属性は一致するか？（とりあえずの对处なので、对应できたら消去して下さい)
    private boolean isAttrAgrees() {
        final int magicattr = _skill.getAttr();
        if (_user instanceof L1NpcInstance) { // NPCが使った场合なんでもOK
            return true;
        }

        if ((_skill.getSkillLevel() >= 17) && (_skill.getSkillLevel() <= 22)
                && (magicattr != 0 // 精灵魔法で、无属性魔法ではなく、
                ) && (magicattr != _player.getElfAttr() // 使用者と魔法の属性が一致しない。
                ) && !_player.isGm()) { // ただしGMは例外
            return false;
        }
        return true;
    }

    /**
     * 判断技能的使用是否需要消耗HP/MP
     * 
     * @return
     */
    private boolean isHPMPConsume() {
        this._mpConsume = _skill.getMpConsume();
        this._hpConsume = _skill.getHpConsume();
        int currentMp = 0;
        int currentHp = 0;

        if (this._user instanceof L1NpcInstance) {
            currentMp = _npc.getCurrentMp();
            currentHp = _npc.getCurrentHp();

        } else {
            currentMp = _player.getCurrentMp();
            currentHp = _player.getCurrentHp();

            // MPのINT轻减
            if ((this._player.getInt() > 12) && (this._skillId > HOLY_WEAPON)
                    && (this._skillId <= FREEZING_BLIZZARD)) { // LV2以上
                this._mpConsume--;
            }
            if ((this._player.getInt() > 13) && (this._skillId > STALAC)
                    && (this._skillId <= FREEZING_BLIZZARD)) { // LV3以上
                this._mpConsume--;
            }
            if ((this._player.getInt() > 14)
                    && (this._skillId > WEAK_ELEMENTAL)
                    && (this._skillId <= FREEZING_BLIZZARD)) { // LV4以上
                this._mpConsume--;
            }
            if ((this._player.getInt() > 15) && (this._skillId > MEDITATION)
                    && (this._skillId <= FREEZING_BLIZZARD)) { // LV5以上
                this._mpConsume--;
            }
            if ((this._player.getInt() > 16) && (this._skillId > DARKNESS)
                    && (this._skillId <= FREEZING_BLIZZARD)) { // LV6以上
                this._mpConsume--;
            }
            if ((this._player.getInt() > 17) && (this._skillId > BLESS_WEAPON)
                    && (this._skillId <= FREEZING_BLIZZARD)) { // LV7以上
                this._mpConsume--;
            }
            if ((this._player.getInt() > 18) && (this._skillId > DISEASE)
                    && (this._skillId <= FREEZING_BLIZZARD)) { // LV8以上
                this._mpConsume--;
            }

            if ((this._player.getInt() > 12) && (this._skillId >= SHOCK_STUN)
                    && (this._skillId <= COUNTER_BARRIER)) {
                this._mpConsume -= (this._player.getInt() - 12);
            }

            // MPの装备轻减
            if ((this._skillId == PHYSICAL_ENCHANT_DEX)
                    && this._player.getInventory().checkEquipped(20013)) { // 迅速ヘルム装备中にPE:DEX
                // this._mpConsume /= 2;
                this._mpConsume = this._mpConsume >> 1;
            }
            if ((this._skillId == HASTE)
                    && this._player.getInventory().checkEquipped(20013)) { // 迅速ヘルム装备中にヘイスト
                // this._mpConsume /= 2;
                this._mpConsume = this._mpConsume >> 1;
            }
            if ((this._skillId == HEAL)
                    && this._player.getInventory().checkEquipped(20014)) { // 治愈ヘルム装备中にヒール
                // this._mpConsume /= 2;
                this._mpConsume = this._mpConsume >> 1;
            }
            if ((this._skillId == EXTRA_HEAL)
                    && this._player.getInventory().checkEquipped(20014)) { // 治愈ヘルム装备中にエキストラヒール
                // this._mpConsume /= 2;
                this._mpConsume = this._mpConsume >> 1;
            }
            if ((this._skillId == ENCHANT_WEAPON)
                    && this._player.getInventory().checkEquipped(20015)) { // 力ヘルム装备中にエンチャントウエポン
                // this._mpConsume /= 2;
                this._mpConsume = this._mpConsume >> 1;
            }
            if ((this._skillId == DETECTION)
                    && this._player.getInventory().checkEquipped(20015)) { // 力ヘルム装备中に无所遁形术
                // this._mpConsume /= 2;
                this._mpConsume = this._mpConsume >> 1;
            }
            if ((this._skillId == PHYSICAL_ENCHANT_STR)
                    && this._player.getInventory().checkEquipped(20015)) { // 力ヘルム装备中にPE:STR
                // this._mpConsume /= 2;
                this._mpConsume = this._mpConsume >> 1;
            }
            if ((this._skillId == HASTE)
                    && this._player.getInventory().checkEquipped(20008)) { // マイナーウィンドヘルム装备中にヘイスト
                // this._mpConsume /= 2;
                this._mpConsume = this._mpConsume >> 1;
            }
            if ((this._skillId == GREATER_HASTE)
                    && this._player.getInventory().checkEquipped(20023)) { // ウィンドヘルム装备中にグレーターヘイスト
                // this._mpConsume /= 2;
                this._mpConsume = this._mpConsume >> 1;
            }

            // MPのオリジナルINT轻减
            if (this._player.getOriginalMagicConsumeReduction() > 0) {
                this._mpConsume -= this._player
                        .getOriginalMagicConsumeReduction();
            }
            //修正使用治盔 为0魔 hjx1000
            if (0 < this._skill.getMpConsume()) { // MPを消费するスキルであれば
                this._mpConsume = Math.max(this._mpConsume, 1); // 最低でも1消费する。
            }
        }

        if (currentHp < this._hpConsume + 1) {
            if (this._user instanceof L1PcInstance) {
                // 279 \f1因体力不足而无法使用魔法。
                this._player.sendPackets(new S_ServerMessage(279));
            }
            return false;

        } else if (currentMp < this._mpConsume) {
            if (this._user instanceof L1PcInstance) {
                // 278 \f1因魔力不足而无法使用魔法。
                this._player.sendPackets(new S_ServerMessage(278));
                if (this._player.isGm()) {
                    this._player.setCurrentMp(this._player.getMaxMp());
                }
            }
            return false;
        }

        return true;
    }

    // 必要材料があるか？
    // 判断技能的使用是否需要其他物品的辅助
    private boolean isItemConsume() {

        final int itemConsume = this._skill.getItemConsumeId();
        final int itemConsumeCount = this._skill.getItemConsumeCount();

        if (itemConsume == 0) {
            return true; // 材料を必要としない魔法
        }

        if (!this._player.getInventory().checkItem(itemConsume,
                itemConsumeCount)) {
            return false; // 必要材料が足りなかった。
        }

        return true;
    }

    /**
     * 使用技能后，相应的HP和MP、Lawful、材料的减少
     */
    private void useConsume() {
        if (this._user instanceof L1NpcInstance) {
            // NPCの场合、HP、MPのみマイナス
            final int current_hp = this._npc.getCurrentHp() - this._hpConsume;
            this._npc.setCurrentHp(current_hp);

            final int current_mp = this._npc.getCurrentMp() - this._mpConsume;
            this._npc.setCurrentMp(current_mp);
            return;
        }

        // HP?MPをマイナス
        if (this.isHPMPConsume()) {
            if (this._skillId == FINAL_BURN) { // ファイナル バーン
                this._player.setCurrentHp(1);
                this._player.setCurrentMp(0);

            } else {
                final int current_hp = this._player.getCurrentHp()
                        - this._hpConsume;
                this._player.setCurrentHp(current_hp);

                final int current_mp = this._player.getCurrentMp()
                        - this._mpConsume;
                this._player.setCurrentMp(current_mp);
            }
        }

        // Lawfulをマイナス
        int lawful = this._player.getLawful() + this._skill.getLawful();
        if (lawful > 32767) {
            lawful = 32767;
        }
        if (lawful < -32767) {
            lawful = -32767;
        }
        this._player.setLawful(lawful);

        final int itemConsume = this._skill.getItemConsumeId();
        final int itemConsumeCount = this._skill.getItemConsumeCount();

        if (itemConsume == 0) {
            return; // 材料を必要としない魔法
        }

        // 使用材料をマイナス
        this._player.getInventory().consumeItem(itemConsume, itemConsumeCount);
    }

    // マジックリストに追加する。
    // 使用相应技能要对玩家人物或者使用的相应道具追加必要动作
    private void addMagicList(final L1Character cha, final boolean repetition) {
        // /System.out.println("111111111111");
        if (_skillTime == 0) {
            _getBuffDuration = _skill.getBuffDuration() * 1000; // 效果时间
            if (_skill.getBuffDuration() == 0) {
                if (_skillId == INVISIBILITY) { // インビジビリティ
                    cha.setSkillEffect(INVISIBILITY, 0);
                }
                return;
            }
        } else {
            _getBuffDuration = _skillTime * 1000; // パラメータのtimeが0以外なら、效果时间として设定する
        }

        if (_skillId == SHOCK_STUN) {
            _getBuffDuration = _shockStunDuration;
        }

        if (_skillId == CURSE_POISON) { // カーズポイズンの效果处理はL1Poisonに移让。
            return;
        }
        if ((_skillId == CURSE_PARALYZE) || (_skillId == CURSE_PARALYZE2)) { // カーズパラライズの效果处理はL1CurseParalysisに移让。
            return;
        }
        if (_skillId == SHAPE_CHANGE) { // シェイプチェンジの效果处理はL1PolyMorphに移让。
            return;
        }
        if ((_skillId == BLESSED_ARMOR)
                || (_skillId == HOLY_WEAPON) // 武器?防具に效果がある处理はL1ItemInstanceに移让。
                || (_skillId == ENCHANT_WEAPON) || (_skillId == BLESS_WEAPON)
                || (_skillId == SHADOW_FANG)) {
            return;
        }
        if (((_skillId == ICE_LANCE) || (_skillId == FREEZING_BLIZZARD) || (_skillId == FREEZING_BREATH))
                && !_isFreeze) { // 冻结失败
            return;
        }
        /*
         * if ((this._skillId == AWAKEN_ANTHARAS) || (this._skillId ==
         * AWAKEN_FAFURION) || (this._skillId == AWAKEN_VALAKAS)) { //
         * 觉醒の效果处理はL1Awakeに移让。 return; }
         */
        final SkillMode mode = L1SkillMode.get().getSkill(this._skillId);
        if (mode == null) {
            cha.setSkillEffect(_skillId, _getBuffDuration);
        }
        // XXX
        if ((cha instanceof L1PcInstance) && repetition) { // 对象がPCで既にスキルが重复している场合
            final L1PcInstance pc = (L1PcInstance) cha;
            sendIcon(pc);
        }
    }

    /**
     * 发送技能图示
     * 
     * @param pc
     */
    private void sendIcon(final L1PcInstance pc) {
        if (this._skillTime == 0) {
            this._getBuffIconDuration = this._skill.getBuffDuration(); // 效果时间

        } else {
            this._getBuffIconDuration = this._skillTime; // パラメータのtimeが0以外なら、效果时间として设定する
        }

        // System.out.println("发送技能图示");
        switch (this._skillId) {
            case SHIELD: // シールド
                pc.sendPackets(new S_SkillIconShield(5,
                        this._getBuffIconDuration));
                break;

            case SHADOW_ARMOR: // シャドウ アーマー
                pc.sendPackets(new S_SkillIconShield(3,
                        this._getBuffIconDuration));
                break;

            case DRESS_DEXTERITY: // ドレス デクスタリティー
                pc.sendPackets(new S_Dexup(pc, 2, this._getBuffIconDuration));
                break;

            case DRESS_MIGHTY: // ドレス マイティー
                pc.sendPackets(new S_Strup(pc, 2, this._getBuffIconDuration));
                break;
                
            case DRESS_HALZ: // 黑暗籠罩
                pc.sendPackets(new S_PacketBoxIconAura(112,
                        this._getBuffIconDuration));
                break;

            case GLOWING_AURA: // グローウィング オーラ
                pc.sendPackets(new S_PacketBoxIconAura(113,
                        this._getBuffIconDuration));
                break;

            case SHINING_AURA: // シャイニング オーラ
                pc.sendPackets(new S_PacketBoxIconAura(114,
                        this._getBuffIconDuration));
                break;

            case BRAVE_AURA: // ブレイブ オーラ
                pc.sendPackets(new S_PacketBoxIconAura(116,
                        this._getBuffIconDuration));
                break;

            case FIRE_WEAPON: // ファイアー ウェポン
                pc.sendPackets(new S_PacketBoxIconAura(147,
                        this._getBuffIconDuration));
                break;

            case WIND_SHOT: // ウィンド ショット
                pc.sendPackets(new S_PacketBoxIconAura(148,
                        this._getBuffIconDuration));
                break;

            case FIRE_BLESS: // ファイアー ブレス
                pc.sendPackets(new S_PacketBoxIconAura(154,
                        this._getBuffIconDuration));
                break;

            case STORM_EYE: // ストーム アイ
                pc.sendPackets(new S_PacketBoxIconAura(155,
                        this._getBuffIconDuration));
                break;

            case EARTH_BLESS: // アース ブレス
                pc.sendPackets(new S_SkillIconShield(7,
                        this._getBuffIconDuration));
                break;

            case BURNING_WEAPON: // バーニング ウェポン
                pc.sendPackets(new S_PacketBoxIconAura(162,
                        this._getBuffIconDuration));
                break;

            case STORM_SHOT: // ストーム ショット
                pc.sendPackets(new S_PacketBoxIconAura(165,
                        this._getBuffIconDuration));
                break;

            case IRON_SKIN: // アイアン スキン
                pc.sendPackets(new S_SkillIconShield(10,
                        this._getBuffIconDuration));
                break;

            case EARTH_SKIN: // アース スキン
                pc.sendPackets(new S_SkillIconShield(6,
                        this._getBuffIconDuration));
                break;

            case PHYSICAL_ENCHANT_STR: // フィジカル エンチャント：STR
                pc.sendPackets(new S_Strup(pc, 5, this._getBuffIconDuration));
                break;

            case PHYSICAL_ENCHANT_DEX: // フィジカル エンチャント：DEX
                pc.sendPackets(new S_Dexup(pc, 5, this._getBuffIconDuration));
                break;

            case HASTE:
            case GREATER_HASTE: // グレーターヘイスト
                pc.sendPackets(new S_SkillHaste(pc.getId(), 1,
                        this._getBuffIconDuration));
                pc.broadcastPacketAll(new S_SkillHaste(pc.getId(), 1, 0));
                break;

            case HOLY_WALK:
            case MOVING_ACCELERATION:
            case WIND_WALK: // ホーリーウォーク、ムービングアクセレーション、ウィンドウォーク
                pc.sendPackets(new S_SkillBrave(pc.getId(), 4,
                        this._getBuffIconDuration));
                pc.broadcastPacketAll(new S_SkillBrave(pc.getId(), 4, 0));
                break;

            case BLOODLUST: // 血之渴望
                pc.sendPackets(new S_SkillBrave(pc.getId(), 6,
                        this._getBuffIconDuration));
                pc.broadcastPacketAll(new S_SkillBrave(pc.getId(), 6, 0));
                break;

            case SLOW:
            case MASS_SLOW:
            case ENTANGLE: // スロー、エンタングル、マススロー
                pc.sendPackets(new S_SkillHaste(pc.getId(), 2,
                        this._getBuffIconDuration));
                pc.broadcastPacketAll(new S_SkillHaste(pc.getId(), 2, 0));
                break;

            case IMMUNE_TO_HARM:
                pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_I2H,
                        this._getBuffIconDuration));
                break;
        }
        pc.sendPackets(new S_OwnCharStatus(pc));
    }

    // グラフィックの送信
    // 图解发送，技能使用完毕后发送结束提示图标
    private void sendGrfx(final boolean isSkillAction) {
        int actionId = _skill.getActionId();
        final int castgfx = _skill.getCastGfx();
        if (castgfx == 0) {
            return; // 表示するグラフィックが无い
        }

        // TODO 施展者为PC
        if (_user instanceof L1PcInstance) {
            if ((_skillId == FIRE_WALL) || (_skillId == LIFE_STREAM)) {
                final L1PcInstance pc = (L1PcInstance) _user;
                if (_skillId == FIRE_WALL) {
                    pc.setHeading(pc.targetDirection(_targetX, _targetY));
                    pc.sendPacketsAll(new S_ChangeHeading(pc));
                }
                pc.sendPacketsAll(new S_DoActionGFX(pc.getId(), actionId));
                return;
            }

            final int targetid = this._target.getId();

            if (_skillId == SHOCK_STUN) {
                if (_targetList.size() == 0) { // 失败
                    return;

                } else {
                    if (_target instanceof L1PcInstance) {
                        final L1PcInstance pc = (L1PcInstance) _target;
                        pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4434));

                    } else if (_target instanceof L1NpcInstance) {
                        _target.broadcastPacketX10(new S_SkillSound(_target
                                .getId(), 4434));
                    }
                    return;
                }
            }

            if (_skillId == LIGHT) {
                final L1PcInstance pc = (L1PcInstance) _target;
                pc.sendPackets(new S_Sound(145));
            }

            if ((_targetList.size() == 0)
                    && !(_skill.getTarget().equals("none"))) {
                // ターゲット数が０で对象を指定するスキルの场合、魔法使用エフェクトだけ表示して终了
                final int tempchargfx = _player.getTempCharGfx();
                switch (tempchargfx) {
                    case 5727:
                    case 5730: // シャドウ系变身のモーション对应
                        actionId = ActionCodes.ACTION_SkillBuff;
                        break;

                    case 5733:
                    case 5736:
                        actionId = ActionCodes.ACTION_Attack;
                        break;
                }
                if (isSkillAction) {
                    _player.sendPacketsX10(new S_DoActionGFX(_player.getId(),
                            actionId));
                }
                return;
            }

            if (_skill.getTarget().equals("attack") && (_skillId != 18)) {
                // 目标对象 是否为宠物 召唤兽 虚拟人物
                if (isPcSummonPet(_target)) {
                    if (_player.isSafetyZone() || // 自己位于安全区
                            _target.isSafetyZone() || // 目标位于安全区
                            _player.checkNonPvP(_player, _target) // 检查是否可以攻击
                    ) {
                        // 封包:物件攻击(NPC / PC 技能使用)
                        _player.sendPacketsX10(new S_UseAttackSkill(_player, 0,
                                castgfx, _targetX, _targetY, actionId, _dmg));
                        return;
                    }
                }

                // 单体攻击魔法
                if (_skill.getArea() == 0) {
                    // 封包:物件攻击(NPC / PC 技能使用)
                    _player.sendPacketsX10(new S_UseAttackSkill(_player,
                            targetid, castgfx, _targetX, _targetY, actionId,
                            _dmg));

                    // 有方向范围魔法
                } else {
                    // 封包:范围魔法
                    _player.sendPacketsX10(new S_RangeSkill(_player,
                            _targetList, castgfx, actionId,
                            S_RangeSkill.TYPE_DIR));
                }

            } else if (_skill.getTarget().equals("none")
                    && (_skill.getType() == L1Skills.TYPE_ATTACK)) { // 无方向范围攻击魔法
                // System.out.println("无方向范围攻击魔法 目标物件数量:" + _targetList.size());
                _player.sendPacketsX10(new S_RangeSkill(_player, _targetList,
                        castgfx, actionId, S_RangeSkill.TYPE_NODIR));

            } else { // 补助魔法
                // テレポート、マステレ、テレポートトゥマザー以外
                if ((_skillId != 5) && (_skillId != 69) && (_skillId != 131)) {
                    // 魔法を使う动作のエフェクトは使用者だけ
                    if (isSkillAction) {
                        _player.sendPacketsX10(new S_DoActionGFX(_player
                                .getId(), _skill.getActionId()));
                    }

                    if ((_skillId == COUNTER_MAGIC) || // 魔法屏障
                            (_skillId == COUNTER_BARRIER) || // 反击屏障
                            (_skillId == COUNTER_MIRROR)) {// 镜反射
                        _player.sendPackets(new S_SkillSound(targetid, castgfx));

                    } else if (_skillId == TRUE_TARGET) { // 精准目标
                        return;

                    } else if ((_skillId == AWAKEN_ANTHARAS) || // 觉醒：安塔瑞斯
                            (_skillId == AWAKEN_FAFURION) || // 觉醒：法利昂
                            (_skillId == AWAKEN_VALAKAS)) { // 觉醒：巴拉卡斯
                        if (_skillId == _player.getAwakeSkillId()) { // 再咏唱なら解除でエフェクトなし
                            _player.sendPacketsX10(new S_SkillSound(targetid,
                                    castgfx));

                        } else {
                            return;
                        }

                    } else {
                        _player.sendPacketsX10(new S_SkillSound(targetid,
                                castgfx));
                    }
                }

                // スキルのエフェクト表示はターゲット全员だが、あまり必要性がないので、ステータスのみ送信
                for (final TargetStatus ts : _targetList) {
                    final L1Character cha = ts.getTarget();
                    if (cha instanceof L1PcInstance) {
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.sendPackets(new S_OwnCharStatus(pc));
                    }
                }
            }

            // TODO 施展者是NPC
        } else if (this._user instanceof L1NpcInstance) { // NPCがスキルを使った场合
            final int targetid = this._target.getId();

            if (this._user instanceof L1MerchantInstance) {
                this._user.broadcastPacketX10(new S_SkillSound(targetid,
                        castgfx));
                return;
            }

            if ((this._targetList.size() == 0)
                    && !(this._skill.getTarget().equals("none"))) {
                // ターゲット数が０で对象を指定するスキルの场合、魔法使用エフェクトだけ表示して终了
                this._user.broadcastPacketX10(new S_DoActionGFX(this._user
                        .getId(), this._skill.getActionId()));
                return;
            }

            if (this._skill.getTarget().equals("attack")
                    && (this._skillId != 18)) {
                if (this._skill.getArea() == 0) { // 单体攻击魔法
                    this._user.broadcastPacketX10(new S_UseAttackSkill(
                            this._user, targetid, castgfx, this._targetX,
                            this._targetY, actionId, this._dmg));

                } else { // 有方向范围攻击魔法
                    this._user.broadcastPacketX10(new S_RangeSkill(this._user,
                            this._targetList, castgfx, actionId,
                            S_RangeSkill.TYPE_DIR));
                }

            } else if (this._skill.getTarget().equals("none")
                    && (this._skill.getType() == L1Skills.TYPE_ATTACK)) { // 无方向范围魔法
                // System.out.println("无方向范围魔法");
                this._user.broadcastPacketX10(new S_RangeSkill(this._user,
                        this._targetList, castgfx, actionId,
                        S_RangeSkill.TYPE_NODIR));

            } else { // 补助魔法
                // テレポート、マステレ、テレポートトゥマザー以外
                if ((this._skillId != 5) && (this._skillId != 69)
                        && (this._skillId != 131)) {
                    // 魔法を使う动作のエフェクトは使用者だけ
                    this._user.broadcastPacketX10(new S_DoActionGFX(this._user
                            .getId(), this._skill.getActionId()));
                    this._user.broadcastPacketX10(new S_SkillSound(targetid,
                            castgfx));
                }
            }
        }
    }

    // 不允许重复的技能组
    private static final int[][] REPEATEDSKILLS = {
            { FIRE_WEAPON, WIND_SHOT, STORM_EYE, BURNING_WEAPON,
                    STORM_SHOT/*, FIRE_BLESS*/ },

            { SHIELD, SHADOW_ARMOR, EARTH_SKIN, EARTH_BLESS, IRON_SKIN },

            { HOLY_WALK, MOVING_ACCELERATION, WIND_WALK, STATUS_BRAVE,
                    STATUS_ELFBRAVE, STATUS_RIBRAVE, BLOODLUST },

            { HASTE, GREATER_HASTE, STATUS_HASTE },

            { PHYSICAL_ENCHANT_DEX, DRESS_DEXTERITY },

            { PHYSICAL_ENCHANT_STR, DRESS_MIGHTY },

            { GLOWING_AURA, SHINING_AURA } };

    /**
     * 删除不能重复/同时使用的技能，图标更改为刚使用时的图标
     * 
     * @param cha
     */
    private void deleteRepeatedSkills(final L1Character cha) {
        for (final int[] skills : REPEATEDSKILLS) {
            for (final int id : skills) {
                if (id == _skillId) {
                    stopSkillList(cha, skills);
                }
            }
        }
    }

    /**
     * 删除全部重复的正在使用的技能
     * 
     * @param cha
     * @param repeat_skill
     */
    private void stopSkillList(final L1Character cha, final int[] repeat_skill) {
        for (final int skillId : repeat_skill) {
            if (skillId != _skillId) {
                cha.removeSkillEffect(skillId);
            }
        }
    }

    // 技能使用延迟的设定
    private void setDelay() {
        if (this._skill.getReuseDelay() > 0) {
            L1SkillDelay.onSkillUse(_user, _skill.getReuseDelay());
        }
    }

    /**
     * 发动技能效果
     */
    private void runSkill() {
        switch (_skillId) {
            case LIFE_STREAM:// 法师技能(治愈能量风暴)
                L1SpawnUtil.spawnEffect(81169, _skill.getBuffDuration(),
                        _targetX, _targetY, _user.getMapId(), _user, 0);
                return;

            case CUBE_IGNITION:// 幻术师技能(立方：燃烧)
                L1SpawnUtil.spawnEffect(80149, _skill.getBuffDuration(),
                        _targetX, _targetY, _user.getMapId(), _user, _skillId);
                return;

            case CUBE_QUAKE:// 幻术师技能(立方：地裂)
                L1SpawnUtil.spawnEffect(80150, _skill.getBuffDuration(),
                        _targetX, _targetY, _user.getMapId(), _user, _skillId);
                return;

            case CUBE_SHOCK:// 幻术师技能(立方：冲击)
                L1SpawnUtil.spawnEffect(80151, _skill.getBuffDuration(),
                        _targetX, _targetY, _user.getMapId(), _user, _skillId);
                return;

            case CUBE_BALANCE:// 幻术师技能(立方：和谐)
                L1SpawnUtil.spawnEffect(80152, _skill.getBuffDuration(),
                        _targetX, _targetY, _user.getMapId(), _user, _skillId);
                return;

            case FIRE_WALL:// 法师技能(火牢)
                // System.out.println("法师技能(火牢):"+_targetX+"/"+_targetY);
                L1SpawnUtil.doSpawnFireWall(_user, _targetX, _targetY);
                return;
        }

        // 有使用次数限制的技能使用成功与否的判断
        for (final int skillId : EXCEPT_COUNTER_MAGIC) {
            if (_skillId == skillId) {
                _isCounterMagic = false; // カウンターマジック无效
                break;
            }
        }

        // NPCにショックスタンを使用させるとonActionでNullPointerExceptionが発生するため
        // とりあえずPCが使用した时のみ
        if ((_skillId == SHOCK_STUN) && (_user instanceof L1PcInstance)) {
            _target.onAction(_player);
        }

        if (!this.isTargetCalc(_target)) {
            return;
        }

        try {
            TargetStatus ts = null;
            L1Character cha = null;
            // int dmg = 0;
            int drainMana = 0;
            int heal = 0;
            boolean isSuccess = false;
            int undeadType = 0;

            for (final Iterator<TargetStatus> iter = _targetList.iterator(); iter
                    .hasNext();) {
                ts = null;
                cha = null;
                // dmg = 0;
                heal = 0;
                isSuccess = false;
                undeadType = 0;

                ts = iter.next();
                cha = ts.getTarget();

                // System.out.println("发动技能效果");
                if (_npc != null) {
                    // 施展者是宠物 XXX
                    if (_npc instanceof L1PetInstance) {
                        if (isParty(_npc, cha)) {
                            ts.isCalc(false);
                            _dmg = 0;
                            continue;
                        }
                    }
                    // 施展者是召唤兽
                    if (_npc instanceof L1SummonInstance) {
                        if (isParty(_npc, cha)) {
                            ts.isCalc(false);
                            _dmg = 0;
                            continue;
                        }
                    }
                }

                if (!ts.isCalc() || !this.isTargetCalc(cha)) {
                    ts.isCalc(false);
                    continue; // 计算する必要がない。不需要计算
                }

                final L1Magic magic = new L1Magic(_user, cha);
                magic.setLeverage(getLeverage());

                if (cha instanceof L1MonsterInstance) { // アンデットの判定
                    undeadType = ((L1MonsterInstance) cha).getNpcTemplate()
                            .get_undead();
                }

                // 确率系スキルで失败が确定している场合
                // 概率系技能失败的确定
                if (((_skill.getType() == L1Skills.TYPE_CURSE) || (_skill
                        .getType() == L1Skills.TYPE_PROBABILITY))
                        && isTargetFailure(cha)) {
                    iter.remove();
                    continue;
                }
                
                if (cha instanceof L1PcInstance) { // ターゲットがPCの场合のみアイコンは送信する。
                    // 只有在目标为pc的情况下发送图标，代表使用成功
                    if (_skillTime == 0) {
                        _getBuffIconDuration = _skill.getBuffDuration(); // 效果时间

                    } else {
                        _getBuffIconDuration = _skillTime; // パラメータのtimeが0以外なら、效果时间として设定する
                    }
                }

                deleteRepeatedSkills(cha); // 删除重复的技能

                // System.out.println("NPC对PC伤害计算 XXX:"+this._skill.getType());
                switch (_skill.getType()) {
                    case L1Skills.TYPE_ATTACK:// 攻击系スキル＆ターゲットが使用者以外であること。
                        if (_user.getId() != cha.getId()) {
                            // 攻击系技能和使用者除外
                            if (isUseCounterMagic(cha)) { // カウンターマジックが発动した场合、リストから削除
                                iter.remove();
                                continue;
                            }
                            _dmg = magic.calcMagicDamage(_skillId);
                            cha.removeSkillEffect(ERASE_MAGIC); // 魔法消除
                        }
                        break;

                    case L1Skills.TYPE_CURSE:
                    case L1Skills.TYPE_PROBABILITY: // 确率系スキル
                        isSuccess = magic.calcProbabilityMagic(this._skillId);                        
                        if (_type == TYPE_GMBUFF) {
                            isSuccess = true;
                        }
                        if (this._skillId != ERASE_MAGIC) {
                            cha.removeSkillEffect(ERASE_MAGIC); // 魔法消除
                        }

                        if (this._skillId != FOG_OF_SLEEPING) {
                            cha.removeSkillEffect(FOG_OF_SLEEPING); // 沉睡之雾
                        }

                        if (isSuccess) { // 成功したがカウンターマジックが発动した场合、リストから削除
                            if (this.isUseCounterMagic(cha)) { // カウンターマジックが発动したか
                                iter.remove();
                                continue;
                            }

                        } else { // 失败した场合、リストから削除
                            if ((this._skillId == FOG_OF_SLEEPING)
                                    && (cha instanceof L1PcInstance)) {
                                final L1PcInstance pc = (L1PcInstance) cha;
                                // 297 你感觉些微地晕眩。
                                pc.sendPackets(new S_ServerMessage(297));
                            }
                            iter.remove();
                            continue;
                        }
                        break;

                    case L1Skills.TYPE_HEAL: // 回复系スキル
                        // 回复量はマイナスダメージで表现
                        this._dmg = -1 * magic.calcHealing(this._skillId);
                        if (cha.hasSkillEffect(WATER_LIFE)) { // 水之元气(回复量2倍)
                            // this._dmg *= 2;
                            // (>> 1: 除) (<< 1: 乘)
                            this._dmg = (this._dmg << 1);
                        }

                        if (cha.hasSkillEffect(POLLUTE_WATER)) { // 污浊之水(回复量1/2倍)
                            // this._dmg /= 2;
                            // (>> 1: 除) (<< 1: 乘)
                            this._dmg = (this._dmg >> 1);
                        }

                        if (cha.hasSkillEffect(ADLV80_2_2)) {// 污浊的水流(水龙副本
                                                             // 回复量1/2倍)
                            this._dmg = (this._dmg >> 1);
                        }

                        if (cha.hasSkillEffect(ADLV80_2_3)) {
                            this._dmg *= -1;
                        }
                        break;
                }

                // TODO SKILL移转               
                final SkillMode mode = L1SkillMode.get()
                        .getSkill(this._skillId);
                if (mode != null) {
                    // 施展者是PC
                    if (this._user instanceof L1PcInstance) {
                        switch (this._skillId) {
                            case TELEPORT:// 指定传送5
                            case MASS_TELEPORT:// 集体传送术69
                                this._dmg = mode.start(this._player, cha,
                                        magic, this._bookmarkId);
                                break;

                            case CALL_CLAN:// 呼唤盟友
                            case RUN_CLAN:// 援护盟友118
                                this._dmg = mode.start(this._player, cha,
                                        magic, this._targetID);
                                break;

                            default:
//                                this._dmg = mode.start(this._player, cha,
//                                        magic, this._getBuffIconDuration);
                            	//修正技能时间.hjx1000
                                this._dmg = mode.start(this._player, cha,
                                        magic, this._skill.getBuffDuration());
                                //System.out.println("===="+this._skill.getBuffDuration());
                                break;
                        }
                    }
                    // 施展者是NPC
                    if (this._user instanceof L1NpcInstance) {
//                        this._dmg = mode.start(this._npc, cha, magic,
//                                this._getBuffIconDuration);
                    	//修正技能时间.hjx1000
                        this._dmg = mode.start(this._npc, cha, magic,
                        		this._skill.getBuffDuration());
                    }

                } else {
                    // ■■■■ 个别处理のあるスキルのみ书いてください。 ■■■■
                    // 需要个别处理的技能（无法简单以技能的属系做判断）
                    // すでにスキルを使用济みの场合なにもしない 重复使用无效的技能
                    // ただしショックスタンは重ねがけ出来るため例外 冲击之晕例外
                    if (cha.hasSkillEffect(this._skillId)) {
                        this.addMagicList(cha, true); // ターゲットに魔法の效果时间を上书き
                        if (this._skillId != SHAPE_CHANGE) { // 变形术
                            continue;
                        }
                    }
                }

                if (this._skillId == DETECTION) { // 无所遁形术
                    if (cha instanceof L1NpcInstance) {
                        final L1NpcInstance npc = (L1NpcInstance) cha;
                        final int hiddenStatus = npc.getHiddenStatus();
                        if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_SINK) {
                            npc.appearOnGround(this._player);
                        }
                    }

                } else if (this._skillId == COUNTER_DETECTION) { // カウンター无所遁形术
                    if (cha instanceof L1PcInstance) {
                        this._dmg = magic.calcMagicDamage(this._skillId);

                    } else if (cha instanceof L1NpcInstance) {
                        final L1NpcInstance npc = (L1NpcInstance) cha;
                        final int hiddenStatus = npc.getHiddenStatus();
                        if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_SINK) {
                            npc.appearOnGround(this._player);
                        } else {
                            this._dmg = 0;
                        }

                    } else {
                        this._dmg = 0;
                    }

                    // ★★★ 回复系スキル ★★★ 恢复系技能
                } else if ((this._skillId == HEAL)
                        || (this._skillId == EXTRA_HEAL)
                        || (this._skillId == GREATER_HEAL)
                        || (this._skillId == FULL_HEAL)
                        || (this._skillId == HEAL_ALL)
                        || (this._skillId == NATURES_TOUCH)
                        || (this._skillId == NATURES_BLESSING)) {
                    if (this._user instanceof L1PcInstance) {
                        cha.removeSkillEffect(WATER_LIFE);
                    }

                    // ★★★ 攻击系スキル ★★★ 攻击系技能
                    // チルタッチ、バンパイアリックタッチ
                } else if ((this._skillId == CHILL_TOUCH)
                        || (this._skillId == VAMPIRIC_TOUCH)) {
                    heal = this._dmg;

                } else if ((this._skillId == 10026) || (this._skillId == 10027)
                        || (this._skillId == 10028) || (this._skillId == 10029)) { // 安息攻击
                    if (this._user instanceof L1NpcInstance) {
                        this._user.broadcastPacketX8(new S_NpcChat(this._npc,
                                "$3717")); // さあ、おまえに安息を与えよう。

                    } else {
                        this._player.broadcastPacketX8(new S_Chat(this._player,
                                "$3717")); // さあ、おまえに安息を与えよう。
                    }

                } else if (this._skillId == 10057) { // 引き寄せ
                    L1Teleport.teleportToTargetFront(cha, this._user, 1);

                    // ★★★ 确率系スキル ★★★ 确率系技能
                } else if ((this._skillId == SLOW)
                        || (this._skillId == MASS_SLOW)
                        || (this._skillId == ENTANGLE)) { // スロー、マス
                    // スロー、エンタングル
                    if (cha instanceof L1PcInstance) {
                        final L1PcInstance pc = (L1PcInstance) cha;
                        if (pc.getHasteItemEquipped() > 0) {
                            continue;
                        }
                    }
                    if (cha.getBraveSpeed() == 5) {// 具有强化勇水状态
                        continue;
                    }
                    switch (cha.getMoveSpeed()) {
                        case 0:
                            if (cha instanceof L1PcInstance) {
                                final L1PcInstance pc = (L1PcInstance) cha;
                                pc.sendPackets(new S_SkillHaste(pc.getId(), 2,
                                        this._getBuffIconDuration));
                            }
                            cha.broadcastPacketAll(new S_SkillHaste(
                                    cha.getId(), 2, this._getBuffIconDuration));
                            cha.setMoveSpeed(2);
                            break;

                        case 1:
                            int skillNum = 0;
                            if (cha.hasSkillEffect(HASTE)) {
                                skillNum = HASTE;

                            } else if (cha.hasSkillEffect(GREATER_HASTE)) {
                                skillNum = GREATER_HASTE;

                            } else if (cha.hasSkillEffect(STATUS_HASTE)) {
                                skillNum = STATUS_HASTE;
                            }

                            if (skillNum != 0) {
                                cha.removeSkillEffect(skillNum);
                                cha.removeSkillEffect(this._skillId);
                                cha.setMoveSpeed(0);
                                continue;
                            }
                            break;
                    }

                    /*
                     * } else if ((this._skillId == CURSE_BLIND) ||
                     * (this._skillId == DARKNESS)) { if (cha instanceof
                     * L1PcInstance) { final L1PcInstance pc = (L1PcInstance)
                     * cha; if (pc.hasSkillEffect(STATUS_FLOATING_EYE)) {
                     * pc.sendPackets(new S_CurseBlind(2));
                     * 
                     * } else { pc.sendPackets(new S_CurseBlind(1)); } }
                     */

                } else if (this._skillId == CURSE_POISON) {
                    L1DamagePoison.doInfection(this._user, cha, 3000, 5);

                    /*
                     * } else if ((this._skillId == CURSE_PARALYZE) ||
                     * (this._skillId == CURSE_PARALYZE2)) { if
                     * (!cha.hasSkillEffect(EARTH_BIND) &&
                     * !cha.hasSkillEffect(ICE_LANCE) &&
                     * !cha.hasSkillEffect(FREEZING_BLIZZARD) &&
                     * !cha.hasSkillEffect(FREEZING_BREATH)) { if (cha
                     * instanceof L1PcInstance) { L1CurseParalysis.curse(cha,
                     * 8000, 16000);
                     * 
                     * } else if (cha instanceof L1MonsterInstance) {
                     * L1CurseParalysis.curse(cha, 0, 16000); } }
                     */

                } else if (this._skillId == WEAKNESS) { // ウィークネス
                    if (cha instanceof L1PcInstance) {
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addDmgup(-5);
                        pc.addHitup(-1);
                    }

                } else if (this._skillId == DISEASE) { // ディジーズ
                    if (cha instanceof L1PcInstance) {
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addDmgup(-6);
                        pc.addAc(12);
                    }

                } else if ((this._skillId == ICE_LANCE) // アイスランス
                        || (this._skillId == FREEZING_BLIZZARD) // フリージングブリザード
                        || (this._skillId == FREEZING_BREATH)) { // フリージングブレス
                    // 计算攻击是否成功
                    this._isFreeze = magic.calcProbabilityMagic(this._skillId);
                    if (this._isFreeze) {
                        // final int time = this._skill.getBuffDuration() *
                        // 1000;
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            // 法师技能(冰矛围篱)
                            L1SpawnUtil.spawnEffect(81168,
                                    this._skill.getBuffDuration(), cha.getX(),
                                    cha.getY(), cha.getMapId(), this._user, 0);

                            pc.sendPacketsAll(new S_Poison(pc.getId(), 2));
                            pc.sendPackets(new S_Paralysis(
                                    S_Paralysis.TYPE_FREEZE, true));

                        } else if ((cha instanceof L1MonsterInstance)
                                || (cha instanceof L1SummonInstance)
                                || (cha instanceof L1PetInstance)) {
                            final L1NpcInstance npc = (L1NpcInstance) cha;
                            // 法师技能(冰矛围篱)
                            L1SpawnUtil.spawnEffect(81168,
                                    this._skill.getBuffDuration(), cha.getX(),
                                    cha.getY(), cha.getMapId(), this._user, 0);

                            npc.broadcastPacketAll(new S_Poison(npc.getId(), 2));
                            npc.setParalyzed(true);
                        }
                    }

                } else if (this._skillId == EARTH_BIND) { // アースバインド
                    if (cha instanceof L1PcInstance) {
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.sendPacketsAll(new S_Poison(pc.getId(), 2));
                        pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE,
                                true));

                    } else if ((cha instanceof L1MonsterInstance)
                            || (cha instanceof L1SummonInstance)
                            || (cha instanceof L1PetInstance)) {
                        final L1NpcInstance npc = (L1NpcInstance) cha;

                        npc.broadcastPacketAll(new S_Poison(npc.getId(), 2));
                        npc.setParalyzed(true);
                    }
				} else if (this._skillId == THUNDER_GRAB) {// 夺命之雷192
					//final Random rad = new Random();
					//final int i = rad.nextInt(100) + 1;
					//if (i <= 60) {
					isSuccess = magic.calcProbabilityMagic(this._skillId);
					if (isSuccess) {
						if (!cha.hasSkillEffect(STATUS_FREEZE)) {
							final Random random = new Random();
					        final int[] grabTimeArray = { 1000, 2000, 3000, 4000};
					        final int rnd = random.nextInt(grabTimeArray.length);
					        final int thunder = grabTimeArray[rnd];
							if (cha instanceof L1PcInstance) {
								final L1PcInstance pc = (L1PcInstance) cha;
								pc.setSkillEffect(STATUS_FREEZE, thunder);
								//pc.setSkillEffect(THUNDER_GRAB, 2000);
								pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true));
//								pc.sendPackets(new S_SkillSound(pc.getId(), 0x1058));//夺命之雷效果 hjx1000
//								pc.broadcastPacketAll(new S_SkillSound(pc.getId(), 0x1058));//夺命之雷效果 hjx1000
							} else if ((cha instanceof L1MonsterInstance)
									|| (cha instanceof L1SummonInstance)
									|| (cha instanceof L1PetInstance)) {
								final L1NpcInstance npc = (L1NpcInstance) cha ;
								npc.setSkillEffect(DARKNESS, thunder);
								//npc.setParalyzed(true);
//								npc.broadcastPacketAll(new S_SkillSound(npc.getId(), 0x1058));//夺命之雷效果 hjx1000
							}
				            L1SpawnUtil.spawnEffect(81182, thunder, cha.getX(), cha.getY(),
				                    cha.getMapId(), cha, 0);
						}
					}
//				} else if (this._skillId == DRESS_HALZ) {// 黑暗笼罩112
//					if (isSuccess) {
//						if (!cha.hasSkillEffect(STATUS_FREEZE)) {
//							if ((cha instanceof L1MonsterInstance) 
//								|| (cha instanceof L1SummonInstance)
//								|| (cha instanceof L1PetInstance)) {
//								final L1NpcInstance npc = (L1NpcInstance) cha ;
//								final Random random = new Random();
//						        final int[] grabTimeArray = { 1000, 2000, 3000};
//						        final int rnd = random.nextInt(grabTimeArray.length);
//						        final int rndtime = grabTimeArray[rnd];
//								npc.setSkillEffect(STATUS_FREEZE, rndtime);
//								npc.setParalyzed(true);
//							}
//						}
//					}
                    /*
                     * } else if (this._skillId == WIND_SHACKLE) { // ウィンド シャックル
                     * if (cha instanceof L1PcInstance) { final L1PcInstance pc
                     * = (L1PcInstance) cha; pc.sendPackets(new
                     * S_PacketBoxWindShackle(pc.getId(),
                     * this._getBuffIconDuration)); }
                     */

                    /*
                     * } else if (this._skillId == CANCELLATION) { // 对象是NPC if
                     * (cha instanceof L1NpcInstance) { final L1NpcInstance npc
                     * = (L1NpcInstance) cha; // 取回NPCID final int npcId =
                     * npc.getNpcTemplate().get_npcId(); switch (npcId) { case
                     * KnightLv45_1._searcherid:// 调查员 if (npc.getGfxId() ==
                     * npc.getTempCharGfx()) { npc.setTempCharGfx(1314);
                     * npc.broadcastPacketAll(new S_ChangeShape(npc, 1314));
                     * return;
                     * 
                     * } else { return; }
                     * 
                     * case ElfLv45_2._npcId:// 独角兽 if (npc.getGfxId() ==
                     * npc.getTempCharGfx()) { final int x = npc.getX(); final
                     * int y = npc.getY(); final short m = npc.getMapId(); final
                     * int h = npc.getHeading(); npc.deleteMe();
                     * L1SpawnUtil.spawnT(45641, x, y, m, h, 300); return;
                     * 
                     * } else { return; }
                     * 
                     * case EWLv40_1._roiid:// 罗伊 if (npc.getGfxId() ==
                     * npc.getTempCharGfx()) { npc.setTempCharGfx(4310);
                     * npc.broadcastPacketAll(new S_ChangeShape(npc, 4310));
                     * return;
                     * 
                     * } else { return; } } } if ((this._player != null) &&
                     * this._player.isInvisble()) { this._player.delInvis(); }
                     * if (!(cha instanceof L1PcInstance)) { final L1NpcInstance
                     * npc = (L1NpcInstance) cha; npc.setMoveSpeed(0);
                     * npc.setBraveSpeed(0); npc.broadcastPacketAll(new
                     * S_SkillHaste(cha.getId(), 0, 0));
                     * npc.broadcastPacketAll(new S_SkillBrave(cha.getId(), 0,
                     * 0)); npc.setWeaponBreaked(false);
                     * npc.setParalyzed(false); npc.setParalysisTime(0); } //
                     * スキルの解除 技能解除 for (int skillNum = SKILLS_BEGIN; skillNum <=
                     * SKILLS_END; skillNum++) { if
                     * (this.isNotCancelable(skillNum) && !cha.isDead()) {
                     * continue; } cha.removeSkillEffect(skillNum); }
                     * 
                     * // ステータス强化、异常の解除 cha.curePoison(); cha.cureParalaysis();
                     * for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_END;
                     * skillNum++) { if ((skillNum == STATUS_CHAT_PROHIBITED //
                     * チャット禁止は解除しない ) || (skillNum == STATUS_CURSE_BARLOG //
                     * バルログの咒いは解除しない ) || (skillNum == STATUS_CURSE_YAHEE)) { //
                     * ヤヒの咒いは解除しない continue; } cha.removeSkillEffect(skillNum);
                     * }
                     * 
                     * // 料理の解除 for (int skillNum = COOKING_BEGIN; skillNum <=
                     * COOKING_END; skillNum++) { if
                     * (this.isNotCancelable(skillNum)) { continue; }
                     * cha.removeSkillEffect(skillNum); }
                     * 
                     * // ヘイストアイテム装备时はヘイスト关连のスキルが何も挂かっていないはずなのでここで解除 if (cha
                     * instanceof L1PcInstance) { final L1PcInstance pc =
                     * (L1PcInstance) cha; if (pc.getHasteItemEquipped() > 0) {
                     * pc.setMoveSpeed(0); pc.sendPacketsAll(new
                     * S_SkillHaste(pc.getId(), 0, 0)); } }
                     * cha.removeSkillEffect(STATUS_FREEZE); // Freeze解除 if (cha
                     * instanceof L1PcInstance) { final L1PcInstance pc =
                     * (L1PcInstance) cha; pc.sendPacketsAll(new
                     * S_CharVisualUpdate(pc)); if (pc.isPrivateShop()) {
                     * pc.sendPacketsAll(new S_DoActionShop(pc.getId(),
                     * pc.getShopChat())); } if (this._user instanceof
                     * L1PcInstance) { L1PinkName.onAction(pc, this._user); } }
                     */

                } else if (this._skillId == TURN_UNDEAD) {
                    if ((undeadType == 1) || (undeadType == 3)) {
                        // ダメージを对象のHPとする。
                        this._dmg = cha.getCurrentHp();
                    }

                } else if (this._skillId == MANA_DRAIN) { // マナ ドレイン
                    final Random random = new Random();
                    final int chance = random.nextInt(10) + 5;
                    drainMana = chance + (this._user.getInt() / 2);
                    if (cha.getCurrentMp() < drainMana) {
                        drainMana = cha.getCurrentMp();
                    }

                } else if (this._skillId == WEAPON_BREAK) { // ウェポン ブレイク
                    /*
                     * 对NPCの场合、L1Magicのダメージ算出でダメージ1/2としているので
                     * こちらには、对PCの场合しか记入しない。 损伤量は1~(int/3)まで
                     */
                    if ((this._calcType == PC_PC) || (this._calcType == NPC_PC)) {
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            final L1ItemInstance weapon = pc.getWeapon();
                            if (weapon != null) {
                                final Random random = new Random();
                                final int weaponDamage = random
                                        .nextInt(this._user.getInt() / 3) + 1;
                                // \f1あなたの%0が损伤しました。
                                pc.sendPackets(new S_ServerMessage(268, weapon
                                        .getLogName()));
                                pc.getInventory().receiveDamage(weapon,
                                        weaponDamage);
                            }
                        }
                    } else {
                        ((L1NpcInstance) cha).setWeaponBreaked(true);
                    }

                } else if (this._skillId == FOG_OF_SLEEPING) {
                    if (cha instanceof L1PcInstance) {
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_SLEEP,
                                true));
                    }
                    cha.setSleeped(true);

                } else if (this._skillId == GUARD_BRAKE) { // ガードブレイク
                    if (cha instanceof L1PcInstance) {
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addAc(15);
                    }

                } else if (this._skillId == HORROR_OF_DEATH) { // ホラーオブデス
                    if (cha instanceof L1PcInstance) {
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addStr(-3);
                        pc.addInt(-3);
                    }
                }

                // ●●●● PCにしか效果のないスキル ●●●● 只允许pc使用的技能
                if ((this._calcType == PC_PC) || (this._calcType == NPC_PC)) {
                    // ★★★ 特殊系スキル★★★ 特殊技能
                    if (this._skillId == CREATE_MAGICAL_WEAPON) { // クリエイト
                        // マジカル ウェポン
                        final L1PcInstance pc = (L1PcInstance) cha;
                        final L1ItemInstance item = pc.getInventory().getItem(
                                this._itemobjid);
                        if ((item != null) && (item.getItem().getType2() == 1)) {
                            final int item_type = item.getItem().getType2();
                            final int safe_enchant = item.getItem()
                                    .get_safeenchant();
                            final int enchant_level = item.getEnchantLevel();
                            String item_name = item.getName();
                            if (safe_enchant < 0) { // 强化不可
                                pc.sendPackets( // \f1何も起きませんでした。
                                new S_ServerMessage(79));
                            } else if (safe_enchant == 0) { // 安全圏+0
                                pc.sendPackets( // \f1何も起きませんでした。
                                new S_ServerMessage(79));
                            } else if ((item_type == 1) && (enchant_level == 0)) {
                                if (!item.isIdentified()) {// 未鉴定
                                    pc.sendPackets( // \f1%0が%2%1光ります。
                                    new S_ServerMessage(161, item_name, "$245",
                                            "$247"));
                                } else {
                                    item_name = "+0 " + item_name;
                                    pc.sendPackets( // \f1%0が%2%1光ります。
                                    new S_ServerMessage(161, "+0 " + item_name,
                                            "$245", "$247"));
                                }
                                item.setEnchantLevel(1);
                                pc.getInventory().updateItem(item,
                                        L1PcInventory.COL_ENCHANTLVL);
                            } else {
                                pc.sendPackets( // \f1何も起きませんでした。
                                new S_ServerMessage(79));
                            }
                        } else {
                            pc.sendPackets( // \f1何も起きませんでした。
                            new S_ServerMessage(79));
                        }

                    } else if (this._skillId == BRING_STONE) { // ブリング ストーン
                        final L1PcInstance pc = (L1PcInstance) cha;
                        final Random random = new Random();
                        final L1ItemInstance item = pc.getInventory().getItem(
                                this._itemobjid);
                        if (item != null) {
                            final int dark = (int) (10 + (pc.getLevel() * 0.8) + (pc
                                    .getWis() - 6) * 1.2);
                            final int brave = (int) (dark / 2.1);
                            final int wise = (int) (brave / 2.0);
                            final int kayser = (int) (wise / 1.9);
                            final int chance = random.nextInt(100) + 1;
                            if (item.getItem().getItemId() == 40320) {
                                pc.getInventory().removeItem(item, 1);
                                if (dark >= chance) {
                                    pc.getInventory().storeItem(40321, 1);
                                    pc.sendPackets(new S_ServerMessage(403,
                                            "$2475")); // %0を手に入れました。
                                } else {
                                    pc.sendPackets(new S_ServerMessage(280)); // \f1魔法が失败しました。
                                }
                            } else if (item.getItem().getItemId() == 40321) {
                                pc.getInventory().removeItem(item, 1);
                                if (brave >= chance) {
                                    pc.getInventory().storeItem(40322, 1);
                                    pc.sendPackets(new S_ServerMessage(403,
                                            "$2476")); // %0を手に入れました。
                                } else {
                                    pc.sendPackets(new S_ServerMessage(280)); // \f1魔法が失败しました。
                                }
                            } else if (item.getItem().getItemId() == 40322) {
                                pc.getInventory().removeItem(item, 1);
                                if (wise >= chance) {
                                    pc.getInventory().storeItem(40323, 1);
                                    pc.sendPackets(new S_ServerMessage(403,
                                            "$2477")); // %0を手に入れました。
                                } else {
                                    pc.sendPackets(new S_ServerMessage(280)); // \f1魔法が失败しました。
                                }
                            } else if (item.getItem().getItemId() == 40323) {
                                pc.getInventory().removeItem(item, 1);
                                if (kayser >= chance) {
                                    pc.getInventory().storeItem(40324, 1);
                                    pc.sendPackets(new S_ServerMessage(403,
                                            "$2478")); // %0を手に入れました。
                                } else {
                                    pc.sendPackets(new S_ServerMessage(280)); // \f1魔法が失败しました。
                                }
                            }
                        }

                        /*
                         * } else if ((this._skillId == LESSER_ELEMENTAL) ||
                         * (this._skillId == GREATER_ELEMENTAL)) { //
                         * レッサーエレメンタル、グレーターエレメンタル final L1PcInstance pc =
                         * (L1PcInstance) cha; final int attr = pc.getElfAttr();
                         * if (attr != 0) { // 无属性でなければ实行 if
                         * (!pc.getMap().isRecallPets()) { // 353：在这附近无法召唤怪物。
                         * pc.sendPackets(new S_ServerMessage(353)); return; }
                         * 
                         * int petcost = 0; final Object[] petlist =
                         * pc.getPetList().values() .toArray(); for (final
                         * Object pet : petlist) { // 现在のペットコスト petcost +=
                         * ((L1NpcInstance) pet) .getPetcost(); }
                         * 
                         * if (petcost == 0) { // 1匹も所属NPCがいなければ实行 int summonid
                         * = 0; int summons[]; if (this._skillId ==
                         * LESSER_ELEMENTAL) { // レッサーエレメンタル[地,火,水,风] summons =
                         * new int[] { 45306, 45303, 45304, 45305 }; } else { //
                         * グレーターエレメンタル[地,火,水,风] summons = new int[] { 81053,
                         * 81050, 81051, 81052 }; } int npcattr = 1; for (int i
                         * = 0; i < summons.length; i++) { if (npcattr == attr)
                         * { summonid = summons[i]; i = summons.length; }
                         * npcattr *= 2; } // 特殊设定の场合ランダムで出现 if (summonid == 0)
                         * { final Random random = new Random(); final int k3 =
                         * random.nextInt(4); summonid = summons[k3]; }
                         * 
                         * final L1Npc npcTemp = NpcTable.get()
                         * .getTemplate(summonid); final L1SummonInstance summon
                         * = new L1SummonInstance( npcTemp, pc);
                         * summon.setPetcost(pc.getCha() + 7); //
                         * 精灵の他にはNPCを所属させられない } }
                         */

                    } else if (this._skillId == ABSOLUTE_BARRIER) { // アブソルート
                                                                    // バリア
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.stopHpRegeneration();
                        pc.stopMpRegeneration();
                    }

                    // ★★★ 变化系スキル（エンチャント） ★★★ 变化系技能
                    if (this._skillId == LIGHT) { // ライト
                        // addMagicList()后に、turnOnOffLight()でパケット送信

                    } else if (this._skillId == GLOWING_AURA) { // グローウィング オーラ
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addHitup(5);
                        pc.addBowHitup(5);
                        pc.addMr(20);
                        pc.sendPackets(new S_SPMR(pc));
                        pc.sendPackets(new S_PacketBoxIconAura(113,
                                this._getBuffIconDuration));

                    } else if (this._skillId == SHINING_AURA) { // シャイニング オーラ
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addAc(-8);
                        pc.sendPackets(new S_PacketBoxIconAura(114,
                                this._getBuffIconDuration));

                    } else if (this._skillId == BRAVE_AURA) { // ブレイブ オーラ
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addDmgup(5);
                        pc.sendPackets(new S_PacketBoxIconAura(116,
                                this._getBuffIconDuration));

                    } else if (this._skillId == SHIELD) { // シールド
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addAc(-2);
                        pc.sendPackets(new S_SkillIconShield(5,
                                this._getBuffIconDuration));

                    } else if (this._skillId == SHADOW_ARMOR) { // シャドウ アーマー
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addAc(-3);
                        pc.sendPackets(new S_SkillIconShield(3,
                                this._getBuffIconDuration));

                    } else if (this._skillId == DRESS_DEXTERITY) { // ドレス
                                                                   // デクスタリティー
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addDex((byte) 3);
                        pc.sendPackets(new S_Dexup(pc, 2,
                                this._getBuffIconDuration));

                    } else if (this._skillId == DRESS_MIGHTY) { // ドレス マイティー
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addStr((byte) 3);
                        pc.sendPackets(new S_Strup(pc, 2,
                                this._getBuffIconDuration));

                    } else if (this._skillId == SHADOW_FANG) { // シャドウ ファング
                        final L1PcInstance pc = (L1PcInstance) cha;
                        final L1ItemInstance item = pc.getInventory().getItem(
                                this._itemobjid);
                        if ((item != null) && (item.getItem().getType2() == 1)) {
                            item.setSkillWeaponEnchant(pc, this._skillId,
                                    this._skill.getBuffDuration() * 1000);
                        } else {
                            pc.sendPackets(new S_ServerMessage(79));
                        }

                    } else if (this._skillId == ENCHANT_WEAPON) { // エンチャント ウェポン
                        final L1PcInstance pc = (L1PcInstance) cha;
                        final L1ItemInstance item = pc.getInventory().getItem(
                                this._itemobjid);
                        if ((item != null) && (item.getItem().getType2() == 1)) {
                            pc.sendPackets(new S_ServerMessage(161, item
                                    .getLogName(), "$245", "$247"));
                            item.setSkillWeaponEnchant(pc, this._skillId,
                                    this._skill.getBuffDuration() * 1000);
                        } else {
                            pc.sendPackets(new S_ServerMessage(79));
                        }

                    } else if ((this._skillId == HOLY_WEAPON // ホーリー ウェポン
                            )
                            || (this._skillId == BLESS_WEAPON)) { // ブレス ウェポン
                        if (!(cha instanceof L1PcInstance)) {
                            return;
                        }
                        final L1PcInstance pc = (L1PcInstance) cha;
                        if (pc.getWeapon() == null) {
                            pc.sendPackets(new S_ServerMessage(79));
                            return;
                        }
                        for (final L1ItemInstance item : pc.getInventory()
                                .getItems()) {
                            if (pc.getWeapon().equals(item)) {
                                pc.sendPackets(new S_ServerMessage(161, item
                                        .getLogName(), "$245", "$247"));
                                item.setSkillWeaponEnchant(pc, this._skillId,
                                        this._skill.getBuffDuration() * 1000);
                                return;
                            }
                        }

                    } else if (this._skillId == BLESSED_ARMOR) { // ブレスド アーマー
                        final L1PcInstance pc = (L1PcInstance) cha;
                        final L1ItemInstance item = pc.getInventory().getItem(
                                this._itemobjid);
                        if ((item != null) && (item.getItem().getType2() == 2)
                                && (item.getItem().getType() == 2)) {
                            pc.sendPackets(new S_ServerMessage(161, item
                                    .getLogName(), "$245", "$247"));
//                            item.setSkillArmorEnchant(pc, this._skillId,
//                                    this._skill.getBuffDuration() * 1000);
                            if (!pc.hasSkillEffect(BLESSED_ARMOR) &&
                            		pc.getInventory().getTypeEquipped(2, 2) >= 1) {
                            	pc.addAc(-3);
                            }
                            pc.setSkillEffect(BLESSED_ARMOR, this._skill.getBuffDuration() * 1000);

                        } else {
                            pc.sendPackets(new S_ServerMessage(79));
                        }

                    } else if (this._skillId == EARTH_BLESS) { // アース ブレス
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addAc(-7);
                        pc.sendPackets(new S_SkillIconShield(7,
                                this._getBuffIconDuration));

                    } else if (this._skillId == RESIST_MAGIC) { // レジスト マジック
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addMr(10);
                        pc.sendPackets(new S_SPMR(pc));

                    } else if (this._skillId == CLEAR_MIND) { // クリアー マインド
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addWis((byte) 3);
                        pc.resetBaseMr();

                    } else if (this._skillId == RESIST_ELEMENTAL) { // レジスト
                                                                    // エレメント
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addWind(10);
                        pc.addWater(10);
                        pc.addFire(10);
                        pc.addEarth(10);
                        pc.sendPackets(new S_OwnCharAttrDef(pc));

                    } else if (this._skillId == ELEMENTAL_PROTECTION) { // エレメンタルプロテクション
                        final L1PcInstance pc = (L1PcInstance) cha;
                        final int attr = pc.getElfAttr();
                        if (attr == 1) {
                            pc.addEarth(50);
                        } else if (attr == 2) {
                            pc.addFire(50);
                        } else if (attr == 4) {
                            pc.addWater(50);
                        } else if (attr == 8) {
                            pc.addWind(50);
                        }
                    } else if ((this._skillId == INVISIBILITY)
                            || (this._skillId == BLIND_HIDING)) { // インビジビリティ、ブラインドハイディング
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.sendPackets(new S_Invis(pc.getId(), 1));
                        pc.broadcastPacketAll(new S_RemoveObject(pc));
                        pc.setSkillEffect(20005, 5000); //隐身延时3秒 hjx1000

                    } else if (this._skillId == IRON_SKIN) { // アイアン スキン
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addAc(-10);
                        pc.sendPackets(new S_SkillIconShield(10,
                                this._getBuffIconDuration));

                    } else if (this._skillId == EARTH_SKIN) { // アース スキン
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addAc(-6);
                        pc.sendPackets(new S_SkillIconShield(6,
                                this._getBuffIconDuration));

                    } else if (this._skillId == PHYSICAL_ENCHANT_STR) { // フィジカルエンチャント：STR
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addStr((byte) 5);
                        pc.sendPackets(new S_Strup(pc, 5,
                                this._getBuffIconDuration));

                    } else if (this._skillId == PHYSICAL_ENCHANT_DEX) { // フィジカルエンチャント：DEX
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addDex((byte) 5);
                        pc.sendPackets(new S_Dexup(pc, 5,
                                this._getBuffIconDuration));

                    } else if (this._skillId == FIRE_WEAPON) { // ファイアー ウェポン
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addDmgup(4);
                        pc.sendPackets(new S_PacketBoxIconAura(147,
                                this._getBuffIconDuration));

//                    } else if (this._skillId == FIRE_BLESS) { // ファイアー ブレス
//                        final L1PcInstance pc = (L1PcInstance) cha;
//                        pc.addDmgup(4);
//                        pc.sendPackets(new S_PacketBoxIconAura(154,
//                                this._getBuffIconDuration));

                    } else if (this._skillId == BURNING_WEAPON) { // バーニング ウェポン
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addDmgup(6);
                        pc.addHitup(6);
                        pc.sendPackets(new S_PacketBoxIconAura(162,
                                this._getBuffIconDuration));

                    } else if (this._skillId == WIND_SHOT) { // ウィンド ショット
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addBowHitup(6);
                        pc.sendPackets(new S_PacketBoxIconAura(148,
                                this._getBuffIconDuration));

                    } else if (this._skillId == STORM_EYE) { // ストーム アイ
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addBowHitup(2);
                        pc.addBowDmgup(3);
                        pc.sendPackets(new S_PacketBoxIconAura(155,
                                this._getBuffIconDuration));

                    } else if (this._skillId == STORM_SHOT) { // ストーム ショット
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addBowDmgup(5);
                        pc.addBowHitup(-1);
                        pc.sendPackets(new S_PacketBoxIconAura(165,
                                this._getBuffIconDuration));

                    } else if (this._skillId == BERSERKERS) { // バーサーカー
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addAc(10);
                        pc.addDmgup(5);
                        pc.addHitup(2);

                    } else if (this._skillId == SHAPE_CHANGE) { // シェイプ チェンジ
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.sendPackets(new S_ShowPolyList(pc.getId()));
                        if (!pc.isShapeChange()) {
                            pc.setSummonMonster(false);
                            pc.setShapeChange(true);
                        }

                    } else if (this._skillId == GREATER_HASTE) { // グレーター ヘイスト
                        final L1PcInstance pc = (L1PcInstance) cha;
                        if (pc.getHasteItemEquipped() > 0) {
                            continue;
                        }
                        if (pc.getMoveSpeed() != 2) { // スロー中以外
                            pc.setDrink(false);
                            pc.setMoveSpeed(1);
                            pc.sendPackets(new S_SkillHaste(pc.getId(), 1,
                                    this._getBuffIconDuration));
                            pc.broadcastPacketAll(new S_SkillHaste(pc.getId(),
                                    1, 0));

                        } else { // スロー中
                            int skillNum = 0;
                            if (pc.hasSkillEffect(SLOW)) {
                                skillNum = SLOW;
                            } else if (pc.hasSkillEffect(MASS_SLOW)) {
                                skillNum = MASS_SLOW;
                            } else if (pc.hasSkillEffect(ENTANGLE)) {
                                skillNum = ENTANGLE;
                            }
                            if (skillNum != 0) {
                                pc.removeSkillEffect(skillNum);
                                pc.removeSkillEffect(GREATER_HASTE);
                                pc.setMoveSpeed(0);
                                continue;
                            }
                        }

                    } else if ((this._skillId == HOLY_WALK)
                            || (this._skillId == MOVING_ACCELERATION)
                            || (this._skillId == WIND_WALK)) { // ホーリーウォーク、ムービングアクセレーション、ウィンドウォーク
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.setBraveSpeed(4);
                        pc.sendPackets(new S_SkillBrave(pc.getId(), 4,
                                this._getBuffIconDuration));
                        pc.broadcastPacketAll(new S_SkillBrave(pc.getId(), 4, 0));

                        /*
                         * } else if (this._skillId == BLOODLUST) { // ブラッドラスト
                         * final L1PcInstance pc = (L1PcInstance) cha;
                         * 
                         * // 强化勇气的药水效果 if (pc.hasSkillEffect(STATUS_BRAVE2)) {
                         * // 1,413：目前情况是无法使用。 pc.sendPackets(new
                         * S_ServerMessage(1413)); return; }
                         * 
                         * // 具有生命之树果实效果 if
                         * (pc.hasSkillEffect(L1SkillId.STATUS_RIBRAVE)) { //
                         * 1,413：目前情况是无法使用。 pc.sendPackets(new
                         * S_ServerMessage(1413)); return; }
                         * 
                         * // 勇敢效果 抵销对应技能 L1BuffUtil.braveStart(pc);
                         * 
                         * pc.setBraveSpeed(6); pc.sendPackets(new
                         * S_SkillBrave(pc.getId(), 6,
                         * this._getBuffIconDuration));
                         * pc.broadcastPacketAll(new S_SkillBrave(pc.getId(), 6,
                         * 0));
                         */

                    } else if (this._skillId == ILLUSION_OGRE) { // イリュージョン：オーガ
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addDmgup(4);
                        pc.addHitup(4);

                    } else if (this._skillId == ILLUSION_LICH) { // イリュージョン：リッチ
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addSp(2);
                        pc.sendPackets(new S_SPMR(pc));

                    } else if (this._skillId == ILLUSION_DIA_GOLEM) { // イリュージョン：ダイアモンドゴーレム
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addAc(-8);

                    } else if (this._skillId == INSIGHT) { // インサイト
                        final L1PcInstance pc = (L1PcInstance) cha;
                        pc.addStr((byte) 1);
                        pc.addCon((byte) 1);
                        pc.addDex((byte) 1);
                        pc.addWis((byte) 1);
                        pc.addInt((byte) 1);

                        /*
                         * } else if (this._skillId == PANIC) {// 恐惧217 final
                         * L1PcInstance pc = (L1PcInstance) cha;
                         * pc.addStr((byte) -1); pc.addCon((byte) -1);
                         * pc.addDex((byte) -1); pc.addWis((byte) -1);
                         * pc.addInt((byte) -1);
                         */

//                    } else if (_skillId == THUNDER_GRAB) {// 夺命之雷192
//                        final Random rad = new Random();
//                        final int i = rad.nextInt(100) + 1;
//                        if (i <= 60) {
//                            final L1PcInstance pc = (L1PcInstance) cha;
//                            pc.setSkillEffect(STATUS_FREEZE, 4000);
//                            pc.sendPackets(new S_Paralysis(
//                                    S_Paralysis.TYPE_BIND, true));
//                        } //修正夺命之雷不能定人 改写到上面 hjx1000
                    }
                }

                // ●●●● NPCにしか效果のないスキル ●●●● npc使用的技能
                if ((_calcType == PC_NPC) || (_calcType == NPC_NPC)) {
                    // ★★★ ペット系スキル ★★★ 宠物使用的技能
                    if ((_skillId == TAMING_MONSTER)
                            && ((L1MonsterInstance) cha).getNpcTemplate()
                                    .isTamable()) { // テイミングモンスター
                        int petcost = 0;
                        final Object[] petlist = _user.getPetList().values()
                                .toArray();
                        for (final Object pet : petlist) {
                            // 现在のペットコスト
                            petcost += ((L1NpcInstance) pet).getPetcost();
                        }
                        int charisma = _user.getCha();
                        if (_player.isElf()) { // エルフ
                            charisma += 12;

                        } else if (_player.isWizard()) { // ウィザード
                            charisma += 6;
                        }
                        charisma -= petcost;
                        if (charisma >= 6) { // ペットコストの确认
                            final L1SummonInstance summon = new L1SummonInstance(
                                    this._targetNpc, this._user, false);
                            this._target = summon; // ターゲット入替え

                        } else {
                            this._player.sendPackets(new S_ServerMessage(319)); // \f1これ以上のモンスターを操ることはできません。
                        }

                    } else if (this._skillId == CREATE_ZOMBIE) { // クリエイトゾンビ
                        int petcost = 0;
                        final Object[] petlist = this._user.getPetList()
                                .values().toArray();
                        for (final Object pet : petlist) {
                            // 现在のペットコスト
                            petcost += ((L1NpcInstance) pet).getPetcost();
                        }
                        int charisma = this._user.getCha();
                        if (this._player.isElf()) { // エルフ
                            charisma += 12;
                        } else if (this._player.isWizard()) { // ウィザード
                            charisma += 6;
                        }
                        charisma -= petcost;
                        if (charisma >= 6) { // ペットコストの确认
                            final L1SummonInstance summon = new L1SummonInstance(
                                    this._targetNpc, this._user, true);
                            this._target = summon; // ターゲット入替え
                        } else {
                            this._player.sendPackets(new S_ServerMessage(319)); // \f1これ以上のモンスターを操ることはできません。
                        }

                    } else if (this._skillId == WEAK_ELEMENTAL) { // ウィーク エレメンタル
                        if (cha instanceof L1MonsterInstance) {
                            final L1Npc npcTemp = ((L1MonsterInstance) cha)
                                    .getNpcTemplate();
                            final int weakAttr = npcTemp.get_weakAttr();
                            if ((weakAttr & 1) == 1) { // 地
                                cha.broadcastPacketX8(new S_SkillSound(cha
                                        .getId(), 2169));
                            }
                            if ((weakAttr & 2) == 2) { // 火
                                cha.broadcastPacketX8(new S_SkillSound(cha
                                        .getId(), 2167));
                            }
                            if ((weakAttr & 4) == 4) { // 水
                                cha.broadcastPacketX8(new S_SkillSound(cha
                                        .getId(), 2166));
                            }
                            if ((weakAttr & 8) == 8) { // 风
                                cha.broadcastPacketX8(new S_SkillSound(cha
                                        .getId(), 2168));
                            }
                        }

                    } else if (this._skillId == RETURN_TO_NATURE) { // リターントゥネイチャー
                        if (cha instanceof L1SummonInstance) {
                            final L1SummonInstance summon = (L1SummonInstance) cha;
                            summon.broadcastPacketX10(new S_SkillSound(summon
                                    .getId(), 2245));
                            summon.returnToNature();

                        } else {
                            if (this._user instanceof L1PcInstance) {
                                this._player
                                        .sendPackets(new S_ServerMessage(79));
                            }
                        }
                    }
                }

                // ■■■■ 个别处理ここまで ■■■■

                if ((this._skill.getType() == L1Skills.TYPE_HEAL)
                        && (this._calcType == PC_NPC) && (undeadType == 1)) {
                    this._dmg *= -1; // もし、アンデットで回复系スキルならばダメージになる。
                }

                if ((this._skill.getType() == L1Skills.TYPE_HEAL)
                        && (this._calcType == PC_NPC) && (undeadType == 3)) {
                    this._dmg = 0; // もし、アンデット系ボスで回复系スキルならば无效
                }

                if (((cha instanceof L1TowerInstance) || (cha instanceof L1DoorInstance))
                        && (this._dmg < 0)) { // ガーディアンタワー、ドアにヒールを使用
                    this._dmg = 0;
                }

                // System.out.println("dmg1:"+dmg); XXX
                if ((this._dmg != 0) || (drainMana != 0)) {
                    // System.out.println("结果质2:(HP) " + this._dmg);
                    magic.commit(this._dmg, drainMana); // ダメージ系、回复系の值をターゲットにコミットする。
                }

                // System.out.println("dmg2:"+dmg);
                // ヒール系の他に、别途回复した场合（V-Tなど）
                if (heal > 0) {
                    // System.out.println("dmg3:"+dmg);
                    if ((heal + this._user.getCurrentHp()) > this._user
                            .getMaxHp()) {
                        this._user.setCurrentHp(this._user.getMaxHp());

                    } else {
                        this._user.setCurrentHp(heal
                                + this._user.getCurrentHp());
                    }
                }

                if (cha instanceof L1PcInstance) { // ターゲットがPCならば、ACとステータスを送信
                    final L1PcInstance pc = (L1PcInstance) cha;
                    pc.turnOnOffLight();
                    pc.sendPackets(new S_OwnCharAttrDef(pc));
                    pc.sendPackets(new S_OwnCharStatus(pc));
                    sendHappenMessage(pc); // ターゲットにメッセージを送信
                }

                addMagicList(cha, false); // ターゲットに魔法の效果时间を设定

                if (cha instanceof L1PcInstance) { // ターゲットがPCならば、ライト状态を更新
                    final L1PcInstance pc = (L1PcInstance) cha;
                    pc.turnOnOffLight();
                }
            }

            if ((_skillId == DETECTION) || (_skillId == COUNTER_DETECTION)) { // 无所遁形术、カウンター无所遁形术
                detection(_player);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * キャンセレーションで解除できないスキルかを返す。 被技能攻击而暂时不能解除
     */
    /*
     * private boolean isNotCancelable(final int skillNum) { return (skillNum ==
     * ENCHANT_WEAPON) || (skillNum == BLESSED_ARMOR) || (skillNum ==
     * ABSOLUTE_BARRIER) || (skillNum == ADVANCE_SPIRIT) || (skillNum ==
     * SHOCK_STUN) || (skillNum == SHADOW_FANG) || (skillNum == REDUCTION_ARMOR)
     * || (skillNum == SOLID_CARRIAGE) || (skillNum == COUNTER_BARRIER) ||
     * (skillNum == AWAKEN_ANTHARAS) || (skillNum == AWAKEN_FAFURION) ||
     * (skillNum == AWAKEN_VALAKAS); }
     */

    private void detection(final L1PcInstance pc) {
        if (!pc.isGmInvis() && pc.isInvisble()) { // 自分
            pc.delInvis();
            pc.beginInvisTimer();
        }

        for (final L1PcInstance tgt : World.get().getVisiblePlayer(pc)) {
            if (!tgt.isGmInvis() && tgt.isInvisble()) {
                tgt.delInvis();
            }
        }

        // 侦测陷阱的处理
        WorldTrap.get().onDetection(pc);
    }

    /**
     * 目标判定
     * 
     * @param cha
     * @param cha
     * @return
     */
    private boolean isTargetCalc(final L1Character cha) {
        // 攻击魔法のNon?PvP判定
        if (this._skill.getTarget().equals("attack")
                && (this._skillId != TURN_UNDEAD)) { // 攻击魔法
            if (this.isPcSummonPet(cha)) { // 对象がPC、サモン、ペット
                if (this._player.isSafetyZone() || cha.isSafetyZone() || // 攻击する侧または攻击される侧がセーフティーゾーン
                        this._player.checkNonPvP(this._player, cha)) { // Non-PvP设定
                    return false;
                }
            }
        }
        switch (this._skillId) {
        // 沉睡之雾
            case FOG_OF_SLEEPING:
                if (this._user.getId() == cha.getId()) {
                    return false;
                }
                break;

            // 集体缓速术
            case MASS_SLOW:
                if (this._user.getId() == cha.getId()) {
                    return false;
                }

                if (cha instanceof L1SummonInstance) {
                    final L1SummonInstance summon = (L1SummonInstance) cha;
                    if (this._user.getId() == summon.getMaster().getId()) {
                        return false;
                    }

                } else if (cha instanceof L1PetInstance) {
                    final L1PetInstance pet = (L1PetInstance) cha;
                    if (this._user.getId() == pet.getMaster().getId()) {
                        return false;
                    }
                }
                break;

            // 集体传送术
            case MASS_TELEPORT:
                if (this._user.getId() != cha.getId()) {
                    return false;
                }
                break;
        }
        return true;
    }

    /**
     * 目标对象 是否为宠物 召唤兽 虚拟人物
     * 
     * @param cha
     * @return
     */
    private boolean isPcSummonPet(final L1Character cha) {
        // PC 对 PC
        switch (this._calcType) {
            case PC_PC:
                return true;

                // PC 对 NPC
            case PC_NPC:
                // 目标对象为召唤兽
                if (cha instanceof L1SummonInstance) {
                    final L1SummonInstance summon = (L1SummonInstance) cha;
                    // 目标对象具有主人
                    if (summon.isExsistMaster()) {
                        return true;
                    }
                }
                // 目标对象为宠物
                if (cha instanceof L1PetInstance) {
                    return true;
                }
                return false;

            default:
                return false;
        }
    }

    // ターゲットに对して必ず失败になるか返す
    private boolean isTargetFailure(final L1Character cha) {
        boolean isTU = false;
        boolean isErase = false;
        boolean isManaDrain = false;
        int undeadType = 0;
        if ((cha instanceof L1TowerInstance) || (cha instanceof L1DoorInstance)) { // ガーディアンタワー、ドアには确率系スキル无效
            return true;
        }

        if (cha instanceof L1PcInstance) { // 对PCの场合
            if ((this._calcType == PC_PC)
                    && this._player.checkNonPvP(this._player, cha)) { // Non-PvP设定
                final L1PcInstance pc = (L1PcInstance) cha;
                if ((this._player.getId() == pc.getId())
                        || ((pc.getClanid() != 0) && (this._player.getClanid() == pc
                                .getClanid()))) {
                    return false;
                }
                return true;
            }
            return false;
        }

        if (cha instanceof L1MonsterInstance) { // ターンアンデット可能か判定
            isTU = ((L1MonsterInstance) cha).getNpcTemplate().get_IsTU();
        }

        if (cha instanceof L1MonsterInstance) { // イレースマジック可能か判定
            isErase = ((L1MonsterInstance) cha).getNpcTemplate().get_IsErase();
        }

        if (cha instanceof L1MonsterInstance) { // アンデットの判定
            undeadType = ((L1MonsterInstance) cha).getNpcTemplate()
                    .get_undead();
        }

        // マナドレインが可能か？
        if (cha instanceof L1MonsterInstance) {
            isManaDrain = true;
        }
        /*
         * 成功除外条件１：T-Uが成功したが、对象がアンデットではない。 成功除外条件２：T-Uが成功したが、对象にはターンアンデット无效。
         * 成功除外条件３：スロー、マススロー、マナドレイン、エンタングル、イレースマジック、ウィンドシャックル无效
         * 成功除外条件４：マナドレインが成功したが、モンスター以外の场合
         */
        if (((this._skillId == TURN_UNDEAD) && ((undeadType == 0) || (undeadType == 2)))
                || ((this._skillId == TURN_UNDEAD) && (isTU == false))
                || (((this._skillId == ERASE_MAGIC) || (this._skillId == SLOW)
                        || (this._skillId == MANA_DRAIN)
                        || (this._skillId == MASS_SLOW)
                        || (this._skillId == ENTANGLE) || (this._skillId == WIND_SHACKLE)) && (isErase == false))
                || ((this._skillId == MANA_DRAIN) && (isManaDrain == false))) {
            return true;
        }
        return false;
    }

    // カウンターマジックが発动したか返す
    private boolean isUseCounterMagic(final L1Character cha) {
        // カウンターマジック有效なスキルでカウンターマジック中
        if (this._isCounterMagic && cha.hasSkillEffect(COUNTER_MAGIC)) {
            cha.removeSkillEffect(COUNTER_MAGIC);
            final int castgfx = SkillsTable.get().getTemplate(COUNTER_MAGIC)
                    .getCastGfx();
            cha.broadcastPacketX10(new S_SkillSound(cha.getId(), castgfx));
            if (cha instanceof L1PcInstance) {
                final L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillSound(pc.getId(), castgfx));
            }
            return true;
        }
        return false;
    }

}
