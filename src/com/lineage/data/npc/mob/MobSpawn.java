package com.lineage.data.npc.mob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.utils.CheckUtil;
import com.lineage.server.utils.L1SpawnUtil;

/**
 * NPC 死亡召唤指定NPC<BR>
 * 设置范例:<BR>
 * classname: mob.MobSpawn 设置范例: mob.MobSpawn 45601<BR>
 * 
 * @author dexc
 * 
 */
public class MobSpawn extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(MobSpawn.class);

    private MobSpawn() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new MobSpawn();
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
                if (_npcid != 0) {
                    final L1Npc l1npc = NpcTable.get().getTemplate(_npcid);
                    if (l1npc == null) {
                        _log.error("召唤NPC编号: " + _npcid
                                + " 不存在资料库中!(mob.MobSpawn)");
                        return pc;
                    }
                    final L1NpcInstance newnpc = L1SpawnUtil.spawnT(_npcid,
                            npc.getX(), npc.getY(), npc.getMapId(),
                            npc.getHeading(), 300);// 5分钟
                    newnpc.onNpcAI();
                    // newnpc.turnOnOffLight();
                    newnpc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);// 如有对话开始说话
                }
            }
            return pc;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    private int _npcid = 0;

    @Override
    public void set_set(String[] set) {
        try {
            _npcid = Integer.parseInt(set[1]);

        } catch (final Exception e) {
        }
    }
}
