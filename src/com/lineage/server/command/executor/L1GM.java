package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SystemMessage;

/**
 * 隐藏显示GM属性
 * 
 * @author dexc
 * 
 */
public class L1GM implements L1CommandExecutor {

    private L1GM() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1GM();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        pc.setGm(!pc.isGm());
        pc.sendPackets(new S_SystemMessage("setGm = " + pc.isGm()));
    }
}
