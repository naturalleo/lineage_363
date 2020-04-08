package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.data.ItemClass;
import com.lineage.data.item_armor.set.ArmorSet;
import com.lineage.server.IdFactory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.templates.L1Armor;
import com.lineage.server.templates.L1EtcItem;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1Weapon;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;

/**
 * 道具,武器,防具资料
 * 
 * @author dexc
 * 
 */
public class ItemTable {

    private static final Log _log = LogFactory.getLog(ItemTable.class);

    // 防具类型核心分类
    private static final Map<String, Integer> _armorTypes = new HashMap<String, Integer>();

    // 武器类型核心分类
    private static final Map<String, Integer> _weaponTypes = new HashMap<String, Integer>();

    // 武器类型触发事件
    private static final Map<String, Integer> _weaponId = new HashMap<String, Integer>();

    // 材质类型核心分类
    private static final Map<String, Integer> _materialTypes = new HashMap<String, Integer>();

    // 道具类型核心分类
    private static final Map<String, Integer> _etcItemTypes = new HashMap<String, Integer>();

    // 道具类型触发事件
    private static final Map<String, Integer> _useTypes = new HashMap<String, Integer>();

    private static ItemTable _instance;

    private L1Item _allTemplates[];

    private static Map<Integer, L1EtcItem> _etcitems;

    private static Map<Integer, L1Armor> _armors;

    private static Map<Integer, L1Weapon> _weapons;

