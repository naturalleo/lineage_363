package com.lineage.server.model.Instance;

import static com.lineage.server.model.skill.L1SkillId.BLESS_WEAPON;
import static com.lineage.server.model.skill.L1SkillId.ENCHANT_WEAPON;
import static com.lineage.server.model.skill.L1SkillId.HOLY_WEAPON;
import static com.lineage.server.model.skill.L1SkillId.SHADOW_FANG;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.lock.PetReading;
import com.lineage.server.model.L1EquipmentTimer;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1ItemPower_name;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1Pet;
import com.lineage.server.utils.RangeLong;
import com.lineage.server.world.World;

/**
 * 物品类控制项
 * 
 * @author dexc
 * 
 */
public class L1ItemInstance extends L1Object {

    private static final Log _log = LogFactory.getLog(L1ItemInstance.class);

    private static final long serialVersionUID = 1L;

    private long _count;

    private int _itemId;

    private boolean _isEquipped = false;

    private boolean _isEquippedTemp = false;

    private int _enchantLevel;// 物品强化质

    private boolean _isIdentified = false;

    private int _durability;

    private int _chargeCount;

    private int _remainingTime;

    private int _lastWeight;

    private boolean _isRunning = false;// 装备强化时间轴

    private int _bless;

    private int _attrEnchantKind;

    private int _attrEnchantLevel;

    private String _gamno = null;

    private L1Item _item;

    private Timestamp _lastUsed = null;

    private final LastStatus _lastStatus = new LastStatus();

    private L1PcInstance _pc;

    private EnchantTimer _timer;
    
    private int rndx = 0;//新增藏宝图随机X
    
    private int rndy = 0;//新增藏宝图随机Y

    public L1ItemInstance() {
        _count = 1;
        _enchantLevel = 0;
    }

    public L1ItemInstance(final L1Item item, final long count) {
        this();
        setItem(item);
        setCount(count);
    }

    /**
     * 传回鉴定状态
     * 
     * @return 确认济みならtrue、未确认ならfalse。
     */
    public boolean isIdentified() {
        return _isIdentified;
    }

    /**
     * 设置鉴定状态
     * 
     * @param identified
     *            确认济みならtrue、未确认ならfalse。
     */
    public void setIdentified(final boolean identified) {
        _isIdentified = identified;
    }

    /**
     * 传回NAMEID
     * 
     * @return
     */
    public String getName() {
        // _item.getName();
        return _item.getNameId();
    }

    /**
     * 传回数量
     * 
     * @return
     */
    public long getCount() {
        return _count;
    }

    /**
     * 数量设置
     * 
     * @param count
     */
    public void setCount(final long count) {
        // System.out.println("数量设置:" + count);
        _count = count;
    }

    /**
     * 场次代号
     * 
     * @return
     */
    public String getGamNo() {
        return _gamno;
    }

    /**
     * 设定场次代号
     * 
     * @param count
     */
    public void setGamNo(final String gamno) {
        _gamno = gamno;
    }

    /**
     * 物品装备状态
     * 
     * @return 已装备true、未装备false。
     */
    public boolean isEquipped() {
        return _isEquipped;
    }

    /**
     * 设置物品装备状态
     * 
     * @param equipped
     *            已装备true、未装备false。
     */
    public void setEquipped(final boolean equipped) {
        _isEquipped = equipped;
    }

    public L1Item getItem() {
        return _item;
    }

    public void setItem(final L1Item item) {
        _item = item;
        _itemId = item.getItemId();
    }

    public int getItemId() {
        return _itemId;
    }

    public void setItemId(final int itemId) {
        _itemId = itemId;
    }

    /**
     * 物品是否可以堆叠
     * 
     * @return true:可以 false:不可以
     */
    public boolean isStackable() {
        return _item.isStackable();
    }

    @Override
    public void onAction(final L1PcInstance player) {
    }

    /**
     * 物品强化质
     * 
     * @return
     */
    public int getEnchantLevel() {
        return _enchantLevel;
    }

    /**
     * 设定物品强化质
     * 
     * @param enchantLevel
     */
    public void setEnchantLevel(final int enchantLevel) {
        _enchantLevel = enchantLevel;
    }

    public int get_gfxid() {
        return _item.getGfxId();
    }

    public int get_durability() {
        return _durability;
    }

    /**
     * 传回可用次数
     * 
     * @return
     */
    public int getChargeCount() {
        return _chargeCount;
    }

    /**
     * 设置可用次数
     * 
     * @param i
     */
    public void setChargeCount(final int i) {
        _chargeCount = i;
    }

    /**
     * 剩余时间
     * 
     * @return
     */
    public int getRemainingTime() {
        return _remainingTime;
    }

    /**
     * 剩余时间
     * 
     * @param i
     */
    public void setRemainingTime(final int i) {
        _remainingTime = i;
    }

