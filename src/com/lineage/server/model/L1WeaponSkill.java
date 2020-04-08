package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static com.lineage.server.model.skill.L1SkillId.BERSERKERS;
import static com.lineage.server.model.skill.L1SkillId.COUNTER_MAGIC;
import static com.lineage.server.model.skill.L1SkillId.EARTH_BIND;
import static com.lineage.server.model.skill.L1SkillId.FREEZING_BLIZZARD;
import static com.lineage.server.model.skill.L1SkillId.FREEZING_BREATH;
import static com.lineage.server.model.skill.L1SkillId.ICE_LANCE;
import static com.lineage.server.model.skill.L1SkillId.STATUS_FREEZE;
import static com.lineage.server.model.skill.L1SkillId.DARKNESS;
import java.util.Random;

import com.lineage.server.ActionCodes;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.datatables.WeaponSkillTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_UseAttackSkill;
import com.lineage.server.templates.L1ItemPower_name;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.timecontroller.pc.HardDelay;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.World;

/**
 * 武器技能
 * 
 * @author daien
 * 
 */
public class L1WeaponSkill {

    private static Random _random = new Random();

    private int _weaponId;

    private int _probability;

    private int _fixDamage;

    private int _randomDamage;

    private int _area;

    private int _skillId;

    private int _skillTime;

    private int _effectId;

    private int _effectTarget; // エフェクトの对象 0:相手 1:自分

    private boolean _isArrowType;

    private int _attr;

    public L1WeaponSkill(final int weaponId, final int probability,
            final int fixDamage, final int randomDamage, final int area,
            final int skillId, final int skillTime, final int effectId,
            final int effectTarget, final boolean isArrowType, final int attr) {
        this._weaponId = weaponId;
        this._probability = probability;
        this._fixDamage = fixDamage;
        this._randomDamage = randomDamage;
        this._area = area;
        this._skillId = skillId;
        this._skillTime = skillTime;
        this._effectId = effectId;
        this._effectTarget = effectTarget;
        this._isArrowType = isArrowType;
        this._attr = attr;
    }

    public int getWeaponId() {
        return this._weaponId;
    }

    public int getProbability() {
        return this._probability;
    }

    public int getFixDamage() {
        return this._fixDamage;
    }

    public int getRandomDamage() {
        return this._randomDamage;
    }

    public int getArea() {
        return this._area;
    }

    public int getSkillId() {
        return this._skillId;
    }

    public int getSkillTime() {
        return this._skillTime;
    }

    public int getEffectId() {
        return this._effectId;
    }

    public int getEffectTarget() {
        return this._effectTarget;
    }

    public boolean isArrowType() {
        return this._isArrowType;
    }

    public int getAttr() {
        return this._attr;
    }

