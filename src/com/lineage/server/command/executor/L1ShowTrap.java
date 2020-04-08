package com.lineage.server.command.executor;

import static com.lineage.server.model.skill.L1SkillId.GMSTATUS_SHOWTRAPS;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1TrapInstance;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 显示陷阱位置(参数:on显示/off关闭显示)
 * 
 * @author dexc
 * 
 */
public class L1ShowTrap implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1ShowTrap.class);

    private L1ShowTrap() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1ShowTrap();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            if (pc.hasSkillEffect(GMSTATUS_SHOWTRAPS)) {
                pc.removeSkillEffect(GMSTATUS_SHOWTRAPS);

                for (final L1Object obj : pc.getKnownObjects()) {
                    if (obj instanceof L1TrapInstance) {
                        pc.removeKnownObject(obj);
                        pc.sendPackets(new S_RemoveObject(obj));
                    }
                }

            } else {
                pc.setSkillEffect(GMSTATUS_SHOWTRAPS, 0);
            }

        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName());
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
        /*
         * if (arg.equalsIgnoreCase("on")) {
         * pc.setSkillEffect(GMSTATUS_SHOWTRAPS, 0);
         * 
         * } else if (arg.equalsIgnoreCase("off")) {
         * pc.removeSkillEffect(GMSTATUS_SHOWTRAPS);
         * 
         * for (final L1Object obj : pc.getKnownObjects()) { if (obj instanceof
         * L1TrapInstance) { pc.removeKnownObject(obj); pc.sendPackets(new
         * S_RemoveObject(obj)); } }
         * 
         * } else { // 261 \f1指令错误。 pc.sendPackets(new S_ServerMessage(261)); }
         */
    }
}
