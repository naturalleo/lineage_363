package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * NPC 一般频道
 * 
 * @author dexc
 * 
 */
public class S_NpcChat extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * NPC 一般频道
     * 
     * @param npc
     *            NPC
     * @param chat
     *            对话内容
     */
    public S_NpcChat(final L1NpcInstance npc, final String chat) {
        this.writeC(S_OPCODE_NPCSHOUT);
        // desc-?.tbl
        this.writeC(0x00); // Color
        this.writeD(npc.getId());
        this.writeS(npc.getNameId() + ": " + chat);
    }

    /**
     * NPC 一般频道
     * 
     * @param npc
     *            NPC
     * @param chat
     *            对话内容
     * @param name
     *            是否显示名称 true:是 false:不是
     */
    public S_NpcChat(final L1NpcInstance npc, final String chat, boolean name) {
        this.writeC(S_OPCODE_NPCSHOUT);
        // desc-?.tbl
        this.writeC(0x00); // Color
        this.writeD(npc.getId());
        this.writeS((name ? npc.getNameId() + ": " : "") + chat);
    }

    /**
     * PC对话输出(使用NPC频道)
     * 
     * @param pc
     * @param chat
     */
    public S_NpcChat(final L1PcInstance pc, final String chat) {
        this.writeC(S_OPCODE_NPCSHOUT);
        // desc-?.tbl
        this.writeC(0x00); // Color
        this.writeD(pc.getId());
        this.writeS(pc.getName() + ": " + chat);
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
