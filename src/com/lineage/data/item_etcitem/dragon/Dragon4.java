package com.lineage.data.item_etcitem.dragon;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * <font color=#00800>4龙物品</font><BR>
 * 
 * @author dexc
 * 
 */
public class Dragon4 extends ItemExecutor {

    /**
	 *
	 */
    private Dragon4() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Dragon4();
    }

    private final int[] _items1 = new int[] { 40341,// 安塔瑞斯之鳞
            40342,// 安塔瑞斯之爪
            40343,// 安塔瑞斯之眼
            40344,// 安塔瑞斯之血
            40345,// 安塔瑞斯之肉
            40346,// 安塔瑞斯之心
            40347,// 安塔瑞斯之骨
            40348,// 安塔瑞斯之牙

    };

    private final int[] _items2 = new int[] { 40349,// 巴拉卡斯之鳞
            40350,// 巴拉卡斯之爪
            40351,// 巴拉卡斯之眼
            40352,// 巴拉卡斯之肉
            40353,// 巴拉卡斯之血
            40354,// 巴拉卡斯之心
            40355,// 巴拉卡斯之骨
            40356,// 巴拉卡斯之牙
    };

    private final int[] _items3 = new int[] { 40357,// 法利昂之鳞
            40358,// 法利昂之爪
            40359,// 法利昂之眼
            40360,// 法利昂之肉
            40361,// 法利昂之血
            40362,// 法利昂之心
            40363,// 法利昂之骨
            40364,// 法利昂之牙
    };

    private final int[] _items4 = new int[] { 40365,// 林德拜尔之鳞
            40366,// 林德拜尔之爪
            40367,// 林德拜尔之眼
            40368,// 林德拜尔之血
            40369,// 林德拜尔之肉
            40370,// 林德拜尔之心
            40371,// 林德拜尔之骨
            40372,// 林德拜尔之牙
    };

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
        switch (item.getItemId()) {
            case 40341:// 安塔瑞斯之鳞
            case 40342:// 安塔瑞斯之爪
            case 40343:// 安塔瑞斯之眼
            case 40344:// 安塔瑞斯之血
            case 40345:// 安塔瑞斯之肉
            case 40346:// 安塔瑞斯之心
            case 40347:// 安塔瑞斯之骨
            case 40348:// 安塔瑞斯之牙
                if (allItem(pc)) {
                    delAll(pc, null, 49500);// 龙 结晶
                    return;

                } else {
                    if (allItem(pc, _items1)) {
                        delAll(pc, _items1, 49504);// 地属性 结晶
                        return;
                    }
                }
                break;

            case 40349:// 巴拉卡斯之鳞
            case 40350:// 巴拉卡斯之爪
            case 40351:// 巴拉卡斯之眼
            case 40352:// 巴拉卡斯之肉
            case 40353:// 巴拉卡斯之血
            case 40354:// 巴拉卡斯之心
            case 40355:// 巴拉卡斯之骨
            case 40356:// 巴拉卡斯之牙
                if (allItem(pc)) {
                    delAll(pc, null, 49500);// 龙 结晶
                    return;

                } else {
                    if (allItem(pc, _items2)) {
                        delAll(pc, _items2, 49501);// 火属性 结晶
                        return;
                    }
                }
                break;

            case 40357:// 法利昂之鳞
            case 40358:// 法利昂之爪
            case 40359:// 法利昂之眼
            case 40360:// 法利昂之肉
            case 40361:// 法利昂之血
            case 40362:// 法利昂之心
            case 40363:// 法利昂之骨
            case 40364:// 法利昂之牙
                if (allItem(pc)) {
                    delAll(pc, null, 49500);// 龙 结晶
                    return;

                } else {
                    if (allItem(pc, _items3)) {
                        delAll(pc, _items3, 49502);// 水属性 结晶
                        return;
                    }
                }
                break;

            case 40365:// 林德拜尔之鳞
            case 40366:// 林德拜尔之爪
            case 40367:// 林德拜尔之眼
            case 40368:// 林德拜尔之血
            case 40369:// 林德拜尔之肉
            case 40370:// 林德拜尔之心
            case 40371:// 林德拜尔之骨
            case 40372:// 林德拜尔之牙
                if (allItem(pc)) {
                    delAll(pc, null, 49500);// 龙 结晶
                    return;

                } else {
                    if (allItem(pc, _items4)) {
                        delAll(pc, _items4, 49503);// 风属性 结晶
                        return;
                    }
                }
                break;
        }
        // 无法使用
        pc.sendPackets(new S_ServerMessage(74, item.getLogName()));
    }

    /**
     * 给予指定物件
     * 
     * @param pc
     *            执行人物
     * @param items
     *            物件组
     * @param mode
     *            给予物件的编号
     */
    private void delAll(L1PcInstance pc, int[] items, int mode) {
        boolean isError = false;
        if (items == null) {
            for (int itemid : _items1) {
                if (!pc.getInventory().consumeItem(itemid, 1)) {
                    isError = true;
                }
            }
            for (int itemid : _items2) {
                if (!pc.getInventory().consumeItem(itemid, 1)) {
                    isError = true;
                }
            }
            for (int itemid : _items3) {
                if (!pc.getInventory().consumeItem(itemid, 1)) {
                    isError = true;
                }
            }
            for (int itemid : _items4) {
                if (!pc.getInventory().consumeItem(itemid, 1)) {
                    isError = true;
                }
            }

        } else {
            for (int itemid : items) {
                if (!pc.getInventory().consumeItem(itemid, 1)) {
                    isError = true;
                }
            }
        }
        if (isError) {
            pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生

        } else {
            // 取得道具
            CreateNewItem.createNewItem(pc, mode, 1);
        }
    }

    /**
     * 全物件检查
     * 
     * @param pc
     * @return
     */
    private boolean allItem(L1PcInstance pc) {
        int check = 0;

        for (int itemid : _items1) {
            if (pc.getInventory().checkItem(itemid)) {
                check++;
            }
        }
        for (int itemid : _items2) {
            if (pc.getInventory().checkItem(itemid)) {
                check++;
            }
        }
        for (int itemid : _items3) {
            if (pc.getInventory().checkItem(itemid)) {
                check++;
            }
        }
        for (int itemid : _items4) {
            if (pc.getInventory().checkItem(itemid)) {
                check++;
            }
        }
        // System.out.println("checkAll: " + check);
        if (check == 32) {
            return true;

        } else {
            return false;
        }
    }

    /**
     * 指定物件检查
     * 
     * @param pc
     * @return
     */
    private boolean allItem(L1PcInstance pc, int[] items) {
        int check = 0;

        for (int itemid : items) {
            if (pc.getInventory().checkItem(itemid)) {
                check++;
            }
        }

        // System.out.println("check: " + check);
        if (check == 8) {
            return true;

        } else {
            return false;
        }
    }
}
