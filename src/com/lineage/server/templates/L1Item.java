package com.lineage.server.templates;

import java.io.Serializable;

public abstract class L1Item implements Serializable {

    private static final long serialVersionUID = 1L;

    public L1Item() {
    }

    // TODO L1EtcItem,L1Weapon,L1Armor 共用项目

    private int _type2; // ● 0=L1EtcItem, 1=L1Weapon, 2=L1Armor

    /**
     * @return 0 if L1EtcItem, 1 if L1Weapon, 2 if L1Armor
     */
    public int getType2() {
        return this._type2;
    }

    /**
     * 0 if L1EtcItem, 1 if L1Weapon, 2 if L1Armor
     * 
     * @param type
     */
    public void setType2(final int type) {
        this._type2 = type;
    }

    private int _itemId; // ● アイテムＩＤ

    public int getItemId() {
        return this._itemId;
    }

    public void setItemId(final int itemId) {
        this._itemId = itemId;
    }

    private String _name; // ● アイテム名

    public String getName() {
        return this._name;
    }

    public void setName(final String name) {
        this._name = name;
    }

    private String _classname;

    public String getclassname() {
        return this._classname;
    }

    public void setClassname(final String classname) {
        this._classname = classname;
    }

    private String _nameId; // ● ネームＩＤ

    public String getNameId() {
        return this._nameId;
    }

    public void setNameId(final String nameid) {
        this._nameId = nameid;
    }

    private int _type; // ● 详细なタイプ

    /**
     * 传回物品分类<br>
     * 
     * @return <p>
     *         <font color=#ff0000>[etcitem]-道具类型</font><br>
     *         0:arrow, 1:wand, 2:light, 3:gem, 4:totem, 5:firecracker,
     *         6:potion, 7:food, 8:scroll, 9:questitem, 10:spellbook,
     *         11:petitem, 12:other, 13:material, 14:event, 15:sting
     *         </p>
     *         <p>
     *         <font color=#ff0000>[weapon]-武器类型</font><br>
     *         1: sword <font color=#00800>剑(单手)</font><br>
     *         2: dagger <font color=#00800>匕首(单手)</font><br>
     *         3: tohandsword <font color=#00800>双手剑(双手)</font><br>
     *         4: bow <font color=#00800>弓(双手)</font><br>
     *         5: spear <font color=#00800>矛(双手)</font><br>
     *         6: blunt <font color=#00800>斧(单手)</font><br>
     *         7: staff <font color=#00800>魔杖(单手)</font><br>
     *         8: throwingknife <font color=#00800>飞刀</font><br>
     *         9: arrow <font color=#00800>箭</font><br>
     *         10: gauntlet <font color=#00800>铁手甲</font><br>
     *         11: claw <font color=#00800>钢爪(双手)</font><br>
     *         12: edoryu <font color=#00800>双刀(双手)</font><br>
     *         13: singlebow <font color=#00800>弓(单手)</font><br>
     *         14: singlespear <font color=#00800>矛(单手)</font><br>
     *         15: tohandblunt <font color=#00800>双手斧(双手)</font><br>
     *         16: tohandstaff <font color=#00800>魔杖(双手)</font><br>
     *         17: kiringku <font color=#00800>奇古兽(单手)</font><br>
     *         18: chainsword <font color=#00800>锁链剑(单手)</font><br>
     *         </p>
     *         <p>
     *         <font color=#ff0000>[armor]-防具类型</font><br>
     *         1: helm <font color=#00800>头盔</font><br>
     *         2: armor <font color=#00800>盔甲</font><br>
     *         3: T <font color=#00800>内衣</font><br>
     *         4: cloak <font color=#00800>斗篷</font><br>
     *         5: glove <font color=#00800>手套</font><br>
     *         6: boots <font color=#00800>靴子</font><br>
     *         7: shield <font color=#00800>盾</font><br>
     *         8: amulet <font color=#00800>项链</font><br>
     *         9: ring <font color=#00800>戒指</font><br>
     *         10: belt <font color=#00800>腰带</font><br>
     *         11: ring2 <font color=#00800>戒指2</font><br>
     *         12: earring <font color=#00800>耳环</font> <br>
     *         13: guarder <font color=#00800>臂甲</font><br>
     *         </p>
     */
    public int getType() {
        return this._type;
    }

