package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 弱化属性
 * 
 * @author dexc
 * 
 */
public class ELEMENTAL_FALL_DOWN extends SkillMode {

    public ELEMENTAL_FALL_DOWN() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;
        if (!cha.hasSkillEffect(L1SkillId.ELEMENTAL_FALL_DOWN)) {
            final int playerAttr = srcpc.getElfAttr();
            final int i = -50;
            if (cha instanceof L1PcInstance) {
                final L1PcInstance pc = (L1PcInstance) cha;
                switch (playerAttr) {
                    case 0:
                        srcpc.sendPackets(new S_ServerMessage(79));
                        break;
                    case 1:
                        pc.addEarth(i);
                        pc.setAddAttrKind(1);
                        break;
                    case 2:
                        pc.addFire(i);
                        pc.setAddAttrKind(2);
                        break;
                    case 4:
                        pc.addWater(i);
                        pc.setAddAttrKind(4);
                        break;
                    case 8:
                        pc.addWind(i);
                        pc.setAddAttrKind(8);
                        break;
                    default:
                        break;
                }

            } else if (cha instanceof L1MonsterInstance) {
                final L1MonsterInstance mob = (L1MonsterInstance) cha;
                switch (playerAttr) {
                    case 0:
                        srcpc.sendPackets(new S_ServerMessage(79));
                        break;
                    case 1:
                        mob.addEarth(i);
                        mob.setAddAttrKind(1);
                        break;
                    case 2:
                        mob.addFire(i);
                        mob.setAddAttrKind(2);
                        break;
                    case 4:
                        mob.addWater(i);
                        mob.setAddAttrKind(4);
                        break;
                    case 8:
                        mob.addWind(i);
                        mob.setAddAttrKind(8);
                        break;
                    default:
                        break;
                }
            }
        }
        cha.setSkillEffect(L1SkillId.ELEMENTAL_FALL_DOWN, integer * 1000);

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
        final int i = 50;
        if (cha instanceof L1PcInstance) {
            final L1PcInstance pc = (L1PcInstance) cha;
            final int attr = pc.getAddAttrKind();
            switch (attr) {
                case 1:
                    pc.addEarth(i);
                    break;
                case 2:
                    pc.addFire(i);
                    break;
                case 4:
                    pc.addWater(i);
                    break;
                case 8:
                    pc.addWind(i);
                    break;
                default:
                    break;
            }
            pc.setAddAttrKind(0);
            pc.sendPackets(new S_OwnCharAttrDef(pc));

        } else if (cha instanceof L1NpcInstance) {
            final L1NpcInstance npc = (L1NpcInstance) cha;
            final int attr = npc.getAddAttrKind();
            switch (attr) {
                case 1:
                    npc.addEarth(i);
                    break;
                case 2:
                    npc.addFire(i);
                    break;
                case 4:
                    npc.addWater(i);
                    break;
                case 8:
                    npc.addWind(i);
                    break;
                default:
                    break;
            }
            npc.setAddAttrKind(0);
        }
    }
}
