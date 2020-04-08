package com.lineage.server.command.executor;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.EchoServerTimer;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 关闭/开启指定监听端口(参数:stop/start 端口编号)
 * 
 * @author dexc
 * 
 */
public class L1Port implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1Port.class);

    private L1Port() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Port();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            final StringTokenizer st = new StringTokenizer(arg);
            final String cmd = st.nextToken();
            final int key = Integer.valueOf(st.nextToken());

            if (cmd.equalsIgnoreCase("stop")) {
                _log.warn("系统命令执行: " + cmdName + " " + arg + " 关闭 指定监听端口。");
                EchoServerTimer.get().stopPort(key);

            } else if (cmd.equalsIgnoreCase("start")) {
                _log.warn("系统命令执行: " + cmdName + " " + arg + " 开启 指定监听端口。");
                EchoServerTimer.get().startPort(key);
            }

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
