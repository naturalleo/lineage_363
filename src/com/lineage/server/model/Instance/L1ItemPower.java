package com.lineage.server.model.Instance;

import java.util.HashMap;
import java.util.Map;

import com.lineage.server.serverpackets.S_SPMR;

/**
 * 物品能力值
 * 
 * @author dexc
 * 
 */
public class L1ItemPower {

    private final L1ItemInstance _itemInstance;

    // 抗魔 = 追加质
    public static final Map<Integer, Integer> MR2 = new HashMap<Integer, Integer>();
    
    // MP = 追加质
    public static final Map<Integer, Integer> Mp2 = new HashMap<Integer, Integer>();
    
    // HP = 追加质
    public static final Map<Integer, Integer> Hp2 = new HashMap<Integer, Integer>();
    
    // SP = 追加质
    public static final Map<Integer, Integer> Sp2 = new HashMap<Integer, Integer>();
    
    //近身伤害
    public static final Map<Integer, Integer> MeleeDmg = new HashMap<Integer, Integer>();
    
    //远程伤害
    public static final Map<Integer, Integer> BowDmg = new HashMap<Integer, Integer>();

    /**
     * 载入强化质影响抗魔的装备
     */
    public static void load() {
        // MR * 1
        MR2.put(20011, new Integer(1));// 抗魔法头盔
        MR2.put(120011, new Integer(1));// 抗魔法头盔
        MR2.put(20110, new Integer(1));// 抗魔法链甲
        MR2.put(21108, new Integer(1));// 魔法抵抗内衣
        MR2.put(20117, new Integer(1));// 巴风特盔甲 add hjx1000
        // 灭魔系列装备 by hjx1000
        MR2.put(28015, new Integer(1));// 灭魔的金属盔甲
        MR2.put(28016, new Integer(1));// 灭魔的鳞甲
        MR2.put(28017, new Integer(1));// 灭魔的披肩
        MR2.put(28018, new Integer(1));// 灭魔的小藤甲

        // MR * 2
        MR2.put(20056, new Integer(2));// 抗魔法斗篷
        MR2.put(120056, new Integer(2));// 抗魔法斗篷
        MR2.put(20049, new Integer(2));//巨蚁女皇的金翅膀 add hjx1000
        MR2.put(20050, new Integer(2));//巨蚁女皇的银翅膀 add hjx1000
        MR2.put(28014, new Integer(2));//真．抗魔法头盔 add hjx1000

        MR2.put(20078, new Integer(3));//混沌斗篷 add hjx1000
        MR2.put(70092, new Integer(3));// 马昆斯斗篷
        MR2.put(70034, new Integer(1));// 塔拉斯长靴

        // 林德拜尔
        MR2.put(30328, new Integer(1));// 林德拜尔的力量
        MR2.put(30329, new Integer(1));// 林德拜尔的魅惑
        MR2.put(30330, new Integer(1));// 林德拜尔的泉源
        MR2.put(30331, new Integer(1));// 林德拜尔的霸气
        
        //大法师之帽
        Mp2.put(28007, new Integer(10)); //大法师之帽防卷+1MP+10
        //魔法师臂甲
        Sp2.put(28010, new Integer(1)); 
        //体力臂甲
        Hp2.put(28008, new Integer(25));
        //巫妖斗篷
        Sp2.put(20107, new Integer(1)); 
        //马昆斯斗篷
        Sp2.put(70092, new Integer(1)); 
        //古代神射臂甲
        //古代斗士臂甲
        BowDmg.put(21105, new Integer(1));
        MeleeDmg.put(21106, new Integer(1));
    }

    protected L1ItemPower(final L1ItemInstance itemInstance) {
        this._itemInstance = itemInstance;
    }

