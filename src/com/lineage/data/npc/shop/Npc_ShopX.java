package com.lineage.data.npc.shop;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.event.ShopXSet;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.ItemRestrictionsTable;
import com.lineage.server.datatables.ShopXTable;
import com.lineage.server.datatables.lock.DwarfShopReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_CnSRetrieve;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_CnSShopSellList;
import com.lineage.server.templates.L1ShopS;

/**
 * 托售管理员<BR>
 * 70535
 * 
 * @author dexc
 * 
 */
public class Npc_ShopX extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_ShopX.class);

    private static final int _adenaid = 44070;

    private static final int _count = 200;// 可托售数量

    /**
	 *
	 */
    private Npc_ShopX() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_ShopX();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            pc.get_other().set_item_objid(0);

            String[] info = new String[] { String.valueOf(ShopXSet.ADENA),
                    String.valueOf(ShopXSet.DATE),
                    String.valueOf(ShopXSet.MIN), String.valueOf(ShopXSet.MAX) };

            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_x_1", info));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        if (cmd.equalsIgnoreCase("s")) {// 我要进行道具出售
            cmd_s2(pc, npc);

        } else if (cmd.equalsIgnoreCase("i")) {// 查看目前其他人出售的道具
            pc.sendPackets(new S_CnSShopSellList(pc, npc));

        } else if (cmd.equalsIgnoreCase("l")) {// 我的出售纪录
            cmd_1(pc, npc);

        } else if (cmd.equalsIgnoreCase("ma")) {// 决定价格
            cmd_ma(pc, npc, amount);

        } else if (cmd.equals("up")) {// 上一页
            final int page = pc.get_other().get_page() - 1;
            showPage(pc, npc.getId(), page);

        } else if (cmd.equals("dn")) {// 下一页
            final int page = pc.get_other().get_page() + 1;
            showPage(pc, npc.getId(), page);

        } else if (cmd.equals("over")) {// 我要取消托售!
            cmd_over(pc, npc);

        } else if (cmd.equals("no")) {// 算了在卖卖看!
            pc.setTempID(0);
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));

            // 数字选项
        } else if (cmd.matches("[0-9]+")) {
            final String pagecmd = pc.get_other().get_page() + cmd;
            update(pc, npc, Integer.parseInt(pagecmd));
        }
    }

    /**
     * 我的出售纪录
     * 
     * @param pc
     * @param npc
     */
    public static void cmd_1(L1PcInstance pc, L1NpcInstance npc) {
        try {
            pc.get_otherList().SHOPXMAP.clear();
            Map<Integer, L1ShopS> temp = DwarfShopReading.get().getShopSMap(
                    pc.getId());

            if (temp != null) {
                pc.get_otherList().SHOPXMAP.putAll(temp);
                temp.clear();
            }
            showPage(pc, npc.getId(), 0);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 决定价格
     * 
     * @param pc
     * @param npc
     */
    public static void cmd_ma(final L1PcInstance pc, final L1NpcInstance npc,
            final long amount) {
        try {
        	
        	final int objid = pc.get_other().get_item_objid();
            // 取回天宝数量
            final L1ItemInstance itemT = pc.getInventory().checkItemX(_adenaid,
                    ShopXSet.ADENA);
            boolean isError = false;
            
            if (objid == 0) {
            	isError = true;
            }
            if (itemT == null) {
                // 337：\f1%0不足%s。 0_o"
                pc.sendPackets(new S_ServerMessage(337, "天宝"));
                isError = true;
            }
            if (amount < ShopXSet.MIN) {
                isError = true;
            }
            if (amount > ShopXSet.MAX) {
                isError = true;
            }

            if (isError) {
                // 关闭对话窗
                pc.sendPackets(new S_CloseList(pc.getId()));
                pc.get_other().set_item_objid(0);
                return;
            }

            L1ItemInstance item = pc.getInventory().getItem(objid);
            if (item == null) {
                // 关闭对话窗
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }
            if (item.isEquipped()) {// 使用中物件
                isError = true;
            }
            if (!item.isIdentified()) {// 未鉴定物品
                isError = true;
            }
            if (item.getItem().getMaxUseTime() != 0) {// 具有时间限制
                isError = true;
            }

            if (ShopXTable.get().getTemplate(item.getItem().getItemId()) != null) {// 不可托售物品
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
            if (!item.getItem().isTradable()) { //不可交易物品
            	isError = true;
            }
            if (item.getBless() > 2) { //封印物品不可以寄卖
            	isError = true;
            }

            if (isError) {
                // 关闭对话窗
                pc.sendPackets(new S_CloseList(pc.getId()));
                pc.get_other().set_item_objid(0);
                return;
            }

            pc.get_other().set_item_objid(0);

            long time = ShopXSet.DATE * 24 * 60 * 60 * 1000;
            final Timestamp overTime = new Timestamp(System.currentTimeMillis()
                    + time); // 到期时间
            final SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            final String key = sdf.format(overTime);

            String[] info = new String[] { item.getLogName(),
                    String.valueOf(amount), key };
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_x_4", info));

            final L1ShopS shopS = new L1ShopS();
            shopS.set_id(DwarfShopReading.get().nextId());
            shopS.set_item_obj_id(item.getId());
            shopS.set_user_obj_id(pc.getId());
            shopS.set_adena((int) amount);
            shopS.set_overtime(overTime);
            shopS.set_end(0);

            final String outname = item.getNumberedName_to_String();// 修正NAMEID显示异常(loli)

            shopS.set_none(outname);
            shopS.set_item(item);

            pc.getInventory().removeItem(itemT, ShopXSet.ADENA);// 移除天宝
            pc.getInventory().removeItem(item);// 移除托售物件
            DwarfShopReading.get().insertItem(item.getId(), item, shopS);

            try {
                pc.save();
                pc.saveInventory();

            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 我要取消托售
     * 
     * @param pc
     * @param npc
     */
    public static void cmd_over(L1PcInstance pc, L1NpcInstance npc) {
        try {
            final L1ShopS shopS = DwarfShopReading.get().getShopS(
                    pc.getTempID());
            pc.setTempID(0);
            shopS.set_end(3);
            shopS.set_item(null);
            DwarfShopReading.get().updateShopS(shopS);
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 我要进行道具出售(全物件)
     * 
     * @param pc
     * @param npc
     */
    private static void cmd_s1(L1PcInstance pc, L1NpcInstance npc) {
        try {
            final Map<Integer, L1ShopS> allShopS = DwarfShopReading.get()
                    .allShopS();
            if (allShopS.size() >= _count) {
                // 75：\f1没有多余的空间可以储存。
                pc.sendPackets(new S_ServerMessage(75));
                // 关闭对话窗
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }

            // 取回天宝数量
            final L1ItemInstance itemT = pc.getInventory().checkItemX(44070,
                    ShopXSet.ADENA);
            if (itemT == null) {
                // 337：\f1%0不足%s。 0_o"
                pc.sendPackets(new S_ServerMessage(337, "天宝"));
                // 关闭对话窗
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }

            final List<L1ItemInstance> itemsx = new CopyOnWriteArrayList<L1ItemInstance>();

            final List<L1ItemInstance> items = pc.getInventory().getItems();
            for (L1ItemInstance item : items) {
                if (ShopXTable.get().getTemplate(item.getItem().getItemId()) != null) {// 不可托售物品
                    continue;
                }

                if (item.isEquipped()) {// 使用中物件
                    continue;
                }
                if (!item.isIdentified()) {// 未鉴定物品
                    continue;
                }
                if (item.getItem().getMaxUseTime() != 0) {// 具有时间限制
                    continue;
                }
                if (item.get_time() != null) {// 具有时间限制
                    continue;
                }

                if (item.getGamNo() != null) {// 赌票
                    continue;
                }
                if (item.getEnchantLevel() < 0) {// 强化为负值
                    continue;
                }
                if (item.getItem().getMaxChargeCount() != 0) {// 具有次数
                    if (item.getChargeCount() <= 0) {// 已无次数
                        continue;
                    }
                }
                if (ItemRestrictionsTable.RESTRICTIONS.contains(item
                        .getItemId())) {
                    continue;
                }
                itemsx.add(item);
            }
            // System.out.println(" 加入清单:" + itemsx.size() + "/" +
            // itemList.size());
            pc.sendPackets(new S_CnSRetrieve(pc, npc.getId(), itemsx));
            allShopS.clear();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 我要进行道具出售(不可转移排除)
     * 
     * @param pc
     * @param npc
     */
    public static void cmd_s2(L1PcInstance pc, L1NpcInstance npc) {
        try {

            final Map<Integer, L1ShopS> allShopS = DwarfShopReading.get()
                    .allShopS();
            if (allShopS.size() >= _count) {
                // 75：\f1没有多余的空间可以储存。
                pc.sendPackets(new S_ServerMessage(75));
                // 关闭对话窗
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }

            // 取回天宝数量
            final L1ItemInstance itemT = pc.getInventory().checkItemX(44070,
                    ShopXSet.ADENA);
            if (itemT == null) {
                // 337：\f1%0不足%s。 0_o"
                pc.sendPackets(new S_ServerMessage(337, "天宝"));
                // 关闭对话窗
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }

            final List<L1ItemInstance> itemsx = new CopyOnWriteArrayList<L1ItemInstance>();

            final List<L1ItemInstance> items = pc.getInventory().getItems();
            for (L1ItemInstance item : items) {
                if (ShopXTable.get().getTemplate(item.getItem().getItemId()) != null) {// 不可托售物品
                    continue;
                }

                if (!item.getItem().isTradable()) {// 不可转移
                    continue;
                }
                if (item.isEquipped()) {// 使用中物件
                    continue;
                }
                if (!item.isIdentified()) {// 未鉴定物品
                    continue;
                }
                if (item.getItem().getMaxUseTime() != 0) {// 具有时间限制
                    continue;
                }
                if (item.get_time() != null) {// 具有时间限制
                    continue;
                }

                if (item.getGamNo() != null) {// 赌票
                    continue;
                }
                if (item.getEnchantLevel() < 0) {// 强化为负值
                    continue;
                }
                if (item.getItem().getMaxChargeCount() != 0) {// 具有次数
                    if (item.getChargeCount() <= 0) {// 已无次数
                        continue;
                    }
                }
                if (ItemRestrictionsTable.RESTRICTIONS.contains(item
                        .getItemId())) {
                    continue;
                }
                itemsx.add(item);
            }
            // System.out.println(" 加入清单:" + itemsx.size() + "/" +
            // itemList.size());
            pc.sendPackets(new S_CnSRetrieve(pc, npc.getId(), itemsx));
            allShopS.clear();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 取回物品销售结果
     * 
     * @param pc
     *            人物
     * @param npc
     *            NPC
     * @param key
     *            排序
     */
    public static void update(L1PcInstance pc, L1NpcInstance npc, int key) {
        Map<Integer, L1ShopS> list = pc.get_otherList().SHOPXMAP;

        pc.setTempID(0);

        final L1ShopS shopS = list.get(key);
        switch (shopS.get_end()) {
            case 0:// 出售中
                pc.setTempID(shopS.get_item_obj_id());
                final SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                final String overTime = sdf.format(shopS.get_overtime());// 到期时间

                final String[] info = new String[] {
                        shopS.get_item().getLogName() + "("
                                + shopS.get_item().getCount() + ")",
                        String.valueOf(shopS.get_adena()), overTime };
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_x_5", info));
                break;

            case 1:// 已售出未领回
                shopS.set_end(2);
                DwarfShopReading.get().updateShopS(shopS);
                CreateNewItem.createNewItem(pc, _adenaid, shopS.get_adena());

                // 关闭对话窗
                pc.sendPackets(new S_CloseList(pc.getId()));
                break;

            case 2:// 已售出已领回
                   // 6158 该托售物品收入已领回
                pc.sendPackets(new S_ServerMessage(166, "该托售物品收入已领回"));

                // 关闭对话窗
                pc.sendPackets(new S_CloseList(pc.getId()));
                break;

            case 3:// 未售出未领回
                shopS.set_end(4);
                shopS.set_item(null);
                DwarfShopReading.get().updateShopS(shopS);
                L1ItemInstance item = DwarfShopReading.get().allItems()
                        .get(shopS.get_item_obj_id());
                DwarfShopReading.get().deleteItem(shopS.get_item_obj_id());
                pc.getInventory().storeTradeItem(item);
                pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // 获得0%。

                // 关闭对话窗
                pc.sendPackets(new S_CloseList(pc.getId()));
                break;

            case 4:// 未售出已领回
                   // 6159 该托售物品已领回
                pc.sendPackets(new S_ServerMessage(166, "该托售物品已领回"));

                // 关闭对话窗
                pc.sendPackets(new S_CloseList(pc.getId()));
                break;
        }
        // 清空拍卖物品暂存
        pc.get_other().set_item_objid(0);
    }

    /**
     * 展示出售纪录
     * 
     * @param pc
     * @param npcobjid
     * @param page
     */
    public static void showPage(L1PcInstance pc, int npcobjid, int page) {
        Map<Integer, L1ShopS> list = pc.get_otherList().SHOPXMAP;
        if (list == null) {
            return;
        }

        // 全部页面数量
        int allpage = list.size() / 10;
        if ((page > allpage) || (page < 0)) {
            page = 0;
        }

        if (list.size() % 10 != 0) {
            allpage += 1;
        }

        pc.get_other().set_page(page);// 设置页面

        final int or = page * 10;
        // System.out.println("OR:"+or);

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((page + 1) + "/" + allpage + ",");

        // 每页显示10笔(showId + 10)资料
        for (int key = or; key < or + 10; key++) {
            final L1ShopS shopS = list.get(key);
            if (shopS != null) {
                stringBuilder.append(shopS.get_none() + " / "
                        + shopS.get_adena() + ",");
                switch (shopS.get_end()) {
                    case 0:// 出售中
                        stringBuilder.append("出售中,");
                        break;

                    case 1:// 已售出未领回
                        stringBuilder.append("已售出未领回,");
                        break;

                    case 2:// 已售出已领回
                        stringBuilder.append("已售出已领回,");
                        break;

                    case 3:// 未售出未领回
                        stringBuilder.append("未售出未领回,");
                        break;

                    case 4:// 未售出已领回
                        stringBuilder.append("未售出已领回,");
                        break;
                }

            } else {
                stringBuilder.append(" ,");// 无该编号 显示为空
            }
        }

        if (allpage >= (page + 1)) {
            String out = stringBuilder.toString();
            String[] clientStrAry = out.split(",");
            pc.sendPackets(new S_NPCTalkReturn(npcobjid, "y_x_2", clientStrAry));

        } else {
            // $6157 没有可以显示的项目
            pc.sendPackets(new S_ServerMessage(166, "没有可以显示的项目"));
        }
    }
}
