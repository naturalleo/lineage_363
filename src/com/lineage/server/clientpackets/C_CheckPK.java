package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 要求查询PK次数
 * 
 * @author daien
 * 
 */
public class C_CheckPK extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_CheckPK.class);

    /*
     * public C_CheckPK() { }
     * 
     * public C_CheckPK(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            // this.read(decrypt);

            final L1PcInstance pc = client.getActiveChar();

            String count = String.valueOf(pc.get_PKcount());
            // 你的PK次数为%0次。
            pc.sendPackets(new S_ServerMessage(562, count));

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
