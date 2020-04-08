package com.lineage.server.serverpackets;

import static com.lineage.server.model.skill.L1SkillId.SHAPE_CHANGE;

import java.util.concurrent.atomic.AtomicInteger;

import com.lineage.server.ActionCodes;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 物件攻击(技能使用)
 * 
 * @author dexc
 * 
 */
public class S_UseAttackSkill extends ServerBasePacket {

    private static AtomicInteger _sequentialNumber = new AtomicInteger(4500000);

    private byte[] _byte = null;

    /**
     * 物件攻击(武器 技能使用-不需动作代号-不送出伤害)
     * 
     * @param cha
     *            执行者
     * @param targetobj
     *            目标OBJID
     * @param spellgfx
     *            远程动画编号
     * @param x
     *            X点
     * @param y
     *            Y点
     * @param actionId
     *            动作代号
     * @param motion
     *            具有执行者
     */
    public S_UseAttackSkill(final L1Character cha, final int targetobj,
            final int spellgfx, final int x, final int y, final int actionId,
            final boolean motion) {
        this.buildPacket(cha, targetobj, spellgfx, x, y, actionId, 0, motion);
    }

    /**
     * 物件攻击(NPC / PC 技能使用)
     * 
     * @param cha
     *            执行者
     * @param targetobj
     *            目标OBJID
     * @param spellgfx
     *            远程动画编号
     * @param x
     *            X点
     * @param y
     *            Y点
     * @param actionId
     *            动作代号
     * @param dmg
     *            伤害力
     */
    public S_UseAttackSkill(final L1Character cha, final int targetobj,
            final int spellgfx, final int x, final int y, final int actionId,
            final int dmg) {
        this.buildPacket(cha, targetobj, spellgfx, x, y, 18, dmg, true);
    }

    /**
     * 物件攻击(技能使用 - PC/NPC共用)
     * 
     * @param cha
     *            执行者
     * @param targetobj
     *            目标OBJID
     * @param spellgfx
     *            远程动画编号
     * @param x
     *            X点
     * @param y
     *            Y点
     * @param actionId
     *            动作代号
     * @param dmg
     *            伤害力
     * @param withCastMotion
     *            具有执行者
     */
    private void buildPacket(final L1Character cha, final int targetobj,
            final int spellgfx, final int x, final int y, int actionId,
            final int dmg, final boolean withCastMotion) {
        if (cha instanceof L1PcInstance) {
            // 变身中变动作代号异动
            if (cha.hasSkillEffect(SHAPE_CHANGE)
                    && (actionId == ActionCodes.ACTION_SkillAttack)) {

                final int tempchargfx = cha.getTempCharGfx();
                if ((tempchargfx == 5727) || (tempchargfx == 5730)) {
                    // 物件具有变身 改变动作代号
                    actionId = ActionCodes.ACTION_SkillBuff;

                } else if ((tempchargfx == 5733) || (tempchargfx == 5736)) {
                    // 物件具有变身 改变动作代号
                    actionId = ActionCodes.ACTION_Attack;
                }
            }
        }
        // 火灵之主动作代号强制变更
        if (cha.getTempCharGfx() == 4013) {
            actionId = ActionCodes.ACTION_Attack;
        }

        // 设置新面向
        final int newheading = calcheading(cha.getX(), cha.getY(), x, y);
        cha.setHeading(newheading);
        /*
         * 0000: 5e 12 1a cc bd 01 a4 6c 00 00 04 00 05 a3 d2 bd
         * ^......l........ 0010: 01 a7 00 06 c3 83 e1 7e c1 83 e5 7e 00 00 00
         * af .......~...~....
         * 
         * 0000: 5e 12 1a cc bd 01 a4 6c 00 00 07 00 05 ff d6 bd
         * ^......l........ 0010: 01 a7 00 06 c3 83 e1 7e c1 83 e5 7e 00 00 00
         * 1a .......~...~....
         * 
         * 0000: 5e 12 1a cc bd 01 3c 20 00 00 07 00 05 f2 da bd ^.....<
         * ........ 0010: 01 a7 00 06 c3 83 e1 7e c0 83 e5 7e 00 00 00 a9
         * .......~...~.... // 吸吻 0000: 5e 12 e1 b1 63 00 a9 1f 00 00 23 00 06
         * 93 c4 65 ^...c.....#....e 0010: 00 ec 00 00 68 7f 96 81 67 7f 96 81
         * 00 00 00 19 ....h..g......
         */
        this.writeC(S_OPCODE_ATTACKPACKET);
        this.writeC(actionId);// 动作代号
        this.writeD(withCastMotion ? cha.getId() : 0x00000000);// 执行者OBJID
        this.writeD(targetobj);// 目标OBJID

        if (dmg > 0) {
            this.writeH(0x000a); // 伤害值

        } else {
            this.writeH(0x0000); // 伤害值
        }

        this.writeC(newheading);// 新面向

        // 以原子方式将当前值加 1。
        this.writeD(_sequentialNumber.incrementAndGet());

        this.writeH(spellgfx);// 远程动画编号
        this.writeC(0x00); // 具备飞行动画:6, 不具备飞行动画:0
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

    private static int calcheading(final int myx, final int myy, final int tx,
            final int ty) {
        int newheading = 0;
        if ((tx > myx) && (ty > myy)) {
            newheading = 3;
        }
        if ((tx < myx) && (ty < myy)) {
            newheading = 7;
        }
        if ((tx > myx) && (ty == myy)) {
            newheading = 2;
        }
        if ((tx < myx) && (ty == myy)) {
            newheading = 6;
        }
        if ((tx == myx) && (ty < myy)) {
            newheading = 0;
        }
        if ((tx == myx) && (ty > myy)) {
            newheading = 4;
        }
        if ((tx < myx) && (ty > myy)) {
            newheading = 5;
        }
        if ((tx > myx) && (ty < myy)) {
            newheading = 1;
        }
        return newheading;
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
