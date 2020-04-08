package com.lineage.server.clientpackets;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.ActionCodes;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_DoActionShop;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1PrivateShopBuyList;
import com.lineage.server.templates.L1PrivateShopSellList;

/**
 * 要求开设个人商店
 * 
 * @author daien
 * 
 */
public class C_Shop extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_Shop.class);

    /*
     * public C_Shop() { }
     * 
     * public C_Shop(final byte[] abyte0, final ClientExecutor client) {
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

            final int mapId = pc.getMapId();
            if ((mapId != 340) && (mapId != 350) && (mapId != 360)
                    && (mapId != 370) && (mapId != 4)) { //增加大陆可以摆商店 hjx1000
                // 876 无法在此开设个人商店。
                pc.sendPackets(new S_ServerMessage(876));
                return;
            }
//            if (mapId == 4 && pc.getLevel() < 30) { //限制30级以上玩家在大陆地面摆个人商店 hjx1000
//                // 876 无法在此开设个人商店。
//                pc.sendPackets(new S_ServerMessage(876));
//                return;
//            }

            final ArrayList<L1PrivateShopSellList> sellList = pc.getSellList();
            final ArrayList<L1PrivateShopBuyList> buyList = pc.getBuyList();
			if (!sellList.isEmpty()) {
                sellList.clear();
			}
			if (!buyList.isEmpty()) {
				buyList.clear();
			}
            L1ItemInstance checkItem;
            boolean tradable = true;

            final int type = this.readC();
            if (type == 0) { // 开始
                final int sellTotalCount = this.readH();// 出售道具
                for (int i = 0; i < sellTotalCount; i++) {
                    final int sellObjectId = this.readD();
                    final int sellPrice = Math.max(0, this.readD());
                    if (sellPrice <= 0) {
                        _log.error("要求开设个人商店传回金币小于等于0: " + pc.getName()
                                + (pc.getNetConnection().kick()));
                        break;
                    }
                    final int sellCount = Math.max(0, this.readD());
                    if (sellCount <= 0) {
                        _log.error("要求开设个人商店传回数量小于等于0: " + pc.getName()
                                + (pc.getNetConnection().kick()));
                        break;
                    }
                    // 取引可能なアイテムかチェック
                    checkItem = pc.getInventory().getItem(sellObjectId);
                    if (!checkItem.getItem().isTradable()) {
                        tradable = false;
                        // 1497 此道具无法在[个人商店]上贩售。
                        pc.sendPackets(new S_ServerMessage(1497));
                    }

                    if (checkItem.get_time() != null) {
                        // 1497 此道具无法在[个人商店]上贩售。
                        pc.sendPackets(new S_ServerMessage(1497));
                        tradable = false;
                    }

                    if (checkItem.isEquipped()) {
                        // \f1你不能够将转移已经装备的物品。
                        pc.sendPackets(new S_ServerMessage(141));
                        return;
                    }

                    // 取回宠物列表
                    final Object[] petlist = pc.getPetList().values().toArray();
                    for (final Object petObject : petlist) {
                        if (petObject instanceof L1PetInstance) {
                            final L1PetInstance pet = (L1PetInstance) petObject;
                            if (checkItem.getId() == pet.getItemObjId()) {
                                tradable = false;
                                // 1,187：宠物项链正在使用中。
                                pc.sendPackets(new S_ServerMessage(1187));
                                return;
                            }
                        }
                    }

                    // 取回娃娃
                    if (pc.getDoll(checkItem.getId()) != null) {
                        // 1,181：这个魔法娃娃目前正在使用中。
                        pc.sendPackets(new S_ServerMessage(1181));
                        return;
                    }                    
                    
                    final L1PrivateShopSellList pssl = new L1PrivateShopSellList();
                    pssl.setItemObjectId(sellObjectId);
                    pssl.setSellPrice(sellPrice);
                    pssl.setSellTotalCount(sellCount); 
                    //pssl.setSellTotalCount((int) checkItem.getCount());//修改为所有数量物品出售
                    sellList.add(pssl);
                }

                final int buyTotalCount = this.readH();// 买入道具
                for (int i = 0; i < buyTotalCount; i++) {
                    final int buyObjectId = this.readD();
                    final int buyPrice = Math.max(0, this.readD());
                    if (buyPrice <= 0) {
                        _log.error("要求买入道具传回金币小于等于0: " + pc.getName()
                                + (pc.getNetConnection().kick()));
                        break;
                    }
                    final int buyCount = Math.max(0, this.readD());
                    if (buyCount <= 0) {
                        _log.error("要求买入道具传回数量小于等于0: " + pc.getName()
                                + (pc.getNetConnection().kick()));
                        break;
                    }
                    // 取引可能なアイテムかチェック
                    checkItem = pc.getInventory().getItem(buyObjectId);

                    if (checkItem.getCount() <= 0) {
                        continue;
                    }

                    if (!checkItem.getItem().isTradable()) {
                        tradable = false;
                        // 1497 此道具无法在[个人商店]上贩售
                        pc.sendPackets(new S_ServerMessage(1497));
                    }

                    if (checkItem.getBless() >= 128) { // 封印的装备
                        // 1497 此道具无法在[个人商店]上贩售
                        pc.sendPackets(new S_ServerMessage(1497));
                        return;
                    }

                    if (checkItem.isEquipped()) {
                        // \f1你不能够将转移已经装备的物品。
                        pc.sendPackets(new S_ServerMessage(141));
                        return;
                    }

                    // 取回宠物列表
                    final Object[] petlist = pc.getPetList().values().toArray();
                    for (final Object petObject : petlist) {
                        if (petObject instanceof L1PetInstance) {
                            final L1PetInstance pet = (L1PetInstance) petObject;
                            if (checkItem.getId() == pet.getItemObjId()) {
                                tradable = false;
                                // 1,187：宠物项链正在使用中。
                                pc.sendPackets(new S_ServerMessage(1187));
                                return;
                            }
                        }
                    }

                    // 取回娃娃
                    if (pc.getDoll(checkItem.getId()) != null) {
                        // 1,181：这个魔法娃娃目前正在使用中。
                        pc.sendPackets(new S_ServerMessage(1181));
                        return;
                    }

                    final L1PrivateShopBuyList psbl = new L1PrivateShopBuyList();
                    psbl.setItemObjectId(buyObjectId);
                    psbl.setBuyPrice(buyPrice);
                    psbl.setBuyTotalCount(buyCount);
                    buyList.add(psbl);
                }
                if (!tradable) { // 取引不可能なアイテムが含まれている场合、个人商店终了
                    sellList.clear();
                    buyList.clear();
                    pc.setPrivateShop(false);
                    pc.sendPacketsAll(new S_DoActionGFX(pc.getId(),
                            ActionCodes.ACTION_Idle));
                    return;
                }

                byte[] chat = this.readByte();
                pc.setShopChat(chat);
                pc.setPrivateShop(true);
                pc.sendPacketsAll(new S_DoActionShop(pc.getId(), chat));

            } else if (type == 1) { // 终了
                sellList.clear();
                buyList.clear();
                pc.setPrivateShop(false);
                pc.sendPacketsAll(new S_DoActionGFX(pc.getId(),
                        ActionCodes.ACTION_Idle));
            }

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
