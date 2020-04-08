package com.lineage.data.item_etcitem.html;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 41019 拉斯塔巴德历史书第1页 41020 拉斯塔巴德历史书第2页 41021 拉斯塔巴德历史书第3页 41022 拉斯塔巴德历史书第4页 41023
 * 拉斯塔巴德历史书第5页 41024 拉斯塔巴德历史书第6页 41025 拉斯塔巴德历史书第7页 41026 拉斯塔巴德历史书第8页
 * 
 */
public class Lashistory extends ItemExecutor {

    /**
	 *
	 */
    private Lashistory() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Lashistory();
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

        // 取得道具编号
        final int itemId = item.getItem().getItemId();

        switch (itemId) {
            case 41019:// 拉斯塔巴德历史书第1页
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory1"));
                break;

            case 41020:// 拉斯塔巴德历史书第2页
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory2"));
                break;

            case 41021:// 拉斯塔巴德历史书第3页
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory3"));
                break;

            case 41022:// 拉斯塔巴德历史书第4页
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory4"));
                break;

            case 41023:// 拉斯塔巴德历史书第5页
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory5"));
                break;

            case 41024:// 拉斯塔巴德历史书第6页
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory6"));
                break;

            case 41025:// 拉斯塔巴德历史书第7页
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory7"));
                break;

            case 41026:// 拉斯塔巴德历史书第8页
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory8"));
                break;
        }
    }
}
