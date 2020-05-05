package com.lineage.server.model.Instance;

import static com.lineage.server.model.skill.L1SkillId.ADDITIONAL_FIRE;
import static com.lineage.server.model.skill.L1SkillId.ADLV80_1;
import static com.lineage.server.model.skill.L1SkillId.ADLV80_2;
import static com.lineage.server.model.skill.L1SkillId.BLIND_HIDING;
import static com.lineage.server.model.skill.L1SkillId.CANCELLATION;
import static com.lineage.server.model.skill.L1SkillId.COUNTER_BARRIER;
import static com.lineage.server.model.skill.L1SkillId.DECREASE_WEIGHT;
import static com.lineage.server.model.skill.L1SkillId.DRESS_EVASION;
import static com.lineage.server.model.skill.L1SkillId.ENTANGLE;
import static com.lineage.server.model.skill.L1SkillId.EXOTIC_VITALIZE;
import static com.lineage.server.model.skill.L1SkillId.FOG_OF_SLEEPING;
import static com.lineage.server.model.skill.L1SkillId.GMSTATUS_HPBAR;
import static com.lineage.server.model.skill.L1SkillId.GREATER_HASTE;
import static com.lineage.server.model.skill.L1SkillId.HASTE;
import static com.lineage.server.model.skill.L1SkillId.HOLY_WALK;
import static com.lineage.server.model.skill.L1SkillId.ILLUSION_AVATAR;
import static com.lineage.server.model.skill.L1SkillId.INVISIBILITY;
import static com.lineage.server.model.skill.L1SkillId.JOY_OF_PAIN;
import static com.lineage.server.model.skill.L1SkillId.MASS_SLOW;
import static com.lineage.server.model.skill.L1SkillId.MORTAL_BODY;
import static com.lineage.server.model.skill.L1SkillId.MOVING_ACCELERATION;
import static com.lineage.server.model.skill.L1SkillId.SHAPE_CHANGE;
import static com.lineage.server.model.skill.L1SkillId.SLOW;
import static com.lineage.server.model.skill.L1SkillId.SOLID_CARRIAGE;
import static com.lineage.server.model.skill.L1SkillId.STATUS_BRAVE;
import static com.lineage.server.model.skill.L1SkillId.STATUS_BRAVE3;
import static com.lineage.server.model.skill.L1SkillId.STATUS_CHAT_PROHIBITED;
import static com.lineage.server.model.skill.L1SkillId.STATUS_ELFBRAVE;
import static com.lineage.server.model.skill.L1SkillId.STATUS_HASTE;
import static com.lineage.server.model.skill.L1SkillId.STATUS_RIBRAVE;
import static com.lineage.server.model.skill.L1SkillId.STRIKER_GALE;
import static com.lineage.server.model.skill.L1SkillId.WIND_WALK;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

import com.lineage.server.model.L1Inventory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigKill;
import com.lineage.config.ConfigOther;
import com.lineage.config.ConfigRate;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.event.OnlineGiftSet;
import com.lineage.data.event.QuestSet;
import com.lineage.data.event.castle_warOnlineGiftSet;
import com.lineage.data.quest.Chapter01R;
import com.lineage.echo.ClientExecutor;
import com.lineage.echo.EncryptExecutor;
import com.lineage.server.ActionCodes;
import com.lineage.server.clientpackets.AcceleratorChecker;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.ExpTable;
import com.lineage.server.datatables.MapLevelTable;
import com.lineage.server.datatables.QuestTable;
import com.lineage.server.datatables.lock.BuddyReading;
import com.lineage.server.datatables.lock.CharBuffReading;
import com.lineage.server.datatables.lock.CharOtherReading;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.L1ActionPc;
import com.lineage.server.model.L1ActionPet;
import com.lineage.server.model.L1ActionSummon;
import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1ChatParty;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1DwarfForElfInventory;
import com.lineage.server.model.L1DwarfInventory;
import com.lineage.server.model.L1EquipmentSlot;
import com.lineage.server.model.L1ExcludingList;
import com.lineage.server.model.L1Karma;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Party;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.L1PcQuest;
import com.lineage.server.model.L1PinkName;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.L1TownLocation;
import com.lineage.server.model.L1War;
import com.lineage.server.model.RocksPrison;
import com.lineage.server.model.classes.L1ClassFeature;
import com.lineage.server.model.monitor.L1PcInvisDelay;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.model.skill.skillmode.SkillMode;
import com.lineage.server.model.skill.L1SkillMode;
import com.lineage.server.serverpackets.S_BlueMessage;
import com.lineage.server.serverpackets.S_Bonusstats;
import com.lineage.server.serverpackets.S_BoxMessage;
import com.lineage.server.serverpackets.S_ChangeShape;
import com.lineage.server.serverpackets.S_DelSkill;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_DoActionShop;
import com.lineage.server.serverpackets.S_Exp;
import com.lineage.server.serverpackets.S_Fishing;
import com.lineage.server.serverpackets.S_HPMeter;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_Invis;
import com.lineage.server.serverpackets.S_Karma;
import com.lineage.server.serverpackets.S_KillMessage;
import com.lineage.server.serverpackets.S_Lawful;
import com.lineage.server.serverpackets.S_Liquor;
import com.lineage.server.serverpackets.S_MPUpdate;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_OtherCharPacks;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_PacketBoxDk;
import com.lineage.server.serverpackets.S_PacketBoxParty;
import com.lineage.server.serverpackets.S_PacketBoxProtection;
import com.lineage.server.serverpackets.S_Poison;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.templates.L1BuddyTmp;
import com.lineage.server.templates.L1PcOther;
import com.lineage.server.templates.L1PcOtherList;
import com.lineage.server.templates.L1PrivateShopBuyList;
import com.lineage.server.templates.L1PrivateShopSellList;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.timecontroller.pc.AutoMagic;
import com.lineage.server.timecontroller.pc.HardDelay;
import com.lineage.server.timecontroller.pc.skillHardDelay;
import com.lineage.server.timecontroller.server.ServerUseMapTimer;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.utils.CalcInitHpMp;
import com.lineage.server.utils.CalcStat;
import com.lineage.server.utils.ListMapUtil;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import com.lineage.server.world.WorldQuest;
import com.lineage.server.world.WorldWar;

/**
 * 对象:PC 控制项
 * 
 * @author dexc
 * 
 */
public class L1PcInstance extends L1Character {

    private static final Log _log = LogFactory.getLog(L1PcInstance.class);

    private static final long serialVersionUID = 1L;

    /** 骑士(男) */
    public static final int CLASSID_KNIGHT_MALE = 61;
    /** 骑士(女) */
    public static final int CLASSID_KNIGHT_FEMALE = 48;

    /** 精灵(男) */
    public static final int CLASSID_ELF_MALE = 138;
    /** 精灵(女) */
    public static final int CLASSID_ELF_FEMALE = 37;

    /** 法师(男) */
    public static final int CLASSID_WIZARD_MALE = 734;
    /** 法师(女) */
    public static final int CLASSID_WIZARD_FEMALE = 1186;

    /** 黑妖(男) */
    public static final int CLASSID_DARK_ELF_MALE = 2786;
    /** 黑妖(女) */
    public static final int CLASSID_DARK_ELF_FEMALE = 2796;

    /** 王族(男) */
    public static final int CLASSID_PRINCE = 0;
    /** 王族(女) */
    public static final int CLASSID_PRINCESS = 1;

    /** 龙骑(男) */
    public static final int CLASSID_DRAGON_KNIGHT_MALE = 6658;
    /** 龙骑(女) */
    public static final int CLASSID_DRAGON_KNIGHT_FEMALE = 6661;

    /** 幻术(男) */
    public static final int CLASSID_ILLUSIONIST_MALE = 6671;
    /** 幻术(女) */
    public static final int CLASSID_ILLUSIONIST_FEMALE = 6650;

    private static Random _random = new Random();

    private boolean _isKill = false;

    public boolean is_isKill() {
        return _isKill;
    }

    public void set_isKill(boolean _isKill) {
        this._isKill = _isKill;
    }

    private short _hpr = 0;

    private short _trueHpr = 0;

    public short getHpr() {
        return this._hpr;
    }

    /**
     * 增加(减少)HP回复量
     * 
     * @param i
     */
    public void addHpr(final int i) {
        this._trueHpr += i;
        this._hpr = (short) Math.max(0, this._trueHpr);
    }

    private short _mpr = 0;
    private short _trueMpr = 0;

    public short getMpr() {
        return this._mpr;
    }

    /**
     * 增加(减少)MP回复量
     * 
     * @param i
     */
    public void addMpr(final int i) {
        this._trueMpr += i;
        this._mpr = (short) Math.max(0, this._trueMpr);
    }

    public short _originalHpr = 0; // ● オリジナルCON HPR

    public short getOriginalHpr() {

        return this._originalHpr;
    }

    public short _originalMpr = 0; // ● オリジナルWIS MPR

    public short getOriginalMpr() {

        return this._originalMpr;
    }

    private boolean _mpRegenActive;
    private boolean _mpReductionActiveByAwake;
    private boolean _hpRegenActive;

    private int _hpRegenType = 0;
    private int _hpRegenState = 4;

    public int getHpRegenState() {
        return this._hpRegenState;
    }

    public void set_hpRegenType(final int hpRegenType) {
        this._hpRegenType = hpRegenType;
    }

    public int hpRegenType() {
        return this._hpRegenType;
    }

    private int regenMax() {
        final int lvlTable[] = new int[] { 30, 25, 20, 16, 14, 12, 11, 10, 9,
                3, 2 };

        int regenLvl = Math.min(10, getLevel());
        if ((30 <= getLevel()) && isKnight()) {
            regenLvl = 11;
        }
        return lvlTable[regenLvl - 1] << 2;
    }

    /**
     * HP回复成立
     * 
     * @return
     */
    public boolean isRegenHp() {
        if (!_hpRegenActive) {
            return false;
        }
        if (hasSkillEffect(EXOTIC_VITALIZE) || hasSkillEffect(ADDITIONAL_FIRE)) {
            return _hpRegenType >= regenMax();
        }
        if (120 <= _inventory.getWeight240()) {
            return false;
        }
        if ((_food < 3)) {
            return false;
        }
        return _hpRegenType >= regenMax();
    }

    private int _mpRegenType = 0;
    private int _mpRegenState = 4;

    public int getMpRegenState() {
        return this._mpRegenState;
    }

    public void set_mpRegenType(final int hpmpRegenType) {
        this._mpRegenType = hpmpRegenType;
    }

    public int mpRegenType() {
        return this._mpRegenType;
    }

    /**
     * MP回复成立
     * 
     * @return
     */
    public boolean isRegenMp() {
        if (!this._mpRegenActive) {
            return false;
        }
        if (this.hasSkillEffect(EXOTIC_VITALIZE)
                || this.hasSkillEffect(ADDITIONAL_FIRE)) {
            return this._mpRegenType >= 64;
        }
        if (120 <= this._inventory.getWeight240()) {
            return false;
        }
        if ((this._food < 3)) {
            return false;
        }
//        // 法师加速
//        if (this.isWizard()) {
//            return this._mpRegenType >= 40;
//        }
        return this._mpRegenType >= 64;
    }

    // HP自然回复 MP自然回复

    /** 无动作 */
    public static final int REGENSTATE_NONE = 4;

    /** 移动中 */
    public static final int REGENSTATE_MOVE = 2;

    /** 攻击中 */
    public static final int REGENSTATE_ATTACK = 1;

    public void setRegenState(final int state) {
        this._mpRegenState = state;
        this._hpRegenState = state;
    }

    /**
     * HP自然回复启用
     */
    public void startHpRegeneration() {
        if (!this._hpRegenActive) {
            this._hpRegenActive = true;
        }
    }

    /**
     * HP自然回复停止
     */
    public void stopHpRegeneration() {
        if (this._hpRegenActive) {
            this._hpRegenActive = false;
        }
    }

    /**
     * HP自然回复状态
     * 
     * @return
     */
    public boolean getHpRegeneration() {
        return _hpRegenActive;
    }

    /**
     * MP自然回复启用
     */
    public void startMpRegeneration() {
        if (!this._mpRegenActive) {
            this._mpRegenActive = true;
        }
    }

    /**
     * MP自然回复停止
     */
    public void stopMpRegeneration() {
        if (this._mpRegenActive) {
            this._mpRegenActive = false;
        }
    }

    /**
     * MP自然回复状态
     * 
     * @return
     */
    public boolean getMpRegeneration() {
        return _mpRegenActive;
    }

    // 龙骑士 觉醒技能 MP自然减少时间
    public static final int INTERVAL_BY_AWAKE = 4;// 秒

    // 龙骑士觉醒技能 MP自然减少计算时间
    private int _awakeMprTime = 0;// 秒

    /**
     * 龙骑士觉醒技能 MP自然减少处理时间
     * 
     * @return
     */
    public int get_awakeMprTime() {
        return _awakeMprTime;
    }

    /**
     * 设置龙骑士觉醒技能 MP自然减少处理时间
     * 
     * @param awakeMprTime
     */
    public void set_awakeMprTime(final int awakeMprTime) {
        this._awakeMprTime = awakeMprTime;
    }

    /**
     * 龙骑士 觉醒技能 MP自然减少启用
     */
    public void startMpReductionByAwake() {
        if (!this._mpReductionActiveByAwake) {
            this.set_awakeMprTime(INTERVAL_BY_AWAKE);
            this._mpReductionActiveByAwake = true;
        }
    }

    /**
     * 龙骑士 觉醒技能 MP自然减少停止
     */
    public void stopMpReductionByAwake() {
        if (this._mpReductionActiveByAwake) {
            this.set_awakeMprTime(0);
            this._mpReductionActiveByAwake = false;
        }
    }

    /**
     * 是否在龙骑士 觉醒技能 MP自然减少中
     * 
     * @return
     */
    public boolean isMpReductionActiveByAwake() {
        return _mpReductionActiveByAwake;
    }

    private int _awakeSkillId = 0;// 龙骑士 觉醒技能编号

    /**
     * 龙骑士 觉醒技能编号
     * 
     * @return
     */
    public int getAwakeSkillId() {
        return this._awakeSkillId;
    }

    /**
     * 龙骑士 觉醒技能编号
     * 
     * @param i
     */
    public void setAwakeSkillId(final int i) {
        this._awakeSkillId = i;
    }

    /**
     * 加入PC 可见物更新处理清单
     */
    public void startObjectAutoUpdate() {
        this.removeAllKnownObjects();
    }

    /**
     * 移出各种处理清单
     */
    public void stopEtcMonitor() {
        // 移出PC 鬼魂模式处理清单
        this.set_ghostTime(-1);
        this.setGhost(false);
        this.setGhostCanTalk(true);
        this.setReserveGhost(false);

        // 移出龙骑士觉醒技能MP自然减少处理
        stopMpReductionByAwake();

        if (ServerUseMapTimer.MAP.get(this) != null) {
            // 移出计时地图处理清单
            ServerUseMapTimer.MAP.remove(this);
        }

        // 移出在线奖励清单
        OnlineGiftSet.remove(this);
        if (this.isCrown()) {
            castle_warOnlineGiftSet.remove(this); //城主守城奖励 hjx1000
        }
        if (this.isActived()) {//玩家离线停止挂机线程
        	this.setActived(false);
        }

        // 清空清单资料
        ListMapUtil.clear(_skillList);
        ListMapUtil.clear(_sellList);
        ListMapUtil.clear(_buyList);
//        ListMapUtil.clear(_trade_items);
    }

    private int _old_lawful;

    /**
     * 原始Lawful
     * 
     * @return
     */
    public int getLawfulo() {
        return _old_lawful;
    }

    /**
     * 更新Lawful
     */
    public void onChangeLawful() {
        if (_old_lawful != getLawful()) {
            _old_lawful = getLawful();
            sendPacketsAll(new S_Lawful(this));
            // 战斗特化效果
//            lawfulUpdate();
        }
    }

    private boolean _jl1 = false;// 正义的守护 Lv.1
    private boolean _jl2 = false;// 正义的守护 Lv.2
    private boolean _jl3 = false;// 正义的守护 Lv.3
    private boolean _el1 = false;// 邪恶的守护 Lv.1
    private boolean _el2 = false;// 邪恶的守护 Lv.2
    private boolean _el3 = false;// 邪恶的守护 Lv.3

    /**
     * TODO 战斗特化<BR>
     */
    private void lawfulUpdate() {
        int l = getLawful();

        if (l >= 10000 && l <= 19999) {
            if (!_jl1) {
                overUpdate();
                _jl1 = true;
                sendPackets(new S_PacketBoxProtection(
                        S_PacketBoxProtection.JUSTICE_L1, 1));
                sendPackets(new S_OwnCharAttrDef(this));
                sendPackets(new S_SPMR(this));
            }

        } else if (l >= 20000 && l <= 29999) {
            if (!_jl2) {
                overUpdate();
                _jl2 = true;
                sendPackets(new S_PacketBoxProtection(
                        S_PacketBoxProtection.JUSTICE_L2, 1));
                sendPackets(new S_OwnCharAttrDef(this));
                sendPackets(new S_SPMR(this));
            }

        } else if (l >= 30000 && l <= 39999) {
            if (!_jl3) {
                overUpdate();
                _jl3 = true;
                sendPackets(new S_PacketBoxProtection(
                        S_PacketBoxProtection.JUSTICE_L3, 1));
                sendPackets(new S_OwnCharAttrDef(this));
                sendPackets(new S_SPMR(this));
            }

        } else if (l >= -19999 && l <= -10000) {
            if (!_el1) {
                overUpdate();
                _el1 = true;
                sendPackets(new S_PacketBoxProtection(
                        S_PacketBoxProtection.EVIL_L1, 1));
                sendPackets(new S_SPMR(this));
            }

        } else if (l >= -29999 && l <= -20000) {
            if (!_el2) {
                overUpdate();
                _el2 = true;
                sendPackets(new S_PacketBoxProtection(
                        S_PacketBoxProtection.EVIL_L2, 1));
                sendPackets(new S_SPMR(this));
            }

        } else if (l >= -39999 && l <= -30000) {
            if (!_el3) {
                overUpdate();
                _el3 = true;
                sendPackets(new S_PacketBoxProtection(
                        S_PacketBoxProtection.EVIL_L3, 1));
                sendPackets(new S_SPMR(this));
            }

        } else {
            if (overUpdate()) {
                sendPackets(new S_OwnCharAttrDef(this));
                sendPackets(new S_SPMR(this));
            }
        }
    }

    /**
     * TODO 战斗特化<BR>
     * 
     * @return
     */
    private boolean overUpdate() {
        if (_jl1) {
            _jl1 = false;
            sendPackets(new S_PacketBoxProtection(
                    S_PacketBoxProtection.JUSTICE_L1, 0));
            return true;

        } else if (_jl2) {
            _jl2 = false;
            sendPackets(new S_PacketBoxProtection(
                    S_PacketBoxProtection.JUSTICE_L2, 0));
            return true;

        } else if (_jl3) {
            _jl3 = false;
            sendPackets(new S_PacketBoxProtection(
                    S_PacketBoxProtection.JUSTICE_L3, 0));
            return true;

        } else if (_el1) {
            _el1 = false;
            sendPackets(new S_PacketBoxProtection(
                    S_PacketBoxProtection.EVIL_L1, 0));
            return true;

        } else if (_el2) {
            _el2 = false;
            sendPackets(new S_PacketBoxProtection(
                    S_PacketBoxProtection.EVIL_L2, 0));
            return true;

        } else if (_el3) {
            _el3 = false;
            sendPackets(new S_PacketBoxProtection(
                    S_PacketBoxProtection.EVIL_L3, 0));
            return true;
        }
        return false;
    }

    /**
     * TODO 战斗特化<BR>
     * <FONT COLOR="#0000ff">遭遇的守护 </FONT>20级以下角色
     * 被超过10级以上的玩家攻击而死亡时，不会失去经验值，也不会掉落物品<BR>
     * 
     * @return
     */
    private boolean isEncounter() {
        if (getLevel() <= ConfigOther.ENCOUNTER_LV) {
            return true;
        }
        return false;
    }

    /**
     * TODO 战斗特化<BR>
     * <FONT COLOR="#0000ff">正义的守护 Lv.1 </FONT>善恶值 10,000 ~ 19,999 (防御：-2 /
     * 魔防+3)<BR>
     * <FONT COLOR="#0000ff">正义的守护 Lv.2 </FONT>善恶值 20,000 ~ 29,999 (防御：-4 /
     * 魔防+6)<BR>
     * <FONT COLOR="#0000ff">正义的守护 Lv.3 </FONT>善恶值 30,000 ~ 32,767 (防御：-6 /
     * 魔防+9)<BR>
     * <FONT COLOR="#0000ff">邪恶的守护 Lv.1 </FONT>善恶值 -10,000 ~ -19,999 (近/远距离攻击力+1
     * / 魔攻+1)<BR>
     * <FONT COLOR="#0000ff">邪恶的守护 Lv.2 </FONT>善恶值 -20,000 ~ -29,999 (近/远距离攻击力+3
     * / 魔攻+2)<BR>
     * <FONT COLOR="#0000ff">邪恶的守护 Lv.3 </FONT>善恶值 -30,000 ~ -32,767 (近/远距离攻击力+5
     * / 魔攻+3)<BR>
     * <FONT COLOR="#0000ff">遭遇的守护 </FONT>20级以下角色
     * 被超过10级以上的玩家攻击而死亡时，不会失去经验值，也不会掉落物品<BR>
     */
    public int guardianEncounter() {
        if (_jl1) {
            return 0;

        } else if (_jl2) {
            return 1;

        } else if (_jl3) {
            return 2;

        } else if (_el1) {
            return 3;

        } else if (_el2) {
            return 4;

        } else if (_el3) {
            return 5;
        }
        return -1;
    }

    private long _old_exp;

    /**
     * 原始Lawful
     * 
     * @return
     */
    public long getExpo() {
        return _old_exp;
    }

    /**
     * 更新EXP
     */
    public void onChangeExp() {
        if (_old_exp != getExp()) {
            _old_exp = getExp();

            final int level = ExpTable.getLevelByExp(getExp());
            final int char_level = getLevel();
            final int gap = level - char_level;

            if (gap == 0) {
                if (level <= 127) {
                    sendPackets(new S_Exp(this));
                } else {
                    sendPackets(new S_OwnCharStatus(this));
                }
                return;
            }

            if (gap > 0) {
                levelUp(gap);

            } else if (gap < 0) {
                levelDown(gap);
            }

//            if (getLevel() > 20) {// LOLI 战斗特化
//                sendPackets(new S_PacketBoxProtection(
//                        S_PacketBoxProtection.ENCOUNTER, 0));
//
//            } else {
//                sendPackets(new S_PacketBoxProtection(
//                        S_PacketBoxProtection.ENCOUNTER, 1));
//            }
        }
    }
    