    public void setType(final int type) {
        this._type = type;
    }

    private int _type1; // ● タイプ

    /**
     * 传地封包影响外型编号<br>
     * 
     * @return <p>
     *         <font color=#ff0000>[weapon]-武器类型</font><br>
     *         sword: 4 <font color=#00800>剑</font><br>
     *         dagger: 46 <font color=#00800>匕首</font><br>
     *         tohandsword: 50 <font color=#00800>双手剑</font><br>
     *         bow: 20 <font color=#00800>弓</font><br>
     *         blunt: 11 <font color=#00800>斧(单手)</font><br>
     *         spear: 24 <font color=#00800>矛(双手)</font><br>
     *         staff: 40 <font color=#00800>魔杖</font><br>
     *         throwingknife: 2922 <font color=#00800>飞刀</font><br>
     *         arrow: 66 <font color=#00800>箭</font><br>
     *         gauntlet: 62 <font color=#00800>铁手甲</font><br>
     *         claw: 58 <font color=#00800>钢爪</font><br>
     *         edoryu: 54 <font color=#00800>双刀</font><br>
     *         singlebow: 20 <font color=#00800>弓(单手)</font><br>
     *         singlespear: 24 <font color=#00800>矛(单手)</font><br>
     *         tohandblunt: 11 <font color=#00800>双手斧</font><br>
     *         tohandstaff: 40 <font color=#00800>魔杖(双手)</font><br>
     *         kiringku: 58 <font color=#00800>奇古兽</font><br>
     *         chainsword: 24 <font color=#00800>锁链剑</font><br>
     *         </p>
     */
    public int getType1() {
        return this._type1;
    }

    public void setType1(final int type1) {
        this._type1 = type1;
    }

    private int _material; // ● 素材

    /**
     * アイテムの素材を返す
     * 
     * @return 0:none 1:液体 2:web 3:植物性 4:动物性 5:纸 6:布 7:皮 8:木 9:骨 10:龙の鳞 11:铁
     *         12:钢铁 13:铜 14:银 15:金 16:プラチナ 17:ミスリル 18:ブラックミスリル 19:ガラス 20:宝石
     *         21:矿物 22:オリハルコン
     */
    public int getMaterial() {
        return this._material;
    }

    public void setMaterial(final int material) {
        this._material = material;
    }

    private int _weight; // ● 重量

    public int getWeight() {
        return this._weight;
    }

    public void setWeight(final int weight) {
        this._weight = weight;
    }

    private int _gfxId; // ● インベントリ内のグラフィックＩＤ

    public int getGfxId() {
        return this._gfxId;
    }

    public void setGfxId(final int gfxId) {
        this._gfxId = gfxId;
    }

    private int _groundGfxId; // ● 地面に置いた时のグラフィックＩＤ

    public int getGroundGfxId() {
        return this._groundGfxId;
    }

    public void setGroundGfxId(final int groundGfxId) {
        this._groundGfxId = groundGfxId;
    }

    private int _minLevel; // ● 使用、装备可能最小ＬＶ

    private int _itemDescId;

    /**
     * 鉴定时に表示されるItemDesc.tblのメッセージIDを返す。
     */
    public int getItemDescId() {
        return this._itemDescId;
    }

    public void setItemDescId(final int descId) {
        this._itemDescId = descId;
    }

    public int getMinLevel() {
        return this._minLevel;
    }

    public void setMinLevel(final int level) {
        this._minLevel = level;
    }

    private int _maxLevel; // ● 使用、装备可能最大ＬＶ

    public int getMaxLevel() {
        return this._maxLevel;
    }

    public void setMaxLevel(final int maxlvl) {
        this._maxLevel = maxlvl;
    }

    private int _bless; // ● 祝福状态

    /**
     * 属性
     * 
     * @return 0:祝福 1:一般 2:诅咒
     */
    public int getBless() {
        return this._bless;
    }

    public void setBless(final int i) {
        this._bless = i;
    }

    private boolean _tradable; // ● トレード可／不可

    /**
     * 转移
     * 
     * @return true:可以 false:不可以
     */
    public boolean isTradable() {
        return this._tradable;
    }

    public void setTradable(final boolean flag) {
        this._tradable = flag;
    }