    public void setLastUsed(final Timestamp t) {
        _lastUsed = t;
    }

    public Timestamp getLastUsed() {
        return _lastUsed;
    }

    public int getLastWeight() {
        return _lastWeight;
    }

    public void setLastWeight(final int weight) {
        _lastWeight = weight;
    }

    /**
     * 祝福 0/128 一般 1/129 诅咒 2/130 ?? 3/131
     * 
     * @param i
     */
    public void setBless(final int i) {
        _bless = i;
    }

    /**
     * 祝福 0/128 一般 1/129 诅咒 2/130 ?? 3/131
     * 
     * @return
     */
    public int getBless() {
        return _bless;
    }

    /**
     * 属性强化类型
     * 
     * @param i
     */
    public void setAttrEnchantKind(final int i) {
        _attrEnchantKind = i;
    }

    /**
     * 属性强化类型
     * 
     * @return
     */
    public int getAttrEnchantKind() {
        return _attrEnchantKind;
    }

    /**
     * 属性强化质
     * 
     * @param i
     */
    public void setAttrEnchantLevel(final int i) {
        _attrEnchantLevel = i;
    }

    /**
     * 属性强化质
     * 
     * @return
     */
    public int getAttrEnchantLevel() {
        return _attrEnchantLevel;
    }

    /*
     * 耐久性、0~127まで -の值は许可しない。
     */
    public void set_durability(int i) {
        if (i < 0) {
            i = 0;
        }

        if (i > 127) {
            i = 127;
        }
        _durability = i;
    }

    public int getWeight() {
        if (getItem().getWeight() == 0) {
            return 0;

        } else {
            return (int) Math.max(getCount() * getItem().getWeight() / 1000, 1);
        }
    }

    /**
     * 前回DBへ保存した际のアイテムのステータスを格纳するクラス
     */
    public class LastStatus {

        public long count;

        public int itemId;

        public boolean isEquipped = false;

        public int enchantLevel;

        public boolean isIdentified = true;

        public int durability;

        public int chargeCount;

        public int remainingTime;

        public Timestamp lastUsed = null;

        public int bless;

        public int attrEnchantKind;

        public int attrEnchantLevel;

        // private String gamno;

        public void updateAll() {
            count = getCount();
            itemId = getItemId();
            isEquipped = isEquipped();
            isIdentified = isIdentified();
            enchantLevel = getEnchantLevel();
            durability = get_durability();
            chargeCount = getChargeCount();
            remainingTime = getRemainingTime();
            lastUsed = getLastUsed();
            bless = getBless();
            attrEnchantKind = getAttrEnchantKind();
            attrEnchantLevel = getAttrEnchantLevel();
            // this.gamno = L1ItemInstance.this.getGamNo();
        }

        public void updateCount() {
            count = getCount();
        }

        public void updateItemId() {
            itemId = getItemId();
        }

        public void updateEquipped() {
            isEquipped = isEquipped();
        }

        public void updateIdentified() {
            isIdentified = isIdentified();
        }

        public void updateEnchantLevel() {
            enchantLevel = getEnchantLevel();
        }

        public void updateDuraility() {
            durability = get_durability();
        }

        public void updateChargeCount() {
            chargeCount = getChargeCount();
        }

        public void updateRemainingTime() {
            remainingTime = getRemainingTime();
        }

        public void updateLastUsed() {
            lastUsed = getLastUsed();
        }

        public void updateBless() {
            bless = getBless();
        }

        public void updateAttrEnchantKind() {
            attrEnchantKind = getAttrEnchantKind();
        }

        public void updateAttrEnchantLevel() {
            attrEnchantLevel = getAttrEnchantLevel();
        }

        /*
         * public void updateGamno() { this.gamno =
         * L1ItemInstance.this.getGamNo(); }
         */
    }

    public LastStatus getLastStatus() {
        return _lastStatus;
    }

    /**
     * 前回DBに保存した时から变化しているカラムをビット集合として返す。
     */
    public int getRecordingColumns() {
        int column = 0;

        if (getCount() != _lastStatus.count) {
            column += L1PcInventory.COL_COUNT;
        }
        if (getItemId() != _lastStatus.itemId) {
            column += L1PcInventory.COL_ITEMID;
        }
        if (isEquipped() != _lastStatus.isEquipped) {
            column += L1PcInventory.COL_EQUIPPED;
        }
        if (getEnchantLevel() != _lastStatus.enchantLevel) {
            column += L1PcInventory.COL_ENCHANTLVL;
        }
        if (get_durability() != _lastStatus.durability) {
            column += L1PcInventory.COL_DURABILITY;
        }
        if (getChargeCount() != _lastStatus.chargeCount) {
            column += L1PcInventory.COL_CHARGE_COUNT;
        }
        if (getLastUsed() != _lastStatus.lastUsed) {
            column += L1PcInventory.COL_DELAY_EFFECT;
        }
        if (isIdentified() != _lastStatus.isIdentified) {
            column += L1PcInventory.COL_IS_ID;
        }
        if (getRemainingTime() != _lastStatus.remainingTime) {
            column += L1PcInventory.COL_REMAINING_TIME;
        }
        if (getBless() != _lastStatus.bless) {
            column += L1PcInventory.COL_BLESS;
        }
        if (getAttrEnchantKind() != _lastStatus.attrEnchantKind) {
            column += L1PcInventory.COL_ATTR_ENCHANT_KIND;
        }
        if (getAttrEnchantLevel() != _lastStatus.attrEnchantLevel) {
            column += L1PcInventory.COL_ATTR_ENCHANT_LEVEL;
        }

        return column;
    }

