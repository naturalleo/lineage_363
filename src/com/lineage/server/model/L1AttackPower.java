package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.*;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillMode;
import com.lineage.server.model.skill.skillmode.SkillMode;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.world.World;

/**
 * 属性武器特殊攻击 0:无属性 1:地 2:火 4:水 8:风 16:光 32:暗 64:圣 128:邪
 * 
 * @author dexc
 * 
 */
public class L1AttackPower {

    private static final Log _log = LogFactory.getLog(L1AttackPower.class);

    private static final Random _random = new Random();

    // 执行PC
    private final L1PcInstance _pc;

    private final L1Character _target;

    // 目标PC
    private L1PcInstance _targetPc;

    // 目标NPC
    private L1NpcInstance _targetNpc;

    private int _weaponAttrEnchantKind = 0;

    private int _weaponAttrEnchantLevel = 0;

    public L1AttackPower(final L1PcInstance attacker, final L1Character target,
            int weaponAttrEnchantKind, int weaponAttrEnchantLevel) {
        _pc = attacker;
        _target = target;
        if (_target instanceof L1NpcInstance) {
            _targetNpc = (L1NpcInstance) _target;

        } else if (_target instanceof L1PcInstance) {
            _targetPc = (L1PcInstance) _target;
        }
        _weaponAttrEnchantKind = weaponAttrEnchantKind;
        _weaponAttrEnchantLevel = weaponAttrEnchantLevel;
    }

