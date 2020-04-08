package com.lineage.echo;

import java.io.BufferedOutputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.echo.encryptions.Encryption;
import com.lineage.server.serverpackets.OpcodesServer;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.StreamUtil;

/**
 * 封包加密管理
 * 
 * @author dexc
 * 
 */
public class EncryptExecutor extends OpcodesServer {

    private static final Log _log = LogFactory.getLog(EncryptExecutor.class);

    private final ClientExecutor _client;

    private final OutputStream _out;

    private final PacketSc _scPacket;

    private Encryption _keys;

    public EncryptExecutor(final ClientExecutor client, final OutputStream out) {
        _client = client;
        _keys = client.get_keys();
        _out = new BufferedOutputStream(out);
        _scPacket = new PacketSc(client, this);
    }

    /**
     * 送出第一组封包的处理
     */
    public void outStart() {
        try {
            synchronized (this) {
                if (_out != null) {
                    if (Config.LOGINS_TO_AUTOENTICATION) {
                        _out.write((int) (_client._authdata & 0xff));
                        _out.write((int) (_client._authdata >> 8 & 0xff));
                        _out.write((int) (_client._authdata >> 16 & 0xff));
                        _out.write((int) (_client._authdata >> 24 & 0xff));
                        _out.flush();
                    }

                    _out.write(OpcodesClient._firstPacket);

                    // 刷新此输出流并强制写出所有缓冲的输出字节。
                    _out.flush();// 清空资料
                    try {
                        _keys.initKeys(OpcodesClient._seed);

                    } catch (final Exception e) {
                        stop();
                        throw new EncryptErrorException("设置加密公式发生异常: "
                                + _client.getIp(), e);
                    }
                }
            }

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 往客户端封包 加密处理
     * 
     * @param serverBasePacket
     */
    public void encrypt(final ServerBasePacket packet) {
        try {
            if (packet == null) {
                return;
            }
            synchronized (this) {
                _scPacket.encrypt(packet);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public OutputStream out() {
        return _out;
    }

    public void satrt() {
        GeneralThreadPool.get().schedule(_scPacket, 0);
    }

    public void stop() {
        try {
            _scPacket.stop();
            StreamUtil.close(_out);

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);
        }
    }
}
