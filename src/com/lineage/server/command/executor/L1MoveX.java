package com.lineage.server.command.executor;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_MapID;
import com.lineage.server.serverpackets.S_OwnCharPack;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 测试未知地图 移动至指定座标边(参数:LOCX - LOCY - MAPID)
 * 
 * @author dexc
 * 
 */
public class L1MoveX implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1MoveX.class);

    private L1MoveX() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1MoveX();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            final StringTokenizer st = new StringTokenizer(arg);
            final int locx = Integer.parseInt(st.nextToken());
            final int locy = Integer.parseInt(st.nextToken());
            short mapid;
            if (st.hasMoreTokens()) {
                mapid = Short.parseShort(st.nextToken());

            } else {
                mapid = pc.getMapId();
            }

            _log.info("测试未知地图 移动至指定座标边(参数:LOCX - LOCY - MAPID) 执行的GM:"
                    + pc.getName());
            pc.setX(locx);
            pc.setY(locy);
            pc.setMap(mapid);
            pc.setTempID(mapid);
            pc.setHeading(5);

            pc.sendPackets(new S_MapID(pc.getTempID()));

            pc.sendPackets(new S_OwnCharPack(pc));

            pc.sendPackets(new S_CharVisualUpdate(pc));

        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName());
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