    /**
     * 背包/仓库 物件完整名称取回<br>
     */
    public String getNumberedViewName(final long count) {
        final StringBuilder name = new StringBuilder(getNumberedName(count,
                true));

//        if (_time != null) {
//            final SimpleDateFormat sdf = new SimpleDateFormat(
//                    "yyyy-MM-dd HH:mm");
//            name.append("[" + sdf.format(_time) + "]"); // 使用期限
//        }

        if (_power_name != null) {
            //name.append(" \\fT");//取消这个颜色 hjx1000
            for (int i = 0; i < _power_name.get_hole_count(); i++) {
                switch (i) {
                    case 0:
                        name.append(set_hole_name(_power_name.get_hole_1()));
                        break;
                    case 1:
                        name.append(set_hole_name(_power_name.get_hole_2()));
                        break;
                    case 2:
                        name.append(set_hole_name(_power_name.get_hole_3()));
                        break;
                    case 3:
                        name.append(set_hole_name(_power_name.get_hole_4()));
                        break;
                    case 4:
                        name.append(set_hole_name(_power_name.get_hole_5()));
                        break;
                }
            }
        }

        switch (_item.getUseType()) {
            default:
                if (isEquippedTemp()) {
                    // 防具/武器/道具 类型物件送出使用中物件上方会出现E
                    name.append(" ($117)"); // 使用中
                }
                break;

            case -12: // 宠物用具
                if (isEquipped()) {
                    name.append(" ($117)"); // 使用中(Worn)
                }
                break;

            case -4: // 项圈
                final L1Pet pet = PetReading.get().getTemplate(getId());
                if (pet != null) {
                    final L1Npc npc = NpcTable.get().getTemplate(
                            pet.get_npcid());
                    name.append("[Lv." + pet.get_level() + "]" + pet.get_name()
                            + " HP" + pet.get_hp() + " " + npc.get_nameid());
                }
                break;

            case 1: // 武器
                if (isEquipped()) {
                    // 武器 类型物件送出使用中物件上方会出现E
                    name.append(" ($9)"); // 挥舞(Armed)
                }
                break;

            case 10: // 照明道具
                if (isNowLighting()) {
                    name.append(" ($10)");// 打开
                }
                switch (_item.getItemId()) {
                    case 40001: // 灯
                    case 40002: // 灯笼
                        if (getRemainingTime() <= 0) {
                            name.append(" ($11)");// 无油
                        }
                        break;
                }
                break;

            case 2: // 盔甲
            case 18: // T恤
            case 19: // 斗篷
            case 20: // 手套
            case 21: // 靴
            case 22: // 头盔
            case 23: // 戒指
            case 24: // 项链
            case 25: // 盾牌
            case 37: // 腰带
            case 40: // 耳环
            case 43:// 副助道具
            case 44:// 副助道具
            case 45:// 副助道具
            case 48:// 副助道具
            case 47:// 副助道具
                if (isEquipped()) {
                    // 防具/道具 类型物件送出使用中物件上方会出现E
                    name.append(" ($117)"); // 使用中(Worn)
                }
                break;
        }
        return name.toString();
    }

    private String set_hole_name(int hole) {
        String string = "";
        switch (hole) {
            case 0:
                string = "◎";
                break;
            case 1:
                string = "力";
                break;
            case 2:
                string = "敏";
                break;
            case 3:
                string = "体";
                break;
            case 4:
                string = "精";
                break;
            case 5:
                string = "智";
                break;
            case 6:
                string = "魅";
                break;
            case 7:
                string = "血";
                break;
            case 8:
                string = "魔";
                break;
            case 9:
                if (this.getItem().getType2() == 1) {
                    string = "攻";
                }
                break;
            case 10:
                if (this.getItem().getType2() == 2) {
                    string = "防";
                }
                break;
            case 11:
                if (this.getItem().getType2() == 2) {
                    string = "抗";
                }
                break;
        }
        return string;
    }

