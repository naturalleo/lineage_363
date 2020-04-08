package com.lineage.server.command.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;

/**
 * 解除卡点(参数:人物名称)
 * 
 * @author dexc
 * 
 */
public class L1SKick implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1SKick.class);

    private L1SKick() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1SKick();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            if (pc == null) {
                _log.warn("系统命令执行: " + cmdName + " " + arg + " 解除卡点。");
            }
            final L1PcInstance target = World.get().getPlayer(arg);
            if (target != null) {
                String info = "进行 人物:" + target.getName() + " 解除卡点作业。";
                if (pc == null) {
                    _log.warn(info);

                } else {
                    pc.sendPackets(new S_SystemMessage(info));
                }
                // SKTへ移动させる
                target.setX(33080);
                target.setY(33392);
                target.setMap((short) 4);
                final ClientExecutor targetClient = target.getNetConnection();
                targetClient.kick();

            } else {
                if (pc == null) {
                    _log.warn(arg + " 不在线上。");

                } else {
                    // 73 \f1%0%d 不在线上。
                    pc.sendPackets(new S_ServerMessage(73, arg));
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
}
