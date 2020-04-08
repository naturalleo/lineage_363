package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

public class S_HireSoldier extends ServerBasePacket {

    private byte[] _byte = null;

    // HTMLを开いているときにこのパケットを送るとnpcdeloy-j.htmlが表示される
    // OKボタンを押すとC_127が飞ぶ
    public S_HireSoldier(final L1PcInstance pc) {
        this.writeC(S_OPCODE_HIRESOLDIER);
        this.writeH(0x0000); // ? クライアントが返すパケットに含まれる
        this.writeH(0x0000); // ? クライアントが返すパケットに含まれる
        this.writeH(0x0000); // 雇用された佣兵の总数
        this.writeS(pc.getName());
        this.writeD(0x00000000); // ? クライアントが返すパケットに含まれる
        this.writeH(0x0000); // 配置可能な佣兵数
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
