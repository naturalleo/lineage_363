package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.WorldTrap;

/**
 * 重新配置陷阱
 * 
 * @author dexc
 * 
 */
public class L1ResetTrap implements L1CommandExecutor {

    private L1ResetTrap() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1ResetTrap();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        WorldTrap.get().resetAllTraps();
        pc.sendPackets(new S_SystemMessage("重新配置陷阱!"));
    }
}
