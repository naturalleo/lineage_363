package com.lineage.server.serverpackets;

/**
 * 血盟战争讯息(编号,血盟名称,目标血盟名称)
 * 
 * @author dexc
 * 
 */
public class S_War extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 血盟战争讯息(编号,血盟名称,目标血盟名称)
     * 
     * @param type
     *            编号 <BR>
     *            1 : 226：%0 血盟向 %1血盟宣战。 <BR>
     *            2 : %0 血盟向 %1 血盟投降了。<BR>
     *            3 : %0 血盟与 %1血盟之间的战争结束了。<BR>
     *            4 : 231：%0 血盟赢了对 %1 血盟的战争。 <BR>
     *            6 : 224：%0 血盟与 %1血盟成为同盟关系。<BR>
     *            7 : 毁掉%0 血盟与 %1血盟之间的同盟。<BR>
     *            8 : \f1目前你的血盟与 %0 血盟交战当中。<BR>
     * @param clan_name1
     *            血盟名称
     * @param clan_name2
     *            目标血盟名称
     */
    public S_War(final int type, final String clan_name1,
            final String clan_name2) {
        this.buildPacket(type, clan_name1, clan_name2);
    }

    private void buildPacket(final int type, final String clan_name1,
            final String clan_name2) {
        // 1 : 226：%0 血盟向 %1血盟宣战。
        // 2 : %0 血盟向 %1 血盟投降了。
        // 3 : %0 血盟与 %1血盟之间的战争结束了。
        // 4 : 231：%0 血盟赢了对 %1 血盟的战争。
        // 6 : 224：%0 血盟与 %1血盟成为同盟关系。
        // 7 : 毁掉%0 血盟与 %1血盟之间的同盟。
        // 8 : \f1目前你的血盟与 %0 血盟交战当中。

        this.writeC(S_OPCODE_WAR);
        this.writeC(type);
        this.writeS(clan_name1);
        this.writeS(clan_name2);
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
