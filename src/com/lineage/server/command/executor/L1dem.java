package com.lineage.server.command.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.command.GmHtml;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * DE清单
 * 
 * @author dexc
 * 
 */
public class L1dem implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1dem.class);

    private L1dem() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1dem();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            GmHtml gmHtml = new GmHtml(pc, 1);
            gmHtml.show();

        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName());
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
