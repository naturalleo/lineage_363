package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 欧西里斯初级宝箱碎片(上)49093<br>
 * 欧西里斯初级宝箱碎片(下)49094<br>
 * 欧西里斯高级宝箱碎片(上)49097<br>
 * 欧西里斯高级宝箱碎片(下)49098<br>
 * <br>
 * 库库尔坎初级宝箱碎片(上)49269<br>
 * 库库尔坎初级宝箱碎片(下)49270<br>
 * 库库尔坎高级宝箱碎片(上)49271<br>
 * 库库尔坎高级宝箱碎片(下)49272<br>
 */
public class Valuable_Box_Fragment extends ItemExecutor {

    /**
	 *
	 */
    private Valuable_Box_Fragment() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Valuable_Box_Fragment();
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
        switch (itemId) {
            case 49093: // 欧西里斯初级宝箱碎片：上
                if (pc.getInventory().checkItem(49094, 1)) {
                    pc.getInventory().consumeItem(49093, 1);
                    pc.getInventory().consumeItem(49094, 1);
                    CreateNewItem.createNewItem(pc, 49095, 1);

                } else {
                    pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
                }
                break;

            case 49094: // 欧西里斯初级宝箱碎片：下
                if (pc.getInventory().checkItem(49093, 1)) {
                    pc.getInventory().consumeItem(49093, 1);
                    pc.getInventory().consumeItem(49094, 1);
                    CreateNewItem.createNewItem(pc, 49095, 1);

                } else {
                    pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
                }
                break;

            case 49097: // 欧西里斯高级宝箱碎片：上
                if (pc.getInventory().checkItem(49098, 1)) {
                    pc.getInventory().consumeItem(49097, 1);
                    pc.getInventory().consumeItem(49098, 1);
                    CreateNewItem.createNewItem(pc, 49099, 1);

                } else {
                    pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
                }
                break;

            case 49098: // 欧西里斯高级宝箱碎片：下
                if (pc.getInventory().checkItem(49097, 1)) {
                    pc.getInventory().consumeItem(49097, 1);
                    pc.getInventory().consumeItem(49098, 1);
                    CreateNewItem.createNewItem(pc, 49099, 1);

                } else {
                    pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
                }
                break;

            // //////////////// 库库尔坎/////////////////FIXME
            case 49269: // 库库尔坎初级宝箱碎片(上)
                if (pc.getInventory().checkItem(49270, 1)) {
                    pc.getInventory().consumeItem(49270, 1);
                    pc.getInventory().consumeItem(49269, 1);
                    CreateNewItem.createNewItem(pc, 49274, 1);

                } else {
                    pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
                }
                break;

            case 49270: // 库库尔坎初级宝箱碎片(下)
                if (pc.getInventory().checkItem(49269, 1)) {
                    pc.getInventory().consumeItem(49269, 1);
                    pc.getInventory().consumeItem(49270, 1);
                    CreateNewItem.createNewItem(pc, 49274, 1);

                } else {
                    pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
                }
                break;

            case 49271: // 库库尔坎高级宝箱碎片(上)
                if (pc.getInventory().checkItem(49272, 1)) {
                    pc.getInventory().consumeItem(49272, 1);
                    pc.getInventory().consumeItem(49271, 1);
                    CreateNewItem.createNewItem(pc, 49275, 1);

                } else {
                    pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
                }
                break;

            case 49272: // 库库尔坎高级宝箱碎片(下)
                if (pc.getInventory().checkItem(49271, 1)) {
                    pc.getInventory().consumeItem(49271, 1);
                    pc.getInventory().consumeItem(49272, 1);
                    CreateNewItem.createNewItem(pc, 49275, 1);

                } else {
                    pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
                }
                break;
        }

    }
}