    /**
     * 抗魔装备设置
     * 
     * @param armor
     * @return
     */
    protected int getMr() {
//		byte xing_count = 0; //星防具额外增加魔防 hjx1000
//		if (_itemInstance.get_power_name()!= null) {
//			xing_count += _itemInstance.get_power_name().get_xing_count();
//		}	
        int mr = _itemInstance.getItem().get_mdef();
        final Integer integer = MR2.get(_itemInstance.getItemId());
        if (integer != null) {
            mr += (_itemInstance.getEnchantLevel() * (integer /*+ xing_count*/));
        }
        return mr;
    }
    
    /**
     * 增MP装备设置
     * @param armor
     * @return
     */
    protected int getMp() { //加防卷额外增加MP的防具
    	int mp = 0;
        final Integer integer = Mp2.get(_itemInstance.getItemId());
        if (integer != null) {
        	mp += _itemInstance.getEnchantLevel() * integer;
        }
    	return mp;
    }
    
    /**
     * 增HP装备设置
     * @param armor
     * @return
     */
    protected int getHp() { //加防卷额外增加MP的防具
    	int hp = 0;
        final Integer integer = Hp2.get(_itemInstance.getItemId());
        if (integer != null) {
        	final int EnchantLevel = _itemInstance.getEnchantLevel();
        	if (EnchantLevel >= 9) {
        		hp = 100;
        	} else if (EnchantLevel >= 7) {
        		hp = 75;
        	} else if (EnchantLevel >= 5) {
        		hp = 50;
        	} else if (EnchantLevel >= 3) {
        		hp = 25;
        	}
        }
    	return hp;
    }
    
    /**
     * 增SP装备设置
     * @param armor
     * @return
     */
    protected int getSp() { //加防卷额外增加SP的防具
    	int sp = 0;
        final Integer integer = Sp2.get(_itemInstance.getItemId());
        if (integer != null) {
//        	if (_itemInstance.getEnchantLevel() >= 5) {
//        		final double itemlevel = _itemInstance.getEnchantLevel();
//            	sp += (itemlevel / 2) - 1.5;
//        	}
        	final int itemid = _itemInstance.getItemId();
        	final int EnchantLevel = _itemInstance.getEnchantLevel();
        	if (itemid == 28010) {
            	if (EnchantLevel >= 9) {
            		sp = 4;
            	} else if (EnchantLevel >= 7) {
            		sp = 3;
            	} else if (EnchantLevel >= 5) {
            		sp = 2;
            	} else if (EnchantLevel >= 3) {
            		sp = 1;
            	}
        	} else if (itemid == 20107) {
        		if (EnchantLevel > 3) {
            		sp = EnchantLevel - 3;
        		}
        	} else if (itemid == 70092) {
        		if (EnchantLevel > 7) {
            		sp = EnchantLevel - 7;
        		}
        	}

        }
    	return sp;
    }
    
    /**
     * 增远程攻击装备设置
     * @param armor
     * @return
     */
    protected int BowDmg() {
    	int bdmg = 0;
    	final Integer integer = BowDmg.get(_itemInstance.getItemId());
        if (integer != null) {
//        	if (_itemInstance.getEnchantLevel() >= 5) {
//        		final double itemlevel = _itemInstance.getEnchantLevel();
//        		bdmg += (itemlevel / 2) - 1.5;
//        	}
        	final int EnchantLevel = _itemInstance.getEnchantLevel();
        	if (EnchantLevel >= 9) {
        		bdmg = 4;
        	} else if (EnchantLevel >= 7) {
        		bdmg = 3;
        	} else if (EnchantLevel >= 5) {
        		bdmg = 2;
        	} else if (EnchantLevel >= 3) {
        		bdmg = 1;
        	}
        }
        return bdmg;
    }
    
    /**
     * 增近身攻击装备设置
     * @param armor
     * @return
     */
    protected int MeleeDmg() {
    	int mdmg = 0;
    	final Integer integer = MeleeDmg.get(_itemInstance.getItemId());
        if (integer != null) {
//        	if (_itemInstance.getEnchantLevel() >= 5) {
//        		final double itemlevel = _itemInstance.getEnchantLevel();
//        		mdmg += (itemlevel / 2) - 1.5;
//        	}
        	final int EnchantLevel = _itemInstance.getEnchantLevel();
        	if (EnchantLevel >= 9) {
        		mdmg = 4;
        	} else if (EnchantLevel >= 7) {
        		mdmg = 3;
        	} else if (EnchantLevel >= 5) {
        		mdmg = 2;
        	} else if (EnchantLevel >= 3) {
        		mdmg = 1;
        	}
        }
        return mdmg;
    }