    public static double getWeaponSkillDamage(final L1PcInstance pc,
            final L1Character cha, final int weaponId) {
        final L1WeaponSkill weaponSkill = WeaponSkillTable.get().getTemplate(
                weaponId);
        if ((pc == null) || (cha == null) || (weaponSkill == null)) {
            return 0;
        }

        final int _weaponEnchant = pc.getWeapon().getEnchantLevel();
        final L1ItemPower_name power_name = pc.getWeapon().get_power_name();
        final int chance = _random.nextInt(100) + 1;
        int _Probability = weaponSkill.getProbability();
        switch (weaponId) {
        case 54:
        case 58:
        case 203:
        case 205:
        	_Probability += _weaponEnchant;
        	break;
        case 76:
        case 121:
        	_Probability += _weaponEnchant << 1;
        	break;
        default:
        	break;
        }
        if (_Probability < chance) { //修改DB控制类魔法武器受武卷数变化机率 hjx1000
            return 0;
        }

        final int skillId = weaponSkill.getSkillId();
        if (skillId != 0) {
            final L1Skills skill = SkillsTable.get().getTemplate(skillId);
            if ((skill != null) && skill.getTarget().equals("buff")) {
                if (!isFreeze(cha)) { // 冻结状态orカウンターマジック中
                    cha.setSkillEffect(skillId,
                            weaponSkill.getSkillTime() * 1000);
                }
            }
        }

        final int effectId = weaponSkill.getEffectId();
        // 具有动画
        if (effectId > 0) {
            int chaId = 0;
            if (weaponSkill.getEffectTarget() == 0) {
                chaId = cha.getId();

            } else {
                chaId = pc.getId();
            }
            final boolean isArrowType = weaponSkill.isArrowType();
            if (!isArrowType) {
                pc.sendPacketsX8(new S_SkillSound(chaId, effectId));

            } else {
                final S_UseAttackSkill packet = new S_UseAttackSkill(pc,
                        cha.getId(), effectId, cha.getX(), cha.getY(),
                        ActionCodes.ACTION_Attack, false);
                pc.sendPacketsX8(packet);
            }
        }
        double damage = 0;
        final int randomDamage = weaponSkill.getRandomDamage();
        if (randomDamage != 0) {
            damage = _random.nextInt(randomDamage);
        }
        damage += weaponSkill.getFixDamage();
        if (power_name != null) { //星武器额外增加魔法武器的伤害
        	damage *= (double)power_name.get_xing_count() * 0.05 + 1;
        }
        final int area = weaponSkill.getArea();
        if ((area > 0) || (area == -1)) { // 范围技能
            for (final L1Object object : World.get().getVisibleObjects(cha,
                    area)) {
                if (object == null) {
                    continue;
                }
                if (!(object instanceof L1Character)) {
                    continue;
                }
                if (object.getId() == pc.getId()) {
                    continue;
                }
                if (object.getId() == cha.getId()) { // 攻击对象はL1Attackで处理するため除外
                    continue;
                }

                // 攻击对象がMOBの场合は、范围内のMOBにのみ当たる
                // 攻击对象がPC,Summon,Petの场合は、范围内のPC,Summon,Pet,MOBに当たる
                if (cha instanceof L1MonsterInstance) {
                    if (!(object instanceof L1MonsterInstance)) {
                        continue;
                    }
                }
                if ((cha instanceof L1PcInstance)
                        || (cha instanceof L1SummonInstance)
                        || (cha instanceof L1PetInstance)) {
                    if (!((object instanceof L1PcInstance)
                            || (object instanceof L1SummonInstance)
                            || (object instanceof L1PetInstance) || (object instanceof L1MonsterInstance))) {
                        continue;
                    }
                }

                damage = calcDamageReduction(pc, (L1Character) object, damage,
                        weaponSkill.getAttr());
                if (damage <= 0) {
                    continue;
                }
                if (object instanceof L1PcInstance) {
                    final L1PcInstance targetPc = (L1PcInstance) object;
                    // 受伤动作
                    targetPc.sendPacketsX8(new S_DoActionGFX(targetPc.getId(),
                            ActionCodes.ACTION_Damage));
                    targetPc.receiveDamage(pc, (int) damage, false, false);
                    if (!targetPc.isHardDelay()) { //动作延时 hjx1000
                    	HardDelay.onHardUse(targetPc, 150);
                    }

                } else if ((object instanceof L1SummonInstance)
                        || (object instanceof L1PetInstance)
                        || (object instanceof L1MonsterInstance)) {
                    final L1NpcInstance targetNpc = (L1NpcInstance) object;
                    // 受伤动作
                    targetNpc.broadcastPacketX8(new S_DoActionGFX(targetNpc
                            .getId(), ActionCodes.ACTION_Damage));
                    targetNpc.receiveDamage(pc, (int) damage);
                }
            }
        }

        return calcDamageReduction(pc, cha, damage, weaponSkill.getAttr());
    }

