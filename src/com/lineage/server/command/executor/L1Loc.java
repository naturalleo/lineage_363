package com.lineage.server.command.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;

/**
 * 取回目前座标资料
 * 
 * @author dexc
 * 
 */
public class L1Loc implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1Loc.class);

    private L1Loc() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Loc();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            final int locx = pc.getX();
            final int locy = pc.getY();
            final short mapid = pc.getMapId();
            final int gab = pc.getMap().getOriginalTile(locx, locy);
            final int h = pc.getHeading();

            final String msg = String.format(
                    "座标 (%d, %d, %d, %d) %d %s",
                    locx,
                    locy,
                    mapid,
                    h,
                    gab,
                    (pc.getMap().isCombatZone(locx, locy) ? "战斗区域" : "")
                            + (pc.getMap().isSafetyZone(locx, locy) ? "安全区域"
                                    : "")
                            + (pc.getMap().isNormalZone(locx, locy) ? "一般区域"
                                    : ""));
            pc.sendPackets(new S_SystemMessage(msg));

        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName());
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
