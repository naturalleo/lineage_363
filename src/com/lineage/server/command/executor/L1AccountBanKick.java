package com.lineage.server.command.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.lock.AccountReading;
import com.lineage.server.datatables.lock.IpReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxGm;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;

/**
 * 帐号封锁(参数:帐号)
 * 
 * @author dexc
 * 
 */
public class L1AccountBanKick implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1AccountBanKick.class);

    private L1AccountBanKick() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1AccountBanKick();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            String gmName = "";
            if (pc == null) {
                _log.warn("系统命令执行: " + cmdName + " 帐号封锁:" + arg);
                gmName = "系统命令";

            } else {
                gmName = pc.getName() + "命令";
            }

            final L1PcInstance target = World.get().getPlayer(arg);

            if (target != null) {
                String info = target.getName() + " 该人物帐号完成封锁。";
                if (pc == null) {
                    _log.warn(info);

                } else {
                    pc.sendPackets(new S_SystemMessage(info));
                }
                start(target, gmName + ":L1AccountBanKick 封锁帐号");

            } else {
                final boolean account = AccountReading.get().isAccount(arg);
                // 输入资料是否为帐号
                if (account) {
                    IpReading.get().add(arg, gmName + ":L1AccountBanKick 封锁帐号");
                    return;
                }
                if (pc == null) {
                    _log.error("指令异常: 这个命令必须输入正确帐号名称才能执行。");

                } else {
                    final int mode = 7;
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

    /**
     * 加入封锁帐号
     * 
     * @param target
     * @param info
     */
    private void start(final L1PcInstance target, String info) {
        // 加入封锁帐号
        IpReading.get().add(target.getAccountName(), info);
        target.getNetConnection().kick();
    }
}