    static {
        // 物品类型
        _etcItemTypes.put("arrow", new Integer(0));// 箭
        _etcItemTypes.put("wand", new Integer(1));// 魔杖
        _etcItemTypes.put("light", new Integer(2));// 照明
        _etcItemTypes.put("gem", new Integer(3));// 宝石
        _etcItemTypes.put("totem", new Integer(4));// 图腾
        _etcItemTypes.put("firecracker", new Integer(5));// 烟火
        _etcItemTypes.put("potion", new Integer(6));// 药水
        _etcItemTypes.put("food", new Integer(7));// 食物
        _etcItemTypes.put("scroll", new Integer(8));// 卷轴
        _etcItemTypes.put("questitem", new Integer(9));// 任务物品
        _etcItemTypes.put("spellbook", new Integer(10));// 魔法书
        _etcItemTypes.put("petitem", new Integer(11));// 宠物物品
        _etcItemTypes.put("other", new Integer(12));// 其他
        _etcItemTypes.put("material", new Integer(13));// 材料
        _etcItemTypes.put("event", new Integer(14));// 活动物品
        _etcItemTypes.put("sting", new Integer(15));// 飞刀
        _etcItemTypes.put("treasure_box", new Integer(16));// 宝盒

        // 物品使用封包类型
        _useTypes.put("petitem", new Integer(-12)); // 宠物道具
        _useTypes.put("other", new Integer(-11)); // 对读取方法调用无法分类的物品
        _useTypes.put("power", new Integer(-10)); // 加速药水
        _useTypes.put("book", new Integer(-9)); // 技术书
        _useTypes.put("makecooking", new Integer(-8));// 料理书
        _useTypes.put("hpr", new Integer(-7));// 增HP道具
        _useTypes.put("mpr", new Integer(-6));// 增MP道具
        _useTypes.put("ticket", new Integer(-5)); // 食人妖精竞赛票/死亡竞赛票/彩票
        _useTypes.put("petcollar", new Integer(-4)); // 项圈
        _useTypes.put("sting", new Integer(-3)); // 飞刀
        _useTypes.put("arrow", new Integer(-2)); // 箭
        _useTypes.put("none", new Integer(-1)); // 无法使用(材料等)
        _useTypes.put("normal", new Integer(0));// 一般物品
        _useTypes.put("weapon", new Integer(1));// 武器
        _useTypes.put("armor", new Integer(2));// 盔甲
        _useTypes.put("spell_1", new Integer(3)); // 创造怪物魔杖(无须选取目标 -
                                                  // 无数量:没有任何事情发生)
        _useTypes.put("4", new Integer(4)); // 希望魔杖 XXX
        _useTypes.put("spell_long", new Integer(5)); // 魔杖类型(须选取目标/座标)
        _useTypes.put("ntele", new Integer(6));// 瞬间移动卷轴
        _useTypes.put("identify", new Integer(7));// 鉴定卷轴
        _useTypes.put("res", new Integer(8));// 复活卷轴
        _useTypes.put("home", new Integer(9)); // 传送回家的卷轴
        _useTypes.put("light", new Integer(10)); // 照明道具
        _useTypes.put("11", new Integer(11)); // 未分类的卷轴 XXX
        _useTypes.put("letter", new Integer(12));// 信纸
        _useTypes.put("letter_card", new Integer(13)); // 信纸(寄出)
        _useTypes.put("choice", new Integer(14));// 请选择一个物品(道具栏位)
        _useTypes.put("instrument", new Integer(15));// 哨子
        _useTypes.put("sosc", new Integer(16));// 变形卷轴
        _useTypes.put("spell_short", new Integer(17)); // 选取目标 (近距离)
        _useTypes.put("T", new Integer(18));// T恤
        _useTypes.put("cloak", new Integer(19));// 斗篷
        _useTypes.put("glove", new Integer(20)); // 手套
        _useTypes.put("boots", new Integer(21));// 靴
        _useTypes.put("helm", new Integer(22));// 头盔
        _useTypes.put("ring", new Integer(23));// 戒指
        _useTypes.put("amulet", new Integer(24));// 项链
        _useTypes.put("shield", new Integer(25));// 盾牌
        _useTypes.put("guarder", new Integer(25));// 臂甲
        _useTypes.put("dai", new Integer(26));// 对武器施法的卷轴
        _useTypes.put("zel", new Integer(27));// 对盔甲施法的卷轴
        _useTypes.put("blank", new Integer(28));// 空的魔法卷轴
        _useTypes.put("btele", new Integer(29));// 瞬间移动卷轴(祝福)
        _useTypes.put("spell_buff", new Integer(30)); // 魔法卷轴选取目标 (远距离 无XY座标传回)
        _useTypes.put("ccard", new Integer(31));// 圣诞卡片
        _useTypes.put("ccard_w", new Integer(32));// 圣诞卡片(寄出)
        _useTypes.put("vcard", new Integer(33));// 情人节卡片
        _useTypes.put("vcard_w", new Integer(34));// 情人节卡片(寄出)
        _useTypes.put("wcard", new Integer(35));// 白色情人节卡片
        _useTypes.put("wcard_w", new Integer(36));// 白色情人节卡片(寄出)
        _useTypes.put("belt", new Integer(37));// 腰带
        _useTypes.put("food", new Integer(38)); // 食物
        _useTypes.put("spell_long2", new Integer(39)); // 选取目标 (远距离)
        _useTypes.put("earring", new Integer(40)); // 耳环
        _useTypes.put("fishing_rod", new Integer(42));// 钓鱼杆
        // _useTypes.put("aid", new Integer(44)); // 副助道具
        _useTypes.put("enc", new Integer(46)); // 饰品强化卷轴

        // _useTypes.put("aidr", new Integer(43));// 3.5TW辅助右
        // _useTypes.put("aidl", new Integer(44));// 3.5TW辅助左
        // _useTypes.put("aidm", new Integer(45));// 3.5TW辅助中
        // _useTypes.put("aidr2", new Integer(48));// 3.5TW辅助右下
        // _useTypes.put("aidl2", new Integer(47));// 3.5TW辅助左下

        // 从左向右
        _useTypes.put("aidr", new Integer(43));// 3.63TW辅助1
        _useTypes.put("aidl", new Integer(44));

        _useTypes.put("choice_doll", new Integer(55));// 请选择魔法娃娃

        _armorTypes.put("none", new Integer(0));
        _armorTypes.put("helm", new Integer(1));// 头盔
        _armorTypes.put("armor", new Integer(2));// 盔甲
        _armorTypes.put("T", new Integer(3));// 内衣
        _armorTypes.put("cloak", new Integer(4));// 斗篷
        _armorTypes.put("glove", new Integer(5));// 手套
        _armorTypes.put("boots", new Integer(6));// 长靴
        _armorTypes.put("shield", new Integer(7));// 盾牌
        _armorTypes.put("amulet", new Integer(8));// 项链
        _armorTypes.put("ring", new Integer(9));// 戒指
        _armorTypes.put("belt", new Integer(10));// 腰带
        _armorTypes.put("ring2", new Integer(11));// 戒指2
        _armorTypes.put("earring", new Integer(12));// 耳环
        _armorTypes.put("guarder", new Integer(13));// 臂甲

        _armorTypes.put("aidl", new Integer(14)); // 副助道具
        _armorTypes.put("aidr", new Integer(15)); // 副助道具
        _armorTypes.put("aidm", new Integer(16)); // 副助道具
        _armorTypes.put("aidl2", new Integer(17)); // 副助道具
        _armorTypes.put("aidr2", new Integer(18)); // 副助道具

        _weaponTypes.put("none", new Integer(0));// 空手
        _weaponTypes.put("sword", new Integer(1));// 剑(单手)
        _weaponTypes.put("dagger", new Integer(2));// 匕首(单手)
        _weaponTypes.put("tohandsword", new Integer(3));// 双手剑(双手)
        _weaponTypes.put("bow", new Integer(4));// 弓(双手)
        _weaponTypes.put("spear", new Integer(5));// 矛(双手)
        _weaponTypes.put("blunt", new Integer(6));// 斧(单手)
        _weaponTypes.put("staff", new Integer(7));// 魔杖(单手)
        _weaponTypes.put("throwingknife", new Integer(8));// 飞刀
        _weaponTypes.put("arrow", new Integer(9));// 箭
        _weaponTypes.put("gauntlet", new Integer(10));// 铁手甲
        _weaponTypes.put("claw", new Integer(11));// 钢爪(双手)
        _weaponTypes.put("edoryu", new Integer(12));// 双刀(双手)
        _weaponTypes.put("singlebow", new Integer(13));// 弓(单手)
        _weaponTypes.put("singlespear", new Integer(14));// 矛(单手)
        _weaponTypes.put("tohandblunt", new Integer(15));// 双手斧(双手)
        _weaponTypes.put("tohandstaff", new Integer(16));// 魔杖(双手)
        _weaponTypes.put("kiringku", new Integer(17));// 奇古兽(单手)
        _weaponTypes.put("chainsword", new Integer(18));// 锁链剑(单手)

        _weaponId.put("sword", new Integer(4));// 剑
        _weaponId.put("dagger", new Integer(46));// 匕首
        _weaponId.put("tohandsword", new Integer(50));// 双手剑
        _weaponId.put("bow", new Integer(20));// 弓
        _weaponId.put("blunt", new Integer(11));// 斧(单手)
        _weaponId.put("spear", new Integer(24));// 矛(双手)
        _weaponId.put("staff", new Integer(40));// 魔杖
        _weaponId.put("throwingknife", new Integer(2922));// 飞刀
        _weaponId.put("arrow", new Integer(66));// 箭
        _weaponId.put("gauntlet", new Integer(62));// 铁手甲
        _weaponId.put("claw", new Integer(58));// 钢爪
        _weaponId.put("edoryu", new Integer(54));// 双刀
        _weaponId.put("singlebow", new Integer(20));// 弓(单手)
        _weaponId.put("singlespear", new Integer(24));// 矛(单手)
        _weaponId.put("tohandblunt", new Integer(11));// 双手斧
        _weaponId.put("tohandstaff", new Integer(40));// 魔杖(双手)
        _weaponId.put("kiringku", new Integer(58));// 奇古兽
        _weaponId.put("chainsword", new Integer(24));// 锁链剑

        // 材质
        _materialTypes.put("none", new Integer(0));// 无
        _materialTypes.put("liquid", new Integer(1));// 忆体
        _materialTypes.put("web", new Integer(2));// 蜡
        _materialTypes.put("vegetation", new Integer(3));// 植物
        _materialTypes.put("animalmatter", new Integer(4));// 动物
        _materialTypes.put("paper", new Integer(5));// 纸
        _materialTypes.put("cloth", new Integer(6));// 布
        _materialTypes.put("leather", new Integer(7));// 皮革
        _materialTypes.put("wood", new Integer(8));// 木
        _materialTypes.put("bone", new Integer(9));// 骨头
        _materialTypes.put("dragonscale", new Integer(10));// 龙鳞
        _materialTypes.put("iron", new Integer(11));// 铁
        _materialTypes.put("steel", new Integer(12));// 钢
        _materialTypes.put("copper", new Integer(13));// 铜
        _materialTypes.put("silver", new Integer(14));// 银
        _materialTypes.put("gold", new Integer(15));// 黄金
        _materialTypes.put("platinum", new Integer(16));// 白金
        _materialTypes.put("mithril", new Integer(17));// 米索莉
        _materialTypes.put("blackmithril", new Integer(18));// 黑色米索莉
        _materialTypes.put("glass", new Integer(19));// 玻璃
        _materialTypes.put("gemstone", new Integer(20));// 宝石
        _materialTypes.put("mineral", new Integer(21));// 矿物
        _materialTypes.put("oriharukon", new Integer(22));// 奥里哈鲁根
    }

