package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.CastleReading;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Castle;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;

/**
 * 要求领出资金
 * 
 * @author daien
 * 
 */
public class C_Drawal extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_Drawal.class);

    /*
     * public C_Drawal() { }
     * 
     * public C_Drawal(final byte[] abyte0, final ClientExecutor client) {
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
            
            if (ServerWarExecutor.get().checkCastleWar() > 0) {
            	pc.sendPackets(new S_ServerMessage("\\aD城堡战争中不可以取出资金。"));
            	return;
            }

            final int objid = this.readD();

            long count = this.readD();
            if (count > Integer.MAX_VALUE) {
                count = Integer.MAX_VALUE;
            }
            count = Math.max(0, count);
            final L1Clan clan = WorldClan.get().getClan(pc.getClanname());

            if (clan != null) {
                final int castle_id = clan.getCastleId();
                if (castle_id != 0) {
                    final L1Castle l1castle = CastleReading.get()
                            .getCastleTable(castle_id);
                    long money = l1castle.getPublicMoney();
                    money -= count;
                    final L1ItemInstance item = ItemTable.get().createItem(
                            L1ItemId.ADENA);
                    if (item != null) {
                        l1castle.setPublicMoney(money);
                        CastleReading.get().updateCastle(l1castle);
                        if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
                            pc.getInventory().storeItem(L1ItemId.ADENA, count);

                        } else {
                            World.get()
                                    .getInventory(pc.getX(), pc.getY(),
                                            pc.getMapId())
                                    .storeItem(L1ItemId.ADENA, count);
                        }

                        // \f1%0%s 给你 %1%o 。
                        pc.sendPackets(new S_ServerMessage(143, "$457", "$4"
                                + " (" + count + ")"));
//                        World.get()
//                        .broadcastPacketToAll(
//                                new S_HelpMessage("\\aD" + pc.getName(), "取出城税资金:" + " (" + count + ")"));
                    }
                }
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