    private boolean _cantDelete; // ● 削除不可

    /**
     * 删除
     * 
     * @return true:可以 false:不可以
     */
    public boolean isCantDelete() {
        return this._cantDelete;
    }

    public void setCantDelete(final boolean flag) {
        this._cantDelete = flag;
    }

    private boolean _save_at_once;

    /**
     * 数量变化储存档案
     */
    public boolean isToBeSavedAtOnce() {
        return this._save_at_once;
    }

    /**
     * 数量变化储存档案
     * 
     * @param flag
     */
    public void setToBeSavedAtOnce(final boolean flag) {
        this._save_at_once = flag;
    }

    private int _maxUseTime = 0; // 物品可使用时间

    /**
     * 物品可使用时间(能持有的时间)
     * 
     * @return
     */
    public int getMaxUseTime() {
        return this._maxUseTime;
    }

    /**
     * 物品可使用时间(能持有的时间)
     * 
     * @param i
     */
    public void setMaxUseTime(final int i) {
        this._maxUseTime = i;
    }

    private int _foodVolume;// 食品类道具饱食度

    /**
     * 食品类道具饱食度
     */
    public int getFoodVolume() {
        return this._foodVolume;
    }

    /**
     * 食品类道具饱食度
     * 
     * @param volume
     */
    public void setFoodVolume(final int volume) {
        this._foodVolume = volume;
    }

    /**
     * 照明道具亮度的设置
     * 
     * @return
     */
    public int getLightRange() {
        int light = 0x00;
        switch (this._itemId) {
            case 40001: // 灯
                light = 0x0b;
                break;

            case 40002: // 灯笼
                light = 0x0e;
                break;

            case 40004: // 魔法灯笼
                light = 0x16;
                break;

            case 40005: // 蜡烛
                light = 0x08;
                break;
        }
        return light;
    }

    /**
     * 照明道具可用时间设置
     */
    public int getLightFuel() {
        int time = 0x00;
        switch (this._itemId) {
            case 40001: // 灯
                time = 6000;
                break;

            case 40002: // 灯笼
                time = 12000;
                break;

            case 40003: // 灯油
                time = 12000;
                break;

            case 40004: // 魔法灯笼
                time = 0;
                break;

            case 40005: // 蜡烛
                time = 600;
                break;

        }
        return time;
    }

    private int _useType;// 物品使用封包类型

