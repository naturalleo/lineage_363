package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.COOKING_BEGIN;
import static com.lineage.server.model.skill.L1SkillId.COOKING_END;
import static com.lineage.server.model.skill.L1SkillId.SKILLS_BEGIN;
import static com.lineage.server.model.skill.L1SkillId.SKILLS_END;
import static com.lineage.server.model.skill.L1SkillId.STATUS_BEGIN;
import static com.lineage.server.model.skill.L1SkillId.STATUS_END;
import static com.lineage.server.model.skill.L1SkillId.STATUS_FREEZE;

import com.lineage.data.quest.EWLv40_1;
import com.lineage.data.quest.ElfLv45_2;
import com.lineage.data.quest.KnightLv45_1;
import com.lineage.data.quest.WizardLv50_1;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.L1PinkName;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillMode;
import com.lineage.server.serverpackets.S_ChangeName;
import com.lineage.server.serverpackets.S_ChangeShape;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_DoActionShop;
import com.lineage.server.serverpackets.S_SkillBrave;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.utils.L1SpawnUtil;

/**
 * 魔法相消术
 * 
 * @author dexc
 * 
 */
public class CANCELLATION extends SkillMode {

    public CANCELLATION() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;// magic.calcMagicDamage(L1SkillId.CURE_POISON);
        // 对象是NPC
        if (cha instanceof L1NpcInstance) {
            final L1NpcInstance tgnpc = (L1NpcInstance) cha;
            // 取回NPCID
            final int npcId = tgnpc.getNpcTemplate().get_npcId();
            switch (npcId) {
                case KnightLv45_1._searcherid:// 调查员
                    if (tgnpc.getGfxId() == tgnpc.getTempCharGfx()) {
                        tgnpc.setTempCharGfx(1314);
                        tgnpc.broadcastPacketAll(new S_ChangeShape(tgnpc, 1314));
                        return dmg;
                    }
                    break;

                case ElfLv45_2._npcId:// 独角兽 => 梦魇
                    if (tgnpc.getGfxId() == tgnpc.getTempCharGfx()) {
                        final int x = tgnpc.getX();
                        final int y = tgnpc.getY();
                        final short m = tgnpc.getMapId();
                        final int h = tgnpc.getHeading();
                        tgnpc.deleteMe();
                        L1SpawnUtil.spawnT(45641, x, y, m, h, 300);
                        return dmg;
                    }
                    break;

                case EWLv40_1._roiid:// 罗伊
                    if (tgnpc.getGfxId() == tgnpc.getTempCharGfx()) {
                        tgnpc.setTempCharGfx(4310);
                        tgnpc.broadcastPacketAll(new S_ChangeShape(tgnpc, 4310));
                        return dmg;
                    }
                    break;

                case WizardLv50_1._npcId:// 迪嘉勒廷的男间谍(欧姆民兵)
                    if (tgnpc.getGfxId() == tgnpc.getTempCharGfx()) {
                        // 改变外型
                        tgnpc.setTempCharGfx(5217);
                        tgnpc.setPassispeed(480);
                        tgnpc.broadcastPacketAll(new S_ChangeShape(tgnpc, 5217));
                        // 改变名称
                        tgnpc.setNameId("$6068");// 公爵之间谍
                        tgnpc.broadcastPacketAll(new S_ChangeName(
                                tgnpc.getId(), "$6068"));
                        return dmg;
                    }
                    break;
            }
        }

        // 施展者 隐身状态解除隐身
        if ((srcpc != null) && srcpc.isInvisble()) {
            srcpc.delInvis();
        }

