package com.lineage.server.model.skill;

import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Character;
import com.lineage.server.thread.GeneralThreadPool;

/**
 * 技能效果时间轴
 * 
 * @author daien
 * 
 */
public class L1SkillTimerTimerImpl implements L1SkillTimer, Runnable {

    private static final Log _log = LogFactory
            .getLog(L1SkillTimerTimerImpl.class);

    private ScheduledFuture<?> _future = null;

    private final L1Character _cha;

    private final int _timeMillis;

    private final int _skillId;

    private int _remainingTime;

    /**
     * 技能效果时间轴
     * 
     * @param cha
     *            执行者
     * @param skillId
     *            技能编号
     * @param timeMillis
     *            技能时间(毫秒)
     */
    public L1SkillTimerTimerImpl(final L1Character cha, final int skillId,
            final int timeMillis) {
        _cha = cha;
        _skillId = skillId;
        _timeMillis = timeMillis;

        _remainingTime = _timeMillis / 1000;
    }

    @Override
    public void run() {
        _remainingTime--;
        /*
         * if (_skillId == 68||_skillId == 89) {
         * System.out.println("_remainingTime:"+_remainingTime); }
         */
        if (_remainingTime <= 0) {
            /*
             * if (_skillId == 68||_skillId == 89) {
             * System.out.println("_remainingTime:"+0); }
             */
            _cha.removeSkillEffect(_skillId);
        }
    }

    @Override
    public void begin() {
        // System.out.println("skillId:"+_skillId + " " + _remainingTime + "/" +
        // _timeMillis);
        _future = GeneralThreadPool.get().scheduleAtFixedRate(this, 1000, 1000);
    }

    @Override
    public void end() {
        kill();
        try {
            L1SkillStop.stopSkill(_cha, _skillId);

        } catch (final Throwable e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void kill() {
        try {
            if (_future != null) {
                // 试图取消对此任务的执行。
                // 如果任务已完成、或已取消，或者由于某些其他原因而无法取消，则此尝试将失败。
                // 当调用 cancel 时，如果调用成功，而此任务尚未启动，则此任务将永不运行。
                // 如果任务已经启动，则 mayInterruptIfRunning
                // 参数确定是否应该以试图停止任务的方式来中断执行此任务的线程。

                // 此方法返回后，对 isDone() 的后续调用将始终返回 true。
                // 如果此方法返回 true，则对 isCancelled() 的后续调用将始终返回 true。
                // 参数：
                // mayInterruptIfRunning - 如果应该中断执行此任务的线程，则为
                // true；否则允许正在运行的任务运行完成
                // 返回：
                // 如果无法取消任务，则返回 false，这通常是由于它已经正常完成；否则返回 true
                _future.cancel(false);
            }

        } catch (final Throwable e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public int getRemainingTime() {
        return _remainingTime;
    }
}
