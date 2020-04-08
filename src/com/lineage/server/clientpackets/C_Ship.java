package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_OwnCharPack;

public class C_Ship extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_Ship.class);

    /*
     * public C_Ship() { }
     * 
     * public C_Ship(final byte[] abyte0, final ClientExecutor client) {
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
            final int mapId = pc.getMapId();

            int item_id = 0;
            switch (mapId) {
                case 5: // Talking Island Ship to Aden Mainland
                    item_id = 40299;
                    break;

                case 6: // Aden Mainland Ship to Talking Island
                    item_id = 40298;
                    break;

                case 83: // Forgotten Island Ship to Aden Mainland
                    item_id = 40300;
                    break;

                case 84: // Aden Mainland Ship to Forgotten Island
                    item_id = 40301;
                    break;

                case 446: // Ship Hidden dock to Pirate island
                    item_id = 40303;
                    break;

                case 447: // Ship Pirate island to Hidden dock
                    item_id = 40302;
                    break;
            }

            if (item_id != 0) {
                pc.getInventory().consumeItem(item_id, 1);

                final int shipMapId = this.readH();
                final int locX = this.readH();
                final int locY = this.readH();

                pc.sendPackets(new S_OwnCharPack(pc));
                L1Teleport
                        .teleport(pc, locX, locY, (short) shipMapId, 0, false);
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