    /**
     * 背包会仓库名称显示。<br>
     * 范例: +6 匕首 (挥舞)
     */
    public String getViewName() {
        return getNumberedViewName(_count);
    }

    /**
     * 返回显示名称。<br>
     * 范例:强力治愈药水(50) / +6 匕首
     */
    public String getLogName() {
        return getNumberedName(_count, true);
    }
    
    /**
     * 返回显示名称。<br>
     * 寄卖商店专用显示诅咒或祝福
     */
    public String getLogName1() {
    	String SSS = "";
    	if (_item.getBless() == 0) {
    		SSS = "受祝福的";
    	} else if (_item.getBless() == 2) {
    		SSS = "受诅咒的";
    	}
        return SSS + getNumberedName(_count, true);
    }

    /**
     * 返回显示名称。<br>
     * 寄卖商店专用显示诅咒或祝福
     */
    public String getLogName2(final long count) {
    	String SSS = "";
    	if (_item.getBless() == 0) {
    		SSS = "受祝福的";
    	} else if (_item.getBless() == 2) {
    		SSS = "受诅咒的";
    	}
        return SSS + getNumberedName(count, true);
    }
    
    /**
     * 附魔专用显示名称。<br>
     * 
     */
    public String getLogNamefumo() {
    	final StringBuilder name = new StringBuilder();
    	if (this.isIdentified()) {
    		switch (_item.getUseType()) {
            case 1:// 武器

				// 追加值
				if (getEnchantLevel() >= 0) {
					name.append("+" + getEnchantLevel() + " ");
					
				} else if (getEnchantLevel() < 0) {
					name.append(String.valueOf(getEnchantLevel()) + " ");
				}
				//显示星武器 add hjx1000
				if (_power_name != null) {
					final int xing = _power_name.get_xing_count();
					switch (xing) { //按阶段给不同的符号 hjx1000
						case 1:
							name.append("觉醒·");
							break;
						case 2:
							name.append("荣耀·");
							break;
						case 3:
							name.append("破军·");
							break;
						case 4:
							name.append("狂怒·");
							break;
						case 5:
							name.append("天启·");
							break;
						case 6:
							name.append("Lv⑥");
							break;
						case 7:
							name.append("Lv⑦");
							break;
						case 8:
							name.append("Lv⑧");
							break;
						case 9:
							name.append("Lv⑨");
							break;			
					}
					name.append(" ");
				}
                break;
    		}
    	}
    	name.append(_item.getNameId());
        return name.toString();
    }
    
    /**
     * 物件完整名称取回
     * 
     * @param count
     *            数量
     * @param mode
     *            模式 true:使用NAMEID false:使用中文注解名称
     * @return
     */
    public String getNumberedName(final long count, final boolean mode) {
        final StringBuilder name = new StringBuilder();

        if (this.isIdentified()) {
            switch (_item.getUseType()) {
                case 1:// 武器
    				// 附加屬性  修正显武器名称顺序 hjx1000
    				final int attrEnchantLevel = getAttrEnchantLevel();
    				if (attrEnchantLevel > 0) {
    					name.append(attrEnchantLevel());
    				}
    				// 追加值
    				if (getEnchantLevel() >= 0) {
    					name.append("+" + getEnchantLevel() + " ");
    					
    				} else if (getEnchantLevel() < 0) {
    					name.append(String.valueOf(getEnchantLevel()) + " ");
    				}
    				//显示星武器 add hjx1000
    				if (_power_name != null) {
    					final int xing = _power_name.get_xing_count();
    					switch (xing) { //按阶段给不同的符号 hjx1000
    						case 1:
    							name.append("觉醒·");
    							break;
    						case 2:
    							name.append("荣耀·");
    							break;
    						case 3:
    							name.append("破军·");
    							break;
    						case 4:
    							name.append("狂怒·");
    							break;
    						case 5:
    							name.append("天启·");
    							break;
    						case 6:
    							name.append("Lv⑥");
    							break;
    						case 7:
    							name.append("Lv⑦");
    							break;
    						case 8:
    							name.append("Lv⑧");
    							break;
    						case 9:
    							name.append("Lv⑨");
    							break;			
    					}
    					name.append(" ");
    				}
                    break;

                case 2: // 盔甲
                case 20: // 手套
                case 21: // 靴
                case 22: // 头盔

                case 18: // T恤
                case 19: // 斗篷
                case 25: // 盾牌
                case 23:// 戒指
                case 24:// 项链
                case 37:// 腰带
                case 40:// 耳环
                    // 追加值
    				if (getEnchantLevel() >= 0) {
    					name.append("+" + getEnchantLevel() + " ");
    					
    				} else if (getEnchantLevel() < 0) {
    					name.append(String.valueOf(getEnchantLevel()) + " ");
    				}
    				//显示星装备 add hjx1000
    				if (_power_name != null) {
    					final int xing = _power_name.get_xing_count();
    					switch (xing) { //按阶段给不同的符号 hjx1000
    						case 1:
    							name.append("附魔①");
    							break;
    						case 2:
    							name.append("附魔②");
    							break;
    						case 3:
    							name.append("附魔③");
    							break;
    						case 4:
    							name.append("附魔④");
    							break;
    						case 5:
    							name.append("附魔⑤");
    							break;
    						case 6:
    							name.append("附魔⑥");
    							break;
    						case 7:
    							name.append("附魔⑦");
    							break;
    						case 8:
    							name.append("附魔⑧");
    							break;
    						case 9:
    							name.append("附魔⑨");
    							break;			
    					}
    					name.append(" ");
    				}
                    break;
            }
        }

        if (mode) {
            name.append(_item.getNameId());

        } else {
            name.append(_item.getName());
        }

        if (_item.getUseType() == -5) { // 食人妖精竞赛票
            name.append("\\f_[" + getGamNo() + "]");
        }

        if (this.isIdentified()) {
            // 资料库原始最大可用次数大于0
            if (getItem().getMaxChargeCount() > 0) {
                name.append(" (" + getChargeCount() + ")");

            } else {
                switch (_item.getItemId()) {
                    case 20383: // 军马头盔
                        name.append(" (" + getChargeCount() + ")");
                        break;

                    default:
                        break;
                }
            }

            // 武器/防具 具有使用时间
            if ((getItem().getMaxUseTime() > 0) && (getItem().getType2() != 0)) {
                name.append(" (" + getRemainingTime() + ")");
            }
        }

        if (count > 1) {
            if (count < 1000000000) {
                name.append(" (" + count + ")");

            } else {
                name.append(" (" + RangeLong.scount(count) + ")");
            }
        }

        return name.toString();
    }

