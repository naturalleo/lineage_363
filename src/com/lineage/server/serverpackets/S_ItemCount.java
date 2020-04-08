package com.lineage.server.serverpackets;

/**
 * 选取物品数量 (NPC道具交换)
 * 
 * @author dexc
 * 
 */
public class S_ItemCount extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 选取物品数量 (NPC道具交换)
     * 
     * @param objId
     *            NPC OBJID
     * @param max
     *            可换数量上限
     * @param cmd
     *            命令
     */
    public S_ItemCount(final int objId, final int max, final String cmd) {
        this.writeC(S_OPCODE_INPUTAMOUNT);
        this.writeD(objId);
        this.writeD(0x00000000);// ?
        this.writeD(0x00000000);// 数量初始质
        this.writeD(0x00000000);// 最低可换数量
        this.writeD(max);// 最高可换数量
        this.writeH(0x0000);// c
        this.writeS("request");// HTML
        this.writeS(cmd);// 命令
    }

    /**
     * 选取物品数量 (NPC道具交换)
     * 
     * @param objId
     *            NPC OBJID
     * @param max
     *            可换数量上限
     * @param html
     *            页面
     * @param cmd
     *            命令
     */
    public S_ItemCount(final int objId, final int max, final String html,
            final String cmd) {
        this.writeC(S_OPCODE_INPUTAMOUNT);
        this.writeD(objId);
        this.writeD(0x00000000);// ?
        this.writeD(0x00000000);// 数量初始质
        this.writeD(0x00000000);// 最低可换数量
        this.writeD(max);// 最高可换数量
        this.writeH(0x0000);// c
        this.writeS(html);// HTML
        this.writeS(cmd);// 命令
    }

    public S_ItemCount(final int objId, final int min, final int max,
            final String html, final String cmd, final String[] data) {
        this.writeC(S_OPCODE_INPUTAMOUNT);
        this.writeD(objId);
        this.writeD(0x00000000);// ?
        this.writeD(min);// 数量初始质
        this.writeD(min);// 最低可换数量
        this.writeD(max);// 最高可换数量
        this.writeH(0x0000);// c
        this.writeS(html);// HTML
        this.writeS(cmd);// 命令
        if ((data != null) && (1 <= data.length)) {
            this.writeH(data.length); // 数量
            for (final String datum : data) {
                this.writeS(datum);
            }
        }
    }

    /**
     * 选取物品数量 (银行管理员)
     * 
     * @param objId
     *            NPC OBJID
     * @param max
     *            可换数量最小质
     * @param max
     *            可换数量最大质
     * @param html
     *            页面
     * @param cmd
     *            命令
     */
    public S_ItemCount(final int objId, final int min, final int max,
            final String html, final String cmd) {
        this.writeC(S_OPCODE_INPUTAMOUNT);
        this.writeD(objId);
        this.writeD(0x00000000);// ?
        this.writeD(min);// 数量初始质
        this.writeD(min);// 最低可换数量
        this.writeD(max);// 最高可换数量
        this.writeH(0x0000);// c
        this.writeS(html);// HTML
        this.writeS(cmd);// 命令
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
