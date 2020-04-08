package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 要求删除物品
 * 
 * @author daien
 * 
 */
public class C_DeleteInventoryItem extends ClientBasePacket {

    private static final Log _log = LogFactory
            .getLog(C_DeleteInventoryItem.class);

    /*
     * public C_DeleteInventoryItem() { }
     * 
     * public C_DeleteInventoryItem(final byte[] abyte0, final ClientExecutor
     * client) { super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final L1PcInstance pc = client.getActiveChar();

            final int itemObjectId = this.readD();
            
            int count = this.readD();

            if (count > Integer.MAX_VALUE) {
                count = Integer.MAX_VALUE;
            }
            count = Math.max(1, count);

            final L1ItemInstance item = pc.getInventory().getItem(itemObjectId);

            // 物品为空
            if (item == null) {
                return;
            }
            if (item.getCount() <= 0) {
                return;
            }

            // 执行人物不是GM
            if (!pc.isGm()) {
                if (item.getItem().isCantDelete()) {
                    // 125 \f1你不能够放弃此样物品。
                    pc.sendPackets(new S_ServerMessage(125));
                    return;
                }
            }

            // 宠物
            final Object[] petlist = pc.getPetList().values().toArray();
            for (final Object petObject : petlist) {
                if (petObject instanceof L1PetInstance) {
                    final L1PetInstance pet = (L1PetInstance) petObject;
                    if (item.getId() == pet.getItemObjId()) {
                        // 125 \f1你不能够放弃此样物品。
                        pc.sendPackets(new S_ServerMessage(125));
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

            if (item.isEquipped()) {
                // 125 \f1你不能够放弃此样物品。
                pc.sendPackets(new S_ServerMessage(125));
                return;
            }
            if (item.getBless() >= 128) { // 封印的装备
                // 210 \f1%0%d是不可转移的…
                pc.sendPackets(new S_ServerMessage(210, item.getItem()
                        .getNameId()));
                return;
            }
            _log.info("人物:" + pc.getName() + "删除物品" + item.getNumberedName_to_String()
                    + " 物品OBJID:" + item.getId());
            pc.getInventory().removeItem(item, count);
            pc.turnOnOffLight();

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
