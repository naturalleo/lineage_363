package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1FollowerInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 物件封包 - 跟随者
 * 
 * @author dexc
 * 
 */
public class S_FollowerPack extends ServerBasePacket {

    private static final int STATUS_POISON = 0x01;
    /*
     * private static final int STATUS_INVISIBLE = 2; private static final int
     * STATUS_PC = 4; private static final int STATUS_FREEZE = 8; private static
     * final int STATUS_BRAVE = 16; private static final int STATUS_ELFBRAVE =
     * 32; private static final int STATUS_FASTMOVABLE = 64; private static
     * final int STATUS_GHOST = 128;
     */

    private byte[] _byte = null;

    /**
     * 物件封包 - 跟随者
     * 
     * @param follower
     * @param pc
     */
    public S_FollowerPack(final L1FollowerInstance follower,
            final L1PcInstance pc) {
        this.writeC(S_OPCODE_CHARPACK);
        this.writeH(follower.getX());
        this.writeH(follower.getY());
        this.writeD(follower.getId());
        this.writeH(follower.getGfxId());
        this.writeC(follower.getStatus());
        this.writeC(follower.getHeading());
        this.writeC(follower.getChaLightSize());
        this.writeC(follower.getMoveSpeed());
        this.writeD(0x00000000);
        this.writeH(0x0000);
        this.writeS(follower.getNameId());
        this.writeS(follower.getTitle());
        int status = 0;
        if (follower.getPoison() != null) { // 毒状态
            if (follower.getPoison().getEffectId() == 1) {
                status |= STATUS_POISON;
            }
        }
        this.writeC(status); // 状态
        this.writeD(0x00000000);
        this.writeS(null);
        this.writeS(null);

        this.writeC(0x00); // 物件分类

        this.writeC(0xff); // HP
        this.writeC(0x00);
        this.writeC(0x00);// LV
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
