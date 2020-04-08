package com.lineage.server.command.executor;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 创造金币(参数:数量)
 * 
 * @author dexc
 * 
 */
public class L1Adena implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1Adena.class);

    private L1Adena() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Adena();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            final StringTokenizer stringtokenizer = new StringTokenizer(arg);
            final long count = Long.parseLong(stringtokenizer.nextToken());

            final L1ItemInstance adena = pc.getInventory().storeItem(
                    L1ItemId.ADENA, count);
            if (adena != null) {
                // 403 获得%0%o 。
                pc.sendPackets(new S_ServerMessage(403, "$4: " + count));
            }

        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName());
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
