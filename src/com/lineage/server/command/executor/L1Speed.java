package com.lineage.server.command.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 赋予GM加速状态
 * 
 * @author dexc
 * 
 */
public class L1Speed implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1Speed.class);

    private L1Speed() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Speed();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            L1BuffUtil.haste(pc, 3600 * 1000);

            L1BuffUtil.brave(pc, 3600 * 1000);

        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName());
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
