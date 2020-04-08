package com.lineage.data.item_etcitem.brave;

import static com.lineage.server.model.skill.L1SkillId.BLOODLUST;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillBrave;

/**
 * 生命之树果实49158<br>
 */
public class Potion_5 extends ItemExecutor {

    /**
	 *
	 */
    private Potion_5() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Potion_5();
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

        // 血之渴望
        if (pc.hasSkillEffect(BLOODLUST)) {
            // 1,413：目前情况是无法使用。
            pc.sendPackets(new S_ServerMessage(1413));
            return;
        }

        if (L1BuffUtil.stopPotion(pc)) {
        	//与官方同步龙骑不可以再用果实 hjx1000
            /*if (pc.isDragonKnight()) {
                this.useBravePotion(pc);

            } else*/ if (pc.isIllusionist()) {
                this.useBravePotion(pc);

            } else { // \f1没有任何事情发生。
                pc.sendPackets(new S_ServerMessage(79));
            }
            pc.getInventory().removeItem(item, 1);
        }
    }

    private void useBravePotion(final L1PcInstance pc) {
        // 解除魔法技能绝对屏障
        L1BuffUtil.cancelAbsoluteBarrier(pc);

        // 勇敢效果 抵销对应技能
        L1BuffUtil.braveStart(pc);

        int time = 480;

        pc.sendPacketsAll(new S_SkillBrave(pc.getId(), 4, 0));
        //pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7110));
        pc.setBraveSpeed(4);
        pc.sendPackets(new S_SkillBrave(pc.getId(), 4,
        		time));

        pc.setSkillEffect(L1SkillId.WIND_WALK, time * 1000);
    }
}