    // 属性武器
    private static final String[][] _attrEnchant = new String[][] {
            new String[] { "$6124", "$6125", "$6126", "$14364", "$14368" },// 地之, 崩裂, 地灵
            new String[] { "$6115", "$6116", "$6117", "$14361", "$14365" },// 火之, 烈焰, 火灵
            new String[] { "$6118", "$6119", "$6120", "$14362", "$14366" },// 水之, 海啸, 水灵
            new String[] { "$6121", "$6122", "$6123", "$14363", "$14367" },// 风之, 暴风, 风灵
            // ADD LOLI
            new String[] { "光之", "闪耀 ", "光灵" },
            new String[] { "暗之", "阴影", "暗灵" },
            new String[] { "圣之", "神圣", "圣灵" },
            new String[] { "邪之", "邪恶", "邪灵" }, };

    /**
     * 属性武器
     * 
     * @return
     */
    private StringBuilder attrEnchantLevel() {
        final StringBuilder attrEnchant = new StringBuilder();

        final int attrEnchantLevel = this.getAttrEnchantLevel();
        int type = 0;
        switch (this.getAttrEnchantKind()) {
            case 1: // 地
                type = 0;
                break;

            case 2: // 火
                type = 1;
                break;

            case 4: // 水
                type = 2;
                break;

            case 8: // 风
                type = 3;
                break;

            case 16: // 光
                type = 4;
                break;

            case 32: // 暗
                type = 5;
                break;

            case 64: // 圣
                type = 6;
                break;

            case 128: // 邪
                type = 7;
                break;

            default:
                break;
        }
        attrEnchant.append(_attrEnchant[type][attrEnchantLevel - 1]);
        return attrEnchant;
    }

    /**
     * 物品详细资料
     */
    public byte[] getStatusBytes(final L1PcInstance owner) {
        final L1ItemStatus itemInfo = new L1ItemStatus(this);
        return itemInfo.getStatusBytes(owner).getBytes();
    }

    /**
     * 抗魔
     * 
     * @return
     */
    public int getMr() {
        final L1ItemPower itemPower = new L1ItemPower(this);
        return itemPower.getMr();
    }
    
    /**
     * 增MP
     * hjx1000
     */
    public int getMp() {
    	final L1ItemPower itemPower = new L1ItemPower(this);
    	return itemPower.getMp();
    }
    
    /**
     * 增HP
     * hjx1000
     */
    public int getHp() {
    	final L1ItemPower itemPower = new L1ItemPower(this);
    	return itemPower.getHp();
    }

    /**
     * 增SP装备设置
     * @param armor
     * @return
     */
    public int getSp() {
    	final L1ItemPower itemPower = new L1ItemPower(this);
    	return itemPower.getSp();
    }
    
    /**
     * 增远程攻击装备设置
     * @param armor
     * @return
     */
    public int BowDmg() {
    	final L1ItemPower itemPower = new L1ItemPower(this);
    	return itemPower.BowDmg();
    }
    
