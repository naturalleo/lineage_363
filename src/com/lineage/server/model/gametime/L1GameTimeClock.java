package com.lineage.server.model.gametime;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.thread.GeneralThreadPool;

/**
 * 游戏时间时计时间轴
 * 
 * @author dexc
 * 
 */
public class L1GameTimeClock {

    private static final Log _log = LogFactory.getLog(L1GameTimeClock.class);

    private static L1GameTimeClock _instance;

    private volatile L1GameTime _currentTime = L1GameTime
            .fromSystemCurrentTime();

    private L1GameTime _previousTime = null;

    private List<L1GameTimeListener> _listeners = new CopyOnWriteArrayList<L1GameTimeListener>();

    private class TimeUpdater implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    _previousTime = _currentTime;
                    _currentTime = L1GameTime.fromSystemCurrentTime();
                    notifyChanged();
                    Thread.sleep(500);
                }

            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    private boolean isFieldChanged(final int field) {
        return _previousTime.get(field) != _currentTime.get(field);
    }

    private void notifyChanged() {
        if (isFieldChanged(Calendar.MONTH)) {
            for (final L1GameTimeListener listener : _listeners) {
                listener.onMonthChanged(_currentTime);
            }
        }
        if (isFieldChanged(Calendar.DAY_OF_MONTH)) {
            for (final L1GameTimeListener listener : _listeners) {
                listener.onDayChanged(_currentTime);
            }
        }
        if (isFieldChanged(Calendar.HOUR_OF_DAY)) {
            for (final L1GameTimeListener listener : _listeners) {
                listener.onHourChanged(this._currentTime);
            }
        }
        if (isFieldChanged(Calendar.MINUTE)) {
            for (final L1GameTimeListener listener : _listeners) {
                listener.onMinuteChanged(_currentTime);
            }
        }
    }

    private L1GameTimeClock() {
        GeneralThreadPool.get().execute(new TimeUpdater());
    }

    public static void init() {
        _instance = new L1GameTimeClock();
    }

    public static L1GameTimeClock getInstance() {
        return _instance;
    }

    public L1GameTime currentTime() {
        return _currentTime;
    }

    public void addListener(final L1GameTimeListener listener) {
        _listeners.add(listener);
    }

    public void removeListener(final L1GameTimeListener listener) {
        _listeners.remove(listener);
    }
}
