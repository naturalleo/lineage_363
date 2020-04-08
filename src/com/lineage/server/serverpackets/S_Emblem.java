package com.lineage.server.serverpackets;

import com.lineage.server.datatables.lock.ClanEmblemReading;
import com.lineage.server.templates.L1EmblemIcon;

/**
 * 角色盟徽
 * 
 * @author dexc
 * 
 */
public class S_Emblem extends ServerBasePacket {

    private byte[] _byte = null;

    public S_Emblem(final int clanid) {
        final L1EmblemIcon emblemIcon = ClanEmblemReading.get().get(clanid);
        if (emblemIcon != null) {
            this.writeC(S_OPCODE_EMBLEM);
            this.writeD(clanid);
            final byte[] icon = emblemIcon.get_clanIcon();
            for (int i = 0; i < icon.length; i++) {
                this.writeP(icon[i]);
            }
        }
    }

    public S_Emblem(final L1EmblemIcon emblemIcon) {
        // System.out.println("S_OPCODE_EMBLEM");
        this.writeC(S_OPCODE_EMBLEM);
        this.writeD(emblemIcon.get_clanid());
        this.writeByte(emblemIcon.get_clanIcon());
        /*
         * final byte[] icon = emblemIcon.get_clanIcon(); for (int i = 0 ; i <
         * icon.length ; i++) { this.writeC(icon[i]); }
         */
    }

    public S_Emblem(final int clanid, final byte[] clanIcon) {
        this.writeC(S_OPCODE_EMBLEM);
        this.writeD(clanid);
        this.writeByte(clanIcon);
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
