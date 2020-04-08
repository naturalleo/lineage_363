package com.lineage.server.templates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.data.event.GamblingSet;
import com.lineage.data.event.gambling.GamblingNpc;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.ShopTable;
import com.lineage.server.datatables.lock.CharItemPowerReading;
import com.lineage.server.datatables.lock.DwarfShopReading;
import com.lineage.server.datatables.lock.GamblingReading;
import com.lineage.server.datatables.lock.ServerCnInfoReading;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.Instance.L1IllusoryInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.timecontroller.event.GamblingTime;
import com.lineage.server.utils.ListMapUtil;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;

/**
 * 人物其他项清单列表
 * 
 * @author DaiEn
 * 
 */
public class L1PcOtherList {

    private static final Log _log = LogFactory.getLog(L1PcOtherList.class);

    private L1PcInstance _pc;

    public Map<Integer, L1ItemInstance> DELIST;// 虚拟商店买入清单(ORDERID/指定的物品)

    private Map<Integer, L1ShopItem> _cnList;// 购买奇怪的商人物品清单(ORDERID/指定的物品数据)

    private Map<Integer, L1ItemInstance> _cnSList;// 购买托售商人物品清单(ORDERID/指定的物品数据)

    private Map<Integer, GamblingNpc> _gamList;// 购买食人妖精竞赛票清单(ORDERID/指定的参赛者数据)

    private Map<Integer, L1Gambling> _gamSellList;// 卖出食人妖精竞赛票清单(物品OBJID/妖精竞赛纪录缓存)

    private Map<Integer, L1IllusoryInstance> _illusoryList;// 召唤分身清单(分身OBJID/分身数据)

    private Map<Integer, L1TeleportLoc> _teleport;// NPC传送点缓存(传点排序编号/传点数据)

    private Map<Integer, Integer> _uplevelList;// 属性重置清单(模式/增加数值总合)

    private Map<Integer, String[]> _shiftingList;// 装备转移人物清单(帐户中人物排序编号/String[]{OBJID/人物名称})

    private Map<Integer, L1ItemInstance> _sitemList;// 装备交换清单(ORDERID/指定的物品)

    private Map<Integer, Integer> _sitemList2;// 装备交换清单(ORDERID/指定的物品ITEMID)

    public Map<Integer, L1Quest> QUESTMAP;// 暂存任务清单

    public Map<Integer, L1ShopS> SHOPXMAP;// 暂存出售纪录清单

    public ArrayList<Integer> ATKNPC;// 暂存需要攻击的NPCID

    private int[] _is;// 暂存人物原始素质改变

    public L1PcOtherList(final L1PcInstance pc) {
        this._pc = pc;
        this.DELIST = new HashMap<Integer, L1ItemInstance>();

        this._cnList = new HashMap<Integer, L1ShopItem>();
        this._cnSList = new HashMap<Integer, L1ItemInstance>();
        this._gamList = new HashMap<Integer, GamblingNpc>();
        this._gamSellList = new HashMap<Integer, L1Gambling>();
        this._illusoryList = new HashMap<Integer, L1IllusoryInstance>();

        this._teleport = new HashMap<Integer, L1TeleportLoc>();
        this._uplevelList = new HashMap<Integer, Integer>();
        this._shiftingList = new HashMap<Integer, String[]>();
        this._sitemList = new HashMap<Integer, L1ItemInstance>();
        this._sitemList2 = new HashMap<Integer, Integer>();

        this.QUESTMAP = new HashMap<Integer, L1Quest>();
        this.SHOPXMAP = new HashMap<Integer, L1ShopS>();
        this.ATKNPC = new ArrayList<Integer>();
    }

