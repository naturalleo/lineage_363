package com.lineage.server.serverpackets;

/**
 * 血盟小屋地图(地点)
 * 
 * @author dexc
 * 
 */
public class S_HouseMap extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 血盟小屋地图(地点)
     * 
     * @param objectId
     * @param house_number
     */
    public S_HouseMap(final int objectId, final String house_number) {
        this.buildPacket(objectId, house_number);
    }

    private void buildPacket(final int objectId, final String house_number) {
        final int number = Integer.valueOf(house_number);

        this.writeC(S_OPCODE_HOUSEMAP);
        this.writeD(objectId);
        this.writeD(number);
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
