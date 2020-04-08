package com.lineage.server.clientpackets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.event.ShopXSet;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.ItemRestrictionsTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.ItemUpdateTable;
import com.lineage.server.datatables.ShopTable;
import com.lineage.server.datatables.ShopXTable;
import com.lineage.server.datatables.lock.CharItemsReading;
import com.lineage.server.datatables.lock.DwarfForClanReading;
import com.lineage.server.datatables.lock.DwarfReading;
import com.lineage.server.datatables.lock.OtherUserBuyReading;
import com.lineage.server.datatables.lock.OtherUserSellReading;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1CnInstance;
import com.lineage.server.model.Instance.L1DwarfInstance;
import com.lineage.server.model.Instance.L1GamblingInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MerchantInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.shop.L1Shop;
import com.lineage.server.model.shop.L1ShopBuyOrderList;
import com.lineage.server.model.shop.L1ShopSellOrderList;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_CnsSell;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1ItemUpdate;
import com.lineage.server.templates.L1PrivateShopBuyList;
import com.lineage.server.templates.L1PrivateShopSellList;
import com.lineage.server.timecontroller.event.GamblingTime;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;

/**
 * 要求列表物品取得
 * 
 * @author dexc
 * 
 */
public class C_Result extends ClientBasePacket {

    public static final Log _log = LogFactory.getLog(C_Result.class);

    public static final Random _random = new Random();

    /*
     * public C_Result() { }
     * 
     * public C_Result(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final L1PcInstance pc = client.getActiveChar();

            if (pc.isGhost()) { // 鬼魂模式
                return;
            }

            if (pc.isDead()) { // 死亡
                return;
            }

            if (pc.isTeleport()) { // 传送中
                return;
            }

            if (pc.isPrivateShop()) { // 商店村模式
                return;
            }

            final int npcObjectId = this.readD();
            final int resultType = this.readC();
            final int size = this.readC();
            final int unknown = this.readC();

            /*
             * System.out.println(npcObjectId); System.out.println(resultType);
             * System.out.println(size); System.out.println(unknown);
             */

            int npcId = 0;
            // String npcImpl = "";
            // String nameid = null;
            boolean isPrivateShop = false;

            final L1Object findObject = World.get().findObject(npcObjectId);
            if (findObject != null) {
                boolean isStop = false;
                // 对象是NPC
                if (findObject instanceof L1NpcInstance) {
                    final L1NpcInstance targetNpc = (L1NpcInstance) findObject;
                    npcId = targetNpc.getNpcTemplate().get_npcId();
                    isStop = true;

                    // 对象是PC
                } else if (findObject instanceof L1PcInstance) {
                    isPrivateShop = true;
                    isStop = true;
                }

                if (isStop) {
                    final int diffLocX = Math
                            .abs(pc.getX() - findObject.getX());
                    final int diffLocY = Math
                            .abs(pc.getY() - findObject.getY());
                    // 距离3格以上无效
                    if ((diffLocX > 3) || (diffLocY > 3)) {
                        return;
                    }
                }
            }
            //System.out.println("isPrivateShop:"+isPrivateShop);

