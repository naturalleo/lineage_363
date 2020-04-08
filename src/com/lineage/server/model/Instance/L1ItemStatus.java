package com.lineage.server.model.Instance;

import java.text.SimpleDateFormat;

import com.lineage.server.datatables.DollPowerTable;
import com.lineage.server.datatables.PetItemTable;
import com.lineage.server.templates.L1Doll;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1ItemPower_name;
import com.lineage.server.templates.L1PetItem;
import com.lineage.server.utils.BinaryOutputStream;

/**
 * 物品详细资料
 * 
 * @author dexc
 * 
 */
public class L1ItemStatus {

    private final L1ItemInstance _itemInstance;

    private final L1Item _item;

    private final BinaryOutputStream _os;

    private final L1ItemPower _itemPower;

    /**
     * 物品详细资料
     * 
     * @param itemInstance
     *            L1ItemInstance
     */
    public L1ItemStatus(final L1ItemInstance itemInstance) {
        this._itemInstance = itemInstance;
        this._item = itemInstance.getItem();
        this._os = new BinaryOutputStream();
        this._itemPower = new L1ItemPower(this._itemInstance);
    }
    

    /**
     * 物品详细资料
     * 
     * @param template
     *            L1Item
     */
    public L1ItemStatus(final L1Item template) {
        this._itemInstance = new L1ItemInstance();
        this._itemInstance.setItem(template);
        this._item = template;
        this._os = new BinaryOutputStream();
        this._itemPower = new L1ItemPower(this._itemInstance);
    }

    public BinaryOutputStream getStatusBytes(final L1PcInstance pc) {
        // 分类
        final int use_type = this._item.getUseType();
        //System.out.println("usertype :"+use_type);
        switch (use_type) {
            case -11: // 对读取方法调用无法分类的物品
            case -10: // 加速药水
            case -9: // 技术书
            case -8: // 料理书
            case -7: // 增HP道具
            case -6: // 增MP道具
            case -5: // 食人妖精竞赛票
            case -4: // 项圈
            case -1: // 无法使用(材料等)
            case 0: // 一般物品
            case 3: // 创造怪物魔杖(无须选取目标 - 无数量:没有任何事情发生)
            case 5: // 魔杖类型(须选取目标)
            case 6: // 瞬间移动卷轴
            case 7: // 鉴定卷轴
            case 9: // 传送回家的卷轴
            case 8: // 复活卷轴
            case 12: // 信纸
            case 13: // 信纸(寄出)
            case 14: // 请选择一个物品(道具栏位)
            case 15: // 哨子
            case 16: // 变形卷轴
            case 17: // 选取目标 (近距离)
            case 26: // 对武器施法的卷轴
            case 27: // 对盔甲施法的卷轴
            case 28: // 空的魔法卷轴
            case 29: // 瞬间移动卷轴(祝福)
            case 30: // 魔法卷轴选取目标 (远距离 无XY座标传回)
            case 31: // 圣诞卡片
            case 32: // 圣诞卡片(寄出)
            case 33: // 情人节卡片
            case 34: // 情人节卡片(寄出)
            case 35: // 白色情人节卡片
            case 36: // 白色情人节卡片(寄出)
            case 39: // 选取目标 (远距离)
            case 42: // 钓鱼杆
            case 46: // 饰品强化卷轴
            case 55: // 请选择魔法娃娃
    			if (this._item.getclassname().equals("doll.Magic_Doll")) {//魔法娃娃增加显示属性
    				return this.dollitem();
    			} else {
    				return this.etcitem();
    			}

            case -12: // 宠物用具
                final L1PetItem petItem = PetItemTable.get().getTemplate(
                        this._item.getItemId());
                // 武器
                if (petItem.isWeapom()) {
                    return this.petweapon(petItem);
                    // 防具
                } else {
                    return this.petarmor(petItem);
                }

            case -3: // 飞刀
            case -2: // 箭
                return this.arrow();

            case 38: // 食物
                return this.fooditem();

            case 10: // 照明道具
                return this.lightitem();

            case 2: // 盔甲
            case 18: // T恤
            case 19: // 斗篷
            case 20: // 手套
            case 21: // 靴
            case 22: // 头盔
            case 25: // 盾牌
                return this.armor();

            case 40: // 耳环
            case 23: // 戒指
            case 24: // 项链
            case 37: // 腰带
                return this.accessories();

            case 43: // 副助道具右
            case 44: // 副助道具左
            case 45: // 副助道具中
            case 48: // 副助道具右下
            case 47: // 副助道具左下
                return this.accessories2();

            case 1: // 武器
                return this.weapon(pc);
        }
        return null;
    }

    /**
     * 飞刀 箭
     * 
     * @return
     */
    private BinaryOutputStream arrow() {
        this._os.writeC(0x01); // 打击值
        this._os.writeC(this._item.getDmgSmall());
        this._os.writeC(this._item.getDmgLarge());
        this._os.writeC(this._item.getMaterial());
        this._os.writeD(this._itemInstance.getWeight());
        return this._os;
    }

    /**
     * 食物
     * 
     * @return
     */
    private BinaryOutputStream fooditem() {
        this._os.writeC(0x15);
        // 荣养
        this._os.writeH(this._item.getFoodVolume());
        this._os.writeC(this._item.getMaterial());
        this._os.writeD(this._itemInstance.getWeight());
        return this._os;
    }

    /**
     * 照明道具
     * 
     * @return
     */
    private BinaryOutputStream lightitem() {
        this._os.writeC(0x16);
        this._os.writeH(this._item.getLightRange());
        this._os.writeC(this._item.getMaterial());
        this._os.writeD(this._itemInstance.getWeight());
        return this._os;
    }

