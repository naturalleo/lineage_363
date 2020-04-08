package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 角色状态<br>
 * 人物游戏画面人物资料视窗显示用
 * 
 * @author dexc
 * 
 */
public class S_OwnCharStatus2 extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 角色状态
     * 
     * @param pc
     */
    public S_OwnCharStatus2(final L1PcInstance pc) {
        if (pc == null) {
            return;
        }
        this.buildPacket(pc);
    }

    private void buildPacket(final L1PcInstance pc) {
        this.writeC(S_OPCODE_OWNCHARSTATUS2);
        this.writeC(pc.getStr());
        this.writeC(pc.getInt());
        this.writeC(pc.getWis());
        this.writeC(pc.getDex());
        this.writeC(pc.getCon());
        this.writeC(pc.getCha());
        this.writeC(pc.getInventory().getWeight240());
    }

    /**
     * 角色状态测试
     * 
     * @param pc
     *            测试GM
     * @param str
     *            测试力量
     */
    public S_OwnCharStatus2(final L1PcInstance pc, final int str) {
        this.writeC(S_OPCODE_OWNCHARSTATUS2);
        this.writeC(str);
        this.writeC(pc.getInt());
        this.writeC(pc.getWis());
        this.writeC(pc.getDex());
        this.writeC(pc.getCon());
        this.writeC(pc.getCha());
        this.writeC(pc.getInventory().getWeight240());
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