    /**
     * 增近身攻击装备设置
     * @param armor
     * @return
     */
    public int MeleeDmg() {
    	final L1ItemPower itemPower = new L1ItemPower(this);
    	return itemPower.MeleeDmg();
    }
    
    /**
     * 强化饰品设置
     */
    public void greater(final L1PcInstance owner, final boolean equipment) {
        final L1ItemPower itemPower = new L1ItemPower(this);
        itemPower.greater(owner, equipment);
    }

    class EnchantTimer extends TimerTask {

        public EnchantTimer() {
        }

        @Override
        public void run() {
            try {
                final int type = getItem().getType();
                final int type2 = getItem().getType2();
                final int objid = getId();
//                if ((_pc != null) && _pc.getInventory().getItem(objid) != null) {
//                    if ((type == 2) && (type2 == 2) && isEquipped()) {
//                        _pc.addAc(3);
//                        _pc.sendPackets(new S_OwnCharStatus(_pc));
//                    }
//                }
                //修正铠甲护持 BUG hjx1000
                if ((type == 2) && (type2 == 2) && isEquipped()) {
                	if ((_pc != null) && _pc.getInventory().getItem(objid) != null) {
                		_pc.addAc(3);
                		_pc.sendPackets(new S_OwnCharStatus(_pc));
                	} else {
                        for (final L1PcInstance pc : World.get().getAllPlayers()) {
                        	if (pc.getInventory().getItem(objid) != null) {
                        		pc.addAc(3);
                        		pc.sendPackets(new S_OwnCharStatus(pc));
                        	}                    	
                        }
                	}
                }
                setAcByMagic(0);
                setDmgByMagic(0);
                setHolyDmgByMagic(0);
                setHitByMagic(0);
                // 308 你的 %0%o 失去了光芒。
                _pc.sendPackets(new S_ServerMessage(308, getLogName()));
                _isRunning = false;
                _timer = null;

            } catch (final Exception e) {
                _log.warn("EnchantTimer: " + getItemId());
            }
        }
    }

    private int _acByMagic = 0;

    /**
     * 魔法增加额外防御力
     * 
     * @return
     */
    public int getAcByMagic() {
        return _acByMagic;
    }

    /**
     * 魔法增加额外防御力
     * 
     * @param i
     */
    public void setAcByMagic(final int i) {
        _acByMagic = i;
    }

    private int _dmgByMagic = 0;

    /**
     * 魔法增加额外攻击
     * 
     * @return
     */
    public int getDmgByMagic() {
        int adddmg = 0;
        if (_power_name != null && this.getItem().getType2() == 1) {
            switch (_power_name.get_hole_1()) {
                case 9:// 攻 额外攻击+3
                    adddmg += 3;
                    break;
            }
            switch (_power_name.get_hole_2()) {
                case 9:// 攻 额外攻击+3
                    adddmg += 3;
                    break;
            }
            switch (_power_name.get_hole_3()) {
                case 9:// 攻 额外攻击+3
                    adddmg += 3;
                    break;
            }
            switch (_power_name.get_hole_4()) {
                case 9:// 攻 额外攻击+3
                    adddmg += 3;
                    break;
            }
            switch (_power_name.get_hole_5()) {
                case 9:// 攻 额外攻击+3
                    adddmg += 3;
                    break;
            }
        }
        return _dmgByMagic + adddmg;
    }

    /**
     * 魔法增加额外攻击
     * 
     * @param i
     */
    public void setDmgByMagic(final int i) {
        _dmgByMagic = i;
    }

    private int _holyDmgByMagic = 0;

    public int getHolyDmgByMagic() {
        return _holyDmgByMagic;
    }

    public void setHolyDmgByMagic(final int i) {
        _holyDmgByMagic = i;
    }

    private int _hitByMagic = 0;

    /**
     * 魔法增加额外命中
     * 
     * @return
     */
    public int getHitByMagic() {
        return _hitByMagic;
    }

    /**
     * 魔法增加额外命中
     * 
     * @param i
     */
    public void setHitByMagic(final int i) {
        _hitByMagic = i;
    }

    /**
     * 盔甲强化时间轴
     * 
     * @param pc
     * @param skillId
     * @param skillTime
     */
    public void setSkillArmorEnchant(final L1PcInstance pc, final int skillId,
            final int skillTime) {
        final int type = getItem().getType();
        final int type2 = getItem().getType2();
        if (_isRunning) {
            _timer.cancel();
            final int objid = this.getId();
            if ((pc != null) && pc.getInventory().getItem(objid) != null) {
                if ((type == 2) && (type2 == 2) && isEquipped()) {
                    pc.addAc(3);
                    pc.sendPackets(new S_OwnCharStatus(pc));
                }
            }
            setAcByMagic(0);
            _isRunning = false;
            _timer = null;
        }

        if ((type == 2) && (type2 == 2) && isEquipped()) {
        	pc.addAc(-3);
            pc.sendPackets(new S_OwnCharStatus(pc));
        }
        setAcByMagic(3);
        _pc = pc;
        _char_objid = _pc.getId();

        _timer = new EnchantTimer();
        (new Timer()).schedule(_timer, skillTime);
        _isRunning = true;
    }

