package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.ItemRestrictionsTable;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.L1Trade;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

/**
 * 要求增加交易物品(商店/个人交易)
 * 
 * @author daien
 * 
 */
public class C_TradeAddItem extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_TradeAddItem.class);

    /*
     * public C_TradeAddItem() { }
     * 
     * public C_TradeAddItem(final byte[] abyte0, final ClientExecutor client) {
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

            // 物品OBJID
            final int itemObjid = this.readD();
            // 交易数量
            long itemcount = this.readD();

            if (itemcount > Integer.MAX_VALUE) {
                itemcount = Integer.MAX_VALUE;
            }
            itemcount = Math.max(0, itemcount);
            final L1ItemInstance item = pc.getInventory().getItem(itemObjid);

            // 物品为空
            if (item == null) {
                return;
            }

            if (item.getCount() <= 0) {
                return;
            }

            if (!pc.isGm()) {
                if (!item.getItem().isTradable()) {
                    // \f1%0%d是不可转移的…
                    pc.sendPackets(new S_ServerMessage(210, item.getItem()
                            .getNameId()));
                    return;
                }

                if (item.getBless() >= 128) { // 封印装备
                    // \f1%0%d是不可转移的…
                    pc.sendPackets(new S_ServerMessage(210, item.getItem()
                            .getNameId()));
                    return;
                }

                if (item.get_time() != null) {
                    // \f1%0%d是不可转移的…
                    pc.sendPackets(new S_ServerMessage(210, item.getItem()
                            .getNameId()));
                    return;
                }
                if (ItemRestrictionsTable.RESTRICTIONS.contains(item
                        .getItemId())) {
                    // \f1%0%d是不可转移的…
                    pc.sendPackets(new S_ServerMessage(210, item.getItem()
                            .getNameId()));
                    return;
                }
            }

            if (item.isEquipped()) {
                // \f1你不能够将转移已经装备的物品。
                pc.sendPackets(new S_ServerMessage(141));
                return;
            }

            // 取回宠物列表
            final Object[] petlist = pc.getPetList().values().toArray();
            for (final Object petObject : petlist) {
                if (petObject instanceof L1PetInstance) {
                    final L1PetInstance pet = (L1PetInstance) petObject;
                    if (item.getId() == pet.getItemObjId()) {
                        // 1,187：宠物项链正在使用中。
                        pc.sendPackets(new S_ServerMessage(1187));
                        return;
                    }
                }
            }

            // 取回娃娃
            if (pc.getDoll(item.getId()) != null) {
                // 1,181：这个魔法娃娃目前正在使用中。
                pc.sendPackets(new S_ServerMessage(1181));
                return;
            }

            final L1PcInstance tradingPartner = (L1PcInstance) World.get()
                    .findObject(pc.getTradeID());
            if (tradingPartner == null) {
                return;
            }
            if (pc.getTradeOk()) {
                return;
            }
            // 容量重量确认
            if (tradingPartner.getInventory().checkAddItem(item, itemcount) != L1Inventory.OK) {
                // \f1当你负担过重时不能交易。
                tradingPartner.sendPackets(new S_ServerMessage(270));
                // \f1对方携带的物品过重，无法交易。
                pc.sendPackets(new S_ServerMessage(271));
                return;
            }

            final L1Trade trade = new L1Trade();
            if (itemcount <= 0) {
                _log.error("要求增加交易物品传回数量小于等于0: " + pc.getName() + ":"
                        + (pc.getNetConnection().kick()));
                return;
            }
            trade.tradeAddItem(pc, itemObjid, itemcount);

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
