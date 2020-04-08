package com.lineage.data.item_etcitem.shop;

import static com.lineage.server.model.skill.L1SkillId.AWAKEN_ANTHARAS;
import static com.lineage.server.model.skill.L1SkillId.AWAKEN_FAFURION;
import static com.lineage.server.model.skill.L1SkillId.AWAKEN_VALAKAS;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 自定义变身卷轴 classname: shop.PolyUserSet 设置范例: shop.PolyUserSet 1080 1800 变身代号
 * 时间(秒) 等级使用限制设置在资料表中
 * 
 * @author dexc
 * 
 */
public class PolyUserSet extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(PolyUserSet.class);

    /**
	 *
	 */
    private PolyUserSet() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new PolyUserSet();
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
        final int awakeSkillId = pc.getAwakeSkillId();
        if ((awakeSkillId == AWAKEN_ANTHARAS)
                || (awakeSkillId == AWAKEN_FAFURION)
                || (awakeSkillId == AWAKEN_VALAKAS)) {
            pc.sendPackets(new S_ServerMessage(1384)); // 目前状态中无法变身。
            return;
        }
        if (_polyid == -1) {
            final int itemId = item.getItemId();
            _log.error("自定义变身卷轴 设定错误: " + itemId + " 没有变身代号!");
            return;
        }
        pc.getInventory().removeItem(item, 1);
        L1PolyMorph.doPoly(pc, _polyid, _time, L1PolyMorph.MORPH_BY_ITEMMAGIC);
    }

    private int _polyid = -1;
    private int _time = 1800;

    @Override
    public void set_set(String[] set) {
        try {
            _polyid = Integer.parseInt(set[1]);

        } catch (Exception e) {
        }
        try {
            _time = Integer.parseInt(set[2]);

        } catch (Exception e) {
        }
    }
}
