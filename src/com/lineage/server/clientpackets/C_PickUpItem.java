package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.ActionCodes;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

/**
 * 要求捡取物品
 * 
 * @author daien
 * 
 */
public class C_PickUpItem extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_PickUpItem.class);

    /*
     * public C_PickUpItem() { }
     * 
     * public C_PickUpItem(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    //修正双重捡取刷物品 hjx1000
    public synchronized void start(final byte[] decrypt, final ClientExecutor client) {
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

            if (pc.isInvisble()) { // 隐身状态
                return;
            }

            if (pc.isInvisDelay()) { // 隐身延迟
                return;
            }

            final int x = this.readH();
            final int y = this.readH();
            final int objectId = this.readD();
            long pickupCount = this.readD();
            if (pickupCount > Integer.MAX_VALUE) {
                pickupCount = Integer.MAX_VALUE;
            }
            pickupCount = Math.max(0, pickupCount);
            final L1Inventory groundInventory = World.get().getInventory(x, y,
                    pc.getMapId());

            final L1Object object = groundInventory.getItem(objectId);
            if ((object != null) && !pc.isDead()) {
                final L1ItemInstance item = (L1ItemInstance) object;
                if (item.getCount() <= 0) {
                    return;
                }
                if ((item.getItemOwnerId() != 0)
                        && (pc.getId() != item.getItemOwnerId())) {
                    // 道具取得失败。
                    pc.sendPackets(new S_ServerMessage(623));
                    return;
                }
                if (pc.getLocation().getTileLineDistance(item.getLocation()) > 3) {
                    return;
                }
                item.set_showId(-1);
                // 容量重量确认
                if (pc.getInventory().checkAddItem(item, pickupCount) == L1Inventory.OK) {
                    if ((item.getX() != 0) && (item.getY() != 0)) {
                        groundInventory.tradeItem(item, pickupCount,
                                pc.getInventory());
                        // 改变亮度
                        pc.turnOnOffLight();

                        // 改变面向
                        pc.setHeading(pc.targetDirection(item.getX(),
                                item.getY()));

                        // 因应改变面向 使用物件攻击封包送出动作以及面向
                        // 不需要对自己送
                        // pc.sendPackets(new S_ChangeHeading(pc));
                        // 送出封包(动作)
                        // pc.sendPacketsAll(new S_DoActionGFX(pc.getId(),
                        // ActionCodes.ACTION_Pickup));
                        // pc.sendPackets(new S_AttackPickUpItem(pc, objectId));
                        if (!pc.isGmInvis()) {
                            pc.broadcastPacketAll(new S_ChangeHeading(pc));
                            // 送出封包(动作)
                            pc.sendPacketsAll(new S_DoActionGFX(pc.getId(),
                                    ActionCodes.ACTION_Pickup));
                            // pc.broadcastPacketAll(new S_AttackPickUpItem(pc,
                            // objectId));
                        }
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
