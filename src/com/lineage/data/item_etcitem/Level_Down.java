package com.lineage.data.item_etcitem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import static com.lineage.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;

/**
 * 洗血药水
 * 
 * @author dexc
 * 
 */
public class Level_Down extends ItemExecutor {
	private static final Log _log = LogFactory.getLog(Level_Down.class);

    /**
	 *
	 */
    private Level_Down() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Level_Down();
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
        if (pc.getLevel() > 9) {
        	pc.setSkillEffect(ABSOLUTE_BARRIER, 1000);
        	pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, true));
        	final long PCEXP = pc.getExp();
        	final int thisMaxHp = pc.getMaxHp();
            pc.setExp(1);// 玩家等级直接变成1级
            pc.sendPackets(new S_ServerMessage(822)); // 你感受到体内深处产生一股不明力量。
            // 删除道具
            //pc.getInventory().removeItem(item, 1);
            try {
            	Thread.sleep(500);
            	if (pc.getInventory().checkItem(44019)) {
                	pc.setExp(PCEXP);// 升回原始等级 hjx1000
                	pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, false));
                	if (thisMaxHp != pc.getMaxHp()) {
                		pc.getInventory().removeItem(item, 1);
                	}
            	}
            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }

        } else {
            pc.sendPackets(new S_ServerMessage(79));// 没有任何事发生
        }
    }

}
