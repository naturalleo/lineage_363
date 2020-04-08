package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.CharBookReading;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 要求删除记忆座标
 * 
 * @author daien
 * 
 */
public class C_DeleteBookmark extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_DeleteBookmark.class);

    /*
     * public C_DeleteBookmark() { }
     * 
     * public C_DeleteBookmark(final byte[] abyte0, final ClientExecutor client)
     * { super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final String bookmarkname = this.readS();

            if (!bookmarkname.isEmpty()) {
                final L1PcInstance pc = client.getActiveChar();
                CharBookReading.get().deleteBookmark(pc, bookmarkname);
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
