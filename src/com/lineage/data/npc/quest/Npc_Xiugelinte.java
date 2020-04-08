package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 休格琳特<BR>
 * 81375<BR>
 * 说明:穿越时空的探险(秘谭)
 * 
 * @author dexc
 * 
 */
public class Npc_Xiugelinte extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Xiugelinte.class);

    private Npc_Xiugelinte() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Xiugelinte();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            // 据说在新发现的地监出现了各种过去的物品
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "hugrint1"));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        try {
            if (cmd.equalsIgnoreCase("0")) {// 购买魔法气息。
                if (pc.getInventory().checkItem(49335)) {// 身上有魔法气息
                    // 你已经有魔法气息了
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "hugrint4"));
                    return;
                }
                // 需要的物件确认
                final L1ItemInstance item = pc.getInventory().checkItemX(40308,
                        1000);

                if (item != null) {
                    pc.getInventory().removeItem(item, 1000);// 删除道具
                    // 给予道具(魔法气息 49312)
                    CreateNewItem.createNewItem(pc, 49335, 1);
                    // 好好的使用它。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "hugrint2"));

                } else {
                    // 还是要补贴一点研究费
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "hugrint3"));
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
