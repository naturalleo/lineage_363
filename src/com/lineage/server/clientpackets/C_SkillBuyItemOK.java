package com.lineage.server.clientpackets;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.list.PcLvSkillList;
import com.lineage.server.datatables.SkillsItemTable;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_AddSkill;
import com.lineage.server.serverpackets.S_ItemError;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1SkillItem;
import com.lineage.server.templates.L1Skills;

/**
 * 要求完成学习魔法(道具)
 * 
 * @author daien
 * 
 */
public class C_SkillBuyItemOK extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_EnterPortal.class);

    /*
     * public C_SkillBuyItemOK() { }
     * 
     * public C_SkillBuyItemOK(final byte[] abyte0, final ClientExecutor client)
     * { super(abyte0); try { this.start(abyte0, client);
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

            final int count = this.readH();

            for (int i = 0; i < count; i++) {
                final int skillId = this.readD() + 1;

                // 检查是否已学习该法术
                if (!CharSkillReading.get().spellCheck(pc.getId(), skillId)) {
                    if (skillList.contains(new Integer(skillId - 1))) {
                        // 取回技能资料
                        final L1Skills l1skills = SkillsTable.get()
                                .getTemplate(skillId);

                        // 取回应耗用材料表
                        final L1SkillItem priceItem = SkillsItemTable.get()
                                .getTemplate(skillId);

                        if (priceItem != null && priceItem.get_items() != null) {
                            final int length = priceItem.get_items().length;
                            // 材料是否足够
                            final boolean isOks[] = new boolean[length];
                            // 检查数量
                            for (int x = 0; x < length; x++) {
                                int itemId = priceItem.get_items()[x];
                                int itemCount = priceItem.get_counts()[x];
                                if (!pc.getInventory().checkItem(itemId,
                                        itemCount)) {
                                    isOks[x] = false;

                                } else {
                                    isOks[x] = true;
                                }
                            }

                            boolean isShopOk = true;
                            for (boolean isOk : isOks) {
                                if (!isOk) {
                                    isShopOk = false;
                                }
                            }

                            if (isShopOk) {
                                for (int x = 0; x < priceItem.get_items().length; x++) {
                                    int itemId = priceItem.get_items()[x];
                                    int itemCount = priceItem.get_counts()[x];
                                    if (pc.getInventory().checkItem(itemId,
                                            itemCount)) {
                                        pc.getInventory().consumeItem(itemId,
                                                itemCount);
                                    }
                                }
                                // 加入资料库
                                CharSkillReading.get().spellMastery(pc.getId(),
                                        l1skills.getSkillId(),
                                        l1skills.getName(), 0, 0);
                                pc.sendPackets(new S_AddSkill(pc, skillId));
                                isGfx = true;

                            } else {
                                // 材料不足
                                pc.sendPackets(new S_ItemError(skillId - 1));
                            }

                        } else {
                            // 939：尚未开放。
                            pc.sendPackets(new S_ServerMessage(939));
                            _log.error("购买技能 材料 设置资料异常 材料未设置: " + skillId);
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
