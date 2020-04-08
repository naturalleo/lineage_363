package com.lineage.data.item_etcitem.dragon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.skill.L1SkillMode;
import com.lineage.server.model.skill.skillmode.SkillMode;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * <font color=#00800>49502 水属性 结晶</font><BR>
 * 水 魔法伤害减免 寒冰耐心+3，持续1200秒
 * 
 * @author dexc
 * 
 */
public class WaterDragon extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(WaterDragon.class);

    /**
	 *
	 */
    private WaterDragon() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new WaterDragon();
    }

    /**
     * 道具物件执行
     * 
     * @param data
     *            参数
     * @param pc
     *            执行者
     * @param item
     *            物件
     */
    @Override
    public void execute(final int[] data, final L1PcInstance pc,
            final L1ItemInstance item) {
        // 例外状况:物件为空
        if (item == null) {
            return;
        }
        // 例外状况:人物为空
        if (pc == null) {
            return;
        }

        int time = L1BuffUtil.cancelDragon(pc);
        if (time != -1) {
            // 1,139：%0 分钟之内无法使用。
            pc.sendPackets(new S_ServerMessage(1139, String.valueOf(time / 60)));
            return;
        }

        pc.getInventory().removeItem(item, 1);
        pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7467));

        // SKILL移转
        final SkillMode mode = L1SkillMode.get().getSkill(L1SkillId.DRAGON3);
        if (mode != null) {
            try {
                mode.start(pc, null, null, 1200);

            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
