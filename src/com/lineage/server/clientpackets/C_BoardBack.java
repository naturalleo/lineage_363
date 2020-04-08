package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.world.WorldNpc;

/**
 * 要求下一页 ( 公布栏 )
 * 
 * @author dexc
 * 
 */
public class C_BoardBack extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_BoardBack.class);

    /*
     * public C_BoardBack() { }
     * 
     * public C_BoardBack(final byte[] abyte0, final ClientExecutor client) {
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
            final int topicNumber = this.readD();// 读取BBS的最后一笔资料ID

            final L1NpcInstance npc = WorldNpc.get().map().get(objId);
            if (npc == null) {
                return;
            }

            final L1PcInstance pc = client.getActiveChar();
            if (pc == null) {
                return;
            }

            if (npc.ACTION != null) {
                npc.ACTION.action(pc, npc, "n", topicNumber);
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