    public static ItemTable get() {
        if (_instance == null) {
            _instance = new ItemTable();
        }
        return _instance;
    }

    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        _etcitems = this.allEtcItem();
        _weapons = this.allWeapon();
        _armors = this.allArmor();
        this.buildFastLookupTable();
        _log.info("载入道具,武器,防具资料: " + _etcitems.size() + "+" + _weapons.size()
                + "+" + _armors.size() + "="
                + +(_etcitems.size() + _weapons.size() + _armors.size()) + "("
                + timer.get() + "ms)");
    }

    /**
     * 道具载入
     * 
     * @return
     */
    private Map<Integer, L1EtcItem> allEtcItem() {
        final Map<Integer, L1EtcItem> result = new HashMap<Integer, L1EtcItem>();

        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        L1EtcItem item = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `etcitem`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                item = new L1EtcItem();
                final int itemid = rs.getInt("item_id");
                item.setItemId(itemid);
                item.setName(rs.getString("name"));
                final String classname = rs.getString("classname");
                item.setClassname(classname);
                item.setNameId(rs.getString("name_id"));
                item.setType((_etcItemTypes.get(rs.getString("item_type")))
                        .intValue());
                item.setUseType(_useTypes.get(rs.getString("use_type"))
                        .intValue());
                item.setType2(0);
                item.setMaterial((_materialTypes.get(rs.getString("material")))
                        .intValue());
                item.setWeight(rs.getInt("weight"));
                item.setGfxId(rs.getInt("invgfx"));
                item.setGroundGfxId(rs.getInt("grdgfx"));
                item.setItemDescId(rs.getInt("itemdesc_id"));
                item.setMinLevel(rs.getInt("min_lvl"));
                item.setMaxLevel(rs.getInt("max_lvl"));
                item.setBless(rs.getInt("bless"));
                item.setTradable(rs.getInt("trade") == 0 ? true : false);
                item.setCantDelete(rs.getInt("cant_delete") == 1 ? true : false);
                item.setDmgSmall(rs.getInt("dmg_small"));
                item.setDmgLarge(rs.getInt("dmg_large"));
                item.set_stackable(rs.getInt("stackable") == 1 ? true : false);
                item.setMaxChargeCount(rs.getInt("max_charge_count"));

                item.set_delayid(rs.getInt("delay_id"));
                item.set_delaytime(rs.getInt("delay_time"));
                item.set_delayEffect(rs.getInt("delay_effect"));
                item.setFoodVolume(rs.getInt("food_volume"));
                item.setToBeSavedAtOnce((rs.getInt("save_at_once") == 1) ? true
                        : false);

                ItemClass.get().addList(itemid, classname, 0);
                result.put(new Integer(item.getItemId()), item);
            }
        } catch (final NullPointerException e) {
            _log.error("加载失败: " + item.getItemId(), e);

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        return result;
    }

    /**
     * 武器载入
     * 
     * @return
     */
    private Map<Integer, L1Weapon> allWeapon() {
        final Map<Integer, L1Weapon> result = new HashMap<Integer, L1Weapon>();

        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        L1Weapon weapon = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `weapon`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                weapon = new L1Weapon();
                final int itemid = rs.getInt("item_id");
                weapon.setItemId(itemid);
                weapon.setName(rs.getString("name"));
                final String classname = rs.getString("classname");
                weapon.setClassname(classname);
                weapon.setNameId(rs.getString("name_id"));
                weapon.setType((_weaponTypes.get(rs.getString("type")))
                        .intValue());
                weapon.setType1((_weaponId.get(rs.getString("type")))
                        .intValue());
                weapon.setType2(1);
                weapon.setUseType(1);
                weapon.setMaterial((_materialTypes.get(rs.getString("material")))
                        .intValue());
                weapon.setWeight(rs.getInt("weight"));
                weapon.setGfxId(rs.getInt("invgfx"));
                weapon.setGroundGfxId(rs.getInt("grdgfx"));
                weapon.setItemDescId(rs.getInt("itemdesc_id"));
                weapon.setDmgSmall(rs.getInt("dmg_small"));
                weapon.setDmgLarge(rs.getInt("dmg_large"));
                weapon.setRange(rs.getInt("range"));
                weapon.set_safeenchant(rs.getInt("safenchant"));
                weapon.setUseRoyal(rs.getInt("use_royal") == 0 ? false : true);
                weapon.setUseKnight(rs.getInt("use_knight") == 0 ? false : true);
                weapon.setUseElf(rs.getInt("use_elf") == 0 ? false : true);
                weapon.setUseMage(rs.getInt("use_mage") == 0 ? false : true);
                weapon.setUseDarkelf(rs.getInt("use_darkelf") == 0 ? false
                        : true);
                weapon.setUseDragonknight(rs.getInt("use_dragonknight") == 0 ? false
                        : true);
                weapon.setUseIllusionist(rs.getInt("use_illusionist") == 0 ? false
                        : true);
                weapon.setHitModifier(rs.getInt("hitmodifier"));
                weapon.setDmgModifier(rs.getInt("dmgmodifier"));
                weapon.set_addstr(rs.getByte("add_str"));
                weapon.set_adddex(rs.getByte("add_dex"));
                weapon.set_addcon(rs.getByte("add_con"));
                weapon.set_addint(rs.getByte("add_int"));
                weapon.set_addwis(rs.getByte("add_wis"));
                weapon.set_addcha(rs.getByte("add_cha"));
                weapon.set_addhp(rs.getInt("add_hp"));
                weapon.set_addmp(rs.getInt("add_mp"));
                weapon.set_addhpr(rs.getInt("add_hpr"));
                weapon.set_addmpr(rs.getInt("add_mpr"));
                weapon.set_addsp(rs.getInt("add_sp"));
                weapon.set_mdef(rs.getInt("m_def"));
                weapon.setDoubleDmgChance(rs.getInt("double_dmg_chance"));
                weapon.setMagicDmgModifier(rs.getInt("magicdmgmodifier"));
                weapon.set_canbedmg(rs.getInt("canbedmg"));
                weapon.setMinLevel(rs.getInt("min_lvl"));
                weapon.setMaxLevel(rs.getInt("max_lvl"));
                weapon.setBless(rs.getInt("bless"));
                weapon.setTradable(rs.getInt("trade") == 0 ? true : false);
                weapon.setCantDelete(rs.getInt("cant_delete") == 1 ? true
                        : false);
                weapon.setHasteItem(rs.getInt("haste_item") == 0 ? false : true);
                weapon.setMaxUseTime(rs.getInt("max_use_time"));

                ItemClass.get().addList(itemid, classname, 1);
                result.put(new Integer(weapon.getItemId()), weapon);
            }
        } catch (final NullPointerException e) {
            _log.error("加载失败: " + weapon.getItemId(), e);

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);

        }
        return result;
    }

    /**
     * 防具载入
     * 
     * @return
     */
    private Map<Integer, L1Armor> allArmor() {
        final Map<Integer, L1Armor> result = new HashMap<Integer, L1Armor>();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        L1Armor armor = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `armor`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                armor = new L1Armor();
                final int itemid = rs.getInt("item_id");
                armor.setItemId(itemid);
                armor.setName(rs.getString("name"));
                final String classname = rs.getString("classname");
                armor.setClassname(classname);
                armor.setNameId(rs.getString("name_id"));
                armor.setType((_armorTypes.get(rs.getString("type")))
                        .intValue());
                armor.setType2(2);
                armor.setUseType((_useTypes.get(rs.getString("type")))
                        .intValue());
                armor.setMaterial((_materialTypes.get(rs.getString("material")))
                        .intValue());
                armor.setWeight(rs.getInt("weight"));
                armor.setGfxId(rs.getInt("invgfx"));
                armor.setGroundGfxId(rs.getInt("grdgfx"));
                armor.setItemDescId(rs.getInt("itemdesc_id"));
                armor.set_ac(rs.getInt("ac"));
                armor.set_safeenchant(rs.getInt("safenchant"));
                armor.setUseRoyal(rs.getInt("use_royal") == 0 ? false : true);
                armor.setUseKnight(rs.getInt("use_knight") == 0 ? false : true);
                armor.setUseElf(rs.getInt("use_elf") == 0 ? false : true);
                armor.setUseMage(rs.getInt("use_mage") == 0 ? false : true);
                armor.setUseDarkelf(rs.getInt("use_darkelf") == 0 ? false
                        : true);
                armor.setUseDragonknight(rs.getInt("use_dragonknight") == 0 ? false
                        : true);
                armor.setUseIllusionist(rs.getInt("use_illusionist") == 0 ? false
                        : true);
                armor.set_addstr(rs.getByte("add_str"));
                armor.set_addcon(rs.getByte("add_con"));
                armor.set_adddex(rs.getByte("add_dex"));
                armor.set_addint(rs.getByte("add_int"));
                armor.set_addwis(rs.getByte("add_wis"));
                armor.set_addcha(rs.getByte("add_cha"));
                armor.set_addhp(rs.getInt("add_hp"));
                armor.set_addmp(rs.getInt("add_mp"));
                armor.set_addhpr(rs.getInt("add_hpr"));
                armor.set_addmpr(rs.getInt("add_mpr"));
                armor.set_addsp(rs.getInt("add_sp"));
                armor.setMinLevel(rs.getInt("min_lvl"));
                armor.setMaxLevel(rs.getInt("max_lvl"));
                armor.set_mdef(rs.getInt("m_def"));
                armor.setDamageReduction(rs.getInt("damage_reduction"));
                armor.setWeightReduction(rs.getInt("weight_reduction"));
                armor.setHitModifierByArmor(rs.getInt("hit_modifier"));
                armor.setDmgModifierByArmor(rs.getInt("dmg_modifier"));
                armor.setBowHitModifierByArmor(rs.getInt("bow_hit_modifier"));
                armor.setBowDmgModifierByArmor(rs.getInt("bow_dmg_modifier"));
                armor.setHasteItem(rs.getInt("haste_item") == 0 ? false : true);
                armor.setBless(rs.getInt("bless"));
                armor.setTradable(rs.getInt("trade") == 0 ? true : false);
                armor.setCantDelete(rs.getInt("cant_delete") == 1 ? true
                        : false);
                armor.set_defense_earth(rs.getInt("defense_earth"));
                armor.set_defense_water(rs.getInt("defense_water"));
                armor.set_defense_wind(rs.getInt("defense_wind"));
                armor.set_defense_fire(rs.getInt("defense_fire"));
                armor.set_regist_stun(rs.getInt("regist_stun"));
                armor.set_regist_stone(rs.getInt("regist_stone"));
                armor.set_regist_sleep(rs.getInt("regist_sleep"));
                armor.set_regist_freeze(rs.getInt("regist_freeze"));
                armor.set_regist_sustain(rs.getInt("regist_sustain"));
                armor.set_regist_blind(rs.getInt("regist_blind"));
                armor.setMaxUseTime(rs.getInt("max_use_time"));
                armor.set_greater(rs.getInt("greater"));

                ItemClass.get().addList(itemid, classname, 2);
                result.put(new Integer(armor.getItemId()), armor);
            }
        } catch (final NullPointerException e) {
            _log.error("加载失败: " + armor.getItemId(), e);

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);

        }
        return result;
    }

    private void buildFastLookupTable() {
        int highestId = 0;

        final Collection<L1EtcItem> items = _etcitems.values();
        for (final L1EtcItem item : items) {
            if (item.getItemId() > highestId) {
                highestId = item.getItemId();
            }
        }

        final Collection<L1Weapon> weapons = _weapons.values();
        for (final L1Weapon weapon : weapons) {
            if (weapon.getItemId() > highestId) {
                highestId = weapon.getItemId();
            }
        }

        final Collection<L1Armor> armors = _armors.values();
        for (final L1Armor armor : armors) {
            if (armor.getItemId() > highestId) {
                highestId = armor.getItemId();
            }
        }

        this._allTemplates = new L1Item[highestId + 1];

        for (final Iterator<Integer> iter = _etcitems.keySet().iterator(); iter
                .hasNext();) {
            final Integer id = iter.next();
            final L1EtcItem item = _etcitems.get(id);
            this._allTemplates[id.intValue()] = item;
        }

        for (final Iterator<Integer> iter = _weapons.keySet().iterator(); iter
                .hasNext();) {
            final Integer id = iter.next();
            final L1Weapon item = _weapons.get(id);
            this._allTemplates[id.intValue()] = item;
        }

        for (final Iterator<Integer> iter = _armors.keySet().iterator(); iter
                .hasNext();) {
            final Integer id = iter.next();
            final L1Armor item = _armors.get(id);
            this._allTemplates[id.intValue()] = item;
        }
    }

    /**
     * 具有套装设置的物件 加入效果数字阵列
     */
    public void se_mode() {
        final PerformanceTimer timer = new PerformanceTimer();
        for (final L1Item item : this._allTemplates) {
            if (item != null) {
                for (final Integer key : ArmorSet.getAllSet().keySet()) {
                    // 套装资料
                    final ArmorSet armorSet = ArmorSet.getAllSet().get(key);
                    // 套装中组件
                    if (armorSet.isPartOfSet(item.getItemId())) {
                        item.set_mode(armorSet.get_mode());
                    }
                }
            }
        }
        _log.info("载入套装效果数字阵列: " + timer.get() + "ms)");
    }

    /**
     * 传回指定编号物品资料
     * 
     * @param id
     * @return
     */
    public L1Item getTemplate(final int id) {
        try {
            return this._allTemplates[id];

        } catch (final Exception e) {
        }
        return null;
    }

    /**
     * 传回指定名称物品资料
     * 
     * @param nameid
     * @return
     */
    public L1Item getTemplate(final String nameid) {
        for (final L1Item item : this._allTemplates) {
            if ((item != null) && item.getNameId().equals(nameid)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 产生新物件
     * 
     * @param itemId
     * @return
     */
    public L1ItemInstance createItem(final int itemId) {
        final L1Item temp = this.getTemplate(itemId);
        if (temp == null) {
            return null;
        }
        final L1ItemInstance item = new L1ItemInstance();
        item.setId(IdFactory.get().nextId());
        item.setItem(temp);
        item.setBless(temp.getBless());

        World.get().storeObject(item);
        return item;
    }

    /**
     * 依名称(NameId)找回itemid
     * 
     * @param name
     * @return
     */
	public int findItemIdByName(final String name) {
		int itemid = 0;
		for (final L1Item item : this._allTemplates) {
			if ((item != null) && item.getNameId().equals(name)) {
				itemid = item.getItemId();
				break;
			}
		}
		return itemid;
	}

    /**
     * 依名称(中文)找回itemid
     * 
     * @param name
     * @return
     */
    public int findItemIdByNameWithoutSpace(final String name) {
		int itemid = 0;
		for (final L1Item item : this._allTemplates) {
			//屏掉这行改为下面那行getNameId 改为 getName  hjx1000
			//if ((item != null) && item.getNameId().replace(" ", "").equals(name)) {
			if ((item != null) && item.getName().replace(" ", "").equals(name)) {	
				itemid = item.getItemId();
				break;
			}
		}
        return itemid;
    }
}
