package com.lineage.data.item_etcitem.dragon;

import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.skill.L1SkillMode;
import com.lineage.server.model.skill.skillmode.SkillMode;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * <font color=#00800>42518 风龙之魔眼</font><BR>
 * 效果时间： 600秒，使用间隔1小时，无限制使用次数 不可转移 魔法暴击率+1 睡眠耐性+3
 * 
 * @author dexc
 * 
 */
public class Eye_Lindvior extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(Eye_Lindvior.class);

    /**
	 *
	 */
    private Eye_Lindvior() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Eye_Lindvior();
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
        try {
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
                pc.sendPackets(new S_ServerMessage(1139, String
                        .valueOf(time / 60)));
                return;
            }

            // pc.getInventory().removeItem(item, 1);
            pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7673));
            // 设置延迟使用机制
            final Timestamp ts = new Timestamp(System.currentTimeMillis());
            item.setLastUsed(ts);
            pc.getInventory().updateItem(item, L1PcInventory.COL_DELAY_EFFECT);
            pc.getInventory().saveItem(item, L1PcInventory.COL_DELAY_EFFECT);

            // SKILL移转
            final SkillMode mode = L1SkillMode.get()
                    .getSkill(L1SkillId.DRAGON4);
            if (mode != null) {
                mode.start(pc, null, null, 600);
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
