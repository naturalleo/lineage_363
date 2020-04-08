package com.lineage.server.serverpackets;

import com.lineage.server.ActionCodes;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 物件攻击(PC 用)
 * 
 * @author dexc
 * 
 */
public class S_AttackPacketPc extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物件攻击 - <font color="#ff0000">命中</font>(PC 用 - 近距离)
     * 
     * @param pc
     *            执行者
     * @param target
     *            目标
     * @param type
     *            0x00:none 0x02:暴击 0x04:双击 0x08:镜反射
     * @param dmg
     *            伤害力
     */
    public S_AttackPacketPc(final L1PcInstance pc, final L1Character target,
            final int type, final int dmg) {
        /*
         * 0000: 5e 01 be ac bf 01 a4 6c 00 00 01 00 04 00 00 00
         * ^......l........ 0010: 00 00 44 00 01 00 aa 30 ..D....0
         * 
         * 0000: 5e 01 be ac bf 01 a4 6c 00 00 00 00 04 00 00 00
         * ^......l........ 0010: 00 00 39 38 00 00 40 97 ..98..@.
         * 
         * 0000: 5e 01 be ac bf 01 3c 20 00 00 01 00 05 00 00 00 ^.....<
         * ........ 0010: 00 00 f7 00 35 34 91 ba ....54..
         */
        this.writeC(S_OPCODE_ATTACKPACKET);
        this.writeC(ActionCodes.ACTION_Attack);// ACTION_AltAttack
        this.writeD(pc.getId());
        this.writeD(target.getId());

        if (dmg > 0) {
            this.writeH(0x0a); // 伤害值

        } else {
            this.writeH(0x00); // 伤害值
        }

        this.writeC(pc.getHeading());

        this.writeH(0x00); // target x
        this.writeH(0x00); // target y
        this.writeC(type); // 0x00:none 0x02:暴击 0x04:双击 0x08:镜反射
    }

    /**
     * 物件攻击 - <font color="#ff0000">未命中</font>(PC 用 - 近距离)
     * 
     * @param pc
     * @param target
     */
    public S_AttackPacketPc(final L1PcInstance pc, final L1Character target) {
        this.writeC(S_OPCODE_ATTACKPACKET);
        this.writeC(ActionCodes.ACTION_Attack);// ACTION_AltAttack
        this.writeD(pc.getId());
        this.writeD(target.getId());
        this.writeH(0x00); // damage
        this.writeC(pc.getHeading());

        this.writeH(0x00); // target x
        this.writeH(0x00); // target y
        this.writeC(0x00); // 0x00:none 0x02:暴击 0x04:双击 0x08:镜反射
    }

    /**
     * 物件攻击 - <font color="#ff0000">空击</font>(PC 用 - 近距离)
     * 
     * @param pc
     */
    public S_AttackPacketPc(final L1PcInstance pc) {
        this.writeC(S_OPCODE_ATTACKPACKET);
        this.writeC(ActionCodes.ACTION_Attack);// ACTION_AltAttack
        this.writeD(pc.getId());
        this.writeD(0x00);
        this.writeH(0x00); // damage
        this.writeC(pc.getHeading());

        this.writeH(0x00); // target x
        this.writeH(0x00); // target y
        this.writeC(0x00); // 0x00:none 0x02:暴击 0x04:双击 0x08:镜反射
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
