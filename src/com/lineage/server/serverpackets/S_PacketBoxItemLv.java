package com.lineage.server.serverpackets;

/**
 * 
 * @author dexc
 * 
 */
public class S_PacketBoxItemLv extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * writeByte(level) writeByte(0): <font color=#00800>(672)
     * 等级%d(51~127)以上才可使用此道具。 </font><br>
     * writeByte(0) writeByte(level): <font color=#00800>(673)
     * 等级%d(0~49)以下才能使用此道具。 </font>
     */
    public static final int MSG_LEVEL_OVER = 0x0c;// 12;

    /**
     * <b><font color=red>封包分类项目 : </font><font
     * color=#008000>封包盒子(物品等级限制)</font></b>
     * 
     * @param minLv
     * @param maxLv
     */
    public S_PacketBoxItemLv(final int minLv, final int maxLv) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(MSG_LEVEL_OVER);
        this.writeC(minLv); // 0~49
        this.writeC(maxLv); // 0~49
    }

    /**
     * <b><font color=red>封包分类项目 : </font><font
     * color=#008000>封包盒子(测试物品等级限制)</font></b>
     * 
     * @param minLv
     */
    public S_PacketBoxItemLv(final int opid) {
        this.writeC(opid);
        this.writeC(MSG_LEVEL_OVER);
        this.writeC(10); // 0~49
        this.writeC(1249); // msg id
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
