package com.lineage.server.serverpackets;

/**
 * 讯息通知
 * 
 * @author dexc
 * 
 */
public class S_HelpMessage extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 强化成功成功讯息
     * 
     * @param mode
     */
    public S_HelpMessage(final String name, final String info) {
        this.writeC(S_OPCODE_NPCSHOUT);
        this.writeC(0x00);// 一般频道
        this.writeD(0x00000000);
        this.writeS(name + " --> \\f4" + info);
    }

    /**
     * 讯息通知(使用NPC对话一般频道)
     * 
     * @param mode
     * @param color
     * <br>
     *            <font color="#bdaaa5">\\fR <b>颜色范例</b></font><br>
     *            <font color="#739e84">\\fS <b>颜色范例</b></font><br>
     *            <font color="#7b9e7b">\\fT <b>颜色范例</b></font><br>
     *            <font color="#7b9aad">\\fU <b>颜色范例</b></font><br>
     *            <font color="#a59ac6">\\fV <b>颜色范例</b></font><br>
     *            <font color="#ad92b5">\\fW <b>颜色范例</b></font><br>
     *            <font color="#b592ad">\\fX <b>颜色范例</b></font><br>
     *            <font color="#bd9a94">\\fY <b>颜色范例</b></font><br>
     */
    public S_HelpMessage(final String string) {
        this.writeC(S_OPCODE_NPCSHOUT);
        this.writeC(0x00);// 一般频道
        this.writeD(0x00000000);
        this.writeS(string);
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
