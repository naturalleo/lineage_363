package com.lineage.server.model;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.PolyTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ChangeShape;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 变形控制项
 * 
 * @author daien
 * 
 */
public class L1PolyMorph {

    private static final Log _log = LogFactory.getLog(L1PolyMorph.class);

    // weapon equip bit(2047)
    private static final int DAGGER_EQUIP = 1;

    private static final int SWORD_EQUIP = 2;

    private static final int TWOHANDSWORD_EQUIP = 4;

    private static final int AXE_EQUIP = 8;

    private static final int SPEAR_EQUIP = 16;

    private static final int STAFF_EQUIP = 32;

    private static final int EDORYU_EQUIP = 64;

    private static final int CLAW_EQUIP = 128;

    private static final int BOW_EQUIP = 256; // ガントレット含む

    private static final int KIRINGKU_EQUIP = 512;

    private static final int CHAINSWORD_EQUIP = 1024;

    // armor equip bit
    private static final int HELM_EQUIP = 1;

    private static final int AMULET_EQUIP = 2;

    private static final int EARRING_EQUIP = 4;

    private static final int TSHIRT_EQUIP = 8;

    private static final int ARMOR_EQUIP = 16;

    private static final int CLOAK_EQUIP = 32;

    private static final int BELT_EQUIP = 64;

    private static final int SHIELD_EQUIP = 128;

    private static final int GLOVE_EQUIP = 256;

    private static final int RING_EQUIP = 512;

    private static final int BOOTS_EQUIP = 1024;

    private static final int GUARDER_EQUIP = 2048;

    // 变身の原因を示すbit
    public static final int MORPH_BY_ITEMMAGIC = 1;

    public static final int MORPH_BY_GM = 2;

    public static final int MORPH_BY_NPC = 4; // 占星术师ケプリシャ以外のNPC

    public static final int MORPH_BY_KEPLISHA = 8;

    public static final int MORPH_BY_LOGIN = 0;

    private static final Map<Integer, Integer> weaponFlgMap = new HashMap<Integer, Integer>();
    static {
        weaponFlgMap.put(1, SWORD_EQUIP);// 剑
        weaponFlgMap.put(2, DAGGER_EQUIP);// 匕首
        weaponFlgMap.put(3, TWOHANDSWORD_EQUIP);// 双手剑
        weaponFlgMap.put(4, BOW_EQUIP);// 弓
        weaponFlgMap.put(5, SPEAR_EQUIP);// 矛(双手)
        weaponFlgMap.put(6, AXE_EQUIP);// 斧(单手)
        weaponFlgMap.put(7, STAFF_EQUIP);// 魔杖
        weaponFlgMap.put(8, BOW_EQUIP);// 飞刀
        weaponFlgMap.put(9, BOW_EQUIP);// 箭
        weaponFlgMap.put(10, BOW_EQUIP);// 铁手甲
        weaponFlgMap.put(11, CLAW_EQUIP);// 钢爪
        weaponFlgMap.put(12, EDORYU_EQUIP);// 双刀
        weaponFlgMap.put(13, BOW_EQUIP);// 弓(单手)
        weaponFlgMap.put(14, SPEAR_EQUIP);// 矛(单手)
        weaponFlgMap.put(15, AXE_EQUIP);// 双手斧
        weaponFlgMap.put(16, STAFF_EQUIP);// 魔杖(双手)
        weaponFlgMap.put(17, KIRINGKU_EQUIP);// 奇古兽
        weaponFlgMap.put(18, CHAINSWORD_EQUIP);// 锁链剑
    }
    private static final Map<Integer, Integer> armorFlgMap = new HashMap<Integer, Integer>();
    static {
        armorFlgMap.put(1, HELM_EQUIP);
        armorFlgMap.put(2, ARMOR_EQUIP);
        armorFlgMap.put(3, TSHIRT_EQUIP);
        armorFlgMap.put(4, CLOAK_EQUIP);
        armorFlgMap.put(5, GLOVE_EQUIP);
        armorFlgMap.put(6, BOOTS_EQUIP);
        armorFlgMap.put(7, SHIELD_EQUIP);
        armorFlgMap.put(8, AMULET_EQUIP);
        armorFlgMap.put(9, RING_EQUIP);
        armorFlgMap.put(10, BELT_EQUIP);
        armorFlgMap.put(12, EARRING_EQUIP);
        armorFlgMap.put(13, GUARDER_EQUIP);
    }

