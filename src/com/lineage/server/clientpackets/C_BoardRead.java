package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.world.WorldNpc;

/**
 * 要求阅读布告栏讯息
 * 
 * @author dexc
 * 
 */
public class C_BoardRead extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_BoardRead.class);

    /*
     * public C_BoardRead() { }
     * 
     * public C_BoardRead(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client)
            throws Exception {
        try {
            // 资料载入
            this.read(decrypt);

            final int objId = this.readD();
            final int topicNumber = this.readD();

            final L1NpcInstance npc = WorldNpc.get().map().get(objId);
            if (npc == null) {
                return;
            }

            final L1PcInstance pc = client.getActiveChar();
            if (pc == null) {
                return;
            }

            if (npc.ACTION != null) {
                npc.ACTION.action(pc, npc, "r", topicNumber);
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
