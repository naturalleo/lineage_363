package com.lineage.server.utils;

import static com.lineage.server.model.skill.L1SkillId.*;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigRate;
import com.lineage.server.datatables.ExpTable;
import com.lineage.server.datatables.MapExpTable;
import com.lineage.server.datatables.lock.PetReading;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1IllusoryInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.serverpackets.S_NPCPack_Pet;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Pet;
import com.lineage.server.world.World;

/**
 * 经验值取得计算
 * 
 * @author dexc
 * 
 */
public class CalcExp {

    private static final Log _log = LogFactory.getLog(CalcExp.class);

    private CalcExp() {
    }

    public static void calcExp(final L1PcInstance srcpc, final int targetid,
            final ArrayList<?> acquisitorList,
            final ArrayList<Integer> hateList, final long exp) {
        try {
            int i = 0;
            double party_level = 0;
            double dist = 0;
            long member_exp = 0;
            int member_lawful = 0;
            final L1Object object = World.get().findObject(targetid);
            L1NpcInstance npc = null;

            if (object instanceof L1NpcInstance) {
                npc = (L1NpcInstance) object;

            } else {
                // object 不是 L1NpcInstance
                return;
            }

            // ヘイトの合计を取得
            L1Character acquisitor;
            int hate = 0;
            long acquire_exp = 0;
            int acquire_lawful = 0;
            long party_exp = 0;
            int party_lawful = 0;
            long totalHateExp = 0;
            int totalHateLawful = 0;
            long partyHateExp = 0;
            int partyHateLawful = 0;
            long ownHateExp = 0;

            if (acquisitorList.size() != hateList.size()) {
                return;
            }

            for (i = hateList.size() - 1; i >= 0; i--) {
                acquisitor = (L1Character) acquisitorList.get(i);
                hate = hateList.get(i);

                boolean isRemove = false;// 取消经验质奖励
                // 攻击者是 分身
                if (acquisitor instanceof L1IllusoryInstance) {
                    isRemove = true;
                }
                // 攻击者是 技能物件
                if (acquisitor instanceof L1EffectInstance) {
                    isRemove = true;
                }
                // 取消经验质奖励(该物件不分取经验质)
                if (isRemove) {
                    if (acquisitor != null) {
                        acquisitorList.remove(i);
                        hateList.remove(i);
                    }
                    continue;
                }

                if ((acquisitor != null) && !acquisitor.isDead()) {
                    totalHateExp += hate;
                    if (acquisitor instanceof L1PcInstance) {
                        totalHateLawful += hate;
                    }

                } else { // nullだったり死んでいたら排除
                    acquisitorList.remove(i);
                    hateList.remove(i);
                }
            }

            if (totalHateExp == 0) { // 取得者がいない场合
                return;
            }

            if ((object != null) && !(npc instanceof L1PetInstance)
                    && !(npc instanceof L1SummonInstance)) {
                // int exp = npc.get_exp();
                if (!World.get().isProcessingContributionTotal()
                        && (srcpc.getHomeTownId() > 0)) {
                    final int contribution = npc.getLevel() / 10;
                    srcpc.addContribution(contribution);
                }
                final int lawful = npc.getLawful();

                if (srcpc.isInParty()) { // パーティー中
                    // パーティーのヘイトの合计を算出
                    // パーティーメンバー以外にはそのまま配分
                    partyHateExp = 0;
                    partyHateLawful = 0;
                    for (i = hateList.size() - 1; i >= 0; i--) {
                        acquisitor = (L1Character) acquisitorList.get(i);
                        hate = hateList.get(i);
                        if (acquisitor instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) acquisitor;
                            if (pc == srcpc) {
                                partyHateExp += hate;
                                partyHateLawful += hate;

                            } else if (srcpc.getParty().isMember(pc)) {
                                partyHateExp += hate;
                                partyHateLawful += hate;

                            } else {
                                if (totalHateExp > 0) {
                                    acquire_exp = (exp * hate / totalHateExp);
                                }
                                if (totalHateLawful > 0) {
                                    acquire_lawful = (lawful * hate / totalHateLawful);
                                }
                                addExp(pc, acquire_exp, acquire_lawful);
                            }

                        } else if (acquisitor instanceof L1PetInstance) {
                            final L1PetInstance pet = (L1PetInstance) acquisitor;
                            final L1PcInstance master = (L1PcInstance) pet
                                    .getMaster();
                            if (master == srcpc) {
                                partyHateExp += hate;

                            } else if (srcpc.getParty().isMember(master)) {
                                partyHateExp += hate;

                            } else {
                                if (totalHateExp > 0) {
                                    acquire_exp = (exp * hate / totalHateExp);
                                }
                                addExpPet(pet, acquire_exp);
                            }

                        } else if (acquisitor instanceof L1SummonInstance) {
                            final L1SummonInstance summon = (L1SummonInstance) acquisitor;
                            final L1PcInstance master = (L1PcInstance) summon
                                    .getMaster();
                            if (master == srcpc) {
                                partyHateExp += hate;

                            } else if (srcpc.getParty().isMember(master)) {
                                partyHateExp += hate;

                            } else {
                            }
                        }
                    }

                    if (totalHateExp > 0) {
                        party_exp = (exp * partyHateExp / totalHateExp);
                    }

                    if (totalHateLawful > 0) {
                        party_lawful = (lawful * partyHateLawful / totalHateLawful);
                    }

                    // EXP、ロウフル配分

                    // プリボーナス
                    double pri_bonus = 0.0D;
                    final L1PcInstance leader = srcpc.getParty().getLeader();
                    if (leader.isCrown()
                            && (srcpc.knownsObject(leader) || srcpc
                                    .equals(leader))) {
                        pri_bonus = 0.059;
                    }

                    final Object[] pcs = srcpc.getParty().partyUsers().values()
                            .toArray();

                    double pt_bonus = 0.0D;
                    for (final Object obj : pcs) {
                        if (obj instanceof L1PcInstance) {
                            final L1PcInstance each = (L1PcInstance) obj;
                            if (each.isDead()) {
                                continue;
                            }
                            if (srcpc.knownsObject(each) || srcpc.equals(each)) {
                                party_level += each.getLevel()
                                        * each.getLevel();
                            }
                            if (srcpc.knownsObject(each)) {
                                pt_bonus += 0.04;
                            }
                        }
                    }
                    party_exp = (long) (party_exp * (1 + pt_bonus + pri_bonus));

                    // 自キャラクターとそのペット?サモンのヘイトの合计を算出
                    if (party_level > 0) {
                        dist = ((srcpc.getLevel() * srcpc.getLevel()) / party_level);
                    }
                    member_exp = (long) (party_exp * dist);
                    member_lawful = (int) (party_lawful * dist);

                    ownHateExp = 0;
                    for (i = hateList.size() - 1; i >= 0; i--) {
                        acquisitor = (L1Character) acquisitorList.get(i);
                        hate = hateList.get(i);
                        if (acquisitor instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) acquisitor;
                            if (pc == srcpc) {
                                ownHateExp += hate;
                            }

                        } else if (acquisitor instanceof L1PetInstance) {
                            final L1PetInstance pet = (L1PetInstance) acquisitor;
                            final L1PcInstance master = (L1PcInstance) pet
                                    .getMaster();
                            if (master == srcpc) {
                                ownHateExp += hate;
                            }

                        } else if (acquisitor instanceof L1SummonInstance) {
                            final L1SummonInstance summon = (L1SummonInstance) acquisitor;
                            final L1PcInstance master = (L1PcInstance) summon
                                    .getMaster();
                            if (master == srcpc) {
                                ownHateExp += hate;
                            }
                        }
                    }
                    // 自キャラクターとそのペット?サモンに分配
                    if (ownHateExp != 0) { // 攻击に参加していた
                        for (i = hateList.size() - 1; i >= 0; i--) {
                            acquisitor = (L1Character) acquisitorList.get(i);
                            hate = hateList.get(i);
                            if (acquisitor instanceof L1PcInstance) {
                                final L1PcInstance pc = (L1PcInstance) acquisitor;
                                if (pc == srcpc) {
                                    if (ownHateExp > 0) {
                                        acquire_exp = (member_exp * hate / ownHateExp);
                                    }
                                    addExp(pc, acquire_exp, member_lawful);
                                }

                            } else if (acquisitor instanceof L1PetInstance) {
                                final L1PetInstance pet = (L1PetInstance) acquisitor;
                                final L1PcInstance master = (L1PcInstance) pet
                                        .getMaster();
                                if (master == srcpc) {
                                    if (ownHateExp > 0) {
                                        acquire_exp = (member_exp * hate / ownHateExp);
                                    }
                                    addExpPet(pet, acquire_exp);
                                }

                            } else if (acquisitor instanceof L1SummonInstance) {
                            }
                        }

                    } else { // 攻击に参加していなかった
                        // 自キャラクターのみに分配
                        addExp(srcpc, member_exp, member_lawful);
                    }

                    // パーティーメンバーとそのペット?サモンのヘイトの合计を算出

                    for (final Object obj : pcs) {
                        if (obj instanceof L1PcInstance) {
                            final L1PcInstance tgpc = (L1PcInstance) obj;
                            if (tgpc.isDead()) {
                                continue;
                            }
                            if (tgpc.isDead()) {
                                continue;
                            }
                            if (srcpc.knownsObject(tgpc)) {
                                if (party_level > 0) {
                                    dist = ((tgpc.getLevel() * tgpc.getLevel()) / party_level);
                                }
                                member_exp = (int) (party_exp * dist);
                                member_lawful = (int) (party_lawful * dist);

                                ownHateExp = 0;
                                for (i = hateList.size() - 1; i >= 0; i--) {
                                    acquisitor = (L1Character) acquisitorList
                                            .get(i);
                                    hate = hateList.get(i);
                                    if (acquisitor instanceof L1PcInstance) {
                                        final L1PcInstance pc = (L1PcInstance) acquisitor;
                                        if (pc == tgpc) {
                                            ownHateExp += hate;
                                        }

                                    } else if (acquisitor instanceof L1PetInstance) {
                                        final L1PetInstance pet = (L1PetInstance) acquisitor;
                                        final L1PcInstance master = (L1PcInstance) pet
                                                .getMaster();
                                        if (master == tgpc) {
                                            ownHateExp += hate;
                                        }

                                    } else if (acquisitor instanceof L1SummonInstance) {
                                        final L1SummonInstance summon = (L1SummonInstance) acquisitor;
                                        final L1PcInstance master = (L1PcInstance) summon
                                                .getMaster();
                                        if (master == tgpc) {
                                            ownHateExp += hate;
                                        }
                                    }
                                }
                                // パーティーメンバーとそのペット?サモンに分配
                                if (ownHateExp != 0) { // 攻击に参加していた
                                    for (i = hateList.size() - 1; i >= 0; i--) {
                                        acquisitor = (L1Character) acquisitorList
                                                .get(i);
                                        hate = hateList.get(i);
                                        if (acquisitor instanceof L1PcInstance) {
                                            final L1PcInstance pc = (L1PcInstance) acquisitor;
                                            if (pc == tgpc) {
                                                if (ownHateExp > 0) {
                                                    acquire_exp = (member_exp
                                                            * hate / ownHateExp);
                                                }
                                                addExp(pc, acquire_exp,
                                                        member_lawful);
                                            }

                                        } else if (acquisitor instanceof L1PetInstance) {
                                            final L1PetInstance pet = (L1PetInstance) acquisitor;
                                            final L1PcInstance master = (L1PcInstance) pet
                                                    .getMaster();
                                            if (master == tgpc) {
                                                if (ownHateExp > 0) {
                                                    acquire_exp = (member_exp
                                                            * hate / ownHateExp);
                                                }
                                                addExpPet(pet, acquire_exp);
                                            }

                                        } else if (acquisitor instanceof L1SummonInstance) {
                                        }
                                    }

                                } else { // 攻击に参加していなかった
                                    // パーティーメンバーのみに分配
                                    addExp(tgpc, member_exp, member_lawful);
                                }
                            }
                        }
                    }

                } else { // パーティーを组んでいない
                    // EXP、ロウフルの分配
                    for (i = hateList.size() - 1; i >= 0; i--) {
                        acquisitor = (L1Character) acquisitorList.get(i);
                        hate = hateList.get(i);
                        acquire_exp = (exp * hate / totalHateExp);
                        if (acquisitor instanceof L1PcInstance) {
                            if (totalHateLawful > 0) {
                                acquire_lawful = (lawful * hate / totalHateLawful);
                            }
                        }

                        if (acquisitor instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) acquisitor;
                            addExp(pc, acquire_exp, acquire_lawful);

                        } else if (acquisitor instanceof L1PetInstance) {
                            final L1PetInstance pet = (L1PetInstance) acquisitor;
                            addExpPet(pet, acquire_exp);

                        } else if (acquisitor instanceof L1SummonInstance) {

                        }
                    }
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 建立EXP 与 正义质 取得
     * 
     * @param pc
     * @param exp
     * @param lawful
     */
    private static void addExp(final L1PcInstance pc, final long exp,
            final int lawful) {
        try {
            final int add_lawful = (int) (lawful * ConfigRate.RATE_LA) * -1;
            if (pc.getAccessLevel() != 4) {//修改玩家权限为4的时候打怪得不到正义值
                pc.addLawful(add_lawful);
            }

            if (pc.getLevel() >= ExpTable.MAX_LEVEL) {// 已达最大等级终止计算
                return;
            }
//            if (pc.getExp() >= 1047600998) {//等级限制77级50%
//            	return;
//            }

            double addExp = exp;

            // 目前等级可获取的经验值
            final double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
            // 目前等级可获取的经验值
            if (exppenalty < 1D) {
                addExp *= exppenalty;
            }

            // 服务器经验加倍
            if (ConfigRate.RATE_XP > 1.0) {
                addExp *= ConfigRate.RATE_XP;
            }

            addExp *= add(pc);
            pc.addExp((long) addExp);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * EXP增减
     * 
     * @param pc
     * @return
     */
    private static double add(L1PcInstance pc) {
        try {
            double add_exp = 1.0D;
            if (pc.is_mazu()) {// 妈祖祝福
                add_exp += 0.1;// 1.1
            }

            // 食物经验值倍数/第一段经验加倍
            double foodBonus = 0.0D;
            if (pc.hasSkillEffect(COOKING_1_7_N)
                    || pc.hasSkillEffect(COOKING_1_7_S)) {
                foodBonus = 0.01;// 1.01
            }
            if (pc.hasSkillEffect(COOKING_2_7_N)
                    || pc.hasSkillEffect(COOKING_2_7_S)) {
                foodBonus = 0.02;// 1.02
            }
            if (pc.hasSkillEffect(COOKING_3_7_N)
                    || pc.hasSkillEffect(COOKING_3_7_S)) {
                foodBonus = 0.03;// 1.03
            }

            if (pc.hasSkillEffect(SEXP11)) {
                foodBonus = 0.10;
            }
            if (pc.hasSkillEffect(EXP13)) {
                foodBonus = 0.30;
            }
            if (pc.hasSkillEffect(EXP15)) {
                foodBonus = 0.50;
            }
            if (pc.hasSkillEffect(EXP17)) {
                foodBonus = 0.70;
            }
            if (pc.hasSkillEffect(EXP20)) {
                foodBonus = 1.00;// 2.0
            }
            if (pc.hasSkillEffect(EXP25)) {
                foodBonus = 1.50;// 2.5
            }
            if (pc.hasSkillEffect(EXP30)) {
                foodBonus = 2.00;// 3.0
            }
            if (pc.hasSkillEffect(EXP35)) {
                foodBonus = 2.50;
            }
            if (pc.hasSkillEffect(EXP40)) {
                foodBonus = 3.00;
            }
            if (pc.hasSkillEffect(EXP45)) {
                foodBonus = 3.50;
            }
            if (pc.hasSkillEffect(EXP50)) {
                foodBonus = 4.00;
            }
            if (pc.hasSkillEffect(EXP55)) {
                foodBonus = 4.50;
            }
            if (pc.hasSkillEffect(EXP60)) {
                foodBonus = 5.00;
            }
            if (pc.hasSkillEffect(EXP65)) {
                foodBonus = 5.50;
            }
            if (pc.hasSkillEffect(EXP70)) {
                foodBonus = 6.00;
            }
            if (pc.hasSkillEffect(EXP75)) {
                foodBonus = 6.50;
            }
            if (pc.hasSkillEffect(EXP80)) {
                foodBonus = 7.00;
            }

            // 食物经验值倍数/第一段经验加倍
            if (foodBonus > 0) {
                add_exp += foodBonus;
            }

            // 经验值增加
            add_exp += pc.getExpAdd();

            // 第二段经验加倍
            double s2_exp = 0.0D;
            if (pc.hasSkillEffect(SEXP13)) {
                s2_exp = 0.30;// 1.30
            }
            if (pc.hasSkillEffect(SEXP15)) {
                s2_exp = 0.50;// 1.50
            }
            if (pc.hasSkillEffect(SEXP17)) {
                s2_exp = 0.70;// 1.70
            }
            if (pc.hasSkillEffect(SEXP20)) {
                s2_exp = 1.00;// 2.00
            }

            if (s2_exp > 0) {
                add_exp += s2_exp;
            }

            final int mapid = pc.getMapId();
            // 地图经验加倍
            if (MapExpTable.get().get_level(mapid, pc.getLevel())) {
                add_exp += (MapExpTable.get().get_exp(mapid) - 1.0D);
            }

            return add_exp;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return 1.0D;
    }

    private static void addExpPet(final L1PetInstance pet, final long exp) {
        try {
            if (pet.getPetType() == null) {
                return;
            }
            final L1PcInstance pc = (L1PcInstance) pet.getMaster();

            final int petItemObjId = pet.getItemObjId();

            final int levelBefore = pet.getLevel();
            long totalExp = (long) (exp * ConfigRate.RATE_XP + pet.getExp());
            // 宠物等级限制
            final int maxLevel = pet.getMaster().getLevel();//主人等级
            if (totalExp >= ExpTable.getExpByLevel(maxLevel)) {
                totalExp = ExpTable.getExpByLevel(maxLevel) - 1;
            }
            pet.setExp(totalExp);

            pet.setLevel(ExpTable.getLevelByExp(totalExp));

            final int expPercentage = ExpTable.getExpPercentage(pet.getLevel(),
                    totalExp);

            final int gap = pet.getLevel() - levelBefore;
            for (int i = 1; i <= gap; i++) {
                final RangeInt hpUpRange = pet.getPetType().getHpUpRange();
                final RangeInt mpUpRange = pet.getPetType().getMpUpRange();
                pet.addMaxHp(hpUpRange.randomValue());
                pet.addMaxMp(mpUpRange.randomValue());
            }

            pet.setExpPercent(expPercentage);
            pc.sendPackets(new S_NPCPack_Pet(pet, pc));

            if (gap != 0) {
                final L1Pet petTemplate = PetReading.get().getTemplate(
                        petItemObjId);
                if (petTemplate == null) {
                    return;
                }
                petTemplate.set_exp((int) pet.getExp());
                petTemplate.set_level(pet.getLevel());
                petTemplate.set_hp(pet.getMaxHp());
                petTemplate.set_mp(pet.getMaxMp());
                PetReading.get().storePet(petTemplate); // 资料保存
                // \f1%0升级了。
                pc.sendPackets(new S_ServerMessage(320, pet.getName()));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
