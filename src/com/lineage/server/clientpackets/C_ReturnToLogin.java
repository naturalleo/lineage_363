package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.list.OnlineUser;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Account;

/**
 * 要求返回登录介面
 * 
 * @author daien
 * 
 */
public class C_ReturnToLogin extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_ReturnToLogin.class);

    /*
     * public C_ReturnToLogin() { }
     * 
     * public C_ReturnToLogin(final byte[] abyte0, final ClientExecutor client)
     * { super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            // this.read(decrypt);

            final L1PcInstance pc = client.getActiveChar();
            if (pc != null) {
                client.quitGame();
            }

            final L1Account account = client.getAccount();
            if (account != null) {
                OnlineUser.get().remove(account.get_login());
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
