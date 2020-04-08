package com.lineage.server.command.executor;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Location;
import com.lineage.server.model.Instance.L1IllusoryInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.L1SpawnUtil;

/**
 * 取回gm保镳
 * 
 * @author dexc
 * 
 */
public class L1ILL implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1ILL.class);

    private final Random _random = new Random();

    private L1ILL() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1ILL();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            if (pc.get_otherList().get_illusoryList().size() < 1) {
                int count = _random.nextInt(5) + 1;
                for (int i = 0; i < count; i++) {
                    final L1Location loc = pc.getLocation().randomLocation(4,
                            false);
                    L1IllusoryInstance spawnIll = L1SpawnUtil.spawn(pc, loc,
                            pc.getHeading(), 30);
                    pc.get_otherList().addIllusoryList(spawnIll.getId(),
                            spawnIll);
                    // 黑暗落雷
                    pc.sendPacketsAll(new S_EffectLocation(loc, 5524));
                }
            }

        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName());
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
