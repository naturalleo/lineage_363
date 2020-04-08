package com.lineage.data.item_etcitem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 返生药水43000
 * 
 */
public class Reactivating_Potion extends ItemExecutor {

    private static final Log _log = LogFactory
            .getLog(Reactivating_Potion.class);

    /**
	 *
	 */
    private Reactivating_Potion() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Reactivating_Potion();
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
        final int pcObjid = pc.getId();
        pc.setExp(1);
        pc.resetLevel();
        pc.setBonusStats(0);

        // 使用后能力重算，HP MP保留10%(新增 YiWei Franky(UID: 4534))
        pc.resetBaseAc();
        pc.resetBaseMr();
        pc.resetBaseHitup();
        pc.resetBaseDmgup();

        final int randomHp = pc.getMaxHp() - ((pc.getMaxHp() * 10) / 100);
        final int randomMp = pc.getMaxMp() - ((pc.getMaxMp() * 10) / 100);
        pc.addBaseMaxHp((short) -randomHp);
        pc.addBaseMaxMp((short) -randomMp);

        pc.setCurrentHp(pc.getMaxHp());
        pc.setCurrentMp(pc.getMaxMp());
        // 使用后能力重算，HP MP保留10%

        pc.sendPacketsX8(new S_SkillSound(pcObjid, 191));
        pc.sendPackets(new S_OwnCharStatus(pc));
        pc.getInventory().removeItem(item, 1);
        pc.sendPackets(new S_ServerMessage(822)); // 你感受到体内深处产生一股不明力量。
        try {
            pc.save();
        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
