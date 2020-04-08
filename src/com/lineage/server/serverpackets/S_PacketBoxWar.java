package com.lineage.server.serverpackets;

import java.util.HashMap;

import com.lineage.server.world.WorldClan;

/**
 * 战争讯息
 * 
 * @author daien
 * 
 */
public class S_PacketBoxWar extends ServerBasePacket {

    /**
     * writeByte(id) writeShort(?): <font color=#00800>(639) %s的攻城战开始。 </font><BR>
     * 1:Kent 2:Orc 3:WW 4:Giran 5:Heine 6:Dwarf 7:Aden 8:Diad 9:城名9 ...
     */
    public static final int MSG_WAR_BEGIN = 0;

    /**
     * writeByte(id) writeShort(?): <font color=#00800>(640) %s的攻城战结束。 </font>
     */
    public static final int MSG_WAR_END = 1;

    /**
     * writeByte(id) writeShort(?): <font color=#00800>(641) %s的攻城战正在进行中。
     * </font>
     */
    public static final int MSG_WAR_GOING = 2;

    /**
     * <font color=#00800>(642) 已掌握了城堡的主导权。 </font>
     */
    public static final int MSG_WAR_INITIATIVE = 3;

    /**
     * <font color=#00800>(643) 已占领城堡。</font>
     */
    public static final int MSG_WAR_OCCUPY = 4;

    /**
     * writeInt(?) writeString(name) writeString(clanname):<br>
     * <font color=#00800>(782) %s 血盟的 %s打败了反王<br>
     * (783) %s 血盟成为新主人。 </font>
     */
    public static final int MSG_WIN_LASTAVARD = 30;

    /** 攻城战结束讯息 */
    public static final int MSG_WAR_OVER = 79;

    private byte[] _byte = null;

    /**
     * 战争讯息<BR>
     * <font color=#00800> 攻城战结束讯息<BR>
     * </font>
     * 
     * @param value
     *            被占领城堡数量 1: 肯特城 2: 妖魔城 3: 风木城 4: 奇岩城 5: 海音城 6: 侏儒城 7: 亚丁城 8:
     *            狄亚得要塞
     * 
     * @param clanName
     *            血盟名称
     */
    public S_PacketBoxWar() {
        final HashMap<Integer, String> map = WorldClan.get().castleClanMap();
        int count = 7;// 要显示的城堡数量
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(MSG_WAR_OVER);
        this.writeC(count);
        for (int key = 1; key < count; key++) {
            String clanName = map.get(key);
            if (clanName != null) {
                this.writeS(clanName);

            } else {
                this.writeS(" ");
            }
        }
        map.clear();
    }

    /**
     * 战争讯息<BR>
     * <font color=#00800> 已掌握了城堡的主导权。<BR>
     * 已占领城堡。<BR>
     * </font>
     * 
     * @param subCode
     *            讯息编号
     */
    public S_PacketBoxWar(final int subCode) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(subCode);
    }

    /**
     * 战争讯息<BR>
     * <font color=#00800> %s的攻城战开始。<BR>
     * %s的攻城战结束。<BR>
     * %s的攻城战正在进行中。<BR>
     * </font>
     * 
     * @param subCode
     *            讯息编号
     * @param value
     *            城堡编号
     */
    public S_PacketBoxWar(final int subCode, final int value) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(subCode);

        this.writeC(value); // castle id
        this.writeH(0x0000); // ?
    }

    /**
     * 战争讯息<BR>
     * <font color=#00800> %s 血盟的 %s打败了反王<BR>
     * %s 血盟成为新主人。<BR>
     * </font>
     * 
     * @param subCode
     *            讯息编号
     * @param id
     * @param name
     *            盟主名称
     * @param clanName
     *            血盟名称
     */
    public S_PacketBoxWar(final int subCode, final int id, final String name,
            final String clanName) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(MSG_WIN_LASTAVARD);

        this.writeD(id); // クランIDか何か？
        this.writeS(name);
        this.writeS(clanName);
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
