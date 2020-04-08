package com.lineage.data.npc.mob;

import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.CKEWLv50_1;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;
import com.lineage.server.world.World;

/**
 * 再生之祭坛<BR>
 * 87000<BR>
 * 
 * @author dexc
 * 
 */
public class CKEW50_Altar extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(CKEW50_Altar.class);

    private CKEW50_Altar() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new CKEW50_Altar();
    }

    @Override
    public int type() {
        return 8;
    }

    @Override
    public L1PcInstance death(final L1Character lastAttacker,
            final L1NpcInstance npc) {
        try {
            // 判断主要攻击者
            final L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);

            if (pc != null) {
                final HashMap<Integer, L1Object> mapList = new HashMap<Integer, L1Object>();
                mapList.putAll(World.get().getVisibleObjects(CKEWLv50_1.MAPID));

                for (L1Object tgobj : mapList.values()) {
                    if (tgobj instanceof L1PcInstance) {
                        final L1PcInstance tgpc = (L1PcInstance) tgobj;
                        // 相同副本
                        if (tgpc.get_showId() == pc.get_showId()) {

                            // 将任务进度提升为3
                            tgpc.getQuest().set_step(CKEWLv50_1.QUEST.get_id(),
                                    3);

                            if (tgpc.getInventory().checkItem(49241)) { // 已经具有物品
                                continue;
                            }
                            // 取得任务道具
                            CreateNewItem.getQuestItem(tgpc, npc, 49241, 1);// 祭坛的碎片
                        }
                    }
                }
                mapList.clear();
            }
            return pc;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }
}
