package com.lineage.server.templates;

import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.poison.L1DamagePoison;
import com.lineage.server.model.poison.L1ParalysisPoison;
import com.lineage.server.model.poison.L1SilencePoison;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.utils.Dice;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.World;

/**
 * 陷阱资料
 * 
 * @author dexc
 * 
 */
public class L1Trap {

    private static final Log _log = LogFactory.getLog(L1Trap.class);

    private String _type;// 陷阱类型

    private int _trap;// 陷阱类型

    private int _id;// 陷阱编号

    private int _gfxId;// 效果图像编号

    private boolean _isDetectionable;// 可以被探查

    private Dice _dice;// 基础伤害质 / 基础治疗质

    private int _base;// 最小伤害质 / 最小治疗质

    private int _diceCount;// 伤害次数 / 治疗次数

    private int _npcId;// 召唤NPC编号

    private int _count;// 召唤数量

    private int _poisonType;// 诅咒类型

    private int _delay;// 诅咒延迟时间

    private int _time;// 诅咒时间

    private int _damage;// 诅咒伤害

    private int _skillId;// 技能编号

    private int _skillTimeSeconds;// 技能时间(秒)

    private L1Location _loc;// 传送位置点资料

    /**
     * 陷阱
     * 
     * @param rs
     */
    public L1Trap(final ResultSet rs) {
        try {
            this._id = rs.getInt("id");
            this._gfxId = rs.getInt("gfxId");
            this._isDetectionable = rs.getBoolean("isDetectionable");

            // 定义陷阱类型
            _type = rs.getString("type");
            if (_type.equalsIgnoreCase("L1DamageTrap")) {
                _trap = 1;// 陷阱-伤害 对接触者
                this._dice = new Dice(rs.getInt("dice"));
                this._base = rs.getInt("base");
                this._diceCount = rs.getInt("diceCount");

            } else if (_type.equalsIgnoreCase("L1HealingTrap")) {
                _trap = 2;// 陷阱-治疗 对接触者
                this._dice = new Dice(rs.getInt("dice"));
                this._base = rs.getInt("base");
                this._diceCount = rs.getInt("diceCount");

            } else if (_type.equalsIgnoreCase("L1MonsterTrap")) {
                _trap = 3;// 陷阱-召唤怪物 对接触者
                this._npcId = rs.getInt("monsterNpcId");
                this._count = rs.getInt("monsterCount");

            } else if (_type.equalsIgnoreCase("L1PoisonTrap")) {
                _trap = 4;// 陷阱-诅咒 对接触者
                // 定义诅咒类型
                final String poisonType = rs.getString("poisonType");
                if (poisonType.equalsIgnoreCase("d")) {
                    _poisonType = 1;// 一般型中毒

                } else if (poisonType.equalsIgnoreCase("s")) {
                    _poisonType = 2;// 沈默型中毒

                } else if (poisonType.equalsIgnoreCase("p")) {
                    _poisonType = 3;// 麻痹型中毒
                }

                this._delay = rs.getInt("poisonDelay");
                this._time = rs.getInt("poisonTime");
                this._damage = rs.getInt("poisonDamage");

            } else if (_type.equalsIgnoreCase("L1SkillTrap")) {
                _trap = 5;// 陷阱-施展指定技能 对接触者
                this._skillId = rs.getInt("skillId");
                this._skillTimeSeconds = rs.getInt("skillTimeSeconds");

            } else if (_type.equalsIgnoreCase("L1TeleportTrap")) {
                _trap = 6;// 陷阱-传送目标 对接触者
                final int x = rs.getInt("teleportX");
                final int y = rs.getInt("teleportY");
                final int mapId = rs.getInt("teleportMapId");
                this._loc = new L1Location(x, y, mapId);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 类型
     * 
     * @return
     */
    public String getType() {
        return _type + "(" + this._trap + "-" + this._id + ")";
    }

    /**
     * 陷阱编号
     * 
     * @return
     */
    public int getId() {
        return this._id;
    }

    /**
     * 效果图像编号
     * 
     * @return
     */
    public int getGfxId() {
        return this._gfxId;
    }

    /**
     * 召唤图像的处理
     * 
     * @param trapObj
     */
    private void sendEffect(final L1Object trapObj) {
        try {
            if (this.getGfxId() == 0) {
                return;
            }
            // 产生动画
            final S_EffectLocation effect = new S_EffectLocation(
                    trapObj.getLocation(), this.getGfxId());

            for (final L1PcInstance pc : World.get()
                    .getRecognizePlayer(trapObj)) {
                pc.sendPackets(effect);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 踩到陷阱的处理
     * 
     * @param trodFrom
     *            接触者
     * @param trapObj
     *            自己
     */
    public void onTrod(L1PcInstance trodFrom, L1Object trapObj) {
        switch (this._trap) {
            case 0:// 未定义
                _log.error("陷阱的处理未定义: " + this._id);
                break;

            case 1:// 陷阱-伤害 对接触者
                onType1(trodFrom, trapObj);
                break;

            case 2:// 陷阱-治疗 对接触者
                onType2(trodFrom, trapObj);
                break;

            case 3:// 陷阱-召唤怪物 对接触者
                onType3(trodFrom, trapObj);
                break;

            case 4:// 陷阱-诅咒 对接触者
                onType4(trodFrom, trapObj);
                break;

            case 5:// 陷阱-施展指定技能 对接触者
                onType5(trodFrom, trapObj);
                break;

            case 6:// 陷阱-传送目标 对接触者
                onType6(trodFrom, trapObj);
                break;
        }
    }

    /**
     * 被探查时状况处理
     * 
     * @param caster
     * @param trapObj
     */
    public void onDetection(final L1PcInstance caster, final L1Object trapObj) {
        if (this._isDetectionable) {
            this.sendEffect(trapObj);
        }
    }

    // TODO 陷阱类型对接触物件的处理

    // 陷阱-伤害 对接触者

    /**
     * 陷阱-伤害 对接触者
     * 
     * @param trodFrom
     *            接触者
     * @param trapObj
     *            自己
     */
    private void onType1(final L1PcInstance trodFrom, final L1Object trapObj) {
        // System.out.println("陷阱-伤害 对接触者:" + _trap);
        try {
            if (_trap != 1) {
                return;
            }
            if (_base <= 0) {
                return;
            }
            this.sendEffect(trapObj);

            final int dmg = this._dice.roll(this._diceCount) + this._base;
            // 送出伤害
            trodFrom.receiveDamage(trodFrom, dmg, false, true);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    // 陷阱-治疗 对接触者

    /**
     * 陷阱-治疗 对接触者
     * 
     * @param trodFrom
     *            接触者
     * @param trapObj
     *            自己
     */
    private void onType2(final L1PcInstance trodFrom, final L1Object trapObj) {
        // System.out.println("陷阱-治疗 对接触者:" + _trap);
        try {
            if (_trap != 2) {
                return;
            }
            if (_base <= 0) {
                return;
            }
            this.sendEffect(trapObj);

            final int pt = this._dice.roll(this._diceCount) + this._base;
            // 治疗
            trodFrom.healHp(pt);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    // 陷阱-召唤怪物 对接触者

    /**
     * 陷阱-召唤怪物 对接触者
     * 
     * @param trodFrom
     *            接触者
     * @param trapObj
     *            自己
     */
    private void onType3(final L1PcInstance trodFrom, final L1Object trapObj) {
        // System.out.println("陷阱-召唤怪物 对接触者:" + _trap);
        try {
            if (_trap != 3) {
                return;
            }
            if (_npcId <= 0) {
                return;
            }
            this.sendEffect(trapObj);

            for (int i = 0; i < this._count; i++) {
                final L1Location loc = trodFrom.getLocation().randomLocation(5,
                        false);
                L1NpcInstance newNpc = L1SpawnUtil.spawn(this._npcId, loc);
                newNpc.setLink(trodFrom);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    // 陷阱-诅咒 对接触者

    /**
     * 陷阱-诅咒 对接触者
     * 
     * @param trodFrom
     *            接触者
     * @param trapObj
     *            自己
     */
    private void onType4(final L1PcInstance trodFrom, final L1Object trapObj) {
        // System.out.println("陷阱-诅咒 对接触者:" + _trap);
        try {
            if (_trap != 4) {
                return;
            }
            this.sendEffect(trapObj);

            // 判断诅咒类型
            switch (this._poisonType) {
                case 1:// 一般型中毒
                       // System.out.println("一般型中毒");
                    L1DamagePoison.doInfection(trodFrom, trodFrom, this._time,
                            this._damage);
                    break;

                case 2:// 沈默型中毒
                       // System.out.println("沈默型中毒");
                    L1SilencePoison.doInfection(trodFrom);
                    break;

                case 3:// 麻痹型中毒
                       // System.out.println("麻痹型中毒");
                    L1ParalysisPoison.doInfection(trodFrom, this._delay,
                            this._time);
                    break;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    // 陷阱-施展指定技能 对接触者

    /**
     * 陷阱-施展指定技能 对接触者
     * 
     * @param trodFrom
     *            接触者
     * @param trapObj
     *            自己
     */
    private void onType5(final L1PcInstance trodFrom, final L1Object trapObj) {
        // System.out.println("陷阱-施展指定技能 对接触者:" + _trap);
        try {
            if (_trap != 5) {
                return;
            }
            if (_skillId <= 0) {
                return;
            }
            this.sendEffect(trapObj);

            new L1SkillUse().handleCommands(trodFrom, _skillId,
                    trodFrom.getId(), trodFrom.getX(), trodFrom.getY(),
                    _skillTimeSeconds, L1SkillUse.TYPE_GMBUFF);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    // 陷阱-传送目标 对接触者

    /**
     * 陷阱-传送目标 对接触者
     * 
     * @param trodFrom
     *            接触者
     * @param trapObj
     *            自己
     */
    private void onType6(final L1PcInstance trodFrom, final L1Object trapObj) {
        try {
            if (_trap != 6) {
                return;
            }
            if (_loc == null) {
                return;
            }
            this.sendEffect(trapObj);

            L1Teleport.teleport(trodFrom, this._loc.getX(), this._loc.getY(),
                    (short) this._loc.getMapId(), 5, true,
                    L1Teleport.ADVANCED_MASS_TELEPORT);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
