package com.lineage.data.npc.mob;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.L1ItemDelay;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 滚动的南瓜 45166<BR>
 * 跳动的南瓜 45167<BR>
 * 
 * @author dexc
 * 
 */
public class E_Event1030 extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(E_Event1030.class);

    private E_Event1030() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new E_Event1030();
    }

    @Override
    public int type() {
        return 4;
    }

    private static Random _random = new Random();

    /**
     * NPC受到攻击(4)<BR>
     * 任务NPC作为抵达目标检查的方法
     * 
     * @param pc
     * @param npc
     */
    @Override
    public void attack(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            int i = _random.nextInt(100);
            if (i >= 0 && i <= 1) {
                if (pc.hasSkillEffect(L1SkillId.ADLV80_2_1)) {
                    return;
                }
                if (pc.hasSkillEffect(L1SkillId.ADLV80_2_2)) {
                    return;
                }
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7782));
                pc.setSkillEffect(L1SkillId.ADLV80_2_2, 12 * 1000);

            } else if (i >= 2 && i <= 5) {
                if (npc.isremovearmor()) {
                    return;
                }
                if (pc.hasSkillEffect(L1SkillId.ADLV80_2_1)) {
                    return;
                }
                if (pc.hasSkillEffect(L1SkillId.ADLV80_2_2)) {
                    return;
                }
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7781));
                pc.setSkillEffect(L1SkillId.ADLV80_2_1, 12 * 1000);

                npc.set_removearmor(true);

            } else if (i >= 94 && i <= 99) {
                if (npc.isremovearmor()) {
                    return;
                }
                // 武器禁止使用
                if (pc.hasItemDelay(L1ItemDelay.WEAPON)) {
                    return;
                }
                // 500:武器禁止使用
                L1ItemDelay.onItemUse(pc, L1ItemDelay.WEAPON, 2000);
                // 使用牛的代号脱除全部装备
                pc.getInventory().takeoffEquip(945);
                // 1,356：盔甲的连接部分被破坏了。
                pc.sendPackets(new S_ServerMessage(1356));
                // 1,027：装备的武器被强制解除。
                pc.sendPackets(new S_ServerMessage(1027));

                npc.set_removearmor(true);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
