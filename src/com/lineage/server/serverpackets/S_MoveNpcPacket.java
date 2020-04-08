package com.lineage.server.serverpackets;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1MonsterInstance;

/**
 * 物件移动
 * 
 * @author dexc
 * 
 */
public class S_MoveNpcPacket extends ServerBasePacket {

    private byte[] _byte = null;

    /***
     * 物件移动
     * 
     * @param npc
     * @param x
     * @param y
     * @param heading
     */
    public S_MoveNpcPacket(final L1MonsterInstance npc, final int x,
            final int y, final int heading) {
        // 0000: 3e d1 72 08 00 d3 83 e7 7e 02 80 9a 0f c3 0f b8
        // >.r.....~.......
        this.writeC(S_OPCODE_MOVEOBJECT);
        this.writeD(npc.getId());
        this.writeH(x);
        this.writeH(y);
        this.writeC(heading);

        this.writeC(0x80);
    }

    /***
     * 物件移动
     * 
     * @param cha
     */
    public S_MoveNpcPacket(final L1Character cha) {
        // 0000: 3e d1 72 08 00 d3 83 e7 7e 02 80 9a 0f c3 0f b8
        // >.r.....~.......
        this.writeC(S_OPCODE_MOVEOBJECT);
        this.writeD(cha.getId());
        this.writeH(cha.getX());
        this.writeH(cha.getY());
        this.writeC(cha.getHeading());

        this.writeC(0x80);
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
