package com.lineage.server.command.executor;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.lock.AccountReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Account;

/**
 * 取回指定帐号资料(参数:帐号)
 * 
 * @author dexc
 * 
 */
public class L1Acc implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1Acc.class);

    private L1Acc() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Acc();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            if (pc == null) {
                _log.warn("系统命令执行: " + cmdName + " 取回指定帐号资料:" + arg);
            }
            final StringTokenizer st = new StringTokenizer(arg);
            final String accname = st.nextToken();

            L1Account acc = AccountReading.get().getAccount(
                    accname.toLowerCase());
            if (acc != null) {
                String ip = null;
                String mac = null;
                if (acc.get_ip() != null) {
                    ip = acc.get_ip();
                }
                if (acc.get_mac() != null) {
                    mac = acc.get_mac();
                }
                final StringBuilder info = new StringBuilder();
                info.append("IP位置:" + ip);
                info.append(" MAC位置:" + mac);
                info.append(" 最后登入时间:" + acc.get_lastactive().toString());
                if (pc == null) {
                    _log.warn(info.toString());
                } else {
                    pc.sendPackets(new S_ServerMessage(166, info.toString()));
                }

                final StringBuilder info2 = new StringBuilder();
                info2.append("密码:" + acc.get_password());
                info2.append(" 超级密码:" + acc.get_spw());
                info2.append(" 仓库密码:" + acc.get_warehouse());
                if (pc == null) {
                    _log.warn(info2.toString());
                } else {
                    pc.sendPackets(new S_ServerMessage(166, info2.toString()));
                }

            } else {
                String e = "指令异常: 没有该帐号(" + accname + ")!!";
                if (pc == null) {
                    _log.warn(e);
                } else {
                    pc.sendPackets(new S_ServerMessage(166, e));
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
