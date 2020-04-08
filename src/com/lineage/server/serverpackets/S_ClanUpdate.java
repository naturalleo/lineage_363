package com.lineage.server.serverpackets;

/**
 * 更新血盟数据
 * 
 * @author KZK
 * 
 */
public class S_ClanUpdate extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 更新血盟数据(加入 创立)
     * 
     * @param pc
     *            [Server] opcode = 97 0000: 61 c2 6b b1 00 a4 d1 b0 f3 31 00 00
     *            00 00 00 00 a.k......1...... 0010: 07 25 cb 44 06 5a b4 3a
     *            .%.D.Z.:
     */
    public S_ClanUpdate(final int objid, final String Clanname, final int rank) {
        writeC(S_OPCODE_UPDATECLANID);
        writeD(objid);// 角色物件
        writeS(Clanname);// 血盟名称
        writeS(null);// 角色封号 官方预设为NULL writeS内容 新成员入盟会直接给予封号
        writeD(0x00000000);// 用途不明
        writeC(rank);// 角色阶级

    }

    /**
     * 更新血盟数据(驱逐退出解散血盟)
     * 
     * @param objid
     */
    public S_ClanUpdate(final int objid) {
        writeC(S_OPCODE_UPDATECLANID);
        writeD(objid);// 角色物件
        writeS(null);// 血盟名称
        writeS(null);// 角色封号 官方预设为NULL
        writeD(0x00000000);// 用途不明
        writeC(0);// 角色阶级
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