            switch (resultType) {
                case 0:// 买入物品
                    if (size > 0) {
                        if (findObject instanceof L1MerchantInstance) {
                            switch (npcId) {
                                case 70535:// 托售管理员
                                    this.mode_shopS(pc, size);
                                    break;

                                default:
                                    this.mode_buy(pc, npcId, size);
                                    break;
                            }
                            return;
                        }

                        if (findObject instanceof L1GamblingInstance) {
                            this.mode_gambling(pc, npcId, size, true);
                            return;
                        }

                        if (findObject instanceof L1CnInstance) {
                            this.mode_cn(pc, size, true);
                            return;
                        }

                        if (pc.equals(findObject)) {// 是自己
                            this.mode_cn(pc, size, true);
                            return;
                        }

                        if (findObject instanceof L1PcInstance) {
                            if (isPrivateShop) {// 买入个人商店物品
                                final L1PcInstance targetPc = (L1PcInstance) findObject;
                                this.mode_buypc(pc, targetPc, size);
                                return;
                            }
                        }
                    }
                    break;

                case 1:// 卖出物品
                    if (size > 0) {
                        if (findObject instanceof L1MerchantInstance) {
                            switch (npcId) {
                                case 99999:// 亚丁商团(垃圾回收)
                                    this.mode_sellall(pc, size);
                                    break;

                                default:
                                    this.mode_sell(pc, npcId, size);
                                    break;
                            }
                            return;
                        }

                        if (findObject instanceof L1GamblingInstance) {
                            this.mode_gambling(pc, npcId, size, false);
                            return;
                        }

                        if (findObject instanceof L1PcInstance) {// 卖出物品给个人商店
                            if (isPrivateShop) {
                                final L1PcInstance targetPc = (L1PcInstance) findObject;
                                this.mode_sellpc(pc, targetPc, size);
                            }
                        }
                    }
                    break;

                case 2:// 个人仓库存入
                    if (size > 0) {
                        if (findObject instanceof L1DwarfInstance) {
                            final int level = pc.getLevel();
                            if (level >= 5) {
                                this.mode_warehouse_in(pc, npcId, size);
                            }
                        }
                    }
                    break;

                case 3:// 个人仓库取出
                    if (size > 0) {
                        if (findObject instanceof L1DwarfInstance) {
                            final int level = pc.getLevel();
                            if (level >= 5) {
                                this.mode_warehouse_out(pc, npcId, size);
                            }
                        }
                    }
                    break;

                case 4:// 血盟仓库存入
                    if (size > 0) {
                        if (findObject instanceof L1DwarfInstance) {
                            final int level = pc.getLevel();
                            if (level >= 5) {
                                this.mode_warehouse_clan_in(pc, npcId, size);
                            }
                        }
                    }
                    break;

                case 5:// 血盟仓库取出
                    if (size > 0) {
                        if (findObject instanceof L1DwarfInstance) {
                            final int level = pc.getLevel();
                            if (level >= 5) {
                                this.mode_warehouse_clan_out(pc, npcId, size);

                            } else {// 血盟仓库取出中 Cancel、ESC
                                final L1Clan clan = WorldClan.get().getClan(
                                        pc.getClanname());
                                if (clan != null) {
                                    clan.setWarehouseUsingChar(0); // 血盟仓库使用解除
                                }
                            }
                        }
                    }
                    break;

                case 6:// 无传递封包
                    break;

                case 7:// 无传递封包
                    break;

                case 8:// 精灵仓库存入
                    if (size > 0 && pc.isGm()) { //关闭玩家存入妖精仓库 hjx1000 
                        if (findObject instanceof L1DwarfInstance) {
                            final int level = pc.getLevel();
                            if ((level >= 5) && pc.isElf()) {
                                this.mode_warehouse_elf_in(pc, npcId, size);
                            }
                        }
                    }
                    break;

                case 9:// 精灵仓库取出
                    if (size > 0) {
                        if (findObject instanceof L1DwarfInstance) {
                            final int level = pc.getLevel();
                            if ((level >= 0) /*&& pc.isElf()*/) { //不限制等级和职业领取妖精仓库 hjx1000
                                this.mode_warehouse_elf_out(pc, npcId, size);
                            }
                        }
                    }
                    break;

                case 10:// 物品强化
                    if (size > 0) {
                        switch (npcId) {
                            case 91141:// 物品升级专员
                            case 91142:// 物品升级专员
                            case 91143:// 物品升级专员
                                mode_update_item(pc, size, npcObjectId);
                                break;

                            default:
                                break;
                        }
                    }
                    break;

                case 11:// 无传递封包
                    break;

                case 12:// 提炼武器/防具
                    if (size > 0) {
                        switch (npcId) {
                            case 70535:// 托售管理员
                                this.mode_shop_item(pc, size, npcObjectId);
                                break;
                        }
                    }
                    break;
            }

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    private void mode_update_item(L1PcInstance pc, int size, int npcObjectId) {
        try {
            if (size != 1) {
                pc.sendPackets(new S_ServerMessage("\\fR你只能选取一样装备用来升级。"));
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }

            final int orderId = this.readD();
            final int count = Math.max(0, this.readD());// 数量
            if (count != 1) {
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }

            final L1ItemInstance item = pc.getInventory().getItem(orderId);
            final ArrayList<L1ItemUpdate> items = ItemUpdateTable.get().get(
                    item.getItemId());
            final String[] names = new String[items.size()];
            for (int index = 0; index < items.size(); index++) {
                int toid = items.get(index).get_toid();
                final L1Item tgitem = ItemTable.get().getTemplate(toid);
                if (tgitem != null) {
                    names[index] = tgitem.getName();
                }
            }

            pc.set_mode_id(orderId);
            pc.sendPackets(new S_NPCTalkReturn(npcObjectId,
                    L1ItemUpdate._html_02, names));

        } catch (Exception e) {
            _log.error("升级装备物品数据异常: " + pc.getName());
        }
    }

    /**
     * 托售管理员(购买物品)
     * 
     * @param pc
     * @param size
     */
    private void mode_shopS(L1PcInstance pc, int size) {
        try {
            final Map<Integer, Integer> sellScoreMapMap = new HashMap<Integer, Integer>();
            for (int i = 0; i < size; i++) {
                final int orderId = this.readD();
                final int count = Math.max(0, this.readD());// 数量
                if (count <= 0) {
                    _log.error("要求列表物品取得传回数量小于等于0: " + pc.getName() + ":"
                            + (pc.getNetConnection().kick()));
                    continue;
                }
                sellScoreMapMap.put(new Integer(orderId), new Integer(count));

            }
            pc.get_otherList().get_buyCnS(sellScoreMapMap);

        } catch (Exception e) {
            _log.error("购买人物托售物品数据异常: " + pc.getName());
        }
    }

