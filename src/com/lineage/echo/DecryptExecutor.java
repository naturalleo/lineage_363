package com.lineage.echo;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.echo.DecryptErrorException;
import com.lineage.echo.encryptions.Encryption;
import com.lineage.server.utils.StreamUtil;

/**
 * 封包解密管理
 * 
 * @author dexc
 * 
 */
public class DecryptExecutor {

    private static final Log _log = LogFactory.getLog(DecryptExecutor.class);

    private final ClientExecutor _client;

    private final InputStream _in;

    private Encryption _keys;

    public DecryptExecutor(final ClientExecutor client, final InputStream in) {
        _client = client;
        _keys = client.get_keys();
        _in = in;
    }

    public ClientExecutor get_client() {
        return _client;
    }

    /**
     * 客户端封包解密处理(IO)
     * 
     * @return
     * @throws Exception
     */
    public byte[] decrypt() throws Exception {

        try {
            int hiByte = this._in.read();
            int loByte = this._in.read();

            if (loByte < 0) {
                throw new DecryptErrorException();
            }

            // TODO 伺服器捆绑
            if (Config.LOGINS_TO_AUTOENTICATION) {
                hiByte ^= _client._xorByte;
                loByte ^= _client._xorByte;
            }

            final int dataLength = ((loByte << 8) + hiByte) - 2;

            final byte data[] = new byte[dataLength];

            int readSize = 0;

            for (int i = 0; (i != -1) && (readSize < dataLength); readSize += i) {
                i = this._in.read(data, readSize, dataLength - readSize);
            }

            if (readSize != dataLength) {
                throw new RuntimeException();
            }

            if (Config.LOGINS_TO_AUTOENTICATION) {
                for (int i = 0; i < dataLength; i++) {
                    data[i] = (byte) (data[i] ^ _client._xorByte);
                }
            }

            return _keys.decrypt(data);

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);
            throw new DecryptErrorException();
        }
    }

    public void stop() {
        try {
            StreamUtil.close(this._in);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
