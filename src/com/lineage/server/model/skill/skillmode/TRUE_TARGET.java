package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_TrueTarget;
import com.lineage.server.world.WorldClan;

/**
 * 精准目标
 * 
 * @author dexc
 * 
 */
public class TRUE_TARGET extends SkillMode {

    public TRUE_TARGET() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;
        // 对自己发送封包
        srcpc.sendPackets(new S_TrueTarget(cha.getId(), srcpc.getId(), srcpc
                .getText()));
        // 施展者具有血盟
        if (srcpc.getClan() != null) {
            final L1PcInstance[] players = WorldClan.get()
                    .getClan(srcpc.getClanname()).getOnlineClanMember();
            // 对血盟成员发送封包
            for (final L1PcInstance pc : players) {
                // 范围内盟友
                if (pc.getLocation().isInScreen(srcpc.getLocation())) {
                    pc.sendPackets(new S_TrueTarget(cha.getId(), pc.getId(),
                            srcpc.getText()));
                }
            }
        }
        // 清空暂时文字串
        srcpc.setText("");
        return dmg;
    }

    @Override
    public int start(final L1NpcInstance npc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;

        return dmg;
    }

    @Override
    public void start(final L1PcInstance srcpc, final Object obj)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void stop(final L1Character cha) throws Exception {
        // TODO Auto-generated method stub
    }
}
