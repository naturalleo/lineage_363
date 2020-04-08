package com.lineage.server.serverpackets;

import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.world.WorldQuest;

/**
 * 更新角色所在的地图
 * 
 * @author dexc
 * 
 */
public class S_MapID extends ServerBasePacket {

    /**
     * 更新角色所在的地图
     * 
     * @param pc
     *            更新角色
     * @param mapid
     *            地图编号
     * @param isUnderwater
     *            是否在水里
     */
    public S_MapID(final L1PcInstance pc, final int mapid,
            final boolean isUnderwater) {
        // 副本地图中判断
        if (QuestMapTable.get().isQuestMap(pc.getMapId())) {
            // 是副本专用地图

        } else {// 离开副本地图
            // 正在参加副本
            if (pc.get_showId() != -1) {
                // 副本编号 是执行中副本
                if (WorldQuest.get().isQuest(pc.get_showId())) {
                    // 移出副本
                    WorldQuest.get().remove(pc.get_showId(), pc);
                }
            }
            // 重置副本编号
            pc.set_showId(-1);
        }

        // 0000: 20 00 50 00 10 f8 00 00 00 00 00 00 10 0d 35 c5 .P...........5.
        this.writeC(S_OPCODE_MAPID);
        this.writeH(mapid);
        this.writeC(isUnderwater ? 0x01 : 0x00);

        /*
         * this.writeC(0x10); this.writeH(0x00f8); this.writeC(0x00);
         * this.writeC(0x00); this.writeC(0x00); this.writeC(0x00);
         * this.writeC(0x00); this.writeC(0x10); this.writeC(0x0d);
         * this.writeC(0x35); this.writeC(0xc5);
         */
    }

    /**
     * GM 移动专用
     * 
     * @param mapid
     */
    public S_MapID(final int mapid) {
        // 0000: 20 00 50 00 10 f8 00 00 00 00 00 00 10 0d 35 c5 .P...........5.
        // System.out.println("GM 移动专用 MAPID:"+mapid);
        this.writeC(S_OPCODE_MAPID);
        this.writeH(mapid);
        this.writeC(0x00);

        this.writeC(0x10);
        this.writeH(0x00f8);
        this.writeC(0x00);
        this.writeC(0x00);
        this.writeC(0x00);
        this.writeC(0x00);
        this.writeC(0x00);
        this.writeC(0x10);
        this.writeC(0x0d);
        this.writeC(0x35);
        this.writeC(0xc5);
    }

    @Override
    public byte[] getContent() {
        return this.getBytes();
    }
}
