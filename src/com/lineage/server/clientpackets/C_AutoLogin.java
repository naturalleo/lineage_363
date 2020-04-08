package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;

/**
 * 自动登入
 * 
 * @author dexc
 * 
 */
public class C_AutoLogin extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_AutoLogin.class);

    /*
     * public C_5M() { }
     * 
     * public C_5M(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            int un = this.readC();
            _log.info("自动登入系统定位:" + un);

            String loginName = this.readS().toLowerCase();
            String srcpassword = this.readS();

            C_AuthLogin authLogin = new C_AuthLogin();
            authLogin.checkLogin(client, loginName, srcpassword, true);

            // 测试用
            /*
             * for (int i = 0 ; i < 50 ; i++) { final ClientExecutor clientx =
             * new ClientExecutor(client.get_socket()); C_AuthLogin authLoginx =
             * new C_AuthLogin(); authLoginx.checkLogin(clientx, "aaaa"+i,
             * "aaaa"+i, true); }
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
