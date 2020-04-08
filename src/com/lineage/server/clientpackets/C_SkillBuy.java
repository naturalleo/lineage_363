package com.lineage.server.clientpackets;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.list.PcLvSkillList;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_SkillBuy;
import com.lineage.server.serverpackets.S_SkillBuyCN;
import com.lineage.server.world.World;

/**
 * 要求学习魔法(金币)
 * 
 * @author daien
 * 
 */
public class C_SkillBuy extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_SkillBuy.class);

    /*
     * public C_SkillBuy() { }
     * 
     * public C_SkillBuy(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final L1PcInstance pc = client.getActiveChar();

            if (pc.isGhost()) { // 鬼魂模式
                return;
            }

            if (pc.isDead()) { // 死亡
                return;
            }

            if (pc.isTeleport()) { // 传送中
                return;
            }

            if (pc.isPrivateShop()) { // 商店村模式
                return;
            }

            final int objid = this.readD();

            final L1Object obj = World.get().findObject(objid);
            if (obj == null) {
                return;
            }

            L1NpcInstance npc = null;

            if (obj instanceof L1NpcInstance) {
                npc = (L1NpcInstance) obj;
            }

            if (npc == null) {
                return;
            }

            if (npc.getNpcId() == 70754) {// 全职技能导师
                pc.get_other().set_shopSkill(true);
                pc.sendPackets(new S_SkillBuyCN(pc, npc));

            } else {
                pc.get_other().set_shopSkill(false);

                final ArrayList<Integer> skillList = PcLvSkillList.scount(pc);
                final ArrayList<Integer> newSkillList = new ArrayList<Integer>();
                // 判断可以学习的魔法
                for (final Integer integer : skillList) {
                    // 检查是否已学习该法术
                    if (!CharSkillReading.get().spellCheck(pc.getId(),
                            (integer + 1))) {
                        newSkillList.add(integer);
                    }
                }
                switch (npc.getNpcId()) {
                    case 70009:// 吉伦
                        if (pc.getLawful() < 0) {// 邪恶
                            // 像你一样如此强壮的人是不会需要它的
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gerengEv2"));
                            return;
                        }
                        if (newSkillList.size() <= 0) {
                            // 你已经学完这个等级所有的课程。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gerengEv3"));
                            return;
                        }
                        pc.sendPackets(new S_SkillBuy(pc, newSkillList));
                        break;

                    default:// 其他
                        pc.sendPackets(new S_SkillBuy(pc, newSkillList));
                        break;
                }

            }

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
