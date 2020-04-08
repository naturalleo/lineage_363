package com.lineage.server.serverpackets;

import com.lineage.server.ActionCodes;
import com.lineage.server.model.Instance.L1DoorInstance;

/**
 * 物件封包 - 门
 * 
 * @author dexc
 * 
 */
public class S_DoorPack extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物件封包 - 门
     * 
     * @param door
     */
    public S_DoorPack(final L1DoorInstance door) {
        this.buildPacket(door);
    }

    private void buildPacket(final L1DoorInstance door) {
        this.writeC(S_OPCODE_CHARPACK);
        this.writeH(door.getX());
        this.writeH(door.getY());
        this.writeD(door.getId());
        this.writeH(door.getGfxId());
        final int doorStatus = door.getStatus();
        final int openStatus = door.getOpenStatus();

        if (door.isDead()) {
            this.writeC(doorStatus);

        } else if (openStatus == ActionCodes.ACTION_Open) {
            this.writeC(openStatus);

        } else if ((door.getMaxHp() > 1) && (doorStatus != 0)) {
            this.writeC(doorStatus);

        } else {
            this.writeC(openStatus);
        }
        this.writeC(0x00);
        this.writeC(0x00);
        this.writeC(0x00);
        this.writeD(0x00000001);
        this.writeH(0x0000);
        this.writeS(null);
        this.writeS(null);
        this.writeC(0x00); // 状态
        this.writeD(0x00000000);
        this.writeS(null);
        this.writeS(null);

        this.writeC(0x00); // 物件分类

        this.writeC(0xff); // HP
        this.writeC(0x00);
        this.writeC(0x00);
        this.writeC(0x00);
        this.writeC(0xff);
        this.writeC(0xff);
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