    /**
     * 属性武器特殊攻击 0:无属性 1:地 2:火 4:水 8:风 16:光 32:暗 64:圣 128:邪
     * 
     * @param damage
     * @return
     */
    public int set_item_power(final int damage) {
        int reset_dmg = damage;
        try {
            if (_weaponAttrEnchantKind > 0) {
                int random = 0;
                // 魔法特效
                switch (_weaponAttrEnchantKind) {
                    case 1: // 地
                        int time = 0;
                        switch (_weaponAttrEnchantLevel) {
                            case 1:// 5%机率束缚敌人0.8秒
                                random = 10;
                                time = 800;
                                break;

                            case 2:// 10%机率束缚敌人1.0秒
                                random = 20;
                                time = 1000;
                                break;

                            case 3:// 20%机率束缚敌人1.5秒
                                random = 30;
                                time = 1500;
                                break;
                        }
                        if (_random.nextInt(1000) <= random) {
                            if (!L1WeaponSkill.isFreeze(_target)) { // 冻结状态
                                _target.broadcastPacketX8(new S_SkillSound(
                                        _target.getId(), 4184));
                                if (_targetPc != null) {
                                    _targetPc.setSkillEffect(STATUS_FREEZE,
                                            time);
                                    _targetPc.sendPackets(new S_SkillSound(
                                            _target.getId(), 4184));
                                    _targetPc.sendPackets(new S_Paralysis(
                                            S_Paralysis.TYPE_BIND, true));

                                } else if (_targetNpc != null) {
                                    _targetNpc.setSkillEffect(STATUS_FREEZE,
                                            time);
                                    _targetNpc.setParalyzed(true);
                                }
                            }
                        }
                        break;

                    case 2: // 火
                        double adddmg = 0.0D;
                        switch (_weaponAttrEnchantLevel) {
                            case 1:// 5%发动机率 造成1.2倍伤害
                                random = 10;
                                adddmg = 1.2D;
                                break;

                            case 2:// 10%发动机率 造成1.4倍伤害
                                random = 20;
                                adddmg = 1.4D;
                                break;

                            case 3:// 20%发动机率 造成1.6倍伤害
                                random = 30;
                                adddmg = 1.6D;
                                break;
                        }
                        if (_random.nextInt(1000) <= random) {
                            _pc.sendPacketsX8(new S_SkillSound(_pc.getId(),
                                    7756));
                            reset_dmg = (int) (reset_dmg * adddmg);
                        }
                        break;

                    case 4: // 水
                        int drainHp = 0;
                        int drainMp = 0;
                        switch (_weaponAttrEnchantLevel) {
                            case 1:// 5%发动机率 吸血(伤害的0.2倍,例如打100滴刚发动就是吸了20滴HP)
                                   // 吸魔随机吸1~5
                                random = 10;
                                drainHp = (int) (reset_dmg * 0.2D);
                                drainMp = 1 + _random.nextInt(5);
                                break;

                            case 2:// 10%发动机率 吸血(伤害的0.4倍,例如打100滴刚发动就是吸了40滴HP)
                                   // 吸魔随机吸2~10
                                random = 20;
                                drainHp = (int) (reset_dmg * 0.4D);
                                drainMp = 2 + _random.nextInt(10);
                                break;

                            case 3:// 20%发动机率 吸血(伤害的0.6倍,例如打100滴刚发动就是吸了60滴HP)
                                   // 吸魔随机吸3~15
                                random = 30;
                                drainHp = (int) (reset_dmg * 0.6D);
                                drainMp = 3 + _random.nextInt(15);
                                break;
                        }
                        if (_random.nextInt(1000) <= random) {
                            _pc.sendPacketsX8(new S_SkillSound(_pc.getId(),
                                    7749));
                            _pc.setCurrentHp((short) (_pc.getCurrentHp() + drainHp));
                            _pc.setCurrentMp((short) (_pc.getCurrentMp() + drainMp));
                            if (_targetPc != null) {
                                _targetPc.setCurrentMp((short) (_targetPc
                                        .getCurrentMp() - drainMp));

                            } else if (_targetNpc != null) {
                                _targetNpc.setCurrentMp((short) (_targetNpc
                                        .getCurrentMp() - drainMp));
                            }
                        }
                        break;

                    case 8: // 风
                        int r = 1;
                        switch (_weaponAttrEnchantLevel) {
                            case 1:// 5%机率造成一格范围伤害 (原始伤害固定50+浮动100)
                                random = 10;
                                r = 1;
                                break;

                            case 2:// 10%机率造成二格范围伤害 (魔法伤害固定50+浮动100)
                                random = 20;
                                r = 2;
                                break;

                            case 3:// 20%机率造成三格范围伤害 (魔法伤害固定50+浮动100)
                                random = 30;
                                r = 3;
                                break;
                        }
                        if (_random.nextInt(1000) <= random) {
                            _pc.sendPacketsX8(new S_SkillSound(_pc.getId(),
                                    7752));
                            final int dmg = 50 + _random.nextInt(100);
                            if (_targetPc != null) {
                                for (L1Object tgobj : World.get()
                                        .getVisibleObjects(_pc, r)) {
                                    if (tgobj instanceof L1PcInstance) {
                                        final L1PcInstance tgpc = (L1PcInstance) tgobj;
                                        if (tgpc.isDead()) {
                                            continue;
                                        }
                                        // 排除同盟
                                        if (tgpc.getClanid() == _pc.getClanid()) {
                                            if (tgpc.getClanid() != 0) {
                                                continue;
                                            }
                                        }
                                        // 排除安全区
                                        if (tgpc.getMap().isSafetyZone(
                                                tgpc.getLocation())) {
                                            continue;
                                        }
                                        tgpc.receiveDamage(_pc, dmg, false,
                                                false);// 物理伤害
                                    }
                                }

                            } else if (_targetNpc != null) {
                                for (L1Object tgobj : World.get()
                                        .getVisibleObjects(_pc, r)) {
                                    if (tgobj instanceof L1MonsterInstance) {
                                        final L1MonsterInstance tgmob = (L1MonsterInstance) tgobj;
                                        if (tgmob.isDead()) {
                                            continue;
                                        }
                                        tgmob.receiveDamage(_pc, dmg);// 物理伤害
                                    }
                                }
                            }
                        }
                        break;

                    case 16: // 光
                        switch (_weaponAttrEnchantLevel) {
                            case 1:// 光之:1%召唤光裂(依人物魔功智力产生伤害)
                                random = 10;
                                break;

                            case 2:// 闪耀:2%召唤光裂(依人物魔功智力产生伤害)
                                random = 20;
                                break;

                            case 3:// 光灵:3%召唤光裂(依人物魔功智力产生伤害)
                                random = 30;
                                break;
                        }
                        if (_random.nextInt(1000) <= random) {
                            final L1Magic magic = new L1Magic(_pc, _target);
                            final int magic_dmg = magic
                                    .calcMagicDamage(DISINTEGRATE);
                            magic.commit(magic_dmg, 0);
                            final L1Skills skill = SkillsTable.get()
                                    .getTemplate(DISINTEGRATE);
                            final int castgfx = skill.getCastGfx();

                            _target.broadcastPacketX8(new S_SkillSound(_target
                                    .getId(), castgfx));
                            if (_targetPc != null) {
                                _targetPc.sendPackets(new S_SkillSound(_target
                                        .getId(), castgfx));
                            }
                        }
                        break;

                    case 32: // 暗
                        switch (_weaponAttrEnchantLevel) {
                            case 1:// 暗之:1%施展闇盲
                                random = 10;
                                break;

                            case 2:// 阴影:2%施展闇盲
                                random = 20;
                                break;

                            case 3:// 暗灵:3%施展闇盲
                                random = 30;
                                break;
                        }
                        if (_random.nextInt(1000) <= random) {
                            // SKILL移转
                            final SkillMode mode = L1SkillMode.get().getSkill(
                                    CURSE_BLIND);
                            if (mode != null) {
                                mode.start(_pc, _target, null, 10);
                            }
                            _target.broadcastPacketX8(new S_SkillSound(_target
                                    .getId(), 746));
                            if (_targetPc != null) {
                                _targetPc.sendPackets(new S_SkillSound(_target
                                        .getId(), 746));
                            }
                        }
                        break;

                    case 64: // 圣
                        int integer = 0;
                        switch (_weaponAttrEnchantLevel) {
                            case 1:// 圣之:1%施展魔法封印(封印时间:5秒)
                                random = 10;
                                integer = 5;
                                break;

                            case 2:// 神圣:2%施展魔法封印(封印时间:8秒)
                                random = 20;
                                integer = 8;
                                break;

                            case 3:// 圣灵:3%施展魔法封印(封印时间:10秒)
                                random = 30;
                                integer = 10;
                                break;
                        }
                        if (_random.nextInt(1000) <= random) {
                            if (!_target.hasSkillEffect(SILENCE)) {
                                _target.setSkillEffect(SILENCE, integer * 1000);
                                _target.broadcastPacketX8(new S_SkillSound(
                                        _target.getId(), 2177));
                                if (_targetPc != null) {
                                    _targetPc.sendPackets(new S_SkillSound(
                                            _targetPc.getId(), 2177, integer));
                                }
                            }
                        }
                        break;

                    case 128: // 邪
                        int[] gfxs = null;
                        switch (_weaponAttrEnchantLevel) {
                            case 1:// 邪之:1%施展变形术(目标变形:狼人,妖魔斗士)
                                random = 10;
                                gfxs = new int[] { 3865, 3864 };
                                break;

                            case 2:// 邪恶:2%施展变形术(目标变形:狼人,妖魔斗士,人形僵尸)
                                random = 20;
                                gfxs = new int[] { 3865, 3864, 3872 };
                                break;

                            case 3:// 邪灵:3%施展变形术(目标变形:狼人,妖魔斗士,人形僵尸,纸人)
                                random = 30;
                                gfxs = new int[] { 3865, 3864, 3872, 1538 };
                                break;
                        }
                        if (_random.nextInt(1000) <= random) {
                            if (_targetPc != null) {
                                _targetPc.sendPacketsX8(new S_SkillSound(
                                        _target.getId(), 230));
                                final int polyId = gfxs[_random
                                        .nextInt(gfxs.length)];
                                L1PolyMorph.doPoly(_targetPc, polyId, 60,
                                        L1PolyMorph.MORPH_BY_ITEMMAGIC);

                            } else if (_targetNpc != null) {
                                // 不是BOSS召唤表物件
                                if (!_targetNpc.getNpcTemplate().is_boss()) {
                                    _target.broadcastPacketX8(new S_SkillSound(
                                            _target.getId(), 230));
                                    final int polyId = gfxs[_random
                                            .nextInt(gfxs.length)];
                                    L1PolyMorph.doPoly(_target, polyId, 60,
                                            L1PolyMorph.MORPH_BY_ITEMMAGIC);
                                }
                            }
                        }
                        break;
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return reset_dmg;
    }

}
