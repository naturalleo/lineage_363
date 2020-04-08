package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

public class S_Karma extends ServerBasePacket {
	
	private static final String S_KARMA = "[S] S_Karma";
	
    public S_Karma(final L1PcInstance pc) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(0x57);
        this.writeD(pc.getKarma());
    }

	@Override
	public byte[] getContent() {
		// TODO Auto-generated method stub
		return this.getBytes();
	}

    @Override
    public String getType() {
        return S_KARMA;
    }
}
