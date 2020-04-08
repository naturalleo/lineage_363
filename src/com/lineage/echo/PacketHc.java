package com.lineage.echo;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 客户资料处理
 * 
 * @author dexc
 * 
 */
public class PacketHc implements Runnable {

    private static final Log _log = LogFactory.getLog(PacketHc.class);

    private final Queue<byte[]> _queue;

    private final PacketHandlerExecutor _handler;

    private final ClientExecutor _executor;

    public PacketHc(ClientExecutor executor) {
        _executor = executor;
        _queue = new ConcurrentLinkedQueue<byte[]>();
        _handler = new PacketHandler(_executor);
    }

    public PacketHc(ClientExecutor executor, final int capacity) {
        _executor = executor;
        _queue = new LinkedBlockingQueue<byte[]>(capacity);
        _handler = new PacketHandler(_executor);
    }

    public void requestWork(final byte data[]) {
        _queue.offer(data);
    }

    @Override
    public void run() {
        try {
            while (_executor.get_socket() != null) {
                for (final Iterator<byte[]> iter = _queue.iterator(); iter
                        .hasNext();) {
                    final byte[] decrypt = iter.next();
                    iter.remove();
                    _handler.handlePacket(decrypt);
                    Thread.sleep(1);
                }
                // 队列为空 休眠
                Thread.sleep(10);
            }

            // 垃圾回收
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
}