    private int _old_karma;
    
    /**
     * 原始友好度
     * 
     * @return
     */
    /**
     * 更新友好度 add hjx1000
     */
    public void onChangeKarma() {
    	if (_old_karma != this.getKarma()) {
    		_old_karma = this.getKarma();
    		sendPackets(new S_Karma(this));
    	}
    	
    }
    /**
     * TODO 接触资讯
     */
    @Override
    public void onPerceive(final L1PcInstance perceivedFrom) {
        try {
            if (this.isGmInvis() || this.isGhost() || this.isInvisble()) {
                return;
            }
            
            // 副本ID不相等 不相护显示
            if (perceivedFrom.get_showId() != this.get_showId()) {
                return;
            }

            perceivedFrom.addKnownObject(this);
            // 发送自身资讯给予接触人物
            perceivedFrom.sendPackets(new S_OtherCharPacks(this));

            // 队伍成员HP状态发送
            if (this.isInParty() && this.getParty().isMember(perceivedFrom)) {
                perceivedFrom.sendPackets(new S_HPMeter(this));
            }

            if (_isFishing) {
                perceivedFrom.sendPackets(new S_Fishing(getId(),
                        ActionCodes.ACTION_Fishing, get_fishX(), get_fishY()));
            }

            if (this.isPrivateShop()) {
                final int mapId = this.getMapId();
                if ((mapId != 340) && (mapId != 350) && (mapId != 360)
                        && (mapId != 370) && (mapId != 4)) { //增加大陆可以摆商店 hjx1000
                    this.getSellList().clear();
                    this.getBuyList().clear();

                    this.setPrivateShop(false);
                    this.sendPacketsAll(new S_DoActionGFX(this.getId(),
                            ActionCodes.ACTION_Idle));

                } else {
                    perceivedFrom.sendPackets(new S_DoActionShop(this.getId(),
                            this.getShopChat()));
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 清除离开可视范围物件
     */
    private void removeOutOfRangeObjects() {
        for (final L1Object known : getKnownObjects()) {
            if (known == null) {
                continue;
            }

            if (Config.PC_RECOGNIZE_RANGE == -1) {
                if (!getLocation().isInScreen(known.getLocation())) { // 画面外
                    removeKnownObject(known);
                    sendPackets(new S_RemoveObject(known));
                }

            } else {
                if (getLocation().getTileLineDistance(known.getLocation()) > Config.PC_RECOGNIZE_RANGE) {
                    removeKnownObject(known);
                    sendPackets(new S_RemoveObject(known));
                }
            }
        }
    }

    /**
     * 可见物更新处理
     */
    public void updateObject() {
        if (getOnlineStatus() != 1) {
            return;
        }
        removeOutOfRangeObjects();

        // 指定可视范围资料更新
        for (final L1Object visible : World.get().getVisibleObjects(this,
                Config.PC_RECOGNIZE_RANGE)) {
            if (visible instanceof L1MerchantInstance) {// 对话NPC
                if (!knownsObject(visible)) {
                    final L1MerchantInstance npc = (L1MerchantInstance) visible;
                    // 未认知物件 执行物件封包发送
                    npc.onPerceive(this);
                }
                continue;
            }

            if (visible instanceof L1DwarfInstance) {// 仓库NPC
                if (!knownsObject(visible)) {
                    final L1DwarfInstance npc = (L1DwarfInstance) visible;
                    // 未认知物件 执行物件封包发送
                    npc.onPerceive(this);
                }
                continue;
            }

            if (visible instanceof L1FieldObjectInstance) {// 景观
                if (!knownsObject(visible)) {
                    final L1FieldObjectInstance npc = (L1FieldObjectInstance) visible;
                    // 未认知物件 执行物件封包发送
                    npc.onPerceive(this);
                }
                continue;
            }

            // 副本ID不相等 不相护显示
            if (visible.get_showId() != get_showId()) {
                continue;
            }

            if (!knownsObject(visible)) {
                // 未认知物件 执行物件封包发送
                visible.onPerceive(this);
            } else {
                if (visible instanceof L1NpcInstance) {
                    final L1NpcInstance npc = (L1NpcInstance) visible;
                    if (getLocation().isInScreen(npc.getLocation())
                            && (npc.getHiddenStatus() != 0)) {
                        npc.approachPlayer(this);
                    }
                }
            }

            // 一般人物 HP可见设置
            if (isHpBarTarget(visible)) {
                final L1Character cha = (L1Character) visible;
                cha.broadcastPacketHP(this);
            }

            // GM HP 查看设置
            if (hasSkillEffect(GMSTATUS_HPBAR)) {
                if (isGmHpBarTarget(visible)) {
                    final L1Character cha = (L1Character) visible;
                    cha.broadcastPacketHP(this);
                }
            }
        }
    }

    /**
     * 可以观看HP的对象(特别定义)
     * 
     * @param obj
     * @return
     */
    public boolean isHpBarTarget(final L1Object obj) {
        // 所在地图位置
        switch (this.getMapId()) {
            case 400:// 大洞穴/大洞穴抵抗军/隐遁者地区
                if (obj instanceof L1FollowerInstance) {
                    final L1FollowerInstance follower = (L1FollowerInstance) obj;
                    if (follower.getMaster().equals(this)) {
                        return true;
                    }
                }
                break;
            case 88:
                if (obj instanceof L1NpcInstance) {
                    if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 100000)
                        return true;
                }
                break;            
        }
        return false;
    }

    /**
     * GM HPBAR 可以观看HP的对象
     * 
     * @param obj
     * @return
     */
    public boolean isGmHpBarTarget(final L1Object obj) {
        if (obj instanceof L1MonsterInstance) {
            return true;
        }
        if (obj instanceof L1PcInstance) {
            return true;
        }
        if (obj instanceof L1SummonInstance) {
            return true;
        }
        if (obj instanceof L1PetInstance) {
            return true;
        }
        if (obj instanceof L1FollowerInstance) {
            return true;
        }
        return false;
    }

    private void sendVisualEffect() {
        int poisonId = 0;
        if (this.getPoison() != null) { // 毒状态
            poisonId = this.getPoison().getEffectId();
        }
        if (this.getParalysis() != null) { // 麻痹状态
            // 麻痹エフェクトを优先して送りたい为、poisonIdを上书き。
            poisonId = this.getParalysis().getEffectId();
        }
        if (poisonId != 0) { // このifはいらないかもしれない
            this.sendPacketsAll(new S_Poison(this.getId(), poisonId));
        }
    }

    public void sendVisualEffectAtLogin() {
        this.sendVisualEffect();
    }

    private boolean _isCHAOTIC = false;

    public boolean isCHAOTIC() {
        return this._isCHAOTIC;
    }

    public void setCHAOTIC(final boolean flag) {
        this._isCHAOTIC = flag;
    }

    public void sendVisualEffectAtTeleport() {
        if (this.isDrink()) { // 醉酒效果
            this.sendPackets(new S_Liquor(this.getId()));
        }
        if (this.isCHAOTIC()) { // 混乱效果
            this.sendPackets(new S_Liquor(this.getId(), 2));
        }
        this.sendVisualEffect();
    }

    // 可用技能编号列表
    private final ArrayList<Integer> _skillList = new ArrayList<Integer>();

    /**
     * 加入技能编号列表
     * 
     * @param skillid
     */
    public void setSkillMastery(final int skillid) {
        if (!this._skillList.contains(new Integer(skillid))) {
            this._skillList.add(new Integer(skillid));
        }
    }

    /**
     * 移出技能编号列表
     * 
     * @param skillid
     */
    public void removeSkillMastery(final int skillid) {
        if (this._skillList.contains(new Integer(skillid))) {
            this._skillList.remove(new Integer(skillid));
        }
    }

    /**
     * 传回是否具有该技能使用权
     * 
     * @param skillid
     * @return
     */
    public boolean isSkillMastery(final int skillid) {
        return this._skillList.contains(new Integer(skillid));
    }

    /**
     * 清空
     */
    public void clearSkillMastery() {
        this._skillList.clear();
    }

    /**
     * TODO 起始设置
     */
    public L1PcInstance() {
        _accessLevel = 0;
        _currentWeapon = 0;
        _inventory = new L1PcInventory(this);
        _dwarf = new L1DwarfInventory(this);
        _dwarfForElf = new L1DwarfForElfInventory(this);
        _tradewindow = new L1Inventory();
        _quest = new L1PcQuest(this);
        _action = new L1ActionPc(this);
        _actionPet = new L1ActionPet(this);
        _actionSummon = new L1ActionSummon(this);
        _equipSlot = new L1EquipmentSlot(this);
    }

    /**
     * 娃娃跟随主人变更移动/速度状态
     */
    public void setNpcSpeed() {
        try {
            // 取回娃娃
            if (!getDolls().isEmpty()) {
                for (final Object obj : getDolls().values().toArray()) {
                    final L1DollInstance doll = (L1DollInstance) obj;
                    if (doll != null) {
                        doll.setNpcMoveSpeed();
                    }
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void setCurrentHp(final int i) {
        int currentHp = Math.min(i, this.getMaxHp());

        if (this.getCurrentHp() == currentHp) {
            return;
        }

        if (currentHp <= 0) {
            if (this.isGm()) {
                currentHp = this.getMaxHp();

            } else {
                if (!this.isDead()) {
                    this.death(null); // HP小于1死亡
                }
            }
        }

        this.setCurrentHpDirect(currentHp);
        this.sendPackets(new S_HPUpdate(currentHp, this.getMaxHp()));
        if (this.isInParty()) { // 队伍状态
            this.getParty().updateMiniHP(this);
        }
    }

    @Override
    public void setCurrentMp(final int i) {
        int currentMp = Math.min(i, this.getMaxMp());
        isUndoPoly(currentMp);
        if (this.getCurrentMp() == currentMp) {
            return;
        }

        this.setCurrentMpDirect(currentMp);

        this.sendPackets(new S_MPUpdate(currentMp, this.getMaxMp()));
    }
    /**
     * 是否解除龙骑士觉醒变身状态.
     * 
     * @param currentMp
     *            - 当前的MP
     */
    private void isUndoPoly(int currentMp) {
        if (currentMp <= 0 && isDragonKnight()) {
            switch (getAwakeSkillId()) {
                case L1SkillId.AWAKEN_ANTHARAS:
                case L1SkillId.AWAKEN_FAFURION:
                case L1SkillId.AWAKEN_VALAKAS:
                    final SkillMode mode = L1SkillMode.get().getSkill(
                            getAwakeSkillId());
                    try {
                        mode.stop(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }
    @Override
    public L1PcInventory getInventory() {
        return this._inventory;
    }

    public L1DwarfInventory getDwarfInventory() {
        return this._dwarf;
    }

    public L1DwarfForElfInventory getDwarfForElfInventory() {
        return this._dwarfForElf;
    }

	public L1Inventory getTradeWindowInventory() {
		return _tradewindow;
	}
    
    public boolean isGmInvis() {
        return this._gmInvis;
    }

    public void setGmInvis(final boolean flag) {
        this._gmInvis = flag;
    }

    public int getCurrentWeapon() {
        return this._currentWeapon;
    }

    public void setCurrentWeapon(final int i) {
        this._currentWeapon = i;
    }

    /**
     * 0:王族 1:骑士 2:精灵 3:法师 4:黑妖 5:龙骑 6:幻术
     * 
     * @return
     */
    public int getType() {
        return this._type;
    }

    /**
     * 0:王族 1:骑士 2:精灵 3:法师 4:黑妖 5:龙骑 6:幻术
     * 
     * @param i
     */
    public void setType(final int i) {
        this._type = i;
    }

    public short getAccessLevel() {
        return this._accessLevel;
    }

    public void setAccessLevel(final short i) {
        this._accessLevel = i;
    }

    public int getClassId() {
        return this._classId;
    }

    public void setClassId(final int i) {
        this._classId = i;
        this._classFeature = L1ClassFeature.newClassFeature(i);
    }

    private L1ClassFeature _classFeature = null;

    public L1ClassFeature getClassFeature() {
        return _classFeature;
    }

    @Override
    public synchronized long getExp() {
        return _exp;
    }

    @Override
    public synchronized void setExp(final long i) {
        _exp = i;
    }

    private int _PKcount; // ● PKカウント

    public int get_PKcount() {
        return this._PKcount;
    }

    public void set_PKcount(final int i) {
        this._PKcount = i;
    }

    private int _PkCountForElf; // ● PKカウント(エルフ用)

    public int getPkCountForElf() {
        return this._PkCountForElf;
    }

    public void setPkCountForElf(final int i) {
        this._PkCountForElf = i;
    }

    private int _clanid; // 血盟ID

    public int getClanid() {
        return this._clanid;
    }

    public void setClanid(final int i) {
        this._clanid = i;
    }

    private String clanname; // 血盟名称

    public String getClanname() {
        return this.clanname;
    }

    public void setClanname(final String s) {
        this.clanname = s;
    }

    /**
     * 血盟资料
     * 
     * @return
     */
    public L1Clan getClan() {
        return WorldClan.get().getClan(this.getClanname());
    }

    private int _clanRank; // ● クラン内のランク(血盟君主、ガーディアン、一般、见习い)

    /**
     * 血盟阶级
     * 
     * @return
     */
    public int getClanRank() {
        return this._clanRank;
    }

    public void setClanRank(final int i) {
        this._clanRank = i;
    }

    private byte _sex; // ● 性别

    /**
     * 性别
     * 
     * @return
     */
    public byte get_sex() {
        return this._sex;
    }

    /**
     * 性别
     * 
     * @param i
     */
    public void set_sex(final int i) {
        this._sex = (byte) i;
    }

    public boolean isGm() {
        return this._gm;
    }

    public void setGm(final boolean flag) {
        this._gm = flag;
    }

    public boolean isMonitor() {
        return this._monitor;
    }

    public void setMonitor(final boolean flag) {
        this._monitor = flag;
    }

    private L1PcInstance getStat() {
        return null;
    }

    public void reduceCurrentHp(final double d, final L1Character l1character) {
        this.getStat().reduceCurrentHp(d, l1character);
    }

    /**
     * 指定されたプレイヤー群にログアウトしたことを通知する
     * 
     * @param playersList
     *            通知するプレイヤーの配列
     */
    private void notifyPlayersLogout(final List<L1PcInstance> playersArray) {
        for (final L1PcInstance player : playersArray) {
            if (player.knownsObject(this)) {
                player.removeKnownObject(this);
                player.sendPackets(new S_RemoveObject(this));
            }
        }
    }

    public void logout() {
        // 保留技能纪录
        CharBuffReading.get().deleteBuff(this);
        CharBuffReading.get().saveBuff(this);

        // 解除旧座标障碍宣告
        this.getMap().setPassable(this.getLocation(), true);

        if (this.getClanid() != 0) {
            final L1Clan clan = WorldClan.get().getClan(this.getClanname());
            if (clan != null) {
                if (clan.getWarehouseUsingChar() == this.getId()) {
                    clan.setWarehouseUsingChar(0); // 解除血盟仓库目前使用者
                }
            }
        }
        this.notifyPlayersLogout(this.getKnownPlayers());

        // 正在参加副本
        if (this.get_showId() != -1) {
            // 副本编号 是执行中副本
            if (WorldQuest.get().isQuest(this.get_showId())) {
                // 移出副本
                WorldQuest.get().remove(this.get_showId(), this);
            }
        }
        // 重置副本编号
        this.set_showId(-1);

        World.get().removeVisibleObject(this);
        World.get().removeObject(this);
        this.notifyPlayersLogout(World.get().getRecognizePlayer(this));

        // this._inventory.clearItems();
        // this._dwarf.clearItems();

        this.removeAllKnownObjects();
        this.stopHpRegeneration();
        this.stopMpRegeneration();
        this.setDead(true); // 使い方おかしいかもしれないけど、ＮＰＣに消灭したことをわからせるため
        this.setNetConnection(null);
        this.setPacketOutput(null);
        this.stopRocksPrison();
    }

    public ClientExecutor getNetConnection() {
        return this._netConnection;
    }

    public void setNetConnection(final ClientExecutor clientthread) {
        this._netConnection = clientthread;
    }

    /**
     * 是否再队伍中
     * 
     * @return
     */
    public boolean isInParty() {
        return this.getParty() != null;
    }

    /**
     * 传回队伍
     * 
     * @return
     */
    public L1Party getParty() {
        return this._party;
    }

    /**
     * 设置队伍
     * 
     * @param p
     */
    public void setParty(final L1Party p) {
        this._party = p;
    }

    public boolean isInChatParty() {
        return this.getChatParty() != null;
    }

    public L1ChatParty getChatParty() {
        return this._chatParty;
    }

    public void setChatParty(final L1ChatParty cp) {
        this._chatParty = cp;
    }

    public int getPartyID() {
        return this._partyID;
    }

    public void setPartyID(final int partyID) {
        this._partyID = partyID;
    }

    public int getTradeID() {
        return this._tradeID;
    }

    public void setTradeID(final int tradeID) {
        this._tradeID = tradeID;
    }

    public void setTradeOk(final boolean tradeOk) {
        this._tradeOk = tradeOk;
    }

    public boolean getTradeOk() {
        return this._tradeOk;
    }

    /**
     * 传回暂时纪录的objid
     * 
     * @return
     */
    public int getTempID() {
        return this._tempID;
    }

    /**
     * 设置暂时纪录的objid
     * 
     * @param tempID
     */
    public void setTempID(final int tempID) {
        this._tempID = tempID;
    }

    /**
     * 是否为传送状态中
     * 
     * @return
     */
    public boolean isTeleport() {
        return this._isTeleport;
    }

    /**
     * 设置传送状态中
     * 
     * @param flag
     */
    public void setTeleport(final boolean flag) {
        if (flag) {
            this.setNowTarget(null);// 解除目前攻击目标设置
        }
        this._isTeleport = flag;
    }

    /**
     * 醉酒状态
     * 
     * @return
     */
    public boolean isDrink() {
        return this._isDrink;
    }

    /**
     * 醉酒状态
     * 
     * @param flag
     */
    public void setDrink(final boolean flag) {
        this._isDrink = flag;
    }

    public boolean isGres() {
        return this._isGres;
    }

    public void setGres(final boolean flag) {
        this._isGres = flag;
    }

    /**
     * 红名状态
     * 
     * @return
     */
    public boolean isPinkName() {
        return this._isPinkName;
    }

    /**
     * 红名状态
     * 
     * @param flag
     */
    public void setPinkName(final boolean flag) {
        this._isPinkName = flag;
    }

    // 卖出物品清单
    private final ArrayList<L1PrivateShopSellList> _sellList = new ArrayList<L1PrivateShopSellList>();

    /**
     * 传回卖出物品清单
     * 
     * @return
     */
    public ArrayList<L1PrivateShopSellList> getSellList() {
        return this._sellList;
    }

    // 回收物品清单
    private final ArrayList<L1PrivateShopBuyList> _buyList = new ArrayList<L1PrivateShopBuyList>();

    /**
     * 传回回收物品清单
     * 
     * @return
     */
    public ArrayList<L1PrivateShopBuyList> getBuyList() {
        return this._buyList;
    }

    private byte[] _shopChat;

    public void setShopChat(final byte[] chat) {
        this._shopChat = chat;
    }

    public byte[] getShopChat() {
        return this._shopChat;
    }

    private boolean _isPrivateShop = false;

    /**
     * 传回商店模式
     * 
     * @return
     */
    public boolean isPrivateShop() {
        return this._isPrivateShop;
    }

    /**
     * 设置商店模式
     * 
     * @param flag
     */
    public void setPrivateShop(final boolean flag) {
        this._isPrivateShop = flag;
    }

    // 正在执行个人商店交易
    private boolean _isTradingInPrivateShop = false;

    /**
     * 正在执行个人商店交易
     * 
     * @return
     */
    public boolean isTradingInPrivateShop() {
        return this._isTradingInPrivateShop;
    }

    /**
     * 正在执行个人商店交易
     * 
     * @param flag
     */
    public void setTradingInPrivateShop(final boolean flag) {
        this._isTradingInPrivateShop = flag;
    }

    private int _partnersPrivateShopItemCount = 0; // 出售物品种类数量

    /**
     * 传回出售物品种类数量
     * 
     * @return
     */
    public int getPartnersPrivateShopItemCount() {
        return this._partnersPrivateShopItemCount;
    }

    /**
     * 设置出售物品种类数量
     * 
     * @param i
     */
    public void setPartnersPrivateShopItemCount(final int i) {
        this._partnersPrivateShopItemCount = i;
    }

    private EncryptExecutor _out;// 封包加密管理

    /**
     * 设置封包加密管理
     * 
     * @param out
     */
    public void setPacketOutput(final EncryptExecutor out) {
        this._out = out;
    }

    /**
     * 发送单体封包
     * 
     * @param packet
     *            封包
     */
    public void sendPackets(final ServerBasePacket packet) {
        if (this._out == null) {
            return;
        }
         //System.out.println(packet.toString());
        try {
            this._out.encrypt(packet);

        } catch (final Exception e) {
            this.logout();
            this.close();
        }
    }

    /**
     * 发送单体封包 与可见范围发送封包
     * 
     * @param packet
     *            封包
     */
    public void sendPacketsAll(final ServerBasePacket packet) {
        if (this._out == null) {
            return;
        }

        try {
            // 自己
            this._out.encrypt(packet);
            if (!this.isGmInvis() && !this.isInvisble()) {
                this.broadcastPacketAll(packet);
            }

        } catch (final Exception e) {
            this.logout();
            this.close();
        }
    }

    /**
     * 发送单体封包 与指定范围发送封包(范围8)
     * 
     * @param packet
     *            封包
     */
    public void sendPacketsX8(final ServerBasePacket packet) {
        if (this._out == null) {
            return;
        }

        try {
            // 自己
            this._out.encrypt(packet);
            if (!this.isGmInvis() && !this.isInvisble()) {
                this.broadcastPacketX8(packet);
            }

        } catch (final Exception e) {
            this.logout();
            this.close();
        }
    }

    /**
     * 发送单体封包 与指定范围发送封包(范围10)
     * 
     * @param packet
     *            封包
     */
    public void sendPacketsX10(final ServerBasePacket packet) {
        if (this._out == null) {
            return;
        }

        try {
            // 自己
            this._out.encrypt(packet);
            if (!this.isGmInvis() && !this.isInvisble()) {
                this.broadcastPacketX10(packet);
            }

        } catch (final Exception e) {
            this.logout();
            this.close();
        }
    }

    /**
     * 发送单体封包 与可见指定范围发送封包
     * 
     * @param packet
     *            封包
     * @param r
     *            范围
     */
    public void sendPacketsXR(final ServerBasePacket packet, final int r) {
        if (this._out == null) {
            return;
        }

        try {
            // 自己
            this._out.encrypt(packet);
            if (!this.isGmInvis() && !this.isInvisble()) {
                this.broadcastPacketXR(packet, r);
            }

        } catch (final Exception e) {
            this.logout();
            this.close();
        }
    }

    /**
     * 关闭连线线程
     */
    private void close() {
        try {
            this.getNetConnection().close();
        } catch (final Exception e) {

        }
    }

    /**
     * 对该物件攻击的调用
     * 
     * @param attacker
     *            攻击方
     */
    @Override
    public void onAction(final L1PcInstance attacker) {
        // NullPointerException回避。onActionの引数の型はL1Characterのほうが良い？
        if (attacker == null) {
            return;
        }
        // テレポート处理中
        if (this.isTeleport()) {
            return;
        }

        // 双方之一 位于安全区域 仅送出动作资讯
        if (this.isSafetyZone() || attacker.isSafetyZone()) {
            // 攻击モーション送信
            final L1AttackMode attack_mortion = new L1AttackPc(attacker, this);
            attack_mortion.action();
            return;
        }

        // 禁止PK服务器 仅送出动作资讯
        if (this.checkNonPvP(this, attacker) == true) {
            final L1AttackMode attack_mortion = new L1AttackPc(attacker, this);
            attack_mortion.action();
            return;
        }

        if ((this.getCurrentHp() > 0) && !this.isDead()) {
            // 攻击行为产生解除隐身
            attacker.delInvis();

            boolean isCounterBarrier = false;
            // 开始计算攻击
            final L1AttackMode attack = new L1AttackPc(attacker, this);
            if (attack.calcHit()) {
                if (this.hasSkillEffect(COUNTER_BARRIER)) {
                    final L1Magic magic = new L1Magic(this, attacker);
                    final boolean isProbability = magic
                            .calcProbabilityMagic(COUNTER_BARRIER);
                    final boolean isShortDistance = attack.isShortDistance();
                    if (isProbability && isShortDistance) {
                        isCounterBarrier = true;
                    }
                }
                if (!isCounterBarrier) {
                    attacker.setPetTarget(this);

                    attack.calcDamage();
                    attack.calcStaffOfMana();
                    attack.addChaserAttack();
                }
            }
            if (isCounterBarrier) {
                // attack.actionCounterBarrier();
                attack.commitCounterBarrier();

            } else {
                attack.action();
                attack.commit();
            }
        }
    }

    /**
     * 检查是否可以攻击
     * 
     * @param pc
     * @param target
     * @return
     */
    public boolean checkNonPvP(final L1PcInstance pc, final L1Character target) {
        L1PcInstance targetpc = null;
        if (target instanceof L1PcInstance) {
            targetpc = (L1PcInstance) target;

        } else if (target instanceof L1PetInstance) {
            targetpc = (L1PcInstance) ((L1PetInstance) target).getMaster();

        } else if (target instanceof L1SummonInstance) {
            targetpc = (L1PcInstance) ((L1SummonInstance) target).getMaster();
        }
        if (targetpc == null) {
            return false; // 相手がPC、サモン、ペット以外
        }

        if (!ConfigAlt.ALT_NONPVP) { // Non-PvP设定
            if (this.getMap().isCombatZone(this.getLocation())) {
                return false;
            }

            // 取回全部战争清单
            for (final L1War war : WorldWar.get().getWarList()) {
                if ((pc.getClanid() != 0) && (targetpc.getClanid() != 0)) { // 共にクラン所属中
                    final boolean same_war = war.checkClanInSameWar(
                            pc.getClanname(), targetpc.getClanname());
                    if (same_war == true) { // 同じ战争に参加中
                        return false;
                    }
                }
            }
            // Non-PvP设定でも战争中は布告なしで攻击可能
            if (target instanceof L1PcInstance) {
                final L1PcInstance targetPc = (L1PcInstance) target;
                if (this.isInWarAreaAndWarTime(pc, targetPc)) {
                    return false;
                }
            }
            return true;

        } else {
            return false;
        }
    }

    /**
     * 战争旗帜座标内
     * 
     * @param pc
     * @param target
     * @return
     */
    private boolean isInWarAreaAndWarTime(final L1PcInstance pc,
            final L1PcInstance target) {
        // pcとtargetが战争中に战争エリアに居るか
        final int castleId = L1CastleLocation.getCastleIdByArea(pc);
        final int targetCastleId = L1CastleLocation.getCastleIdByArea(target);
        if ((castleId != 0) && (targetCastleId != 0)
                && (castleId == targetCastleId)) {
            if (ServerWarExecutor.get().isNowWar(castleId)) {
                return true;
            }
        }
        return false;
    }

    private static boolean _debug = Config.DEBUG;

    /**
     * 设置 宠物/召换兽/分身/护卫 攻击目标
     * 
     * @param target
     */
    public void setPetTarget(final L1Character target) {
        if (target == null) {
            return;
        }
        if (target.isDead()) {
            return;
        }
        final Map<Integer, L1NpcInstance> petList = this.getPetList();

        // 有宠物元素
        try {
            if (!petList.isEmpty()) {// 有宠物元素
                for (final Iterator<L1NpcInstance> iter = petList.values()
                        .iterator(); iter.hasNext();) {
                    final L1NpcInstance pet = iter.next();
                    if (pet != null) {
                        if (pet instanceof L1PetInstance) {// 宠物
                            final L1PetInstance pets = (L1PetInstance) pet;
                            pets.setMasterTarget(target);

                        } else if (pet instanceof L1SummonInstance) {// 召换兽
                            final L1SummonInstance summon = (L1SummonInstance) pet;
                            summon.setMasterTarget(target);
                        }
                    }
                }
            }

        } catch (final Exception e) {
            if (_debug) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }

        final Map<Integer, L1IllusoryInstance> illList = this.get_otherList()
                .get_illusoryList();

        // 有分身元素
        try {
            if (!illList.isEmpty()) {// 有分身元素
                // 控制分身攻击
                if (this.getId() != target.getId()) {
                    for (final Iterator<L1IllusoryInstance> iter = illList
                            .values().iterator(); iter.hasNext();) {
                        final L1IllusoryInstance ill = iter.next();
                        if (ill != null) {
                            ill.setLink(target);
                        }
                    }
                }
            }

        } catch (final Exception e) {
            if (_debug) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    /**
     * 解除隐身术/暗隐术
     */
    public void delInvis() {
        if (this.hasSkillEffect(INVISIBILITY)) { // 隐身术
            this.killSkillEffectTimer(INVISIBILITY);
            this.sendPackets(new S_Invis(this.getId(), 0));
            this.broadcastPacketAll(new S_OtherCharPacks(this));
        }
        if (this.hasSkillEffect(BLIND_HIDING)) { // 暗隐术
            this.killSkillEffectTimer(BLIND_HIDING);
            this.sendPackets(new S_Invis(this.getId(), 0));
            this.broadcastPacketAll(new S_OtherCharPacks(this));
        }
    }

    /**
     * 解除暗隐术
     */
    public void delBlindHiding() {
        this.killSkillEffectTimer(BLIND_HIDING);
        this.sendPackets(new S_Invis(this.getId(), 0));
        this.broadcastPacketAll(new S_OtherCharPacks(this));
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
            damage /= 2.0;
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

        double coefficient = (1.0 - attrDeffence + 3.0 / 32.0);// 0.09375

        if (coefficient > 0) {
            damage *= coefficient;
        }
        this.receiveDamage(attacker, damage, false, false);
    }

    /**
     * 受攻击mp减少计算
     * 
     * @param attacker
     * @param mpDamage
     */
    public void receiveManaDamage(final L1Character attacker, final int mpDamage) {
        if ((mpDamage > 0) && !this.isDead()) {
            this.delInvis();
            if (attacker instanceof L1PcInstance) {
                L1PinkName.onAction(this, attacker);
            }
            if ((attacker instanceof L1PcInstance)
                    && ((L1PcInstance) attacker).isPinkName()) {
                // ガードが画面内にいれば、攻击者をガードのターゲットに设定する
                for (final L1Object object : World.get().getVisibleObjects(
                        attacker)) {
                    if (object instanceof L1GuardInstance) {
                        final L1GuardInstance guard = (L1GuardInstance) object;
                        guard.setTarget(((L1PcInstance) attacker));
                    }
                }
            }

            int newMp = this.getCurrentMp() - mpDamage;
            if (newMp > this.getMaxMp()) {
                newMp = this.getMaxMp();
            }
            newMp = Math.max(newMp, 0);

            this.setCurrentMp(newMp);
        }
    }

    public long _oldTime = 0; // 连续魔法减低损伤使用

    private static final Map<Long, Double> _magicDamagerList = new HashMap<Long, Double>();

    /**
     * 连续魔法减低损伤质预先载入 特殊定义道具 预先载入
     */
    public static void load() {
        double newdmg = 100.00;
        for (long i = 2000; i > 0; i--) {
            if (i % 100 == 0) {
                newdmg -= 3.33;
            }
            _magicDamagerList.put(i, newdmg);
        }
    }

    /**
     * 连续魔法减低损伤
     * 
     * @param damage
     * @return
     */
    public double isMagicDamager(final double damage) {
        final long nowTime = System.currentTimeMillis();
        final long interval = nowTime - this._oldTime;

        double newdmg = 0;
        if (damage < 0) {
            newdmg = damage;

        } else {
            Double tmpnewdmg = _magicDamagerList.get(interval);
            if (tmpnewdmg != null) {
                newdmg = (damage * tmpnewdmg) / 100;

            } else {
                newdmg = damage;
            }
            newdmg = Math.max(newdmg, 0);

            this._oldTime = nowTime; // 次回时间纪录
        }
        return newdmg;
    }

    /**
     * 受攻击hp减少计算
     * 
     * @param attacker
     *            攻击者
     * @param damage
     *            伤害
     * @param isMagicDamage
     *            连续魔法伤害减低
     * @param isCounterBarrier
     *            这个伤害是否不执行反馈 true:不执行反馈 false:执行反馈
     */
    public void receiveDamage(final L1Character attacker, double damage,
            final boolean isMagicDamage, final boolean isCounterBarrier) {
        if ((this.getCurrentHp() > 0) && !this.isDead()) {

            if (attacker != null) {
                if (attacker != this) {
                    if (!(attacker instanceof L1EffectInstance)
                            && !this.knownsObject(attacker)
                            && attacker.getMapId() == this.getMapId()) {
                        attacker.onPerceive(this);
                    }
                }

                // 连续魔法伤害减低
                if (isMagicDamage == true) {
                    damage = this.isMagicDamager(damage);
                }

                // 攻击者定义
                L1PcInstance attackPc = null;
                L1NpcInstance attackNpc = null;

                if (attacker instanceof L1PcInstance) {
                    attackPc = (L1PcInstance) attacker;// 攻击者为PC

                } else if (attacker instanceof L1NpcInstance) {
                    attackNpc = (L1NpcInstance) attacker;// 攻击者为NPC
                }

                // 伤害大于等于0(小于0回复HP)
                if (damage > 0) {
                    // 解除隐身
                    this.delInvis();
                    // 解除沉睡之雾
                    this.removeSkillEffect(FOG_OF_SLEEPING);

                    if (attackPc != null) {
                        L1PinkName.onAction(this, attackPc);
                        if (attackPc.isPinkName()) {
                            // 警卫对攻击者的处分
                            for (final L1Object object : World.get()
                                    .getVisibleObjects(attacker)) {
                                if (object instanceof L1GuardInstance) {
                                    final L1GuardInstance guard = (L1GuardInstance) object;
                                    guard.setTarget(((L1PcInstance) attacker));
                                }
                            }
                        }
                    }
                    //this.setHate(attacker, (int) damage);//PCAI hjx1000
                }

                if (!isCounterBarrier) {// false:执行反馈
                    // 致命身躯(自身具有效果)
                    if (this.hasSkillEffect(MORTAL_BODY)) {
                        if (this.getId() != attacker.getId()) {
                            final int rnd = _random.nextInt(100) + 1;
                            if ((damage > 0) && (rnd <= 18)) {// 2011-11-26 0-15
                                final int dmg = attacker.getLevel() >> 1;//修改龙骑致命身躯反伤减低一倍.hjx1000
                                // SRC DMG = 50
                                if (attackPc != null) {
                                    attackPc.sendPacketsX10(new S_DoActionGFX(
                                            attackPc.getId(),
                                            ActionCodes.ACTION_Damage));
                                    attackPc.receiveDamage(this, dmg, false,
                                            true);
                                    if (!attackPc.isHardDelay()) { //动作延时 hjx1000
                                    	HardDelay.onHardUse(attackPc, 150);
                                    }

                                } else if (attackNpc != null) {
                                    attackNpc
                                            .broadcastPacketX10(new S_DoActionGFX(
                                                    attackNpc.getId(),
                                                    ActionCodes.ACTION_Damage));
                                    attackNpc.receiveDamage(this, dmg);
                                }
                            }
                        }
                    }

                    // 攻击者被施放疼痛的欢愉
                    if (attacker.hasSkillEffect(JOY_OF_PAIN)) {
                        if (this.getId() != attacker.getId()) {
                            attacker.killSkillEffectTimer(JOY_OF_PAIN);
                            // 攻击者药物提高的HP
                            int hpup = this.get_other().get_addhp();

                            // (>> 1: 除) (<< 1: 乘)
                            final int nowDamage = (getMaxHp() - getCurrentHp() - hpup) / 5;// /
                                                                                           // 2012-06-18

                            if (nowDamage > 0) {
                                // 给予伤害质
                                if (attackPc != null) {
                                    attackPc.sendPacketsX10(new S_DoActionGFX(
                                            attackPc.getId(),
                                            ActionCodes.ACTION_Damage));
                                    attackPc.receiveDamage(this, nowDamage,
                                            false, true);
                                    if (!attackPc.isHardDelay()) { //动作延时 hjx1000
                                    	HardDelay.onHardUse(attackPc, 150);
                                    }

                                } else if (attackNpc != null) {
                                    attackNpc
                                            .broadcastPacketX10(new S_DoActionGFX(
                                                    attackNpc.getId(),
                                                    ActionCodes.ACTION_Damage));
                                    attackNpc.receiveDamage(this, nowDamage);
                                }
                            }
                        }
                    }
                }
            }

            // 装备使自己伤害加深的装备
            if (this.getInventory().checkEquipped(145) // 狂战士斧
                    || this.getInventory().checkEquipped(149)) { // 牛人斧头
                damage *= 1.5; // 伤害提高1.5倍
            }

            // 幻觉：化身219
            if (this.hasSkillEffect(ILLUSION_AVATAR)) {
                damage *= 1.05; // 伤害提高5%
            }

            int addhp = 0;
            if (_elitePlateMail_Fafurion > 0) {
                if (_random.nextInt(1000) <= _elitePlateMail_Fafurion) {
                    this.sendPacketsX8(new S_SkillSound(this.getId(), 2187));
                    addhp = _random.nextInt(_fafurion_hpmax - _fafurion_hpmin
                            + 1)
                            + _fafurion_hpmin;// 受到攻击时，4%的机率会恢复体力72~86点。
                }
            }

            int newHp = this.getCurrentHp() - (int) (damage) + addhp;
            if (newHp > this.getMaxHp()) {
                newHp = this.getMaxHp();
            }
            if (newHp <= 0) {
                if (!this.isGm()) {
                    this.death(attacker);
                }
            }

            this.setCurrentHp(newHp);

        } else if (!this.isDead()) {
            _log.error("人物hp减少处理失败 可能原因: 初始hp为0");
            this.death(attacker);
        }
    }

    /**
     * 死亡的处理
     * 
     * @param lastAttacker
     *            攻击致死的攻击者
     */
    public void death(final L1Character lastAttacker) {
        synchronized (this) {
            if (this.isDead()) {
                return;
            }
            this.setNowTarget(null);// 解除目前攻击目标设置
            this.setDead(true);
            this.setStatus(ActionCodes.ACTION_Die);
            if (this.isActived()) {
                this.setActived(false);//停止挂机状态 hjx1000
            }
        }
        GeneralThreadPool.get().execute(new Death(lastAttacker));

    }

    /**
     * 人物死亡的处理
     * 
     * @author dexc
     * 
     */
    private class Death implements Runnable {

        private L1Character _lastAttacker;

        private Death(final L1Character cha) {
            this._lastAttacker = cha;
        }

        @Override
        public void run() {
            final L1Character lastAttacker = this._lastAttacker;
            this._lastAttacker = null;
            L1PcInstance.this.setCurrentHp(0);
            L1PcInstance.this.setGresValid(false); // EXPロストするまでG-RES无效

            while (L1PcInstance.this.isTeleport()) { // 传送状态中延迟
                try {
                    Thread.sleep(300);

                } catch (final Exception e) {
                }
            }
            if (L1PcInstance.this.isInParty()) {// 队伍中
                for (final L1PcInstance member : L1PcInstance.this.getParty()
                        .partyUsers().values()) {
                    member.sendPackets(new S_PacketBoxParty(getParty(),
                            L1PcInstance.this));
                }
            }
            // 加入死亡清单
            set_delete_time(300);

            // 娃娃删除
            if (!getDolls().isEmpty()) {
                for (Object obj : getDolls().values().toArray()) {
                    final L1DollInstance doll = (L1DollInstance) obj;
                    doll.deleteDoll();
                }
            }

            L1PcInstance.this.stopHpRegeneration();
            L1PcInstance.this.stopMpRegeneration();

            final int targetobjid = L1PcInstance.this.getId();
            L1PcInstance.this.getMap().setPassable(
                    L1PcInstance.this.getLocation(), true);

            // 死亡时具有变身状态
            int tempchargfx = 0;
            if (L1PcInstance.this.hasSkillEffect(SHAPE_CHANGE)) {
                tempchargfx = L1PcInstance.this.getTempCharGfx();
                L1PcInstance.this.setTempCharGfxAtDead(tempchargfx);

            } else {
                L1PcInstance.this.setTempCharGfxAtDead(L1PcInstance.this
                        .getClassId());
            }

            // 死亡时 现有技能消除
            final L1SkillUse l1skilluse = new L1SkillUse();
            l1skilluse.handleCommands(L1PcInstance.this, CANCELLATION,
                    L1PcInstance.this.getId(), L1PcInstance.this.getX(),
                    L1PcInstance.this.getY(), 0, L1SkillUse.TYPE_LOGIN);

            // シャドウ系变身中に死亡するとクライアントが落ちるため暂定对应
            if ((tempchargfx == 5727) || (tempchargfx == 5730)
                    || (tempchargfx == 5733) || (tempchargfx == 5736)) {
                tempchargfx = 0;
            }

            if (tempchargfx == 7351) {
                tempchargfx = L1PcInstance.this.getClassId();
                L1PcInstance.this.setTempCharGfx(tempchargfx);
            }

            if (tempchargfx != 0) {
                // System.out.println("tempchargfx: " + tempchargfx);
                L1PcInstance.this.sendPacketsAll(new S_ChangeShape(
                        L1PcInstance.this, tempchargfx));

            } else {
                // シャドウ系变身中に攻击しながら死亡するとクライアントが落ちるためディレイを入れる
                try {
                    Thread.sleep(1000);
                } catch (final Exception e) {
                }
            }

            boolean isSafetyZone = false;// 是否为安全区中

            boolean isCombatZone = false;// 是否为战斗区中

            boolean isWar = false;// 是否参战

            if (L1PcInstance.this.isSafetyZone()) {
                isSafetyZone = true;
            }
            if (L1PcInstance.this.isCombatZone()) {
                isCombatZone = true;
            }

            // 杀人次数的减少
            if (lastAttacker instanceof L1GuardInstance) {
                if (L1PcInstance.this.get_PKcount() > 0) {
                    L1PcInstance.this.set_PKcount(L1PcInstance.this
                            .get_PKcount() - 1);
                }
                L1PcInstance.this.setLastPk(null);
            }

            if (lastAttacker instanceof L1GuardianInstance) {
                if (L1PcInstance.this.getPkCountForElf() > 0) {
                    L1PcInstance.this.setPkCountForElf(L1PcInstance.this
                            .getPkCountForElf() - 1);
                }
                L1PcInstance.this.setLastPkForElf(null);
            }

            // 检查攻击者是否为PC(宠物 定义为主人)
            L1PcInstance fightPc = null;

            if (lastAttacker instanceof L1PcInstance) {// 攻击者是玩家
                fightPc = (L1PcInstance) lastAttacker;

            } else if (lastAttacker instanceof L1PetInstance) {// 攻击者是宠物
                final L1PetInstance npc = (L1PetInstance) lastAttacker;
                if (npc.getMaster() != null) {
                    fightPc = (L1PcInstance) npc.getMaster();
                }

            } else if (lastAttacker instanceof L1SummonInstance) {// 攻击者是 召换兽
                final L1SummonInstance npc = (L1SummonInstance) lastAttacker;
                if (npc.getMaster() != null) {
                    fightPc = (L1PcInstance) npc.getMaster();
                }

            } else if (lastAttacker instanceof L1IllusoryInstance) {// 攻击者是 分身
                final L1IllusoryInstance npc = (L1IllusoryInstance) lastAttacker;
                if (npc.getMaster() != null) {
                    fightPc = (L1PcInstance) npc.getMaster();
                }

            } else if (lastAttacker instanceof L1EffectInstance) {// 攻击者是 技能物件
                final L1EffectInstance npc = (L1EffectInstance) lastAttacker;
                if (npc.getMaster() != null) {
                    fightPc = (L1PcInstance) npc.getMaster();
                }
            }

            L1PcInstance.this.sendPacketsAll(new S_DoActionGFX(targetobjid,
                    ActionCodes.ACTION_Die));
            final boolean castle_area = L1CastleLocation.checkInAllWarArea(
                    getX(), getY(), getMapId());// 战争旗中
            if (fightPc != null) {
            	
                // 战斗区中 并且不在战争旗中
                if (isCombatZone && !castle_area && getMapId() != 70) {//修改遗忘岛PK掉经验
                	return;
                }
                // 决斗中
                if ((L1PcInstance.this.getFightId() == fightPc.getId())
                        && (fightPc.getFightId() == L1PcInstance.this.getId())) {
                    L1PcInstance.this.setFightId(0);
                    L1PcInstance.this.sendPackets(new S_PacketBox(
                            S_PacketBox.MSG_DUEL, 0, 0));
                    fightPc.setFightId(0);
                    fightPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL,
                            0, 0));
                    return;
                }

                // 效果: 被超过10级以上的玩家攻击而死亡时，不会失去经验值，也不会掉落物品
                if (isEncounter()) {// 遭遇的守护
                    if (fightPc.getLevel() > getLevel()) {
                        if ((fightPc.getLevel() - getLevel()) >= 10) {
                            return;
                        }
                    }
                }               

                // 攻城战争进行状态
                if (L1PcInstance.this.castleWarResult()) {
                    isWar = true;
                }

                // 血盟战争进行状态
                if (L1PcInstance.this.simWarResult(lastAttacker)) {
                    isWar = true;
                }

                // 攻城战进行状态
                if (L1PcInstance.this.isInWarAreaAndWarTime(L1PcInstance.this,
                        fightPc)) {
                    isWar = true;
                }

                // 死亡公告
                if (L1PcInstance.this.getLevel() >= ConfigKill.KILLLEVEL) {
                    if (!fightPc.isGm()) {
                        boolean isShow = false;// 是否公告
                        if (isWar) {// 战争中
                            isShow = true;

                        } else {// 非战争中
                            // 非战斗区
                            if (!isCombatZone || getMapId() == 70) {//修改遗忘岛PK掉经验 hjx1000
                                isShow = true;
                            }
                        }
                        if (isShow) {
                            // 杀人公告
                            World.get().broadcastPacketToAll(
                                    new S_KillMessage(fightPc.getName(),
                                            L1PcInstance.this.getName()));
                            fightPc.get_other().add_killCount(1);
                            L1PcInstance.this.get_other().add_deathCount(1);
                        }
                    }
                }
            }

            // 安全区中
            if (isSafetyZone) {
                return;
            }
            // 战斗区中
//            if (isCombatZone) {
//                return;
//            }
            // 死亡逞罚
            if (!L1PcInstance.this.getMap().isEnabledDeathPenalty()) {
                return;
            }

//            final boolean castle_area = L1CastleLocation.checkInAllWarArea(
//                    getX(), getY(), getMapId());
            if (castle_area && !ConfigOther.CITY_WAR_LOSS) {// 战争旗中
                return;
            }

            // 正义质未满
            if (L1PcInstance.this.getLawful() < 30000) { //由 32767 更改为 30000 - hjx1000
                // 物品掉落判断
                this.lostRate();

                // 技能掉落的判断
                this.lostSkillRate();
            }

            // 经验值掉落的判断
            this.expRate(fightPc);

            // 参战中
            if (isWar) {
                return;
            }

            if (fightPc != null) {
                if (fightPc.getClan() != null && getClan() != null) {
                    if (WorldWar.get().isWar(fightPc.getClan().getClanName(),
                            getClan().getClanName())) {
                        return;
                    }
                }
                if (fightPc.isSafetyZone()) {
                    return;
                }
                if (fightPc.isCombatZone()) {
                    return;
                }
                if ((L1PcInstance.this.getLawful() >= 0)
                        && (L1PcInstance.this.isPinkName() == false)) {
                    boolean isChangePkCount = false;
                    // boolean isChangePkCountForElf = false;
                    // アライメントが30000未满の场合はPKカウント增加
                    if (fightPc.getLawful() < 30000) {
                        fightPc.set_PKcount(fightPc.get_PKcount() + 1);
                        isChangePkCount = true;
                        if (fightPc.isElf() && L1PcInstance.this.isElf()) {
                            fightPc.setPkCountForElf(fightPc.getPkCountForElf() + 1);
                            // isChangePkCountForElf = true;
                        }
                    }
                    fightPc.setLastPk();
                    if (fightPc.isElf() && L1PcInstance.this.isElf()) {
                        fightPc.setLastPkForElf();
                    }

                    // アライメント处理
                    // 公式の発表および各LVでのPKからつじつまの合うように变更
                    // （PK侧のLVに依存し、高LVほどリスクも高い）
                    // 48あたりで-8kほど DKの时点で10k强
                    // 60で约20k强 65で30k弱
                    int lawful;

                    if (fightPc.getLevel() < 50) {
                        // lawful = -1 * (int) ((Math.pow(fightPc.getLevel(), 2)
                        // * 4));
                        lawful = -1
                                * (((int) Math.pow(fightPc.getLevel(), 2)) << 2);

                    } else {
                        lawful = -1
                                * (int) ((Math.pow(fightPc.getLevel(), 3) * 0.08));
                    }
                    // もし(元々のアライメント-1000)が计算后より低い场合
                    // 元々のアライメント-1000をアライメント值とする
                    // （连续でPKしたときにほとんど值が变わらなかった记忆より）
                    // これは上の式よりも自信度が低いうろ觉えですので
                    // 明らかにこうならない！という场合は修正お愿いします
                    if ((fightPc.getLawful() - 1000) < lawful) {
                        lawful = fightPc.getLawful() - 1000;
                    }

                    if (lawful <= -32768) {
                        lawful = -32768;
                    }
                    fightPc.setLawful(lawful);
                    add_friends(fightPc); //将杀死自己的人添加到好友列表 hjx1000
                    fightPc.sendPacketsAll(new S_Lawful(fightPc));

                    if (ConfigAlt.ALT_PUNISHMENT) {
                        if (isChangePkCount && (fightPc.get_PKcount() >= 5)
                                && (fightPc.get_PKcount() < 100)) {
                            // あなたのPK回数が%0になりました。回数が%1になると地狱行きです。
                            fightPc.sendPackets(new S_BlueMessage(551, String
                                    .valueOf(fightPc.get_PKcount()), "100"));

                        } else if (isChangePkCount
                                && (fightPc.get_PKcount() >= 100)) {
                            fightPc.beginHell(true);
                        }
                    }

                } else {
                    setPinkName(false);
                }
            }

            /*
             * if (PcDeleteList.get(L1PcInstance.this) == null) {
             * PcDeleteList.put(L1PcInstance.this);// 5M }
             */
        }
        
        /**
         * 将杀死自己的人添加到好友列表 hjx1000
         */
        private void add_friends(final L1PcInstance fightPc) {
            // 取回该人物好友清单
            final ArrayList<L1BuddyTmp> list = BuddyReading.get().userBuddy(
            		L1PcInstance.this.getId());

            if (list != null) {
            	if (list.size() >= 50) { //好友列表已满
            		return;
            	}
                for (final L1BuddyTmp buddyTmp : list) { //好友已存在
                    if (fightPc.getName().equalsIgnoreCase(buddyTmp.get_buddy_name())) {
                        return;
                    }
                }
            }

            int objid = CharObjidTable.get().charObjid(fightPc.getName());
            if (objid != 0) {
                String name = CharObjidTable.get().isChar(objid);
                BuddyReading.get().addBuddy(L1PcInstance.this.getId(), objid, name);
                return;
            }
        }

        /**
         * <FONT COLOR="#0000ff">经验值掉落判断</FONT>
         */
        private void expRate(final L1PcInstance fightPc) { //修改天使药水 PK时无效 hjx1000
            if (fightPc != null) {
            	final L1ItemInstance itemEQ = getInventory().checkEquippedItem(31010);
            	if (itemEQ != null) {
            		getInventory().deleteItem(itemEQ);
            		sendPackets(new S_ServerMessage(638, itemEQ.getLogName()));
            		CreateNewItem.createNewItem(fightPc, 44070, 2);
            	}
            } else {
                final L1ItemInstance item1 = getInventory().checkItemX(44060, 1);
                if (item1 != null) {
                    getInventory().removeItem(item1, 1);// 删除1个药水
                    sendPackets(new S_ServerMessage("\\fU你身上带有" + item1.getName()
                            + "，刚刚死掉没有损失经验值！"));
                    return;
                }
            }

            deathPenalty(); // 经验质逞罚

            setGresValid(true); // EXPロストしたらG-RES有效

            if (getExpRes() == 0) {
                setExpRes(1);
            }

            onChangeExp();
        }

        /**
         * <FONT COLOR="#0000ff">物品掉落判断</FONT>
         */
        private void lostRate() {
            final L1ItemInstance item2 = L1PcInstance.this.getInventory()
                    .checkItemX(44061, 1);
            if (item2 != null) {
                L1PcInstance.this.getInventory().removeItem(item2, 1);// 删除1个
//                if (fightPc != null) {
//                	CreateNewItem.createNewItem(fightPc, 44070, 2);
//                }
                sendPackets(new S_ServerMessage("\\fU你身上带有" + item2.getName()
                        + "，刚刚死掉没有喷装！"));
                return;
            }

            // 产生物品掉落机率
            // 正义质32000以上0%、每-1000增加0.4%
            // 正义质小于0 每-1000增加0.8%
            // 正义质-32000以下 最高51.2%掉落率
            int lostRate = ((int) ((L1PcInstance.this.getLawful() + 32768D) / 1000D - 65D)) << 2;

            if (lostRate < 0) {
                lostRate *= -1;
                if (L1PcInstance.this.getLawful() < 0) {
                    // lostRate *= 2;
                    lostRate = lostRate << 1;
                }
                final int rnd = _random.nextInt(1000) + 1;
                if (rnd <= lostRate) {
                    int count = 0;
                    int lawful = L1PcInstance.this.getLawful();
                    if (lawful <= -32768) {// 小于-30000掉落1~5件
                        count = _random.nextInt(5) + 1;

                    } else if (lawful > -32768 && lawful <= -30000) {// 小于-30000掉落1~3件
                        count = _random.nextInt(4) + 1;

                    } else if (lawful > -30000 && lawful <= -20000) {// 小于-20000掉落1~3件
                        count = _random.nextInt(3) + 1;

                    } else if (lawful > -20000 && lawful <= -10000) {// 小于-10000掉落1~2件
                        count = _random.nextInt(2) + 1;

                    } else if (lawful > -10000 && lawful <= -0) {// 小于0掉落1件
                        count = _random.nextInt(1) + 1;
                    }

                    if (count > 0) {
                        L1PcInstance.this.caoPenaltyResult(count);
                    }
                }
            }
        }

        /**
         * <FONT COLOR="#0000ff">死亡技能遗失判断</FONT>
         */
        private void lostSkillRate() {
            // 人物拥有技能数量
            int skillCount = _skillList.size();

            // 技能数量大于0
            if (skillCount > 0) {
                // 预计掉落技能数量
                int count = 0;
                // 人物正义质
                int lawful = getLawful();

                // 引用随机质 0 ~ 199
                int random = _random.nextInt(200);

                if (lawful <= -32768) {
                    count = _random.nextInt(4) + 1;// 随机质 小于 技能数量

                } else if (lawful > -32768 && lawful <= -30000) {
                    if (random <= (skillCount + 1)) {
                        count = _random.nextInt(3) + 1;// 随机质 小于 技能数量
                    }

                } else if (lawful > -30000 && lawful <= -20000) {
                    if (random <= ((skillCount >> 1) + 1)) {// 随机质 小于 (技能数量 / 2)
                        count = _random.nextInt(2) + 1;
                    }

                } else if (lawful > -20000 && lawful <= -10000) {
                    if (random <= ((skillCount >> 2) + 1)) {// 随机质 小于 (技能数量 / 4)
                        count = 1;
                    }
                }

                if (count > 0) {
                    delSkill(count);
                }
            }
        }
    }

    /**
     * <FONT COLOR="#0000ff">死亡掉落物品</FONT>
     * 
     * @param count
     *            掉落数量
     */
    private void caoPenaltyResult(final int count) {
        for (int i = 0; i < count; i++) {
            final L1ItemInstance item = getInventory().caoPenalty();
            if (item != null) {
                item.set_showId(get_showId());

                final int x = getX();
                final int y = getY();
                final short m = getMapId();
                if (item.getBless() > 100) {
                    getInventory().deleteItem(item); //封印装备掉落直接删除 hjx1000
                } else {
                    _log.info("人物:" + this.getName() + "死亡掉落物品" + item.getItem().getName()
                            + " 物品OBJID:" + item.getId()); //死亡掉落物品也做记录 hjx1000
                    getInventory().tradeItem(item,
                            item.isStackable() ? item.getCount() : 1,// 物件不可堆叠 数量:1
                                                                     // 可堆叠 数量:全部
                            World.get().getInventory(x, y, m));
                    // 638 您损失了 %0。
                }
                sendPackets(new S_ServerMessage(638, item.getLogName()));
                World.get().broadcastPacketToAll(new S_BoxMessage("玩家: " + getName() + " 死亡掉落物品 " + item.getLogName()));
            } 
        }
    }

    /**
     * <FONT COLOR="#0000ff">死亡技能遗失</FONT>
     * 
     * @param count
     *            掉落数量
     */
    private void delSkill(final int count) {
        for (int i = 0; i < count; i++) {
            // 随机取得 INDEX 位置点
            int index = _random.nextInt(this._skillList.size());
            // 取回随机位置点技能编号
            Integer skillid = _skillList.get(index);

            if (this._skillList.remove(skillid)) {
                this.sendPackets(new S_DelSkill(this, skillid));
                CharSkillReading.get().spellLost(this.getId(), skillid);
            }
        }
    }

    /**
     * <FONT COLOR="#0000ff">复活移出死亡清单</FONT>
     */
    public void stopPcDeleteTimer() {
        this.setDead(false);
        // 加入死亡清单
        set_delete_time(0);
    }

    /**
     * <FONT COLOR="#0000ff">是否在参加攻城战中</FONT>
     * 
     * @return true:是 false:不是
     */
    public boolean castleWarResult() {
        if ((this.getClanid() != 0) && this.isCrown()) { // 具有血盟的王族
            final L1Clan clan = WorldClan.get().getClan(this.getClanname());
            if (clan.getCastleId() == 0) {
                // 取回全部战争清单
                for (final L1War war : WorldWar.get().getWarList()) {
                    final int warType = war.getWarType();
                    final boolean isInWar = war.checkClanInWar(this
                            .getClanname());
                    final boolean isAttackClan = war.checkAttackClan(this
                            .getClanname());
                    if ((this.getId() == clan.getLeaderId()) && // 攻城战中 攻击方盟主死亡
                                                                // 退出战争
                            (warType == 1) && isInWar && isAttackClan) {
                        final String enemyClanName = war.getEnemyClanName(this
                                .getClanname());
                        if (enemyClanName != null) {
                            war.ceaseWar(this.getClanname(), enemyClanName); // 结束
                        }
                        break;
                    }
                }
            }
        }

        int castleId = 0;
        boolean isNowWar = false;
        castleId = L1CastleLocation.getCastleIdByArea(this);
        if (castleId != 0) { // 战争范围旗帜内城堡ID
            isNowWar = ServerWarExecutor.get().isNowWar(castleId);
        }
        return isNowWar;
    }

    /**
     * <FONT COLOR="#0000ff">是否在参加血盟战争中</FONT>
     * 
     * @param lastAttacker
     * @return true:是 false:不是
     */
    public boolean simWarResult(final L1Character lastAttacker) {
        if (this.getClanid() == 0) { // クラン所属していない
            return false;
        }

        L1PcInstance attacker = null;
        String enemyClanName = null;
        boolean sameWar = false;

        // 判断主要攻击者
        if (lastAttacker instanceof L1PcInstance) {// 攻击者是玩家
            attacker = (L1PcInstance) lastAttacker;

        } else if (lastAttacker instanceof L1PetInstance) {// 攻击者是宠物
            attacker = (L1PcInstance) ((L1PetInstance) lastAttacker)
                    .getMaster();

        } else if (lastAttacker instanceof L1SummonInstance) {// 攻击者是 召换兽
            attacker = (L1PcInstance) ((L1SummonInstance) lastAttacker)
                    .getMaster();

        } else if (lastAttacker instanceof L1IllusoryInstance) {// 攻击者是 分身
            attacker = (L1PcInstance) ((L1IllusoryInstance) lastAttacker)
                    .getMaster();

        } else if (lastAttacker instanceof L1EffectInstance) {// 攻击者是 技能物件(火牢)
            attacker = (L1PcInstance) ((L1EffectInstance) lastAttacker)
                    .getMaster();

        } else {
            return false;
        }

        // 取回全部战争清单
        for (final L1War war : WorldWar.get().getWarList()) {
            final L1Clan clan = WorldClan.get().getClan(this.getClanname());

            final int warType = war.getWarType();
            final boolean isInWar = war.checkClanInWar(this.getClanname());
            if ((attacker != null) && (attacker.getClanid() != 0)) { // lastAttackerがPC、サモン、ペットでクラン所属中
                sameWar = war.checkClanInSameWar(this.getClanname(),
                        attacker.getClanname());
            }

            if ((this.getId() == clan.getLeaderId()) && // 血盟主で模拟战中
                    (warType == 2) && (isInWar == true)) {
                enemyClanName = war.getEnemyClanName(this.getClanname());
                if (enemyClanName != null) {
                    war.ceaseWar(this.getClanname(), enemyClanName); // 结束
                }
            }

            if ((warType == 2) && sameWar) {// 模拟战で同じ战争に参加中の场合、ペナルティなし
                return true;
            }
        }
        return false;
    }

    /**
     * 经验质恢复
     */
    public void resExp() {
        final int oldLevel = this.getLevel();
        final long needExp = ExpTable.getNeedExpNextLevel(oldLevel);
        long exp = 0;
        switch (oldLevel) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
                exp = (long) (needExp * 0.05);
                break;

            case 45:
                exp = (long) (needExp * 0.045);
                break;

            case 46:
                exp = (long) (needExp * 0.04);
                break;

            case 47:
                exp = (long) (needExp * 0.035);
                break;

            case 48:
                exp = (long) (needExp * 0.03);
                break;

            case 49:
                exp = (long) (needExp * 0.025);
                break;

            default:
                exp = (long) (needExp * 0.025);
                break;
        }

        if (exp == 0) {
            return;
        }
        this.addExp(exp);
    }

    /**
     * 经验质逞罚
     * 
     * @return
     */
    private long deathPenalty() {
        final int oldLevel = this.getLevel();
        final long needExp = ExpTable.getNeedExpNextLevel(oldLevel);
        long exp = 0;
        switch (oldLevel) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
                exp = 0;
                break;
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
                exp = (long) (needExp * 0.1);
                break;

            case 45:
                exp = (long) (needExp * 0.09);
                break;

            case 46:
                exp = (long) (needExp * 0.08);
                break;

            case 47:
                exp = (long) (needExp * 0.07);
                break;

            case 48:
                exp = (long) (needExp * 0.06);
                break;

            case 49:
                exp = (long) (needExp * 0.05);
                break;

            default:
                exp = (long) (needExp * 0.05);
                break;
        }

        if (exp == 0) {
            return 0;
        }
        this.addExp(-exp);
        return exp;
    }

    private int _originalEr = 0; // ● オリジナルDEX ER补正

    public int getOriginalEr() {

        return this._originalEr;
    }

    public int getEr() {
        if (this.hasSkillEffect(STRIKER_GALE)) {
            return 0;
        }

        int er = 0;
        if (this.isKnight()) {
            er = this.getLevel() >> 2;// /4 // ナイト

        } else if (this.isCrown() || this.isElf()) {
            er = this.getLevel() >> 3;// / 8; // 君主?エルフ

        } else if (this.isDarkelf()) {
            er = this.getLevel() / 6; // ダークエルフ

        } else if (this.isWizard()) {
            er = this.getLevel() / 10; // ウィザード

        } else if (this.isDragonKnight()) {
            er = this.getLevel() / 7; // ドラゴンナイト

        } else if (this.isIllusionist()) {
            er = this.getLevel() / 9; // イリュージョニスト
        }

        er += (this.getDex() - 8) >> 1;// / 2;

        er += this.getOriginalEr();

        if (this.hasSkillEffect(DRESS_EVASION)) {// 闪避提升
            er += 12;
        }

        if (this.hasSkillEffect(SOLID_CARRIAGE)) {// 坚固防护
        	if (this.getInventory().getTypeEquipped(2, 7) >= 1) {//修正坚固防护必须使用盾牌才有效果 hjx1000
        		er += 15;
        	}
        }

        if (this.hasSkillEffect(ADLV80_1)) {// 卡瑞的祝福(地龙副本)
            er += 30;
        }

        if (this.hasSkillEffect(ADLV80_2)) {// 莎尔的祝福(水龙副本)
            er += 15;
        }
        return er;
    }

    /**
     * 使用的武器
     * 
     * @return
     */
    public L1ItemInstance getWeapon() {
        return this._weapon;
    }

    /**
     * 使用的武器
     * 
     * @param weapon
     */
    public void setWeapon(final L1ItemInstance weapon) {
        this._weapon = weapon;
    }

    /**
     * 传回任务状态类
     * 
     * @return
     */
    public L1PcQuest getQuest() {
        return this._quest;
    }

    /**
     * 传回选单命令执行类
     * 
     * @return
     */
    public L1ActionPc getAction() {
        return this._action;
    }

    /**
     * 传回宠物选单命令执行类
     * 
     * @return
     */
    public L1ActionPet getActionPet() {
        return this._actionPet;
    }

    /**
     * 传回召唤兽选单命令执行类
     * 
     * @return
     */
    public L1ActionSummon getActionSummon() {
        return this._actionSummon;
    }

    /**
     * 王族
     * 
     * @return
     */
    public boolean isCrown() {
        return ((this.getClassId() == CLASSID_PRINCE) || (this.getClassId() == CLASSID_PRINCESS));
    }

    /**
     * 骑士
     * 
     * @return
     */
    public boolean isKnight() {
        return ((this.getClassId() == CLASSID_KNIGHT_MALE) || (this
                .getClassId() == CLASSID_KNIGHT_FEMALE));
    }

    /**
     * 精灵
     * 
     * @return
     */
    public boolean isElf() {
        return ((this.getClassId() == CLASSID_ELF_MALE) || (this.getClassId() == CLASSID_ELF_FEMALE));
    }

    /**
     * 法师
     * 
     * @return
     */
    public boolean isWizard() {
        return ((this.getClassId() == CLASSID_WIZARD_MALE) || (this
                .getClassId() == CLASSID_WIZARD_FEMALE));
    }

    /**
     * 黑暗精灵
     * 
     * @return
     */
    public boolean isDarkelf() {
        return ((this.getClassId() == CLASSID_DARK_ELF_MALE) || (this
                .getClassId() == CLASSID_DARK_ELF_FEMALE));
    }

    /**
     * 龙骑士
     * 
     * @return
     */
    public boolean isDragonKnight() {
        return ((this.getClassId() == CLASSID_DRAGON_KNIGHT_MALE) || (this
                .getClassId() == CLASSID_DRAGON_KNIGHT_FEMALE));
    }

    /**
     * 幻术师
     * 
     * @return
     */
    public boolean isIllusionist() {
        return ((this.getClassId() == CLASSID_ILLUSIONIST_MALE) || (this
                .getClassId() == CLASSID_ILLUSIONIST_FEMALE));
    }

    private ClientExecutor _netConnection = null;
    private int _classId;
    private int _type;
    private long _exp;
    private final L1Karma _karma = new L1Karma();
    private boolean _gm;
    private boolean _monitor;
    private boolean _gmInvis;
    private short _accessLevel;
    private int _currentWeapon;
    private final L1PcInventory _inventory;
    private final L1DwarfInventory _dwarf;
    private final L1DwarfForElfInventory _dwarfForElf;
    private final L1Inventory _tradewindow;
    private L1ItemInstance _weapon;
    private L1Party _party;
    private L1ChatParty _chatParty;
    private int _partyID;
    private int _tradeID;
    private boolean _tradeOk;
    private int _tempID;
    private boolean _isTeleport = false;
    private boolean _isDrink = false;
    private boolean _isGres = false;
    private boolean _isPinkName = false;
    private final L1PcQuest _quest;
    private final L1ActionPc _action;
    private final L1ActionPet _actionPet;
    private final L1ActionSummon _actionSummon;

    private final L1EquipmentSlot _equipSlot;

    private String _accountName; // ● アカウントネーム

    public String getAccountName() {
        return this._accountName;
    }

    public void setAccountName(final String s) {
        this._accountName = s;
    }

    private short _baseMaxHp = 0; // ● ＭＡＸＨＰベース（1～32767）

    /**
     * 基础HP
     * 
     * @return
     */
    public short getBaseMaxHp() {
        return this._baseMaxHp;
    }

    /**
     * 基础HP
     * 
     * @param i
     */
    public void addBaseMaxHp(short i) {
        i += this._baseMaxHp;
        if (i >= 32767) {
            i = 32767;

        } else if (i < 1) {
            i = 1;
        }
        this.addMaxHp(i - this._baseMaxHp);
        this._baseMaxHp = i;
    }

    private short _baseMaxMp = 0; // ● ＭＡＸＭＰベース（0～32767）

    /**
     * 基础MP
     * 
     * @return
     */
    public short getBaseMaxMp() {
        return this._baseMaxMp;
    }

    /**
     * 基础MP
     * 
     * @param i
     */
    public void addBaseMaxMp(short i) {
        i += this._baseMaxMp;
        if (i >= 32767) {
            i = 32767;

        } else if (i < 1) {
            i = 1;
        }
        this.addMaxMp(i - this._baseMaxMp);
        this._baseMaxMp = i;
    }

    private int _baseAc = 0; // ● ＡＣベース（-128～127）

    public int getBaseAc() {
        return this._baseAc;
    }

    private int _originalAc = 0; // ● オリジナルDEX ＡＣ补正

    public int getOriginalAc() {
        return this._originalAc;
    }

    private int _baseStr = 0; // ● ＳＴＲベース（1～127）

    /**
     * 原始力量(内含素质提升/万能药)
     * 
     * @return
     */
    public int getBaseStr() {
        return this._baseStr;
    }

    /**
     * 原始力量(内含素质提升/万能药)
     * 
     * @param i
     */
    public void addBaseStr(int i) {
        i += this._baseStr;
        if (i >= 254) {
            i = 254;

        } else if (i < 1) {
            i = 1;
        }
        this.addStr((i - this._baseStr));
        this._baseStr = i;
    }

    private int _baseCon = 0; // ● ＣＯＮベース（1～127）

    /**
     * 原始体质(内含素质提升/万能药)
     * 
     * @return
     */
    public int getBaseCon() {
        return this._baseCon;
    }

    /**
     * 原始体质(内含素质提升/万能药)
     * 
     * @param i
     */
    public void addBaseCon(int i) {
        i += this._baseCon;
        if (i >= 254) {
            i = 254;

        } else if (i < 1) {
            i = 1;
        }
        this.addCon((i - this._baseCon));
        this._baseCon = i;
    }

    private int _baseDex = 0; // ● ＤＥＸベース（1～127）

    /**
     * 原始敏捷(内含素质提升/万能药)
     * 
     * @return
     */
    public int getBaseDex() {
        return this._baseDex;
    }

    /**
     * 原始敏捷(内含素质提升/万能药)
     * 
     * @param i
     */
    public void addBaseDex(int i) {
        i += this._baseDex;
        if (i >= 254) {
            i = 254;

        } else if (i < 1) {
            i = 1;
        }
        this.addDex((i - this._baseDex));
        this._baseDex = i;
    }

    private int _baseCha = 0; // ● ＣＨＡベース（1～127）

    /**
     * 原始魅力(内含素质提升/万能药)
     * 
     * @return
     */
    public int getBaseCha() {
        return this._baseCha;
    }

    /**
     * 原始魅力(内含素质提升/万能药)
     * 
     * @param i
     */
    public void addBaseCha(int i) {
        i += this._baseCha;
        if (i >= 254) {
            i = 254;

        } else if (i < 1) {
            i = 1;
        }
        this.addCha((i - this._baseCha));
        this._baseCha = i;
    }

    private int _baseInt = 0; // ● ＩＮＴベース（1～127）

    /**
     * 原始智力(内含素质提升/万能药)
     * 
     * @return
     */
    public int getBaseInt() {
        return this._baseInt;
    }

    /**
     * 原始智力(内含素质提升/万能药)
     * 
     * @param i
     */
    public void addBaseInt(int i) {
        i += this._baseInt;
        if (i >= 254) {
            i = 254;

        } else if (i < 1) {
            i = 1;
        }
        this.addInt((i - this._baseInt));
        this._baseInt = i;
    }

    private int _baseWis = 0; // ● ＷＩＳベース（1～127）

    /**
     * 原始精神(内含素质提升/万能药)
     * 
     * @return
     */
    public int getBaseWis() {
        return this._baseWis;
    }

    /**
     * 原始精神(内含素质提升/万能药)
     * 
     * @param i
     */
    public void addBaseWis(int i) {
        i += this._baseWis;
        if (i >= 254) {
            i = 254;

        } else if (i < 1) {
            i = 1;
        }
        this.addWis((i - this._baseWis));
        this._baseWis = i;
    }

    // //////////////////////////////////////////////////////////////////////////////////////

    private int _originalStr = 0; // ● オリジナル STR

    /**
     * 原始力量(人物出生)
     * 
     * @return
     */
    public int getOriginalStr() {
        return this._originalStr;
    }

    /**
     * 原始力量(人物出生)
     * 
     * @param i
     */
    public void setOriginalStr(final int i) {
        this._originalStr = i;
    }

    private int _originalCon = 0; // ● オリジナル CON

    /**
     * 原始体质(人物出生)
     * 
     * @return
     */
    public int getOriginalCon() {
        return this._originalCon;
    }

    /**
     * 原始体质(人物出生)
     * 
     * @param i
     */
    public void setOriginalCon(final int i) {
        this._originalCon = i;
    }

    private int _originalDex = 0; // ● オリジナル DEX

    /**
     * 原始敏捷(人物出生)
     * 
     * @return
     */
    public int getOriginalDex() {
        return this._originalDex;
    }

    /**
     * 原始敏捷(人物出生)
     * 
     * @param i
     */
    public void setOriginalDex(final int i) {
        this._originalDex = i;
    }

    private int _originalCha = 0; // ● オリジナル CHA

    /**
     * 原始魅力(人物出生)
     * 
     * @return
     */
    public int getOriginalCha() {
        return this._originalCha;
    }

    /**
     * 原始魅力(人物出生)
     * 
     * @param i
     */
    public void setOriginalCha(final int i) {
        this._originalCha = i;
    }

    private int _originalInt = 0; // ● オリジナル INT

    /**
     * 原始智力(人物出生)
     * 
     * @return
     */
    public int getOriginalInt() {
        return this._originalInt;
    }

    /**
     * 原始智力(人物出生)
     * 
     * @param i
     */
    public void setOriginalInt(final int i) {
        this._originalInt = i;
    }

    private int _originalWis = 0; // ● オリジナル WIS

    /**
     * 原始精神(人物出生)
     * 
     * @return
     */
    public int getOriginalWis() {
        return this._originalWis;
    }

    /**
     * 原始精神(人物出生)
     * 
     * @param i
     */
    public void setOriginalWis(final int i) {
        this._originalWis = i;
    }

    private int _originalDmgup = 0; // ● オリジナルSTR ダメージ补正

    public int getOriginalDmgup() {
        return this._originalDmgup;
    }

    private int _originalBowDmgup = 0; // ● オリジナルDEX 弓ダメージ补正

    public int getOriginalBowDmgup() {
        return this._originalBowDmgup;
    }

    private int _originalHitup = 0; // ● オリジナルSTR 命中补正

    public int getOriginalHitup() {
        return this._originalHitup;
    }

    private int _originalBowHitup = 0; // ● オリジナルDEX 命中补正

    public int getOriginalBowHitup() {
        return this._originalHitup + this._originalBowHitup;
    }

    private int _originalMr = 0; // ● オリジナルWIS 魔法防御

    public int getOriginalMr() {
        return this._originalMr;
    }

    private int _originalMagicHit = 0; // ● オリジナルINT 魔法命中

    /**
     * 智力(依职业)附加魔法命中
     * 
     * @return
     */
    public int getOriginalMagicHit() {
        return this._originalMagicHit;
    }

    private int _originalMagicCritical = 0; // ● オリジナルINT 魔法クリティカル

    public int getOriginalMagicCritical() {
        return this._originalMagicCritical;
    }

    private int _originalMagicConsumeReduction = 0; // ● オリジナルINT 消费MP轻减

    public int getOriginalMagicConsumeReduction() {
        return this._originalMagicConsumeReduction;
    }

    private int _originalMagicDamage = 0; // ● オリジナルINT 魔法ダメージ

    /**
     * 魔攻
     * 
     * @return
     */
    public int getOriginalMagicDamage() {
        return this._originalMagicDamage;
    }

    private int _originalHpup = 0; // ● オリジナルCON HP上升值补正

    /**
     * 体质 HP上升值补正
     * 
     * @return
     */
    public int getOriginalHpup() {
        return this._originalHpup;
    }

    private int _originalMpup = 0; // ● オリジナルWIS MP上升值补正

    /**
     * 精神 MP上升值补正
     * 
     * @return
     */
    public int getOriginalMpup() {
        return this._originalMpup;
    }

    private int _baseDmgup = 0; // ● ダメージ补正ベース（-128～127）

    public int getBaseDmgup() {
        return this._baseDmgup;
    }

    private int _baseBowDmgup = 0; // ● 弓ダメージ补正ベース（-128～127）

    public int getBaseBowDmgup() {
        return this._baseBowDmgup;
    }

    private int _baseHitup = 0; // ● 命中补正ベース（-128～127）

    /**
     * 命中补正
     * 
     * @return
     */
    public int getBaseHitup() {
        return this._baseHitup;
    }

    private int _baseBowHitup = 0; // ● 弓命中补正ベース（-128～127）

    /**
     * 弓命中补正
     * 
     * @return
     */
    public int getBaseBowHitup() {
        return this._baseBowHitup;
    }

    private int _baseMr = 0; // ● 魔法防御ベース（0～）

    /**
     * 魔法防御
     * 
     * @return
     */
    public int getBaseMr() {
        return this._baseMr;
    }

    private int _advenHp; // 暂时增加的HP

    /**
     * 暂时增加的HP
     * 
     * @return
     */
    public int getAdvenHp() {
        return this._advenHp;
    }

    /**
     * 暂时增加的HP
     * 
     * @param i
     */
    public void setAdvenHp(final int i) {
        this._advenHp = i;
    }

    private int _advenMp; // 暂时增加的MP

    /**
     * 暂时增加的MP
     * 
     * @return
     */
    public int getAdvenMp() {
        return this._advenMp;
    }

    /**
     * 暂时增加的MP
     * 
     * @param i
     */
    public void setAdvenMp(final int i) {
        this._advenMp = i;
    }

    private int _highLevel; // ● 过去最高レベル

    public int getHighLevel() {
        return this._highLevel;
    }

    public void setHighLevel(final int i) {
        this._highLevel = i;
    }

    private int _bonusStats; // 升级点数使用次数

    /**
     * 升级点数使用次数
     * 
     * @return
     */
    public int getBonusStats() {
        return this._bonusStats;
    }

    /**
     * 设置升级点数使用次数
     * 
     * @param i
     */
    public void setBonusStats(final int i) {
        this._bonusStats = i;
    }

    private int _elixirStats; // 万能药使用次数

    /**
     * 万能药使用次数
     * 
     * @return
     */
    public int getElixirStats() {
        return this._elixirStats;
    }

    /**
     * 设置万能药使用次数
     * 
     * @param i
     */
    public void setElixirStats(final int i) {
        this._elixirStats = i;
    }

    private int _elfAttr; // ● エルフの属性

    /**
     * 精灵属性
     * 
     * @return
     */
    public int getElfAttr() {
        return this._elfAttr;
    }

    public void setElfAttr(final int i) {
        this._elfAttr = i;
    }

    private int _expRes; // ● EXP复旧

    public int getExpRes() {
        return this._expRes;
    }

    public void setExpRes(final int i) {
        this._expRes = i;
    }

    private int _partnerId; // ● 结婚相手

    public int getPartnerId() {
        return this._partnerId;
    }

    public void setPartnerId(final int i) {
        _partnerId = i;
    }

    private int _onlineStatus; // 人物连线状态

    /**
     * 人物连线状态
     * 
     * @return
     */
    public int getOnlineStatus() {
        return _onlineStatus;
    }

    /**
     * 设置人物连线状态
     * 
     * @param i
     */
    public void setOnlineStatus(final int i) {
        _onlineStatus = i;
    }

    private int _homeTownId; // ● ホームタウン

    public int getHomeTownId() {
        return _homeTownId;
    }

    public void setHomeTownId(final int i) {
        _homeTownId = i;
    }

    private int _contribution; // 贡献度

    /**
     * 贡献度
     * 
     * @return
     */
    public int getContribution() {
        return this._contribution;
    }

    /**
     * 贡献度
     * 
     * @param i
     */
    public void setContribution(final int i) {
        this._contribution = i;
    }

    private int _hellTime;// 地狱滞留时间

    /**
     * 地狱滞留时间
     * 
     * @return
     */
    public int getHellTime() {
        return this._hellTime;
    }

    /**
     * 地狱滞留时间
     * 
     * @param i
     */
    public void setHellTime(final int i) {
        this._hellTime = i;
    }

    private boolean _banned; // ● 冻结

    public boolean isBanned() {
        return this._banned;
    }

    public void setBanned(final boolean flag) {
        this._banned = flag;
    }

    private int _food; // ● 满腹度

    public int get_food() {
        return _food;
    }

    public void set_food(int i) {
        if (i > 225) {
            i = 225;
        }
        _food = i;
        if (_food == 225) {// LOLI 生存呐喊
            final Calendar cal = Calendar.getInstance();
            long h_time = cal.getTimeInMillis() / 1000;// 换算为秒
            set_h_time(h_time);// 纪录吃饱时间

        } else {
            set_h_time(-1);// 纪录吃饱时间
        }
    }

    public L1EquipmentSlot getEquipSlot() {
        return this._equipSlot;
    }

    /**
     * 加载指定PC资料
     * 
     * @param charName
     *            PC名称
     * @return
     */
    public static L1PcInstance load(final String charName) {
        L1PcInstance result = null;
        try {
            result = CharacterTable.get().loadCharacter(charName);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    /**
     * 人物资料存档
     * 
     * @throws Exception
     */
    public void save() throws Exception {
        if (isGhost()) {
            return;
        }

        if (isInCharReset()) {
            return;
        }

        // 其它事件纪录
        if (_other != null) {
            CharOtherReading.get().storeOther(getId(), _other);
        }

        CharacterTable.get().storeCharacter(this);
    }

    /**
     * 背包资料存档
     */
    public void saveInventory() {
        for (final L1ItemInstance item : getInventory().getItems()) {
            getInventory().saveItem(item, item.getRecordingColumns());
        }
    }

    public double getMaxWeight() {
        final int str = getStr();
        final int con = getCon();
        double maxWeight = (150 * (Math.floor(0.6 * str + 0.4 * con + 1)))
                * get_weightUP();

        double weightReductionByArmor = getWeightReduction(); // 减重设置
        weightReductionByArmor /= 100;

        int weightReductionByMagic = 0;
        if (hasSkillEffect(DECREASE_WEIGHT)) { // ディクリースウェイト
            weightReductionByMagic = 180;
        }

        double originalWeightReduction = 0; // オリジナルステータスによる重量轻减
        originalWeightReduction += 0.04 * (getOriginalStrWeightReduction() + getOriginalConWeightReduction());

        final double weightReduction = 1 + weightReductionByArmor
                + originalWeightReduction;

        maxWeight *= weightReduction;

        maxWeight += weightReductionByMagic;

        maxWeight *= ConfigRate.RATE_WEIGHT_LIMIT; // 服务器提高设置

        return maxWeight;
    }

	/**
	 *
	 * 神聖疾走效果
	 * 行走加速效果
	 * 風之疾走效果
	 * 
	 * 
	 * @return
	 */
	public boolean isFastMovable() {
		return (this.hasSkillEffect(HOLY_WALK)
				|| this.hasSkillEffect(MOVING_ACCELERATION)
				|| this.hasSkillEffect(WIND_WALK)
				//|| this.hasSkillEffect(BLOODLUST)//add 血之渴望 hjx1000
				/*|| this.hasSkillEffect(STATUS_RIBRAVE)*/);
	}

    /**
     * 
     * 生命之樹果實效果
     * @return
     */
    public boolean isFastAttackable() {
        return this.hasSkillEffect(STATUS_RIBRAVE);
    }

    /**
     * 勇敢药水效果
     * 
     * @return
     */
    public boolean isBrave() {
        return this.hasSkillEffect(STATUS_BRAVE);
    }

    /**
     * 精灵饼干效果
     * 
     * @return
     */
    public boolean isElfBrave() {
        return this.hasSkillEffect(STATUS_ELFBRAVE);
    }

    /**
     * 巧克力蛋糕效果
     * 
     * @return
     */
    public boolean isBraveX() {
        return this.hasSkillEffect(STATUS_BRAVE3);
    }

    /**
     * 加速效果
     * 
     * @return
     */
    public boolean isHaste() {
        return (this.hasSkillEffect(STATUS_HASTE) || this.hasSkillEffect(HASTE)
                || this.hasSkillEffect(GREATER_HASTE) || (this.getMoveSpeed() == 1));
    }

    private int invisDelayCounter = 0;

    public boolean isInvisDelay() {
        return (this.invisDelayCounter > 0);
    }

    private final Object _invisTimerMonitor = new Object();

    public void addInvisDelayCounter(final int counter) {
        synchronized (this._invisTimerMonitor) {
            this.invisDelayCounter += counter;
        }
    }

    private static final long DELAY_INVIS = 3000L;

    /**
     * 启用隐身时间轴设置
     */
    public void beginInvisTimer() {
        this.addInvisDelayCounter(1);
        GeneralThreadPool.get().pcSchedule(new L1PcInvisDelay(this.getId()),
                DELAY_INVIS);
    }

    public synchronized void addExp(final long exp) {
        final long newexp = _exp + exp;
        _exp = newexp;
        if (this.getMapId() >= 5167 && this.getMapId() <= 5168) {//恶魔领地检测 hjx1000
        	if (this.get_other().get_usemapTime() <= 0) {
        		this.getNetConnection().kick();
        	}
        }
    }

    /**
     * 增加贡献度
     * 
     * @param contribution
     */
    public synchronized void addContribution(final int contribution) {
        _contribution += contribution;
    }

    /**
     * 等级提升的判断
     * 
     * @param gap
     */
    private void levelUp(final int gap) {
        resetLevel();
        for (int i = 0; i < gap; i++) {
            final short randomHp = CalcStat.calcStatHp(getType(),
                    getBaseMaxHp(), getBaseCon(), getOriginalHpup());
            final short randomMp = CalcStat.calcStatMp(getType(),
                    getBaseMaxMp(), getBaseWis(), getOriginalMpup());
            addBaseMaxHp(randomHp);
            addBaseMaxMp(randomMp);
        }

        resetBaseHitup();
        resetBaseDmgup();
        resetBaseAc();
        resetBaseMr();
        if (getLevel() > getHighLevel()) {
            setHighLevel(getLevel());
        }

        setCurrentHp(getMaxHp());
        setCurrentMp(getMaxMp());

        try {
            // 人物资料存档
            save();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            // 更新人物资讯
            sendPackets(new S_OwnCharStatus(this));

            // 地图等级限制判断
            MapLevelTable.get().get_level(getMapId(), this);
            showWindows();
        }
    }

    /**
     * 判断是否展示视窗<BR>
     * 能力质/任务
     */
    public void showWindows() {
        // 任务/副本系统启动
        if (QuestSet.ISQUEST) {
            // 判断是否出现任务提示视窗
            int quest = QuestTable.get().levelQuest(this, this.getLevel());
            if (quest > 0) {
                // 展示任务室窗
                isWindows();

            } else {
                // 判断是否出现能力选取视窗
                if (power()) {
                    this.sendPackets(new S_Bonusstats(this.getId()));
                }
            }

        } else {
            // 判断是否出现能力选取视窗
            if (power()) {
                this.sendPackets(new S_Bonusstats(this.getId()));
            }
        }
    }

    /**
     * 展示任务室窗
     */
    public void isWindows() {
        // 判断是否出现能力选取视窗
        if (power()) {// 是
            this.sendPackets(new S_NPCTalkReturn(this.getId(), "y_qs_10"));

        } else {// 不是
            this.sendPackets(new S_NPCTalkReturn(this.getId(), "y_qs_00"));
        }
    }

    /**
     * 判断是否出现能力选取视窗
     * 
     * @return
     */
    public boolean power() {
        if (this.getLevel() >= 51) {
            if (this.getLevel() - 50 > this.getBonusStats()) {
                int power = getBaseStr() + getBaseDex() + getBaseCon()
                        + getBaseInt() + getBaseWis() + getBaseCha();
                if (power < ConfigAlt.POWER * 6) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 等级下降
     * 
     * @param gap
     */
    private void levelDown(final int gap) {
        this.resetLevel();

        for (int i = 0; i > gap; i--) {
            // レベルダウン时はランダム值をそのままマイナスする为に、base值に0を设定
            final short randomHp = CalcStat.calcStatHp(this.getType(), 0,
                    this.getBaseCon(), this.getOriginalHpup());
            final short randomMp = CalcStat.calcStatMp(this.getType(), 0,
                    this.getBaseWis(), this.getOriginalMpup());
            this.addBaseMaxHp((short) -randomHp);
            this.addBaseMaxMp((short) -randomMp);
        }

        if (this.getLevel() == 1) {
            final int initHp = CalcInitHpMp.calcInitHp(this);
            final int initMp = CalcInitHpMp.calcInitMp(this);
            this.addBaseMaxHp((short) -this.getBaseMaxHp());
            this.addBaseMaxHp((short) initHp);
            this.setCurrentHp((short) initHp);
            this.addBaseMaxMp((short) -this.getBaseMaxMp());
            this.addBaseMaxMp((short) initMp);
            this.setCurrentMp((short) initMp);
        }

        this.resetBaseHitup();
        this.resetBaseDmgup();
        this.resetBaseAc();
        this.resetBaseMr();

        try {
            // 存入资料
            this.save();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            // 更新人物资讯
            sendPackets(new S_OwnCharStatus(this));

            // 地图等级限制判断
            MapLevelTable.get().get_level(getMapId(), this);
        }
    }

    private boolean _ghost = false; // 鬼魂状态

    /**
     * 鬼魂状态
     * 
     * @return
     */
    public boolean isGhost() {
        return this._ghost;
    }

    /**
     * 设置鬼魂状态
     * 
     * @param flag
     */
    private void setGhost(final boolean flag) {
        this._ghost = flag;
    }

    private int _ghostTime = -1; // 鬼魂状态时间

    /**
     * 鬼魂状态时间
     * 
     * @return
     */
    public int get_ghostTime() {
        return this._ghostTime;
    }

    /**
     * 设置鬼魂状态时间
     * 
     * @param ghostTime
     */
    public void set_ghostTime(final int ghostTime) {
        this._ghostTime = ghostTime;
    }

    private boolean _ghostCanTalk = true; // 鬼魂状态NPC对话允许

    /**
     * 鬼魂状态NPC对话允许
     * 
     * @return
     */
    public boolean isGhostCanTalk() {
        return this._ghostCanTalk;
    }

    /**
     * 设置鬼魂状态NPC对话允许
     * 
     * @param flag
     */
    private void setGhostCanTalk(final boolean flag) {
        this._ghostCanTalk = flag;
    }

    private boolean _isReserveGhost = false; // 准备鬼魂状态解除

    /**
     * 准备鬼魂状态解除
     * 
     * @return
     */
    public boolean isReserveGhost() {
        return this._isReserveGhost;
    }

    /**
     * 准备鬼魂状态解除
     * 
     * @param flag
     */
    private void setReserveGhost(final boolean flag) {
        this._isReserveGhost = flag;
    }

    /**
     * 鬼魂模式传送
     * 
     * @param locx
     * @param locy
     * @param mapid
     * @param canTalk
     */
    public void beginGhost(final int locx, final int locy, final short mapid,
            final boolean canTalk) {
        this.beginGhost(locx, locy, mapid, canTalk, 0);
    }

    /**
     * 鬼魂模式传送
     * 
     * @param locx
     * @param locy
     * @param mapid
     * @param canTalk
     * @param sec
     */
    public void beginGhost(final int locx, final int locy, final short mapid,
            final boolean canTalk, final int sec) {
        if (this.isGhost()) {
            return;
        }
        this.setGhost(true);
        this._ghostSaveLocX = this.getX();
        this._ghostSaveLocY = this.getY();
        this._ghostSaveMapId = this.getMapId();
        this._ghostSaveHeading = this.getHeading();
        this.setGhostCanTalk(canTalk);
        L1Teleport.teleport(this, locx, locy, mapid, 5, true);
        if (sec > 0) {
            this.set_ghostTime(sec);
        }
    }

    /**
     * 离开鬼魂模式(传送回出发点)
     */
    public void makeReadyEndGhost() {
        this.setReserveGhost(true);
        L1Teleport.teleport(this, this._ghostSaveLocX, this._ghostSaveLocY,
                this._ghostSaveMapId, this._ghostSaveHeading, true);
    }

    /**
     * 结束鬼魂模式
     */
    public void endGhost() {
        this.set_ghostTime(-1);
        this.setGhost(false);
        this.setGhostCanTalk(true);
        this.setReserveGhost(false);
    }

    private int _ghostSaveLocX = 0;
    private int _ghostSaveLocY = 0;
    private short _ghostSaveMapId = 0;
    private int _ghostSaveHeading = 0;

    /**
     * 地狱以外に居るときは地狱へ强制移动
     * 
     * @param isFirst
     */
    public void beginHell(final boolean isFirst) {
        // 地狱以外に居るときは地狱へ强制移动
        if (this.getMapId() != 666) {
            final int locx = 32701;
            final int locy = 32777;
            final short mapid = 666;
            L1Teleport.teleport(this, locx, locy, mapid, 5, false);
        }

        if (isFirst) {
            if (this.get_PKcount() <= 10) {
                this.setHellTime(300);

            } else {
                this.setHellTime(300 * (this.get_PKcount() - 10) + 300);
            }
            // 552 因为你已经杀了 %0 人所以被打入地狱。 你将在这里停留 %1 分钟。
            this.sendPackets(new S_BlueMessage(552, String.valueOf(this
                    .get_PKcount()), String.valueOf(this.getHellTime() / 60)));

        } else {
            // 637 你必须在此地停留 %0 秒。
            this.sendPackets(new S_BlueMessage(637, String.valueOf(this
                    .getHellTime())));
        }
    }

    /**
     * 地狱时间终止
     */
    public void endHell() {
        // 地狱时间终止 返回然柳村
        final int[] loc = L1TownLocation
                .getGetBackLoc(L1TownLocation.TOWNID_ORCISH_FOREST);
        L1Teleport.teleport(this, loc[0], loc[1], (short) loc[2], 5, true);

        try {
            this.save();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void setPoisonEffect(final int effectId) {
        this.sendPackets(new S_Poison(this.getId(), effectId));

        if (!this.isGmInvis() && !this.isGhost() && !this.isInvisble()) {
            this.broadcastPacketAll(new S_Poison(this.getId(), effectId));
        }
    }

    @Override
    public void healHp(final int pt) {
        super.healHp(pt);

        this.sendPackets(new S_HPUpdate(this));
    }

    @Override
    public int getKarma() {
        return this._karma.get();
    }

    @Override
    public void setKarma(final int i) {
        this._karma.set(i);
    }

    public void addKarma(final int i) {
        synchronized (this._karma) {
            this._karma.add(i);
        }
    }

    public int getKarmaLevel() {
        return this._karma.getLevel();
    }

    public int getKarmaPercent() {
        return this._karma.getPercent();
    }

    private Timestamp _lastPk;

    /**
     * プレイヤーの最终PK时间を返す。
     * 
     * @return _lastPk
     * 
     */
    public Timestamp getLastPk() {
        return this._lastPk;
    }

    /**
     * プレイヤーの最终PK时间を设定する。
     * 
     * @param time
     *            最终PK时间（Timestamp型） 解除する场合はnullを代入
     */
    public void setLastPk(final Timestamp time) {
        this._lastPk = time;
    }

    /**
     * プレイヤーの最终PK时间を现在の时刻に设定する。
     */
    public void setLastPk() {
        this._lastPk = new Timestamp(System.currentTimeMillis());
    }

    /**
     * プレイヤーが手配中であるかを返す。
     * 
     * @return 手配中であれば、true
     */
    public boolean isWanted() {
        if (this._lastPk == null) {
            return false;

            // 距离PK时间超过1小时
        } else if (System.currentTimeMillis() - this._lastPk.getTime() > 1 * 3600 * 1000) {
            this.setLastPk(null);
            return false;
        }
        return true;
    }

    private Timestamp _lastPkForElf;

    public Timestamp getLastPkForElf() {
        return this._lastPkForElf;
    }

    public void setLastPkForElf(final Timestamp time) {
        this._lastPkForElf = time;
    }

    public void setLastPkForElf() {
        this._lastPkForElf = new Timestamp(System.currentTimeMillis());
    }

    public boolean isWantedForElf() {
        if (this._lastPkForElf == null) {
            return false;

        } else if (System.currentTimeMillis() - this._lastPkForElf.getTime() > 24 * 3600 * 1000) {
            this.setLastPkForElf(null);
            return false;
        }
        return true;
    }

    private Timestamp _deleteTime; // キャラクター削除までの时间

    public Timestamp getDeleteTime() {
        return this._deleteTime;
    }

    public void setDeleteTime(final Timestamp time) {
        this._deleteTime = time;
    }

    /**
     * 职业魔法等级
     */
    @Override
    public int getMagicLevel() {
        return this.getClassFeature().getMagicLevel(this.getLevel());
    }

    private double _weightUP = 1.0D;// 负重提高%

    /**
     * 负重提高%
     * 
     * @return
     */
    public double get_weightUP() {
        return _weightUP;
    }

    /**
     * 负重提高%
     * 
     * @param i
     */
    public void add_weightUP(final int i) {
        _weightUP += (i / 100D);
    }

    private int _weightReduction = 0;// 减重

    /**
     * 减重
     * 
     * @return
     */
    public int getWeightReduction() {
        return this._weightReduction;
    }

    /**
     * 减重
     * 
     * @param i
     */
    public void addWeightReduction(final int i) {
        this._weightReduction += i;
    }

    private int _originalStrWeightReduction = 0; // ● オリジナルSTR 重量轻减

    public int getOriginalStrWeightReduction() {
        return this._originalStrWeightReduction;
    }

    private int _originalConWeightReduction = 0; // ● オリジナルCON 重量轻减

    public int getOriginalConWeightReduction() {
        return this._originalConWeightReduction;
    }

    private int _hasteItemEquipped = 0;// 装备有加速能力装备(装备数量)

    /**
     * 装备有加速能力装备(装备数量)
     * 
     * @return
     */
    public int getHasteItemEquipped() {
        return this._hasteItemEquipped;
    }

    /**
     * 装备有加速能力装备(装备数量)
     * 
     * @param i
     */
    public void addHasteItemEquipped(final int i) {
        this._hasteItemEquipped += i;
    }

    public void removeHasteSkillEffect() {
        if (this.hasSkillEffect(SLOW)) {
            this.removeSkillEffect(SLOW);
        }

        if (this.hasSkillEffect(MASS_SLOW)) {
            this.removeSkillEffect(MASS_SLOW);
        }

        if (this.hasSkillEffect(ENTANGLE)) {
            this.removeSkillEffect(ENTANGLE);
        }

        if (this.hasSkillEffect(HASTE)) {
            this.removeSkillEffect(HASTE);
        }

        if (this.hasSkillEffect(GREATER_HASTE)) {
            this.removeSkillEffect(GREATER_HASTE);
        }

        if (this.hasSkillEffect(STATUS_HASTE)) {
            this.removeSkillEffect(STATUS_HASTE);
        }
    }

    private int _damageReductionByArmor = 0; // 防具增加伤害减免

    public int getDamageReductionByArmor() {
//        int damageReduction = 0;
//        if (_damageReductionByArmor > 10) {
//            damageReduction = 10 + (_random
//                    .nextInt((_damageReductionByArmor - 10)) + 1);
//
//        } else {
//            damageReduction = _damageReductionByArmor;
//        }
//        return damageReduction;
    	//修正减伤不正确 hjx1000
    	return this._damageReductionByArmor;
    }

    public void addDamageReductionByArmor(final int i) {
        this._damageReductionByArmor += i;
    }

    private int _hitModifierByArmor = 0; // 防具增加物理命中

    public int getHitModifierByArmor() {
        return this._hitModifierByArmor;
    }

    public void addHitModifierByArmor(final int i) {
        this._hitModifierByArmor += i;
    }

    private int _dmgModifierByArmor = 0; // 防具增加物理伤害

    public int getDmgModifierByArmor() {
        return this._dmgModifierByArmor;
    }

    public void addDmgModifierByArmor(final int i) {
        this._dmgModifierByArmor += i;
    }

    private int _bowHitModifierByArmor = 0; // 防具增加远距离物理命中

    public int getBowHitModifierByArmor() {
        return this._bowHitModifierByArmor;
    }

    public void addBowHitModifierByArmor(final int i) {
        this._bowHitModifierByArmor += i;
    }

    private int _bowDmgModifierByArmor = 0; // 防具增加远距离物理伤害

    public int getBowDmgModifierByArmor() {
        return this._bowDmgModifierByArmor;
    }

    public void addBowDmgModifierByArmor(final int i) {
        this._bowDmgModifierByArmor += i;
    }

    private boolean _gresValid; // G-RESが有效か

    private void setGresValid(final boolean valid) {
        this._gresValid = valid;
    }

    public boolean isGresValid() {
        return this._gresValid;
    }

    private boolean _isFishing = false;// 钓鱼状态

    /**
     * 钓鱼状态
     * 
     * @return
     */
    public boolean isFishing() {
        return this._isFishing;
    }

    private int _fishX = -1;

    public int get_fishX() {
        return _fishX;
    }

    private int _fishY = -1;

    public int get_fishY() {
        return _fishY;
    }

    /**
     * 钓鱼状态
     * 
     * @param flag
     * @param fishY
     * @param fishX
     */
    public void setFishing(final boolean flag, int fishX, int fishY) {
        this._isFishing = flag;
        _fishX = fishX;
        _fishY = fishY;
    }

    private int _cookingId = 0;

    public int getCookingId() {
        return this._cookingId;
    }

    public void setCookingId(final int i) {
        this._cookingId = i;
    }

    private int _dessertId = 0;

    public int getDessertId() {
        return this._dessertId;
    }

    public void setDessertId(final int i) {
        this._dessertId = i;
    }

    /**
     * LVによる命中ボーナスを设定する LVが变动した场合などに呼び出せば再计算される
     * 
     * @return
     */
    public void resetBaseDmgup() {
        int newBaseDmgup = 0;
        int newBaseBowDmgup = 0;
        if (this.isKnight() || this.isDarkelf() || this.isDragonKnight()) { // ナイト、ダークエルフ、ドラゴンナイト
            newBaseDmgup = this.getLevel() / 10;
            newBaseBowDmgup = 0;

        } else if (this.isElf()) { // エルフ
            newBaseDmgup = 0;
            newBaseBowDmgup = this.getLevel() / 10;
        }
        this.addDmgup(newBaseDmgup - this._baseDmgup);
        this.addBowDmgup(newBaseBowDmgup - this._baseBowDmgup);
        this._baseDmgup = newBaseDmgup;
        this._baseBowDmgup = newBaseBowDmgup;
    }

    /**
     * LVによる命中ボーナスを设定する LVが变动した场合などに呼び出せば再计算される
     * 
     * @return
     */
    public void resetBaseHitup() {
        int newBaseHitup = 0;
        int newBaseBowHitup = 0;
        if (this.isCrown()) { // プリ
            newBaseHitup = this.getLevel() / 5;
            newBaseBowHitup = this.getLevel() / 5;

        } else if (this.isKnight()) { // ナイト
            newBaseHitup = this.getLevel() / 3;
            newBaseBowHitup = this.getLevel() / 3;

        } else if (this.isElf()) { // エルフ
            newBaseHitup = this.getLevel() / 5;
            newBaseBowHitup = this.getLevel() / 5;

        } else if (this.isDarkelf()) { // ダークエルフ
            newBaseHitup = this.getLevel() / 3;
            newBaseBowHitup = this.getLevel() / 3;

        } else if (this.isDragonKnight()) { // ドラゴンナイト
            newBaseHitup = this.getLevel() / 3;
            newBaseBowHitup = this.getLevel() / 3;

        } else if (this.isIllusionist()) { // イリュージョニスト
            newBaseHitup = this.getLevel() / 5;
            newBaseBowHitup = this.getLevel() / 5;
        }

        this.addHitup(newBaseHitup - this._baseHitup);
        this.addBowHitup(newBaseBowHitup - this._baseBowHitup);
        this._baseHitup = newBaseHitup;
        this._baseBowHitup = newBaseBowHitup;
    }

    /**
     * キャラクターステータスからACを再计算して设定する 初期设定时、LVUP,LVDown时などに呼び出す
     */
    public void resetBaseAc() {
        final int newAc = CalcStat.calcAc(this.getLevel(), this.getBaseDex());
        this.addAc(newAc - this._baseAc);
        this._baseAc = newAc;
    }

    /**
     * キャラクターステータスから素のMRを再计算して设定する 初期设定时、スキル使用时やLVUP,LVDown时に呼び出す
     */
    public void resetBaseMr() {
        int newMr = 0;
        if (this.isCrown()) { // プリ
            newMr = 10;

        } else if (this.isElf()) { // エルフ
            newMr = 25;

        } else if (this.isWizard()) { // ウィザード
            newMr = 15;

        } else if (this.isDarkelf()) { // ダークエルフ
            newMr = 10;

        } else if (this.isDragonKnight()) { // ドラゴンナイト
            newMr = 18;

        } else if (this.isIllusionist()) { // イリュージョニスト
            newMr = 20;
        }
        newMr += CalcStat.calcStatMr(this.getWis()); // WIS分のMRボーナス
        newMr += this.getLevel() / 2; // LVの半分だけ追加
        this.addMr(newMr - this._baseMr);
        this._baseMr = newMr;
    }

    /**
     * 重新设置等级为目前经验质所属
     */
    public void resetLevel() {
        this.setLevel(ExpTable.getLevelByExp(this._exp));
    }

    /**
     * 初期ステータスから现在のボーナスを再计算して设定する 初期设定时、再配分时に呼び出す
     */
    public void resetOriginalHpup() {
        this._originalHpup = L1PcOriginal.resetOriginalHpup(this);
    }

    public void resetOriginalMpup() {
        this._originalMpup = L1PcOriginal.resetOriginalMpup(this);
    }

    public void resetOriginalStrWeightReduction() {
        this._originalStrWeightReduction = L1PcOriginal
                .resetOriginalStrWeightReduction(this);
    }

    public void resetOriginalDmgup() {
        this._originalDmgup = L1PcOriginal.resetOriginalDmgup(this);
    }

    public void resetOriginalConWeightReduction() {
        this._originalConWeightReduction = L1PcOriginal
                .resetOriginalConWeightReduction(this);
    }

    public void resetOriginalBowDmgup() {
        this._originalBowDmgup = L1PcOriginal.resetOriginalBowDmgup(this);
    }

    public void resetOriginalHitup() {
        this._originalHitup = L1PcOriginal.resetOriginalHitup(this);
    }

    public void resetOriginalBowHitup() {
        this._originalBowHitup = L1PcOriginal.resetOriginalBowHitup(this);
    }

    public void resetOriginalMr() {
        this._originalMr = L1PcOriginal.resetOriginalMr(this);
        this.addMr(this._originalMr);
    }

    public void resetOriginalMagicHit() {
        this._originalMagicHit = L1PcOriginal.resetOriginalMagicHit(this);
    }

    public void resetOriginalMagicCritical() {
        this._originalMagicCritical = L1PcOriginal
                .resetOriginalMagicCritical(this);
    }

    public void resetOriginalMagicConsumeReduction() {
        this._originalMagicConsumeReduction = L1PcOriginal
                .resetOriginalMagicConsumeReduction(this);
    }

    public void resetOriginalMagicDamage() {
        this._originalMagicDamage = L1PcOriginal.resetOriginalMagicDamage(this);
    }

    public void resetOriginalAc() {
        this._originalAc = L1PcOriginal.resetOriginalAc(this);
        // System.out.println("_originalAc:"+_originalAc);
        this.addAc(0 - this._originalAc);
    }

    public void resetOriginalEr() {
        this._originalEr = L1PcOriginal.resetOriginalEr(this);
    }

    public void resetOriginalHpr() {
        this._originalHpr = L1PcOriginal.resetOriginalHpr(this);
    }

    public void resetOriginalMpr() {
        this._originalMpr = L1PcOriginal.resetOriginalMpr(this);
    }

    /**
     * 全属性重置
     */
    public void refresh() {
        this.resetLevel();
        this.resetBaseHitup();
        this.resetBaseDmgup();
        this.resetBaseMr();
        this.resetBaseAc();
        this.resetOriginalHpup();
        this.resetOriginalMpup();
        this.resetOriginalDmgup();
        this.resetOriginalBowDmgup();
        this.resetOriginalHitup();
        this.resetOriginalBowHitup();
        this.resetOriginalMr();
        this.resetOriginalMagicHit();
        this.resetOriginalMagicCritical();
        this.resetOriginalMagicConsumeReduction();
        this.resetOriginalMagicDamage();
        this.resetOriginalAc();
        this.resetOriginalEr();
        this.resetOriginalHpr();
        this.resetOriginalMpr();
        this.resetOriginalStrWeightReduction();
        this.resetOriginalConWeightReduction();
    }

    // 人物讯息拒绝清单
    private final L1ExcludingList _excludingList = new L1ExcludingList();

    /**
     * 人物讯息拒绝清单
     * 
     * @return
     */
    public L1ExcludingList getExcludingList() {
        return this._excludingList;
    }

    private int _teleportX = 0;// 传送目的座标X

    /**
     * 传送目的座标X
     * 
     * @return
     */
    public int getTeleportX() {
        return this._teleportX;
    }

    /**
     * 传送目的座标X
     * 
     * @param i
     */
    public void setTeleportX(final int i) {
        this._teleportX = i;
    }

    private int _teleportY = 0;// 传送目的座标Y

    /**
     * 传送目的座标Y
     * 
     * @return
     */
    public int getTeleportY() {
        return this._teleportY;
    }

    /**
     * 传送目的座标Y
     * 
     * @param i
     */
    public void setTeleportY(final int i) {
        this._teleportY = i;
    }

    private short _teleportMapId = 0;// 传送目的座标MAP

    /**
     * 传送目的座标MAP
     * 
     * @return
     */
    public short getTeleportMapId() {
        return this._teleportMapId;
    }

    /**
     * 传送目的座标MAP
     * 
     * @param i
     */
    public void setTeleportMapId(final short i) {
        this._teleportMapId = i;
    }

    private int _teleportHeading = 0;// 传送后面向

    /**
     * 传送后面向
     * 
     * @return
     */
    public int getTeleportHeading() {
        return this._teleportHeading;
    }

    /**
     * 传送后面向
     * 
     * @param i
     */
    public void setTeleportHeading(final int i) {
        this._teleportHeading = i;
    }

    private int _tempCharGfxAtDead;// 死亡时外型代号

    /**
     * 死亡时外型代号
     * 
     * @return
     */
    public int getTempCharGfxAtDead() {
        return this._tempCharGfxAtDead;
    }

    /**
     * 死亡时外型代号
     * 
     * @param i
     */
    private void setTempCharGfxAtDead(final int i) {
        this._tempCharGfxAtDead = i;
    }

    private boolean _isCanWhisper = true;// 全秘密语(收听)

    /**
     * 全秘密语(收听)
     * 
     * @return flag true:接收 false:拒绝
     */
    public boolean isCanWhisper() {
        return this._isCanWhisper;
    }

    /**
     * 全秘密语(收听)
     * 
     * @param flag
     *            flag true:接收 false:拒绝
     */
    public void setCanWhisper(final boolean flag) {
        this._isCanWhisper = flag;
    }

    private boolean _isShowTradeChat = true;// 买卖频道(收听)

    /**
     * 买卖频道(收听)
     * 
     * @return flag true:接收 false:拒绝
     */
    public boolean isShowTradeChat() {
        return this._isShowTradeChat;
    }

    /**
     * 买卖频道(收听)
     * 
     * @param flag
     *            true:接收 false:拒绝
     */
    public void setShowTradeChat(final boolean flag) {
        this._isShowTradeChat = flag;
    }

    private boolean _isShowWorldChat = true;// 全体聊天(收听)

    /**
     * 全体聊天(收听)
     * 
     * @return flag true:接收 false:拒绝
     */
    public boolean isShowWorldChat() {
        return this._isShowWorldChat;
    }

    /**
     * 全体聊天(收听)
     * 
     * @param flag
     *            flag true:接收 false:拒绝
     */
    public void setShowWorldChat(final boolean flag) {
        this._isShowWorldChat = flag;
    }

    private int _fightId;// 决斗对象OBJID

    /**
     * 决斗对象OBJID
     * 
     * @return
     */
    public int getFightId() {
        return this._fightId;
    }

    /**
     * 决斗对象OBJID
     * 
     * @param i
     */
    public void setFightId(final int i) {
        this._fightId = i;
    }

    private byte _chatCount = 0;// 对话检查次数

    private long _oldChatTimeInMillis = 0L;// 对话检查毫秒差

    /**
     * 对话检查(洗画面)
     */
    public void checkChatInterval() {
        final long nowChatTimeInMillis = System.currentTimeMillis();
        if (this._chatCount == 0) {
            this._chatCount++;
            this._oldChatTimeInMillis = nowChatTimeInMillis;
            return;
        }

        final long chatInterval = nowChatTimeInMillis
                - this._oldChatTimeInMillis;
        // 时间差异2秒以上
        if (chatInterval > 2000) {
            this._chatCount = 0;
            this._oldChatTimeInMillis = 0;

        } else {
            if (this._chatCount >= 5) {
                this.setSkillEffect(STATUS_CHAT_PROHIBITED, 120 * 1000);
                this.sendPackets(new S_PacketBox(S_PacketBox.ICON_CHATBAN, 120));
                // \f3因洗画面的关系，2分钟之内无法聊天。
                this.sendPackets(new S_ServerMessage(153));
                this._chatCount = 0;
                this._oldChatTimeInMillis = 0;
            }
            this._chatCount++;
        }
    }

    private int _callClanId;// 呼唤盟友(对象OBJID)

    /**
     * 传回呼唤盟友(对象OBJID)
     * 
     * @return
     */
    public int getCallClanId() {
        return this._callClanId;
    }

    /**
     * 设置呼唤盟友(对象OBJID)
     * 
     * @param i
     */
    public void setCallClanId(final int i) {
        this._callClanId = i;
    }

    private int _callClanHeading;// 设置呼唤盟友(自己的面向)

    /**
     * 设置呼唤盟友(自己的面向)
     * 
     * @return
     */
    public int getCallClanHeading() {
        return this._callClanHeading;
    }

    /**
     * 传回呼唤盟友(自己的面向)
     * 
     * @return
     */
    public void setCallClanHeading(final int i) {
        this._callClanHeading = i;
    }

    private boolean _isInCharReset = false;// 执行人物重设状态

    /**
     * 传回执行人物重设状态
     * 
     * @return
     */
    public boolean isInCharReset() {
        return this._isInCharReset;
    }

    /**
     * 设置执行人物重设状态
     * 
     * @param flag
     */
    public void setInCharReset(final boolean flag) {
        this._isInCharReset = flag;
    }

    private int _tempLevel = 1;// 人物重置等级暂存(最低)

    /**
     * 人物重置等级暂存(最低)
     * 
     * @return
     */
    public int getTempLevel() {
        return this._tempLevel;
    }

    /**
     * 人物重置等级暂存(最低)
     * 
     * @param i
     */
    public void setTempLevel(final int i) {
        this._tempLevel = i;
    }

    private int _tempMaxLevel = 1;// 人物重置等级暂存(最高)

    /**
     * 人物重置等级暂存(最高)
     * 
     * @return
     */
    public int getTempMaxLevel() {
        return this._tempMaxLevel;
    }

    /**
     * 人物重置等级暂存(最高)
     * 
     * @param i
     */
    public void setTempMaxLevel(final int i) {
        this._tempMaxLevel = i;
    }

    private boolean _isSummonMonster = false;// 是否展开召唤控制选单

    /**
     * 设置是否展开召唤控制选单
     * 
     * @param SummonMonster
     */
    public void setSummonMonster(final boolean SummonMonster) {
        this._isSummonMonster = SummonMonster;
    }

    /**
     * 是否展开召唤控制选单
     * 
     * @return
     */
    public boolean isSummonMonster() {
        return this._isSummonMonster;
    }

    private boolean _isShapeChange = false;// 是否展开变身控制选单

    /**
     * 设置是否展开变身控制选单
     * 
     * @param isShapeChange
     */
    public void setShapeChange(final boolean isShapeChange) {
        this._isShapeChange = isShapeChange;
    }

    /**
     * 是否展开变身控制选单
     * 
     * @return
     */
    public boolean isShapeChange() {
        return this._isShapeChange;
    }

    private String _text;// 暂存文字串

    /**
     * 设置暂存文字串(收件者)
     * 
     * @param text
     */
    public void setText(final String text) {
        this._text = text;
    }

    /**
     * 传回暂存文字串(收件者)
     * 
     * @return
     */
    public String getText() {
        return this._text;
    }

    private byte[] _textByte = null;// 暂存byte[]阵列

    /**
     * 设定暂存byte[]阵列
     * 
     * @param textByte
     */
    public void setTextByte(final byte[] textByte) {
        this._textByte = textByte;
    }

    /**
     * 传回暂存byte[]阵列
     * 
     * @return
     */
    public byte[] getTextByte() {
        return this._textByte;
    }

    private L1PcOther _other;// 额外纪录资料

    /**
     * 额外纪录资料
     * 
     * @param other
     */
    public void set_other(final L1PcOther other) {
        this._other = other;
    }

    /**
     * 额外纪录资料
     * 
     * @return
     */
    public L1PcOther get_other() {
        return this._other;
    }

    private L1PcOtherList _otherList;// 额外清单纪录资料

    /**
     * 额外清单纪录资料
     * 
     * @param other
     */
    public void set_otherList(final L1PcOtherList other) {
        _otherList = other;
    }

    /**
     * 额外清单纪录资料
     * 
     * @return
     */
    public L1PcOtherList get_otherList() {
        return _otherList;
    }

    private int _oleLocX;// 移动前座标暂存X

    /**
     * 移动前座标暂存X
     * 
     * @param oleLocx
     */
    public void setOleLocX(final int oleLocx) {
        this._oleLocX = oleLocx;
    }

    /**
     * 移动前座标暂存X
     * 
     * @return
     */
    public int getOleLocX() {
        return this._oleLocX;
    }

    private int _oleLocY;// 移动前座标暂存Y

    /**
     * 移动前座标暂存Y
     * 
     * @param oleLocy
     */
    public void setOleLocY(final int oleLocy) {
        this._oleLocY = oleLocy;
    }

    /**
     * 移动前座标暂存Y
     * 
     * @return
     */
    public int getOleLocY() {
        return this._oleLocY;
    }

    private L1Character _target = null;
    

    /**
     * 设置目前攻击对象
     * 
     * @param target
     */
    public void setNowTarget(final L1Character target) {
        this._target = target;
    }

    /**
     * 传回目前攻击对象
     */
    public L1Character getNowTarget() {
        return this._target;
    }

    private int _dmgDown = 0;

    /**
     * 副助道具伤害减免
     * 
     * @param dmgDown
     */
    public void set_dmgDown(int dmgDown) {
        _dmgDown = dmgDown;
    }

    /**
     * 副助道具伤害减免
     * 
     * @return
     */
    public int get_dmgDown() {
        return _dmgDown;
    }

    /**
     * 保存宠物目前模式
     * 
     * @param pc
     */
    public void setPetModel() {
        try {
            // 宠物的跟随移动
            for (final L1NpcInstance petNpc : getPetList().values()) {
                if (petNpc != null) {
                    if (petNpc instanceof L1SummonInstance) { // 召唤兽的跟随移动
                        final L1SummonInstance summon = (L1SummonInstance) petNpc;
                        summon.set_tempModel();

                    } else if (petNpc instanceof L1PetInstance) { // 宠物的跟随移动
                        final L1PetInstance pet = (L1PetInstance) petNpc;
                        pet.set_tempModel();
                    }
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 恢复宠物目前模式
     * 
     * @param pc
     */
    public void getPetModel() {
        try {
            // 宠物的跟随移动
            for (final L1NpcInstance petNpc : getPetList().values()) {
                if (petNpc != null) {
                    if (petNpc instanceof L1SummonInstance) { // 召唤兽的跟随移动
                        final L1SummonInstance summon = (L1SummonInstance) petNpc;
                        summon.get_tempModel();

                    } else if (petNpc instanceof L1PetInstance) { // 宠物的跟随移动
                        final L1PetInstance pet = (L1PetInstance) petNpc;
                        pet.get_tempModel();
                    }
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private long _h_time;// 生存呐喊时间

    /**
     * 生存呐喊时间
     * 
     * @return
     */
    public long get_h_time() {
        return _h_time;
    }

    /**
     * 生存呐喊时间
     * 
     * @param h_time
     */
    public void set_h_time(long time) {
        _h_time = time;
    }

    private boolean _mazu = false;// 妈祖祝福

    /**
     * 妈祖祝福
     * 
     * @param b
     */
    public void set_mazu(boolean b) {
        _mazu = b;
    }

    /**
     * 妈祖祝福
     * 
     * @return
     */
    public boolean is_mazu() {
        return _mazu;
    }

    private long _mazu_time = 0;// 妈祖祝福时间

    /**
     * 妈祖祝福时间
     * 
     * @return
     */
    public long get_mazu_time() {
        return _mazu_time;
    }

    /**
     * 妈祖祝福时间
     * 
     * @param time
     */
    public void set_mazu_time(long time) {
        _mazu_time = time;
    }

    private int _int1;// 机率增加攻击力
    private int _int2;// 机率(1/100)

    /**
     * 机率增加攻击力
     * 
     * @param int1
     * @param int2
     */
    public void set_dmgAdd(int int1, int int2) {
        _int1 += int1;
        _int2 += int2;
    }

    /**
     * 传回机率增加的攻击力
     * 
     * @return
     */
    public int dmgAdd() {
        if (_int2 == 0) {
            return 0;
        }
        if ((_random.nextInt(100) + 1) <= _int2) {
            if (!getDolls().isEmpty()) {
                for (L1DollInstance doll : getDolls().values()) {
                    doll.show_action(1);
                }
            }
            return _int1;
        }
        return 0;
    }

    private int _evasion;// 回避机率(1/1000)

    /**
     * 回避机率
     * 
     * @param int1
     */
    public void set_evasion(int int1) {
        _evasion += int1;
    }

    /**
     * 传回回避机率
     * 
     * @return
     */
    public int get_evasion() {
        return _evasion;
    }

    private double _expadd = 0.0D;// 经验值增加

    /**
     * 经验值增加
     * 
     * @param int1
     */
    public void set_expadd(int int1) {
        _expadd += (int1 / 100D);
    }

    /**
     * 经验值增加
     * 
     * @return
     */
    public double getExpAdd() {
        return _expadd;
    }

    private int _dd1;// 机率伤害减免
    private int _dd2;// 机率(1/100)

    /**
     * 机率伤害减免
     * 
     * @param int1
     * @param int2
     */
    public void set_dmgDowe(int int1, int int2) {
        _dd1 += int1;
        _dd2 += int2;
    }

    /**
     * 传回机率伤害减免
     * 
     * @return
     */
    public int dmgDowe() {
        if (_dd2 == 0) {
            return 0;
        }
        if ((_random.nextInt(100) + 1) <= _dd2) {
            if (!getDolls().isEmpty()) {
                for (L1DollInstance doll : getDolls().values()) {
                    doll.show_action(2);
                }
            }
            return _dd1;
        }
        return 0;
    }

    private boolean _isFoeSlayer = false;

    /**
     * 是否使用屠宰者
     * 
     * @return
     */
    public boolean isFoeSlayer() {
        return _isFoeSlayer;
    }

    /**
     * 是否使用屠宰者
     */
    public void isFoeSlayer(boolean isFoeSlayer) {
        _isFoeSlayer = isFoeSlayer;
    }

    private int _weaknss = 0;
    private long _weaknss_t = 0;// 时间

    /**
     * 弱点曝光时间
     * 
     * @return
     */
    public long get_weaknss_t() {
        return _weaknss_t;
    }

    /**
     * 弱点曝光阶段
     * 
     * @return
     */
    public int get_weaknss() {
        return _weaknss;
    }

    /**
     * 弱点曝光阶段
     * 
     * @param lv
     */
    public void set_weaknss(int lv, long t) {
        _weaknss = lv;
        _weaknss_t = t;
        switch (_weaknss) { //设置之弱点暴光状态 hjx1000
        	case 1:
        		this.sendPackets(new S_PacketBoxDk(S_PacketBoxDk.LV1));
        		break;
        	case 2:
        		this.sendPackets(new S_PacketBoxDk(S_PacketBoxDk.LV2));
        		break;
        	case 3:
        		this.sendPackets(new S_PacketBoxDk(S_PacketBoxDk.LV3));
        		break;
        	
        }
    }

    private int _actionId = -1;// 角色表情动作代号

    /**
     * 角色表情动作代号
     * 
     * @param actionId
     */
    public void set_actionId(int actionId) {
        _actionId = actionId;
    }

    /**
     * 角色表情动作代号
     * 
     * @return
     */
    public int get_actionId() {
        return _actionId;
    }

    private Chapter01R _hardin = null;// 哈汀副本线程

    /**
     * 哈汀副本线程
     * 
     * @param hardin
     */
    public void set_hardinR(Chapter01R hardin) {
        _hardin = hardin;
    }

    /**
     * 哈汀副本线程
     * 
     * @return
     */
    public Chapter01R get_hardinR() {
        return _hardin;
    }

    private int _unfreezingTime = 0;// 解除人物卡点

    public void set_unfreezingTime(int i) {
        _unfreezingTime = i;
    }

    public int get_unfreezingTime() {
        return _unfreezingTime;
    }

    private int _magic_modifier_dmg = 0;// 套装增加魔法伤害

    public void add_magic_modifier_dmg(int add) {
        _magic_modifier_dmg += add;
    }

    public int get_magic_modifier_dmg() {
        return _magic_modifier_dmg;
    }

    private int _magic_reduction_dmg = 0;// 套装减免魔法伤害

    public void add_magic_reduction_dmg(int add) {
        _magic_reduction_dmg += add;
    }

    public int get_magic_reduction_dmg() {
        return _magic_reduction_dmg;
    }

    private boolean _rname = false;// 重设名称

    /**
     * 重设名称
     * 
     * @param b
     */
    public void rename(boolean b) {
        _rname = b;
    }

    /**
     * 重设名称
     * 
     * @return
     */
    public boolean is_rname() {
        return _rname;
    }

    private boolean _retitle = false;// 重设封号

    /**
     * 重设封号
     * 
     * @return
     */
    public boolean is_retitle() {
        return _retitle;
    }

    /**
     * 重设封号
     * 
     * @param b
     */
    public void retitle(boolean b) {
        _retitle = b;
    }

    private int _repass = 0;// 重设密码

    /**
     * 重设密码
     * 
     * @return
     */
    public int is_repass() {
        return _repass;
    }

    /**
     * 重设密码
     * 
     * @param b
     */
    public void repass(int b) {
        _repass = b;
    }

//    // 交易物品暂存
//    private final ArrayList<L1TradeItem> _trade_items = new ArrayList<L1TradeItem>();
//
//    /**
//     * 加入交易物品暂存
//     * 
//     * @param info
//     */
//    public void add_trade_item(L1TradeItem info) {
//        if (_trade_items.size() == 16) {
//            return;
//        }
//        _trade_items.add(info);
//    }
//
//    /**
//     * 交易物品暂存
//     * 
//     * @return
//     */
//    public ArrayList<L1TradeItem> get_trade_items() {
//        return _trade_items;
//    }
//
//    /**
//     * 清空交易物品暂存
//     */
//    public void get_trade_clear() {
//        _tradeID = 0;
//        _trade_items.clear();
//    }

    private int _mode_id = 0;// 记录选取位置

    /**
     * 记录选取位置
     * 
     * @param mode
     */
    public void set_mode_id(int mode) {
        _mode_id = mode;
    }

    /**
     * 记录选取位置
     * 
     * @return
     */
    public int get_mode_id() {
        return _mode_id;
    }

    private boolean _check_item = false;

    public void set_check_item(boolean b) {
        _check_item = b;
    }

    public boolean get_check_item() {
        return _check_item;
    }

    private long _global_time = 0;

    public long get_global_time() {
        return _global_time;
    }

    public void set_global_time(final long global_time) {
        _global_time = global_time;
    }

    // DOLL 指定时间HP恢复

    private int _doll_hpr = 0;

    public int get_doll_hpr() {
        return _doll_hpr;
    }

    public void set_doll_hpr(int hpr) {
        _doll_hpr = hpr;
    }

    private int _doll_hpr_time = 0;// 计算用时间(秒)

    public int get_doll_hpr_time() {
        return _doll_hpr_time;
    }

    public void set_doll_hpr_time(int time) {
        _doll_hpr_time = time;
    }

    private int _doll_hpr_time_src = 0;// 恢复时间(秒)

    public int get_doll_hpr_time_src() {
        return _doll_hpr_time_src;
    }

    public void set_doll_hpr_time_src(int time) {
        _doll_hpr_time_src = time;
    }

    // DOLL 指定时间MP恢复

    private int _doll_mpr = 0;

    public int get_doll_mpr() {
        return _doll_mpr;
    }

    public void set_doll_mpr(int mpr) {
        _doll_mpr = mpr;
    }

    private int _doll_mpr_time = 0;// 计算用时间(秒)

    public int get_doll_mpr_time() {
        return _doll_mpr_time;
    }

    public void set_doll_mpr_time(int time) {
        _doll_mpr_time = time;
    }

    private int _doll_mpr_time_src = 0;// 恢复时间(秒)

    public int get_doll_mpr_time_src() {
        return _doll_mpr_time_src;
    }

    public void set_doll_mpr_time_src(int time) {
        _doll_mpr_time_src = time;
    }

    // DOLL 指定时间给予物品

    private final int[] _doll_get = new int[2];

    public int[] get_doll_get() {
        return _doll_get;
    }

    public void set_doll_get(int itemid, int count) {
        _doll_get[0] = itemid;
        _doll_get[1] = count;
    }

    private int _doll_get_time = 60;// 计算用时间(秒)

    public int get_doll_get_time() {
        return _doll_get_time;
    }

    public void set_doll_get_time(int time) {
        _doll_get_time = time;
    }

    private int _doll_get_time_src = 0;// 给予时间(秒)

    public int get_doll_get_time_src() {
        return _doll_get_time_src;
    }

    public void set_doll_get_time_src(int time) {
        _doll_get_time_src = time;
    }

    // 留言版使用
    private String _board_title;// 暂存文字串

    public void set_board_title(final String text) {
        this._board_title = text;
    }

    public String get_board_title() {
        return this._board_title;
    }

    private String _board_content;// 暂存文字串

    public void set_board_content(final String text) {
        this._board_content = text;
    }

    public String get_board_content() {
        return this._board_content;
    }

    // 封包接收速度纪录
    private long _spr_move_time = 0;// 移动

    public void set_spr_move_time(final long spr_time) {
        _spr_move_time = spr_time;
    }

    public long get_spr_move_time() {
        return this._spr_move_time;
    }

    private long _spr_attack_time = 0;// 攻击

    public void set_spr_attack_time(final long spr_time) {
        _spr_attack_time = spr_time;
    }

    public long get_spr_attack_time() {
        return this._spr_attack_time;
    }

    private long _spr_skill_time = 0;// 技能

    public void set_spr_skill_time(final long spr_time) {
        _spr_skill_time = spr_time;
    }

    public long get_spr_skill_time() {
        return this._spr_skill_time;
    }

    // 死亡相关

    private int _delete_time = 0;// 死亡时间

    public void set_delete_time(final int time) {
        _delete_time = time;
    }

    public int get_delete_time() {
        return _delete_time;
    }

    // 药水使用HP恢复增加

    private int _up_hp_potion = 0;

    /**
     * 药水使用HP恢复增加(1/100)
     * 
     * @param up_hp_potion
     */
    public void set_up_hp_potion(final int up_hp_potion) {
        _up_hp_potion = up_hp_potion;
    }

    /**
     * 药水使用HP恢复增加(1/100)
     * 
     * @return
     */
    public int get_up_hp_potion() {
        return _up_hp_potion;
    }

    // 法利昂的治愈守护(1/1000)

    private int _elitePlateMail_Fafurion = 0;
    private int _fafurion_hpmin = 0;
    private int _fafurion_hpmax = 0;

    /**
     * 法利昂的治愈守护(1/1000)
     * 
     * @param r
     */
    public void set_elitePlateMail_Fafurion(final int r, final int hpmin,
            final int hpmax) {
        _elitePlateMail_Fafurion = r;
        _fafurion_hpmin = hpmin;
        _fafurion_hpmax = hpmax;
    }

    // 毒性抵抗效果
    int _venom_resist = 0;

    /**
     * 毒性抵抗效果(装备)
     * 
     * @param i
     *            装备数量
     */
    public void set_venom_resist(int i) {
        _venom_resist += i;
    }

    /**
     * 毒性抵抗效果(装备)
     * 
     * @return
     */
    public int get_venom_resist() {
        return _venom_resist;
    }

    // 加速检测器
    private AcceleratorChecker _speed = null;

    /**
     * 加速检测器
     * 
     * @return
     */
    public AcceleratorChecker speed_Attack() {
        if (_speed == null) {
            _speed = new AcceleratorChecker(this);
        }
        return _speed;
    }

    /** 杀怪数量. */
    private int killMonstersNumber;

    /**
     * 取得杀怪数量.
     * 
     * @return killMonstersNumber
     */
    public int getKillMonstersNumber() {
        return killMonstersNumber;
    }

    /**
     * 设置杀怪数量.
     * 
     * @param number
     *            - 数量
     */
    public void setKillMonstersNumber(int number) {
        killMonstersNumber = number;
    }

    /** 装备中的戒指1. */
    private int equipmentRing1ID;
    /** 装备中的戒指2. */
    private int equipmentRing2ID;
    /** 装备中的戒指3. */
    private int equipmentRing3ID;
    /** 装备中的戒指4. */
    private int equipmentRing4ID;

    /**
     * 取得装备中的戒指1.
     * 
     * @return 该道具的OBJID
     */
    public int getEquipmentRing1ID() {
        return equipmentRing1ID;
    }

    /**
     * 设置装备中的戒指1.
     * 
     * @param i
     *            - 该道具的OBJID
     */
    public void setEquipmentRing1ID(int i) {
        equipmentRing1ID = i;
    }

    /**
     * 取得装备中的戒指2.
     * 
     * @return 该道具的OBJID
     */
    public int getEquipmentRing2ID() {
        return equipmentRing2ID;
    }

    /**
     * 设置装备中的戒指2.
     * 
     * @param i
     *            - 该道具的OBJID
     */
    public void setEquipmentRing2ID(int i) {
        equipmentRing2ID = i;
    }

    /**
     * 取得装备中的戒指3.
     * 
     * @return 该道具的OBJID
     */
    public int getEquipmentRing3ID() {
        return equipmentRing3ID;
    }

    /**
     * 设置装备中的戒指3.
     * 
     * @param i
     *            - 该道具的OBJID
     */
    public void setEquipmentRing3ID(int i) {
        equipmentRing3ID = i;
    }

    /**
     * 取得装备中的戒指4.
     * 
     * @return 该道具的OBJID
     */
    public int getEquipmentRing4ID() {
        return equipmentRing4ID;
    }

    /**
     * 设置装备中的戒指4.
     * 
     * @param i
     *            - 该道具的OBJID
     */
    public void setEquipmentRing4ID(int i) {
        equipmentRing4ID = i;
    }

    /** 是否为进入游戏世界状态. */
    private boolean isLoginToServer;

    /**
     * 是否为进入游戏世界状态.
     * 
     * @return 返回 true or false
     */
    public boolean isLoginToServer() {
        return isLoginToServer;
    }

    /**
     * 设置进入游戏世界状态.
     * 
     * @param flag
     *            - true or false
     */
    public void setLoginToServer(boolean flag) {
        isLoginToServer = flag;
    }

    /** 是否显示血盟聊天. */
    private boolean _isShowClanChat = true;

    /**
     * 是否显示血盟聊天信息.
     * 
     * @return 是则返回 true
     */
    public boolean isShowClanChat() {
        return _isShowClanChat;
    }

    /**
     * 设置显示血盟聊天.
     * 
     * @param flag
     *            True：是、False：否
     */
    public void setShowClanChat(boolean flag) {
        _isShowClanChat = flag;
    }

    /** 是否显示组队聊天. */
    private boolean _isShowPartyChat = true;

    /**
     * 是否显示组队聊天信息.
     * 
     * @return 是则返回 true
     */
    public boolean isShowPartyChat() {
        return _isShowPartyChat;
    }

    /**
     * 设置显示组队聊天.
     * 
     * @param flag
     *            True：是、False：否
     */
    public void setShowPartyChat(boolean flag) {
        _isShowPartyChat = flag;
    }

    /** 魔法传送临时地图编号(记忆坐标). */
    private short tempBookmarkMapID;
    /** 魔法传送临时X坐标(记忆坐标). */
    private int tempBookmarkLocX;
    /** 魔法传送临时Y坐标(记忆坐标). */
    private int tempBookmarkLocY;

    /**
     * 取得魔法传送临时地图编号(记忆坐标).
     * 
     * @return tempBookmarkMapID
     */
    public short getTempBookmarkMapID() {
        return tempBookmarkMapID;
    }

    /**
     * 设置魔法传送临时地图编号(记忆坐标).
     * 
     * @param tempBookmarkMapID
     *            - tempBookmarkMapID
     */
    public void setTempBookmarkMapID(short tempBookmarkMapID) {
        this.tempBookmarkMapID = tempBookmarkMapID;
    }

    /**
     * 取得魔法传送临时X坐标(记忆坐标).
     * 
     * @return tempBookmarkLocX
     */
    public int getTempBookmarkLocX() {
        return tempBookmarkLocX;
    }

    /**
     * 设置魔法传送临时X坐标(记忆坐标).
     * 
     * @param tempBookmarkLocX
     *            - tempBookmarkLocX
     */
    public void setTempBookmarkLocX(int tempBookmarkLocX) {
        this.tempBookmarkLocX = tempBookmarkLocX;
    }

    /**
     * 取得魔法传送临时Y坐标(记忆坐标).
     * 
     * @return tempBookmarkLocY
     */
    public int getTempBookmarkLocY() {
        return tempBookmarkLocY;
    }

    /**
     * 设置魔法传送临时Y坐标(记忆坐标).
     * 
     * @param tempBookmarkLocY
     *            - tempBookmarkLocY
     */
    public void setTempBookmarkLocY(int tempBookmarkLocY) {
        this.tempBookmarkLocY = tempBookmarkLocY;
    }

    /** . */
    private RocksPrison _rocksPrison;
    /** 奇岩地监(开关). */
    private boolean _rocksPrisonActive;
    /** 奇岩地监时间(秒). */
    private int _rocksPrisonTime = 0;
    /**
     * 奇岩地监发送显示时间
     */
    private boolean _isShow = true;
    /**
     * 奇岩地监是否发送显示时间
     */
	public void setShow(boolean flag) {
		_isShow = flag;
	}
    /**
     * 奇岩地监发送显示时间
     */
    public boolean isShow() {
		return _isShow;
	}



	/**
     * 取得奇岩地监时间.
     * 
     * @return 时间
     */
    public int getRocksPrisonTime() {
        return this._rocksPrisonTime;
    }

    /**
     * 设置奇岩地监时间.
     * 
     * @param time
     *            时间
     */
    public void setRocksPrisonTime(final int time) {
        this._rocksPrisonTime = time;
    }

    private static Timer _regenTimer = new Timer(true);

    /**
     * 奇岩地监计时(开始).
     */
    public void startRocksPrison() {
        final int INTERVAL = 1000;
        if (!this._rocksPrisonActive) {
            this._rocksPrison = new RocksPrison(this);
            _regenTimer.scheduleAtFixedRate(this._rocksPrison, INTERVAL,
                    INTERVAL);
            this._rocksPrisonActive = true;
        }
    }

    /**
     * 奇岩地监计时(停止).
     */
    public void stopRocksPrison() {
        if (this._rocksPrisonActive) {
            this._rocksPrison.cancel();
            this._rocksPrison = null;
            this._rocksPrisonActive = false;
        }
    }
    /**
     * 外挂验证数值
     */
    private int Check_number;//检测外挂状态 hjx1000

	public int getCheck_number() {
		return Check_number;
	}

	public void setCheck_number(int check_number) {
		Check_number = check_number;
	}
	
    /**
     * 外挂验证次数 hjx1000
     */
	private int Check_cou = 0;

	public int getCheck_cou() {
		return Check_cou;
	}

	public void setCheck_cou(int check_cou) {
		Check_cou = check_cou;
	}
	
    /**
     * 动作延时 hjx1000 防变档
     */
    private boolean _isHardDelay = false;
    
    /**
     * 设定动作延迟中
     * 
     * @param flag
     *            true:是 false:否
     */
    public void setHardDelay(final boolean flag) {
        _isHardDelay = flag;
    }
    
    /**
     * 是否动作延迟中
     * 
     * @return true:是 false:否
     */
    public boolean isHardDelay() {
        return _isHardDelay;
    }
    
    /**
     * 技能动作延时 hjx1000 防变档
     */
    private boolean _isskillHardDelay = false;
    
    /**
     * 设定技能动作延迟中
     * 
     * @param flag
     *            true:是 false:否
     */
    public void setskillHardDelay(final boolean flag) {
        _isskillHardDelay = flag;
        if (flag == true) {
        	skillHardDelay.onHardUse(this);
        }
    }
    
    /**
     * 是否技能动作延迟中
     * 
     * @return true:是 false:否
     */
    public boolean isskillHardDelay() {
        return _isskillHardDelay;
    }
    
	private ArrayList<String> _cmalist = new ArrayList<String>(); //血盟UI hjx1000
	  
	public void addCMAList(String name) { //血盟UI hjx1000
	    if (this._cmalist.contains(name)) {
	    	return;
	    }
	    this._cmalist.add(name);
	}
	
	public void removeCMAList(String name) { //血盟UI hjx1000
		if (!this._cmalist.contains(name)) {
			return;
		}	      

		 this._cmalist.remove(name);
	}

	public ArrayList<String> getCMAList() { //血盟UI hjx1000
		return this._cmalist;
	}
	
	
//    private String _Recommend_account; //推荐人系统hjx1000
//
//    public String getRecommend_account() { //推荐人系统hjx1000
//        return this._Recommend_account;
//    }
//
//    public void setRecommend_account(final String s) {//推荐人系统hjx1000
//        this._Recommend_account = s;
//    }
    private int _homeX; // 开始挂机的x坐标

    public int getHomeX() {
        return this._homeX;
    }

    public void setHomeX(final int i) {
        this._homeX = i;
    }

    private int _homeY; // 开始挂机的y坐标

    public int getHomeY() {
        return this._homeY;
    }

    public void setHomeY(final int i) {
        this._homeY = i;
    }
    
    private int _hookrange; // 挂机范围
    
    public int gethookrange() {
    	return _hookrange;
    }
    
    public void sethookrange(final int i) {
    	this._hookrange = i;
    }

//    protected final L1HateList _hateList = new L1HateList();// 目标清单
//    private boolean _firstAttack = false; 
    protected NpcMoveExecutor _pcMove = null;// XXX
    /**
     * 启用PC AI
     */
	public void startAI() {
        if (this.isDead()) {
            return;
        }
        if (this.isGhost()) {
            return;
        }
        if (this.getCurrentHp() <= 0) {
            return;
        }
        if (this.isPrivateShop()) {
        	return;
        }
        if (this.isParalyzed()) {
        	return;
        }

        if (_pcMove != null) {
        	_pcMove = null;
        }
        _pcMove = new pcMove(this);
        this.setAiRunning(true);
        this.setActived(true);
        final PcAI npcai = new PcAI(this);
        npcai.startAI();
    }
	
    private boolean _aiRunning = false; //PC AI时间轴 正在运行 
    /**
     * PC AI时间轴 正在运行
     * 
     * @param aiRunning
     */
    protected void setAiRunning(final boolean aiRunning) {
        this._aiRunning = aiRunning;
    }

    /**
     * PC AI时间轴 正在运行
     * 
     * @return
     */
    public boolean isAiRunning() {
        return this._aiRunning;
    }
    
    /**
     * 清除全部目标
     */
    public void allTargetClear() {
        // XXX
        if (_pcMove != null) {
            _pcMove.clear();
        }
        //_hateList.clear();
        _target = null;
//        setFirstAttack(false);
    }
    
    /**
     * 清除单个目标
     */
	public void targetClear() {
		if (_target == null) {
			return;
		}
		//_hateList.remove(_target);
		_target = null;
	}
    
    /**
     * 有效目标检查
     */
    public void checkTarget() {
        try {
            if (_target == null) {// 目标为空
            	//targetClear();
                return;
            }
            if (_target.getMapId() != getMapId()) {// 目标地图不相等
            	targetClear();
                return;
            }
            if (_target.getCurrentHp() <= 0) {// 目标HP小于等于0
            	targetClear();
                return;
            }
            if (_target.isDead()) {// 目标死亡
            	targetClear();
                return;
            }
            if (get_showId() != _target.get_showId()) {// 副本ID不相等
            	targetClear();
                return;
            }
//            if (!_hateList.containsKey(_target)) {// 目标不在已有攻击清单中
//            	targetClear();
//                return;
//            }

            final int distance = getLocation().getTileDistance(
                    _target.getLocation());
            if (distance > 15) {
            	targetClear();
                return;
            }

        } catch (final Exception e) {
            return;
        }
    }
       
    /**
     * 现在目标
     */
    public L1Character is_now_target() {
        return _target;
    }
    
    /**
     * 对目标进行攻击
     * 
     * @param target
     */
    public void attackTarget(final L1Character target) { 
    	
        if (this.getInventory().getWeight240() >= 197) { // 重量过重
            // 110 \f1当负重过重的时候，无法战斗。
            this.sendPackets(new S_ServerMessage(110));
            // _log.error("要求角色攻击:重量过重");
            return;
        }

        if (target instanceof L1PcInstance) {
            final L1PcInstance player = (L1PcInstance) target;
            if (player.isTeleport()) { // テレポート处理中
                return;
            }
            if (!player.isPinkName()) {
            	this.allTargetClear();
            	return;
            }

        } else if (target instanceof L1PetInstance) {
            final L1PetInstance pet = (L1PetInstance) target;
            final L1Character cha = pet.getMaster();
            if (cha instanceof L1PcInstance) {
                final L1PcInstance player = (L1PcInstance) cha;
                if (player.isTeleport()) { // テレポート处理中
                    return;
                }
            }

        } else if (target instanceof L1SummonInstance) {
            final L1SummonInstance summon = (L1SummonInstance) target;
            final L1Character cha = summon.getMaster();
            if (cha instanceof L1PcInstance) {
                final L1PcInstance player = (L1PcInstance) cha;
                if (player.isTeleport()) { // テレポート处理中
                    return;
                }
            }
        }
    

        if (target instanceof L1NpcInstance) {
            final L1NpcInstance npc = (L1NpcInstance) target;
            if (npc.getHiddenStatus() != 0) { // 地中に潜っているか、飞んでいる
                this.allTargetClear();
                return;
            }
        }


        target.onAction(this);
    }
    
    /**
     * 攻击目标搜寻
     */
    public void searchTarget() {
//    	System.out.println("AI攻击目标搜寻");
    	//搜索目标前,先清空已有目标的列表
    	//_hateList.clear();
    	int hate = 16;
        final Collection<L1Object> allObj = World.get()
                .getVisibleObjects(this, 15);
        for (final Iterator<L1Object> iter = allObj.iterator(); iter.hasNext();) {
            final L1Object obj = iter.next();
            if (!(obj instanceof L1MonsterInstance)) {
            	continue;
            }
            final L1MonsterInstance mob = (L1MonsterInstance) obj;
        	if (mob.isDead()) {
        		continue;
        	}
            if (mob.getCurrentHp() <= 0) {
                continue;
            }
            if (mob.getHiddenStatus() > 0) {
            	continue;                	
            }
            if (mob.getAtkspeed() == 0) {
            	continue;
            }
            if (mob.hasSkillEffect(this.getId() + 100000)
            		&& !this.isAttackPosition(mob.getX(), mob.getY(), 1)) {
            	continue;
            }
//            if (mob.is_now_target() != null 
//            	&& mob.is_now_target() != this
//            	&& mob.isAttackPosition(mob.is_now_target().getX(), mob.is_now_target().getY(), mob.get_ranged())) { // 试试不抢怪模式
//            	continue;
//            }
            if (mob != null) {
                final int Distance = this.getTileLineDistance(mob);
//                if (this.glanceCheck(mob.getX(), mob.getY())) {
//                	Distance = + 10;
//                }
            	//_hateList.add(mob, Distance);
            	
                if (hate > Distance) {
                	_target = mob;
                    hate = Distance;
                }
                if (hate < 2) {
                	break;
                }
            }
        }
       // _target = _hateList.getMaxHateCharacter();
//    	System.out.println("AI启动666 _target==:" + _target);
    	if (_target == null) { //如果目标等于空

    		//等待处理，，瞬移。。等等设置
    		if (this.getMap().isTeleportable() 
    			&& this.getInventory().consumeItem(58026, 1)) { //地图可瞬移身上有白瞬卷 hjx1000
    			L1Teleport.randomTeleport(this, true);
    		}
    	}
    }
        
    /**
     * 具有目标的处理 (攻击的判断)
     */
    public void onTarget() {
        try {

            final L1Character target = _target;

            if (target == null) {
            	return;
            }
            attack(target);
            

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
    
    private boolean _Attack_or_walk = false;//攻击或走路 
    

    /**
     * 攻击或者走路
     * true = 攻击
     * false = 走路
     */
    public boolean Attack_or_walk() {
    	return this._Attack_or_walk;
    }
    private void attack(L1Character target) {
        // 攻击可能位置
    	int attack_Range = 1;
    	if (this.getWeapon() != null) {
    		attack_Range = this.getWeapon().getItem().getRange();
    	}
    	if (attack_Range < 0) {
    		attack_Range = 15;
    	}
        if (isAttackPosition(target.getX(), target.getY(), attack_Range)) {// 已经到达可以攻击的距离
            setHeading(targetDirection(target.getX(), target.getY()));
            attackTarget(target);
            this._Attack_or_walk = true;
            // XXX
            if (_pcMove != null) {
                _pcMove.clear();
            }

        } else { // 攻击不可能位置
//                final int distance = getLocation().getTileDistance(
//                        target.getLocation());
                if (_pcMove != null) {
                    final int dir = _pcMove.moveDirection(target.getX(),
                            target.getY());
                    if (dir == -1) {
                    	_target.setSkillEffect(this.getId() + 100000, 20000);//给予20秒状态
                    	targetClear();

                    } else {
                        _pcMove.setDirectionMove(dir);
//                        setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
                        this._Attack_or_walk = false;
                    }
                }
        }
    }
    
    
    private boolean _actived = false; // 挂机激活
    private boolean _Pathfinding = false; //寻路中.. hjx1000
    /**
     * PC已经激活
     * 
     * @param actived
     *            true:激活 false:无
     */
    public void setActived(final boolean actived) {
        this._actived = actived;
        if (!actived) {
            if (this.isskill132()) {
            	this.setskill132(false);
            }
            if (this.isskill187()) {
            	this.setskill187(false);
            }
            if (this.isskill46()) {
            	this.setskill46(false);
            }
        }
    }

    /**
     * PC已经激活
     * 
     * @return true:激活 false:无
     */
    public boolean isActived() {
        return this._actived;
    }
    
//    protected void setFirstAttack(final boolean firstAttack) {
//        this._firstAttack = firstAttack;
//    }
//
//    protected boolean isFirstAttack() {
//        return this._firstAttack;
//    }
//    
//    /**
//     * 攻击目标设置
//     * 
//     * @param cha
//     * @param hate
//     */
//    public void setHate(final L1Character cha, int hate) {
//        try {
//            if ((cha != null) && /*(cha.getId() != getId())*/ _target != null) {
//                if (!isFirstAttack() && (hate > 0)) {
//                    //hate += getMaxHp() / 10; // ＦＡヘイト
//                    setFirstAttack(true);
//                    if (_pcMove != null) {
//                        _pcMove.clear();// XXX
//                    }
//                    //System.out.println("isFirstAttack=" + isFirstAttack());
//                    _hateList.add(cha, 5);
//                    _target = _hateList.getMaxHateCharacter();
//                    checkTarget();
//                }
//            }
//
//        } catch (final Exception e) {
//            return;
//        }
//    }
    /**
     * 目标为空挂机寻路中
     * @return
     */
    public boolean isPathfinding() {
    	return this._Pathfinding;
    }
    public void setPathfinding(final boolean fla) {
    	this._Pathfinding = fla;
    }
    // 随机移动距离
//    private int _randomMoveDistance = 0;
    // 随机移动方向
    private int _randomMoveDirection = 0;
    public int getrandomMoveDirection() {
		return _randomMoveDirection;
	}

	public void setrandomMoveDirection(int randomMoveDirection) {
		this._randomMoveDirection = randomMoveDirection;
	}

	/**
     * 没有目标的处理 (传回本次AI是否执行完成)<BR>
     * 具有主人 跟随主人移动
     * 
     * @return true:本次AI执行完成 <BR>
     *         false:本次AI执行未完成
     */
    public void noTarget() {
        // 如果移动距离已经为0 重新定义随机移动
//        if (_randomMoveDistance == 0) {
//            // 产生移动距离
//            _randomMoveDistance = _random.nextInt(3) + 1;

            // 产生移动方向(随机数值超出7物件会暂停移动)
//            _randomMoveDirection = _random.nextInt(8);
//            if (_randomMoveDirection < 8) {
//            	
//            }
//
//        } else {
//            _randomMoveDistance--;
//        }
    	if (!_Pathfinding) {
        	_Pathfinding = true; //设置寻路中 
    	}
    	if (_randomMoveDirection > 7) {
    		_randomMoveDirection = 0;
    	}
        //System.out.println("_randomMoveDirection=:" + _randomMoveDirection);
        if (_pcMove != null) {
            if (getrandomMoveDirection() < 8) {
                int dir = _pcMove
                        .checkObject(_randomMoveDirection);
                dir = _pcMove.openDoor(dir);

                if (dir != -1) {
                    _pcMove.setDirectionMove(dir);
                } else {
                	_randomMoveDirection = _random.nextInt(8);
                }
            }
        }
    }
    
    private boolean _showDmg = false; // 显示攻击

    public void setshowDmg(final boolean showDmg) {
    	this._showDmg = showDmg;
    }

    /**
    * PC是否显示
    * 
    * @return true:显示 false:不显示
    */
    public boolean showDmg() {
    	return this._showDmg;
    }
    

    private boolean _skill46 = false;//烈炎术
    /**
     * PC是否自动施放烈炎术
     * true & false
     */
    public void setskill46(final boolean setskill46) {
    	this._skill46 = setskill46;
    	if (setskill46) {
    		if (this.isSkillMastery(46)) { //判断是否有该技能
        		AutoMagic.automagic(this, 46);
    		}
    	}
    }    
    /**
     * 自动施法烈炎术是否开启
     */
    public boolean isskill46() {
    	return this._skill46;
    }
    
    
    
    private boolean _skill132 = false;//三重矢  
    /**
     * PC是否自动施放三重矢
     * true & false
     */
    public void setskill132(final boolean setskill132) {
    	this._skill132 = setskill132;
    	if (setskill132) {
    		if (this.isSkillMastery(132)) { //判断是否有该技能
        		AutoMagic.automagic(this, 132);
    		}
    	}
    }   
    /**
     * 自动施法三重矢是否开启
     */
    public boolean isskill132() {
    	return this._skill132;
    }
    
    
    
    private boolean _skill187 = false;//屠宰者  
    /**
     * PC是否自动施放屠宰者
     * true & false
     */
    public void setskill187(final boolean setskill187) {
    	this._skill187 = setskill187;
    	if (setskill187) {
    		if (this.isSkillMastery(187)) { //判断是否有该技能
        		AutoMagic.automagic(this, 187);
    		}
    	}
    }   
    /**
     * 自动施法屠宰者是否开启
     */
    public boolean isskill187() {
    	return this._skill187;
    }
    

}
