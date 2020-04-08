package com.lineage.server.serverpackets;

import com.lineage.server.model.L1Object;

/**
 * 物件删除
 * 
 * @author dexc
 * 
 */
public class S_RemoveObject extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物件删除
     * 
     * @param obj
     */
    public S_RemoveObject(final L1Object obj) {
        // 0000: 7c 2c b6 00 00 55 b1 ac |,...U..
        this.writeC(S_OPCODE_REMOVE_OBJECT);
        this.writeD(obj.getId());

        /*
         * this.writeC(0x55); this.writeC(0xb1); this.writeC(0xac);
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
