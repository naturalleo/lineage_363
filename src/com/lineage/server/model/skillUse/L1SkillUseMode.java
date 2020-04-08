package com.lineage.server.model.skillUse;

import static com.lineage.server.model.skill.L1SkillId.*;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Skills;

/**
 * 技能相关使用条件/限制
 * 
 * @author daien
 * 
 */
public class L1SkillUseMode {

    /**
     * 该技能所需耗用的HP/MP/材料/正义质
     * 
     * @param user
     *            执行者
     * @param skill
     *            技能资料
     * @return
     */
    public boolean isConsume(final L1Character user, L1Skills skill) {
        int mpConsume = skill.getMpConsume();// 取回:技能耗用的MP
        int hpConsume = skill.getHpConsume();// 取回:技能耗用的HP

        final int itemConsume = skill.getItemConsumeId();// 取回:所需耗用材料
        final int itemConsumeCount = skill.getItemConsumeCount();// 取回:所需耗用材料数量

        final int lawful = skill.getLawful();// 取回:所需耗用正义质

        final int skillId = skill.getSkillId();// 取回:技能编号

        int currentMp = 0;// 执行者目前的MP
        int currentHp = 0;// 执行者目前的HP

        L1NpcInstance useNpc = null;
        L1PcInstance usePc = null;

        // 执行者是NPC
        if (user instanceof L1NpcInstance) {
            useNpc = (L1NpcInstance) user;
            currentMp = useNpc.getCurrentMp();
            currentHp = useNpc.getCurrentHp();

            boolean isStop = false;

            // 下列状态无法使用魔法(魔法封印)
            if (useNpc.hasSkillEffect(SILENCE)) {
                isStop = true;
            }

            // 下列状态无法使用魔法(封印禁地)
            if (useNpc.hasSkillEffect(AREA_OF_SILENCE) && !isStop) {
                isStop = true;
            }

            // 下列状态无法使用魔法(沈默毒素效果)
            if (useNpc.hasSkillEffect(STATUS_POISON_SILENCE) && !isStop) {
                isStop = true;
            }

            if (isStop) {
                return false;
            }
        }

        // 执行者是PC
        if (user instanceof L1PcInstance) {
            usePc = (L1PcInstance) user;

            currentMp = usePc.getCurrentMp();
            currentHp = usePc.getCurrentHp();

            // 智力 对应 技能等级 降低MP损耗
            if ((usePc.getInt() > 12) && (skillId > HOLY_WEAPON)
                    && (skillId <= FREEZING_BLIZZARD)) { // LV2以上
                mpConsume--;
            }
            if ((usePc.getInt() > 13) && (skillId > STALAC)
                    && (skillId <= FREEZING_BLIZZARD)) { // LV3以上
                mpConsume--;
            }
            if ((usePc.getInt() > 14) && (skillId > WEAK_ELEMENTAL)
                    && (skillId <= FREEZING_BLIZZARD)) { // LV4以上
                mpConsume--;
            }
            if ((usePc.getInt() > 15) && (skillId > MEDITATION)
                    && (skillId <= FREEZING_BLIZZARD)) { // LV5以上
                mpConsume--;
            }
            if ((usePc.getInt() > 16) && (skillId > DARKNESS)
                    && (skillId <= FREEZING_BLIZZARD)) { // LV6以上
                mpConsume--;
            }
            if ((usePc.getInt() > 17) && (skillId > BLESS_WEAPON)
                    && (skillId <= FREEZING_BLIZZARD)) { // LV7以上
                mpConsume--;
            }
            if ((usePc.getInt() > 18) && (skillId > DISEASE)
                    && (skillId <= FREEZING_BLIZZARD)) { // LV8以上
                mpConsume--;
            }

            if ((usePc.getInt() > 12) && (skillId >= SHOCK_STUN)
                    && (skillId <= COUNTER_BARRIER)) {
                mpConsume -= (usePc.getInt() - 12);
            }

            // 装备减低MP损耗
            switch (skillId) {
                case PHYSICAL_ENCHANT_DEX:// 通畅气脉术
                    if (usePc.getInventory().checkEquipped(20013)) {// 敏捷魔法头盔
                        mpConsume = mpConsume >> 1;
                    }
                    break;

                case HASTE:// 加速术
                    if (usePc.getInventory().checkEquipped(20013)) {// 敏捷魔法头盔
                        mpConsume = mpConsume >> 1;
                    }
                    if (usePc.getInventory().checkEquipped(20008)) {// 小型风之头盔
                        mpConsume = mpConsume >> 1;
                    }
                    break;

                case HEAL:// 初级治愈术
                    if (usePc.getInventory().checkEquipped(20014)) {// 治愈魔法头盔
                        mpConsume = mpConsume >> 1;
                    }
                    break;

                case EXTRA_HEAL:// 中级治愈术
                    if (usePc.getInventory().checkEquipped(20014)) {// 治愈魔法头盔
                        mpConsume = mpConsume >> 1;
                    }
                    break;

                case ENCHANT_WEAPON:// 拟似魔法武器
                    if (usePc.getInventory().checkEquipped(20015)) {// 力量魔法头盔
                        mpConsume = mpConsume >> 1;
                    }
                    break;

                case DETECTION:// 无所遁形术
                    if (usePc.getInventory().checkEquipped(20015)) {// 力量魔法头盔
                        mpConsume = mpConsume >> 1;
                    }
                    break;

                case PHYSICAL_ENCHANT_STR:// 体魄强健术
                    if (usePc.getInventory().checkEquipped(20015)) {// 力量魔法头盔
                        mpConsume = mpConsume >> 1;
                    }
                    break;

                case GREATER_HASTE:// 强力加速术
                    if (usePc.getInventory().checkEquipped(20023)) {// 风之头盔
                        mpConsume = mpConsume >> 1;
                    }
                    break;
            }

            // 智力 对应 降低MP损耗
            if (usePc.getOriginalMagicConsumeReduction() > 0) {
                mpConsume -= usePc.getOriginalMagicConsumeReduction();
            }

            if (0 < skill.getMpConsume()) { // 该技能MP耗用大于0
                mpConsume = Math.max(mpConsume, 1); // 最低耗用1
            }

            // 精灵的限制
            if (usePc.isElf()) {
                boolean isError = false;
                String msg = null;
                if ((skill.getSkillLevel() >= 17)
                        && (skill.getSkillLevel() <= 22)) {
                    final int magicattr = skill.getAttr();// 取回:技能属性
                    switch (magicattr) {
                        case 1:// 地
                            if (magicattr != usePc.getElfAttr()) {
                                isError = true;
                                msg = "$1062";
                            }
                            break;

                        case 2:// 火
                            if (magicattr != usePc.getElfAttr()) {
                                isError = true;
                                msg = "$1059";
                            }
                            break;

                        case 4:// 水
                            if (magicattr != usePc.getElfAttr()) {
                                isError = true;
                                msg = "$1060";
                            }
                            break;

                        case 8:// 风
                            if (magicattr != usePc.getElfAttr()) {
                                isError = true;
                                msg = "$1061";
                            }
                            break;
                    }
                    if ((skillId == ELEMENTAL_PROTECTION)
                            && (usePc.getElfAttr() == 0)) {
                        // 280 \f1施咒失败。
                        usePc.sendPackets(new S_ServerMessage(280));
                        return false;
                    }
                }
                if (isError) {
                    if (!usePc.isGm()) {
                        // 1059火属性
                        // 1060水属性
                        // 1061风属性
                        // 1062地属性
                        // 352 若要使用这个法术，属性必须成为 %0。
                        usePc.sendPackets(new S_ServerMessage(1385, msg));
                        return false;
                    }
                }
            }

            // 法师的限制
            /*if (usePc.isDragonKnight()) {*/
            //修正究极光裂术正义限制 hjx1000
            if (usePc.isWizard()) {
                // 究极光裂术
                if ((skillId == DISINTEGRATE) && (usePc.getLawful() < 500)) {
                    // 352 若要使用这个法术，属性必须成为 %0。
                    usePc.sendPackets(new S_ServerMessage(352, "$967"));
                    return false;
                }
            }

            // 黑暗精灵的限制
            if (usePc.isDarkelf()) {
                if (skillId == FINAL_BURN) {
                    hpConsume = currentHp - 1;
                }
            }

            // 龙骑士的限制
            if (usePc.isDragonKnight()) {
                boolean isError = false;
                switch (usePc.getAwakeSkillId()) {
                    case AWAKEN_ANTHARAS:
                        switch (skillId) {
                            case AWAKEN_ANTHARAS:
                            case MAGMA_BREATH:
                            case SHOCK_SKIN:
                            case FREEZING_BREATH:
                                break;
                            default:
                                isError = true;
                                break;
                        }
                        break;

                    case AWAKEN_FAFURION:
                        switch (skillId) {
                            case AWAKEN_FAFURION:
                            case MAGMA_BREATH:
                            case SHOCK_SKIN:
                            case FREEZING_BREATH:
                                break;
                            default:
                                isError = true;
                                break;
                        }
                        break;

                    case AWAKEN_VALAKAS:
                        switch (skillId) {
                            case AWAKEN_VALAKAS:
                            case MAGMA_BREATH:
                            case SHOCK_SKIN:
                            case FREEZING_BREATH:
                                break;
                            default:
                                isError = true;
                                break;
                        }
                        break;
                }
                if (isError) {
                    // 1385 目前状态中无法使用觉醒魔法。
                    usePc.sendPackets(new S_ServerMessage(1385));
                    return false;
                }
            }

            // 材料判定           
            if (itemConsume != 0) {
                if (!usePc.getInventory().checkItem(itemConsume,
                        itemConsumeCount)) {
                    if (!usePc.isGm()) {
                        // 299 \f1施放魔法所需材料不足。
                        usePc.sendPackets(new S_ServerMessage(299));
                        return false;
                    }
                }
            }
        }

        // 体力不足
        if (currentHp < hpConsume + 1) {
            if (usePc != null) {
                // 279 \f1因体力不足而无法使用魔法。
                usePc.sendPackets(new S_ServerMessage(279));
            }
            return false;
        }

        // 魔力不足
        if (currentMp < mpConsume) {
            if (usePc != null) {
                // 278 \f1因魔力不足而无法使用魔法。
                usePc.sendPackets(new S_ServerMessage(278));
                // 执行者是GM 恢复魔力
                if (usePc.isGm()) {
                    usePc.setCurrentMp(usePc.getMaxMp());
                }
            }
            return false;
        }

        if (usePc != null) {
            // 正义质增减
            if (lawful != 0) {
                int newLawful = usePc.getLawful() + lawful;
                if (newLawful > 32767) {
                    newLawful = 32767;
                }
                if (newLawful < -32767) {
                    newLawful = -32767;
                }
                usePc.setLawful(newLawful);
            }

            if (itemConsume != 0) {
                // 材料的消耗
                usePc.getInventory().consumeItem(itemConsume, itemConsumeCount);
            }
        }

        final int current_hp = user.getCurrentHp() - hpConsume;
        user.setCurrentHp(current_hp);

        final int current_mp = user.getCurrentMp() - mpConsume;
        user.setCurrentMp(current_mp);

        return true;
    }
}
