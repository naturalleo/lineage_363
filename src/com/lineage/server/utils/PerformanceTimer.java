package com.lineage.server.utils;

import java.util.Calendar;
import java.util.TimeZone;

import com.lineage.config.Config;

/**
 * 执行时间计算工具
 * 
 * @author dexc
 * 
 */
public class PerformanceTimer {

    private long _begin;

    public PerformanceTimer() {
        _begin = System.currentTimeMillis();
    }

    public void reset() {
        this._begin = System.currentTimeMillis();
    }

    public long get() {
        return System.currentTimeMillis() - this._begin;
    }

    /**
     * 目前时间
     * 
     * @return
     */
    public static Calendar getRealTime() {
        // final Calendar cal = Calendar.getInstance();
        final TimeZone _tz = TimeZone.getTimeZone(Config.TIME_ZONE);
        final Calendar cal = Calendar.getInstance(_tz);
        return cal;
    }
}
