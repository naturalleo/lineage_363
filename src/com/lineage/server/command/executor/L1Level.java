package com.lineage.server.command.executor;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.ExpTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.utils.RangeInt;
import com.lineage.server.world.World;

/**
 * 变更该GM人物等级
 * 
 * @author dexc
 * 
 */
public class L1Level implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1Level.class);
    
    /** 目标. */
    private L1PcInstance target;
    
    /** 等级. */
    private int level;

    private L1Level() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Level();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            final StringTokenizer tok = new StringTokenizer(arg);
            final String charName = tok.nextToken();
            if (tok.hasMoreTokens()) {
            	this.level = Integer.parseInt(tok.nextToken());
            	this.target = World.get().getPlayer(charName);
            } else {
                this.level = Integer.parseInt(charName);
                this.target = pc;
            }

            if (level == this.target.getLevel()) {
                return;
            }
            if (!RangeInt.includes(level, 1, ExpTable.MAX_LEVEL)) {
                pc.sendPackets(new S_SystemMessage("范围限制 1~"
                        + ExpTable.MAX_LEVEL));
                return;
            }
            this.target.setExp(ExpTable.getExpByLevel(level));

        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName());
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
