package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.Teleportation;

/**
 * 座标异常重整
 * 
 * @author dexc
 * 
 */
public class S_Lock extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 座标异常重整
     * 
     * @param type
     * @param equipped
     */
    public S_Lock() {
        this.buildPacket(-1);
    }

    public S_Lock(int i) {
        this.buildPacket(i);
    }
    
    public S_Lock(final L1PcInstance pc) {
        pc.sendPackets(new S_Paralysis(
                S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
        pc.setTeleportX(pc.getOleLocX());
        pc.setTeleportY(pc.getOleLocY());
        pc.setTeleportMapId(pc.getMapId());
        pc.setTeleportHeading(pc.getHeading());
        // 传送回原始座标
        System.out.println("===传送回原始座标sa===");
        Teleportation.teleportation(pc);
    }

    private void buildPacket(int i) {
        if (i >= 0) {
            this.writeC(i);
        } else {
        	System.out.println("===传送回原始座标s===");
            this.writeC(S_OPCODE_CHARLOCK);
        }
        this.writeC(0x00);
        /*
         * this.writeC(0xf1); this.writeC(0x2d); this.writeC(0x7d);
         * this.writeC(0x02); this.writeC(0xf9);
         */
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
