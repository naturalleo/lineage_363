package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;

/**
 * 要求顾用佣兵
 * 
 * @author daien
 * 
 */
public class C_HireSoldier extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_HireSoldier.class);

    /*
     * public C_HireSoldier() { }
     * 
     * public C_HireSoldier(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final int something1 = this.readH(); // S_HireSoldierパケットの引数
            final int something2 = this.readH(); // S_HireSoldierパケットの引数
            final int something3 = this.readD(); // 1以外入らない？
            final int something4 = this.readD(); // S_HireSoldierパケットの引数
            final int number = this.readH(); // 雇用する数

            // < 佣兵雇用处理

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
