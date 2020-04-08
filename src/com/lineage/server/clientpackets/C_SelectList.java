package com.lineage.server.clientpackets;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.lock.CharItemPowerReading;
import com.lineage.server.datatables.lock.PetReading;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_HelpMessage;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1ItemPower_name;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1Pet;
import com.lineage.server.world.World;

/**
 * 要求物品维修
 * 
 * @author daien
 * 
 */
public class C_SelectList extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_SelectList.class);

    /*
     * public C_SelectList() { }
     * 
     * public C_SelectList(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final L1PcInstance pc = client.getActiveChar();

            if (pc.isGhost()) { // 鬼魂模式
                return;
            }

            if (pc.isDead()) { // 死亡
                return;
            }

            if (pc.isTeleport()) { // 传送中
                return;
            }

            if (pc.isPrivateShop()) { // 商店村模式
                return;
            }

            final int itemObjectId = this.readD();
            final int npcObjectId = this.readD();      
            if (npcObjectId != 0) { // 武器修理
                final L1Object obj = World.get().findObject(npcObjectId);
                if (obj != null) {
                    if (obj instanceof L1NpcInstance) {
                        final L1NpcInstance npc = (L1NpcInstance) obj;
                        final int difflocx = Math.abs(pc.getX() - npc.getX());
                        final int difflocy = Math.abs(pc.getY() - npc.getY());
                        // 3格以上距离无效
                        if ((difflocx > 3) || (difflocy > 3)) {
                            return;
                        }
                        if (npc.getNpcId() == 98022) {//装备附魔师
                        	final Random random = new Random();
                            final L1PcInventory pcInventory = pc.getInventory();
                            final L1ItemInstance item = pcInventory.getItem(itemObjectId);
                            final int safe_enchant = item.getItem().get_safeenchant();
                            
                            if (item.getBless() >= 128) {
                            	return;
                            }
                            
                    		if (safe_enchant < 0) {
                    			return;
                    		}
                    		
                            if (item.getItem().getType2() != 2) {
                            	return;
                            }

                            if (item.get_power_name() != null) {
                            	//final int adxc = item.getEnchantLevel() - safe_enchant; //计算附魔限制
                            	final int xing = item.get_power_name().get_xing_count();
                            	if (xing > 2) {
                                	pc.sendPackets(new S_SystemMessage("这个阶级的装备不可以再附魔。"));
                                	return;
                            	}

                            	if (xing == 1) {
                                    final int[] needids = new int[] { 140074, 240074, 140129, 50085, 40308 };
                                    final int[] counts = new int[] { 
                                    		10, 10, 4, 5, 4000000  };
                                    if ((CreateNewItem.checkNewItem(pc, needids, counts) < 1)) {
                                    	return;
                                    }
                                    if (CreateNewItem.delItems(pc, needids, counts, 1)) {
                                    	
                                    } else {
                                    	return;
                                    }
                            	}
                            	if (xing == 2) {
                                    final int[] needids = new int[] { 140074, 240074, 140129, 50086, 40308 };
                                    final int[] counts = new int[] { 
                                    		20, 20, 8, 5, 8000000 };
                                    if ((CreateNewItem.checkNewItem(pc, needids, counts) < 1)) {
                                    	return;
                                    }
                                    if (CreateNewItem.delItems(pc, needids, counts, 1)) {
                                    	
                                    } else {
                                    	return;
                                    }
                                    item.get_power_name().set_hole_2(random.nextInt(6) + 1);
                            	}
                        		item.get_power_name().set_xing_count(xing + 1);
                        		CharItemPowerReading.get().updateItem(item.getId(), item.get_power_name());
                				pc.getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
                				pc.getInventory().saveItem(item, L1PcInventory.COL_ENCHANTLVL);
                				pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4541));
                				pc.sendPackets(new S_SystemMessage("附魔成功。"));

                            } else {
                                final int[] needids = new int[] { 
                                		140074, 240074, 140129, 50084, 40308 };
                                final int[] counts = new int[] { 5, 5, 2, 5, 2000000 };
                                if ((CreateNewItem.checkNewItem(pc, needids, // 需要物件
                                        counts) >= 1)) {
                                    CreateNewItem.delItems(pc, needids, counts, 1);
                                	L1ItemPower_name power = null;
                					power = new L1ItemPower_name();
                					power.set_xing_count(1);
                		            power.set_hole_count(0);
                		            power.set_hole_1(random.nextInt(6) + 1);//装备附魔第一段随机增加一点属性
                		            power.set_hole_2(0);
                		            power.set_hole_3(0);
                		            power.set_hole_4(0);
                		            power.set_hole_5(0);
                					item.set_power_name(power);
                					CharItemPowerReading.get().storeItem(item.getId(), item.get_power_name());
                					CharItemPowerReading.get().updateItem(item.getId(), item.get_power_name());
                					pc.getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
                					pc.getInventory().saveItem(item, L1PcInventory.COL_ENCHANTLVL);
                					pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4541));
                					pc.sendPackets(new S_SystemMessage("附魔成功。"));
                                }
                            }
                        } else if (npc.getNpcId() == 98023) {//武器附魔师
                        	final Random random = new Random();
                            final L1PcInventory pcInventory = pc.getInventory();
                            final L1ItemInstance item = pcInventory.getItem(itemObjectId);
                            final int safe_enchant = item.getItem().get_safeenchant();
                            final int enchant = item.getEnchantLevel();
                            if (item.getBless() >= 128) {
                            	return;
                            }
                            
                    		if (safe_enchant < 0) {
                    			return;
                    		}
                            if (item.getItem().getType2() != 1) {
                            	return;
                            }
                            if (safe_enchant == 0 && enchant < 5) {
                            	pc.sendPackets(new S_SystemMessage("您必需先强化到 +5 才可以附魔。"));
                            	return;
                            }
                            if (safe_enchant == 6 && enchant < 7) {
                            	pc.sendPackets(new S_SystemMessage("您必需先强化到 +7 才可以附魔。"));
                            	return;
                            }
                            if (item.isEquipped()) {
                            	pc.sendPackets(new S_SystemMessage("您必需先解除武器，才可以附魔。"));//hjx1000
                            	return;
                            }

                            if (item.get_power_name() != null) {
                            	final int xing = item.get_power_name().get_xing_count();
                            	if (xing > 8) {
                            		pc.sendPackets(new S_SystemMessage("您的武器已经达到最高级，不可以再附魔。"));
                            		return;
                            	}
                                if (!pc.getInventory().checkEnchantItem(item.getItemId(), enchant, 1, item.getId(), xing)) {
                                	pc.sendPackets(new S_ServerMessage(337, item.getLogNamefumo()));
                                	return;
                                }
                                final int[] needids = new int[] {
                                		140130, 140087, 240087, 40087 };
                                final int[] counts = new int[] { 3 , 10 , 10 , 50 };
                                if ((CreateNewItem.checkNewItem(pc, needids, counts) >= 1)) {//需要材料
                                    CreateNewItem.delItems(pc, needids, counts, 1);//删除需要材料
                                    pc.getInventory().consumeEnchantItem(item.getItemId(), enchant, 1, item.getId(), xing);
                                    //final int rnd1 = random.nextInt(100) + 1;
                                    //item.get_power_name().set_hole_2(random.nextInt(6) + 1);//附魔到六段随机得到一点属性
                                    if (random.nextInt(100) + 1 >= 70) {
                                		item.get_power_name().set_xing_count(xing + 1);
                                		item.setEnchantLevel(enchant + 1);
                                		CharItemPowerReading.get().updateItem(item.getId(), item.get_power_name());
                        				pc.getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
                        				pc.getInventory().saveItem(item, L1PcInventory.COL_ENCHANTLVL);
                        				pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4541));
                        				pc.sendPackets(new S_SystemMessage("附魔成功。"));
                        				final StringBuilder s = new StringBuilder();
                        				s.append("玩家" + pc.getName() + " 成功打造出 神器  " + item.getLogName());
                                        // 产生讯息封包 (强化成功)
                                        World.get()
                                                .broadcastPacketToAll(
                                                        new S_HelpMessage(s.toString()));
                                    } else {
                                    	pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4581));
                        				pc.sendPackets(new S_SystemMessage("附魔失败。"));
                                    }
                                }
                            } else {
                                if (!pc.getInventory().checkEnchantItem(item.getItemId(), enchant, 1, item.getId(), 0)) {
                                	pc.sendPackets(new S_ServerMessage(337, item.getLogNamefumo()));
                                	return;
                                }
                                final int[] needids = new int[] {
                                		140130, 140087, 240087, 40087 };
                                final int[] counts = new int[] { 3, 10, 10, 50 };
                                if ((CreateNewItem.checkNewItem(pc, needids, counts) >= 1)) {
                                    CreateNewItem.delItems(pc, needids, counts, 1);
                                    pc.getInventory().consumeEnchantItem(item.getItemId(), enchant, 1, item.getId(), 0);
                                    if (random.nextInt(100) + 1 >= 70) {
                                    	L1ItemPower_name power = null;
                    					power = new L1ItemPower_name();
                    					power.set_xing_count(1);
                    		            power.set_hole_count(0);
                    		            power.set_hole_1(0);
                    		            power.set_hole_2(0);
                    		            power.set_hole_3(0);
                    		            power.set_hole_4(0);
                    		            power.set_hole_5(0);
                    					item.set_power_name(power);
                    					item.setEnchantLevel(enchant + 1);
                    					CharItemPowerReading.get().storeItem(item.getId(), item.get_power_name());
                    					CharItemPowerReading.get().updateItem(item.getId(), item.get_power_name());
                    					pc.getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
                    					pc.getInventory().saveItem(item, L1PcInventory.COL_ENCHANTLVL);
                    					pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4541));
                    					pc.sendPackets(new S_SystemMessage("附魔成功。"));
                        				final StringBuilder s = new StringBuilder();
                        				s.append("玩家" + pc.getName() + " 成功打造出 神器  " + item.getLogName());
                                        // 产生讯息封包 (强化成功)
                                        World.get()
                                                .broadcastPacketToAll(
                                                        new S_HelpMessage(s.toString()));
                                    } else {
                                    	pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4581));
                        				pc.sendPackets(new S_SystemMessage("附魔失败。"));
                                    }
                                }
                            }
                        } else {
                            final L1PcInventory pcInventory = pc.getInventory();
                            final L1ItemInstance item = pcInventory.getItem(itemObjectId);
                            final int cost = item.get_durability() * 200;// 每一点损坏度200元
                            if (!pc.getInventory().consumeItem(L1ItemId.ADENA, cost)) {
                                // 189：\f1金币不足。 。
                                pc.sendPackets(new S_ServerMessage(189));
                                return;
                            }

                            item.set_durability(0);
                            // 464：%0 现在变成像个新的一样。
                            pc.sendPackets(new S_ServerMessage(464, item.getLogName()));
                            pcInventory.updateItem(item, L1PcInventory.COL_DURABILITY);
                        }
                    }
                }
            } else { // 宠物清单
                int petCost = 0;
                int petCount = 0;
                int divisor = 6;
                final Object[] petList = pc.getPetList().values().toArray();
                if (petList.length > 2) {
                    // 489：你无法一次控制那么多宠物。
                    pc.sendPackets(new S_ServerMessage(489));
                    return;
                }
                for (final Object pet : petList) {
                    petCost += ((L1NpcInstance) pet).getPetcost();
                }

                int charisma = pc.getCha();
                if (pc.isCrown()) { // 君主
                    charisma += 6;

                } else if (pc.isElf()) { // エルフ
                    charisma += 12;

                } else if (pc.isWizard()) { // WIZ
                    charisma += 6;

                } else if (pc.isDarkelf()) { // DE
                    charisma += 6;

                } else if (pc.isDragonKnight()) { // ドラゴンナイト
                    charisma += 6;

                } else if (pc.isIllusionist()) { // イリュージョニスト
                    charisma += 6;
                }

                final L1Pet l1pet = PetReading.get().getTemplate(itemObjectId);

                if (l1pet != null) {
                    final int npcId = l1pet.get_npcid();
                    charisma -= petCost;
                    if ((npcId == 45313) || (npcId == 45710 // タイガー、バトルタイガー
                            ) || (npcId == 45711) || (npcId == 45712)) { // 纪州犬の子犬、纪州犬
                        divisor = 12;
                    } else {
                        divisor = 6;
                    }
                    petCount = charisma / divisor;

                    // TODO 亮 一个玩家最多只能携带3只宠物
                    /*
                     * final int iii = petCost / 6; if (iii > 2) {
                     * System.out.println("iii > 2 :" + iii); //
                     * 489：你无法一次控制那么多宠物。 pc.sendPackets(new
                     * S_ServerMessage(489)); // System.out.println("iii > 2");
                     * return; }
                     */

                    if (petCount <= 0) {
                        // 489：你无法一次控制那么多宠物。
                        pc.sendPackets(new S_ServerMessage(489)); // 引き取ろうとするペットが多すぎます。
                        return;
                    }
                    final L1Npc npcTemp = NpcTable.get().getTemplate(npcId);
                    final L1PetInstance pet = new L1PetInstance(npcTemp, pc,
                            l1pet);
                    pet.setPetcost(divisor);
                }
            }

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
