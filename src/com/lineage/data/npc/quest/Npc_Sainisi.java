package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 赛尼斯<BR>
 * 91297<BR>
 * 说明:魔法师．哈汀(故事)
 * 
 * @author dexc
 * 
 */
public class Npc_Sainisi extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Sainisi.class);

    private Npc_Sainisi() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Sainisi();
    }

    @Override
    public int type() {
        return 1;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.get_hardinR() != null) {
                if (pc.get_hardinR().get_time() > 0
                        && pc.get_hardinR().get_time() <= 72) {
                    // 依欧林你来看，哈汀对我感不感兴趣呢？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_ep003"));

                } else if (pc.get_hardinR().get_time() > 74
                        && pc.get_hardinR().get_time() <= 96) {
                    // 啊！ 那些妖魔们…。 这下麻烦了，欧林拜托你了。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_ep005"));

                } else if (pc.get_hardinR().get_time() > 96
                        && pc.get_hardinR().get_time() <= 104) {
                    // 妖魔们该怎么办啊！ 哈汀知道这里是妖魔巢穴吗？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_ep006"));

                } else if (pc.get_hardinR().get_time() > 136
                        && pc.get_hardinR().get_time() <= 156) {
                    // 现在只剩最后一个。 欧林也要加油啊！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_ep007"));

                } else if (pc.get_hardinR().get_time() > 156
                        && pc.get_hardinR().get_time() <= 170) {
                    // 先让欧林移动吧！ 快去吧！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_ep008"));
                } else {
                    // 准备这么慢，是不是下面的人偷懒啊！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_ep004"));
                }

            } else {
                // 该讯息只有发生错误时才会显示。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_html05"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