    /**
     * 强化饰品设置
     * 
     * @param armor
     *            饰品
     * @param equipment
     *            true穿着 false脱除
     */
    protected void greater(final L1PcInstance owner, final boolean equipment) {
		final int level = _itemInstance.getEnchantLevel();
		if (level <= 0) {
			return;
		}
		if (equipment) {
			switch (_itemInstance.getItem().get_greater()) {
			case 0:// 高等
				/*switch (level) {
				case 0:
					break;
				case 1:
					owner.addEarth(1);
					owner.addWind(1);
					owner.addWater(1);
					owner.addFire(1);
					break;
				case 2:
					owner.addEarth(2);
					owner.addWind(2);
					owner.addWater(2);
					owner.addFire(2);
					break;
				case 3:
					owner.addEarth(3);
					owner.addWind(3);
					owner.addWater(3);
					owner.addFire(3);
					break;
				case 4:
					owner.addEarth(4);
					owner.addWind(4);
					owner.addWater(4);
					owner.addFire(4);
					break;
				case 5:
					owner.addEarth(5);
					owner.addWind(5);
					owner.addWater(5);
					owner.addFire(5);
					break;
				case 6:
					owner.addEarth(6);
					owner.addWind(6);
					owner.addWater(6);
					owner.addFire(6);
					owner.addHpr(1);
					owner.addMpr(1);
					break;
				case 7:
					owner.addEarth(10);
					owner.addWind(10);
					owner.addWater(10);
					owner.addFire(10);
					owner.addHpr(3);
					owner.addMpr(3);
					break;
				default:
					owner.addEarth(15);
					owner.addWind(15);
					owner.addWater(15);
					owner.addFire(15);
					owner.addHpr(3);
					owner.addMpr(3);
					break;
				}*/
				//修正为仿官设置 hjx1000
				owner.addEarth(level);
				owner.addWind(level);
				owner.addWater(level);
				owner.addFire(level);
				//修改自定义属性 hjx1000
				owner.addMaxHp(level * 5);
				owner.addMaxMp(level * 2);
				if (level > 5) {//+6以上
					owner.addHpr(1);
					owner.addMpr(1);
				}
				break;
				
			case 1:// 中等
				/*switch (level) {
				case 0:
					break;
				case 1:
					owner.addMaxHp(5);
					break;
				case 2:
					owner.addMaxHp(10);
					break;
				case 3:
					owner.addMaxHp(15);
					break;
				case 4:
					owner.addMaxHp(20);
					break;
				case 5:
					owner.addMaxHp(25);
					break;
				case 6:
					owner.addMaxHp(30);
					owner.addMr(2);
					break;
				case 7:
					owner.addMaxHp(40);
					owner.addMr(7);
					break;
				default:
					owner.addMaxHp((40 + level));
					owner.addMr((12 + level));
					break;
				}*/
				//修正为仿官设置 hjx1000
				//修改自定义属性
				owner.addMaxHp(level * 5);
				if (level > 5) {//+6以上
					owner.addMr(1);
				}
				break;
				
			case 2:// 初等
				/*switch (level) {
				case 0:
					break;
				case 1:
					owner.addMaxMp(3);
					break;
				case 2:
					owner.addMaxMp(6);
					break;
				case 3:
					owner.addMaxMp(9);
					break;
				case 4:
					owner.addMaxMp(12);
					break;
				case 5:
					owner.addMaxMp(15);
					break;
				case 6:
					owner.addMaxMp(25);
					owner.addSp(1);
					break;
				case 7:
					owner.addMaxMp(40);
					owner.addSp(2);
					break;
				default:
					owner.addMaxMp((40 + level));
					owner.addSp(3);
					break;
				}*/
				//修正为仿官设置 hjx1000
				owner.addMaxMp(level * 2);
				if (level > 5) {//+6以上
					owner.addSp(1);
				}
				break;
			}
			
		} else {
			switch (_itemInstance.getItem().get_greater()) {
			case 0:// 高等
				/*switch (level) {
				case 0:
					break;
				case 1:
					owner.addEarth(-1);
					owner.addWind(-1);
					owner.addWater(-1);
					owner.addFire(-1);
					break;
				case 2:
					owner.addEarth(-2);
					owner.addWind(-2);
					owner.addWater(-2);
					owner.addFire(-2);
					break;
				case 3:
					owner.addEarth(-3);
					owner.addWind(-3);
					owner.addWater(-3);
					owner.addFire(-3);
					break;
				case 4:
					owner.addEarth(-4);
					owner.addWind(-4);
					owner.addWater(-4);
					owner.addFire(-4);
					break;
				case 5:
					owner.addEarth(-5);
					owner.addWind(-5);
					owner.addWater(-5);
					owner.addFire(-5);
					break;
				case 6:
					owner.addEarth(-6);
					owner.addWind(-6);
					owner.addWater(-6);
					owner.addFire(-6);
					owner.addHpr(-1);
					owner.addMpr(-1);
					break;
				case 7:
					owner.addEarth(-10);
					owner.addWind(-10);
					owner.addWater(-10);
					owner.addFire(-10);
					owner.addHpr(-3);
					owner.addMpr(-3);
					break;
				default:
					owner.addEarth(-15);
					owner.addWind(-15);
					owner.addWater(-15);
					owner.addFire(-15);
					owner.addHpr(-3);
					owner.addMpr(-3);
					break;
				}*/
				owner.addEarth(-level);
				owner.addWind(-level);
				owner.addWater(-level);
				owner.addFire(-level);
				//修改自定义属性 hjx1000
				owner.addMaxHp(-level * 5);
				owner.addMaxMp(-level * 2);
				if (level > 5) {//+6以上
					owner.addHpr(-1);
					owner.addMpr(-1);
				}
				break;
				
			case 1:// 中等
				/*switch (level) {
				case 0:
					break;
				case 1:
					owner.addMaxHp(-5);
					break;
				case 2:
					owner.addMaxHp(-10);
					break;
				case 3:
					owner.addMaxHp(-15);
					break;
				case 4:
					owner.addMaxHp(-20);
					break;
				case 5:
					owner.addMaxHp(-25);
					break;
				case 6:
					owner.addMaxHp(-30);
					owner.addMr(-2);
					break;
				case 7:
					owner.addMaxHp(-40);
					owner.addMr(-7);
					break;
				default:
					owner.addMaxHp(-(40 + level));
					owner.addMr(-(12 + level));
					break;
				}*/
				//修正为仿官设置 hjx1000
				owner.addMaxHp(-level * 5);
				if (level > 5) {//+6以上
					owner.addMr(-1);
					owner.sendPackets(new S_SPMR(owner));
				}
				break;
				
			case 2:// 初等
				/*switch (level) {
				case 0:
					break;
				case 1:
					owner.addMaxMp(-3);
					break;
				case 2:
					owner.addMaxMp(-6);
					break;
				case 3:
					owner.addMaxMp(-9);
					break;
				case 4:
					owner.addMaxMp(-12);
					break;
				case 5:
					owner.addMaxMp(-15);
					break;
				case 6:
					owner.addMaxMp(-25);
					owner.addSp(-1);
					break;
				case 7:
					owner.addMaxMp(-40);
					owner.addSp(-2);
					break;
				default:
					owner.addMaxMp(-(40 + level));
					owner.addSp(-3);
					break;
				}*/
				//修正为仿官设置 hjx1000
				owner.addMaxMp(-level * 2);
				if (level > 5) {//+6以上
					owner.addSp(-1);
					owner.sendPackets(new S_SPMR(owner));
				}
				break;
			}
		}
	}
}
