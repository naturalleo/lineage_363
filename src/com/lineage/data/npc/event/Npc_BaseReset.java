package com.lineage.data.npc.event;

import static com.lineage.server.model.skill.L1SkillId.CANCELLATION;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_CharReset;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 71251 回忆蜡烛向导露露
 * 
 * @author loli
 * 
 */
public class Npc_BaseReset extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_BaseReset.class);

    private Npc_BaseReset() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_BaseReset();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "baseReset"));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        try {
            if (cmd.equalsIgnoreCase("ent")) {// 点燃回忆蜡烛

                if (!pc.getInventory().checkItem(49142)) { // 回忆蜡烛
                    pc.sendPackets(new S_ServerMessage(1290)); // 没有角色初始化所需要的道具。
                    return;
                }
                if (pc.getLevel() < 52) {
                	pc.sendPackets(new S_ServerMessage("\\aD必需52级以上才可以重置点数。"));
                	return;
                }

                // 消除现有技能状态
                final L1SkillUse l1skilluse = new L1SkillUse();
                l1skilluse.handleCommands(pc, CANCELLATION, pc.getId(),
                        pc.getX(), pc.getY(), 0, L1SkillUse.TYPE_LOGIN);

                pc.getInventory().takeoffEquip(945); // 脱除全部装备

                // 传送至转生用地图
                L1Teleport.teleport(pc, 32737, 32789, (short) 997, 4, false);
                pc.save();//人物存档
                final int initStatusPoint = 75 + pc.getElixirStats();
                int pcStatusPoint = pc.getBaseStr() + pc.getBaseInt()
                        + pc.getBaseWis() + pc.getBaseDex() + pc.getBaseCon()
                        + pc.getBaseCha();

                if (pc.getLevel() > 50) {
                    pcStatusPoint += (pc.getLevel() - 50 - pc.getBonusStats());
                }

                final int diff = pcStatusPoint - initStatusPoint;
                /**
                 * [50级以上]
                 * 
                 * 目前点数 - 初始点数 = 人物应有等级 - 50 -> 人物应有等级 = 50 + (目前点数 - 初始点数)
                 */
                int maxLevel = 1;

                if (diff > 0) {
                    // 最高到99级:也就是?不支援转生
//                    maxLevel = Math.min(50 + diff, 99);
                	maxLevel = pc.getLevel();

                } else {
                    maxLevel = pc.getLevel();
                }

                pc.setTempMaxLevel(maxLevel);
                pc.setTempLevel(1);
                pc.setInCharReset(true);
                pc.sendPackets(new S_CharReset(pc));
            }
            pc.sendPackets(new S_CloseList(pc.getId()));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
