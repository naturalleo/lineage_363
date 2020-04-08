/**
 *                            License
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS  
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). 
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW.  
 * ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS LICENSE OR  
 * COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND  
 * AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE  
 * MAY BE CONSIDERED TO BE A CONTRACT,
 * THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED 
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */
package com.lineage.data.npc.teleport;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 奇岩出入口传送师 - 50035.
 * 
 * @version 1.0
 * @author hjx1000
 * @data 
 */
public class Qiyan_Telecport extends NpcExecutor {

    @Override
    public int type() {
        // TODO Auto-generated method stub
        return 3;
    }

    public static NpcExecutor get() {
        return new Qiyan_Telecport();
    }

    private Qiyan_Telecport() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.lineage.server.model.npc.NpcExecutor#attack(com.lineage.server.model
     * .Instance.L1PcInstance, com.lineage.server.model.Instance.L1NpcInstance)
     */
    @Override
    public void attack(final L1PcInstance pc, final L1NpcInstance npc) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.lineage.server.model.npc.NpcExecutor#death(com.lineage.server.model
     * .L1Character, com.lineage.server.model.Instance.L1NpcInstance)
     */
    @Override
    public L1PcInstance death(final L1Character lastAttacker,
            final L1NpcInstance npc) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.lineage.server.model.npc.NpcExecutor#npcTalkAction(com.lineage.server
     * .model.Instance.L1PcInstance,
     * com.lineage.server.model.Instance.L1NpcInstance, java.lang.String, int)
     */
    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String s, final long objid) {
        //System.out.println("对话");
        if (s.equalsIgnoreCase("teleportURL")) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "telecgiran2"));
        }
        if (s.equalsIgnoreCase("teleport giranc-girandun")) {
//        	if (pc.getLevel() > 75) {
//        		pc.sendPackets(new S_SystemMessage("必需76级以下才可以进入。"));
//        		return;
//        	}
            final int time = pc.getRocksPrisonTime();
            final int maxTime = 10800; // 3小时
            if (time >= maxTime) {
                final S_ServerMessage msg = new S_ServerMessage(1523,
                        String.valueOf(180));
                pc.sendPackets(msg); // 您已经使用完了可以逗留在地监的%0分钟
                return;
            }
            final int locx = 32731;
            final int locy = 32728;
            final short mapid = 55;
            final int heading = 5;
            L1Teleport.teleport(pc, locx, locy, mapid, heading, true);
            pc.startRocksPrison();
        } else if (s.equalsIgnoreCase("teleport giranc-village")) {
            final int locx = 33438;
            final int locy = 32796;
            final short mapid = 4;
            final int heading = 5;
            L1Teleport.teleport(pc, locx, locy, mapid, heading, true);
        } else if (s.equalsIgnoreCase("teleport giranc-dragonvalley")) {
            final int locx = 33432;
            final int locy = 32546;
            final short mapid = 4;
            final int heading = 5;
            L1Teleport.teleport(pc, locx, locy, mapid, heading, true);
        } else if (s.equalsIgnoreCase("teleport giranc-littlebb")) {
            final int locx = 33517;
            final int locy = 32851;
            final short mapid = 4;
            final int heading = 5;
            L1Teleport.teleport(pc, locx, locy, mapid, heading, true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.lineage.server.model.npc.NpcExecutor#npcTalkReturn(com.lineage.server
     * .model.Instance.L1PcInstance,
     * com.lineage.server.model.Instance.L1NpcInstance)
     */
    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {

        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "telecgiran1"));

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.lineage.server.model.npc.NpcExecutor#spawn(com.lineage.server.model
     * .Instance.L1NpcInstance)
     */
    @Override
    public void spawn(final L1NpcInstance npc) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.lineage.server.model.npc.NpcExecutor#work(com.lineage.server.model
     * .Instance.L1NpcInstance)
     */
    @Override
    public void work(final L1NpcInstance npc) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.lineage.server.model.npc.NpcExecutor#workTime()
     */
    @Override
    public int workTime() {
        // TODO Auto-generated method stub
        return 0;
    }
}
