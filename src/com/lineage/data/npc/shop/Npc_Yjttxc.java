package com.lineage.data.npc.shop;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;

/**
 * 倚剑天堂宣传<BR>
 * 99101
 * 
 * @author hjx1000
 * 
 */
public class Npc_Yjttxc extends NpcExecutor {

    /**
	 *
	 */
    private Npc_Yjttxc() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Yjttxc();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "yjxcjl"));
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
    	if (pc.getInventory().getSize() >= 160) {
            // 263 \f1一个角色最多可携带180个道具。
            pc.sendPackets(new S_ServerMessage(263));
            return;
    	}
    	
    	
        if (cmd.equalsIgnoreCase("yjeh1")) {
            if (pc.getInventory().consumeItem(44071, 600)) {
            	CreateNewItem.createNewItem(pc, 33005, 1);//倚剑耳环(力)
            } else {
            	pc.sendPackets(new S_SystemMessage("你的宣传币不够。"));
            }
        } else if (cmd.equalsIgnoreCase("yjeh2")) {
            if (pc.getInventory().consumeItem(44071, 600)) {
            	CreateNewItem.createNewItem(pc, 33006, 1);//倚剑耳环(敏)
            } else {
            	pc.sendPackets(new S_SystemMessage("你的宣传币不够。"));
            }
        } else if (cmd.equalsIgnoreCase("yjeh3")) {
            if (pc.getInventory().consumeItem(44071, 600)) {
            	CreateNewItem.createNewItem(pc, 33007, 1);//倚剑耳环(智)
            } else {
            	pc.sendPackets(new S_SystemMessage("你的宣传币不够。"));
            }
        } else if (cmd.equalsIgnoreCase("yjxj")) {
            if (pc.getInventory().consumeItem(44071, 600)) {
            	CreateNewItem.createNewItem(pc, 33008, 1);//倚剑勋章
            } else {
            	pc.sendPackets(new S_SystemMessage("你的宣传币不够。"));
            }
        }
