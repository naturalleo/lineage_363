package com.lineage.server.serverpackets;

import com.lineage.server.clientpackets.C_CreateChar;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 人物属性资讯
 * 
 * @author daien
 * 
 */
public class S_CharResetInfo extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 人物属性资讯
     */
    public S_CharResetInfo(final L1PcInstance pc) {
        final int baseStr = C_CreateChar.ORIGINAL_STR[pc.getType()];
        final int baseDex = C_CreateChar.ORIGINAL_DEX[pc.getType()];
        final int baseCon = C_CreateChar.ORIGINAL_CON[pc.getType()];
        final int baseWis = C_CreateChar.ORIGINAL_WIS[pc.getType()];
        final int baseCha = C_CreateChar.ORIGINAL_CHA[pc.getType()];
        final int baseInt = C_CreateChar.ORIGINAL_INT[pc.getType()];

        final int originalStr = pc.getOriginalStr();
        final int originalDex = pc.getOriginalDex();
        final int originalCon = pc.getOriginalCon();
        final int originalWis = pc.getOriginalWis();
        final int originalCha = pc.getOriginalCha();
        final int originalInt = pc.getOriginalInt();

        final int upStr = originalStr - baseStr;
        final int upDex = originalDex - baseDex;
        final int upCon = originalCon - baseCon;
        final int upWis = originalWis - baseWis;
        final int upCha = originalCha - baseCha;
        final int upInt = originalInt - baseInt;

        this.writeC(S_OPCODE_CHARRESET);
        this.writeC(0x04);
        this.writeC((upInt << 4) + upStr);
        this.writeC((upDex << 4) + upWis);
        this.writeC((upCha << 4) + upCon);
        this.writeC(0x00);
        this.writeH(0x00);

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
