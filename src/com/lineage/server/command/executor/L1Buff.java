package com.lineage.server.command.executor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.world.World;

/**
 * 附加给予指定状态(参数:对象(me,all)/技能编号)
 * 
 * @author dexc
 * 
 */
public class L1Buff implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1Buff.class);

    private L1Buff() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Buff();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            final StringTokenizer tok = new StringTokenizer(arg);
            Collection<L1PcInstance> players = null;
            String s = tok.nextToken();
            if (s.equals("me")) {
                players = new ArrayList<L1PcInstance>();
                players.add(pc);
                s = tok.nextToken();

            } else if (s.equals("all")) {
                players = World.get().getAllPlayers();
                s = tok.nextToken();

            } else {
                players = World.get().getVisiblePlayer(pc);
            }

            final int skillId = Integer.parseInt(s);
            int time = 0;
            if (tok.hasMoreTokens()) {
                time = Integer.parseInt(tok.nextToken());
            }

            final L1Skills skill = SkillsTable.get().getTemplate(skillId);

            if (skill.getTarget().equals("buff")) {
                for (final L1PcInstance tg : players) {
                    new L1SkillUse()
                            .handleCommands(pc, skillId, tg.getId(), tg.getX(),
                                    tg.getY(), time, L1SkillUse.TYPE_SPELLSC);
                }
            } else if (skill.getTarget().equals("none")) {
                for (final L1PcInstance tg : players) {
                    new L1SkillUse().handleCommands(tg, skillId, tg.getId(),
                            tg.getX(), tg.getY(), time, L1SkillUse.TYPE_GMBUFF);
                }
            } else {
                pc.sendPackets(new S_SystemMessage("buff系のスキルではありません。"));
            }
        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName());
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
