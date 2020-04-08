package com.lineage.server.utils;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StreamUtil {

    private static final Log _log = LogFactory.getLog(StreamUtil.class);

    /**
     * 关闭此流并释放与此流关联的所有系统资源。如果已经关闭该流，则调用此方法无效。
     * 
     * @param closeables
     */
    public static void close(final Closeable... closeables) {
        for (final Closeable c : closeables) {
            try {
                if (c != null) {
                    c.close();
                }

            } catch (final IOException e) {
                _log.error("关闭Closeable发生异常", e);
            }
        }
    }

    /**
     * 请求取消此键的通道到其选择器的注册
     * 
     * @param keys
     */
    public static void close(final SelectionKey... keys) {
        for (final SelectionKey key : keys) {
            if (key != null) {
                key.cancel();
            }
        }
    }

    /**
     * 关闭此选择器
     * 
     * @param keys
     */
    public static void close(final Selector... selectors) {
        for (final Selector selector : selectors) {
            try {
                if (selector != null) {
                    selector.close();
                }

            } catch (final IOException e) {
                _log.error("关闭Selector发生异常", e);
            }
        }
    }

    /**
     * 关闭此套接字。 所有当前阻塞于此套接字上的 I/O 操作中的线程都将抛出 SocketException。
     * 
     * 套接字被关闭后，便不可在以后的网络连接中使用（即无法重新连接或重新绑定）。需要创建新的套接字。
     * 
     * 关闭此套接字也将会关闭该套接字的 InputStream 和 OutputStream。
     * 
     * 如果此套接字有一个与之关联的通道，则关闭该通道。
     * 
     * @param csocket
     */
    public static void close(final Socket csocket) {
        try {
            if (!csocket.isClosed()) {
                csocket.shutdownInput();
                csocket.shutdownOutput();
                csocket.close();
            }

        } catch (final IOException e) {
            _log.error("关闭Socket发生异常", e);
        }
    }

    /**
     * 关闭此套接字。 在 accept() 中所有当前阻塞的线程都将会抛出 SocketException。
     * 如果此套接字有一个与之关联的通道，则关闭该通道。
     * 
     * @param server
     */
    public static void close(final ServerSocket server) {
        try {
            if (!server.isClosed()) {
                server.close();
            }

        } catch (final IOException e) {
            _log.error("关闭ServerSocket发生异常", e);
        }
    }

    /**
     * 中断线程。 如果当前线程没有中断它自己（这在任何情况下都是允许的）， 则该线程的 checkAccess 方法就会被调用，这可能抛出
     * SecurityException。
     * 
     * 如果线程在调用 Object 类的 wait()、wait(long) 或 wait(long, int) 方法， 或者该类的
     * join()、join(long)、join(long, int)、sleep(long) 或 sleep(long, int)
     * 方法过程中受阻，则其中断状态将被清除， 它还将收到一个 InterruptedException。
     * 
     * 如果该线程在可中断的通道上的 I/O 操作中受阻，则该通道将被关闭， 该线程的中断状态将被设置并且该线程将收到一个
     * ClosedByInterruptException。
     * 
     * 如果该线程在一个 Selector 中受阻，则该线程的中断状态将被设置，它将立即从选择操作返回， 并可能带有一个非零值，就好像调用了选择器的
     * wakeup 方法一样。
     * 
     * 如果以前的条件都没有保存，则该线程的中断状态将被设置。
     * 
     * 中断一个不处于活动状态的线程不需要任何作用。
     * 
     * @param serverExecutor
     */
    public static void interrupt(final Thread thread) {
        try {
            if (thread.isAlive()) {
                thread.interrupt();
            }

        } catch (final Exception e) {
            _log.error("关闭Thread发生异常", e);
        }
    }
}