    /**
     * 武器强化时间轴
     * 
     * @param pc
     * @param skillId
     * @param skillTime
     */
    public void setSkillWeaponEnchant(final L1PcInstance pc, final int skillId,
            final int skillTime) {
        if (getItem().getType2() != 1) {
            return;
        }
        if (_isRunning) {
            _timer.cancel();
            setDmgByMagic(0);
            setHolyDmgByMagic(0);
            setHitByMagic(0);
            _isRunning = false;
            _timer = null;
        }

        switch (skillId) {
            case HOLY_WEAPON:
                setHolyDmgByMagic(1);
                setHitByMagic(1);
                break;

            case ENCHANT_WEAPON:
                setDmgByMagic(2);
                break;

            case BLESS_WEAPON:
                setDmgByMagic(2);
                setHitByMagic(2);
                break;

            case SHADOW_FANG:
                setDmgByMagic(5);
                break;

            default:
                break;
        }

        _pc = pc;
        _char_objid = _pc.getId();

        _timer = new EnchantTimer();
        (new Timer()).schedule(_timer, skillTime);
        _isRunning = true;
    }

    private int _itemOwnerId = 0;

    public int getItemOwnerId() {
        return _itemOwnerId;
    }

    public void setItemOwnerId(final int i) {
        _itemOwnerId = i;
    }

    private L1EquipmentTimer _equipmentTimer;

    /**
     * 计时物件启用
     * 
     * @param pc
     */
    public void startEquipmentTimer(final L1PcInstance pc) {
        if (getRemainingTime() > 0) {
            _equipmentTimer = new L1EquipmentTimer(pc, this);
            final Timer timer = new Timer(true);
            timer.scheduleAtFixedRate(_equipmentTimer, 1000, 1000);
        }
    }

    /**
     * 计时物件停止计时
     * 
     * @param pc
     */
    public void stopEquipmentTimer(final L1PcInstance pc) {
        if (getRemainingTime() > 0) {
            _equipmentTimer.cancel();
            _equipmentTimer = null;
        }
    }

    private boolean _isNowLighting = false;

    public boolean isNowLighting() {
        return _isNowLighting;
    }

    public void setNowLighting(final boolean flag) {
        _isNowLighting = flag;
    }

    /**
     * 传回物件使用中
     * 
     * @return
     */
    public boolean isEquippedTemp() {
        return _isEquippedTemp;
    }

    /**
     * 设置物件使用中
     * 
     * @param isEquippedTemp
     */
    public void set_isEquippedTemp(final boolean isEquippedTemp) {
        _isEquippedTemp = isEquippedTemp;
    }

    private boolean _isMatch = false;

    /**
     * 完成套装
     * 
     * @param isMatch
     */
    public void setIsMatch(final boolean isMatch) {
        _isMatch = isMatch;
    }

    /**
     * 完成套装
     * 
     * @return true:完成套装 false:未完成套装
     */
    public boolean isMatch() {
        return _isMatch;
    }

    // 物品使用者OBJID
    private int _char_objid = -1;

    /**
     * 设置物品使用者OBJID
     * 
     * @param skilltime
     */
    public void set_char_objid(int char_objid) {
        _char_objid = char_objid;
    }

    /**
     * 物品使用者OBJID
     * 
     * @return _skilltime
     */
    public int get_char_objid() {
        return _char_objid;
    }

    // 物品使用期限结束时间
    private Timestamp _time = null;

    /**
     * 设置物品使用期限结束时间
     * 
     * @param skilltime
     */
    public void set_time(Timestamp time) {
        _time = time;
    }

    /**
     * 物品使用期限结束时间
     * 
     * @return _skilltime
     */
    public Timestamp get_time() {
        return _time;
    }

    public boolean isRunning() {
        return _timer != null;
    }

    // 凹槽
    private L1ItemPower_name _power_name = null;

    public void set_power_name(L1ItemPower_name power_name) {
        _power_name = power_name;
    }

    public L1ItemPower_name get_power_name() {
        return _power_name;
    }

