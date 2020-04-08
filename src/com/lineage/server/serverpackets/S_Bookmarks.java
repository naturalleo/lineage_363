package com.lineage.server.serverpackets;

/**
 * 角色座标名单
 * 
 * @author dexc
 * 
 */
public class S_Bookmarks extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 角色座标名单
     * 
     * @param name
     * @param map
     * @param id
     */
    public S_Bookmarks(final String name, final int map, final int x,
            final int y, final int id) {
        this.buildPacket(name, map, x, y, id);
    }

    private void buildPacket(final String name, final int map, final int x,
            final int y, final int id) {
        this.writeC(S_OPCODE_BOOKMARKS);
        this.writeS(name);
        this.writeH(map);
        this.writeH(x);
        this.writeH(y);
        this.writeD(id);
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