    /**
     * 巴风特魔杖
     * @param pc
     * @param cha
     * @return
     */
    public static double getBaphometStaffDamage(final L1PcInstance pc,
            final L1Character cha) {
        double dmg = 0;
        final int _weaponEnchant = pc.getWeapon().getEnchantLevel(); //使用武卷成功+1，魔法發動機率也會+1% hjx1000
        final int chance = _random.nextInt(100) + 1;
        final L1ItemPower_name power_name = pc.getWeapon().get_power_name();
        if (15 + _weaponEnchant >= chance) {
            final int locx = cha.getX();
            final int locy = cha.getY();
            final int sp = pc.getSp();
            final int intel = pc.getInt();
            double bsk = 0;
            if (pc.hasSkillEffect(BERSERKERS)) {
                bsk = 0.2;
            }
            dmg = (intel + sp) * (1.8 + bsk) + _random.nextInt(intel + sp)
                    * 2.5;
            if (power_name != null) { //星武器额外增加魔法武器的伤害
            	dmg *= (double)power_name.get_xing_count() * 0.05 + 1;
            }
            pc.sendPacketsAll(new S_EffectLocation(locx, locy, 129));
        }
        return calcDamageReduction(pc, cha, dmg, L1Skills.ATTR_EARTH);
    }

    /**
     * 圣晶魔杖
     * @param pc
     * @param cha
     * @return
     */
    public static double getHoly_CrystalStaffDamage(final L1PcInstance pc,
            final L1Character cha) {
        double dmg = 0;
        final int _weaponEnchant = pc.getWeapon().getEnchantLevel(); //使用武卷成功+1，魔法發動機率也會+1% hjx1000
        final int chance = _random.nextInt(100) + 1;
        final L1ItemPower_name power_name = pc.getWeapon().get_power_name();
        if (14 + _weaponEnchant >= chance) {
            final int locx = cha.getX();
            final int locy = cha.getY();
            final int sp = pc.getSp();
            final int intel = pc.getInt();
            double bsk = 0;
            if (pc.hasSkillEffect(BERSERKERS)) {
                bsk = 0.2;
            }
            dmg = (intel + sp) * (1.8 + bsk) + _random.nextInt(intel + sp)
                    * 3.5;
            if (power_name != null) { //星武器额外增加魔法武器的伤害
            	dmg *= (double)power_name.get_xing_count() * 0.05 + 1;
            }
            pc.sendPacketsAll(new S_EffectLocation(locx, locy, 5889));
        }
        return calcDamageReduction(pc, cha, dmg, L1Skills.ATTR_WIND);
    }
    
    public static double getDiceDaggerDamage(final L1PcInstance pc,
            final L1PcInstance targetPc, final L1ItemInstance weapon) {
        double dmg = 0;
        final int chance = _random.nextInt(100) + 1;
        if (2 >= chance) {
            dmg = targetPc.getCurrentHp() * 2 / 3;
            if (targetPc.getCurrentHp() - dmg < 0) {
                dmg = 0;
            }
            final String msg = weapon.getLogName();
            pc.sendPackets(new S_ServerMessage(158, msg));
            // \f1%0が蒸発してなくなりました。
            pc.getInventory().removeItem(weapon, 1);
        }
        return dmg;
    }

    /**
     * 冰之女王魔杖 add hjx1000
     * 
     * @param pc
     * @param targetPc
     * @param weapon
     * @return
     */
    public static double getThe_ice_queenDamage(final L1PcInstance pc,
            final L1Character cha) {
        double dmg = 0;
        final int _weaponEnchant = pc.getWeapon().getEnchantLevel() << 1; //使用武卷成功+1，魔法發動機率也會+2% hjx1000
        final int chance = _random.nextInt(100) + 1;
        final L1ItemPower_name power_name = pc.getWeapon().get_power_name();
        if (7 + _weaponEnchant >= chance) {
            final int locx = cha.getX();
            final int locy = cha.getY();
            final int sp = pc.getSp();
            final int intel = pc.getInt();
            double bsk = 0;
            if (pc.hasSkillEffect(BERSERKERS)) {
                bsk = 0.2;
            }
            dmg = (intel + sp) * (1.8 + bsk) + _random.nextInt(intel + sp)
                    * 1.5;
            if (power_name != null) { //星武器额外增加魔法武器的伤害
            	dmg *= (double)power_name.get_xing_count() * 0.05 + 1;
            }
            pc.sendPacketsAll(new S_EffectLocation(locx, locy, 1810));
        }
        return calcDamageReduction(pc, cha, dmg, L1Skills.ATTR_WATER);
    }
    
