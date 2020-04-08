package com.lineage.data.item_etcitem;

import java.util.Random;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 骰子1阶40325<BR>
 * 骰子3阶40326<BR>
 * 骰子4阶40327<BR>
 * 骰子6阶40328<BR>
 */
public class Dice extends ItemExecutor {

    /**
	 *
	 */
    private Dice() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Dice();
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
        final int itemId = item.getItemId();
        final Random _random = new Random();
        int gfxid = 0;
        switch (itemId) {
            case 40325: // 2种可能
                if (pc.getInventory().checkItem(40318, 1)) {
                    gfxid = 3237 + _random.nextInt(2);

                } else {// 没有任何事情发生。
                    pc.sendPackets(new S_ServerMessage(79));
                }
                break;

            case 40326: // 3种可能
                if (pc.getInventory().checkItem(40318, 1)) {
                    gfxid = 3229 + _random.nextInt(3);

                } else {// 没有任何事情发生。
                    pc.sendPackets(new S_ServerMessage(79));
                }
                break;

            case 40327: // 4种可能
                if (pc.getInventory().checkItem(40318, 1)) {
                    gfxid = 3241 + _random.nextInt(4);

                } else {// 没有任何事情发生。
                    pc.sendPackets(new S_ServerMessage(79));
                }
                break;

            case 40328: // 6种可能
                if (pc.getInventory().checkItem(40318, 1)) {
                    gfxid = 3204 + _random.nextInt(6);

                } else {// 没有任何事情发生。
                    pc.sendPackets(new S_ServerMessage(79));
                }
                break;
        }

        if (gfxid != 0) {
            pc.getInventory().consumeItem(40318, 1);
            pc.sendPacketsAll(new S_SkillSound(pc.getId(), gfxid));
        }
    }
}
