package com.lineage.server.command.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.EchoServerTimer;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 重新启用监听
 */
public class L1Echo implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1Echo.class);

    private L1Echo() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Echo();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            /*
             * if (pc == null) { _log.warn("系统命令执行: " + cmdName + " " + arg +
             * " 监听端口设置作业。"); }
             */
            if (arg.equalsIgnoreCase("stop")) {
                if (pc == null) {
                    _log.warn("系统命令执行: 全部端口关闭监听!");

                } else {
                    pc.sendPackets(new S_ServerMessage(166, "全部端口关闭监听!"));
                }
                EchoServerTimer.get().stopEcho();
                return;
            }
            String info = "重新启动服务器端口监听!";
            _log.info(info);
            // 监听端口启动重置作业
            EchoServerTimer.get().reStart();
            if (pc != null) {
                pc.sendPackets(new S_ServerMessage(166, info));
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

        } finally {
            _log.info("监听端口设置作业完成!!");
        }
    }
}
