package com.lineage.echo;

import java.io.IOException;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.echo.encryptions.Encryption;
import com.lineage.echo.encryptions.PacketPrint;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.types.UByte8;
import com.lineage.server.types.UChar8;

/**
 * 封包输出管理
 * 
 * @author dexc
 * 
 */
public class PacketSc implements Runnable {

    private static final Log _log = LogFactory.getLog(PacketSc.class);

    private static boolean _debug = Config.DEBUG;

    private final Queue<byte[]> _queue;// 队列

    private final ClientExecutor _client;

    private final EncryptExecutor _executor;

    //private Encryption _keys;
    private final Encryption _keys; // hjx1000

    public PacketSc(ClientExecutor client, EncryptExecutor executor) {
        _client = client;
        _keys = client.get_keys();
        _executor = executor;
        // 创建一个最初为空的 ConcurrentLinkedQueue。
        _queue = new ConcurrentLinkedQueue<byte[]>();
    }

    /**
     * 加入工作列队
     * 
     * @param data
     */
    private void requestWork(final byte data[]) {
        _queue.offer(data);
    }

    /**
     * 加密封包
     * 
     * @param packet
     * @throws Exception
     */
    public void encrypt(final ServerBasePacket packet) throws Exception {
        byte[] encrypt = packet.getContent();
        // _log.info("加密封包: 长度: "+encrypt.length);
        if ((encrypt.length > 0) && (_executor.out() != null)) {
//            final int opid = encrypt[0];
//            if (opid == -1) {
//                _log.error("拒绝发送: " + packet.getType() + " OPID: " + opid);
//                return;
//            } // hjx1000

            if (_debug) {
                _log.info("服务端: " + packet.getType() + "\nOP ID: "
                        + (encrypt[0] & 0xff) + "\nInfo:\n"
                        + PacketPrint.get().printData(encrypt, encrypt.length));
            }

            char ac[] = new char[encrypt.length];
            ac = UChar8.fromArray(encrypt);
            // 加密
            ac = _keys.encrypt(ac);
            if (ac == null) {
                return;
            }
            encrypt = UByte8.fromArray(ac);

            requestWork(encrypt);
        }
    }

    @Override
    public void run() {
        try {
            while (_client.get_socket() != null) {
                for (final Iterator<byte[]> iter = _queue.iterator(); iter
                        .hasNext();) {
                    final byte[] decrypt = iter.next();// 返回迭代的下一个元素。
                    // 从迭代器指向的 collection 中移除迭代器返回的最后一个元素
                    iter.remove();
                    outPacket(decrypt);
                    Thread.sleep(1);
                }
                // 队列为空 休眠
                Thread.sleep(10);
            }
            // finalize();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

            /*
             * } catch (Throwable e) { _log.error(e.getLocalizedMessage(), e);//
             */

        } finally {
            // 移除此 collection 中的所有元素
            _queue.clear();
        }
    }

    /**
     * 输出封包
     * 
     * @param decrypt
     */
    private void outPacket(final byte[] decrypt) {
        try {
            final int outLength = decrypt.length + 2;
            // 将指定的位元组写入此输出流。
            _executor.out().write(outLength & 0xff);
            _executor.out().write(outLength >> 8 & 0xff);
            // 将 decrypt.length 个位元组从指定的 byte 阵列写入此输出流。
            _executor.out().write(decrypt);
            // 刷新此输出流并强制写出所有缓冲的输出位元组。
            _executor.out().flush();

            /*
             * long tt = System.currentTimeMillis();
             * _log.info("加密封包: 长度: "+decrypt.length+"/"+(tt -
             * _client.TTL.get(1))); _client.TTL.put(1, tt);
             */

        } catch (final IOException e) {
            // 输出异常
            // _log.error(e.getLocalizedMessage(), e);
            _executor.stop();
        }
    }

    public void stop() {
        // 移除此 collection 中的所有元素
        _queue.clear();
    }
}
