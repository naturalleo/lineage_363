package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.world.WorldNpc;

/**
 * 要求删除公布栏内容
 * 
 * @author dexc
 * 
 */
public class C_BoardDelete extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_BoardDelete.class);

    /*
     * public C_BoardDelete() { }
     * 
     * public C_BoardDelete(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final int objId = this.readD();
            final int topicId = this.readD();

            final L1NpcInstance npc = WorldNpc.get().map().get(objId);
            if (npc == null) {
                return;
            }

            final L1PcInstance pc = client.getActiveChar();
            if (pc == null) {
                return;
            }

            if (npc.ACTION != null) {
                npc.ACTION.action(pc, npc, "d", topicId);
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
