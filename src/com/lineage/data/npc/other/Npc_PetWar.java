package com.lineage.data.npc.other;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.L1PetMatch;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 80088 宠物战管理人
 * 
 * @author loli
 * 
 */
public class Npc_PetWar extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_PetWar.class);

    /**
	 *
	 */
    private Npc_PetWar() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_PetWar();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "petmatcher"));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        try {
            boolean isCloseList = false;
            final String[] temp = cmd.split(",");
            final int objid2 = Integer.valueOf(temp[2]);

            final Object[] petlist = pc.getPetList().values().toArray();
            if (petlist.length > 0) {
                // 1187 宠物项链正在使用中。
                pc.sendPackets(new S_ServerMessage(1187));
                return;
            }
            if (!L1PetMatch.getInstance().enterPetMatch(pc, objid2)) {
                // 1182 游戏已经开始了
                pc.sendPackets(new S_ServerMessage(1182));
                return;
            }

            if (temp[0].equalsIgnoreCase("ent")) {

            }

            if (isCloseList) {
                // 关闭对话窗
                pc.sendPackets(new S_CloseList(pc.getId()));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
