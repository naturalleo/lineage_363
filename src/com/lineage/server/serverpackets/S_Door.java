package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1DoorInstance;

/**
 * 物件属性(门)
 * 
 * @author dexc
 * 
 */
public class S_Door extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物件属性(门)
     * 
     * @param door
     */
    public S_Door(final L1DoorInstance door) {
        this.buildPacket(door.getEntranceX(), door.getEntranceY(),
                door.getDirection(), door.getPassable());
    }

    /**
     * 物件属性(门)
     * 
     * @param x
     * @param y
     * @param direction
     * @param passable
     */
    public S_Door(final int x, final int y, final int direction,
            final int passable) {
        this.buildPacket(x, y, direction, passable);
    }

    private void buildPacket(final int x, final int y, final int direction,
            final int passable) {
        this.writeC(S_OPCODE_ATTRIBUTE);
        this.writeH(x);
        this.writeH(y);
        this.writeC(direction); // ドアの方向 0: ／ 1: ＼
        this.writeC(passable);
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