    /**
     * 托售管理员(托售物品)
     * 
     * @param pc
     * @param size
     * @param npcObjectId
     */
    private void mode_shop_item(L1PcInstance pc, int size, int npcObjectId) {
        try {
            if (size == 1) {
                final int objid = this.readD();
                final L1Object object = pc.getInventory().getItem(objid);

                boolean isError = false;
                if (object instanceof L1ItemInstance) {
                    final L1ItemInstance item = (L1ItemInstance) object;
                    if (item.isEquipped()) {// 使用中物件
                        isError = true;
                    }
                    if (!item.isIdentified()) {// 未鉴定物品
                        isError = true;
                    }
                    if (item.getItem().getMaxUseTime() != 0) {// 具有时间限制
                        isError = true;
                    }
                    if (item.get_time() != null) {
                        isError = true;
                    }

                    if (ShopXTable.get()
                            .getTemplate(item.getItem().getItemId()) != null) {// 不可托售物品
                        isError = true;
                    }

                    // 宠物
                    final Object[] petlist = pc.getPetList().values().toArray();
                    for (final Object petObject : petlist) {
                        if (petObject instanceof L1PetInstance) {
                            final L1PetInstance pet = (L1PetInstance) petObject;
                            if (item.getId() == pet.getItemObjId()) {
                                isError = true;
                            }
                        }
                    }

                    // 取回娃娃
                    if (pc.getDoll(item.getId()) != null) {
                        isError = true;
                    }

                    if (item.getGamNo() != null) {// 赌票
                        isError = true;
                    }
                    if (item.getEnchantLevel() < 0) {// 强化为负值
                        isError = true;
                    }
                    if (item.getItem().getMaxChargeCount() != 0) {// 具有次数
                        if (item.getChargeCount() <= 0) {// 已无次数
                            isError = true;
                        }
                    }

                    if (isError) {
                        pc.sendPackets(new S_NPCTalkReturn(npcObjectId,
                                "y_x_e1"));

                    } else {
                        // 取回天宝数量
                        final L1ItemInstance itemT = pc.getInventory()
                                .checkItemX(44070, ShopXSet.ADENA);
                        if (itemT == null) {
                            // 337：\f1%0不足%s。 0_o"
                            pc.sendPackets(new S_ServerMessage(337, "天宝"));
                            // 关闭对话窗
                            pc.sendPackets(new S_CloseList(pc.getId()));
                            return;
                        }
                        // 暂存物件讯息
                        pc.get_other().set_item_objid(objid);
                        pc.sendPackets(new S_CnsSell(npcObjectId, "y_x_3", "ma"));
                    }
                }

            } else {
                pc.sendPackets(new S_NPCTalkReturn(npcObjectId, "y_x_e"));
            }

        } catch (Exception e) {
            _log.error("人物托售物品数据异常: " + pc.getName());
        }
    }

    /**
     * 回收商人/买入玩家物品
     * 
     * @param pc
     * @param npcId
     * @param size
     */
    private void mode_sellall(final L1PcInstance pc, final int size) {
        try {
            final Map<Integer, Integer> sellallMap = new HashMap<Integer, Integer>();
            for (int i = 0; i < size; i++) {
                final int objid = this.readD();
                final int count = Math.max(0, this.readD());// 数量
                if (count <= 0) {
                    _log.error("要求列表物品取得传回数量小于等于0: " + pc.getName() + ":"
                            + (pc.getNetConnection().kick()));
                    continue;
                }
                sellallMap.put(new Integer(objid), new Integer(count));

            }
            pc.get_otherList().sellall(sellallMap);

        } catch (Exception e) {
            _log.error("回收商人/买入玩家物品数据异常: " + pc.getName());
        }
    }

    /**
     * 奇怪的商人
     * 
     * @param pc
     * @param size
     * @param isShop
     */
    private void mode_cn(final L1PcInstance pc, final int size,
            final boolean isShop) {
        try {
            if (isShop) {// 买入
                final Map<Integer, Integer> cnMap = new HashMap<Integer, Integer>();
                for (int i = 0; i < size; i++) {
                    final int orderId = this.readD();
                    final int count = Math.max(0, this.readD());// 数量
                    if (count <= 0) {
                        _log.error("要求列表物品取得传回数量小于等于0: " + pc.getName() + ":"
                                + (pc.getNetConnection().kick()));
                        continue;
                    }
                    cnMap.put(new Integer(orderId), new Integer(count));
                }
                pc.get_otherList().get_buyCn(cnMap);
            }

        } catch (Exception e) {
            _log.error("奇怪的商人买入物品数据异常: " + pc.getName());
        }
    }

    /**
     * 赌场NPC
     * 
     * @param pc
     * @param npcId
     * @param size
     * @param isShop
     *            true买入 false卖出
     */
    private void mode_gambling(final L1PcInstance pc, final int npcId,
            final int size, final boolean isShop) {
        if (isShop) {// 买入
            if (GamblingTime.isStart()) {
                // 关闭对话窗
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }
            final Map<Integer, Integer> gamMap = new HashMap<Integer, Integer>();
            for (int i = 0; i < size; i++) {
                final int orderId = this.readD();
                final int count = Math.max(0, this.readD());// 数量
                if (count <= 0) {
                    _log.error("要求列表物品取得传回数量小于等于0: " + pc.getName() + ":"
                            + (pc.getNetConnection().kick()));
                    continue;
                }
                gamMap.put(new Integer(orderId), new Integer(count));
            }
            pc.get_otherList().get_buyGam(gamMap);

        } else {// 卖出
            for (int i = 0; i < size; i++) {
                final int objid = this.readD();
                final int count = Math.max(0, this.readD());// 数量
                if (count <= 0) {
                    _log.error("要求列表物品取得传回数量小于等于0: " + pc.getName() + ":"
                            + (pc.getNetConnection().kick()));
                    continue;
                }
                pc.get_otherList().get_sellGam(objid, count);
            }
        }
    }

