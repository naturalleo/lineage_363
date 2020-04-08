package com.lineage.data.item_etcitem;

import static com.lineage.server.model.skill.L1SkillId.AWAKEN_ANTHARAS;
import static com.lineage.server.model.skill.L1SkillId.AWAKEN_FAFURION;
import static com.lineage.server.model.skill.L1SkillId.AWAKEN_VALAKAS;
import static com.lineage.server.model.skill.L1SkillId.SHAPE_CHANGE;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.PolyTable;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * <font color=#00800>变形卷轴 time = 1800</font><BR>
 * Scroll of Polymorph<BR>
 * <font color=#00800>变形卷轴(祝福)time = 2100</font><BR>
 * Scroll of Polymorph<BR>
 * <font color=#00800>象牙塔变身卷轴 time = 1800</font><BR>
 * 
 * @author dexc
 * 
 */
public class Sosc_PolyReel extends ItemExecutor {

    /**
	 *
	 */
    private Sosc_PolyReel() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Sosc_PolyReel();
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
        final String text = pc.getText();

        if (text == null) {// 无字串传回
            return;
        }

        // 清空字串
        pc.setText(null);

        int time = 1800;

        if (item.getBless() == 0) {// 祝福
            time = 2100;
        }

        if (item.getBless() == 128) {// 祝福
            time = 2100;
        }

        final int awakeSkillId = pc.getAwakeSkillId();
        if ((awakeSkillId == AWAKEN_ANTHARAS)
                || (awakeSkillId == AWAKEN_FAFURION)
                || (awakeSkillId == AWAKEN_VALAKAS)) {
            // 1,384：目前状态中无法变身。
            pc.sendPackets(new S_ServerMessage(1384));
            return;
        }

        final L1PolyMorph poly = PolyTable.get().getTemplate(text);

        if ((poly != null) || text.equals("")) {
            if (text.equals("")) {
                pc.removeSkillEffect(SHAPE_CHANGE);

            } else if ((poly.getMinLevel() <= pc.getLevel()) || pc.isGm()) {
                L1PolyMorph.doPoly(pc, poly.getPolyId(), time,
                        L1PolyMorph.MORPH_BY_ITEMMAGIC);
            }
            pc.getInventory().removeItem(item, 1);

        } else {
            // 181 \f1无法变成你指定的怪物。
            pc.sendPackets(new S_ServerMessage(181));
        }
    }
}
