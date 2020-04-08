package com.lineage.server.command.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxGm;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;

/**
 * 踢除下线(参数:人物名称/选单)
 * 
 * @author dexc
 * 
 */
public class L1Kick implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1Kick.class);

    private L1Kick() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Kick();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            final L1PcInstance target = World.get().getPlayer(arg);

            if (target != null) {
                if (pc == null) {
                    _log.warn("系统命令执行: " + cmdName + " 踢除:" + arg + " 下线。");

                } else {
                    pc.sendPackets(new S_SystemMessage("踢除:" + arg + " 下线。"));
                }
                start(target);

            } else {
                if (pc == null) {
                    _log.error("指令异常: 指定人物不在线上，这个命令必须输入正确人物名称才能执行。");

                } else {
                    final int mode = 5;
                    pc.sendPackets(new S_PacketBoxGm(pc, mode));
                }
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

    public void start(final L1PcInstance target) {
        final ClientExecutor targetClient = target.getNetConnection();
        targetClient.kick();
    }
}
