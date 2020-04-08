package com.lineage.server.model.skill.skillmode;

import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_ShowSummonList;
import com.lineage.server.templates.L1Npc;

/**
 * 召唤术
 * 
 * @author dexc
 * 
 */
public class SUMMON_MONSTER extends SkillMode {

    public SUMMON_MONSTER() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;// magic.calcMagicDamage(L1SkillId.SUMMON_MONSTER);
        final int level = srcpc.getLevel();
        int[] summons;
        if (!srcpc.getMap().isRecallPets()) {
            // 353：在这附近无法召唤怪物。
            srcpc.sendPackets(new S_ServerMessage(353));
            return dmg;
        }
        // 召唤控制戒指(20284)
        if (srcpc.getInventory().checkEquipped("$1937")) {
            srcpc.sendPackets(new S_ShowSummonList(srcpc.getId()));
            if (!srcpc.isSummonMonster()) {
                srcpc.setShapeChange(false);
                srcpc.setSummonMonster(true);
            }

        } else {
            summons = new int[] { 81210, 81213, 81216, 81219, 81222, 81225,
                    81228 };

            int summonid = 0;
            int summoncost = 6;
            int levelRange = 32;
            for (int i = 0; i < summons.length; i++) { // 目前等级
                if ((level < levelRange) || (i == summons.length - 1)) {
                    summonid = summons[i];
                    break;
                }
                levelRange += 4;
            }
            if (level >= 52) {
            	summoncost = 8;
            }
            if (level >= 56) {
            	summoncost = 10;
            }

            int petcost = 0;
            final Object[] petlist = srcpc.getPetList().values().toArray();
            for (final Object pet : petlist) {
                // 目前宠物数量
                petcost += ((L1NpcInstance) pet).getPetcost();
            }
            final int charisma = srcpc.getCha() + 6 - petcost;
            final int summoncount = charisma / summoncost;
            final L1Npc npcTemp = NpcTable.get().getTemplate(summonid);
            for (int i = 0; i < summoncount; i++) {
                final L1SummonInstance summon = new L1SummonInstance(npcTemp,
                        srcpc);
                summon.setPetcost(summoncost);
            }
        }
        return dmg;
    }

    @Override
    public int start(final L1NpcInstance npc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;

        return dmg;
    }

    @Override
    public void start(final L1PcInstance srcpc, final Object obj)
            throws Exception {
        final String s = (String) obj;
        String[] summonstr_list;
        int[] summonid_list;
        int[] summonlvl_list;
        int[] summoncha_list;
        int summonid = 0;
        int levelrange = 0;
        int summoncost = 0;

        // 传回质
        summonstr_list = new String[] { "7", "263", "519", "8", "264", "520",
                "9", "265", "521", "10", "266", "522", "11", "267", "523",
                "12", "268", "524", "13", "269", "525", "14", "270", "526",
                "15", "271", "527", "16", "17", "18", "274" };

        // NPCID
        summonid_list = new int[] { 81210, 81211, 81212, 81213, 81214, 81215,
                81216, 81217, 81218, 81219, 81220, 81221, 81222, 81223, 81224,
                81225, 81226, 81227, 81228, 81229, 81230, 81231, 81232, 81233,
                81234, 81235, 81236, 81237, 81238, 81239, 81240 };

        // 等级
        summonlvl_list = new int[] { 28, 28, 28, 32, 32, 32, 36, 36, 36, 40,
                40, 40, 44, 44, 44, 48, 48, 48, 52, 52, 52, 56, 56, 56, 60, 60,
                60, 64, 68, 72, 72 };

        // 魅力
        summoncha_list = new int[] { 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
                6, 6, 6, 6, 8, 8, 8, 10, 10, 10, 12, 12, 12, 20, 42, 42, 50 }; //hjx1000

        for (int loop = 0; loop < summonstr_list.length; loop++) {
            if (s.equalsIgnoreCase(summonstr_list[loop])) {
                summonid = summonid_list[loop];
                levelrange = summonlvl_list[loop];
                summoncost = summoncha_list[loop];
                break;
            }
        }

        // Lv不足
        if (srcpc.getLevel() < levelrange) {
            // 743 等级太低而无法召唤怪物
            srcpc.sendPackets(new S_ServerMessage(743));
            return;
        }

        int petcost = 0;
        final Object[] petlist = srcpc.getPetList().values().toArray();
        for (final Object pet : petlist) {
            // 目前耗用质
            petcost += ((L1NpcInstance) pet).getPetcost();
        }

        // 变形怪/黑豹/巨大牛人
        if (((summonid == 81238) || (summonid == 81239) || (summonid == 81240))
                && (petcost != 0)) {
            srcpc.sendPackets(new S_CloseList(srcpc.getId()));
            return;
        }

        final int charisma = srcpc.getCha() + 6 - petcost;
        final int summoncount = charisma / summoncost;

        boolean isStop = false;
        if ((srcpc.getCha() + 6) < summoncost) {
            isStop = true;
        }
        if (summoncount <= 0) {
            isStop = true;
        }

        if (isStop) {
            // 319：\f1你不能拥有太多的怪物。
            srcpc.sendPackets(new S_ServerMessage(319));
            srcpc.sendPackets(new S_CloseList(srcpc.getId()));
            return;
        }

        final L1Npc npcTemp = NpcTable.get().getTemplate(summonid);

        for (int cnt = 0; cnt < summoncount; cnt++) {
            final L1SummonInstance summon = new L1SummonInstance(npcTemp, srcpc);
            int upPetcost = 0;
            if (summon.getNameId().equals("$1554")) {// 变形怪
                upPetcost = 7;
            }
            if (summon.getNameId().equals("$2106")) {// 巨大牛人
                upPetcost = 7;
            }
            if (summon.getNameId().equals("$2587")) {// 黑豹
                upPetcost = 7;
            }
            summon.setPetcost(summoncost + upPetcost);
        }
        srcpc.sendPackets(new S_CloseList(srcpc.getId()));
    }

    @Override
    public void stop(final L1Character cha) throws Exception {
        // TODO Auto-generated method stub
    }
}
