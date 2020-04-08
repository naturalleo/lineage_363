package com.lineage.server.serverpackets;

import com.lineage.server.model.L1Character;

/**
 * 物件攻击(远程-物理攻击 PC/NPC共用)
 * 
 * @author dexc
 * 
 */
public class S_UseArrowSkill extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物件攻击 - <font color="#ff0000">命中</font>(远程-物理攻击 PC/NPC共用)
     * 
     * @param cha
     *            执行者
     * @param targetobj
     *            目标OBJID
     * @param spellgfx
     *            远程动画编号
     * @param x
     *            目标X
     * @param y
     *            目标Y
     * @param dmg
     *            伤害力
     */
    public S_UseArrowSkill(final L1Character cha, final int targetobj,
            final int spellgfx, final int x, final int y, final int dmg) {

        int aid = 1;
        // 外型编号改变动作
        switch (cha.getTempCharGfx()) {
            case 3860:// 妖魔弓箭手
                aid = 21;
                break;

            case 2716:// 古代亡灵
                aid = 19;
                break;
        }

        /*
         * 0000: 5e 01 8e 24 bb 01 a4 6c 00 00 0a 00 05 52 01 00
         * ^..$...l.....R.. 0010: 00 42 00 00 c3 83 e1 7e c1 83 e5 7e 00 00 00
         * 85 .B.....~...~....
         * 
         * 0000: 5e 01 8e 24 bb 01 a4 6c 00 00 0d 00 05 52 01 00
         * ^..$...l.....R.. 0010: 00 42 00 00 c3 83 e1 7e c1 83 e5 7e 00 00 00
         * ee .B.....~...~....
         * 
         * 0000: 5e 01 8e 24 bb 01 3c 20 00 00 0b 00 05 52 01 00 ^..$..<
         * .....R.. 0010: 00 42 00 00 c3 83 e1 7e c0 83 e5 7e 00 00 00 58
         * .B.....~...~...X
         */
        this.writeC(S_OPCODE_ATTACKPACKET);
        this.writeC(aid);// 动作代号
        this.writeD(cha.getId());// 执行者OBJID
        this.writeD(targetobj);// 目标OBJID

        if (dmg > 0) {
            this.writeH(0x0a); // 伤害值

        } else {
            this.writeH(0x00); // 伤害值
        }

        this.writeC(cha.getHeading());// 新面向

        // 以原子方式将当前值加 1。
        this.writeD(0x00000152);

        this.writeH(spellgfx);// 远程动画编号
        this.writeC(0x7f);// ??
        this.writeH(cha.getX());// 执行者X点
        this.writeH(cha.getY());// 执行者Y点
        this.writeH(x);// 目标X点
        this.writeH(y);// 目标Y点

        this.writeD(0x00000000);
        this.writeC(0x00);
        // this.writeC(0x00);
        // this.writeC(0x00);
        // this.writeC(0x00);
    }

    /**
     * 物件攻击 - <font color="#ff0000">未命中</font>(远程-物理攻击 PC/NPC共用) 空攻击使用
     * 
     * @param cha
     *            执行者
     * @param spellgfx
     *            远程动画编号
     * @param x
     *            目标X
     * @param y
     *            目标Y
     */
    public S_UseArrowSkill(final L1Character cha, final int spellgfx,
            final int x, final int y) {

        int aid = 1;
        // 外型编号改变动作
        if (cha.getTempCharGfx() == 3860) {
            aid = 21;
        }
        this.writeC(S_OPCODE_ATTACKPACKET);
        this.writeC(aid);// 动作代号
        this.writeD(cha.getId());// 执行者OBJID
        this.writeD(0x00);// 目标OBJID
        this.writeH(0x00);// 伤害力
        this.writeC(cha.getHeading());// 新面向

        // 以原子方式将当前值加 1。
        this.writeD(0x00000152);

        this.writeH(spellgfx);// 远程动画编号
        this.writeC(0x7f);// ??
        this.writeH(cha.getX());// 执行者X点
        this.writeH(cha.getY());// 执行者Y点
        this.writeH(x);// 目标X点
        this.writeH(y);// 目标Y点

        this.writeD(0x00000000);
        this.writeC(0x00);
        // this.writeC(0x00);
        // this.writeC(0x00);
        // this.writeC(0x00);
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
