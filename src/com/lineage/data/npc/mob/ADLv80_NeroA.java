package com.lineage.data.npc.mob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ADLv80_2;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;
import com.lineage.server.world.World;

/**
 * 70976 阿尔波斯 耐尔(A)<BR>
 * 70976<BR>
 * 
 * @author dexc
 * 
 */
public class ADLv80_NeroA extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(ADLv80_NeroA.class);

    private ADLv80_NeroA() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new ADLv80_NeroA();
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
                // 任务已经开始
                if (pc.getMapId() == ADLv80_2.MAPID) {
                    for (final L1Object obj : World.get()
                            .getVisibleObjects(ADLv80_2.MAPID).values()) {
                        if (obj instanceof L1DoorInstance) {
                            final L1DoorInstance door = (L1DoorInstance) obj;
                            if (door.get_showId() != pc.get_showId()) {
                                continue;
                            }
                            if (door.getDoorId() == 10008) {// A
                                door.open();
                                door.deleteMe();
                            }
                        }
                    }
                }
            }
            return pc;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }
}
