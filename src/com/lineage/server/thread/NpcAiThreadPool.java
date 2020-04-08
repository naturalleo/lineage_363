package com.lineage.server.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 线程管理中心
 * 
 * @author dexc
 * 
 */
public class NpcAiThreadPool {

    private static final Log _log = LogFactory.getLog(NpcAiThreadPool.class);

    private static NpcAiThreadPool _instance;

    private static final int SCHEDULED_CORE_POOL_SIZE = 100;

    // 执行已提交的 Runnable 任务的对象。
    // 此接口提供一种将任务提交与每个任务将如何运行的机制（包括线程使用的细节、调度等）分离开来的方法。
    // 通常使用 Executor 而不是显式地创建线程。例如，可能会使用以下方法，
    // 而不是为一组任务中的每个任务调用 new Thread(new(RunnableTask())).start()：
    private Executor _executor;

    // 一个 ExecutorService，可安排在给定的延迟后运行或定期执行的命令。
    private ScheduledExecutorService _scheduler;

    public static NpcAiThreadPool get() {
        if (_instance == null) {
            _instance = new NpcAiThreadPool();
        }
        return _instance;
    }

    private NpcAiThreadPool() {
        // 创建一个可根据需要创建新线程的线程池，但是在以前构造的线程可用时将重用它们。
        _executor = Executors.newCachedThreadPool();

        // 常规(创建一个线程池，它可安排在给定延迟后运行命令或者定期地执行。)
        _scheduler = Executors.newScheduledThreadPool(SCHEDULED_CORE_POOL_SIZE,
                new PriorityThreadFactory("NpcAiTPool", Thread.NORM_PRIORITY));
    }

    // Executor

    /**
     * 使该线程开始执行；Java 虚拟机调用该线程的 run 方法。
     * 
     * @param r
     */
    public void execute(final Runnable r) {
        try {
            if (_executor == null) {
                final Thread t = new Thread(r);
                t.start();

            } else {
                _executor.execute(r);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 使该线程开始执行；Java 虚拟机调用该线程的 run 方法。
     * 
     * @param t
     */
    public void execute(final Thread t) {
        try {
            t.start();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 创建并执行在给定延迟后启用的一次性操作。
     * 
     * @param r
     *            要执行的任务
     * @param delay
     *            从现在开始延迟执行的时间
     * @return
     */
    public ScheduledFuture<?> schedule(final Runnable r, final long delay) {
        try {
            if (delay <= 0) {
                _executor.execute(r);
                return null;
            }
            return _scheduler.schedule(r, delay, TimeUnit.MILLISECONDS);

        } catch (final RejectedExecutionException e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 根据需要创建新线程的对象。 使用线程工厂就无需再手工编写对 new Thread 的调用了，从而允许应用程序使用特殊的线程子类、属性等等。
     * 
     * @author daien
     * 
     */
    private class PriorityThreadFactory implements ThreadFactory {

        private final int _prio;

        private final String _name;

        private final AtomicInteger _threadNumber = new AtomicInteger(1);

        private final ThreadGroup _group;

        /**
         * PriorityThreadFactory
         * 
         * @param name
         *            线程名称
         * @param prio
         *            优先等级
         */
        public PriorityThreadFactory(final String name, final int prio) {
            this._prio = prio;
            this._name = name;
            this._group = new ThreadGroup(this._name);
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
         */
        @Override
        public Thread newThread(final Runnable r) {
            final Thread t = new Thread(this._group, r);
            t.setName(this._name + "-" + this._threadNumber.getAndIncrement());
            t.setPriority(this._prio);
            return t;
        }

        @SuppressWarnings("unused")
        public ThreadGroup getGroup() {
            return this._group;
        }
    }
}
