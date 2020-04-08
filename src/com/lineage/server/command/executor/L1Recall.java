package com.lineage.server.command.executor;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxGm;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;

/**
 * 召唤玩家(参数:ALL(全部玩家)/人物名称/选单)
 * 
 * @author dexc
 * 
 */
public class L1Recall implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1Recall.class);

    private L1Recall() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Recall();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            Collection<L1PcInstance> targets = null;
            if (arg.equalsIgnoreCase("all")) {
                targets = World.get().getAllPlayers();

            } else {
                targets = new ArrayList<L1PcInstance>();
                final L1PcInstance tg = World.get().getPlayer(arg);
                if (tg == null) {
                    final int mode = 2;
                    pc.sendPackets(new S_PacketBoxGm(pc, mode));
                    return;
                }
                targets.add(tg);
            }

            for (final L1PcInstance target : targets) {
                if (target.isGm()) {
                    continue;
                }
                L1Teleport.teleportToTargetFront(target, pc, 2);
                target.sendPackets(new S_SystemMessage("管理者召唤。"));
            }

        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName());
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
