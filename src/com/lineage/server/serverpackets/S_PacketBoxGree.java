package com.lineage.server.serverpackets;

/**
 * 画面中央颜色对话讯息
 * 
 * @author dexc
 * 
 */
public class S_PacketBoxGree extends ServerBasePacket {

    private byte[] _byte = null;

    /** 画面中央颜色对话讯息 */
    private static final int GREEN_MESSAGE = 0x54;

    /** 画面特效 */
    private static final int SECRETSTORY_GFX = 0x53;

    /**
     * \\fW=莓红紫 \\fT=橄榄绿 \\fR=大便色 \\fU=土耳其蓝 \\fY=暗粉红 \\fI=鹅黄色 \\fS=绿蓝色 \\fX=紫红色
     * \\fV=紫色
     * 
     * @param time
     */
    public S_PacketBoxGree(final String msg) {
        writeC(S_OPCODE_PACKETBOX);
        writeC(GREEN_MESSAGE);// 57
        writeC(0x02);// 44
        writeS(msg);
    }

    /**
     * 秘坛副本特殊特效(哈汀-欧林)
     * 
     * @param type
     *            01 画面粉红特效
     * @param type
     *            02 画面震动
     * @param type
     *            03 烟火特效
     */
    public S_PacketBoxGree(int type) {
        writeC(S_OPCODE_PACKETBOX);
        writeC(SECRETSTORY_GFX);// 0x53
        writeD(type);// 01画面震动 02 画面粉红特效 03 烟火特效
        writeC(0x00);
        writeC(0x00);
        writeC(0x00);
    }

    @Override
    public byte[] getContent() {
        if (_byte == null) {
            _byte = getBytes();
        }
        return _byte;
    }

    @Override
    public String getType() {
        return getClass().getSimpleName();
    }
}
