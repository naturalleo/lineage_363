package com.lineage.data.item_etcitem.quest;

import java.util.Random;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.ActionCodes;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * YiWei积分石
 * 
 * @author dexc
 * 
 */
public class ScoreItem1 extends ItemExecutor {

    private final Random _random = new Random();

    /**
	 *
	 */
    private ScoreItem1() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new ScoreItem1();
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
        final int srcItemId = 44049;// 材料道具编号(积分石)
        final int count = 100;// 耗用数量
        boolean isError = true;

        if (pc.getMapId() == 4) {
            int x = pc.getX();
            int y = pc.getY();

            if ((x > 34026) && (x < 34080) && (y > 32235) && (y < 32314)) {
                final L1ItemInstance itemX = pc.getInventory().checkItemX(
                        srcItemId, count);
                if (itemX != null) {
                    // 送出动作封包
                    pc.sendPacketsAll(new S_SkillSound(pc.getId(), 2944));// 2944
                    pc.sendPacketsAll(new S_DoActionGFX(pc.getId(),
                            ActionCodes.ACTION_SkillBuff));

                    pc.getInventory().removeItem(itemX, count);// 删除道具

                    if (_random.nextInt(100) < 95) {
                        CreateNewItem.createNewItem(pc, 41223, 1);// 高品质
                                                                  // YiWei积分石

                    } else {
                        // 280：\f1施咒失败。
                        pc.sendPackets(new S_ServerMessage(280));
                    }
                    isError = false;
                }
            }
        }

        if (isError) {
            // 没有任何事情发生
            pc.sendPackets(new S_ServerMessage(79));
        }
    }
}
