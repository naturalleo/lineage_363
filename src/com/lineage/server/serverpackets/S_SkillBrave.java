package com.lineage.server.serverpackets;

/**
 * 魔法效果:勇敢药水颣
 * 
 * @author dexc
 * 
 */
public class S_SkillBrave extends ServerBasePacket {

    /**
     * 魔法效果:勇敢药水颣
     * 
     * @param objid
     * @param mode
     * <br>
     *            0:你的情绪回复到正常。(解除 )<br>
     *            1:从身体的深处感到热血沸腾。(第一阶段勇水)<br>
     *            3:身体内深刻的感觉到充满了森林的活力。(精灵饼干)<br>
     *            4:风之疾走 / 神圣疾走 / 行走加速 / 生命之树果实效果<br>
     *            5:从身体的深处感到热血沸腾。(第二阶段勇水)<br>
     *            6:引发龙之血暴发出来了。<br>
     * @param time
     */
    public S_SkillBrave(final int objid, final int mode, final int time) {
        // 0000: 4e 91 46 a9 01 04 c0 03 N.F.....
        this.writeC(S_OPCODE_SKILLBRAVE);
        this.writeD(objid);
        this.writeC(mode);
        this.writeH(time);
    }

    /**
     * TEST
     * 
     * @param objid
     * @param opid
     */
    public S_SkillBrave(final int objid, final int mode) {
        // 0000: 4e 91 46 a9 01 04 c0 03 N.F.....
        this.writeC(S_OPCODE_SKILLBRAVE);
        this.writeD(objid);
        this.writeC(mode);
        this.writeH(300);
    }

    @Override
    public byte[] getContent() {
        return this.getBytes();
    }
}
