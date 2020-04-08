package com.lineage.server.command.executor;

import static com.lineage.server.model.skill.L1SkillId.*;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_PacketBoxGm;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;

/**
 * 全技能状态(参数:对象(gm,all))
 * 
 * @author dexc
 * 
 */
public class L1AllBuff implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1AllBuff.class);

    private static final int[] allBuffSkill = { PHYSICAL_ENCHANT_DEX,
            PHYSICAL_ENCHANT_STR,
            BLESS_WEAPON,
            BERSERKERS,
            IMMUNE_TO_HARM, //
            ADVANCE_SPIRIT, //

            REDUCTION_ARMOR,
            BOUNCE_ATTACK,//
            SOLID_CARRIAGE, BURNING_SPIRIT,

            VENOM_RESIST, DOUBLE_BRAKE,
            UNCANNY_DODGE,
            DRESS_EVASION, //
            GLOWING_AURA, BRAVE_AURA,

            RESIST_MAGIC, CLEAR_MIND, ELEMENTAL_PROTECTION, AQUA_PROTECTER,
            BURNING_WEAPON, IRON_SKIN, EXOTIC_VITALIZE, WATER_LIFE,
            ELEMENTAL_FIRE, SOUL_OF_FLAME, ADDITIONAL_FIRE,

            DRAGON_SKIN, BURNING_SLASH };

    private L1AllBuff() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1AllBuff();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            final StringTokenizer st = new StringTokenizer(arg);
            final String name = st.nextToken();
            // 全玩家
            if (name.equalsIgnoreCase("all")) {
                AllBuffRunnable allBuffRunnable = new AllBuffRunnable();
                GeneralThreadPool.get().execute(allBuffRunnable);
                return;
            }
            // GM 本身
            if (name.equalsIgnoreCase("gm")) {
                startGm(pc);
                return;
            }
            // 指定人物
            final L1PcInstance target = World.get().getPlayer(name);
            if (target == null) {
                // 73 \f1%0%d 不在线上。
                pc.sendPackets(new S_ServerMessage(73, name));
                return;
            }

            startPc(target);

        } catch (final Exception e) {
            final int mode = 4;
            pc.sendPackets(new S_PacketBoxGm(pc, mode));
        }
    }

    public static void startPc(final L1PcInstance target) {
        L1BuffUtil.haste(target, 3600 * 1000);
        L1BuffUtil.brave(target, 3600 * 1000);

        L1ItemInstance weapon = target.getWeapon();
        if (weapon != null) {
            int weaponType = weapon.getItem().getType();
            int polyid = -1;

            switch (weaponType) {
                case 1:// 剑
                case 2:// 匕首
                case 3:// 双手剑
                case 6:// 斧(单手)
                case 15:// 斧(双手)
                    polyid = 6276;// 白金骑士
                    break;

                case 11:// 钢爪
                case 12:// 双刀
                    polyid = 6282;// 白金刺客
                    break;

                case 16:// 魔杖(双手)
                case 7:// 魔杖(单手)
                case 17:// 奇古兽
                    polyid = 6277;// 白金法师
                    break;

                case 4:// 弓(双手)
                case 13:// 弓(单手)
                case 10:// 铁手甲
                    polyid = 6278;// 白金巡守
                    break;

                case 18:// 锁链剑
                case 14:// 矛(单手)
                case 5:// 矛(双手)
                    polyid = 7341;// 狂暴将军
                    break;
            }
            if (polyid != -1) {
                L1PolyMorph.doPoly(target, polyid, 7200,
                        L1PolyMorph.MORPH_BY_ITEMMAGIC);
            }
        }

        for (int i = 0; i < allBuffSkill.length; i++) {
            final L1Skills skill = SkillsTable.get().getTemplate(
                    allBuffSkill[i]);
            final L1SkillUse skillUse = new L1SkillUse();
            skillUse.handleCommands(target, allBuffSkill[i], target.getId(),
                    target.getX(), target.getY(), skill.getBuffDuration(),
                    L1SkillUse.TYPE_GMBUFF);// */
        }
    }

    public static void startGm(final L1PcInstance target) {
        L1BuffUtil.haste(target, 3600 * 1000);

        L1BuffUtil.brave(target, 3600 * 1000);

        L1PolyMorph.doPoly(target, 5641, 7200, L1PolyMorph.MORPH_BY_GM);

        for (int i = 0; i < allBuffSkill.length; i++) {
            final L1Skills skill = SkillsTable.get().getTemplate(
                    allBuffSkill[i]);
            new L1SkillUse().handleCommands(target, allBuffSkill[i],
                    target.getId(), target.getX(), target.getY(),
                    skill.getBuffDuration(), L1SkillUse.TYPE_GMBUFF);
        }
    }

    /**
     * 执行多数物件技能状态给予
     * 
     * @author daien
     * 
     */
    private class AllBuffRunnable implements Runnable {

        @Override
        public void run() {
            try {
                // int i = 0;
                for (L1PcInstance tgpc : World.get().getAllPlayers()) {
                    startPc(tgpc);
                    Thread.sleep(1);
                    // System.out.println(i++);
                }

            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }

        }
    }
}