    /**
     * 物品使用封包类型
     * 
     * @return <p>
     *         petitem: -14 <font color=#00800>能量石</font><br>
     *         petitem: -13 <font color=#00800>强化石</font><br>
     *         petitem: -12 <font color=#00800>宠物道具</font><br>
     *         other: -11 <font color=#00800>对读取方法调用无法分类的物品</font><br>
     *         power: -10 <font color=#00800>加速药水</font><br>
     *         book: -9 <font color=#00800>技术书</font><br>
     *         makecooking: -8 <font color=#00800>料理书</font><br>
     *         hpr: -7 <font color=#00800>增HP道具</font><br>
     *         mpr: -6 <font color=#00800>增MP道具</font><br>
     *         ticket: -5 <font color=#00800>食人妖精竞赛票/死亡竞赛票/彩票</font><br>
     *         petcollar: -4 <font color=#00800>项圈</font><br>
     *         sting: -3 <font color=#00800>飞刀</font><br>
     *         arrow: -2 <font color=#00800>箭</font><br>
     *         none: -1 <font color=#00800>无法使用(材料等)</font><br>
     *         normal: 0 <font color=#00800>一般物品</font><br>
     *         weapon: 1 <font color=#00800>武器</font><br>
     *         armor: 2 <font color=#00800>盔甲</font><br>
     *         spell_1: 3 <font color=#00800>创造怪物魔杖(无须选取目标 -
     *         无数量:没有任何事情发生)</font><br>
     *         guarder: 4 <font color=#808080>希望魔杖 --- 未使用</font><br>
     *         spell_long: 5 <font color=#00800>魔杖类型(须选取目标/座标)</font><br>
     *         ntele: 6 <font color=#00800>瞬间移动卷轴</font><br>
     *         identify: 7 <font color=#00800>鉴定卷轴</font><br>
     *         res: 8 <font color=#00800>复活卷轴</font><br>
     *         home: 9 <font color=#00800>传送回家的卷轴</font><br>
     *         light: 10 <font color=#00800>照明道具</font><br>
     *         guarder: 11 <font color=#808080>未分类的卷轴 --- 未使用</font><br>
     *         letter: 12 <font color=#00800>信纸</font><br>
     *         letter_card: 13 <font color=#00800>信纸(寄出)</font><br>
     *         choice: 14 <font color=#00800>请选择一个物品(道具栏位)</font><br>
     *         instrument: 15 <font color=#00800>哨子</font><br>
     *         sosc: 16 <font color=#00800>变形卷轴</font><br>
     *         spell_short: 17 <font color=#00800>选取目标 (近距离)</font><br>
     *         T: 18 <font color=#00800>T恤</font><br>
     *         cloak: 19 <font color=#00800>斗篷</font><br>
     *         glove: 20 <font color=#00800>手套</font><br>
     *         boots: 21 <font color=#00800>靴</font><br>
     *         helm: 22 <font color=#00800>头盔</font><br>
     *         ring: 23 <font color=#00800>戒指</font><br>
     *         amulet: 24 <font color=#00800>项链</font><br>
     *         shield: 25 <font color=#00800>盾牌</font><br>
     *         guarder: 25 <font color=#00800>臂甲</font><br>
     *         dai: 26 <font color=#00800>对武器施法的卷轴</font><br>
     *         zel: 27 <font color=#00800>对盔甲施法的卷轴</font><br>
     *         blank: 28 <font color=#00800>空的魔法卷轴</font><br>
     *         btele: 29 <font color=#00800>瞬间移动卷轴(祝福)</font><br>
     *         spell_buff: 30 <font color=#00800>选取目标 (对NPC需要Ctrl 远距离
     *         无XY座标传回)</font><br>
     *         ccard: 31 <font color=#00800>圣诞卡片</font><br>
     *         ccard_w: 32 <font color=#00800>圣诞卡片(寄出)</font><br>
     *         vcard: 33 <font color=#00800>情人节卡片</font><br>
     *         vcard_w: 34 <font color=#00800>情人节卡片(寄出)</font><br>
     *         wcard: 35 <font color=#00800>白色情人节卡片</font><br>
     *         wcard_w: 36 <font color=#00800>白色情人节卡片(寄出)</font><br>
     *         belt: 37 <font color=#00800>腰带</font><br>
     *         food: 38 <font color=#00800>食物</font><br>
     *         spell_long2: 39 <font color=#00800>选取目标 (远距离)</font><br>
     *         earring: 40 <font color=#00800>耳环</font><br>
     * 
     *         fishing_rod: 42 <font color=#00800>钓鱼杆</font><br>
     * 
     *         aidr: 43 <font color=#00800>辅助右</font><br>
     *         aidl: 44 <font color=#00800>辅助左</font><br>
     *         aidm: 45 <font color=#00800>辅助中 </font><br>
     * 
     *         enc: 46 <font color=#ff0000>饰品强化卷轴</font><br>
     * 
     *         aidl2: 47 <font color=#00800>辅助左下</font><br>
     *         aidr2: 48 <font color=#00800>辅助右下</font><br>
     * 
     *         choice_doll: 55 <font color=#ff0000>请选择魔法娃娃</font><br>
     *         </p>
     */
    public int getUseType() {
        return this._useType;
    }

    /**
     * 物品使用封包类型
     * 
     * @param useType
     */
    public void setUseType(final int useType) {
        this._useType = useType;
    }

    // TODO L1EtcItem,L1Weapon 共通项目

    private int _dmgSmall = 0; // ● 最小ダメージ

    public int getDmgSmall() {
        return this._dmgSmall;
    }

    public void setDmgSmall(final int dmgSmall) {
        this._dmgSmall = dmgSmall;
    }

    private int _dmgLarge = 0; // ● 最大ダメージ

    public int getDmgLarge() {
        return this._dmgLarge;
    }

    public void setDmgLarge(final int dmgLarge) {
        this._dmgLarge = dmgLarge;
    }

    // TODO L1EtcItem,L1Armor 共通项目

    // TODO L1Weapon,L1Armor 共通项目

    private int[] _mode = null; // 套装附加的效果阵列

