package com.lineage.data.cmd;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigRecord;
import com.lineage.server.datatables.lock.LogEnchantReading;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1ItemPower;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_HelpMessage;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

/**
 * 
 * @author daien
 * 
 */
public class EnchantArmor extends EnchantExecutor {

    private static final Log _log = LogFactory.getLog(EnchantArmor.class);

    /**
     * 强化失败
     * 
     * @param pc
     *            执行者
     * @param item
     *            对象物件
     */
    @Override
    public void failureEnchant(final L1PcInstance pc, final L1ItemInstance item) {
        final StringBuilder s = new StringBuilder();

        if (ConfigRecord.LOGGING_BAN_ENCHANT) {
            LogEnchantReading.get().failureEnchant(pc, item);
        }

        // 未鉴定
        if (!item.isIdentified()) {
            s.append(item.getName());

        } else {
            s.append(item.getLogName());
        }
        // 164 \f1%0%s 产生激烈的 %1 光芒，一会儿后就消失了。银色的
        pc.sendPackets(new S_ServerMessage(164, s.toString(), "$252"));
        pc.getInventory().removeItem(item, item.getCount());
        _log.info("人物:" + pc.getName() + "点爆物品" + item.getNumberedName_to_String()
                + " 物品OBJID:" + item.getId());
    }

