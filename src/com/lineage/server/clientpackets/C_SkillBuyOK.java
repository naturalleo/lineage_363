package com.lineage.server.clientpackets;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.list.PcLvSkillList;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_AddSkill;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillBuyCN;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1Skills;

/**
 * 要求完成学习魔法(金币)
 * 
 * @author daien
 * 
 */
public class C_SkillBuyOK extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_SkillBuyOK.class);

    // 初始化价格计算表(技能等级区分 0~27)
    private static final int[] PRICE = new int[] { 1, // 0
            4, // 1
            9, // 2
            16, // 3
            25, // 4
            36, // 5
            49, // 6
            64, // 7
            81, // 8
            100, // 9
            121, // 10
            144, // 11
            169, // 12
            196, // 13
            225, // 14
            0, // 15
            289, // 16
            324, // 17
            361, // 18
            400, // 19
            441, // 20
            484, // 21
            529, // 22
            576, // 23
            625, // 24
            676, // 25
            729, // 26
            784, // 27
    };

    /*
     * public C_SkillBuyOK() { }
     * 
     * public C_SkillBuyOK(final byte[] abyte0, final ClientExecutor client) {
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

            final int count = this.readH();

            ArrayList<Integer> skillList = null;

            // 各职业等级可学技能清单
            if (pc.isCrown()) {// 王族
                skillList = PcLvSkillList.isCrown(pc);

            } else if (pc.isKnight()) {// 骑士
                skillList = PcLvSkillList.isKnight(pc);

            } else if (pc.isElf()) {// 精灵
                skillList = PcLvSkillList.isElf(pc);

            } else if (pc.isWizard()) {// 法师
                skillList = PcLvSkillList.isWizard(pc);

            } else if (pc.isDarkelf()) {// 黑妖
                skillList = PcLvSkillList.isDarkelf(pc);

            } else if (pc.isDragonKnight()) {// 龙骑
                skillList = PcLvSkillList.isDragonKnight(pc);

            } else if (pc.isIllusionist()) {// 幻术
                skillList = PcLvSkillList.isIllusionist(pc);
            }

            if (skillList == null) {
                return;
            }

            // 产生动画
            boolean isGfx = false;

            boolean shopSkill = false;
            if (pc.get_other().get_shopSkill()) {
                shopSkill = true;
            }

            for (int i = 0; i < count; i++) {
                final int skillId = this.readD() + 1;

                // 检查是否已学习该法术
                if (!CharSkillReading.get().spellCheck(pc.getId(), (skillId))) {
                    if (skillList.contains(new Integer(skillId - 1))) {
                        // 取回技能资料
                        final L1Skills l1skills = SkillsTable.get()
                                .getTemplate(skillId);

                        // 技能等级价格计算表
                        final int skillLvPrice = PRICE[l1skills.getSkillLevel() - 1];

                        // 耗用金币(shopSkill=true以价格表分类 : shopSkill=false以6000计价)
                        final int price = (shopSkill ? S_SkillBuyCN.PCTYPE[pc
                                .getType()] : 6000) * skillLvPrice;

                        if (pc.getInventory().checkItem(L1ItemId.ADENA, price)) {
                            pc.getInventory()
                                    .consumeItem(L1ItemId.ADENA, price);
                            // 加入资料库
                            CharSkillReading.get().spellMastery(pc.getId(),
                                    l1skills.getSkillId(), l1skills.getName(),
                                    0, 0);
                            pc.sendPackets(new S_AddSkill(pc, skillId));
                            isGfx = true;

                        } else {
                            // 金币不足
                            pc.sendPackets(new S_ServerMessage(189));
                        }
                    }
                }
            }

            if (isGfx) {
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 224));
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