    /**
     * 防具类
     * 
     * @return
     */
    private BinaryOutputStream armor() {
        
    	int pw_sDr = this._item.getDamageReduction(); //伤害减少 hjx1000
        if (_itemInstance.get_power_name() != null) {
        	final int xing = _itemInstance.get_power_name().get_xing_count();
			switch (xing) { //星防具增加额外防御 hjx1000
			case 2:
			case 3:
			case 4:
			case 5:
				pw_sDr += 1; //减少一伤害
				break;
			case 6:
			case 7:
			case 8:
				pw_sDr += 2; //减少二伤害
				break;
			case 9:
				pw_sDr += 3; //减少三伤害
				break;
			}
        }
        this._os.writeC(0x13);
        // AC
        int ac = this._item.get_ac();
        if (ac < 0) {
            ac = Math.abs(ac);
        }
        this._os.writeC(ac);

        this._os.writeC(this._item.getMaterial());
        this._os.writeC(this._item.get_greater());// CNOP修正
        this._os.writeD(this._itemInstance.getWeight());

        // 强化数
        if (this._itemInstance.getEnchantLevel() != 0) {
            this._os.writeC(0x02);
            this._os.writeC(this._itemInstance.getEnchantLevel());
        }
        // 损伤度
        if (this._itemInstance.get_durability() != 0) {
            this._os.writeC(0x03);
            this._os.writeC(this._itemInstance.get_durability());
        }

        int pw_s1 = this._item.get_addstr();// 力量
        int pw_s2 = this._item.get_adddex();// 敏捷
        int pw_s3 = this._item.get_addcon();// 体质
        int pw_s4 = this._item.get_addwis();// 精神
        int pw_s5 = this._item.get_addint();// 智力
        int pw_s6 = this._item.get_addcha();// 魅力

        int pw_sHp = this._item.get_addhp() + this._itemPower.getHp();// +HP
        int pw_sMp = this._item.get_addmp() + this._itemPower.getMp();// +MP
        int pw_sMr = this._itemPower.getMr();// MR(抗魔)
        int pw_sSp = this._item.get_addsp() + this._itemPower.getSp();// SP(魔攻)

        int pw_sDg = this._item.getDmgModifierByArmor() + this._itemPower.MeleeDmg();// DG(攻击力)
        int pw_sHi = this._item.getHitModifierByArmor();// Hit(攻击成功)
        int pw_bDg = this._item.getBowDmgModifierByArmor() + this._itemPower.BowDmg();//远程攻击力

        int pw_d4_1 = this._item.get_defense_fire();// 火属性
        int pw_d4_2 = this._item.get_defense_water();// 水属性
        int pw_d4_3 = this._item.get_defense_wind();// 风属性
        int pw_d4_4 = this._item.get_defense_earth();// 地属性

        int pw_k6_1 = this._item.get_regist_freeze();// 寒冰耐性
        int pw_k6_2 = this._item.get_regist_stone();// 石化耐性
        int pw_k6_3 = this._item.get_regist_sleep();// 睡眠耐性
        int pw_k6_4 = this._item.get_regist_blind();// 暗黑耐性
        int pw_k6_5 = this._item.get_regist_stun();// 昏迷耐性
        int pw_k6_6 = this._item.get_regist_sustain();// 支撑耐性
        

        // 攻击成功
        if (pw_sHi != 0) {
            this._os.writeC(0x05);
            this._os.writeC(pw_sHi);
        }

        // 追加打击
        if (pw_sDg != 0) {
            this._os.writeC(0x06);
            this._os.writeC(pw_sDg);
        }

        // 使用可能
        int bit = 0;
        bit |= this._item.isUseRoyal() ? 1 : 0;
        bit |= this._item.isUseKnight() ? 2 : 0;
        bit |= this._item.isUseElf() ? 4 : 0;
        bit |= this._item.isUseMage() ? 8 : 0;
        bit |= this._item.isUseDarkelf() ? 16 : 0;
        bit |= this._item.isUseDragonknight() ? 32 : 0;
        bit |= this._item.isUseIllusionist() ? 64 : 0;
        this._os.writeC(0x07);
        this._os.writeC(bit);

        // 弓命中追加
        if (this._item.getBowHitModifierByArmor() != 0) {
            this._os.writeC(0x18);
            this._os.writeC(this._item.getBowHitModifierByArmor());
        }

        // 弓伤害追加
        if (pw_bDg != 0) {
            this._os.writeC(0x23);
            this._os.writeC(pw_bDg);
        }

        // 特别定义套装
        int s6_1 = 0;// 力量
        int s6_2 = 0;// 敏捷
        int s6_3 = 0;// 体质
        int s6_4 = 0;// 精神
        int s6_5 = 0;// 智力
        int s6_6 = 0;// 魅力
        int aH_1 = 0;// +HP
        int aM_1 = 0;// +MP
        int aMR_1 = 0;// MR(抗魔)
        int aSP_1 = 0;// SP(魔攻)
        int aSS_1 = 0;// 加速效果
        int d4_1 = 0;// 火属性
        int d4_2 = 0;// 水属性
        int d4_3 = 0;// 风属性
        int d4_4 = 0;// 地属性
        int k6_1 = 0;// 寒冰耐性
        int k6_2 = 0;// 石化耐性
        int k6_3 = 0;// 睡眠耐性
        int k6_4 = 0;// 暗黑耐性
        int k6_5 = 0;// 昏迷耐性
        int k6_6 = 0;// 支撑耐性
        if (_itemInstance.isMatch()) {// 完成套装
            s6_1 = _item.get_mode()[0];// 力量
            s6_2 = _item.get_mode()[1];// 敏捷
            s6_3 = _item.get_mode()[2];// 体质
            s6_4 = _item.get_mode()[3];// 精神
            s6_5 = _item.get_mode()[4];// 智力
            s6_6 = _item.get_mode()[5];// 魅力
            aH_1 = _item.get_mode()[6];// +HP
            aM_1 = _item.get_mode()[7];// +MP
            aMR_1 = _item.get_mode()[8];// MR(抗魔)
            aSP_1 = _item.get_mode()[9];// SP(魔攻)
            aSS_1 = _item.get_mode()[10];// 加速效果
            d4_1 = _item.get_mode()[11];// 火属性
            d4_2 = _item.get_mode()[12];// 水属性
            d4_3 = _item.get_mode()[13];// 风属性
            d4_4 = _item.get_mode()[14];// 地属性
            k6_1 = _item.get_mode()[15];// 寒冰耐性
            k6_2 = _item.get_mode()[16];// 石化耐性
            k6_3 = _item.get_mode()[17];// 睡眠耐性
            k6_4 = _item.get_mode()[18];// 暗黑耐性
            k6_5 = _item.get_mode()[19];// 昏迷耐性
            k6_6 = _item.get_mode()[20];// 支撑耐性
        }

        if (_itemInstance.get_power_name() != null) {
            final L1ItemPower_name power = _itemInstance.get_power_name();
            //aH_1 += power.get_xing_count() * 5; //修改附魔后　每段加5点血
            switch (power.get_hole_1()) {
                case 1:// 力 力+1
                    s6_1 += 1;
                    break;
                case 2:// 敏 敏+1
                    s6_2 += 1;
                    break;
                case 3:// 体 体+1 血+15
                    s6_3 += 1;
                    aH_1 += 15;
                    break;
                case 4:// 精 精+1 魔+15
                    s6_4 += 1;
                    aM_1 += 15;
                    break;
                case 5:// 智 智力+1
                    s6_5 += 1;
                    break;
                case 6:// 魅 魅力+1
                    s6_6 += 1;
                    break;
                case 7:// 血 血+25
                    aH_1 += 25;
                    break;
                case 8:// 魔 魔+25
                    aM_1 += 25;
                    break;
                case 9:// 攻
                    break;
                case 10:// 防 防御-2
                    break;
                case 11:// 抗 抗魔+3
                    aMR_1 += 3;
                    break;
            }
            switch (power.get_hole_2()) {
                case 1:// 力 力+1
                    s6_1 += 1;
                    break;
                case 2:// 敏 敏+1
                    s6_2 += 1;
                    break;
                case 3:// 体 体+1 血+15
                    s6_3 += 1;
                    aH_1 += 15;
                    break;
                case 4:// 精 精+1 魔+15
                    s6_4 += 1;
                    aM_1 += 15;
                    break;
                case 5:// 智 智力+1
                    s6_5 += 1;
                    break;
                case 6:// 魅 魅力+1
                    s6_6 += 1;
                    break;
                case 7:// 血 血+25
                    aH_1 += 25;
                    break;
                case 8:// 魔 魔+25
                    aM_1 += 25;
                    break;
                case 9:// 攻
                    break;
                case 10:// 防 防御-2
                    break;
                case 11:// 抗 抗魔+3
                    aMR_1 += 3;
                    break;
            }
            switch (power.get_hole_3()) {
                case 1:// 力 力+1
                    s6_1 += 1;
                    break;
                case 2:// 敏 敏+1
                    s6_2 += 1;
                    break;
                case 3:// 体 体+1 血+15
                    s6_3 += 1;
                    aH_1 += 15;
                    break;
                case 4:// 精 精+1 魔+15
                    s6_4 += 1;
                    aM_1 += 15;
                    break;
                case 5:// 智 智力+1
                    s6_5 += 1;
                    break;
                case 6:// 魅 魅力+1
                    s6_6 += 1;
                    break;
                case 7:// 血 血+25
                    aH_1 += 25;
                    break;
                case 8:// 魔 魔+25
                    aM_1 += 25;
                    break;
                case 9:// 攻
                    break;
                case 10:// 防 防御-2
                    break;
                case 11:// 抗 抗魔+3
                    aMR_1 += 3;
                    break;
            }
            switch (power.get_hole_4()) {
                case 1:// 力 力+1
                    s6_1 += 1;
                    break;
                case 2:// 敏 敏+1
                    s6_2 += 1;
                    break;
                case 3:// 体 体+1 血+15
                    s6_3 += 1;
                    aH_1 += 15;
                    break;
                case 4:// 精 精+1 魔+15
                    s6_4 += 1;
                    aM_1 += 15;
                    break;
                case 5:// 智 智力+1
                    s6_5 += 1;
                    break;
                case 6:// 魅 魅力+1
                    s6_6 += 1;
                    break;
                case 7:// 血 血+25
                    aH_1 += 25;
                    break;
                case 8:// 魔 魔+25
                    aM_1 += 25;
                    break;
                case 9:// 攻
                    break;
                case 10:// 防 防御-2
                    break;
                case 11:// 抗 抗魔+3
                    aMR_1 += 3;
                    break;
            }
            switch (power.get_hole_5()) {
                case 1:// 力 力+1
                    s6_1 += 1;
                    break;
                case 2:// 敏 敏+1
                    s6_2 += 1;
                    break;
                case 3:// 体 体+1 血+15
                    s6_3 += 1;
                    aH_1 += 15;
                    break;
                case 4:// 精 精+1 魔+15
                    s6_4 += 1;
                    aM_1 += 15;
                    break;
                case 5:// 智 智力+1
                    s6_5 += 1;
                    break;
                case 6:// 魅 魅力+1
                    s6_6 += 1;
                    break;
                case 7:// 血 血+25
                    aH_1 += 25;
                    break;
                case 8:// 魔 魔+25
                    aM_1 += 25;
                    break;
                case 9:// 攻
                    break;
                case 10:// 防 防御-2
                    break;
                case 11:// 抗 抗魔+3
                    aMR_1 += 3;
                    break;
            }
        }

        // 力量
        final int addstr = pw_s1 + s6_1;
        if (addstr != 0) {
            this._os.writeC(0x08);
            this._os.writeC(addstr);
        }
        // 敏捷
        final int adddex = pw_s2 + s6_2;
        if (adddex != 0) {
            this._os.writeC(0x09);
            this._os.writeC(adddex);
        }
        // 体质
        final int addcon = pw_s3 + s6_3;
        if (addcon != 0) {
            this._os.writeC(0x0a);
            this._os.writeC(addcon);
        }
        // 精神
        final int addwis = pw_s4 + s6_4;
        if (addwis != 0) {
            this._os.writeC(0x0b);
            this._os.writeC(addwis);
        }
        // 智力
        final int addint = pw_s5 + s6_5;
        if (addint != 0) {
            this._os.writeC(0x0c);
            this._os.writeC(addint);
        }
        // 魅力
        final int addcha = pw_s6 + s6_6;
        if (addcha != 0) {
            this._os.writeC(0x0d);
            this._os.writeC(addcha);
        }
        // +HP
        final int addhp = pw_sHp + aH_1;
        if (addhp != 0) {
            this._os.writeC(0x0e);
            this._os.writeH(addhp);
        }
        // +MP
        final int addmp = pw_sMp + aM_1;
        if (addmp != 0) {
            this._os.writeC(0x20);
            this._os.writeC(addmp);
        }
        // MR(抗魔)
        final int addmr = pw_sMr + aMR_1;
        if (addmr != 0) {
            this._os.writeC(0x0f);
            this._os.writeH(addmr);
        }
        // SP(魔攻)
        final int addsp = pw_sSp + aSP_1;
        if (addsp != 0) {
            this._os.writeC(0x11);
            this._os.writeC(addsp);
        }
		//修正防具显示回复HP/MP值 hjx1000
		final int addhpr = _item.get_addhpr();
		if (addhpr != 0) {
			this._os.writeC(0x25);
			this._os.writeC(addhpr);
		}
		final int addmpr = _item.get_addmpr();
		if (addmpr != 0) {
			this._os.writeC(0x26);
			this._os.writeC(addmpr);
		}
		//修正防具显示回复HP/MP值 end
        // 具备加速效果
		if (pw_sDr >= 1) { //显示减少伤害 hjx1000
			this._os.writeC(0x27);
			this._os.writeS("减少伤害: " + pw_sDr);
		}
        boolean haste = this._item.isHasteItem();

        if (aSS_1 == 1) {
            haste = true;
        }
        if (haste) {
            this._os.writeC(0x12);
        }
        // 增加火属性
        final int fire = pw_d4_1 + d4_1;
        if (fire != 0) {
            this._os.writeC(0x1b);
            this._os.writeC(fire);
        }
        // 增加水属性
        final int water = pw_d4_2 + d4_2;
        if (water != 0) {
            this._os.writeC(0x1c);
            this._os.writeC(water);
        }
        // 增加风属性
        final int wind = pw_d4_3 + d4_3;
        if (wind != 0) {
            this._os.writeC(0x1d);
            this._os.writeC(wind);
        }
        // 增加地属性
        final int earth = pw_d4_4 + d4_4;
        if (earth != 0) {
            this._os.writeC(0x1e);
            this._os.writeC(earth);
        }

        boolean isOut = false;
        // 寒冰耐性
        final int freeze = pw_k6_1 + k6_1;
        // System.out.println("寒冰耐性:"+freeze);
        if (freeze != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(0x21);
                this._os.writeC(0xd6);
                isOut = true;
            }
            this._os.writeC(0x0f);
            this._os.writeH(freeze);
            this._os.writeC(0x21);
            this._os.writeC(0x01);
        }
        // 石化耐性
        final int stone = pw_k6_2 + k6_2;
        // System.out.println("石化耐性:"+stone);
        if (stone != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(0x21);
                this._os.writeC(0xd6);
                isOut = true;
            }
            this._os.writeC(0x0f);
            this._os.writeH(stone);
            this._os.writeC(0x21);
            this._os.writeC(0x02);
        }
        // 睡眠耐性
        final int sleep = pw_k6_3 + k6_3;
        // System.out.println("睡眠耐性:"+sleep);
        if (sleep != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(0x21);
                this._os.writeC(0xd6);
                isOut = true;
            }
            this._os.writeC(0x0f);
            this._os.writeH(sleep);
            this._os.writeC(0x21);
            this._os.writeC(0x03);
        }
        // 暗黑耐性
        final int blind = pw_k6_4 + k6_4;
        // System.out.println("暗黑耐性:"+blind);
        if (blind != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(0x21);
                this._os.writeC(0xd6);
                isOut = true;
            }
            this._os.writeC(0x0f);
            this._os.writeH(blind);
            this._os.writeC(0x21);
            this._os.writeC(0x04);
        }
        // 昏迷耐性
        final int stun = pw_k6_5 + k6_5;
        // System.out.println("昏迷耐性:"+stun);
        if (stun != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(0x21);
                this._os.writeC(0xd6);
                isOut = true;
            }
            this._os.writeC(0x0f);
            this._os.writeH(stun);
            this._os.writeC(0x21);
            this._os.writeC(0x05);
        }
        // 支撑耐性
        final int sustain = pw_k6_6 + k6_6;
        // System.out.println("支撑耐性:"+sustain);
        if (sustain != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(0x21);
                this._os.writeC(0xd6);
                isOut = true;
            }
            this._os.writeC(0x0f);
            this._os.writeH(sustain);
            this._os.writeC(0x21);
            this._os.writeC(0x06);
        }
        //显示安定
		this._os.writeC(0x27);
		this._os.writeS("安定值: " + _item.get_safeenchant());
        //转移到期时间到详情来显示  
        if (this._itemInstance.get_time() != null) {
          final SimpleDateFormat sdf = new SimpleDateFormat(
          "yyyy-MM-dd HH:mm");
    		this._os.writeC(0x27);
    		this._os.writeS("[" + sdf.format(this._itemInstance.get_time()) + "]");
        }
		
        return this._os;
    }

    /**
     * 饰品类
     * 
     * @return
     */
    private BinaryOutputStream accessories() {
        // AC
        this._os.writeC(0x13);
        int ac = this._item.get_ac();
        if (ac < 0) {
            ac = Math.abs(ac);
        }
        this._os.writeC(ac);

        this._os.writeC(this._item.getMaterial());
        this._os.writeC(this._item.get_greater());// 饰品等级
        this._os.writeD(this._itemInstance.getWeight());

        int pw_s1 = this._item.get_addstr();// 力量
        int pw_s2 = this._item.get_adddex();// 敏捷
        int pw_s3 = this._item.get_addcon();// 体质
        int pw_s4 = this._item.get_addwis();// 精神
        int pw_s5 = this._item.get_addint();// 智力
        int pw_s6 = this._item.get_addcha();// 魅力

        int pw_sHp = this._item.get_addhp();// +HP
        int pw_sMp = this._item.get_addmp();// +MP
        int pw_sMr = this._itemPower.getMr();// MR(抗魔)
        int pw_sSp = this._item.get_addsp();// SP(魔攻)

        int pw_sDg = this._item.getDmgModifierByArmor();// DG(攻击力)
        int pw_sHi = this._item.getHitModifierByArmor();// Hit(攻击成功)

        int pw_d4_1 = this._item.get_defense_fire();// 火属性
        int pw_d4_2 = this._item.get_defense_water();// 水属性
        int pw_d4_3 = this._item.get_defense_wind();// 风属性
        int pw_d4_4 = this._item.get_defense_earth();// 地属性

        int pw_k6_1 = this._item.get_regist_freeze();// 寒冰耐性
        int pw_k6_2 = this._item.get_regist_stone();// 石化耐性
        int pw_k6_3 = this._item.get_regist_sleep();// 睡眠耐性
        int pw_k6_4 = this._item.get_regist_blind();// 暗黑耐性
        int pw_k6_5 = this._item.get_regist_stun();// 昏迷耐性
        int pw_k6_6 = this._item.get_regist_sustain();// 支撑耐性

        // 攻击成功
        if (pw_sHi != 0) {
            this._os.writeC(0x05);
            this._os.writeC(pw_sHi);
        }

        // 追加打击
        if (pw_sDg != 0) {
            this._os.writeC(0x06);
            this._os.writeC(pw_sDg);
        }

        // 使用可能
        int bit = 0;
        bit |= this._item.isUseRoyal() ? 1 : 0;
        bit |= this._item.isUseKnight() ? 2 : 0;
        bit |= this._item.isUseElf() ? 4 : 0;
        bit |= this._item.isUseMage() ? 8 : 0;
        bit |= this._item.isUseDarkelf() ? 16 : 0;
        bit |= this._item.isUseDragonknight() ? 32 : 0;
        bit |= this._item.isUseIllusionist() ? 64 : 0;
        this._os.writeC(0x07);
        this._os.writeC(bit);

        // 弓命中追加
        if (this._item.getBowHitModifierByArmor() != 0) {
            this._os.writeC(0x18);
            this._os.writeC(this._item.getBowHitModifierByArmor());
        }
        // 弓伤害追加
        if (this._item.getBowDmgModifierByArmor() != 0) {
            this._os.writeC(0x23);
            this._os.writeC(this._item.getBowDmgModifierByArmor());
        }

        // 特别定义套装
        int s6_1 = 0;// 力量
        int s6_2 = 0;// 敏捷
        int s6_3 = 0;// 体质
        int s6_4 = 0;// 精神
        int s6_5 = 0;// 智力
        int s6_6 = 0;// 魅力
        int aH_1 = 0;// +HP
        int aM_1 = 0;// +MP
        int aMR_1 = 0;// MR(抗魔)
        int aSP_1 = 0;// SP(魔攻)
        int aSS_1 = 0;// 加速效果
        int d4_1 = 0;// 火属性
        int d4_2 = 0;// 水属性
        int d4_3 = 0;// 风属性
        int d4_4 = 0;// 地属性
        int k6_1 = 0;// 寒冰耐性
        int k6_2 = 0;// 石化耐性
        int k6_3 = 0;// 睡眠耐性
        int k6_4 = 0;// 暗黑耐性
        int k6_5 = 0;// 昏迷耐性
        int k6_6 = 0;// 支撑耐性

        if (this._itemInstance.isMatch()) {// 完成套装
            s6_1 = this._item.get_mode()[0];// 力量
            s6_2 = this._item.get_mode()[1];// 敏捷
            s6_3 = this._item.get_mode()[2];// 体质
            s6_4 = this._item.get_mode()[3];// 精神
            s6_5 = this._item.get_mode()[4];// 智力
            s6_6 = this._item.get_mode()[5];// 魅力
            aH_1 = this._item.get_mode()[6];// +HP
            aM_1 = this._item.get_mode()[7];// +MP
            aMR_1 = this._item.get_mode()[8];// MR(抗魔)
            aSP_1 = this._item.get_mode()[9];// SP(魔攻)
            aSS_1 = this._item.get_mode()[10];// 加速效果
            d4_1 = this._item.get_mode()[11];// 火属性
            d4_2 = this._item.get_mode()[12];// 水属性
            d4_3 = this._item.get_mode()[13];// 风属性
            d4_4 = this._item.get_mode()[14];// 地属性
            k6_1 = this._item.get_mode()[15];// 寒冰耐性
            k6_2 = this._item.get_mode()[16];// 石化耐性
            k6_3 = this._item.get_mode()[17];// 睡眠耐性
            k6_4 = this._item.get_mode()[18];// 暗黑耐性
            k6_5 = this._item.get_mode()[19];// 昏迷耐性
            k6_6 = this._item.get_mode()[20];// 支撑耐性
        }

        // 力量
        final int addstr = pw_s1 + s6_1;
        if (addstr != 0) {
            this._os.writeC(0x08);
            this._os.writeC(addstr);
        }
        // 敏捷
        final int adddex = pw_s2 + s6_2;
        if (adddex != 0) {
            this._os.writeC(0x09);
            this._os.writeC(adddex);
        }
        // 体质
        final int addcon = pw_s3 + s6_3;
        if (addcon != 0) {
            this._os.writeC(0x0a);
            this._os.writeC(addcon);
        }
        // 精神
        final int addwis = pw_s4 + s6_4;
        if (addwis != 0) {
            this._os.writeC(0x0b);
            this._os.writeC(addwis);
        }
        // 智力
        final int addint = pw_s5 + s6_5;
        if (addint != 0) {
            this._os.writeC(0x0c);
            this._os.writeC(addint);
        }
        // 魅力
        final int addcha = pw_s6 + s6_6;
        if (addcha != 0) {
            this._os.writeC(0x0d);
            this._os.writeC(addcha);
        }

        // +HP MR 火 水 风 地 HP MP MR SP HPR MPR
        final int addhp = pw_sHp + this.greater()[4] + aH_1;
        if (addhp != 0) {
            this._os.writeC(0x0e);
            this._os.writeH(addhp);
        }

        // +MP MR 火 水 风 地 HP MP MR SP HPR MPR
        final int addmp = pw_sMp + this.greater()[5] + aM_1;
        if (addmp != 0) {
            this._os.writeC(0x20);
            this._os.writeC(addmp);
        }

        // MR(抗魔) MR 火 水 风 地 HP MP MR SP HPR MPR
        final int addmr = pw_sMr + this.greater()[6] + aMR_1;
        if (addmr != 0) {
            this._os.writeC(0x0f);
            this._os.writeH(addmr);
        }
        // SP(魔攻)火 水 风 地 HP MP MR SP HPR MPR
        final int addsp = pw_sSp + this.greater()[7] + aSP_1;
        if (addsp != 0) {
            this._os.writeC(0x11);
            this._os.writeC(addsp);
        }
		//修正首饰显示回复HP/MP值 hjx1000
		final int addhpr = _item.get_addhpr()+this.greater()[8];
		if (addhpr != 0) {
			this._os.writeC(0x25);
			this._os.writeC(addhpr);
		}
		final int addmpr = _item.get_addmpr()+this.greater()[9];
		if (addmpr != 0) {
			this._os.writeC(0x26);
			this._os.writeC(addmpr);
		}
		//修正首饰显示回复HP/MP值 end

        // 具备加速效果
        boolean haste = this._item.isHasteItem();
        if (aSS_1 == 1) {
            haste = true;
        }
        if (haste) {
            this._os.writeC(0x12);
        }

        // 增加火属性
        final int defense_fire = pw_d4_1 + this.greater()[0] + d4_1;
        if (defense_fire != 0) {
            this._os.writeC(0x1b);
            this._os.writeC(defense_fire);
        }

        // 增加水属性
        final int defense_water = pw_d4_2 + this.greater()[1] + d4_2;
        if (defense_water != 0) {
            this._os.writeC(0x1c);
            this._os.writeC(defense_water);
        }

        // 增加风属性
        final int defense_wind = pw_d4_3 + this.greater()[2] + d4_3;
        if (defense_wind != 0) {
            this._os.writeC(0x1d);
            this._os.writeC(defense_wind);
        }

        // 增加地属性
        final int defense_earth = pw_d4_4 + this.greater()[3] + d4_4;
        if (defense_earth != 0) {
            this._os.writeC(0x1e);
            this._os.writeC(defense_earth);
        }

        boolean isOut = false;
        // 寒冰耐性
        final int freeze = pw_k6_1 + k6_1;
        if (freeze != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(0x21);
                this._os.writeC(0xd6);
                isOut = true;
            }
            this._os.writeC(0x0f);
            this._os.writeH(freeze);
            this._os.writeC(0x21);
            this._os.writeC(0x01);
        }

        // 石化耐性
        final int stone = pw_k6_2 + k6_2;
        if (stone != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(0x21);
                this._os.writeC(0xd6);
                isOut = true;
            }
            this._os.writeC(0x0f);
            this._os.writeH(stone);
            this._os.writeC(0x21);
            this._os.writeC(0x02);
        }

        // 睡眠耐性
        final int sleep = pw_k6_3 + k6_3;
        if (sleep != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(0x21);
                this._os.writeC(0xd6);
                isOut = true;
            }
            this._os.writeC(0x0f);
            this._os.writeH(sleep);
            this._os.writeC(0x21);
            this._os.writeC(0x03);
        }

        // 暗黑耐性
        final int blind = pw_k6_4 + k6_4;
        if (blind != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(0x21);
                this._os.writeC(0xd6);
                isOut = true;
            }
            this._os.writeC(0x0f);
            this._os.writeH(blind);
            this._os.writeC(0x21);
            this._os.writeC(0x04);
        }

        // 昏迷耐性
        final int stun = pw_k6_5 + k6_5;
        if (stun != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(0x21);
                this._os.writeC(0xd6);
                isOut = true;
            }
            this._os.writeC(0x0f);
            this._os.writeH(stun);
            this._os.writeC(0x21);
            this._os.writeC(0x05);
        }

        // 支撑耐性
        final int sustain = pw_k6_6 + k6_6;
        if (sustain != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(0x21);
                this._os.writeC(0xd6);
                isOut = true;
            }
            this._os.writeC(0x0f);
            this._os.writeH(sustain);
            this._os.writeC(0x21);
            this._os.writeC(0x06);
        }
        if (this._item.getItemId() == 33001 ||
        		this._item.getItemId() == 33002 ||
        		this._item.getItemId() == 33003 ||
        		this._item.getItemId() == 33005 ||
        		this._item.getItemId() == 33006 ||
        		this._item.getItemId() == 33007) {
            //显示狩猎经验
    		this._os.writeC(0x27);
    		this._os.writeS("狩猎经验: " + " 10 %" );
        }
        //显示安定
		this._os.writeC(0x27);
		this._os.writeS("安定值: " + _item.get_safeenchant());
        //转移到期时间到详情来显示  
        if (this._itemInstance.get_time() != null) {
          final SimpleDateFormat sdf = new SimpleDateFormat(
          "yyyy-MM-dd HH:mm");
    		this._os.writeC(0x27);
    		this._os.writeS("[" + sdf.format(this._itemInstance.get_time()) + "]");
        }
		
        return this._os;
    }

    /**
     * 副助道具
     * 
     * @return
     */
    private BinaryOutputStream accessories2() {
        // AC
        this._os.writeC(0x13);
        int ac = this._item.get_ac();
        if (ac < 0) {
            ac = Math.abs(ac);
        }
        this._os.writeC(ac);

        this._os.writeC(this._item.getMaterial());
        this._os.writeC(this._item.get_greater());// 饰品等级
        this._os.writeD(this._itemInstance.getWeight());

        int pw_s1 = this._item.get_addstr();// 力量
        int pw_s2 = this._item.get_adddex();// 敏捷
        int pw_s3 = this._item.get_addcon();// 体质
        int pw_s4 = this._item.get_addwis();// 精神
        int pw_s5 = this._item.get_addint();// 智力
        int pw_s6 = this._item.get_addcha();// 魅力

        int pw_sHp = this._item.get_addhp();// +HP
        int pw_sMp = this._item.get_addmp();// +MP
        int pw_sMr = this._itemPower.getMr();// MR(抗魔)
        int pw_sSp = this._item.get_addsp();// SP(魔攻)

        int pw_sDg = this._item.getDmgModifierByArmor();// DG(攻击力)
        int pw_sHi = this._item.getHitModifierByArmor();// Hit(攻击成功)

        int pw_d4_1 = this._item.get_defense_fire();// 火属性
        int pw_d4_2 = this._item.get_defense_water();// 水属性
        int pw_d4_3 = this._item.get_defense_wind();// 风属性
        int pw_d4_4 = this._item.get_defense_earth();// 地属性

        int pw_k6_1 = this._item.get_regist_freeze();// 寒冰耐性
        int pw_k6_2 = this._item.get_regist_stone();// 石化耐性
        int pw_k6_3 = this._item.get_regist_sleep();// 睡眠耐性
        int pw_k6_4 = this._item.get_regist_blind();// 暗黑耐性
        int pw_k6_5 = this._item.get_regist_stun();// 昏迷耐性
        int pw_k6_6 = this._item.get_regist_sustain();// 支撑耐性
        if (_itemInstance.get_power_name() != null) {
            final L1ItemPower_name power = _itemInstance.get_power_name();
            //aH_1 += power.get_xing_count() * 5; //修改附魔后　每段加5点血
            switch (power.get_hole_1()) {
                case 1:// 力 力+1
                	pw_s1 += 1;
                    break;
                case 2:// 敏 敏+1
                	pw_s2 += 1;
                    break;
                case 3:// 体 体+1 血+15
                	pw_s3 += 1;
                	pw_sHp += 15;
                    break;
                case 4:// 精 精+1 魔+15
                	pw_s4 += 1;
                	pw_sMp += 15;
                    break;
                case 5:// 智 智力+1
                	pw_s5 += 1;
                    break;
                case 6:// 魅 魅力+1
                	pw_s6 += 1;
                    break;
                case 7:// 血 血+25
                	pw_sHp += 25;
                    break;
                case 8:// 魔 魔+25
                	pw_sMp += 25;
                    break;
                case 9:// 攻
                    break;
                case 10:// 防 防御-2
                    break;
                case 11:// 抗 抗魔+3
                	pw_sMr += 3;
                    break;
            }
            switch (power.get_hole_2()) {
            case 1:// 力 力+1
            	pw_s1 += 1;
                break;
            case 2:// 敏 敏+1
            	pw_s2 += 1;
                break;
            case 3:// 体 体+1 血+15
            	pw_s3 += 1;
            	pw_sHp += 15;
                break;
            case 4:// 精 精+1 魔+15
            	pw_s4 += 1;
            	pw_sMp += 15;
                break;
            case 5:// 智 智力+1
            	pw_s5 += 1;
                break;
            case 6:// 魅 魅力+1
            	pw_s6 += 1;
                break;
            case 7:// 血 血+25
            	pw_sHp += 25;
                break;
            case 8:// 魔 魔+25
            	pw_sMp += 25;
                break;
            case 9:// 攻
                break;
            case 10:// 防 防御-2
                break;
            case 11:// 抗 抗魔+3
            	pw_sMr += 3;
                break;
            }
            switch (power.get_hole_3()) {
            case 1:// 力 力+1
            	pw_s1 += 1;
                break;
            case 2:// 敏 敏+1
            	pw_s2 += 1;
                break;
            case 3:// 体 体+1 血+15
            	pw_s3 += 1;
            	pw_sHp += 15;
                break;
            case 4:// 精 精+1 魔+15
            	pw_s4 += 1;
            	pw_sMp += 15;
                break;
            case 5:// 智 智力+1
            	pw_s5 += 1;
                break;
            case 6:// 魅 魅力+1
            	pw_s6 += 1;
                break;
            case 7:// 血 血+25
            	pw_sHp += 25;
                break;
            case 8:// 魔 魔+25
            	pw_sMp += 25;
                break;
            case 9:// 攻
                break;
            case 10:// 防 防御-2
                break;
            case 11:// 抗 抗魔+3
            	pw_sMr += 3;
                break;
            }
            switch (power.get_hole_4()) {
            case 1:// 力 力+1
            	pw_s1 += 1;
                break;
            case 2:// 敏 敏+1
            	pw_s2 += 1;
                break;
            case 3:// 体 体+1 血+15
            	pw_s3 += 1;
            	pw_sHp += 15;
                break;
            case 4:// 精 精+1 魔+15
            	pw_s4 += 1;
            	pw_sMp += 15;
                break;
            case 5:// 智 智力+1
            	pw_s5 += 1;
                break;
            case 6:// 魅 魅力+1
            	pw_s6 += 1;
                break;
            case 7:// 血 血+25
            	pw_sHp += 25;
                break;
            case 8:// 魔 魔+25
            	pw_sMp += 25;
                break;
            case 9:// 攻
                break;
            case 10:// 防 防御-2
                break;
            case 11:// 抗 抗魔+3
            	pw_sMr += 3;
                break;
            }
            switch (power.get_hole_5()) {
            case 1:// 力 力+1
            	pw_s1 += 1;
                break;
            case 2:// 敏 敏+1
            	pw_s2 += 1;
                break;
            case 3:// 体 体+1 血+15
            	pw_s3 += 1;
            	pw_sHp += 15;
                break;
            case 4:// 精 精+1 魔+15
            	pw_s4 += 1;
            	pw_sMp += 15;
                break;
            case 5:// 智 智力+1
            	pw_s5 += 1;
                break;
            case 6:// 魅 魅力+1
            	pw_s6 += 1;
                break;
            case 7:// 血 血+25
            	pw_sHp += 25;
                break;
            case 8:// 魔 魔+25
            	pw_sMp += 25;
                break;
            case 9:// 攻
                break;
            case 10:// 防 防御-2
                break;
            case 11:// 抗 抗魔+3
            	pw_sMr += 3;
                break;
            }
        }
        // 攻击成功
        if (pw_sHi != 0) {
            this._os.writeC(0x05);
            this._os.writeC(pw_sHi);
        }

        // 追加打击
        if (pw_sDg != 0) {
            this._os.writeC(0x06);
            this._os.writeC(pw_sDg);
        }

        // 使用可能
        int bit = 0;
        bit |= this._item.isUseRoyal() ? 1 : 0;
        bit |= this._item.isUseKnight() ? 2 : 0;
        bit |= this._item.isUseElf() ? 4 : 0;
        bit |= this._item.isUseMage() ? 8 : 0;
        bit |= this._item.isUseDarkelf() ? 16 : 0;
        bit |= this._item.isUseDragonknight() ? 32 : 0;
        bit |= this._item.isUseIllusionist() ? 64 : 0;
        this._os.writeC(0x07);
        this._os.writeC(bit);

        // 弓命中追加
        if (this._item.getBowHitModifierByArmor() != 0) {
            this._os.writeC(0x18);
            this._os.writeC(this._item.getBowHitModifierByArmor());
        }
        // 弓伤害追加
        if (this._item.getBowDmgModifierByArmor() != 0) {
            this._os.writeC(0x23);
            this._os.writeC(this._item.getBowDmgModifierByArmor());
        }

        // 力量
        final int addstr = pw_s1;
        if (addstr != 0) {
            this._os.writeC(0x08);
            this._os.writeC(addstr);
        }
        // 敏捷
        final int adddex = pw_s2;
        if (adddex != 0) {
            this._os.writeC(0x09);
            this._os.writeC(adddex);
        }
        // 体质
        final int addcon = pw_s3;
        if (addcon != 0) {
            this._os.writeC(0x0a);
            this._os.writeC(addcon);
        }
        // 精神
        final int addwis = pw_s4;
        if (addwis != 0) {
            this._os.writeC(0x0b);
            this._os.writeC(addwis);
        }
        // 智力
        final int addint = pw_s5;
        if (addint != 0) {
            this._os.writeC(0x0c);
            this._os.writeC(addint);
        }
        // 魅力
        final int addcha = pw_s6;
        if (addcha != 0) {
            this._os.writeC(0x0d);
            this._os.writeC(addcha);
        }

        // +HP MR 火 水 风 地 HP MP MR SP HPR MPR
        final int addhp = pw_sHp;
        if (addhp != 0) {
            this._os.writeC(0x0e);
            this._os.writeH(addhp);
        }

        // +MP MR 火 水 风 地 HP MP MR SP HPR MPR
        final int addmp = pw_sMp;
        if (addmp != 0) {
            this._os.writeC(0x20);
            this._os.writeC(addmp);
        }

        // MR(抗魔) MR 火 水 风 地 HP MP MR SP HPR MPR
        final int addmr = pw_sMr;
        if (addmr != 0) {
            this._os.writeC(0x0f);
            this._os.writeH(addmr);
        }
        // SP(魔攻)火 水 风 地 HP MP MR SP HPR MPR
        final int addsp = pw_sSp;
        if (addsp != 0) {
            this._os.writeC(0x11);
            this._os.writeC(addsp);
        }
		//修正副助道具显示回复HP/MP值 hjx1000
		final int addhpr = _item.get_addhpr();
		if (addhpr != 0) {
			this._os.writeC(0x25);
			this._os.writeC(addhpr);
		}
		final int addmpr = _item.get_addmpr();
		if (addmpr != 0) {
			this._os.writeC(0x26);
			this._os.writeC(addmpr);
		}
		//修正副助道具显示回复HP/MP值 end

        // 具备加速效果
        boolean haste = this._item.isHasteItem();
        if (haste) {
            this._os.writeC(0x12);
        }

        // 增加火属性
        final int defense_fire = pw_d4_1;
        if (defense_fire != 0) {
            this._os.writeC(0x1b);
            this._os.writeC(defense_fire);
        }

        // 增加水属性
        final int defense_water = pw_d4_2;
        if (defense_water != 0) {
            this._os.writeC(0x1c);
            this._os.writeC(defense_water);
        }

        // 增加风属性
        final int defense_wind = pw_d4_3;
        if (defense_wind != 0) {
            this._os.writeC(0x1d);
            this._os.writeC(defense_wind);
        }

        // 增加地属性
        final int defense_earth = pw_d4_4;
        if (defense_earth != 0) {
            this._os.writeC(0x1e);
            this._os.writeC(defense_earth);
        }

        boolean isOut = false;
        // 寒冰耐性
        final int freeze = pw_k6_1;
        if (freeze != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(0x21);
                this._os.writeC(0xd6);
                isOut = true;
            }
            this._os.writeC(0x0f);
            this._os.writeH(freeze);
            this._os.writeC(0x21);
            this._os.writeC(0x01);
        }

        // 石化耐性
        final int stone = pw_k6_2;
        if (stone != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(0x21);
                this._os.writeC(0xd6);
                isOut = true;
            }
            this._os.writeC(0x0f);
            this._os.writeH(stone);
            this._os.writeC(0x21);
            this._os.writeC(0x02);
        }

        // 睡眠耐性
        final int sleep = pw_k6_3;
        if (sleep != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(0x21);
                this._os.writeC(0xd6);
                isOut = true;
            }
            this._os.writeC(0x0f);
            this._os.writeH(sleep);
            this._os.writeC(0x21);
            this._os.writeC(0x03);
        }

        // 暗黑耐性
        final int blind = pw_k6_4;
        if (blind != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(0x21);
                this._os.writeC(0xd6);
                isOut = true;
            }
            this._os.writeC(0x0f);
            this._os.writeH(blind);
            this._os.writeC(0x21);
            this._os.writeC(0x04);
        }

        // 昏迷耐性
        final int stun = pw_k6_5;
        if (stun != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(0x21);
                this._os.writeC(0xd6);
                isOut = true;
            }
            this._os.writeC(0x0f);
            this._os.writeH(stun);
            this._os.writeC(0x21);
            this._os.writeC(0x05);
        }

        // 支撑耐性
        final int sustain = pw_k6_6;
        if (sustain != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(0x21);
                this._os.writeC(0xd6);
                isOut = true;
            }
            this._os.writeC(0x0f);
            this._os.writeH(sustain);
            this._os.writeC(0x21);
            this._os.writeC(0x06);
        }
        //转移到期时间到详情来显示  
        if (this._itemInstance.get_time() != null) {
          final SimpleDateFormat sdf = new SimpleDateFormat(
          "yyyy-MM-dd HH:mm");
    		this._os.writeC(0x27);
    		this._os.writeS("[" + sdf.format(this._itemInstance.get_time()) + "]");
        }
        return this._os;
    }

    // 属性武器
    private static final String[][] _attrEnchantString = new String[][] {
            new String[] { "1%束缚敌人0.8秒", "2%束缚敌人1.0秒", "3%束缚敌人1.5秒" },// 地之, 崩裂,
                                                                      // 地灵
            new String[] { "1%发动1.2倍伤害", "2%发动1.4倍伤害", "3%发动1.6倍伤害" },// 火之, 烈焰,
                                                                      // 火灵
            new String[] { "1%吸血吸魔", "2%吸血吸魔", "3%吸血吸魔" },// 水之, 海啸, 水灵
            new String[] { "1%发动1格范围伤害", "2%发动2格范围伤害", "3%发动3格范围伤害" },// 风之, 暴风,
                                                                      // 风灵
            // ADD LOLI
            new String[] { "1%召唤光裂", "2%召唤光裂", "3%召唤光裂" },// "光之 ", "闪耀 ", "光灵 "
            new String[] { "1%施展闇盲", "2%施展闇盲", "3%施展闇盲" },// "暗之 ", "阴影 ", "暗灵 "
            new String[] { "1%施展魔封", "2%施展魔封", "3%施展魔封" },// "圣之 ", "神圣 ", "圣灵 "
            new String[] { "1%施展变形术", "2%施展变形术", "3%施展变形术" },// "邪之 ", "邪恶 ",
                                                             // "邪灵 "
    };

    /**
     * 武器
     * 
     * @return
     */
    private BinaryOutputStream weapon(final L1PcInstance owner) {
        // 打击值
        this._os.writeC(0x01);
        this._os.writeC(this._item.getDmgSmall());
        this._os.writeC(this._item.getDmgLarge());

        this._os.writeC(this._item.getMaterial());
        this._os.writeD(this._itemInstance.getWeight());

        // 强化数
        if (this._itemInstance.getEnchantLevel() != 0) {
            this._os.writeC(0x02);
            this._os.writeC(this._itemInstance.getEnchantLevel());
        }
        // 损伤度
        if (this._itemInstance.get_durability() != 0) {
            this._os.writeC(0x03);
            this._os.writeC(this._itemInstance.get_durability());
        }
        // 两手武器
        if (this._item.isTwohandedWeapon()) {
            this._os.writeC(0x04);
        }

        int get_addstr = this._item.get_addstr();// 力量
        int get_adddex = this._item.get_adddex();// 敏捷
        int get_addcon = this._item.get_addcon();// 体质
        int get_addwis = this._item.get_addwis();// 精神
        int get_addint = this._item.get_addint();// 智力
        int get_addcha = this._item.get_addcha();// 魅力

        int get_addhp = this._item.get_addhp();// +HP
        int get_addmp = this._item.get_addmp();// +MP
        int mr = this._itemPower.getMr();// MR(抗魔)

        int addWeaponSp = this._item.get_addsp();// SP(魔攻)
        int addDmgModifier = this._item.getDmgModifier();// DG(攻击力)
        int addHitModifier = this._item.getHitModifier();// Hit(攻击成功)

        int pw_d4_1 = this._item.get_defense_fire();// 火属性
        int pw_d4_2 = this._item.get_defense_water();// 水属性
        int pw_d4_3 = this._item.get_defense_wind();// 风属性
        int pw_d4_4 = this._item.get_defense_earth();// 地属性

        int pw_k6_1 = this._item.get_regist_freeze();// 寒冰耐性
        int pw_k6_2 = this._item.get_regist_stone();// 石化耐性
        int pw_k6_3 = this._item.get_regist_sleep();// 睡眠耐性
        int pw_k6_4 = this._item.get_regist_blind();// 暗黑耐性
        int pw_k6_5 = this._item.get_regist_stun();// 昏迷耐性
        int pw_k6_6 = this._item.get_regist_sustain();// 支撑耐性
        int pro = 0;//显示武器暴击值 (小怪)
        int pro1 = 0;//显示武器暴击值 (小怪)
        int pro2 = 0;//显示武器暴击值(大怪)
        int por_red = 0;//致命一击机率
        
        if (_itemInstance.get_power_name() != null) {
            final L1ItemPower_name power = _itemInstance.get_power_name();
        	final int pro_x = power.get_xing_count()/*_itemInstance.getEnchantLevel() - Math.max(4, _item.get_safeenchant()) - 1*/;
            pro = this._item.getDmgModifier() + _itemInstance.getDmgByMagic() + 1;
            pro1 = this._item.getDmgModifier() + _itemInstance.getDmgByMagic() + this._item.getDmgSmall();
            pro2 = this._item.getDmgModifier() + _itemInstance.getDmgByMagic() + this._item.getDmgLarge();

            switch (this._item.getType()) {
    		case 4:// 弓
    		case 10:// 鐵手甲
    		case 13:// 弓(單手)
    			pro *= (pro_x * 0.4 + 0.6);
    			pro1 *= (pro_x * 0.4 + 0.6);
    			pro2 *= (pro_x * 0.4 + 0.6);
    			if (owner != null) {
    				final int DEX_1 = owner.getDex();//敏捷 == 暴击机率
    				por_red = DEX_1;
    			}
    			break;
    		case 11: //钢爪
    		case 12: //双刀
    		case 15:// 雙手斧
    		case 1:// 劍
    		case 2:// 匕首
    		case 3:// 雙手劍
    		case 5:// 矛(雙手)
    		case 6:// 斧(單手)
    		case 14:// 矛(單手)
    		case 18:// 鎖鏈劍
    			pro *= (pro_x * 0.3 + 0.55);
    			pro1 *= (pro_x * 0.3 + 0.55);
    			pro2 *= (pro_x * 0.3 + 0.55);
    			if (owner != null) {
        			final int STR_1 = owner.getStr();//力量 == 暴击机率
        			por_red = STR_1;
    			}
    			break;
    		case 17:// 奇古獸
    		case 7:// 魔杖
    		case 16:// 魔杖(雙手)
    			pro *= (pro_x * 0.4 + 0.6);
    			pro1 *= (pro_x * 0.4 + 0.6);
    			pro2 *= (pro_x * 0.4 + 0.6);
    			if (owner != null) {
        			final int jinagji = owner.getInt();//智力  == 暴击机率
        			por_red = jinagji;
    			}
    			break;
            }
            switch (power.get_hole_1()) {
                case 1:// 力 力+1
                    get_addstr += 1;
                    break;
                case 2:// 敏 敏+1
                    get_adddex += 1;
                    break;
                case 3:// 体 体+1 血+15
                    get_addcon += 1;
                    get_addhp += 15;
                    break;
                case 4:// 精 精+1 魔+15
                    get_addwis += 1;
                    get_addmp += 15;
                    break;
                case 5:// 智 智力+1
                    get_addint += 1;
                    break;
                case 6:// 魅 魅力+1
                    get_addcha += 1;
                    break;
                case 7:// 血 血+25
                    get_addhp += 25;
                    break;
                case 8:// 魔 魔+25
                    get_addmp += 25;
                    break;
                case 9:// 攻 额外攻击+3
                    addDmgModifier += 3;
                    break;
            }
            switch (power.get_hole_2()) {
                case 1:// 力 力+1
                    get_addstr += 1;
                    break;
                case 2:// 敏 敏+1
                    get_adddex += 1;
                    break;
                case 3:// 体 体+1 血+15
                    get_addcon += 1;
                    get_addhp += 15;
                    break;
                case 4:// 精 精+1 魔+15
                    get_addwis += 1;
                    get_addmp += 15;
                    break;
                case 5:// 智 智力+1
                    get_addint += 1;
                    break;
                case 6:// 魅 魅力+1
                    get_addcha += 1;
                    break;
                case 7:// 血 血+25
                    get_addhp += 25;
                    break;
                case 8:// 魔 魔+25
                    get_addmp += 25;
                    break;
                case 9:// 攻 额外攻击+3
                    addDmgModifier += 3;
                    break;
            }
            switch (power.get_hole_3()) {
                case 1:// 力 力+1
                    get_addstr += 1;
                    break;
                case 2:// 敏 敏+1
                    get_adddex += 1;
                    break;
                case 3:// 体 体+1 血+15
                    get_addcon += 1;
                    get_addhp += 15;
                    break;
                case 4:// 精 精+1 魔+15
                    get_addwis += 1;
                    get_addmp += 15;
                    break;
                case 5:// 智 智力+1
                    get_addint += 1;
                    break;
                case 6:// 魅 魅力+1
                    get_addcha += 1;
                    break;
                case 7:// 血 血+25
                    get_addhp += 25;
                    break;
                case 8:// 魔 魔+25
                    get_addmp += 25;
                    break;
                case 9:// 攻 额外攻击+3
                    addDmgModifier += 3;
                    break;
            }
            switch (power.get_hole_4()) {
                case 1:// 力 力+1
                    get_addstr += 1;
                    break;
                case 2:// 敏 敏+1
                    get_adddex += 1;
                    break;
                case 3:// 体 体+1 血+15
                    get_addcon += 1;
                    get_addhp += 15;
                    break;
                case 4:// 精 精+1 魔+15
                    get_addwis += 1;
                    get_addmp += 15;
                    break;
                case 5:// 智 智力+1
                    get_addint += 1;
                    break;
                case 6:// 魅 魅力+1
                    get_addcha += 1;
                    break;
                case 7:// 血 血+25
                    get_addhp += 25;
                    break;
                case 8:// 魔 魔+25
                    get_addmp += 25;
                    break;
                case 9:// 攻 额外攻击+3
                    addDmgModifier += 3;
                    break;
            }
            switch (power.get_hole_5()) {
                case 1:// 力 力+1
                    get_addstr += 1;
                    break;
                case 2:// 敏 敏+1
                    get_adddex += 1;
                    break;
                case 3:// 体 体+1 血+15
                    get_addcon += 1;
                    get_addhp += 15;
                    break;
                case 4:// 精 精+1 魔+15
                    get_addwis += 1;
                    get_addmp += 15;
                    break;
                case 5:// 智 智力+1
                    get_addint += 1;
                    break;
                case 6:// 魅 魅力+1
                    get_addcha += 1;
                    break;
                case 7:// 血 血+25
                    get_addhp += 25;
                    break;
                case 8:// 魔 魔+25
                    get_addmp += 25;
                    break;
                case 9:// 攻 额外攻击+3
                    addDmgModifier += 3;
                    break;
            }
            addDmgModifier += power.get_xing_count(); //增加显示，，附魔每点增加1伤害
            addHitModifier += power.get_xing_count() << 1;//增加显示，，附魔每点增加2命中
        }

        // 攻击成功
        // int addHitModifier = this._item.getHitModifier() + pw_sHi;
        if (addHitModifier != 0) {
            this._os.writeC(0x05);
            this._os.writeC(addHitModifier);
        }

        // 追加打击
        // int addDmgModifier = this._item.getDmgModifier() + pw_sDg;
        if (addDmgModifier != 0) {
            this._os.writeC(0x06);
            this._os.writeC(addDmgModifier);
        }
        //显示武器暴击值
        if (pro1 != 0) {
    		this._os.writeC(0x27);
    		this._os.writeS("致命 " + pro + "～" + pro1 + "/" + pro + "～" + pro2);
        }
        //显示暴击机率值
        if (por_red != 0 && this._itemInstance.isEquipped()) {
    		this._os.writeC(0x27);
    		this._os.writeS("致命机率 " + por_red + "%");
        }

        // 使用可能
        int bit = 0;
        bit |= this._item.isUseRoyal() ? 1 : 0;
        bit |= this._item.isUseKnight() ? 2 : 0;
        bit |= this._item.isUseElf() ? 4 : 0;
        bit |= this._item.isUseMage() ? 8 : 0;
        bit |= this._item.isUseDarkelf() ? 16 : 0;
        bit |= this._item.isUseDragonknight() ? 32 : 0;
        bit |= this._item.isUseIllusionist() ? 64 : 0;
        this._os.writeC(0x07);
        this._os.writeC(bit);

        // 弓命中追加
        /*
         * if (_item.getBowHitModifierByArmor() != 0) { os.writeC(24);
         * os.writeC(_item.getBowHitModifierByArmor()); } // 弓伤害追加 if
         * (_item.getBowDmgModifierByArmor() != 0) { os.writeC(35);
         * os.writeC(_item.getBowDmgModifierByArmor()); }
         */
        
        // 晕迷命中
        if ((this._itemInstance.getItemId() == 59)
                || (this._itemInstance.getItemId() == 100059)) {
    		this._os.writeC(0x27);
    		this._os.writeS("昏迷命中 + " + 5);
        }
        // MP吸收
        if ((this._itemInstance.getItemId() == 126)
                || (this._itemInstance.getItemId() == 127)) {
            this._os.writeC(0x10);
        }
        // HP吸收
        if (this._itemInstance.getItemId() == 262) {
            this._os.writeC(0x22);
        }

        // int get_addstr = this._item.get_addstr();
        // STR~CHA
        if (get_addstr != 0) {
            this._os.writeC(0x08);
            this._os.writeC(get_addstr);
        }

        // int get_adddex = this._item.get_adddex();
        if (get_adddex != 0) {
            this._os.writeC(0x09);
            this._os.writeC(get_adddex);
        }

        // int get_addcon = this._item.get_addcon();
        if (get_addcon != 0) {
            this._os.writeC(0x0a);
            this._os.writeC(get_addcon);
        }

        // int get_addwis = this._item.get_addwis();
        if (get_addwis != 0) {
            this._os.writeC(0x0b);
            this._os.writeC(get_addwis);
        }

        // int get_addint = this._item.get_addint();
        if (get_addint != 0) {
            this._os.writeC(0x0c);
            this._os.writeC(get_addint);
        }

        // int get_addcha = this._item.get_addcha();
        if (get_addcha != 0) {
            this._os.writeC(0x0d);
            this._os.writeC(get_addcha);
        }

        // HP, MP

        // int get_addhp = this._item.get_addhp();
        if (get_addhp != 0) {
            this._os.writeC(0x0e);
            this._os.writeH(get_addhp);
        }

        // int get_addmp = this._item.get_addmp();
        if (get_addmp != 0) {
            this._os.writeC(0x20);
            this._os.writeC(get_addmp);
        }

        // MR
        // final int mr = this._itemPower.getMr();
        if (mr != 0) {
            this._os.writeC(0x0f);
            this._os.writeH(mr);
        }
        // SP(魔法攻击力)
        // int addWeaponSp = this._item.get_addsp() + pw_sSp;
        if (addWeaponSp != 0) {
            this._os.writeC(0x11);
            this._os.writeC(addWeaponSp);
        }
		//修正武器显示回复HP/MP值 hjx1000
		final int addhpr = _item.get_addhpr();
		if (addhpr != 0) {
			this._os.writeC(0x25);
			this._os.writeC(addhpr);
		}
		final int addmpr = _item.get_addmpr();
		if (addmpr != 0) {
			this._os.writeC(0x26);
			this._os.writeC(addmpr);
		}
		//修正武器显示回复HP/MP值 end
        // 具备加速效果
        if (this._item.isHasteItem()) {
            this._os.writeC(0x12);
        }
        // 增加火属性
        if (pw_d4_1 != 0) {
            this._os.writeC(0x1b);
            this._os.writeC(pw_d4_1);
        }
        // 增加水属性
        if (pw_d4_2 != 0) {
            this._os.writeC(0x1c);
            this._os.writeC(pw_d4_2);
        }
        // 增加风属性
        if (pw_d4_3 != 0) {
            this._os.writeC(0x1d);
            this._os.writeC(pw_d4_3);
        }
        // 增加地属性
        if (pw_d4_4 != 0) {
            this._os.writeC(0x1e);
            this._os.writeC(pw_d4_4);
        }

        // 冻结耐性
        if (pw_k6_1 != 0) {
            this._os.writeC(0x0f);
            this._os.writeH(pw_k6_1);
            this._os.writeC(0x21);
            this._os.writeC(0x01);
        }
        // 石化耐性
        if (pw_k6_2 != 0) {
            this._os.writeC(0x0f);
            this._os.writeH(pw_k6_2);
            this._os.writeC(0x21);
            this._os.writeC(0x02);
        }
        // 睡眠耐性
        if (pw_k6_3 != 0) {
            this._os.writeC(0x0f);
            this._os.writeH(pw_k6_3);
            this._os.writeC(0x21);
            this._os.writeC(0x03);
        }
        // 暗闇耐性
        if (pw_k6_4 != 0) {
            this._os.writeC(0x0f);
            this._os.writeH(pw_k6_4);
            this._os.writeC(0x21);
            this._os.writeC(0x04);
        }
        // 昏迷耐性
        if (pw_k6_5 != 0) {
            this._os.writeC(0x0f);
            this._os.writeH(pw_k6_5);
            this._os.writeC(0x21);
            this._os.writeC(0x05);
        }
        // 支撑耐性
        if (pw_k6_6 != 0) {
            this._os.writeC(0x0f);
            this._os.writeH(pw_k6_6);
            this._os.writeC(0x21);
            this._os.writeC(0x06);
        }
        //显示安定
		this._os.writeC(0x27);
		this._os.writeS("安定值: " + _item.get_safeenchant());
        //转移到期时间到详情来显示  
        if (this._itemInstance.get_time() != null) {
          final SimpleDateFormat sdf = new SimpleDateFormat(
          "yyyy-MM-dd HH:mm");
    		this._os.writeC(0x27);
    		this._os.writeS("[" + sdf.format(this._itemInstance.get_time()) + "]");
        }
        return this._os;
    }

    /**
     * 额外文字显示
     * 
     * @param hole
     *            类型
     * @param type
     *            1:武器 2:防具
     * @return
     */
    private String set_other_msg(final int hole, final int type) {
        String otherMsg = null;
        switch (hole) {
            case 1:// 力 力+1
                otherMsg = "[力]";
                break;
            case 2:// 敏 敏+1
                otherMsg = "[敏]";
                break;
            case 3:// 体 体+1 血+15
                otherMsg = "[体]";
                break;
            case 4:// 精 精+1 魔+15
                otherMsg = "[精]";
                break;
            case 5:// 智 智力+1
                otherMsg = "[智]";
                break;
            case 6:// 魅 魅力+1
                otherMsg = "[魅]";
                break;
            case 7:// 血 血+25
                otherMsg = "[生]";
                break;
            case 8:// 魔 魔+25
                otherMsg = "[魔]";
                break;
            case 9:// 攻 额外攻击+3
                otherMsg = "[攻]";
                break;
            case 10:// 防 防御-23
                otherMsg = "[防]";
                break;
            case 11:// 抗 抗魔+3
                otherMsg = "[抗]";
                break;
        }
        return otherMsg;
    }

    /**
     * 一般道具
     * 
     * @return
     */
    private BinaryOutputStream etcitem() {
        this._os.writeC(0x17); // 材质
        this._os.writeC(this._item.getMaterial());
        this._os.writeD(this._itemInstance.getWeight());
        //转移到期时间到详情来显示  
        if (this._itemInstance.get_time() != null) {
          final SimpleDateFormat sdf = new SimpleDateFormat(
          "yyyy-MM-dd HH:mm");
    		this._os.writeC(0x27);
    		this._os.writeS("[" + sdf.format(this._itemInstance.get_time()) + "]");
        }
        return this._os;
    }
    
	/**
	 * 魔法娃娃item
	 * @return
	 */
    private BinaryOutputStream dollitem() {
    	final L1Doll type = DollPowerTable.get().get_type(this._itemInstance.getItemId());
		if (type != null) {
			final int[] powlists = type.get_powlist();
			for (int i = 0 ; i < powlists.length ; i++) {
				final int pow = powlists[i];
				if (pow != 0) {
					final String MSG = DollPowerTable.get().get_powMsg(pow);
					this._os.writeC(0x27);
					this._os.writeS(MSG);
				}
			}
		}
        this._os.writeC(0x17); // 材质
        this._os.writeC(this._item.getMaterial());
        this._os.writeD(this._itemInstance.getWeight());
        //转移到期时间到详情来显示  
        if (this._itemInstance.get_time() != null) {
          final SimpleDateFormat sdf = new SimpleDateFormat(
          "yyyy-MM-dd HH:mm");
    		this._os.writeC(0x27);
    		this._os.writeS("[" + sdf.format(this._itemInstance.get_time()) + "]");
        }
    	return this._os;
    }

    /**
     * 宠物防具
     * 
     * @return
     */
    private BinaryOutputStream petarmor(final L1PetItem petItem) {
        this._os.writeC(0x13);
        int ac = petItem.getAddAc();
        if (ac < 0) {
            ac = Math.abs(ac);
        }
        this._os.writeC(ac);
        this._os.writeC(this._item.getMaterial());
        this._os.writeD(this._itemInstance.getWeight());

        if (petItem.getHitModifier() != 0) {
            this._os.writeC(5);
            this._os.writeC(petItem.getHitModifier());
        }

        if (petItem.getDamageModifier() != 0) {
            this._os.writeC(6);
            this._os.writeC(petItem.getDamageModifier());
        }

        if (petItem.isHigher()) {
            this._os.writeC(7);
            this._os.writeC(128);
        }

        if (petItem.getAddStr() != 0) {
            this._os.writeC(8);
            this._os.writeC(petItem.getAddStr());
        }
        if (petItem.getAddDex() != 0) {
            this._os.writeC(9);
            this._os.writeC(petItem.getAddDex());
        }
        if (petItem.getAddCon() != 0) {
            this._os.writeC(10);
            this._os.writeC(petItem.getAddCon());
        }
        if (petItem.getAddWis() != 0) {
            this._os.writeC(11);
            this._os.writeC(petItem.getAddWis());
        }
        if (petItem.getAddInt() != 0) {
            this._os.writeC(12);
            this._os.writeC(petItem.getAddInt());
        }

        // HP, MP
        if (petItem.getAddHp() != 0) {
            this._os.writeC(14);
            this._os.writeH(petItem.getAddHp());
        }
        if (petItem.getAddMp() != 0) {
            this._os.writeC(32);
            this._os.writeC(petItem.getAddMp());
        }
        // MR
        if (petItem.getAddMr() != 0) {
            this._os.writeC(15);
            this._os.writeH(petItem.getAddMr());
        }
        // SP(魔力)
        if (petItem.getAddSp() != 0) {
            this._os.writeC(17);
            this._os.writeC(petItem.getAddSp());
        }
        return this._os;
    }

    /**
     * 宠物武器
     * 
     * @return
     */
    private BinaryOutputStream petweapon(final L1PetItem petItem) {
        this._os.writeC(0x01); // 打击值
        this._os.writeC(0x00);
        this._os.writeC(0x00);
        this._os.writeC(this._item.getMaterial());
        this._os.writeD(this._itemInstance.getWeight());

        if (petItem.isHigher()) {
            this._os.writeC(7);
            this._os.writeC(128);
        }

        if (petItem.getAddStr() != 0) {
            this._os.writeC(8);
            this._os.writeC(petItem.getAddStr());
        }
        if (petItem.getAddDex() != 0) {
            this._os.writeC(9);
            this._os.writeC(petItem.getAddDex());
        }
        if (petItem.getAddCon() != 0) {
            this._os.writeC(10);
            this._os.writeC(petItem.getAddCon());
        }
        if (petItem.getAddWis() != 0) {
            this._os.writeC(11);
            this._os.writeC(petItem.getAddWis());
        }
        if (petItem.getAddInt() != 0) {
            this._os.writeC(12);
            this._os.writeC(petItem.getAddInt());
        }

        // HP, MP
        if (petItem.getAddHp() != 0) {
            this._os.writeC(14);
            this._os.writeH(petItem.getAddHp());
        }
        if (petItem.getAddMp() != 0) {
            this._os.writeC(32);
            this._os.writeC(petItem.getAddMp());
        }
        // MR
        if (petItem.getAddMr() != 0) {
            this._os.writeC(15);
            this._os.writeH(petItem.getAddMr());
        }
        return this._os;
    }

    /**
     * 饰品能力显示 火, 水, 风, 地, HP, MP, MR, SP, HPR, MPR
     */
    private int[] greater() {
		final int level = this._itemInstance.getEnchantLevel();

		int[] rint = new int[10];
		switch (this._itemInstance.getItem().get_greater()) {
		case 0:// 高等
			switch (level) {
			case 0:
				break;
			case 1:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{1,1,1,1,5,2,0,0,0,0};
				break;
			case 2:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{2,2,2,2,10,4,0,0,0,0};
				break;
			case 3:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{3,3,3,3,15,6,0,0,0,0};
				break;
			case 4:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{4,4,4,4,20,8,0,0,0,0};
				break;
			case 5:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{5,5,5,5,25,10,0,0,0,0};
				break;
			/*case 6:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{6,6,6,6,0,0,0,0,1,1};
				break;
			case 7:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{10,10,10,10,0,0,0,0,3,3};
				break;
			default:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{15,15,15,15,0,0,0,0,3,3};
				break;*/
				//修正为仿官显示能力值 hjx1000
			default:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{level,level,level,level,level*5,level*2,0,0,1,1};
				break;
			}
			break;
			
		case 1:// 中等
			switch (level) {
			case 0:
				break;
			case 1:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{0,0,0,0,5,0,0,0,0,0};
				break;
			case 2:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{0,0,0,0,10,0,0,0,0,0};
				break;
			case 3:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{0,0,0,0,15,0,0,0,0,0};
				break;
			case 4:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{0,0,0,0,20,0,0,0,0,0};
				break;
			case 5:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{0,0,0,0,25,0,0,0,0,0};
				break;
			/*case 6:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{0,0,0,0,30,0,2,0,0,0};
				break;
			case 7:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{0,0,0,0,40,0,7,0,0,0};
				break;
			default:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{0,0,0,0,40,0,12,0,0,0};
				break;*/
				//修正为仿官显示能力值 hjx1000
			default:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{0,0,0,0,level*5,0,1,0,0,0};
				break;
			}
			break;
			
		case 2:// 初等
			switch (level) {
			case 0:
				break;
			case 1:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{0,0,0,0,0,2,0,0,0,0};
				break;
			case 2:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{0,0,0,0,0,4,0,0,0,0};
				break;
			case 3:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{0,0,0,0,0,6,0,0,0,0};
				break;
			case 4:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{0,0,0,0,0,8,0,0,0,0};
				break;
			case 5:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{0,0,0,0,0,10,0,0,0,0};
				break;
			/*case 6:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{0,0,0,0,0,25,0,1,0,0};
				break;
			case 7:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{0,0,0,0,0,40,0,2,0,0};
				break;
			default:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{0,0,0,0,0,40,0,3,0,0};
				break;*/
				//修正为仿官显示能力值 hjx1000
			default:
				// 火, 水, 風, 地, HP, MP, MR, SP, HPR, MPR
				rint = new int[]{0,0,0,0,0,level*2,0,1,0,0};
				break;
			}
			break;
			
		default:
			break;
		}
		return rint;
	}
}
