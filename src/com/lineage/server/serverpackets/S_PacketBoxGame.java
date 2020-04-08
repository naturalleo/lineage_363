package com.lineage.server.serverpackets;

import java.util.ArrayList;

public class S_PacketBoxGame extends ServerBasePacket {

    private byte[] _byte = null;

    /** 比赛视窗(倒数开始) */
    public static final int GAMESTART = 64;

    /** 开始正向计时 */
    public static final int TIMESTART = 65;

    /** 显示资讯 */
    public static final int GAMEINFO = 66;

    /** 比赛视窗(倒数结束/停止计时) */
    public static final int GAMEOVER = 69;

    /** 移除比赛视窗 */
    public static final int GAMECLEAR = 70;

    /** 开始反向计时 */
    public static final int STARTTIME = 71;

    /** 移除开始反向计时视窗 */
    public static final int STARTTIMECLEAR = 72;

    /**
     * 开始正向计时<br>
     * 移除比赛视窗<br>
     * 移除开始反向计时视窗<br>
     * 
     * @param subCode
     */
    public S_PacketBoxGame(final int subCode) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(subCode);

        switch (subCode) {
            case TIMESTART:// 开始正向计时
            case GAMECLEAR:// 移除比赛视窗
            case STARTTIMECLEAR:// 移除开始反向计时视窗
                break;
        }
    }

    /**
     * 倒数开始0~10<br>
     * 倒数结束0~10<br>
     * 开始反向计时0~3600<br>
     * 
     * @param subCode
     * @param value
     */
    public S_PacketBoxGame(final int subCode, final int value) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(subCode);

        switch (subCode) {
            case GAMESTART:// 倒数开始
            case GAMEOVER:// 倒数结束
                this.writeC(value); // 倒数时间 0~10
                break;

            case STARTTIME:// 开始反向计时
                this.writeH(value); // 0~3600
                break;
        }
    }

    /**
     * 显示资讯
     * 
     * @param list
     */
    public S_PacketBoxGame(final ArrayList<StringBuilder> list) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(GAMEINFO);
        this.writeC(list.size());// 显示笔数
        this.writeC(0x00);//
        this.writeC(0x00);//
        this.writeC(0x00);//

        if (list != null) {// 内容与标题
            for (final StringBuilder string : list) {
                this.writeS(string.toString());
            }
        }
    }

    /**
     * 显示资讯
     * 
     * @param type
     * @param title
     * @param list
     */
    public S_PacketBoxGame(final StringBuilder title,
            final ArrayList<StringBuilder> list) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(GAMEINFO);
        this.writeC(list.size() + 1);// 显示笔数
        this.writeC(0x00);//
        this.writeC(0x00);//
        this.writeC(0x00);//

        this.writeS(title.toString());// 标题
        if (list != null) {
            for (final StringBuilder c : list) {
                this.writeS(c.toString());
            }
        }
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