    private int _id;
    private String _name;
    private int _polyId;
    private int _minLevel;
    private int _weaponEquipFlg;
    private int _armorEquipFlg;
    private boolean _canUseSkill;
    private int _causeFlg;

    public L1PolyMorph(final int id, final String name, final int polyId,
            final int minLevel, final int weaponEquipFlg,
            final int armorEquipFlg, final boolean canUseSkill,
            final int causeFlg) {
        this._id = id;
        this._name = name;
        this._polyId = polyId;
        this._minLevel = minLevel;
        this._weaponEquipFlg = weaponEquipFlg;
        this._armorEquipFlg = armorEquipFlg;
        this._canUseSkill = canUseSkill;
        this._causeFlg = causeFlg;
    }

    public int getId() {
        return this._id;
    }

    public String getName() {
        return this._name;
    }

    public int getPolyId() {
        return this._polyId;
    }

    public int getMinLevel() {
        return this._minLevel;
    }

    public int getWeaponEquipFlg() {
        return this._weaponEquipFlg;
    }

    public int getArmorEquipFlg() {
        return this._armorEquipFlg;
    }

    public boolean canUseSkill() {
        return this._canUseSkill;
    }

    public int getCauseFlg() {
        return this._causeFlg;
    }

    public static void handleCommands(final L1PcInstance pc, final String s) {
        if ((pc == null) || pc.isDead()) {
            return;
        }
        final L1PolyMorph poly = PolyTable.get().getTemplate(s);
        if ((poly != null) || s.equals("none")) {
            if (s.equals("none")) {
                if ((pc.getTempCharGfx() == 6034)
                        || (pc.getTempCharGfx() == 6035)) {
                } else {
                    pc.removeSkillEffect(L1SkillId.SHAPE_CHANGE);
                    pc.sendPackets(new S_CloseList(pc.getId()));
                }

            } else if ((pc.getLevel() >= poly.getMinLevel()) || pc.isGm()) {
                if ((pc.getTempCharGfx() == 6034)
                        || (pc.getTempCharGfx() == 6035)) {
                    // 181:\f1无法变成你指定的怪物。
                    pc.sendPackets(new S_ServerMessage(181));

                } else {
                    doPoly(pc, poly.getPolyId(), 7200, MORPH_BY_ITEMMAGIC);
                    pc.sendPackets(new S_CloseList(pc.getId()));
                }

            } else {
                // 181:\f1无法变成你指定的怪物。
                pc.sendPackets(new S_ServerMessage(181));
            }
        }
    }

