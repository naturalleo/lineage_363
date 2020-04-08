package com.lineage.server.command.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_GMHtml;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 显示指定HTML
 * 
 * @author dexc
 * 
 */
public class L1Html implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1Html.class);

    private L1Html() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Html();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {

            pc.sendPackets(new S_GMHtml(pc.getId(), arg));

        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName());
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
