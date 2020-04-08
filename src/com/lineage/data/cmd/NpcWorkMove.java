package com.lineage.data.cmd;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.serverpackets.S_MoveCharPacket;
import com.lineage.server.types.Point;

/**
 * NPC 工作判断类
 * 
 * @author dexc
 * 
 */
public class NpcWorkMove {

    private static final Log _log = LogFactory.getLog(NpcWorkMove.class);

    private static final byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };

    private static final byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

    private L1NpcInstance _npc;

    public NpcWorkMove(final L1NpcInstance npc) {
        _npc = npc;
    }

    /**
     * 往指定作标点位置方向移动一格
     * 
     * @param point
     * @return
     */
    public boolean actionStart(final Point point) {
        final int x = point.getX();
        final int y = point.getY();
        try {
            // 取回行进方向
            final int dir = _npc.targetDirection(x, y);
            setDirectionMove(dir);
            if (_npc.getLocation().getTileLineDistance(point) == 0) {
                return false;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return true;
    }

    /**
     * 往指定面向移动1格(无障碍设置)
     */
    private void setDirectionMove(final int heading) {
        // System.out.println("往指定面向移动1格: "+_npc.getNpcId() + " 面向:" + heading);
        int locx = _npc.getX();
        int locy = _npc.getY();
        locx += HEADING_TABLE_X[heading];
        locy += HEADING_TABLE_Y[heading];
        _npc.setHeading(heading);

        _npc.setX(locx);
        _npc.setY(locy);
        _npc.broadcastPacketAll(new S_MoveCharPacket(_npc));
    }
}