    // 属性武器
    private static final String[][] _attrEnchantString = new String[][] {
        new String[] { "地灵：1段", "地灵：2段", "地灵：3段", "地灵：4段", "地灵：5段" },// 地之, 崩裂, 地灵
        new String[] { "火灵：1段", "火灵：2段", "火灵：3段", "火灵：4段", "火灵：5段" },// 火之, 烈焰, 火灵
        new String[] { "水灵：1段", "水灵：2段", "水灵：3段", "水灵：4段", "水灵：5段" },// 水之, 海啸, 水灵
        new String[] { "风灵：1段", "风灵：2段", "风灵：3段", "风灵：4段", "风灵：5段" },// 风之, 暴风, 风灵
            // ADD LOLI
            new String[] { "光之", "闪耀 ", "光灵" },
            new String[] { "暗之", "阴影", "暗灵" },
            new String[] { "圣之", "神圣", "圣灵" },
            new String[] { "邪之", "邪恶", "邪灵" }, };

    public String getNumberedName_to_String() {
        final StringBuilder name = new StringBuilder();
        // 追加值
       /* if (getEnchantLevel() >= 0) {
            name.append("+" + getEnchantLevel() + " ");

        } else if (getEnchantLevel() < 0) {
            name.append(String.valueOf(getEnchantLevel()) + " ");
        }*/ //修正武器名称顺序显示 hjx1000

        switch (_item.getUseType()) {
            case 1:// 武器
                   // 附加属性
                final int attrEnchantLevel = getAttrEnchantLevel();
                if (attrEnchantLevel > 0) {
                    int type = 0;
                    switch (this.getAttrEnchantKind()) {
                        case 1: // 地
                            type = 0;
                            break;

                        case 2: // 火
                            type = 1;
                            break;

                        case 4: // 水
                            type = 2;
                            break;

                        case 8: // 风
                            type = 3;
                            break;

                        case 16: // 光
                            type = 4;
                            break;

                        case 32: // 暗
                            type = 5;
                            break;

                        case 64: // 圣
                            type = 6;
                            break;

                        case 128: // 邪
                            type = 7;
                            break;
                    }
                    name.append(_attrEnchantString[type][attrEnchantLevel - 1]);
                }
                break;

            case 2: // 盔甲
            case 20: // 手套
            case 21: // 靴
            case 22: // 头盔
            case 18: // T恤
            case 19: // 斗篷
            case 25: // 盾牌
            case 23:// 戒指
            case 24:// 项链
            case 37:// 腰带
            case 40:// 耳环
                break;
        }
		// 追加值 //修正武器显示排序 hjx1000
		if (getEnchantLevel() >= 0) {
			name.append("+" + getEnchantLevel() + " ");
			
		} else if (getEnchantLevel() < 0) {
			name.append(String.valueOf(getEnchantLevel()) + " ");
		}
		//显示星装备及武器 add hjx1000
		if (_power_name != null) {
			final int xing = _power_name.get_xing_count();
			switch (xing) { //按阶段给不同的符号 hjx1000
				case 1:
					name.append("附魔①");
					break;
				case 2:
					name.append("附魔②");
					break;
				case 3:
					name.append("附魔③");
					break;
				case 4:
					name.append("附魔④");
					break;
				case 5:
					name.append("附魔⑤");
					break;
				case 6:
					name.append("附魔⑥");
					break;
				case 7:
					name.append("附魔⑦");
					break;
				case 8:
					name.append("附魔⑧");
					break;
				case 9:
					name.append("附魔⑨");
					break;			
			}
			name.append(" ");
		}
        name.append(_item.getName());
        // 资料库原始最大可用次数大于0
        if (getItem().getMaxChargeCount() > 0) {
            name.append(" (" + getChargeCount() + ")");

        } else {
            switch (_item.getItemId()) {
                case 20383: // 军马头盔
                    name.append(" (" + getChargeCount() + ")");
                    break;

                default:
                    break;
            }
        }

        if (_power_name != null) {
            for (int i = 0; i < _power_name.get_hole_count(); i++) {
                switch (i) {
                    case 0:
                        name.append(set_hole_name(_power_name.get_hole_1()));
                        break;
                    case 1:
                        name.append(set_hole_name(_power_name.get_hole_2()));
                        break;
                    case 2:
                        name.append(set_hole_name(_power_name.get_hole_3()));
                        break;
                    case 3:
                        name.append(set_hole_name(_power_name.get_hole_4()));
                        break;
                    case 4:
                        name.append(set_hole_name(_power_name.get_hole_5()));
                        break;
                }
            }
        }

        long count = this.getCount();
        if (count > 1) {
            if (count < 1000000000) {
                name.append(" (" + count + ")");

            } else {
                name.append(" (" + RangeLong.scount(count) + ")");
            }
        }
        return name.toString();
    }

	public int getRndx() {
		return rndx;
	}

	public void setRndx(int rndx) {
		this.rndx = rndx;
	}

	public int getRndy() {//宝图随机y坐标
		return rndy;
	}

	public void setRndy(int rndy) {
		this.rndy = rndy;
	}

}
