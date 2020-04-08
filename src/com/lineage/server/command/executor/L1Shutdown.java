package com.lineage.server.command.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.Shutdown;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 关机指令(参数:now()/abort()/倒数秒数)
 * 
 * @author dexc
 * 
 */
public class L1Shutdown implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1Shutdown.class);

    private L1Shutdown() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Shutdown();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            if (pc == null) {
                _log.warn("系统命令执行: " + cmdName + " " + arg + " 秒后关闭伺服器。");
            }
            if (arg.equalsIgnoreCase("now")) {
                Shutdown.getInstance().startShutdown(pc, 5, true);
                return;
            }
            if (arg.equalsIgnoreCase("abort")) {
                Shutdown.getInstance().abort(pc);
                return;
            }
            final int sec = Math.max(5, Integer.parseInt(arg));
            Shutdown.getInstance().startShutdown(pc, sec, true);

        } catch (final Exception e) {
            if (pc == null) {
                _log.error("错误的命令格式: " + this.getClass().getSimpleName());

            } else {
                _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                        + " 执行的GM:" + pc.getName());
                // 261 \f1指令错误。
                pc.sendPackets(new S_ServerMessage(261));
            }
        }
    }
}
