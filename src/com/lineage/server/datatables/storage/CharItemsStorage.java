package com.lineage.server.datatables.storage;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.lineage.server.model.Instance.L1ItemInstance;

/**
 * 人物背包资料
 * 
 * @author dexc
 * 
 */
public interface CharItemsStorage {

    /**
     * 资料预先载入
     */
    public void load();

    /**
     * 传回该人物背包资料
     * 
     * @param objid
     * @return
     */
    public CopyOnWriteArrayList<L1ItemInstance> loadItems(final Integer objid);

    /**
     * 删除人物背包资料
     * 
     * @param objid
     */
    public void delUserItems(final Integer objid);

    /**
     * 该人物背包是否有指定数据
     * 
     * @param pcObjid
     * @param objid
     * @param count
     * @return
     */
    public boolean getUserItems(final Integer pcObjid, final int objid,
            final long count);

    /**
     * 是否有指定数据
     * 
     * @param pcObjid
     * @param objid
     * @param count
     * @return
     */
    public boolean getUserItem(final int objid);

    /**
     * 删除指定编号全部数据
     * 
     * @param itemid
     */
    public void del_item(final int itemid);

    /**
     * 增加背包物品
     * 
     * @param objId
     *            人物OBJID
     * @param item
     *            物品
     * 
     * @throws Exception
     */
    public void storeItem(final int objId, final L1ItemInstance item)
            throws Exception;

    /**
     * 删除背包物品
     * 
     * @param objid
     *            人物OBJID
     * @param item
     *            物品
     * 
     * @throws Exception
     */
    public void deleteItem(final int objid, final L1ItemInstance item)
            throws Exception;

    /**
     * 更新物品ITEMID 与中文名称
     * 
     * @param item
     */
    public void updateItemId_Name(final L1ItemInstance item) throws Exception;

    /**
     * 更新ITEMID
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemId(final L1ItemInstance item) throws Exception;

    /**
     * 更新数量
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemCount(final L1ItemInstance item) throws Exception;

    /**
     * 更新损坏度
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemDurability(final L1ItemInstance item)
            throws Exception;

    /**
     * 更新可用次数
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemChargeCount(final L1ItemInstance item)
            throws Exception;

    /**
     * 更新可用时间
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemRemainingTime(final L1ItemInstance item)
            throws Exception;

    /**
     * 更新强化度
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemEnchantLevel(final L1ItemInstance item)
            throws Exception;

    /**
     * 更新使用状态
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemEquipped(final L1ItemInstance item) throws Exception;

    /**
     * 更新鉴定状态
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemIdentified(final L1ItemInstance item)
            throws Exception;

    /**
     * 更新祝福状态
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemBless(final L1ItemInstance item) throws Exception;

    /**
     * 更新强化属性
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemAttrEnchantKind(final L1ItemInstance item)
            throws Exception;

    /**
     * 更新强化属性强化度
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemAttrEnchantLevel(final L1ItemInstance item)
            throws Exception;

    /**
     * 更新最后使用时间
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemDelayEffect(final L1ItemInstance item)
            throws Exception;

    /**
     * 传回对应所有物品数量
     * 
     * @param objId
     * @return
     * @throws Exception
     */
    public int getItemCount(final int objId) throws Exception;

    /**
     * 给予金币(对离线人物)
     * 
     * @param objId
     * @param count
     * @throws Exception
     */
    public void getAdenaCount(final int objId, final long count)
            throws Exception;

    /**
     * 传回佣有该物品ID的人物清单<BR>
     * (适用该物品每人只能佣有一个的状态)
     * 
     * @param itemid
     * @return
     */
    public Map<Integer, L1ItemInstance> getUserItems(int itemid);
}