        // 技能状态解除
        for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
            if (L1SkillMode.get().isNotCancelable(skillNum) && !cha.isDead()) {
                continue;
            }
            cha.removeSkillEffect(skillNum);
        }

        // 技能状态解除
        cha.curePoison();
        cha.cureParalaysis();

        for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_END; skillNum++) {
            if (L1SkillMode.get().isNotCancelable(skillNum) && !cha.isDead()) {
                continue;
            }
            cha.removeSkillEffect(skillNum);
        }

        // 料理状态解除
        for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
            if (L1SkillMode.get().isNotCancelable(skillNum)) {
                continue;
            }
            cha.removeSkillEffect(skillNum);
        }

        cha.removeSkillEffect(STATUS_FREEZE); // Freeze解除

        // 对象是PC
        if (cha instanceof L1PcInstance) {
            final L1PcInstance tgpc = (L1PcInstance) cha;
            // 加速效果道具(武器/防具)效果解除
            if (tgpc.getHasteItemEquipped() > 0) {
                tgpc.setMoveSpeed(0);
                tgpc.sendPacketsAll(new S_SkillHaste(tgpc.getId(), 0, 0));
            }

            tgpc.sendPacketsAll(new S_CharVisualUpdate(tgpc));
            // 商店状态PC 状态恢复
            if (tgpc.isPrivateShop()) {
                tgpc.sendPacketsAll(new S_DoActionShop(tgpc.getId(), tgpc
                        .getShopChat()));
            }
            // 攻击对象是PC 设置红名
            L1PinkName.onAction(tgpc, srcpc);

        } else {// 对象不是PC
            // 解除NPC状态
            final L1NpcInstance tgnpc = (L1NpcInstance) cha;
            tgnpc.setMoveSpeed(0);
            tgnpc.setBraveSpeed(0);
            tgnpc.broadcastPacketAll(new S_SkillHaste(cha.getId(), 0, 0));
            tgnpc.broadcastPacketAll(new S_SkillBrave(cha.getId(), 0, 0));
            tgnpc.setWeaponBreaked(false);
            tgnpc.setParalyzed(false);
        }

        // 对象不是PC
        /*
         * if (!(cha instanceof L1PcInstance)) { // 解除NPC状态 final L1NpcInstance
         * npc = (L1NpcInstance) cha; npc.setMoveSpeed(0); npc.setBraveSpeed(0);
         * npc.broadcastPacketAll(new S_SkillHaste(cha.getId(), 0, 0));
         * npc.broadcastPacketAll(new S_SkillBrave(cha.getId(), 0, 0));
         * npc.setWeaponBreaked(false); npc.setParalyzed(false);
         * npc.setParalysisTime(0); }
         * 
         * if (cha instanceof L1PcInstance) { final L1PcInstance pc =
         * (L1PcInstance) cha; pc.sendPacketsAll(new S_CharVisualUpdate(pc)); //
         * 商店状态PC 状态恢复 if (pc.isPrivateShop()) { pc.sendPacketsAll(new
         * S_DoActionShop(pc.getId(), pc.getShopChat())); } // 攻击对象是PC 设置红名
         * L1PinkName.onAction(pc, srcpc); }
         */
        return dmg;
    }

    @Override
    public int start(final L1NpcInstance npc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;

        // 技能状态解除
        for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
            if (L1SkillMode.get().isNotCancelable(skillNum) && !cha.isDead()) {
                continue;
            }
            cha.removeSkillEffect(skillNum);
        }

        // 技能状态解除
        cha.curePoison();
        cha.cureParalaysis();

        for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_END; skillNum++) {
            if (L1SkillMode.get().isNotCancelable(skillNum) && !cha.isDead()) {
                continue;
            }
            cha.removeSkillEffect(skillNum);
        }

        // 料理状态解除
        for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
            if (L1SkillMode.get().isNotCancelable(skillNum)) {
                continue;
            }
            cha.removeSkillEffect(skillNum);
        }

        cha.removeSkillEffect(STATUS_FREEZE); // Freeze解除

        // 对象是PC
        if (cha instanceof L1PcInstance) {
            final L1PcInstance tgpc = (L1PcInstance) cha;
            // 加速效果道具(武器/防具)效果解除
            if (tgpc.getHasteItemEquipped() > 0) {
                tgpc.setMoveSpeed(0);
                tgpc.sendPacketsAll(new S_SkillHaste(tgpc.getId(), 0, 0));
            }

            tgpc.sendPacketsAll(new S_CharVisualUpdate(tgpc));
            // 商店状态PC 状态恢复
            if (tgpc.isPrivateShop()) {
                tgpc.sendPacketsAll(new S_DoActionShop(tgpc.getId(), tgpc
                        .getShopChat()));
            }

        } else {// 对象不是PC
            // 解除NPC状态
            final L1NpcInstance tgnpc = (L1NpcInstance) cha;
            tgnpc.setMoveSpeed(0);
            tgnpc.setBraveSpeed(0);
            tgnpc.broadcastPacketAll(new S_SkillHaste(cha.getId(), 0, 0));
            tgnpc.broadcastPacketAll(new S_SkillBrave(cha.getId(), 0, 0));
            tgnpc.setWeaponBreaked(false);
            tgnpc.setParalyzed(false);
        }
        return dmg;
    }

    @Override
    public void start(final L1PcInstance srcpc, final Object obj)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void stop(final L1Character cha) throws Exception {
        // TODO Auto-generated method stub
    }
}
