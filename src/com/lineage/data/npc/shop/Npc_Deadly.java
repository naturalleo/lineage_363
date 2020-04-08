package com.lineage.data.npc.shop;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 致命魔法卷轴打造 98020<BR>
 * 
 * 
 * @author hjx1000
 * 
 */
public class Npc_Deadly extends NpcExecutor {

    /**
	 *
	 */
    private Npc_Deadly() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Deadly();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "deadly"));
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        if (cmd.equalsIgnoreCase("deadly_lv1")) {
            final int[] items = new int[] { 140087, 240087, 140074 };
            final int[] counts = new int[] { 10, 10, 10};
            final int[] gitems = new int[] { 55111 };
            final int[] gcounts = new int[] { 1 };
            if (CreateNewItem.checkNewItem(pc, items, counts) >= 1) {
                CreateNewItem.createNewItem(pc, items, counts, gitems, 1, gcounts);// 给予
            }
        } else if (cmd.equalsIgnoreCase("deadly_lv2")) {
        	final int[] items = new int[] { 140087, 240087, 140074 };
        	final int[] counts = new int[] { 20, 20, 20};
        	final int[] gitems = new int[] { 55112 };
        	final int[] gcounts = new int[] { 1 };
            if (CreateNewItem.checkNewItem(pc, items, counts) >= 1) {
                CreateNewItem.createNewItem(pc, items, counts, gitems, 1, gcounts);// 给予
            }
        } else if (cmd.equalsIgnoreCase("deadly_lv3")) {
        	final int[] items = new int[] { 140087, 240087, 140074, 40964 };
        	final int[] counts = new int[] { 30, 30, 30, 20 };
        	final int[] gitems = new int[] { 55113 };
        	final int[] gcounts = new int[] { 1 };
            if (CreateNewItem.checkNewItem(pc, items, counts) >= 1) {
                CreateNewItem.createNewItem(pc, items, counts, gitems, 1, gcounts);// 给予
            }
        } else if (cmd.equalsIgnoreCase("deadly_lv4")) {
        	final int[] items = new int[] { 140087, 240087, 140074, 50634 };
        	final int[] counts = new int[] { 40, 40, 40, 20 };
        	final int[] gitems = new int[] { 55114 };
        	final int[] gcounts = new int[] { 1 };
            if (CreateNewItem.checkNewItem(pc, items, counts) >= 1) {
                CreateNewItem.createNewItem(pc, items, counts, gitems, 1, gcounts);// 给予
            }
        } else if (cmd.equalsIgnoreCase("deadly_lv5")) {
        	final int[] items = new int[] { 140087, 240087, 140074, 40993 };
        	final int[] counts = new int[] { 50, 50, 50, 20 };
        	final int[] gitems = new int[] { 55115 };
        	final int[] gcounts = new int[] { 1 };
            if (CreateNewItem.checkNewItem(pc, items, counts) >= 1) {
                CreateNewItem.createNewItem(pc, items, counts, gitems, 1, gcounts);// 给予
            }
        }
        // 关闭对话窗
        pc.sendPackets(new S_CloseList(pc.getId()));
    }
}
