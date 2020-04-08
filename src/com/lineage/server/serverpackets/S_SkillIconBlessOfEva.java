package com.lineage.server.serverpackets;

/**
 * 魔法效果:水底呼吸
 * 
 * @author dexc
 * 
 */
public class S_SkillIconBlessOfEva extends ServerBasePacket {

    /**
     * 魔法效果:水底呼吸
     * 
     * @param objectId
     * @param time
     */
    public S_SkillIconBlessOfEva(final int objectId, final int time) {
        this.writeC(S_OPCODE_BLESSOFEVA);
        this.writeD(objectId);
        this.writeH(time);
    }

    @Override
    public byte[] getContent() {
        return this.getBytes();
    }
}
