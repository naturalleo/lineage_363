/**
 *                            License
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS  
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). 
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW.  
 * ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS LICENSE OR  
 * COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND  
 * AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE  
 * MAY BE CONSIDERED TO BE A CONTRACT,
 * THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED 
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */
package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 地图剩余使用时间[退出游戏界面].
 * 
 * @author kzk
 */
public final class S_PacketBoxMapTimerOut extends ServerBasePacket {
    private byte[] _byte = null;

    /**
     * 地图剩余使用时间[退出游戏界面].
     */
    public S_PacketBoxMapTimerOut(L1PcInstance pc) {
        writeC(S_OPCODE_PACKETBOX);
        writeC(S_PacketBox.WINDOW_MAP_TIME);
        writeD(3);
        writeD(1);
        writeS("$12125"); // 奇岩监狱
        final int maxTime = 10800; // 3小时
        int time = (maxTime - pc.getRocksPrisonTime()) / 60;
        if (time <= 0) {
            writeD(0);
        } else {
            writeD(time);
        }
        writeD(2);
        writeS("$6081"); // 象牙塔
        writeD(60);
        writeD(3);
        writeS("$12126"); // 拉斯塔巴德地监
        writeD(720);
        // TODO 时间待实装
    }

    @Override
    public byte[] getContent() {
        if (this._byte == null) {
            this._byte = this.getBytes();
        }
        return this._byte;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
