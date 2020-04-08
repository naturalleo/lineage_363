package com.lineage.server.command.executor;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.CommandsTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Command;

/**
 * 显示管理指令群
 * 
 * @author daien
 * 
 */
public class L1CommandHelp implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1CommandHelp.class);

    /**
     * 显示管理指令群
     */
    private L1CommandHelp() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1CommandHelp();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        final ArrayList<L1Command> list = new ArrayList<L1Command>();
        for (final L1Command command : CommandsTable.get().getList()) {
            if (pc == null) {
                if (command.isSystem()) {
                    list.add(command);
                }

            } else {
                if (command.getLevel() <= pc.getAccessLevel()) {
                    list.add(command);
                }
            }
        }
        for (final L1Command command : list) {
            if (pc == null) {
                _log.info("可用命令: " + command.getName() + ": "
                        + command.get_note());

            } else {
                pc.sendPackets(new S_ServerMessage(166, command.getName()
                        + ": " + command.get_note()));
            }
        }
        if (pc == null) {
            _log.info("可用命令: c: 对游戏中玩家广播公告.");
        }
    }
}