    /**
     * 执行变身
     * 
     * @param cha
     * @param polyId
     * @param timeSecs
     * @param cause
     */
    public static void doPoly(final L1Character cha, final int polyId,
            final int timeSecs, final int cause) {
        try {
            if ((cha == null) || cha.isDead()) {
                return;
            }
            // 变身禁止使用
            if (cha.hasItemDelay(L1ItemDelay.POLY) == true) {
                return;
            }
            if (cha instanceof L1PcInstance) {
                final L1PcInstance pc = (L1PcInstance) cha;
                if (pc.getMapId() == 5300) { // 钓鱼池(5124)
                    // 这里不可以变身。
                    pc.sendPackets(new S_ServerMessage(1170));
                    return;
                }
                if (pc.getMapId() == 9000) { // 从前的说话之岛
                    // 这里不可以变身。
                    pc.sendPackets(new S_ServerMessage(1170));
                    return;
                }
                if (pc.getMapId() == 9100) { // 象牙塔的秘密研究室
                    // 这里不可以变身。
                    pc.sendPackets(new S_ServerMessage(1170));
                    return;
                }
                if ((pc.getTempCharGfx() == 6034)
                        || (pc.getTempCharGfx() == 6035)) {
                    // 181:\f1无法变成你指定的怪物。
                    pc.sendPackets(new S_ServerMessage(181));
                    return;
                }
                if (!isMatchCause(polyId, cause)) {
                    // 181:\f1无法变成你指定的怪物。
                    pc.sendPackets(new S_ServerMessage(181));
                    return;
                }

                pc.killSkillEffectTimer(L1SkillId.SHAPE_CHANGE);
                pc.setSkillEffect(L1SkillId.SHAPE_CHANGE, timeSecs * 1000);

                if (pc.getTempCharGfx() != polyId) { // 同じ变身の场合はアイコン送信以外が必要ない
                    L1ItemInstance weapon = pc.getWeapon();
                    // 变身によって武器が外れるか
                    final boolean weaponTakeoff = ((weapon != null) && !isEquipableWeapon(
                            polyId, weapon.getItem().getType()));
                    pc.setTempCharGfx(polyId);
                    pc.sendPackets(new S_ChangeShape(pc, polyId, weaponTakeoff));
                    if (!pc.isGmInvis() && !pc.isInvisble()) {
                        pc.broadcastPacketAll(new S_ChangeShape(pc, polyId));
                    }

                    pc.getInventory().takeoffEquip(polyId);
                    weapon = pc.getWeapon();

                    if (weapon != null) {
                        pc.sendPacketsAll(new S_CharVisualUpdate(pc));
                    }
                }
                pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_POLYMORPH,
                        timeSecs));

            } else if (cha instanceof L1MonsterInstance) {
                final L1MonsterInstance mob = (L1MonsterInstance) cha;
                mob.killSkillEffectTimer(L1SkillId.SHAPE_CHANGE);
                mob.setSkillEffect(L1SkillId.SHAPE_CHANGE, timeSecs * 1000);
                if (mob.getTempCharGfx() != polyId) { // 同じ变身の场合はアイコン送信以外が必要ない
                    mob.setTempCharGfx(polyId);
                    mob.broadcastPacketAll(new S_ChangeShape(mob, polyId));
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 解除变身
     * 
     * @param cha
     */
    public static void undoPoly(final L1Character cha) {
        try {
            if (cha instanceof L1PcInstance) {
                final L1PcInstance pc = (L1PcInstance) cha;
                final int classId = pc.getClassId();
                pc.setTempCharGfx(classId);
                pc.sendPacketsAll(new S_ChangeShape(pc, classId));
                final L1ItemInstance weapon = pc.getWeapon();
                if (weapon != null) {
                    pc.sendPacketsAll(new S_CharVisualUpdate(pc));
                }

            } else if (cha instanceof L1MonsterInstance) {
                final L1MonsterInstance mob = (L1MonsterInstance) cha;
                mob.setTempCharGfx(0);
                mob.broadcastPacketAll(new S_ChangeShape(mob, mob.getGfxId()));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    // 指定したpolyIdがweapontTypeの武器を装备出来るか？
    public static boolean isEquipableWeapon(final int polyId,
            final int weaponType) {
        try {
            final L1PolyMorph poly = PolyTable.get().getTemplate(polyId);
            if (poly == null) {
                return true;
            }

            final Integer flg = weaponFlgMap.get(weaponType);
            if (flg != null) {
                return 0 != (poly.getWeaponEquipFlg() & flg);
            }
            return true;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return true;
    }

    // 指定したpolyIdがarmorTypeの防具を装备出来るか？
    public static boolean isEquipableArmor(final int polyId, final int armorType) {
        try {
            final L1PolyMorph poly = PolyTable.get().getTemplate(polyId);
            if (poly == null) {
                return true;
            }

            final Integer flg = armorFlgMap.get(armorType);
            if (flg != null) {
                return 0 != (poly.getArmorEquipFlg() & flg);
            }
            return true;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return true;
    }

    // 指定したpolyIdが何によって变身し、それが变身させられるか？
    public static boolean isMatchCause(final int polyId, final int cause) {
        try {
            final L1PolyMorph poly = PolyTable.get().getTemplate(polyId);
            if (poly == null) {
                return true;
            }
            if (cause == MORPH_BY_LOGIN) {
                return true;
            }
            return 0 != (poly.getCauseFlg() & cause);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return false;
    }
}