    /**
     * 席琳的拐魔杖 add hjx1000
     * 
     * @param pc
     * @param targetPc
     * @param weapon
     * @return
     */
    public static double getcelion_queenDamage(final L1PcInstance pc,
            final L1Character cha) {
        double dmg = 0;
        final int _weaponEnchant = pc.getWeapon().getEnchantLevel();
        final int chance = _random.nextInt(100) + 1;
        final L1ItemPower_name power_name = pc.getWeapon().get_power_name();
        if (7 >= chance) {
            final int locx = cha.getX();
            final int locy = cha.getY();
            final int addDmg = pc.getSp() + pc.getInt();
            double bsk = 0;
            if (pc.hasSkillEffect(BERSERKERS)) {
                bsk = 0.2;
            }
            dmg = addDmg * (1.8 + bsk) + _random.nextInt(_weaponEnchant * addDmg)
                    * 1.5;
            if (power_name != null) { //星武器额外增加魔法武器的伤害
            	dmg *= (double)power_name.get_xing_count() * 0.05 + 1;
            }
            pc.sendPacketsAll(new S_EffectLocation(locx, locy, 1810));
        }
        return calcDamageReduction(pc, cha, dmg, L1Skills.ATTR_WATER);
    }

    /**
     * 奇古兽伤害计算
     * 
     * @param pc
     * @param cha
     * @return
     */
    public static double getKiringkuDamage(final L1PcInstance pc,
            final L1Character cha) {
        int dmg = 0;
        final int dice = 5;
        final int diceCount = 2;
        final int value = 8;
        final L1ItemInstance weapon = pc.getWeapon();
        //final L1ItemPower_name power_name = pc.getWeapon().get_power_name();
        int kiringkuDamage = 0;
        int charaIntelligence = 0;
        // final int getTargetMr = 0;
        // XXX ダイスと值は本来、キーリンク每に违うが不明な为、判明しているDSKのものに固定。

        for (int i = 0; i < diceCount; i++) {
            kiringkuDamage += (_random.nextInt(dice) + 1);
        }
        kiringkuDamage += value;

        final int spByItem = pc.getSp() - pc.getTrueSp();
        charaIntelligence = pc.getInt() + spByItem - 12;
        if (charaIntelligence < 1) {
            charaIntelligence = 1;
        }
        final double kiringkuCoefficientA = (1.0 + charaIntelligence * 3.0 / 32.0);

        kiringkuDamage *= kiringkuCoefficientA;

        final double kiringkuFloor = Math.floor(kiringkuDamage);

        dmg += kiringkuFloor + weapon.getItem().getMagicDmgModifier();

        switch (pc.getWeapon().getItem().getGfxId()) {
            case 3018:// 蓝宝石奇古兽
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 6983));
                break;

