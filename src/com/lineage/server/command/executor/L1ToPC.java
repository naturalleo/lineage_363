package com.lineage.server.command.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxGm;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

/**
 * 移动座标至指定人物身边(参数:人物名称/选单)
 * 
 * @author dexc
 * 
 */
public class L1ToPC implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1ToPC.class);

    private L1ToPC() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1ToPC();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            final L1PcInstance target = World.get().getPlayer(arg);

            if (target != null) {
                L1Teleport.teleport(pc, target.getX(), target.getY(),
                        target.getMapId(), 5, false);
                // 设置副本编号
                pc.set_showId(target.get_showId());
                pc.sendPackets(new S_ServerMessage(166, "移动座标至指定人物身边: " + arg));

            } else {
                final int mode = 1;
                pc.sendPackets(new S_PacketBoxGm(pc, mode));
            }

        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName());
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