    /**
     * 清空全部资料
     */
    public void clearAll() {
        try {
            ListMapUtil.clear(DELIST);
            ListMapUtil.clear(_cnList);
            ListMapUtil.clear(_cnSList);
            ListMapUtil.clear(_gamList);
            ListMapUtil.clear(_gamSellList);
            ListMapUtil.clear(_illusoryList);
            ListMapUtil.clear(_teleport);
            ListMapUtil.clear(_uplevelList);
            ListMapUtil.clear(_shiftingList);
            ListMapUtil.clear(_sitemList);
            ListMapUtil.clear(_sitemList2);
            ListMapUtil.clear(QUESTMAP);
            ListMapUtil.clear(SHOPXMAP);
            ListMapUtil.clear(ATKNPC);

            this.DELIST = null;// 虚拟商店买入清单
            this._cnList = null;
            this._cnSList = null;
            this._gamList = null;
            this._gamSellList = null;
            this._illusoryList = null;
            this._teleport = null;
            this._uplevelList = null;
            this._shiftingList = null;
            this._sitemList = null;
            this._sitemList2 = null;
            this.QUESTMAP = null;
            this.SHOPXMAP = null;
            this.ATKNPC = null;

            this._is = null;

            this._pc = null;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    // TODO 装备交换清单

    /**
     * 传回装备交换清单(可换)
     * 
     * @return _sitemList2
     */
    public Map<Integer, Integer> get_sitemList2() {
        return this._sitemList2;
    }

    /**
     * 加入装备交换清单(可换)
     * 
     * @param key
     * @param value
     */
    public void add_sitemList2(final Integer key, final Integer value) {
        this._sitemList2.put(key, value);
    }

    /**
     * 清空装备交换清单(可换)
     */
    public void clear_sitemList2() {
        this._sitemList2.clear();
    }

    // TODO 装备交换清单

    /**
     * 传回装备交换清单(准备)
     * 
     * @return _sitemList
     */
    public Map<Integer, L1ItemInstance> get_sitemList() {
        return this._sitemList;
    }

    /**
     * 加入装备交换清单(准备)
     * 
     * @param key
     * @param value
     */
    public void add_sitemList(final Integer key, final L1ItemInstance value) {
        this._sitemList.put(key, value);
    }

    /**
     * 清空装备交换清单(准备)
     */
    public void clear_sitemList() {
        this._sitemList.clear();
    }

    // TODO 帐户人物清单

    /**
     * 传回帐户人物清单
     * 
     * @return _shiftingList
     */
    public Map<Integer, String[]> get_shiftingList() {
        return this._shiftingList;
    }

    /**
     * 加入帐户人物清单
     * 
     * @param key
     * @param value
     */
    public void add_shiftingList(final Integer key, final String[] value) {
        this._shiftingList.put(key, value);
    }

    /**
     * 移出帐户人物清单
     * 
     * @param key
     */
    public void remove_shiftingList(final Integer key) {
        this._shiftingList.remove(key);
    }

    /**
     * 读取人物列表<BR>
     * 将资料置入MAP中
     */
    public void set_shiftingList() {
        try {
            _shiftingList.clear();
            Connection conn = null;
            PreparedStatement pstm = null;
            ResultSet rs = null;
            try {

                conn = DatabaseFactory.get().getConnection();
                pstm = conn
                        .prepareStatement("SELECT * FROM `characters` WHERE `account_name`=?");
                pstm.setString(1, this._pc.getAccountName());
                rs = pstm.executeQuery();

                int key = 0;
                while (rs.next()) {
                    final int objid = rs.getInt("objid");
                    final String name = rs.getString("char_name");
                    if (!name.equalsIgnoreCase(this._pc.getName())) {
                        key++;
                        this.add_shiftingList(key,
                                new String[] { String.valueOf(objid), name });
                    }
                }

            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);

            } finally {
                SQLUtil.close(rs);
                SQLUtil.close(pstm);
                SQLUtil.close(conn);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    // TODO

    /**
     * 传回分身
     * 
     * @return _illusoryList
     */
    public Map<Integer, L1IllusoryInstance> get_illusoryList() {
        return this._illusoryList;
    }

    /**
     * 加入分身清单
     * 
     * @param key
     * @param value
     */
    public void addIllusoryList(final Integer key,
            final L1IllusoryInstance value) {
        this._illusoryList.put(key, value);
    }

    /**
     * 移出分身清单
     * 
     * @param key
     */
    public void removeIllusoryList(final Integer key) {
        try {
            if (_illusoryList == null) {
                return;
            }
            if (_illusoryList.get(key) != null) {
                this._illusoryList.remove(key);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    // TODO 传送

    /**
     * 传送点缓存
     * 
     * @param teleportMap
     */
    public void teleport(final HashMap<Integer, L1TeleportLoc> teleportMap) {
        try {
            ListMapUtil.clear(_teleport);
            _teleport.putAll(teleportMap);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 传送点缓存
     * 
     * @return _teleport
     */
    public Map<Integer, L1TeleportLoc> teleportMap() {
        return this._teleport;
    }

    /**
     * 卖出全部物品
     * 
     * @param sellallMap
     */
    public void sellall(final Map<Integer, Integer> sellallMap) {
        try {
            int getprice = 0;
            for (final Integer integer : sellallMap.keySet()) {
                final L1ItemInstance item = this._pc.getInventory().getItem(
                        integer);
                if (item != null) {
                    final int key = item.getItemId();
                    final int price = ShopTable.get().getPrice(key);
                    final Integer count = sellallMap.get(integer);
                    final long remove = this._pc.getInventory().removeItem(
                            integer, count);
                    if (remove == count) {
                        getprice += (price * count);
                    }
                }
            }

            if (getprice > 0) {
                // 物品(金币)
                final L1ItemInstance item = ItemTable.get().createItem(40308);
                item.setCount(getprice);
                this.createNewItem(item);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    // TODO 购物清单

    /**
     * 清空全部买入资料
     */
    public void clear() {
        try {
            ListMapUtil.clear(this._cnList);
            ListMapUtil.clear(this._gamList);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    // 奇岩赌场

    /**
     * 复制卖出资料(清空旧资料)
     * 
     * @param sellList
     */
    public void set_gamSellList(final Map<Integer, L1Gambling> sellList) {
        try {
            ListMapUtil.clear(_gamSellList);
            _gamSellList.putAll(sellList);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 卖出食人妖精竞赛票
     * 
     * @param element
     * @param index
     */
    public void get_sellGam(final int objid, final int count) {
        try {
            final L1Gambling element = _gamSellList.get(objid);
            if (element == null) {
                return;
            }
            final long countx = (long) (element.get_rate() * GamblingSet.GAMADENA)
                    * count;
            final long remove = this._pc.getInventory()
                    .removeItem(objid, count);
            if (remove == count) {
                final int outcount = element.get_outcount() - count;
                if (outcount < 0) {
                    return;
                }
                element.set_outcount(outcount);
                GamblingReading.get()
                        .updateGambling(element.get_id(), outcount);
                // 奇岩赌场 下注使用物品编号(预设金币40308)
                final L1ItemInstance item = ItemTable.get().createItem(
                        GamblingSet.ADENAITEM);
                item.setCount(countx);
                this.createNewItem(item);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 加入购买食人妖精竞赛票
     * 
     * @param element
     * @param index
     */
    public void add_gamList(final GamblingNpc element, final int index) {
        this._gamList.put(new Integer(index), element);
    }

    /**
     * 购买食人妖精竞赛票
     * 
     * @param gamMap
     */
    public void get_buyGam(final Map<Integer, Integer> gamMap) {
        try {
            for (final Integer integer : gamMap.keySet()) {
                final int index = integer;
                final int count = gamMap.get(integer);
                this.get_gamItem(index, count);
            }
            ListMapUtil.clear(this._gamList);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private synchronized void get_gamItem(final int index, final int count) {
        try {
            if (count <= 0) {
                return;
            }
            final GamblingNpc element = this._gamList.get(index);
            if (element == null) {
                return;
            }
//            final long alladena = element.get_gambling().get_allRate();
//            if (alladena + count > 100) {
//            	this._pc.sendPackets(new S_SystemMessage("本场限注最大100。"));
//            	this._pc.sendPackets(new S_SystemMessage("目前已下注: " + alladena));
//            	return;
//            }

            final int npcid = element.get_npc().getNpcId();// 比赛者NPCID
            final int no = GamblingTime.get_gamblingNo();// 比赛场次编号
            final long adena = GamblingSet.GAMADENA * count;// 需要数量
            final long srcCount = this._pc.getInventory().countItems(
                    GamblingSet.ADENAITEM);// 现有数量
            final long src = adena / GamblingSet.GAMADENA;
            if (adena <= 0) {
            	return;
            }
            if (src != count) {
            	return;
            }

            // 奇岩赌场 下注使用物品编号(预设金币40308)检查
            if (srcCount >= adena) {
                // 食人妖精竞赛票
                final L1ItemInstance item = ItemTable.get().createItem(40309);
                // 容量重量确认
                if (this._pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
                    // 扣除奇岩赌场 下注使用物品编号(预设金币40308)
                    this._pc.getInventory().consumeItem(GamblingSet.ADENAITEM,
                            adena);

                    item.setCount(count);
                    item.setGamNo(no + "-" + npcid);
                    this.createNewItem(item);
                    element.add_adena(adena);

                } else {
                    // \f1当你负担过重时不能交易。
                    this._pc.sendPackets(new S_ServerMessage(270));
                }

            } else {
                final L1Item item = ItemTable.get().getTemplate(
                        GamblingSet.ADENAITEM);
                long nc = adena - srcCount;
                // 337：\f1%0不足%s。
                this._pc.sendPackets(new S_ServerMessage(337, item.getNameId()
                        + "(" + nc + ")"));
                // 337：\f1%0不足%s。
                // this._pc.sendPackets(new S_ServerMessage(189));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    // 托售管理员

    /**
     * 加入购买托售管理员物品
     */
    public void add_cnSList(final L1ItemInstance shopItem, final int index) {
        this._cnSList.put(new Integer(index), shopItem);
    }

    /**
     * 买入托售管理员物品
     */
    public synchronized void get_buyCnS(final Map<Integer, Integer> cnMap) {
        try {
            final int itemid_cn = 44070;// 天宝 44070
            for (final Integer integer : cnMap.keySet()) {
                final int count = cnMap.get(integer);
                if (count > 0) {
                    // 取回卖出视窗对应排序编号物品
                    final L1ItemInstance element = this._cnSList.get(integer
                            .intValue());
                    final L1ShopS shopS = DwarfShopReading.get().getShopS(
                            element.getId());
                    if (element != null && shopS != null) {
                        if (shopS.get_end() != 0) {// 物品非出售中
                            continue;
                        }
                        if (shopS.get_item() == null) {// 物品设置为空
                            continue;
                        }
                        // 取回天宝数量
                        final L1ItemInstance itemT = _pc.getInventory()
                                .checkItemX(itemid_cn, shopS.get_adena());
                        if (itemT == null) {
                            // 337：\f1%0不足%s。 0_o"
                            _pc.sendPackets(new S_ServerMessage(337, "天宝"));
                            continue;
                        }

                        shopS.set_end(1);// 设置资讯为售出
                        shopS.set_item(null);
                        DwarfShopReading.get().updateShopS(shopS);
                        DwarfShopReading.get().deleteItem(element.getId());

                        this._pc.getInventory().consumeItem(itemid_cn,
                                shopS.get_adena());
                        this._pc.getInventory().storeTradeItem(element);
                        this._pc.sendPackets(new S_ServerMessage(403, element
                                .getLogName())); // 获得0%。
                        // createNewItem(element);
                    }
                }
            }
            ListMapUtil.clear(this._cnList);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    // 奇怪的商人

    /**
     * 加入购买奇怪的商人物品
     */
    public void add_cnList(final L1ShopItem shopItem, final int index) {
        this._cnList.put(new Integer(index), shopItem);
    }

    /**
     * 买入奇怪的商人物品
     */
    public void get_buyCn(final Map<Integer, Integer> cnMap) {
        try {
            for (final Integer integer : cnMap.keySet()) {
                final int index = integer;
                final int count = cnMap.get(integer);
                if (count > 0) {
                    final L1ShopItem element = this._cnList.get(index);
                    if (element != null) {
                        this.get_cnItem(element, count);
                    }
                }
            }
            ListMapUtil.clear(this._cnList);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void get_cnItem(final L1ShopItem element, final int count) {
        try {
            final int itemid_cn = 44070;// 天宝 44070
            final int itemid = element.getItemId();// 物品编号
            final int getCount = element.getPackCount() * count;// 给予数量
            final int adenaCount = element.getPrice() * count;// 花费
            final int src = adenaCount / element.getPrice();
            final Random random = new Random();

            if (adenaCount <= 0){
            	_log.error("有人改内存");
            	return;
            }           	
            if (src != count) {
            	_log.error("有人改内存");
            	return;
            }
            // 物品检查(天宝 44070)
            long srcCount = this._pc.getInventory().countItems(itemid_cn);
            if (srcCount >= adenaCount) {
                this._pc.getInventory().consumeItem(itemid_cn, adenaCount);
                // 找回物品
                final L1Item itemtmp = ItemTable.get().getTemplate(itemid);

                toGmMsg(itemtmp, adenaCount);

                if (itemtmp.isStackable()) {
                    // 找回物品
                    final L1ItemInstance item = ItemTable.get().createItem(
                            itemid);
                    item.setCount(getCount);
                    this.createNewItem(item);

                } else {
                    for (int i = 0; i < getCount; i++) {
                        // 找回物品
                        final L1ItemInstance item = ItemTable.get().createItem(
                                itemid);
                        if (item.getItemId() == 31010) {//添加随机属性符石
                        	final L1ItemPower_name power = new L1ItemPower_name();
                        	power.set_item_obj_id(item.getId());
                        	power.set_hole_count(0);
                        	power.set_hole_1(random.nextInt(6) + 1);
                        	power.set_hole_2(random.nextInt(6) + 1);
                        	power.set_hole_3(random.nextInt(6) + 1);
                        	power.set_hole_4(random.nextInt(6) + 1);
                        	power.set_hole_5(random.nextInt(6) + 1);
                        	power.set_xing_count(0);
                        	item.set_power_name(power);
                        	CharItemPowerReading.get()
                        	.storeItem(item.getId(),
                        			item.get_power_name());
                        }
//                        if (PowerItemSet.START) {
//                            // 凹槽诞生
//                            switch (item.getItem().getUseType()) {
//                                case 1:// 武器
//                                case 2:// 盔甲
//                                case 18:// T恤
//                                case 19:// 斗篷
//                                case 20:// 手套
//                                case 21:// 靴
//                                case 22:// 头盔
//                                case 25:// 盾牌
//                                    final L1ItemPower_name power = new L1ItemPower_name();
//                                    power.set_item_obj_id(item.getId());
//                                    power.set_hole_count(1);
//                                    power.set_hole_1(0);
//                                    power.set_hole_2(0);
//                                    power.set_hole_3(0);
//                                    power.set_hole_4(0);
//                                    power.set_hole_5(0);
//                                    item.set_power_name(power);
//                                    CharItemPowerReading.get()
//                                            .storeItem(item.getId(),
//                                                    item.get_power_name());// 修正
//                                                                           // 75052911
//                                                                           // (UID:
//                                                                           // 1959)
//                                    break;
//                            }
//                        }
                        this.createNewItem(item);
                    }
                }

            } else {
                long nc = adenaCount - srcCount;
                // 337：\f1%0不足%s。
                this._pc.sendPackets(new S_ServerMessage(337, "天宝(" + nc + ")"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    // 后处理

    /**
     * 通知GM
     */
    private void toGmMsg(final L1Item itemtmp, final int adenaCount) {
        try {
            ServerCnInfoReading.get().create(this._pc, itemtmp, adenaCount);
            final Collection<L1PcInstance> allPc = World.get().getAllPlayers();
            for (L1PcInstance tgpc : allPc) {
                if (tgpc.isGm()) {
                    final StringBuilder topc = new StringBuilder();
                    topc.append("人物:" + this._pc.getName() + " 买入:"
                            + itemtmp.getNameId() + " 花费:" + adenaCount);
                    tgpc.sendPackets(new S_ServerMessage(166, topc.toString()));
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 给予物件的处理
     * 
     * @param pc
     * @param item
     */
    private void createNewItem(final L1ItemInstance item) {
        try {
            this._pc.getInventory().storeItem(item);
            this._pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // 获得0%。

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    // TODO 属性重置处理

    /**
     * 属性重置
     * 
     * @param key
     *            模式<BR>
     *            0 升级点数/万能药点数 可分配数量<BR>
     * 
     *            1 力量 (原始)<BR>
     *            2 敏捷 (原始)<BR>
     *            3 体质 (原始)<BR>
     *            4 精神 (原始)<BR>
     *            5 智力 (原始)<BR>
     *            6 魅力 (原始)<BR>
     * 
     *            7 力量 +-<BR>
     *            8 敏捷 +-<BR>
     *            9 体质 +-<BR>
     *            10 精神 +-<BR>
     *            11 智力 +-<BR>
     *            12 魅力 +-<BR>
     * 
     *            13 目前分配点数模式 0:升级点数 1:万能药点数<BR>
     * @param value
     *            增加数值总合
     */
    public void add_levelList(final int key, final int value) {
        _uplevelList.put(key, value);
    }

    /**
     * 属性重置清单
     * 
     * @return
     */
    public Map<Integer, Integer> get_uplevelList() {
        return this._uplevelList;
    }

    /**
     * 指定数值参数
     * 
     * @param key
     * @return
     */
    public Integer get_uplevelList(int key) {
        return this._uplevelList.get(key);
    }

    /**
     * 清空属性重置处理清单
     */
    public void clear_uplevelList() {
        ListMapUtil.clear(this._uplevelList);
    }

    /**
     * 暂存人物原始素质改变
     * 
     * @param is
     */
    public void set_newPcOriginal(final int[] is) {
        this._is = is;
    }

    /**
     * 传回暂存人物原始素质改变
     * 
     * @return
     */
    public int[] get_newPcOriginal() {
        return this._is;
    }
}
