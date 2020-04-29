package com.lineage.data.npc.other;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;

/** 真@抗魔法头盔打造 by:42621391 2014年8月28日15:01:34 */
public class Npc_YX_mm extends NpcExecutor {

	//protected static final Random _random = new Random();

	private Npc_YX_mm() {
	}

	public static NpcExecutor get() {
		return new Npc_YX_mm();
	}

	@Override
	public int type() {
		return 3;
	}

	@Override
	public void talk(L1PcInstance pc, L1NpcInstance npc) {
		pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mimofj"));
	}

	@Override
	public void action(L1PcInstance pc, L1NpcInstance npc, String cmd,
			long amount) {
		if (cmd.equalsIgnoreCase("A")) { // 对话当
			if (pc.getInventory().checkItem(20095,1) && pc.getInventory().checkEnchantItem(20110, 8, 1) && pc.getInventory().checkItem(41246, 1000000)) {
				pc.getInventory().consumeItem(20095,1);
				pc.getInventory().consumeEnchantItem(20110,8,1);
				pc.getInventory().consumeItem(41246,1000000);
				pc.getInventory().storeItem(28015, 1);
				pc.sendPackets(new S_SystemMessage("获得 灭魔的金属盔甲."));
			} else {
				//pc.sendPackets(new S_SystemMessage("请检查条件是否达到."));
				CreateNewItem.checkNewItem(pc, 20095, 1);
				CreateNewItem.checkNewItem(pc, 41246, 1000000);
				if (!pc.getInventory().checkEnchantItem(20110, 8, 1)) {
                    pc.sendPackets(new S_ServerMessage(337, "+8 "
                            + ItemTable.get().getTemplate(20110)
                                    .getNameId())); // \f1%0が不足しています。
				}
			}
		}
		if (cmd.equalsIgnoreCase("B")) { // 对话当
			if (pc.getInventory().checkItem(20094,1) && pc.getInventory().checkEnchantItem(20110, 8, 1) && pc.getInventory().checkItem(41246, 1000000)) {
				pc.getInventory().consumeItem(20094,1);
				pc.getInventory().consumeEnchantItem(20110,8,1);
				pc.getInventory().consumeItem(41246,1000000);
				pc.getInventory().storeItem(28016, 1);
				pc.sendPackets(new S_SystemMessage("获得 灭魔的鳞甲."));
			} else {
				//pc.sendPackets(new S_SystemMessage("请检查条件是否达到."));
				CreateNewItem.checkNewItem(pc, 20094, 1);
				CreateNewItem.checkNewItem(pc, 41246, 1000000);
				if (!pc.getInventory().checkEnchantItem(20110, 8, 1)) {
                    pc.sendPackets(new S_ServerMessage(337, "+8 "
                            + ItemTable.get().getTemplate(20110)
                                    .getNameId())); // \f1%0が不足しています。
				}
			}
		}
		if (cmd.equalsIgnoreCase("C")) { // 对话当
			if (pc.getInventory().checkItem(20093,1) && pc.getInventory().checkEnchantItem(20110, 8, 1) && pc.getInventory().checkItem(41246, 1000000)) {
				pc.getInventory().consumeItem(20093,1);
				pc.getInventory().consumeEnchantItem(20110,8,1);
				pc.getInventory().consumeItem(41246,1000000);
				pc.getInventory().storeItem(28017, 1);
				pc.sendPackets(new S_SystemMessage("获得 灭魔的披肩."));
			} else {
				//pc.sendPackets(new S_SystemMessage("请检查条件是否达到."));
				CreateNewItem.checkNewItem(pc, 20093, 1);
				CreateNewItem.checkNewItem(pc, 41246, 1000000);
				if (!pc.getInventory().checkEnchantItem(20110, 8, 1)) {
                    pc.sendPackets(new S_ServerMessage(337, "+8 "
                            + ItemTable.get().getTemplate(20110)
                                    .getNameId())); // \f1%0が不足しています。
				}
			}
		}
		if (cmd.equalsIgnoreCase("D")) { // 对话当
			if (pc.getInventory().checkItem(20092,1) && pc.getInventory().checkEnchantItem(20110, 8, 1) && pc.getInventory().checkItem(41246, 1000000)) {
				pc.getInventory().consumeItem(20092,1);
				pc.getInventory().consumeEnchantItem(20110,8,1);
				pc.getInventory().consumeItem(41246,1000000);
				pc.getInventory().storeItem(28018, 1);
				pc.sendPackets(new S_SystemMessage("获得 灭魔的小藤甲."));
			} else {
				//pc.sendPackets(new S_SystemMessage("请检查条件是否达到."));
				CreateNewItem.checkNewItem(pc, 20092, 1);
				CreateNewItem.checkNewItem(pc, 41246, 1000000);
				if (!pc.getInventory().checkEnchantItem(20110, 8, 1)) {
                    pc.sendPackets(new S_ServerMessage(337, "+8 "
                            + ItemTable.get().getTemplate(20110)
                                    .getNameId())); // \f1%0が不足しています。
				}
			}
		}
		if (cmd.equalsIgnoreCase("XH1")) { // 兑换苍天双刀
			if (pc.getInventory().checkEnchantItem(81, 8, 1) && pc.getInventory().checkItem(40308, 100000) && pc.getInventory().checkItem(44070, 30)) {
				pc.getInventory().consumeEnchantItem(81,8,1);
				pc.getInventory().consumeItem(40308,1000000);
                pc.getInventory().consumeItem(44070,30);
				pc.getInventory().storeItem(236, 1);
				pc.sendPackets(new S_SystemMessage("苍天双刀."));
			} else {
				//pc.sendPackets(new S_SystemMessage("请检查条件是否达到."));
				CreateNewItem.checkNewItem(pc, 40308, 1000000);
				CreateNewItem.checkNewItem(pc, 44070, 30);
				if (!pc.getInventory().checkEnchantItem(81, 8, 1)) {
                    pc.sendPackets(new S_ServerMessage(337, "+8 "
                            + ItemTable.get().getTemplate(81)
                                    .getNameId())); // \f1%0が不足しています。
				}
			}
		}
        if (cmd.equalsIgnoreCase("XH2")) { // 兑换苍天之剑
			if (pc.getInventory().checkEnchantItem(100041, 8, 1) && pc.getInventory().checkItem(40308, 100000) && pc.getInventory().checkItem(44070, 30)) {
				pc.getInventory().consumeEnchantItem(100041,8,1);
				pc.getInventory().consumeItem(40308,1000000);
                pc.getInventory().consumeItem(44070,30);
				pc.getInventory().storeItem(232, 1);
				pc.sendPackets(new S_SystemMessage("苍天之剑."));
			} else {
				//pc.sendPackets(new S_SystemMessage("请检查条件是否达到."));
				CreateNewItem.checkNewItem(pc, 40308, 1000000);
				CreateNewItem.checkNewItem(pc, 44070, 30);
				if (!pc.getInventory().checkEnchantItem(100041, 8, 1)) {
                    pc.sendPackets(new S_ServerMessage(337, "+8 "
                            + ItemTable.get().getTemplate(100041)
                                    .getNameId())); // \f1%0が不足しています。
				}
			}
		}
        if (cmd.equalsIgnoreCase("XH3")) { // 兑换苍天巨剑
			if (pc.getInventory().checkEnchantItem(64, 8, 1) && pc.getInventory().checkItem(40308, 100000) && pc.getInventory().checkItem(44070, 30)) {
				pc.getInventory().consumeEnchantItem(64,8,1);
				pc.getInventory().consumeItem(40308,1000000);
                pc.getInventory().consumeItem(44070,30);
				pc.getInventory().storeItem(233, 1);
				pc.sendPackets(new S_SystemMessage("苍天巨剑."));
			} else {
				//pc.sendPackets(new S_SystemMessage("请检查条件是否达到."));
				CreateNewItem.checkNewItem(pc, 40308, 1000000);
				CreateNewItem.checkNewItem(pc, 44070, 30);
				if (!pc.getInventory().checkEnchantItem(64, 8, 1)) {
                    pc.sendPackets(new S_ServerMessage(337, "+8 "
                            + ItemTable.get().getTemplate(64)
                                    .getNameId())); // \f1%0が不足しています。
				}
			}
		}
        if (cmd.equalsIgnoreCase("XH4")) { // 兑换苍天之弓
			if (pc.getInventory().checkEnchantItem(181, 8, 1) && pc.getInventory().checkItem(40308, 100000) && pc.getInventory().checkItem(44070, 30)) {
				pc.getInventory().consumeEnchantItem(181,8,1);
				pc.getInventory().consumeItem(40308,1000000);
                pc.getInventory().consumeItem(44070,30);
				pc.getInventory().storeItem(239, 1);
				pc.sendPackets(new S_SystemMessage("苍天之弓."));
			} else {
				//pc.sendPackets(new S_SystemMessage("请检查条件是否达到."));
				CreateNewItem.checkNewItem(pc, 40308, 1000000);
				CreateNewItem.checkNewItem(pc, 44070, 30);
				if (!pc.getInventory().checkEnchantItem(181, 8, 1)) {
                    pc.sendPackets(new S_ServerMessage(337, "+8 "
                            + ItemTable.get().getTemplate(181)
                                    .getNameId())); // \f1%0が不足しています。
				}
			}
		}
        if (cmd.equalsIgnoreCase("XH5")) { // 兑换苍天魔杖
			if (pc.getInventory().checkEnchantItem(131, 8, 1) && pc.getInventory().checkItem(40308, 100000) && pc.getInventory().checkItem(44070, 30)) {
				pc.getInventory().consumeEnchantItem(131,8,1);
				pc.getInventory().consumeItem(40308,1000000);
                pc.getInventory().consumeItem(44070,30);
				pc.getInventory().storeItem(238, 1);
				pc.sendPackets(new S_SystemMessage("苍天魔杖."));
			} else {
				//pc.sendPackets(new S_SystemMessage("请检查条件是否达到."));
				CreateNewItem.checkNewItem(pc, 40308, 1000000);
				CreateNewItem.checkNewItem(pc, 44070, 30);
				if (!pc.getInventory().checkEnchantItem(131, 8, 1)) {
                    pc.sendPackets(new S_ServerMessage(337, "+8 "
                            + ItemTable.get().getTemplate(131)
                                    .getNameId())); // \f1%0が不足しています。
				}
			}
		}
        if (cmd.equalsIgnoreCase("XH6")) { // 兑换苍天钢爪
			if (pc.getInventory().checkEnchantItem(162, 8, 1) && pc.getInventory().checkItem(40308, 100000) && pc.getInventory().checkItem(44070, 30)) {
				pc.getInventory().consumeEnchantItem(162,8,1);
				pc.getInventory().consumeItem(40308,1000000);
                pc.getInventory().consumeItem(44070,30);
				pc.getInventory().storeItem(237, 1);
				pc.sendPackets(new S_SystemMessage("苍天钢爪."));
			} else {
				//pc.sendPackets(new S_SystemMessage("请检查条件是否达到."));
				CreateNewItem.checkNewItem(pc, 40308, 1000000);
				CreateNewItem.checkNewItem(pc, 44070, 30);
				if (!pc.getInventory().checkEnchantItem(162, 8, 1)) {
                    pc.sendPackets(new S_ServerMessage(337, "+8 "
                            + ItemTable.get().getTemplate(181)
                                    .getNameId())); // \f1%0が不足しています。
				}
			}
		}
        if (cmd.equalsIgnoreCase("XH7")) { // 制作塔拉斯手套
			if (pc.getInventory().checkEnchantItem(20187, 7, 1) && pc.getInventory().checkItem(40308, 100000) && pc.getInventory().checkItem(44070, 30)) {
				pc.getInventory().consumeEnchantItem(20187,7,1);
				pc.getInventory().consumeItem(40308,1000000);
                pc.getInventory().consumeItem(44070,30);
				pc.getInventory().storeItem(28011, 1);
				pc.sendPackets(new S_SystemMessage("塔拉斯手套."));
			} else {
				//pc.sendPackets(new S_SystemMessage("请检查条件是否达到."));
				CreateNewItem.checkNewItem(pc, 40308, 1000000);
				CreateNewItem.checkNewItem(pc, 44070, 30);
				if (!pc.getInventory().checkEnchantItem(20187, 7, 1)) {
                    pc.sendPackets(new S_ServerMessage(337, "+7 "
                            + ItemTable.get().getTemplate(20187)
                                    .getNameId())); // \f1%0が不足しています。
				}
			}
		}
        if (cmd.equalsIgnoreCase("XH8")) { // 制作塔拉斯长靴
			if (pc.getInventory().checkEnchantItem(20194, 7, 1) && pc.getInventory().checkItem(40308, 100000)&& pc.getInventory().checkItem(44070, 30)) {
				pc.getInventory().consumeEnchantItem(20194,7,1);
				pc.getInventory().consumeItem(40308,1000000);
                pc.getInventory().consumeItem(44070,30);
				pc.getInventory().storeItem(70034, 1);
				pc.sendPackets(new S_SystemMessage("塔拉斯长靴."));
			} else {
				//pc.sendPackets(new S_SystemMessage("请检查条件是否达到."));
				CreateNewItem.checkNewItem(pc, 40308, 1000000);
				CreateNewItem.checkNewItem(pc, 44070, 30);
				if (!pc.getInventory().checkEnchantItem(20194, 7, 1)) {
                    pc.sendPackets(new S_ServerMessage(337, "+7 "
                            + ItemTable.get().getTemplate(20194)
                                    .getNameId())); // \f1%0が不足しています。
				}
			}
		}
        if (cmd.equalsIgnoreCase("XH9")) { // 制作真．抗魔法头盔
			if (pc.getInventory().checkEnchantItem(20011, 8, 1) && pc.getInventory().checkItem(40308, 100000) && pc.getInventory().checkItem(44070, 30)) {
				pc.getInventory().consumeEnchantItem(20011,8,1);
				pc.getInventory().consumeItem(40308,1000000);
                pc.getInventory().consumeItem(44070,30);
				pc.getInventory().storeItem(28014, 1);
				pc.sendPackets(new S_SystemMessage("真.抗魔法头盔."));
			} else {
				//pc.sendPackets(new S_SystemMessage("请检查条件是否达到."));
				CreateNewItem.checkNewItem(pc, 40308, 1000000);
				CreateNewItem.checkNewItem(pc, 44070, 30);
				if (!pc.getInventory().checkEnchantItem(20011, 8, 1)) {
                    pc.sendPackets(new S_ServerMessage(337, "+8 "
                            + ItemTable.get().getTemplate(20011)
                                    .getNameId())); // \f1%0が不足しています。
				}
			}
		}
	}
}