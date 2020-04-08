package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 三重矢
 * 
 * @author dexc
 * 
 */
public class TRIPLE_ARROW extends SkillMode {

    public TRIPLE_ARROW() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {

        final int dmg = 0;
        // 可施放外型限制
        boolean gfxcheck = false;
        final int playerGFX = srcpc.getTempCharGfx();
        switch (playerGFX) {
            case 138:
            case 37:
            case 3126:
            case 3420:
            case 3105:
            case 3145:
            case 3148:
            case 3151:
            case 4125:
            case 4950:
            case 6826:
            case 6827:
            case 6836:
            case 6837:
            case 6846:
            case 6847:
            case 6856:
            case 6857:
            case 6866:
            case 6867:
            case 6876:
            case 6877:
            case 6886:
            case 6887:
            case 8842:
            case 8845:
            case 7959:
            case 7967:
            case 7968:
            case 7969:
            case 7970:
            case 3860:
            case 3871:
            case 2323:
            case 2284:
            case 4918:
            case 4917:
            case 4919:
            case 3892:
            case 3895:
            case 3898:
            case 3901:
            case 4190:
            case 6140:
            case 6269:
            case 6145:
            case 6272:
            case 6150:
            case 6275:
            case 6155:
            case 6278:
            case 6160:
            case 6087:
            case 8900:
            case 9225:
            case 8913:
            case 9226:
            case 8860:
            case 8786:
            case 8792:
            case 8798:
            case 8804:
            case 8808:
                // 姬舞者
            case 10283:
            case 10286:
            case 10275:
                gfxcheck = true;
                break;
        }
        if (!gfxcheck) {
            return dmg;
        }

        for (int i = 0; i < 3; i++) {
            cha.onAction(srcpc);
        }
        // 三重矢 加速封包
        srcpc.sendPacketsX8(new S_SkillSound(srcpc.getId(), 4394));

        return dmg;
    }

    @Override
    public int start(final L1NpcInstance npc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {

        final int dmg = 0;
        for (int i = 3; i > 0; i--) {
            npc.attackTarget(cha);
        }
        npc.broadcastPacketX10(new S_SkillSound(npc.getId(), 4394));
        return dmg;
    }

    @Override
    public void start(final L1PcInstance srcpc, final Object obj)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void stop(final L1Character cha) throws Exception {

    }
}
