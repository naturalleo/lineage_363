package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.serverpackets.S_ServerVersion;

/**
 * ◎登入端用- 要求接收伺服器版本
 * 
 * @author daien
 * 
 */
public class C_ServerVersion extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_ServerVersion.class);

    /*
     * public C_ServerVersion() { }
     * 
     * public C_ServerVersion(final byte[] abyte0, final ClientExecutor client)
     * { super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            client.out().encrypt(new S_ServerVersion());

            // String ipaddr = client.getIp().toString();

            /*
             * if (LanSecurityManager.LOADMAP.containsKey(ipaddr)) { // 移出延迟检查清单
             * LanSecurityManager.LOADMAP.remove(ipaddr);
             * _log.info("IP连线正常 移出延迟登入IP位置: " + ipaddr); }
             */

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