    /**
     * 精灵仓库取出 XXX
     * 
     * @param pc
     * @param npcId
     * @param size
     */
    private void mode_warehouse_elf_out(final L1PcInstance pc, final int npcId,
            final int size) {
        int objectId, count;
        L1ItemInstance item;
        for (int i = 0; i < size; i++) {
            objectId = this.readD();
            count = Math.max(0, this.readD());
            if (count <= 0) {
                _log.error("要求精灵仓库取出传回数量小于等于0: " + pc.getName()
                        + (pc.getNetConnection().kick()));
                break;
            }
            item = pc.getDwarfForElfInventory().getItem(objectId);
            if (item == null) {
                _log.error("精灵仓库取出数据异常(物品为空): " + pc.getName() + "/"
                        + pc.getNetConnection().hashCode());
                break;
            }
//            if (!DwarfForElfReading.get().getUserItems(pc.getAccountName(),
//                    objectId, count)) {
//                _log.error("精灵仓库取出数据异常(该仓库指定数据有误): " + pc.getName() + "/"
//                        + pc.getNetConnection().hashCode());
//                break;
//            } //修改精领仓库为可以即存即取 hjx1000

            if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // 容量重量确认及びメッセージ送信
                if (pc.getInventory().consumeItem(40308, 1)) { // ミスリル
                    pc.getDwarfForElfInventory().tradeItem(item, count,
                            pc.getInventory());
                    _log.info("人物:" + pc.getName() + "从妖仓库取出物品" + item.getLogName1()+" 数量=:"
                            + count + " 物品OBJID:" + item.getId());
                } else {
                    pc.sendPackets(new S_ServerMessage(337, "$767")); // \f1%0が不足しています。
                    break;
                }

            } else {
                pc.sendPackets(new S_ServerMessage(270)); // \f1持っているものが重くて取引できません。
                break;
            }
        }
    }

    /**
     * 精灵仓库存入
     * 
     * @param pc
     * @param npcId
     * @param size
     */
    private void mode_warehouse_elf_in(final L1PcInstance pc, final int npcId,
            final int size) {
        int objectId, count;
        for (int i = 0; i < size; i++) {
            objectId = this.readD();
            count = Math.max(0, this.readD());
            if (count <= 0) {
                _log.error("要求精灵仓库存入传回数量小于等于0: " + pc.getName()
                        + (pc.getNetConnection().kick()));
                break;
            }
            final L1Object object = pc.getInventory().getItem(objectId);
            if (object == null) {
                _log.error("人物背包资料取出数据异常(物品为空): " + pc.getName() + "/"
                        + pc.getNetConnection().hashCode());
                break;
            }
            if (!CharItemsReading.get().getUserItems(pc.getId(), objectId,
                    count)) {
                _log.error("人物背包资料取出数据异常(该仓库指定数据有误): " + pc.getName() + "/"
                        + pc.getNetConnection().hashCode());
                break;
            }
            final L1ItemInstance item = (L1ItemInstance) object;
            if (!item.getItem().isTradable()) {
                // 210 \f1%0%d是不可转移的…
                pc.sendPackets(new S_ServerMessage(210, item.getItem()
                        .getNameId()));
                break;
            }

            if (item.get_time() != null) {
                // \f1%0%d是不可转移的…
                pc.sendPackets(new S_ServerMessage(210, item.getItem()
                        .getNameId()));
                break;
            }

            // 宠物
            final Object[] petlist = pc.getPetList().values().toArray();
            boolean PetInuse = false;
            for (final Object petObject : petlist) {
                if (petObject instanceof L1PetInstance) {
                    final L1PetInstance pet = (L1PetInstance) petObject;
                    if (item.getId() == pet.getItemObjId()) {
                    	PetInuse = true;
                    }
                }
            }
            if (PetInuse) {//修正宠物 使用中可以存取BUG hjx1000
                // 210 \f1%0%d是不可转移的…
                pc.sendPackets(new S_ServerMessage(210, item
                        .getItem().getNameId()));
            	break;
            }

            // 取回娃娃
            if (pc.getDoll(item.getId()) != null) {
                // 1,181：这个魔法娃娃目前正在使用中。
                pc.sendPackets(new S_ServerMessage(1181));
                break;
            }

            if (pc.getDwarfForElfInventory().checkAddItemToWarehouse(item,
                    count, L1Inventory.WAREHOUSE_TYPE_PERSONAL) == L1Inventory.SIZE_OVER) {
                pc.sendPackets(new S_ServerMessage(75)); // \f1これ以上ものを置く场所がありません。
                break;
            }
            pc.getInventory().tradeItem(objectId, count,
                    pc.getDwarfForElfInventory());
            // pc.turnOnOffLight();
        }
    }

    /**
     * 血盟仓库取出 XXX
     * 
     * @param pc
     * @param npcId
     * @param size
     */
    private synchronized void mode_warehouse_clan_out(final L1PcInstance pc,
            final int npcId, final int size) {
        int objectId, count;
        L1ItemInstance item;

        final L1Clan clan = WorldClan.get().getClan(pc.getClanname());
        try {
            if (clan != null && clan.getWarehouseUsingChar() == pc.getId()) { //修正玩家可以利用血盟仓库刷物品
                for (int i = 0; i < size; i++) {
                    objectId = this.readD();
                    count = Math.max(0, this.readD());
                    if (count <= 0) {
                        _log.error("要求血盟仓库取出传回数量小于等于0: " + pc.getName()
                                + (pc.getNetConnection().kick()));
                        break;
                    }
                    item = clan.getDwarfForClanInventory().getItem(objectId);
                    if (item == null) {
                        _log.error("血盟仓库取出数据异常(物品为空): " + pc.getName() + "/"
                                + pc.getNetConnection().hashCode());
                        break;
                    }
                    if (!DwarfForClanReading.get().getUserItems(
                            pc.getClanname(), objectId, count)) {
                        _log.error("血盟仓库取出数据异常(该仓库指定数据有误): " + pc.getName()
                                + "/" + pc.getNetConnection().hashCode());
                        break;
                    }

                    if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // 容量重量确认及びメッセージ送信
                        if (pc.getInventory().consumeItem(L1ItemId.ADENA, 30)) {
                            clan.getDwarfForClanInventory().tradeItem(item,
                                    count, pc.getInventory());

                        } else {
                            pc.sendPackets(new S_ServerMessage(189)); // 189
                                                                      // \f1金币不足。
                            break;
                        }

                    } else {
                        pc.sendPackets(new S_ServerMessage(270)); // \f1持っているものが重くて取引できません。
                        break;
                    }
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            if (clan != null) {
                clan.setWarehouseUsingChar(0); // 解除盟仓使用状态
            }
        }
    }

    /**
     * 血盟仓库存入
     * 
     * @param pc
     * @param npcId
     * @param size
     */
    private void mode_warehouse_clan_in(final L1PcInstance pc, final int npcId,
            final int size) {
        int objectId, count;
        try {
            if (pc.getClanid() != 0) { // クラン所属
                for (int i = 0; i < size; i++) {
                    objectId = this.readD();
                    count = Math.max(0, this.readD());
                    if (count <= 0) {
                        _log.error("要求血盟仓库存入传回数量小于等于0: " + pc.getName()
                                + (pc.getNetConnection().kick()));
                        break;
                    }
                    final L1Clan clan = WorldClan.get().getClan(
                            pc.getClanname());
                    final L1Object object = pc.getInventory().getItem(objectId);
                    if (object == null) {
                        _log.error("人物背包资料取出数据异常(物品为空): " + pc.getName() + "/"
                                + pc.getNetConnection().hashCode());
                        break;
                    }
                    if (!CharItemsReading.get().getUserItems(pc.getId(),
                            objectId, count)) {
                        _log.error("人物背包资料取出数据异常(该仓库指定数据有误): " + pc.getName()
                                + "/" + pc.getNetConnection().hashCode());
                        break;
                    }
                    final L1ItemInstance item = (L1ItemInstance) object;

                    if (!item.getItem().isTradable()) {
                        // 210 \f1%0%d是不可转移的…
                        pc.sendPackets(new S_ServerMessage(210, item.getItem()
                                .getNameId()));
                        break;
                    }

                    if (item.get_time() != null) {
                        // \f1%0%d是不可转移的…
                        pc.sendPackets(new S_ServerMessage(210, item.getItem()
                                .getNameId()));
                        break;
                    }
                    if (ItemRestrictionsTable.RESTRICTIONS.contains(item
                            .getItemId())) {
                        // \f1%0%d是不可转移的…
                        pc.sendPackets(new S_ServerMessage(210, item.getItem()
                                .getNameId()));
                        break;
                    }

                    // 宠物
                    final Object[] petlist = pc.getPetList().values().toArray();
                    boolean PetInuse = false;
                    for (final Object petObject : petlist) {
                        if (petObject instanceof L1PetInstance) {
                            final L1PetInstance pet = (L1PetInstance) petObject;
                            if (item.getId() == pet.getItemObjId()) {
                            	PetInuse = true;
                            }
                        }
                    }
                    if (PetInuse) {//修正宠物 使用中可以存取BUG hjx1000
                        // 210 \f1%0%d是不可转移的…
                        pc.sendPackets(new S_ServerMessage(210, item
                                .getItem().getNameId()));
                    	break;
                    }

                    // 取回娃娃
                    if (pc.getDoll(item.getId()) != null) {
                        // 1,181：这个魔法娃娃目前正在使用中。
                        pc.sendPackets(new S_ServerMessage(1181));
                        break;
                    }

                    if (clan != null) {
                        if (clan.getDwarfForClanInventory()
                                .checkAddItemToWarehouse(item, count,
                                        L1Inventory.WAREHOUSE_TYPE_CLAN) == L1Inventory.SIZE_OVER) {
                            pc.sendPackets(new S_ServerMessage(75)); // \f1これ以上ものを置く场所がありません。
                            break;
                        }
                        pc.getInventory().tradeItem(objectId, count,
                                clan.getDwarfForClanInventory());
                        // pc.turnOnOffLight();
                    }
                }
            } else {
                pc.sendPackets(new S_ServerMessage(208)); // \f1血盟仓库を使用するには血盟に加入していなくてはなりません。
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
        }
    }

    /**
     * 个人仓库取出 XXX
     * 
     * @param pc
     * @param npcId
     * @param size
     */
    private synchronized void mode_warehouse_out(final L1PcInstance pc, final int npcId,
            final int size) {
        int objectId, count;
        L1ItemInstance item;
        for (int i = 0; i < size; i++) {
            objectId = this.readD();
            count = Math.max(0, this.readD());
            if (count <= 0) {
                _log.error("要求个人仓库取出传回数量小于等于0: " + pc.getName()
                        + (pc.getNetConnection().kick()));
                break;
            }
            item = pc.getDwarfInventory().getItem(objectId);
            if (item == null) {
                _log.error("个人仓库取出数据异常(物品为空): " + pc.getName() + "/"
                        + pc.getNetConnection().hashCode());
                break;
            }
            if (!DwarfReading.get().getUserItems(pc.getAccountName(), objectId,
                    count)) {
                _log.error("个人仓库取出数据异常(该仓库指定数据有误): " + pc.getName() + "/"
                        + pc.getNetConnection().hashCode());
                break;
            }

            if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {// 容量重量确认
                if (pc.getInventory().consumeItem(L1ItemId.ADENA, 30)) {
                    pc.getDwarfInventory().tradeItem(item, count,
                            pc.getInventory());

                } else {
                    // \f1金币不足。
                    pc.sendPackets(new S_ServerMessage(189));
                    break;
                }

            } else {
                // 270 \f1当你负担过重时不能交易。
                pc.sendPackets(new S_ServerMessage(270));
                break;
            }
        }
    }

    /**
     * 个人仓库存入
     * 
     * @param pc
     * @param npcId
     * @param size
     */
    private void mode_warehouse_in(final L1PcInstance pc, final int npcId,
            final int size) {
        int objectId, count;
        for (int i = 0; i < size; i++) {
            objectId = this.readD();
            count = Math.max(0, this.readD());
            if (count <= 0) {
                _log.error("要求个人仓库存入传回数量小于等于0: " + pc.getName()
                        + (pc.getNetConnection().kick()));
                break;
            }
            final L1Object object = pc.getInventory().getItem(objectId);
            if (object == null) {
                _log.error("人物背包资料取出数据异常(物品为空): " + pc.getName() + "/"
                        + pc.getNetConnection().hashCode());
                break;
            }
            if (!CharItemsReading.get().getUserItems(pc.getId(), objectId,
                    count)) {
                _log.error("人物背包资料取出数据异常(该仓库指定数据有误): " + pc.getName() + "/"
                        + pc.getNetConnection().hashCode());
                break;
            }
            final L1ItemInstance item = (L1ItemInstance) object;
            if (item.getCount() <= 0) {
                break;
            }

            if (!item.getItem().isTradable()) {
                // 210 \f1%0%d是不可转移的…
                pc.sendPackets(new S_ServerMessage(210, item.getItem()
                        .getNameId()));
                break;
            }

            if (item.get_time() != null) {
                // \f1%0%d是不可转移的…
                pc.sendPackets(new S_ServerMessage(210, item.getItem()
                        .getNameId()));
                break;
            }

            // 宠物
            final Object[] petlist = pc.getPetList().values().toArray();
            boolean PetInuse = false;
            for (final Object petObject : petlist) {
                if (petObject instanceof L1PetInstance) {
                    final L1PetInstance pet = (L1PetInstance) petObject;
                    if (item.getId() == pet.getItemObjId()) {
                    	PetInuse = true;
                    }
                }
            }
            if (PetInuse) {//修正宠物 使用中可以存取BUG hjx1000
                // 210 \f1%0%d是不可转移的…
                pc.sendPackets(new S_ServerMessage(210, item
                        .getItem().getNameId()));
            	break;
            }

            // 取回娃娃
            if (pc.getDoll(item.getId()) != null) {
                // 1,181：这个魔法娃娃目前正在使用中。
                pc.sendPackets(new S_ServerMessage(1181));
                break;
            }

            if (pc.getDwarfInventory().checkAddItemToWarehouse(item, count,
                    L1Inventory.WAREHOUSE_TYPE_PERSONAL) == L1Inventory.SIZE_OVER) {
                pc.sendPackets(new S_ServerMessage(75)); // \f1これ以上ものを置く场所がありません。
                break;
            }
            pc.getInventory()
                    .tradeItem(objectId, count, pc.getDwarfInventory());
            // pc.turnOnOffLight();
        }
    }

    /**
     * 卖出物品给个人商店
     * 
     * @param pc
     *            卖出物品的玩家
     * @param targetPc
     *            设置商店的玩家
     * @param size
     *            数量
     */
    private synchronized void mode_sellpc(final L1PcInstance pc,
            final L1PcInstance targetPc, final int size) {
        int count;
        int order;
        ArrayList<L1PrivateShopBuyList> buyList;
        L1PrivateShopBuyList psbl;
        int itemObjectId;
        L1ItemInstance item;// 卖出物品

        int buyItemObjectId;
        long buyPrice;
        int buyTotalCount;
        int buyCount;

        // L1ItemInstance targetItem;
        final boolean[] isRemoveFromList = new boolean[8];

        // 正在执行个人商店交易
        if (targetPc.isTradingInPrivateShop()) {
            return;
        }

        targetPc.setTradingInPrivateShop(true);
        buyList = targetPc.getBuyList();

        for (int i = 0; i < size; i++) {
            itemObjectId = this.readD();
            count = this.readCH();
            count = Math.max(0, count);
            if (count <= 0) {
                _log.error("要求列表物品取得传回数量小于等于0: " + pc.getName()
                        + (pc.getNetConnection().kick()));
                break;
            }
            order = this.readC();
            item = pc.getInventory().getItem(itemObjectId);
            if (item == null) {
                continue;
            }
            if (item.get_time() != null) {
                // 具有时间
                continue;
            }
            psbl = buyList.get(order);

            buyItemObjectId = psbl.getItemObjectId();// 传回要购买的物品OBJID(商店玩家身上)
            buyPrice = psbl.getBuyPrice();// 回收价格
            buyTotalCount = psbl.getBuyTotalCount(); // 预计买入数量
            buyCount = psbl.getBuyCount(); // 买入数量累计

            if (count > buyTotalCount - buyCount) {
                count = buyTotalCount - buyCount;
            }

            if (item.isEquipped()) {
                // 无法贩卖装备中的道具。
                pc.sendPackets(new S_ServerMessage(905));
                continue;
            }

            final L1ItemInstance srcItem = targetPc.getInventory().getItem(
                    buyItemObjectId);

            if (srcItem.get_time() != null) {
                // 具有时间
                continue;
            }
            if ((item.getItemId() == srcItem.getItemId())
                    && (item.getEnchantLevel() == srcItem.getEnchantLevel())) {
                if (targetPc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // 容量重量确认及びメッセージ送信
                    for (int j = 0; j < count; j++) {
                        if (buyPrice * j > 2000000000) {
                            // 总共贩卖价格无法超过 %d金币。
                            targetPc.sendPackets(new S_ServerMessage(904,
                                    "2000000000"));
                            return;
                        }
                    }

                    // 判断回收者身上金币数量是否足够
                    if (targetPc.getInventory().checkItem(L1ItemId.ADENA,
                            count * buyPrice)) {
                        // 取回金币资料
                        final L1ItemInstance adena = targetPc.getInventory()
                                .findItemId(L1ItemId.ADENA);
                        // 金币足够
                        if (adena != null) {
                            // 出售者物件不足
                            if (item.getCount() < count) {
                                // 989：无法与开设个人商店的玩家进行交易。
                                pc.sendPackets(new S_ServerMessage(989));
                                _log.error("可能使用bug进行交易 人物名称(卖出道具给予个人商店/交易数量不吻合): "
                                        + pc.getName() + " objid:" + pc.getId());
                                continue;
                            }
                            // 卖出物品给个人商店纪录
                            OtherUserSellReading.get().add(
                                    item.getNumberedName_to_String(), item.getId(),
                                    (int) buyPrice, count, pc.getId(),
                                    pc.getName(), targetPc.getId(),
                                    targetPc.getName());

                            // 移动回收者物件
                            targetPc.getInventory().tradeItem(adena,
                                    (count * buyPrice), pc.getInventory());
                            // 移动出售者物件
                            pc.getInventory().tradeItembuypc(item, count,
                                    targetPc.getInventory());

                            psbl.setBuyCount(count + buyCount);
                            buyList.set(order, psbl);

                            if (psbl.getBuyCount() == psbl.getBuyTotalCount()) { // 购买数量已达到
                                isRemoveFromList[order] = true;
                            }
                        }

                    } else {
                        // \f1金币不足。
                        targetPc.sendPackets(new S_ServerMessage(189));
                        break;
                    }

                } else {
                    // \f1对方携带的物品过重，无法交易。
                    pc.sendPackets(new S_ServerMessage(271));
                    break;
                }

                // 交易条件不吻合
            } else {
                _log.error("可能使用bug进行交易 人物名称(卖出道具给予个人商店/交易条件不吻合): "
                        + pc.getName() + " objid:" + pc.getId());
                return;
            }
        }

        // 买い切ったアイテムをリストの末尾から削除
        for (int i = 7; i >= 0; i--) {
            if (isRemoveFromList[i]) {
                buyList.remove(i);
            }
        }
        targetPc.setTradingInPrivateShop(false);
    }

    /**
     * 买入个人商店物品
     * 
     * @param pc
     *            买入商店物品的玩家
     * @param targetPc
     *            设置商店的玩家
     * @param size
     *            买入物品种类清单
     */
    private synchronized void mode_buypc(final L1PcInstance pc, final L1PcInstance targetPc,
            final int size) {
        int order;
        int count;
        int price;
        final ArrayList<L1PrivateShopSellList> sellList;
        L1PrivateShopSellList pssl;
        int itemObjectId;
        int sellPrice;
        int sellTotalCount;
        int sellCount;
        L1ItemInstance item;
        final boolean[] isRemoveFromList = new boolean[8];

        if (targetPc.isTradingInPrivateShop()) {
            return;
        }

        // 摆放个人商店的全部物品种类清单
        sellList = targetPc.getSellList();
//        synchronized (sellList) {
//
//        }
		if (pc.getPartnersPrivateShopItemCount() != sellList.size()) {
			return;
		}
        //System.out.println("isTradingInPrivateShop(0)=="+pc.getName()+targetPc.isTradingInPrivateShop());

        targetPc.setTradingInPrivateShop(true);
        //System.out.println("isTradingInPrivateShop(1)=="+pc.getName()+targetPc.isTradingInPrivateShop());
        for (int i = 0; i < size; i++) { // 购入予定の商品
            order = this.readD();
            count = Math.max(0, this.readD());
            if (count <= 0) {
                _log.error("要求买入个人商店物品传回数量小于等于0: " + pc.getName()
                        + (pc.getNetConnection().kick()));
                break;
            }
            // 取回商店卖出的道具资讯
            pssl = sellList.get(order);
            itemObjectId = pssl.getItemObjectId();// 物品objid
            sellPrice = pssl.getSellPrice();// 售价
            sellTotalCount = pssl.getSellTotalCount(); // 预计卖出的数量
            sellCount = pssl.getSellCount(); // 已经卖出数量的累计

            // 取回卖出物品资料
            item = targetPc.getInventory().getItem(itemObjectId);
            if (item == null) {
                // 该物件为空的状态
                continue;
            }

            if (item.get_time() != null) {
                // 具有时间
                continue;
            }

            // 卖出物品 买方选取超出数量
            if (count > sellTotalCount - sellCount) {
                count = sellTotalCount - sellCount;
            }

            // 卖出物品数量为零
            if (count <= 0) {
                continue;
            }

            if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // 容量重量确认
                for (int j = 0; j < count; j++) { // 计算收入
                    if (sellPrice * j > 2000000000) {
                        // 总共贩卖价格无法超过 %d金币。
                        pc.sendPackets(new S_ServerMessage(904,
                                "2000000000"));
                        targetPc.setTradingInPrivateShop(false);
                        return;
                    }
                }

                // 所需花费
                price = count * sellPrice;

                // 取回金币资料
                final L1ItemInstance adena = pc.getInventory().findItemId(
                        L1ItemId.ADENA);

                if (adena == null) {
                    // \f1金币不足。
                    pc.sendPackets(new S_ServerMessage(189));
                    continue;
                }
                // 买入物品玩家金币数量不足
                if (adena.getCount() < price) {
                    // \f1金币不足。
                    pc.sendPackets(new S_ServerMessage(189));
                    continue;
                }

                // 商店玩家对象对象不为空
                if (targetPc != null) {
                    // 卖出物品数量不足
                    if (item.getCount() < count) {
                        // 989：无法与开设个人商店的玩家进行交易。
                        pc.sendPackets(new S_ServerMessage(989));
                        continue;
                    }

                    // 买入个人商店物品纪录
                    OtherUserBuyReading.get().add(item.getNumberedName_to_String(),
                            item.getId(), sellPrice, count, pc.getId(),
                            pc.getName(), targetPc.getId(),
                            targetPc.getName());

                    // 转移道具给予购买者
                    targetPc.getInventory().tradeItembuypc(item, count, //修正离线商店出售物品可以刷物BUG hjx1000
                            pc.getInventory());
                    // 把花费的金币转移给设置为商店的玩家
                    pc.getInventory().tradeItem(adena, price,
                            targetPc.getInventory());

                    // 产生讯息
                    final String message = item.getItem().getNameId()
                            + " (" + String.valueOf(count) + ")";
                    // 877 将 %1%o 卖给 %0。
                    targetPc.sendPackets(new S_ServerMessage(877, pc
                            .getName(), message));

                    // 设置物品已卖出数量
                    pssl.setSellCount(count + sellCount);
                    sellList.set(order, pssl);

                    // 贩卖物件已售完
                    if (pssl.getSellCount() == pssl.getSellTotalCount()) {
                        isRemoveFromList[order] = true;
                    }
                }

            } else {
                // \f1当你负担过重时不能交易。
                pc.sendPackets(new S_ServerMessage(270));
                break;
            }
        }
        // 该道具贩卖结束移出贩卖清单
        for (int i = 7; i >= 0; i--) {
            if (isRemoveFromList[i]) {
                sellList.remove(i);
            }
        }
        //System.out.println("isTradingInPrivateShop(2)=="+pc.getName()+targetPc.isTradingInPrivateShop());
        targetPc.setTradingInPrivateShop(false);
    }

    /**
     * NPC商店卖出物品(买入玩家物品)
     * 
     * @param pc
     * @param npcId
     * @param size
     */
    private void mode_sell(final L1PcInstance pc, final int npcId,
            final int size) {
        final L1Shop shop = ShopTable.get().get(npcId);
        if (shop != null) {
            final L1ShopSellOrderList orderList = shop.newSellOrderList(pc);
            for (int i = 0; i < size; i++) {
                final int objid = this.readD();
                final int count = Math.max(0, this.readD());// 数量

                if (count <= 0) {
                    _log.error("要求列表物品取得传回数量小于等于0: " + pc.getName() + ":"
                            + (pc.getNetConnection().kick()));
                    continue;
                }
                orderList.add(objid, count);
            }
            shop.buyItems(orderList);

            // 没有贩售资料
        } else {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    /**
     * NPC商店买入物品(卖出物品给予玩家)
     * 
     * @param pc
     * @param npcId
     * @param size
     */
    private void mode_buy(final L1PcInstance pc, final int npcId, final int size) {
        final L1Shop shop = ShopTable.get().get(npcId);
        if (shop != null) {
            final L1ShopBuyOrderList orderList = shop.newBuyOrderList();
            for (int i = 0; i < size; i++) {
                final int orderId = this.readD();
                final int count = Math.max(0, this.readD());// 数量
                if (count <= 0) {
                    _log.error("要求列表物品取得传回数量小于等于0: " + pc.getName() + ":"
                            + (pc.getNetConnection().kick()));
                    continue;
                }
                //限制玩家购买NPC物品数量最多 9999 hjx1000
                if (count > 9999) {
                    _log.error("要求列表物品取得传回数量大于9999: " + pc.getName() + ":"
                            + (pc.getNetConnection().kick()));
                    continue;
                }
                //限制玩家购买NPC物品数量最多 9999
                orderList.add(orderId, count);
            }
            shop.sellItems(pc, orderList);

            // 没有贩售资料
        } else {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
