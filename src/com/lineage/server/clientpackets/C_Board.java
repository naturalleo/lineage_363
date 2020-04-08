package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.world.WorldNpc;

/**
 * 要求读取公布栏/拍卖公告
 * 
 * @author dexc
 * 
 */
public class C_Board extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_Board.class);

    /**
     * 要求读取公布栏/拍卖公告
     */
    /*
     * public C_Board() { }
     */

    /**
     * 要求读取公布栏/拍卖公告
     * 
     * @param abyte0
     * @param client
     */
    /*
     * public C_Board(final byte[] abyte0, final ClientExecutor client) {
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

            final L1NpcInstance npc = WorldNpc.get().map().get(objId);
            if (npc == null) {
                return;
            }

            final L1PcInstance pc = client.getActiveChar();
            if (pc == null) {
                return;
            }

            if (npc.ACTION != null) {
                npc.ACTION.talk(pc, npc);
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
