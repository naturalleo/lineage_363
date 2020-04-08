package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.BuddyReading;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 要求删除朋友名单
 * 
 * @author daien
 * 
 */
public class C_DelBuddy extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_DelBuddy.class);

    /*
     * public C_DelBuddy() { }
     * 
     * public C_DelBuddy(final byte[] abyte0, final ClientExecutor client) {
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
            final String charName = this.readS().toLowerCase();

            if (charName.isEmpty()) {
                return;
            }

            BuddyReading.get().removeBuddy(pc.getId(), charName);

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