    /**
     * 套装附加的效果阵列
     * 
     * @return
     */
    public int[] get_mode() {
        return _mode;
    }

    /**
     * 套装附加的效果阵列
     * 
     * @param mode
     */
    public void set_mode(final int[] mode) {
        _mode = mode;
    }

    private int _safeEnchant = 0; // 安定值

    /**
     * 安定值
     * 
     * @return
     */
    public int get_safeenchant() {
        return this._safeEnchant;
    }

    /**
     * 安定值
     * 
     * @param safeenchant
     */
    public void set_safeenchant(final int safeenchant) {
        this._safeEnchant = safeenchant;
    }

    private boolean _useRoyal = false; // ● ロイヤルクラスが装备できるか

    public boolean isUseRoyal() {
        return this._useRoyal;
    }

    public void setUseRoyal(final boolean flag) {
        this._useRoyal = flag;
    }

    private boolean _useKnight = false; // ● ナイトクラスが装备できるか

    public boolean isUseKnight() {
        return this._useKnight;
    }

    public void setUseKnight(final boolean flag) {
        this._useKnight = flag;
    }

    private boolean _useElf = false; // ● エルフクラスが装备できるか

    public boolean isUseElf() {
        return this._useElf;
    }

    public void setUseElf(final boolean flag) {
        this._useElf = flag;
    }

    private boolean _useMage = false; // ● メイジクラスが装备できるか

    public boolean isUseMage() {
        return this._useMage;
    }

    public void setUseMage(final boolean flag) {
        this._useMage = flag;
    }

    private boolean _useDarkelf = false; // ● ダークエルフクラスが装备できるか

    public boolean isUseDarkelf() {
        return this._useDarkelf;
    }

    public void setUseDarkelf(final boolean flag) {
        this._useDarkelf = flag;
    }

    private boolean _useDragonknight = false; // ● ドラゴンナイト装备できるか

    public boolean isUseDragonknight() {
        return this._useDragonknight;
    }

    public void setUseDragonknight(final boolean flag) {
        this._useDragonknight = flag;
    }

    private boolean _useIllusionist = false; // ● イリュージョニスト装备できるか

    public boolean isUseIllusionist() {
        return this._useIllusionist;
    }

    public void setUseIllusionist(final boolean flag) {
        this._useIllusionist = flag;
    }

    private byte _addstr = 0; // ● ＳＴＲ补正

    public byte get_addstr() {
        return this._addstr;
    }

    public void set_addstr(final byte addstr) {
        this._addstr = addstr;
    }

    private byte _adddex = 0; // ● ＤＥＸ补正

    public byte get_adddex() {
        return this._adddex;
    }

    public void set_adddex(final byte adddex) {
        this._adddex = adddex;
    }

    private byte _addcon = 0; // ● ＣＯＮ补正

    public byte get_addcon() {
        return this._addcon;
    }

    public void set_addcon(final byte addcon) {
        this._addcon = addcon;
    }

    private byte _addint = 0; // ● ＩＮＴ补正

    public byte get_addint() {
        return this._addint;
    }

    public void set_addint(final byte addint) {
        this._addint = addint;
    }

    private byte _addwis = 0; // ● ＷＩＳ补正

    public byte get_addwis() {
        return this._addwis;
    }

    public void set_addwis(final byte addwis) {
        this._addwis = addwis;
    }

    private byte _addcha = 0; // ● ＣＨＡ补正

    public byte get_addcha() {
        return this._addcha;
    }

    public void set_addcha(final byte addcha) {
        this._addcha = addcha;
    }

    private int _addhp = 0; // ● ＨＰ补正

    public int get_addhp() {
        return this._addhp;
    }

    public void set_addhp(final int addhp) {
        this._addhp = addhp;
    }

    private int _addmp = 0; // ● ＭＰ补正

    public int get_addmp() {
        return this._addmp;
    }

    public void set_addmp(final int addmp) {
        this._addmp = addmp;
    }

    private int _addhpr = 0; // ● ＨＰＲ补正

    public int get_addhpr() {
        return this._addhpr;
    }

    public void set_addhpr(final int addhpr) {
        this._addhpr = addhpr;
    }

    private int _addmpr = 0; // ● ＭＰＲ补正

    public int get_addmpr() {
        return this._addmpr;
    }

