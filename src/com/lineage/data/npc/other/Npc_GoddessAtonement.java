package com.lineage.data.npc.other;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;

/**
 * 赎罪的神女<BR>
 * 70530,70611,70658,70757,70781,70801,70823
 * 
 * @author dexc
 * 
 */
public class Npc_GoddessAtonement extends NpcExecutor {

    /**
	 *
	 */
    private Npc_GoddessAtonement() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_GoddessAtonement();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "restore1pk"));
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        if (cmd.equalsIgnoreCase("pk")) { // 赎罪
            if (pc.getLawful() < 30000) {
                // \f1您目前的条件不符合赎罪的资格，请再次确认您的PK次数、正义值及金币是否符合。
                pc.sendPackets(new S_ServerMessage(559));

            } else if (pc.get_PKcount() < 5) {
                // \f1你目前PK的次数还不需要去减轻你的恶行。
                pc.sendPackets(new S_ServerMessage(560));

            } else {
                if (pc.getInventory().consumeItem(L1ItemId.ADENA, 700000)) {
                    pc.set_PKcount(pc.get_PKcount() - 5);
                    // 你的PK次数剩余%0次。
                    pc.sendPackets(new S_ServerMessage(561, String.valueOf(pc
                            .get_PKcount())));

                } else {
                    // 189 \f1金币不足。
                    pc.sendPackets(new S_ServerMessage(189));
                }
            }
        } else if (cmd.equalsIgnoreCase("lawful")) { //赎买正义
        	pc.sendPackets(new S_SystemMessage("GM已经关闭该功能"));
        	return;
//        	if (pc.getLawful() > 30000) {
//        		pc.sendPackets(new S_SystemMessage("你的正义值已到达30000以上"));
//        	} else if (pc.getInventory().consumeItem(L1ItemId.ADENA, 100000)) {
//        		pc.addLawful(500);
//        	} else {
//                // 189 \f1金币不足。
//                pc.sendPackets(new S_ServerMessage(189));
//            }
        }
    }
}
