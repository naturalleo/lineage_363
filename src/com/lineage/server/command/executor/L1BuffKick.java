package com.lineage.server.command.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.lock.CharBuffReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxGm;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 删除已存人物保留技能(参数:人物objid/选单)
 * 
 * @author dexc
 * 
 */
public class L1BuffKick implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1BuffKick.class);

    private L1BuffKick() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1BuffKick();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            try {
                final int objid = Integer.parseInt(arg);

                CharBuffReading.get().deleteBuff(objid);
                pc.sendPackets(new S_ServerMessage(166, objid + " Buff清除!"));

            } catch (final Exception e) {
                final int mode = 0;
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
