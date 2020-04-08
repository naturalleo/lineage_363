package com.lineage.server.command.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 外挂处分(参数:人物名称 - 分钟)
 * 
 * @author dexc
 * 
 */
public class L1BanUser implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1BanUser.class);

    private L1BanUser() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1BanUser();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {

        } catch (final Exception e) {
            if (pc != null) {
                _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                        + " 执行的GM:" + pc.getName());
                // 261 \f1指令错误。
                pc.sendPackets(new S_ServerMessage(261));
            }
        }
    }
}
