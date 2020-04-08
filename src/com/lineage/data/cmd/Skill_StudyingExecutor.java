package com.lineage.data.cmd;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 技能学习成功与否的判断
 * 
 * @author dexc
 * 
 */
public interface Skill_StudyingExecutor {

    /**
     * 学习技能等级限制 与地图位置判断
     * 
     * @param pc
     *            人物
     * @param skillId
     *            技能编号
     * @param magicAtt
     *            技能等级分组<BR>
     *            1~10共同魔法<BR>
     *            11~20精灵魔法<BR>
     *            21~30王族魔法<BR>
     *            31~40骑士魔法<BR>
     *            41~50黑暗精灵魔法<BR>
     *            51~60龙骑士魔法<BR>
     *            61~70幻术师魔法<BR>
     * 
     * @param attribute
     *            技能属性<BR>
     *            0:中立属性魔法<BR>
     *            1:正义属性魔法<BR>
     *            2:邪恶属性魔法<BR>
     *            3:精灵专属魔法<BR>
     *            4:王族专属魔法<BR>
     *            5:骑士专属技能<BR>
     *            6:黑暗精灵专属魔法<BR>
     *            7:龙骑士专属魔法<BR>
     *            8:幻术师专属魔法<BR>
     * 
     * @param itemObj
     *            道具objid(点选的物品)
     */
    public void magic(L1PcInstance pc, int skillId, int magicAtt,
            int attribute, int itemObj);
}
