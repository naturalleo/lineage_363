package com.lineage.server.serverpackets;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.event.SkillTeacherSet;
import com.lineage.list.PcLvSkillList;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 魔法购买(金币 / 全职技能导师)
 * 
 * @author dexc pctype 0:王族 1:骑士 2:精灵 3:法师 4:黑妖 5:龙骑 6:幻术
 * 
 */
public class S_SkillBuyCN extends ServerBasePacket {

    private static final Log _log = LogFactory.getLog(S_SkillBuyCN.class);

    private byte[] _byte = null;

    // 职业定义初始化价格(0:王族 1:骑士 2:精灵 3:法师 4:黑妖 5:龙骑 6:幻术)
    public static final int[] PCTYPE = new int[] { 2150, 2450, 1800, 5580,
            1950, 620, 470 };

    /**
     * 魔法购买(金币 / 全职技能导师)
     * 
     * @param pc
     * @param npc
     */
    public S_SkillBuyCN(final L1PcInstance pc, final L1NpcInstance npc) {
        ArrayList<Integer> skillList = null;

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

        final ArrayList<Integer> newSkillList = new ArrayList<Integer>();
        // 排除不予学习的技能
        for (final Integer integer : skillList) {
            if (SkillTeacherSet.RESKILLLIST.get(integer) == null) {
                // 检查是否已学习该法术
                if (!CharSkillReading.get().spellCheck(pc.getId(),
                        (integer + 1))) {
                    newSkillList.add(integer);
                }
            }
        }

        if (newSkillList.size() <= 0) {// 全职技能导师
            this.writeC(S_OPCODE_SHOWHTML);
            this.writeD(npc.getId());
            this.writeS("y_skill_02");
            this.writeH(0x00);
            this.writeH(0x00);

        } else {

            final int startAdena = PCTYPE[pc.getType()];

            try {
                this.writeC(S_OPCODE_SKILLBUY);
                this.writeD(startAdena);
                this.writeH(newSkillList.size());
                for (final Integer integer : newSkillList) {
                    this.writeD(integer);
                }

            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
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
