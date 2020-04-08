package com.lineage.data.cmd;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.CharShiftingReading;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_HelpMessage;
import com.lineage.server.serverpackets.S_ItemCount;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.DigitalUtil;
import com.lineage.server.world.World;

/**
 * 给予物件的处理
 * 
 * @author dexc
 * 
 */
public class CreateNewItem {

    private static final Log _log = LogFactory.getLog(CreateNewItem.class);

    /**
     * 删除需要的材料(仅于删除/不会给与)
     * 
     * @param pc
     * @param srcItemIds
     *            删除物品群
     * @param counts
     *            删除物品数量群
     * @param amount
     *            数量
     * @return 是否删除完成 true可以执行 false:有错误
     */
    public static boolean delItems(final L1PcInstance pc,
            final int[] srcItemIds, final int[] counts, final long amount) {
        try {
            if (pc == null) {
                return false;
            }
            if (amount <= 0) {
                return false;
            }
            if (srcItemIds.length <= 0) {
                return false;
            }
            if (counts.length <= 0) {
                return false;
            }
            if (srcItemIds.length != counts.length) {
                _log.error("道具交换物品与数量阵列设置异常!");
                return false;
            }

            // 需要物件数量确认
            for (int i = 0; i < srcItemIds.length; i++) {
                final long itemCount = counts[i] * amount;// 需要的物件数量
                final L1ItemInstance item = pc.getInventory().checkItemX(
                        srcItemIds[i], itemCount);// 需要的物件确认
                if (item == null) {
                    return false;
                }
            }
            // 删除需要物件(材料)
            for (int i = 0; i < srcItemIds.length; i++) {
                final long itemCount1 = counts[i] * amount;// 需要的物件数量 * 换取数量
                // 需要的物件确认
                final L1ItemInstance item = pc.getInventory().checkItemX(
                        srcItemIds[i], itemCount1);
                if (item != null) {
                    pc.getInventory().removeItem(item, itemCount1);// 删除道具

                } else {
                    // 删除失败
                    return false;
                }
            }
            return true;

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    /**
     * 交换物品<BR>
     * amount(指定交换数量)为0时 传送出false以及数量选取网页<BR>
     * amount(指定交换数量)不为0时 传送出true执行物件交换<BR>
     * 这个方法的缺点为选取数量网业输出为原始HTML命令<BR>
     * 如果文字串较长 将会导致封包变大<BR>
     * 
     * @param pc
     *            执行者
     * @param pc
     *            对话NPC
     * @param items
     *            需要物件组
     * @param counts
     *            需要物件数量组
     * @param gitems
     *            给予物件组
     * @param gcounts
     *            给予物件组数量组
     * @param amount
     *            指定交换数量
     * 
     * @return
     */
    public static boolean getItem(final L1PcInstance pc,
            final L1NpcInstance npc, final String cmd, final int[] items,
            final int[] counts, final int[] gitems, final int[] gcounts,
            long amount) {
        // 可制作数量
        final long xcount = checkNewItem(pc, items, counts);
        if (xcount > 0) {
            if (amount == 0) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, cmd));
                return false;

            } else {
                if (xcount >= amount) {
                    // 收回需要物件 给予完成物件
                    createNewItem(pc, items, counts, // 需要
                            gitems, amount, gcounts);// 给予
                }
                return true;
            }
        }
        return true;
    }

    // TODO 换取物品数量预先处理(单一需求物品)

    /**
     * 换取物品数量预先处理(单一需求物品)<BR>
     * 数量不足传回讯息
     * 
     * @param pc
     *            人物
     * @param srcItemId
     *            需要的物件
     * @param count
     *            需要的数量
     * 
     * @return 可换取的数量
     */
    public static long checkNewItem(final L1PcInstance pc, final int srcItemId,
            final int count) {
        try {
            if (pc == null) {
                return -1;
            }

            final L1ItemInstance item = pc.getInventory().findItemIdNoEq(
                    srcItemId);// 需要的物件

            long itemCount = -1;

            if (item != null) {
                itemCount = item.getCount() / count;
            }

            if (itemCount < 1) {
                // 原始物件资料
                final L1Item tgItem = ItemTable.get().getTemplate(srcItemId);
                // 337 \f1%0不足%s。
                pc.sendPackets(new S_ServerMessage(337, tgItem.getNameId()
                        + "(" + (count - (item == null ? 0 : item.getCount()))
                        + ")"));
                return -1;
            }
            return itemCount;

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return -1;
    }

    // TODO 换取物品数量处理(需要复数物件)

    /**
     * 换取物品数量预先处理(需要复数物件)<BR>
     * 检查人物可换取数量<BR>
     * 数量不足传回讯息
     * 
     * @param pc
     *            人物
     * @param srcItemIds
     *            需要的物件(复数)
     * @param counts
     *            需要的数量(复数)
     * 
     * @return 可换取的数量
     */
    public static long checkNewItem(final L1PcInstance pc,
            final int[] srcItemIds, final int[] counts) {
        try {
            if (pc == null) {
                return -1;
            }

            if (srcItemIds.length <= 0) {
                return -1;
            }
            if (counts.length <= 0) {
                return -1;
            }
            if (srcItemIds.length != counts.length) {
                _log.error("道具交换物品与数量阵列设置异常!");
                return -1;
            }

            // 即将输出检查可交换数量的数据
            long[] checkCount = new long[srcItemIds.length];

            boolean error = false;
            

            // 需要物件数量确认
            for (int i = 0; i < srcItemIds.length; i++) {
                int itemid = srcItemIds[i];// 需要的物品
                int count = counts[i];// 需要的数量

                // 人物背包该物件数据
                final L1ItemInstance item = pc.getInventory().findItemIdNoEq(
                        itemid);

                if (item != null) {
                    long itemCount = item.getCount() / count;

                    // 建立该编号物件可交换数量
                    checkCount[i] = itemCount;
                    if (itemCount < 1) {
                        // 337 \f1%0不足%s。
                        pc.sendPackets(new S_ServerMessage(337, item.getName()
                                + "(" + (count - item.getCount()) + ")"));
                        error = true;
                    }

                } else {
                    // 原始物件资料
                    final L1Item tgItem = ItemTable.get().getTemplate(itemid);
                    // 337 \f1%0不足%s。
                    pc.sendPackets(new S_ServerMessage(337, tgItem.getNameId()
                            + "(" + (count) + ")"));
                    error = true;
                }
            }

            // 道具足
            if (!error) {
                long count = DigitalUtil.returnMin(checkCount);
                return count;
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return -1;
    }

    // TODO 换取物品数量处理(需要复数物件/给予复数物件 且 数量大于等于1)

    /**
     * 换取物品数量处理(需要复数物件/给予复数物件 且 数量大于等于1)<BR>
     * 超过容量/重量不予换取<BR>
     * 
     * @param pc
     *            人物
     * @param srcItemIds
     *            需要的物件(复数)
     * @param counts
     *            需要的数量(复数)
     * @param getItemIds
     *            给予的物件编号(复数)
     * @param amount
     *            交换的数量
     * @param getCounts
     *            给予的物件数量(复数)
     */
    public static void createNewItem(final L1PcInstance pc,
            final int[] srcItemIds, final int[] counts, final int[] getItemIds,
            final long amount, final int[] getCounts) {
        try {
            if (pc == null) {
                return;
            }
            if (amount <= 0) {
                return;
            }
            if (srcItemIds.length <= 0) {
                return;
            }
            if (counts.length <= 0) {
                return;
            }
            if (srcItemIds.length != counts.length) {
                _log.error("道具交换物品与数量阵列设置异常!");
                return;
            }
            if (getItemIds.length <= 0) {
                return;
            }
            if (getCounts.length <= 0) {
                return;
            }
            if (getItemIds.length != getCounts.length) {
                _log.error("道具交换物品与数量阵列设置异常!");
                return;
            }
            boolean error = false;

            // 需要物件数量确认
            for (int i = 0; i < srcItemIds.length; i++) {
                final long itemCount = counts[i] * amount;// 需要的物件数量
                final L1ItemInstance item = pc.getInventory().checkItemX(
                        srcItemIds[i], itemCount);// 需要的物件确认
                if (item == null) {
                    error = true;
                }
            }

            if (!error) {
                for (int i = 0; i < getItemIds.length; i++) {
                    if (!getItemIsOk(pc, getItemIds[i], amount, getCounts[i])) {
                        error = true;
                    }
                }
            }

            if (!error) {
                // 删除需要物件(材料)
                for (int i = 0; i < srcItemIds.length; i++) {
                    final long itemCount1 = counts[i] * amount;// 需要的物件数量 * 换取数量
                    // 需要的物件确认
                    final L1ItemInstance item = pc.getInventory().checkItemX(
                            srcItemIds[i], itemCount1);
                    if (item != null) {
                        pc.getInventory().removeItem(item, itemCount1);// 删除道具

                    } else {
                        // 删除失败
                        error = true;
                    }
                }
            }

            if (!error) {
                // 给予物品
                for (int i = 0; i < getItemIds.length; i++) {
                    getItemIs(pc, getItemIds[i], amount, getCounts[i]);
                }
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 换取物品数量后处理(需要复数物件/给予的物件数量大于1)<BR>
     * 检查 人物 容量/重量<BR>
     * 
     * @param pc
     *            人物
     * @param getItemId
     *            给予的物件编号
     * @param amount
     *            交换的数量
     * @param getCount
     *            给予的物件数量
     * 
     * @return true:允许加入物品 false:不允许加入物品
     */
    private static boolean getItemIsOk(final L1PcInstance pc,
            final int getItemId, final long amount, final int getCount) {
        try {
            if (pc == null) {
                return false;
            }
            // 产容量/重量比对物件
            final L1Item tgItem = ItemTable.get().getTemplate(getItemId);

            // 增加物品是否成功
            if (pc.getInventory().checkAddItem(tgItem, amount * getCount) != L1Inventory.OK) {
                return false;
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return true;
    }

    /**
     * 换取物品数量后处理(需要复数物件/给予的物件数量大于1)<BR>
     * 给予物品
     * 
     * @param pc
     *            人物
     * @param getItemId
     *            给予的物件编号
     * @param amount
     *            交换的数量
     * @param getCount
     *            给予的物件数量
     */
    private static void getItemIs(final L1PcInstance pc, final int getItemId,
            final long amount, final int getCount) {
        try {
            if (pc == null) {
                return;
            }
            // 原始物件资料
            final L1Item tgItem = ItemTable.get().getTemplate(getItemId);
            // 给予物品可以堆叠
            if (tgItem.isStackable()) {
                final L1ItemInstance tgItemX = ItemTable.get().createItem(
                        getItemId);
                tgItemX.setCount(amount * getCount);// 设置给予物品数量
                createNewItem(pc, tgItemX);// 给予物品

            } else {
                for (int get = 0; get < amount * getCount; get++) {
                    final L1ItemInstance tgItemX = ItemTable.get().createItem(
                            getItemId);
                    tgItemX.setCount(1);// 设置给予物品数量
                    createNewItem(pc, tgItemX);// 给予物品
                }
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 换取物品后处理(给予的物件数量大于1)<BR>
     * 超过容量/重量不予换取<BR>
     * 
     * @param pc
     *            人物
     * @param srcItemId
     *            需要的物件
     * @param count
     *            需要的数量
     * @param getItemId
     *            给予的物件
     * @param getCount
     *            给予的物件数量
     */
    public static void createNewItem(final L1PcInstance pc,
            final int srcItemId, final int count, final int getItemId,
            final int getCount) {
        createNewItem(pc, srcItemId, count, getItemId, 1, getCount);
    }

    /**
     * 换取物品后处理(给予的物件数量大于1)<BR>
     * 超过容量/重量不予换取<BR>
     * 
     * @param pc
     *            人物
     * @param srcItemId
     *            需要的物件
     * @param count
     *            需要的数量
     * @param getItemId
     *            给予的物件
     * @param amount
     *            交换的数量
     * @param getCount
     *            给予的物件数量
     */
    public static void createNewItem(final L1PcInstance pc,
            final int srcItemId, final int count, final int getItemId,
            final long amount, final int getCount) {
        final long itemCount1 = count * amount;// 需要的物件数量
        final L1ItemInstance item1 = pc.getInventory().checkItemX(srcItemId,
                itemCount1);// 需要的物件确认
        if (item1 != null) {
            // 产生新物件
            final L1ItemInstance tgItem = ItemTable.get().createItem(getItemId);

            // 增加物品是否成功
            if (pc.getInventory().checkAddItem(tgItem, amount * getCount) == L1Inventory.OK) {
                pc.getInventory().removeItem(item1, itemCount1);// 删除道具
                // 物品可以堆叠
                if (tgItem.isStackable()) {
                    tgItem.setCount(amount * getCount);// 设置给予物品数量
                    createNewItem(pc, tgItem);// 给予物品

                } else {
                    for (int get = 0; get < amount * getCount; get++) {
                        final L1ItemInstance tgItemX = ItemTable.get()
                                .createItem(getItemId);
                        tgItemX.setCount(1);// 设置给予物品数量
                        createNewItem(pc, tgItemX);// 给予物品
                    }
                }

            } else {
                // 移出世界
                World.get().removeObject(tgItem);
            }
        }
    }

    /**
     * 换取物品数量后处理(需要数量为1, 给予数量为1)<BR>
     * 超过容量/重量不予换取
     * 
     * @param pc
     *            人物
     * @param srcItemId
     *            需要的物件
     * @param count
     *            需要的数量
     * @param getItemId
     *            给予的物件
     * @param amount
     *            交换的数量
     */
    public static void createNewItem(final L1PcInstance pc,
            final int srcItemId, final int count, final int getItemId,
            final long amount) {
        final long itemCount1 = count * amount;// 需要的物件数量
        final L1ItemInstance item1 = pc.getInventory().checkItemX(srcItemId,
                itemCount1);// 需要的物件确认
        if (item1 != null) {
            // 产生新物件
            final L1ItemInstance tgItem = ItemTable.get().createItem(getItemId);

            // 增加物品是否成功
            if (pc.getInventory().checkAddItem(tgItem, amount) == L1Inventory.OK) {
                pc.getInventory().removeItem(item1, itemCount1);// 删除道具
                // 物品可以堆叠
                if (tgItem.isStackable()) {
                    tgItem.setCount(amount);// 设置给予物品数量
                    createNewItem(pc, tgItem);// 给予物品

                } else {
                    for (int get = 0; get < amount; get++) {
                        final L1ItemInstance tgItemX = ItemTable.get()
                                .createItem(getItemId);
                        tgItemX.setCount(1);// 设置给予物品数量
                        createNewItem(pc, tgItemX);// 给予物品
                    }
                }
            }
        }
    }

    /**
     * 给予物件的处理(物件已加入世界)
     * 
     * @param pc
     *            执行人物
     * @param item
     *            物件
     * @return
     */
    public static void createNewItem(final L1PcInstance pc,
            final L1ItemInstance item) {
        try {
            if (pc == null) {
                return;
            }
            if (item == null) {
                return;
            }
            pc.getInventory().storeItem(item);
            pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // 获得0%。

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 给予物件的处理(物件已加入世界)<BR>
     * 超出数量掉落地面
     * 
     * @param pc
     * @param item
     * @param count
     */
    public static void createNewItem(final L1PcInstance pc,
            final L1ItemInstance item, final long count) {
        try {
            if (pc == null) {
                return;
            }
            if (item == null) {
                return;
            }
            item.setCount(count);

            if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
                pc.getInventory().storeItem(item);

            } else {
                item.set_showId(pc.get_showId());
                // 掉落地面
                World.get().getInventory(pc.getX(), pc.getY(), pc.getMapId())
                        .storeItem(item);
            }

            pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // 获得0%。

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 给予物件的处理(超出容量到落地面)<BR>
     * 只能给予可堆叠物品<BR>
     * 不能堆叠限定只能给一个<BR>
     * 
     * @param pc
     *            执行人物
     * @param item_id
     *            物件编号
     * @param count
     *            数量
     * @return
     */
    public static boolean createNewItem(final L1PcInstance pc,
            final int item_id, final long count) {
        try {
            if (pc == null) {
                return false;
            }
            // 产生新物件
            final L1ItemInstance item = ItemTable.get().createItem(item_id);
            if (item != null) {
                item.setCount(count);
                if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
                    pc.getInventory().storeItem(item);

                } else {
                    item.set_showId(pc.get_showId());
                    // 掉落地面
                    World.get()
                            .getInventory(pc.getX(), pc.getY(), pc.getMapId())
                            .storeItem(item);
                }
                pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // 获得0%。
                return true;

            } else {
                _log.error("给予物件失败 原因: 指定编号物品不存在(" + item_id + ")");
                return false;
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    /**
     * 给予任务物件的处理(超出容量到落地面)<BR>
     * 只能给予可堆叠物品<BR>
     * 不能堆叠限定只能给一个<BR>
     * 
     * @param atk
     *            给予的对象
     * @param npc
     *            给予物品的NPC
     * @param item_id
     *            物件编号
     * @param count
     *            数量
     */
    public static void getQuestItem(final L1Character atk,
            final L1NpcInstance npc, final int item_id, final long count) {
        try {
            if (atk == null) {
                return;
            }
            // 产生新物件
            final L1ItemInstance item = ItemTable.get().createItem(item_id);
            if (item != null) {
                item.setCount(count);
                if (atk.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
                    atk.getInventory().storeItem(item);

                } else {
                    item.set_showId(atk.get_showId());
                    // 掉落地面
                    World.get()
                            .getInventory(atk.getX(), atk.getY(),
                                    atk.getMapId()).storeItem(item);
                }

                if (atk instanceof L1PcInstance) {
                    final L1PcInstance pc = (L1PcInstance) atk;
                    if (npc != null) {
                        // \f1%0%s 给你 %1%o 。
                        pc.sendPackets(new S_HelpMessage("\\fW"
                                + npc.getNameId() + "给你" + item.getLogName()));
                    } else {
                        // \f1%0%s 给你 %1%o 。
                        pc.sendPackets(new S_HelpMessage("\\fW" + "给你", item
                                .getLogName()));
                    }
                }

            } else {
                _log.error("给予物件失败 原因: 指定编号物品不存在(" + item_id + ")");
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    // TODO 防具武器 升级/交换 (保留原始效果)

    /**
     * 防具武器 升级/交换 (保留原始效果)
     * 
     * @param pc
     *            执行人物
     * @param srcItem
     *            原始物件
     * @param newItem
     *            新物件
     * @param enchant
     *            强化质大于设定降低强化质
     * @param down
     *            降低的数量
     * @param mode
     *            模式 0: 交换装备 1: 装备升级 2: 转移装备
     */
    public static void updateA(final L1PcInstance pc,
            final L1ItemInstance srcItem, final L1ItemInstance newItem,
            final int enchant, final int down, final int mode) {
        try {
            if (pc == null) {
                return;
            }
            if (srcItem == null) {
                return;
            }
            if (newItem == null) {
                return;
            }
            newItem.setCount(1);
            // 强化质大于0
            if (srcItem.getEnchantLevel() > enchant) {
                newItem.setEnchantLevel(srcItem.getEnchantLevel() - down);

            } else {
                newItem.setEnchantLevel(srcItem.getEnchantLevel());
            }

            newItem.setAttrEnchantKind(srcItem.getAttrEnchantKind());
            newItem.setAttrEnchantLevel(srcItem.getAttrEnchantLevel());
            newItem.setIdentified(true);

            final int srcObjid = srcItem.getId();
            final L1Item srcItemX = srcItem.getItem();
            // 删除原始物件
            if (pc.getInventory().removeItem(srcItem) == 1) {
                // 给予物品
                pc.getInventory().storeItem(newItem);

                pc.sendPackets(new S_ServerMessage(403, newItem.getLogName())); // 获得0%。

                // 建立纪录
                CharShiftingReading.get().newShifting(pc, 0, null, srcObjid,
                        srcItemX, newItem, mode);
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    // TODO 防具武器 升级/交换 (不保留原始效果)

    /**
     * 防具武器升级/交换 (不保留原始效果)
     * 
     * @param pc
     *            执行人物
     * @param srcItem
     *            原始物件
     * @param newid
     *            新物件编号
     */
    public static void updateB(final L1PcInstance pc,
            final L1ItemInstance srcItem, final int newid) {
        try {
            if (pc == null) {
                return;
            }
            if (srcItem == null) {
                return;
            }

            final L1ItemInstance newItem = ItemTable.get().createItem(newid);
            if (newItem != null) {
                // 删除原始物件
                if (pc.getInventory().removeItem(srcItem) == 1) {
                    // 给予物品
                    pc.getInventory().storeItem(newItem);
                }

            } else {
                _log.error("给予物件失败 原因: 指定编号物品不存在(" + newid + ")");
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
