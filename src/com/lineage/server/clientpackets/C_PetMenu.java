package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.serverpackets.S_PetInventory;
import com.lineage.server.world.WorldPet;

/**
 * 要求宠物回报选单
 * 
 * @author daien
 * 
 */
public class C_PetMenu extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_PetMenu.class);

    /*
     * public C_PetMenu() { }
     * 
     * public C_PetMenu(final byte[] abyte0, final ClientExecutor client) {
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

            if (pc == null) {
                return;
            }

            if (pc.isGhost()) { // 鬼魂模式
                return;
            }

            if (pc.isTeleport()) { // 传送中
                return;
            }

            if (pc.isPrivateShop()) { // 商店村模式
                return;
            }

            final int petId = this.readD();

            final L1PetInstance pet = WorldPet.get().get(petId);
            if (pet == null) {
                return;
            }
            if (pc.getPetList().get(petId) == null) {
                return;
            }

            pc.sendPackets(new S_PetInventory(pet, true));

            /*
             * if ((obj != null) && (pc != null)) { if (obj instanceof
             * L1PetInstance) { final L1PetInstance pet = (L1PetInstance) obj;
             * pc.sendPackets(new S_PetInventory(pet)); } }
             */

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