            default:
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7049));
                break;
        }

        return calcDamageReduction(pc, cha, dmg, 0);
    }

    public static double getAreaSkillWeaponDamage(final L1PcInstance pc,
            final L1Character cha, final int weaponId) {
        double dmg = 0;
        int probability = 0;
        int attr = 0;
        final int chance = _random.nextInt(100) + 1;
        final L1ItemPower_name power_name = pc.getWeapon().get_power_name();
        final int _weaponEnchant = pc.getWeapon().getEnchantLevel(); //使用武卷成功+1，魔法發動機率也會+1% hjx1000
        if (weaponId == 263) { // フリージングランサー
            probability = 5;
            attr = L1Skills.ATTR_WATER;
        } else if (weaponId == 260) { // レイジングウィンド
            probability = 4;
            attr = L1Skills.ATTR_WIND;
        }
        if (probability + _weaponEnchant >= chance) {
            final int sp = pc.getSp();
            final int intel = pc.getInt();
            int area = 0;
            int effectTargetId = 0;
            int effectId = 0;
            L1Character areaBase = cha;
            double damageRate = 0;

            if (weaponId == 263) { // フリージングランサー
                area = 3;
                damageRate = 1.4D;
                effectTargetId = cha.getId();
                effectId = 1804;
                areaBase = cha;

            } else if (weaponId == 260) { // レイジングウィンド
                area = 4;
                damageRate = 1.5D;
                effectTargetId = pc.getId();
                effectId = 758;
                areaBase = pc;
            }
            double bsk = 0;
            if (pc.hasSkillEffect(BERSERKERS)) {
                bsk = 0.2;
            }
            dmg = (intel + sp) * (damageRate + bsk)
                    + _random.nextInt(intel + sp) * damageRate;
            if (power_name != null) { //星武器额外增加魔法武器的伤害
            	dmg *= (double)power_name.get_xing_count() * 0.05 + 1;
            }
            pc.sendPacketsX8(new S_SkillSound(effectTargetId, effectId));

            for (final L1Object object : World.get().getVisibleObjects(
                    areaBase, area)) {
                if (object == null) {
                    continue;
                }
                if (!(object instanceof L1Character)) {
                    continue;
                }
                if (object.getId() == pc.getId()) {
                    continue;
                }
                if (object.getId() == cha.getId()) { // 攻击对象は除外
                    continue;
                }

                // 攻击对象がMOBの场合は、范围内のMOBにのみ当たる
                // 攻击对象がPC,Summon,Petの场合は、范围内のPC,Summon,Pet,MOBに当たる
                if (cha instanceof L1MonsterInstance) {
                    if (!(object instanceof L1MonsterInstance)) {
                        continue;
                    }
                }
                if ((cha instanceof L1PcInstance)
                        || (cha instanceof L1SummonInstance)
                        || (cha instanceof L1PetInstance)) {
                    if (!((object instanceof L1PcInstance)
                            || (object instanceof L1SummonInstance)
                            || (object instanceof L1PetInstance) || (object instanceof L1MonsterInstance))) {
                        continue;
                    }
                }

                dmg = calcDamageReduction(pc, (L1Character) object, dmg, attr);
                if (dmg <= 0) {
                    continue;
                }
                if (object instanceof L1PcInstance) {
                    final L1PcInstance targetPc = (L1PcInstance) object;
                    // 受伤动作
                    targetPc.sendPacketsX8(new S_DoActionGFX(targetPc.getId(),
                            ActionCodes.ACTION_Damage));
                    if (!targetPc.isHardDelay()) { //动作延时 hjx1000
                    	HardDelay.onHardUse(targetPc, 150);
                    }
                    targetPc.receiveDamage(pc, (int) dmg, false, false);

                } else if ((object instanceof L1SummonInstance)
                        || (object instanceof L1PetInstance)
                        || (object instanceof L1MonsterInstance)) {
                    final L1NpcInstance targetNpc = (L1NpcInstance) object;
                    // 受伤动作
                    targetNpc.broadcastPacketX8(new S_DoActionGFX(targetNpc
                            .getId(), ActionCodes.ACTION_Damage));
                    targetNpc.receiveDamage(pc, (int) dmg);
                }
            }
        }
        return calcDamageReduction(pc, cha, dmg, attr);
    }

    public static double getLightningEdgeDamage(final L1PcInstance pc,
            final L1Character cha) {
        double dmg = 0;
        final int chance = _random.nextInt(100) + 1;
        final L1ItemPower_name power_name = pc.getWeapon().get_power_name();
        final int _weaponEnchant = pc.getWeapon().getEnchantLevel(); //使用武卷成功+1，魔法發動機率也會+1% hjx1000
        if (4 + _weaponEnchant >= chance) {
            final int sp = pc.getSp();
            final int intel = pc.getInt();
            double bsk = 0;
            if (pc.hasSkillEffect(BERSERKERS)) {
                bsk = 0.2;
            }
            dmg = (intel + sp) * (2 + bsk) + _random.nextInt(intel + sp) * 2;
            if (power_name != null) { //星武器额外增加魔法武器的伤害
            	dmg *= (double)power_name.get_xing_count() * 0.05 + 1;
            }

            pc.sendPacketsX8(new S_SkillSound(cha.getId(), 10));
        }
        return calcDamageReduction(pc, cha, dmg, L1Skills.ATTR_WIND);
    }
    
    /**
     * 极寒锁链剑
     * @param pc
     * @param cha
     * @return
     */
    public static double getChaserDamageliliang(final L1PcInstance pc,
            final L1Character cha) {
        double dmg = 0;
        final int chance = _random.nextInt(100) + 1;
        final L1ItemPower_name power_name = pc.getWeapon().get_power_name();
        final int _weaponEnchant = pc.getWeapon().getEnchantLevel(); //使用武卷成功+1，魔法發動機率也會+1% hjx1000
        if (1 + _weaponEnchant >= chance) {
            final int sp = pc.getSp();
            final int intel = pc.getInt();
            double bsk = 0;
            if (pc.hasSkillEffect(BERSERKERS)) {
                bsk = 0.2;
            }
            dmg = (intel + sp) * (2 + bsk) + _random.nextInt(intel + sp) * 2;
            if (power_name != null) { //星武器额外增加魔法武器的伤害
            	dmg *= (double)power_name.get_xing_count() * 0.05 + 1;
            }

            pc.sendPacketsX8(new S_SkillSound(cha.getId(), 3685));
        }
        return calcDamageReduction(pc, cha, dmg, L1Skills.ATTR_WATER);
    }
    
    /**
     * 极寒奇古兽
     * @param pc
     * @param cha
     */
    public static double getMindBreak(L1PcInstance pc, L1Character cha) {//趁茄狼虐傅农
        double dmg = 0;
        final int chance = _random.nextInt(100) + 1;
        final L1ItemPower_name power_name = pc.getWeapon().get_power_name();
        final int _weaponEnchant = pc.getWeapon().getEnchantLevel(); //使用武卷成功+1，魔法發動機率也會+1% hjx1000
        if (1 + _weaponEnchant >= chance) {
            final int sp = pc.getSp();
            final int intel = pc.getInt();
            double bsk = 0;
            if (pc.hasSkillEffect(BERSERKERS)) {
                bsk = 0.2;
            }
            dmg = (intel + sp) * (2 + bsk) + _random.nextInt(intel + sp) * 2.5D;
            if (power_name != null) { //星武器额外增加魔法武器的伤害
            	dmg *= (double)power_name.get_xing_count() * 0.05 + 1;
            }

            pc.sendPacketsX8(new S_SkillSound(cha.getId(), 6553));
        }
        return calcDamageReduction(pc, cha, dmg, L1Skills.ATTR_WATER);
    }

    public static void giveArkMageDiseaseEffect(final L1PcInstance pc,
            final L1Character cha) {
        final int chance = _random.nextInt(1000) + 1;
        int probability = (5 - ((cha.getMr() / 10) * 5)) * 10;
        if (probability == 0) {
            probability = 10;
        }
        if (probability >= chance) {
            final L1SkillUse l1skilluse = new L1SkillUse();
            l1skilluse.handleCommands(pc, L1SkillId.DISEASE, cha.getId(),
                    cha.getX(), cha.getY(), 0, L1SkillUse.TYPE_GMBUFF);
        }
    }

    /**
     * 深红之弩冻结
     * 
     * @param pc
     * @param cha
     */
    public static void giveFettersEffect(final L1PcInstance pc,
            final L1Character cha) {
        final int fettersTime = 4000;//4秒
        if (isFreeze(cha)) { // 冻结状态orカウンターマジック中
            return;
        }
        if ((_random.nextInt(100) + 1) <= 2) {
            L1SpawnUtil.spawnEffect(81182, fettersTime, cha.getX(), cha.getY(),
                    cha.getMapId(), cha, 0);
            if (cha instanceof L1PcInstance) {
                final L1PcInstance targetPc = (L1PcInstance) cha;
                targetPc.setSkillEffect(STATUS_FREEZE, fettersTime);
                targetPc.sendPacketsX8(new S_SkillSound(targetPc.getId(), 4184));

                targetPc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND,
                        true));

            } else if ((cha instanceof L1MonsterInstance)
                    || (cha instanceof L1SummonInstance)
                    || (cha instanceof L1PetInstance)) {
                final L1NpcInstance npc = (L1NpcInstance) cha;
                npc.setSkillEffect(DARKNESS, fettersTime); //修正定怪 hjx1000
                npc.broadcastPacketX8(new S_SkillSound(npc.getId(), 4184));
                //npc.setParalyzed(true);
            }
        }
    }

    public static double calcDamageReduction(final L1PcInstance pc,
            final L1Character cha, double dmg, final int attr) {
        // 冻结状态orカウンターマジック中
        if (isFreeze(cha)) {
            return 0;
        }

        // MRによるダメージ轻减
        final int mr = cha.getMr();
        double mrFloor = 0;
        if (mr <= 100) {
            mrFloor = Math.floor((mr - pc.getOriginalMagicHit()) / 2);
        } else if (mr >= 100) {
            mrFloor = Math.floor((mr - pc.getOriginalMagicHit()) / 10);
        }
        double mrCoefficient = 0;
        if (mr <= 100) {
            mrCoefficient = 1 - 0.01 * mrFloor;
        } else if (mr >= 100) {
            mrCoefficient = 0.6 - 0.01 * mrFloor;
        }
        dmg *= mrCoefficient;

        // 属性によるダメージ轻减
        int resist = 0;
        if (attr == L1Skills.ATTR_EARTH) {
            resist = cha.getEarth();
        } else if (attr == L1Skills.ATTR_FIRE) {
            resist = cha.getFire();
        } else if (attr == L1Skills.ATTR_WATER) {
            resist = cha.getWater();
        } else if (attr == L1Skills.ATTR_WIND) {
            resist = cha.getWind();
        }
        int resistFloor = (int) (0.32 * Math.abs(resist));
        if (resist >= 0) {
            resistFloor *= 1;
        } else {
            resistFloor *= -1;
        }
        final double attrDeffence = resistFloor / 32.0;
        dmg = (1.0 - attrDeffence) * dmg;

        return dmg;
    }

    /**
     * 冻结中
     * 
     * @param cha
     * @return
     */
    public static boolean isFreeze(final L1Character cha) {
        if (cha.hasSkillEffect(STATUS_FREEZE)) {
            return true;
        }
        if (cha.hasSkillEffect(ABSOLUTE_BARRIER)) {
            return true;
        }
        if (cha.hasSkillEffect(ICE_LANCE)) {
            return true;
        }
        if (cha.hasSkillEffect(FREEZING_BLIZZARD)) {
            return true;
        }
        if (cha.hasSkillEffect(FREEZING_BREATH)) {
            return true;
        }
        if (cha.hasSkillEffect(EARTH_BIND)) {
            return true;
        }

        // カウンターマジック判定
        if (cha.hasSkillEffect(COUNTER_MAGIC)) {
            cha.removeSkillEffect(COUNTER_MAGIC);
            final int castgfx = SkillsTable.get().getTemplate(COUNTER_MAGIC)
                    .getCastGfx();
            cha.broadcastPacketX8(new S_SkillSound(cha.getId(), castgfx));
            if (cha instanceof L1PcInstance) {
                final L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), castgfx));
            }
            return true;
        }
        return false;
    }
}
