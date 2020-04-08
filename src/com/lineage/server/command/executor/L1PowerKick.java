package com.lineage.server.command.executor;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.commons.system.LanSecurityManager;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.IpReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxGm;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;

/**
 * 封锁IP/MAC(参数:人物名称/选单)
 * 
 * @author dexc
 * 
 */
public class L1PowerKick implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1PowerKick.class);

    private L1PowerKick() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1PowerKick();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            String gmName = "";
            if (pc == null) {
                _log.warn("系统命令执行: " + cmdName + " " + arg + " 封锁IP/MAC。");
                gmName = "系统命令";

            } else {
                gmName = pc.getName() + "命令";
            }

            // XXX 解除
            if (arg.indexOf("remove") != -1) {
                final StringTokenizer st = new StringTokenizer(arg);
                st.nextToken();
                final String ipaddr = st.nextToken();

                boolean isBan = false;// 已经是封锁位置

                if (LanSecurityManager.BANIPMAP.containsKey(arg)) {
                    isBan = true;
                }

                if (!isBan) {
                    // 加入IP/MAC封锁
                    IpReading.get().remove(ipaddr);
                    _log.warn("系统命令执行: " + cmdName + " " + ipaddr
                            + " 解除封锁IP/MAC。");
                }
                return;

                // XXX 封锁
            } else if (arg.lastIndexOf(".") != -1) {
                if (!LanSecurityManager.BANIPMAP.containsKey(arg)) {
                    // 加入IP封锁
                    IpReading.get().add(arg.toString(),
                            gmName + ":L1PowerKick 封锁IP");
                }
                return;
            }

            final L1PcInstance target = World.get().getPlayer(arg);

            if (target != null) {
                final ClientExecutor targetClient = target.getNetConnection();
                final String ipaddr = targetClient.getIp().toString();
                if (ipaddr != null) {
                    if (!LanSecurityManager.BANIPMAP.containsKey(ipaddr)) {
                        // 加入IP封锁
                        IpReading.get().add(ipaddr.toString(),
                                gmName + ":L1PowerKick 封锁IP");
                    }
                }
                if (pc != null) {
                    pc.sendPackets(new S_SystemMessage(target.getName()
                            + " 封锁IP/MAC。"));
                }
                targetClient.kick();

            } else {
                if (pc == null) {
                    _log.error("指令异常: 这个命令必须输入正确人物名称 或是 IP/MAC位置才能执行。");

                } else {
                    final int mode = 6;
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
}
