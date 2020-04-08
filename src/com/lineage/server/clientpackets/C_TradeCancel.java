package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.L1Trade;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 要求取消交易(个人/商店)
 * 
 * @author daien
 * 
 */
public class C_TradeCancel extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_TradeCancel.class);

    /*
     * public C_TradeCancel() { }
     * 
     * public C_TradeCancel(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            // this.read(decrypt);

            final L1PcInstance player = client.getActiveChar();
            final L1Trade trade = new L1Trade();
            trade.tradeCancel(player);

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
