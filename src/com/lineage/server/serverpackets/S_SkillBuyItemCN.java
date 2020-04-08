package com.lineage.server.serverpackets;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.list.PcLvSkillList;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 魔法购买清单(材料)
 * 
 * @author dexc
 * 
 */
public class S_SkillBuyItemCN extends ServerBasePacket {

    private static final Log _log = LogFactory.getLog(S_SkillBuyItemCN.class);

    private byte[] _byte = null;

    /**
     * 魔法购买清单(材料)
     * 
     * @param pc
     * @param npc
     */
    public S_SkillBuyItemCN(final L1PcInstance pc, final L1NpcInstance npc) {
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

        for (final Integer integer : skillList) {
            // 检查是否已学习该法术
            if (!CharSkillReading.get().spellCheck(pc.getId(), (integer + 1))) {
                newSkillList.add(integer);
            }
        }

        if (newSkillList.size() <= 0) {
            this.writeC(S_OPCODE_SKILLBUYITEM);
            this.writeH(0x0000);

        } else {
            try {
                // 0000: 01 01 00 17 00 00 00 ee ........
                this.writeC(S_OPCODE_SKILLBUYITEM);
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
