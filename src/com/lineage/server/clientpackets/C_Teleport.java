package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.Teleportation;

/**
 * 要求更新周围物件
 * 
 * @author daien
 * 
 */
public class C_Teleport extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_Teleport.class);

    /*
     * public C_Teleport() { }
     * 
     * public C_Teleport(final byte[] abyte0, final ClientExecutor client) {
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
            Teleportation.teleportation(pc);

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
