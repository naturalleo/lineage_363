package com.lineage.server.serverpackets;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;

/**
 * 物件攻击(NPC用 - 近距离)
 * 
 * @author dexc
 * 
 */
public class S_AttackPacketNpc extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物件攻击 - <font color="#ff0000">命中</font>(NPC用 - 近距离)
     * 
     * @param npc
     *            执行者
     * @param target
     *            目标
     * @param type
     *            动作编号
     * @param dmg
     *            伤害值
     */
    public S_AttackPacketNpc(final L1NpcInstance npc, final L1Character target,
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
        this.writeC(type);
        this.writeD(npc.getId());// 执行者OBJID
        this.writeD(target.getId());// 被攻击者OBJID

        if (dmg > 0) {
            this.writeH(0x0a); // 伤害值

        } else {
            this.writeH(0x00); // 伤害值
        }
        // this.writeH(0x00); // 伤害值

        this.writeC(npc.getHeading()); // 执行者面向

        this.writeH(0x00); // target x
        this.writeH(0x00); // target y

        this.writeC(0x00); // 0x00:none 0x04:Claw 0x08:CounterMirror
    }

    /**
     * 物件攻击 - <font color="#ff0000">命中</font>(NPC用 - 近距离)
     * 
     * @param npc
     *            执行者
     * @param target
     *            目标
     * @param type
     *            动作编号
     * @param dmg
     *            伤害值
     */
    public S_AttackPacketNpc(final L1NpcInstance npc, final int targetid,
            final int type, final int dmg) {
        this.writeC(S_OPCODE_ATTACKPACKET);
        this.writeC(type);
        this.writeD(npc.getId());// 执行者OBJID
        this.writeD(targetid);// 被攻击者OBJID

        if (dmg > 0) {
            this.writeH(0x0a); // 伤害值

        } else {
            this.writeH(0x00); // 伤害值
        }
        // this.writeH(0x00); // 伤害值

        this.writeC(npc.getHeading()); // 执行者面向

        this.writeH(0x00); // target x
        this.writeH(0x00); // target y

        this.writeC(0x00); // 0x00:none 0x04:Claw 0x08:CounterMirror
    }

    /**
     * 物件攻击 - <font color="#ff0000">未命中</font>(NPC用 - 近距离)
     * 
     * @param npc
     *            执行者
     * @param target
     *            目标
     * @param type
     *            动作编号
     */
    public S_AttackPacketNpc(final L1NpcInstance npc, final L1Character target,
            final int type) {
        this.writeC(S_OPCODE_ATTACKPACKET);
        this.writeC(type);
        this.writeD(npc.getId());// 执行者OBJID
        this.writeD(target.getId());// 被攻击者OBJID
        this.writeH(0x00); // 伤害值
        this.writeC(npc.getHeading()); // 执行者面向

        this.writeH(0x00); // target x
        this.writeH(0x00); // target y

        this.writeC(0x00); // 0x00:none 0x04:Claw 0x08:CounterMirror
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