    public void set_addmpr(final int addmpr) {
        this._addmpr = addmpr;
    }

    private int _addsp = 0; // ● ＳＰ补正

    public int get_addsp() {
        return this._addsp;
    }

    public void set_addsp(final int addsp) {
        this._addsp = addsp;
    }

    private int _mdef = 0; // 抗魔(MR)

    /**
     * 抗魔(MR)
     * 
     * @return
     */
    public int get_mdef() {
        return this._mdef;
    }

    /**
     * 抗魔(MR)
     * 
     * @param i
     */
    public void set_mdef(final int i) {
        this._mdef = i;
    }

    private boolean _isHasteItem = false; // 是否具有加速效果

    /**
     * 是否具有加速效果
     * 
     * @return
     */
    public boolean isHasteItem() {
        return this._isHasteItem;
    }

    /**
     * 是否具有加速效果
     * 
     * @param flag
     */
    public void setHasteItem(final boolean flag) {
        this._isHasteItem = flag;
    }

    // TODO L1EtcItem 专属项目

    /**
     * 物品可堆叠
     * 
     * @return true:可 false:不可
     */
    public boolean isStackable() {
        return false;
    }

    /**
     * 延迟编号
     * 
     * @return
     */
    public int get_delayid() {
        return 0;
    }

    /**
     * 延迟时间
     * 
     * @return
     */
    public int get_delaytime() {
        return 0;
    }

    /**
     * 最大可用次数
     * 
     * @return
     */
    public int getMaxChargeCount() {
        return 0;
    }

    private int _delay_effect;// 使用时间限制

    /**
     * 设定使用时间限制(上次使用到下次使用必须间隔时间)
     * 
     * @param delay_effect
     */
    public void set_delayEffect(final int delay_effect) {
        this._delay_effect = delay_effect;
    }

    /**
     * 传回使用时间限制(上次使用到下次使用必须间隔时间)
     * 
     * @return
     */
    public int get_delayEffect() {
        return this._delay_effect;
    }

    // TODO L1Weapon 专属项目

    public int get_add_dmg() {
        return 0;
    }

    public int getRange() {
        return 0;
    }

    public int getHitModifier() {
        return 0;
    }

    public int getDmgModifier() {
        return 0;
    }

    public int getDoubleDmgChance() {
        return 0;
    }

    public int getMagicDmgModifier() {
        return 0;
    }

    public int get_canbedmg() {
        return 0;
    }

    public boolean isTwohandedWeapon() {
        return false;
    }

    // TODO L1Armor 专属项目

    public int get_ac() {
        return 0;
    }

    public int getDamageReduction() {
        return 0;
    }

    public int getWeightReduction() {
        return 0;
    }

    public int getHitModifierByArmor() {
        return 0;
    }

    public int getDmgModifierByArmor() {
        return 0;
    }

    public int getBowHitModifierByArmor() {
        return 0;
    }

    public int getBowDmgModifierByArmor() {
        return 0;
    }

    /**
     * 增加水属性
     * 
     * @return
     */
    public int get_defense_water() {
        return 0;
    }

    /**
     * 增加火属性
     * 
     * @return
     */
    public int get_defense_fire() {
        return 0;
    }

    /**
     * 增加地属性
     * 
     * @return
     */
    public int get_defense_earth() {
        return 0;
    }

    /**
     * 增加风属性
     * 
     * @return
     */
    public int get_defense_wind() {
        return 0;
    }

    /**
     * 昏迷耐性
     * 
     * @return
     */
    public int get_regist_stun() {
        return 0;
    }

    /**
     * 石化耐性
     * 
     * @return
     */
    public int get_regist_stone() {
        return 0;
    }

    /**
     * 睡眠耐性
     * 
     * @return
     */
    public int get_regist_sleep() {
        return 0;
    }

    /**
     * 寒冰耐性
     * 
     * @return
     */
    public int get_regist_freeze() {
        return 0;
    }

    /**
     * 支撑耐性
     * 
     * @return
     */
    public int get_regist_sustain() {
        return 0;
    }

    /**
     * 暗黑耐性
     * 
     * @return
     */
    public int get_regist_blind() {
        return 0;
    }

    /**
     * 饰品强度
     * 
     * @return
     */
    public int get_greater() {
        return 3;
    }
}