    /**
     * 强化成功
     * 
     * @param pc
     *            执行者
     * @param item
     *            对象物件
     * @param i
     *            强化质
     */
    @Override
    public void successEnchant(final L1PcInstance pc,
            final L1ItemInstance item, final int i) {
        final StringBuilder s = new StringBuilder();
        final StringBuilder sa = new StringBuilder();
        final StringBuilder sb = new StringBuilder();

        // 未鉴定
        if (!item.isIdentified()) {
            s.append(item.getName());

        } else {
        	s.append(item.getLogName());
        }

        switch (i) {
            case 0:
                // \f1%0%s %2 产生激烈的 %1 光芒，但是没有任何事情发生。
                pc.sendPackets(new S_ServerMessage(160, s.toString(), "$252",
                        "$248"));
                return;

            case -1:
                sa.append("$246");// 黑色的
                sb.append("$247");// 一瞬间发出
                break;

            case 1: // '\001'
                sa.append("$252");// 银色的
                sb.append("$247");// 一瞬间发出
                break;

            case 2: // '\002'
            case 3: // '\003'
                sa.append("$252");// 银色的
                sb.append("$248");// 持续发出
                break;
    		case 4: //上星 hjx1000
    			return;
        }

        // 161 \f1%0%s %2 %1 光芒。
        pc.sendPackets(new S_ServerMessage(161, s.toString(), sa.toString(), sb
                .toString()));

        final int oldEnchantLvl = item.getEnchantLevel();// 原追加值
        final int oldSp = item.getSp();//原增加SP值
        final int oldBowDmg = item.BowDmg(); //原增加远程攻击的装备
        final int oldMeleeDmg = item.MeleeDmg(); //原增加近身攻击的装备
        final int oldHp = item.getHp(); //原增加的HP装备
        final int newEnchantLvl = oldEnchantLvl + i;// 新追加值

        if (oldEnchantLvl != newEnchantLvl) {
            item.setEnchantLevel(newEnchantLvl);
            pc.getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
            pc.getInventory().saveItem(item, L1PcInventory.COL_ENCHANTLVL);
            if (newEnchantLvl >= 9) {// 强化值等于或超过9
                // 1,652：强化
                // 产生讯息封包 (强化成功)
                World.get()
                        .broadcastPacketToAll(
                                new S_HelpMessage(pc.getName(), s.toString()
                                        + " " + sb.toString() + " "
                                        + sa.toString() + " $251"));
            }

            if (item.isEquipped()) {
                // 取得物件触发事件
                final int use_type = item.getItem().getUseType();
                switch (use_type) {
                    case 2:// 盔甲
                    case 22:// 头盔
                    case 19:// 斗篷
                    case 18:// T恤
                    case 20:// 手套
                    case 21:// 靴
                    case 25:// 盾牌
                        pc.addAc(-i);
//                        final int mr = item.getMr();
//                        final int mp = item.getMp();
//                        if (mr != 0) {
//                            final Integer integer = L1ItemPower.MR2.get(item
//                                    .getItemId());
//                            if (integer != null) {
//                                pc.addMr(i * integer);
//                                pc.sendPackets(new S_SPMR(pc));
//                            }
//                        }
//                        if(mp != 0) {
//                            final Integer integer = L1ItemPower.Mp2.get(item
//                                    .getItemId());
//                            if (integer != null) {
//                            	pc.addMaxMp(i * integer);
//                            }
//                        }
                        final Integer sp2 = L1ItemPower.Sp2.get(item
                        		.getItemId());
                        final Integer mr2 = L1ItemPower.MR2.get(item
                        		.getItemId());
                        final Integer mp2 = L1ItemPower.Mp2.get(item
                        		.getItemId());
                        final Integer hp2 = L1ItemPower.Hp2.get(item
                        		.getItemId());
                        final Integer BowDmg = L1ItemPower.BowDmg.get(item
                        		.getItemId());
                        final Integer MeleeDmg = L1ItemPower.MeleeDmg.get(item
                        		.getItemId());
                        if (mr2 != null) {
                        	pc.addMr(i * mr2);
                        	pc.sendPackets(new S_SPMR(pc));
                        }
                    	if (mp2 != null) {
                    		pc.addMaxMp(i * mp2);
                    	}
                    	if (hp2 != null) {
                    		final int hp = item.getHp() - oldHp;
                    		if (hp != 0) {
                    			pc.addMaxHp(hp);
                    		}
                    	}
                    	if (sp2 != null) {
                    		final int sp = item.getSp() - oldSp;
                    		if (sp != 0) {
                    			pc.addSp(sp);
                    			pc.sendPackets(new S_SPMR(pc));
                    		}                    		
                    	}
                    	if (BowDmg != null) {
                    		final int bdmg = item.BowDmg() - oldBowDmg;
                    		if (bdmg != 0) {
                    			pc.addBowDmgModifierByArmor(bdmg);
                    		}
                    	}
                    	if (MeleeDmg != null) {
                    		final int mdmg = item.MeleeDmg() - oldMeleeDmg;
                    		if (mdmg != 0) {
                    			pc.addDmgModifierByArmor(mdmg);
                    		}
                    	}
                        break;

                    case 23:// 戒指
                    case 24:// 项链
                    case 37:// 腰带
                    case 40:// 耳环
                        /*if (item.getItem().get_greater() != 3) {
                            item.greater(pc, true);
                        }*/
    					//修正饰品强化后临时属性多加的问题 hjx1000
    					switch (item.getItem().get_greater()) {
    					case 0:
    						pc.addEarth(1);
    						pc.addWater(1);
    						pc.addWater(1);
    						pc.addFire(1);
    						pc.addMaxHp(5);
    						pc.addMaxMp(2);
    						if (item.getEnchantLevel() == 6) {
    							pc.addHpr(1);
    							pc.addMpr(1);
    						}
    						break;
    					case 1:
    						pc.addMaxHp(5);
    						if (item.getEnchantLevel() == 6) {
    							pc.addMr(1);
    							pc.sendPackets(new S_SPMR(pc));
    						}
    						break;
    					case 2:
    						pc.addMaxMp(2);
    						if (item.getEnchantLevel() == 6) {
    							pc.addSp(1);
    							pc.sendPackets(new S_SPMR(pc));
    						}
    						break;
    						//end hjx1000
    					}
                        break;

                    default:
                        break;
                }
                pc.sendPackets(new S_OwnCharStatus(pc));
            }
        }
    }
}
