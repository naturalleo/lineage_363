package com.lineage.server.command.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.TrapsSpawn;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SystemMessage;

/**
 * 重新载入陷阱
 * 
 * @author dexc
 * 
 */
public class L1ReloadTrap implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1ReloadTrap.class);

    private L1ReloadTrap() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1ReloadTrap();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        if (pc == null) {
            _log.warn("系统命令执行: " + cmdName + "重新载入陷阱。");

        } else {
            pc.sendPackets(new S_SystemMessage("重新载入陷阱"));
        }
        // 重新载入数据
        TrapsSpawn.get().reloadTraps();
    }
}
