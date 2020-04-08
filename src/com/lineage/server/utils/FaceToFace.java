package com.lineage.server.utils;

import java.util.List;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

/**
 * 面向物件判断
 * 
 * @author DaiEn
 * 
 */
public class FaceToFace {

    public static L1PcInstance faceToFace(final L1PcInstance pc) {
        final int pcX = pc.getX();
        final int pcY = pc.getY();
        final int pcHeading = pc.getHeading();
        final List<L1PcInstance> players = World.get().getVisiblePlayer(pc, 1);

        if (players.size() == 0) { // 1格内无物件(PC)
            // 93 \f1你注视的地方没有人。
            pc.sendPackets(new S_ServerMessage(93));
            return null;
        }

        for (final L1PcInstance target : players) {
            final int targetX = target.getX();
            final int targetY = target.getY();
            final int targetHeading = target.getHeading();
            if ((pcHeading == 0) && (pcX == targetX) && (pcY == (targetY + 1))) {
                if (targetHeading == 4) {
                    return target;
                } else {
                    // 91 \f1%0%s 没有面对看你。
                    pc.sendPackets(new S_ServerMessage(91, target.getName()));
                    return null;
                }

            } else if ((pcHeading == 1) && (pcX == (targetX - 1))
                    && (pcY == (targetY + 1))) {
                if (targetHeading == 5) {
                    return target;
                } else {
                    // 91 \f1%0%s 没有面对看你。
                    pc.sendPackets(new S_ServerMessage(91, target.getName()));
                    return null;
                }

            } else if ((pcHeading == 2) && (pcX == (targetX - 1))
                    && (pcY == targetY)) {
                if (targetHeading == 6) {
                    return target;
                } else {
                    // 91 \f1%0%s 没有面对看你。
                    pc.sendPackets(new S_ServerMessage(91, target.getName()));
                    return null;
                }

            } else if ((pcHeading == 3) && (pcX == (targetX - 1))
                    && (pcY == (targetY - 1))) {
                if (targetHeading == 7) {
                    return target;
                } else {
                    // 91 \f1%0%s 没有面对看你。
                    pc.sendPackets(new S_ServerMessage(91, target.getName()));
                    return null;
                }

            } else if ((pcHeading == 4) && (pcX == targetX)
                    && (pcY == (targetY - 1))) {
                if (targetHeading == 0) {
                    return target;
                } else {
                    // 91 \f1%0%s 没有面对看你。
                    pc.sendPackets(new S_ServerMessage(91, target.getName()));
                    return null;
                }

            } else if ((pcHeading == 5) && (pcX == (targetX + 1))
                    && (pcY == (targetY - 1))) {
                if (targetHeading == 1) {
                    return target;
                } else {
                    // 91 \f1%0%s 没有面对看你。
                    pc.sendPackets(new S_ServerMessage(91, target.getName()));
                    return null;
                }

            } else if ((pcHeading == 6) && (pcX == (targetX + 1))
                    && (pcY == targetY)) {
                if (targetHeading == 2) {
                    return target;
                } else {
                    // 91 \f1%0%s 没有面对看你。
                    pc.sendPackets(new S_ServerMessage(91, target.getName()));
                    return null;
                }

            } else if ((pcHeading == 7) && (pcX == (targetX + 1))
                    && (pcY == (targetY + 1))) {
                if (targetHeading == 3) {
                    return target;
                } else {
                    // 91 \f1%0%s 没有面对看你。
                    pc.sendPackets(new S_ServerMessage(91, target.getName()));
                    return null;
                }
            }
        }
        // \f1你注视的地方没有人。
        pc.sendPackets(new S_ServerMessage(93));
        return null;
    }
}
