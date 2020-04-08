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
package com.lineage.server.command.executor;

import java.util.StringTokenizer;

import com.lineage.server.model.L1UTDSpawn;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBox;

/**
 * .
 * 
 * @version 1.0
 * @author jrwz
 * @data 2013-5-5下午1:37:01
 */
public class Check implements L1CommandExecutor {
    private Check() {
    }

    public static L1CommandExecutor getInstance() {
        return new Check();
    }

    @Override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        //final StringTokenizer tok = new StringTokenizer(arg);
        //final int opid = Integer.parseInt(tok.nextToken());
        // try {
        // SOpTest.testOp(pc, opid);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // pc.sendPackets(new S_Lock(opid));
       // S_PacketBox spb = new S_PacketBox(0, opid * 60, null);
        //pc.sendPackets(spb);
        L1UTDSpawn.getInstance().addMember(pc);
        L1UTDSpawn.getInstance().start();

    }
}
