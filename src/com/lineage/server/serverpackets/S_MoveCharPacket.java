package com.lineage.server.serverpackets;

import com.lineage.server.model.L1Character;

/**
 * 物件移动
 * 
 * @author dexc
 * 
 */
public class S_MoveCharPacket extends ServerBasePacket {

    private byte[] _byte = null;

    // 反向
    private static final byte HEADING_TABLE_XR[] = { 0, -1, -1, -1, 0, 1, 1, 1 };
    private static final byte HEADING_TABLE_YR[] = { 1, 1, 0, -1, -1, -1, 0, 1 };

    /**
     * 物件移动
     * 
     * @param cha
     */
    public S_MoveCharPacket(final L1Character cha) {
        int locx = cha.getX();
        int locy = cha.getY();
        final int heading = cha.getHeading();
        locx += HEADING_TABLE_XR[heading];
        locy += HEADING_TABLE_YR[heading];

        // 0000: 3e d1 72 08 00 d3 83 e7 7e 02 80 9a 0f c3 0f b8
        // >.r.....~.......
        writeC(S_OPCODE_MOVEOBJECT);
        writeD(cha.getId());
        writeH(locx);
        writeH(locy);
        writeC(cha.getHeading());

        writeC(0x80);
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