//        } else if (cmd.equalsIgnoreCase("yjww1")) {
//            if (pc.getInventory().consumeItem(44071, 400)) {
//            	CreateNewItem.createNewItem(pc, 59132, 1);//魔法娃娃：骑士(男骑士)
//            } else {
//            	pc.sendPackets(new S_SystemMessage("你的宣传币不够。"));
//            }
//        } else if (cmd.equalsIgnoreCase("yjww2")) {
//            if (pc.getInventory().consumeItem(44071, 400)) {
//            	CreateNewItem.createNewItem(pc, 59133, 1);//魔法娃娃：妖精(男妖精)
//            } else {
//            	pc.sendPackets(new S_SystemMessage("你的宣传币不够。"));
//            }
//        } else if (cmd.equalsIgnoreCase("yjww3")) {
//            if (pc.getInventory().consumeItem(44071, 400)) {
//            	CreateNewItem.createNewItem(pc, 59134, 1);//魔法娃娃：法师(男法师)
//            } else {
//            	pc.sendPackets(new S_SystemMessage("你的宣传币不够。"));
//            }
//        }
        
        if (cmd.equalsIgnoreCase("yjtx1")) {
            if (pc.getInventory().consumeItem(44071, 1000)) {
            	CreateNewItem.createNewItem(pc, 21028, 1);//力量T恤
            } else {
            	pc.sendPackets(new S_SystemMessage("你的宣传币不够。"));
            }
        } else if (cmd.equalsIgnoreCase("yjtx2")) {
            if (pc.getInventory().consumeItem(44071, 1000)) {
            	CreateNewItem.createNewItem(pc, 21029, 1);//敏捷T恤
            } else {
            	pc.sendPackets(new S_SystemMessage("你的宣传币不够。"));
            }
        } else if (cmd.equalsIgnoreCase("yjtx3")) {
            if (pc.getInventory().consumeItem(44071, 1000)) {
            	CreateNewItem.createNewItem(pc, 21031, 1);//智力T恤
            } else {
            	pc.sendPackets(new S_SystemMessage("你的宣传币不够。"));
            }
        } else if (cmd.equalsIgnoreCase("liaoli1")) {
            if (pc.getInventory().consumeItem(44071, 5)) {
            	CreateNewItem.createNewItem(pc, 49252, 1);//智力T恤
            } else {
            	pc.sendPackets(new S_SystemMessage("你的宣传币不够。"));
            }
        } else if (cmd.equalsIgnoreCase("liaoli2")) {
            if (pc.getInventory().consumeItem(44071, 5)) {
            	CreateNewItem.createNewItem(pc, 49253, 1);//智力T恤
            } else {
            	pc.sendPackets(new S_SystemMessage("你的宣传币不够。"));
            }
        } else if (cmd.equalsIgnoreCase("liaoli3")) {
            if (pc.getInventory().consumeItem(44071, 5)) {
            	CreateNewItem.createNewItem(pc, 49254, 1);//智力T恤
            } else {
            	pc.sendPackets(new S_SystemMessage("你的宣传币不够。"));
            }
        } else if (cmd.equalsIgnoreCase("liaoli4")) {
            if (pc.getInventory().consumeItem(44071, 5)) {
            	CreateNewItem.createNewItem(pc, 49255, 1);//智力T恤
            } else {
            	pc.sendPackets(new S_SystemMessage("你的宣传币不够。"));
            }
        } else if (cmd.equalsIgnoreCase("liaoli5")) {
            if (pc.getInventory().consumeItem(44071, 5)) {
            	CreateNewItem.createNewItem(pc, 49256, 1);//智力T恤
            } else {
            	pc.sendPackets(new S_SystemMessage("你的宣传币不够。"));
            }
        } else if (cmd.equalsIgnoreCase("liaoli6")) {
            if (pc.getInventory().consumeItem(44071, 5)) {
            	CreateNewItem.createNewItem(pc, 49257, 1);//智力T恤
            } else {
            	pc.sendPackets(new S_SystemMessage("你的宣传币不够。"));
            }
        } else if (cmd.equalsIgnoreCase("liaoli7")) {
            if (pc.getInventory().consumeItem(44071, 5)) {
            	CreateNewItem.createNewItem(pc, 49258, 1);//智力T恤
            } else {
            	pc.sendPackets(new S_SystemMessage("你的宣传币不够。"));
            }
        } else if (cmd.equalsIgnoreCase("liaoli8")) {
            if (pc.getInventory().consumeItem(44071, 5)) {
            	CreateNewItem.createNewItem(pc, 49259, 1);//智力T恤
            } else {
            	pc.sendPackets(new S_SystemMessage("你的宣传币不够。"));
            }
        } else if (cmd.equalsIgnoreCase("yjww1")) {
        	pc.sendPackets(new S_SystemMessage("暂不开放这个兑换。"));
//        	if (pc.getInventory().consumeItem(44071, 300)) {
//        		CreateNewItem.createNewItem(pc, 59132, 1);//魔法娃娃：骑士(男骑士)
//        	} else {
//        		pc.sendPackets(new S_SystemMessage("你的宣传币不够。"));
//        	}
        } else if (cmd.equalsIgnoreCase("yjww2")) {
        	pc.sendPackets(new S_SystemMessage("暂不开放这个兑换。"));
//        	if (pc.getInventory().consumeItem(44071, 300)) {
//        		CreateNewItem.createNewItem(pc, 59133, 1);//魔法娃娃：妖精(男妖精)
//        	} else {
//        		pc.sendPackets(new S_SystemMessage("你的宣传币不够。"));
//        	}
        } else if (cmd.equalsIgnoreCase("yjww3")) {
        	pc.sendPackets(new S_SystemMessage("暂不开放这个兑换。"));
//        	if (pc.getInventory().consumeItem(44071, 300)) {
//        		CreateNewItem.createNewItem(pc, 59134, 1);//魔法娃娃：法师(男法师)
//        	} else {
//        		pc.sendPackets(new S_SystemMessage("你的宣传币不够。"));
//        	}
        }
    }
}
