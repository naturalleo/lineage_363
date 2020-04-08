package com.lineage.server.thread;

import java.util.TimerTask;
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

import com.lineage.config.Config;
import com.lineage.server.model.monitor.L1PcMonitor;

/**
 * 线程管理中心(PC)
 * 
 * @author dexc
 * 
 */
public class PcOtherThreadPool {

    private static final Log _log = LogFactory.getLog(PcOtherThreadPool.class);

    private static PcOtherThreadPool _instance;

    private Executor _executor;

    // 一个 ExecutorService，可安排在给定的延迟后运行或定期执行的命令。
    private ScheduledExecutorService _scheduler;

    private final int _pcSchedulerPoolSize = 1 + Config.MAX_ONLINE_USERS / 10;

    public static PcOtherThreadPool get() {
        if (_instance == null) {
            _instance = new PcOtherThreadPool();
        }
        return _instance;
    }

    private PcOtherThreadPool() {
        // 创建一个可根据需要创建新线程的线程池，但是在以前构造的线程可用时将重用它们。
        _executor = Executors.newCachedThreadPool();

        // AI(创建一个线程池，它可安排在给定延迟后运行命令或者定期地执行。)
        _scheduler = Executors.newScheduledThreadPool(_pcSchedulerPoolSize,
                new PriorityThreadFactory("PcOth", Thread.NORM_PRIORITY));
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
     * 创建并执行在给定延迟后启用的一次性操作。
     * 
     * @param r
     *            - 要执行的任务
     * @param delay
     *            - 从现在开始延迟执行的时间
     * @return
     */
    public ScheduledFuture<?> pcSchedule(final L1PcMonitor r, final long delay) {
        try {
            if (delay <= 0) {
                // 在未来某个时间执行给定的命令。
                // 该命令可能在新的线程、已入池的线程或者正调用的线程中执行，这由 Executor 实现决定。
                _executor.execute(r);
                return null;
            }
            // 创建并执行在给定延迟后启用的一次性操作。
            return _scheduler.schedule(r, delay, TimeUnit.MILLISECONDS);

        } catch (final RejectedExecutionException e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    // ScheduledThreadPoolExecutor

    /**
     * 创建并执行一个在给定初始延迟后首次启用的定期操作，后续操作具有给定的周期； 也就是将在 initialDelay 后开始执行，然后在
     * initialDelay+period 后执行， 接着在 initialDelay + 2 * period 后执行，依此类推。
     * 如果任务的任何一个执行遇到异常，则后续执行都会被取消。
     * 
     * 否则，只能通过执行程序的取消或终止方法来终止该任务。 如果此任务的任何一个执行要花费比其周期更长的时间，则将推迟后续执行，但不会同时执行。
     * 
     * @param command
     *            - 要执行的任务
     * @param initialDelay
     *            - 首次执行的延迟时间
     * @param period
     *            - 连续执行之间的周期
     * @return
     */
    public ScheduledFuture<?> scheduleAtFixedRate(final TimerTask command,
            final long initialDelay, final long period) {
        try {
            return _scheduler.scheduleAtFixedRate(command, initialDelay,
                    period, TimeUnit.MILLISECONDS);

        } catch (final RejectedExecutionException e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    // 取消任务计时器

    /**
     * 试图取消对此任务的执行。 如果任务已完成、或已取消，或者由于某些其他原因而无法取消，则此尝试将失败。 当调用 cancel
     * 时，如果调用成功，而此任务尚未启动，则此任务将永不运行。 如果任务已经启动，则 mayInterruptIfRunning
     * 参数确定是否应该以试图停止任务的方式来中断执行此任务的线程。 此方法返回后，对 isDone() 的后续调用将始终返回 true。 如果此方法返回
     * true，则对 isCancelled() 的后续调用将始终返回 true。
     * 
     * @param future
     *            - 一个延迟的、结果可接受的操作，可将其取消。
     * @param mayInterruptIfRunning
     *            - 如果应该中断执行此任务的线程，则为 true；否则允许正在运行的任务运行完成
     */
    public void cancel(final ScheduledFuture<?> future,
            boolean mayInterruptIfRunning) {
        try {
            future.cancel(mayInterruptIfRunning);

        } catch (final RejectedExecutionException e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 取消任务计时器 取消此计时器任务。如果任务安排为一次执行且还未运行，<BR>
     * 或者尚未安排，则永远不会运行。如果任务安排为重复执行，<BR>
     * 则永远不会再运行。（如果发生此调用时任务正在运行，则任务将运行完，但永远不会再运行。） <BR>
     * <BR>
     * 注意，从重复的计时器任务的 run 方法中调用此方法绝对保证计时器任务不会再运行。 <BR>
     * <BR>
     * 此方法可以反复调用；第二次和以后的调用无效。<BR>
     * <BR>
     * 
     * @param task
     *            - 所要安排的任务。
     */
    public void cancel(final TimerTask task) {
        try {
            // 取消此计时器任务。
            task.cancel();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
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
            _prio = prio;
            _name = name;
            _group = new ThreadGroup(_name);
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
         */
        @Override
        public Thread newThread(final Runnable r) {
            final Thread t = new Thread(_group, r);
            t.setName(_name + "-" + _threadNumber.getAndIncrement());
            t.setPriority(_prio);
            return t;
        }

        @SuppressWarnings("unused")
        public ThreadGroup getGroup() {
            return _group;
        }
    }
}
