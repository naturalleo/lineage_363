package com.lineage.server.model.gametime;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import com.lineage.server.utils.RangeInt;

public class L1GameTime {
    // 2003年7月3日 12:00(UTC)が1月1日00:00
    private static final long BASE_TIME_IN_MILLIS_REAL = 1057233600000L;

    private final int _time;

    private final Calendar _calendar;

    private Calendar makeCalendar(final int time) {
        final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(0);
        cal.add(Calendar.SECOND, time);
        return cal;
    }

    private L1GameTime(final int time) {
        this._time = time;
        this._calendar = this.makeCalendar(time);
    }

    public static L1GameTime valueOf(final long timeMillis) {
        final long t1 = timeMillis - BASE_TIME_IN_MILLIS_REAL;
        if (t1 < 0) {
            throw new IllegalArgumentException();
        }
        final int t2 = (int) ((t1 * 6) / 1000L);
        final int t3 = t2 % 3; // 时间が3の倍数になるように调整
        return new L1GameTime(t2 - t3);
    }

    public static L1GameTime fromSystemCurrentTime() {
        return L1GameTime.valueOf(System.currentTimeMillis());
    }

    public static L1GameTime valueOfGameTime(final Time time) {
        final long t = time.getTime() + TimeZone.getDefault().getRawOffset();
        return new L1GameTime((int) (t / 1000L));
    }

    public Time toTime() {
        final int t = this._time % (24 * 3600); // 日付情报分を切り舍て
        return new Time(t * 1000L - TimeZone.getDefault().getRawOffset());
    }

    public int get(final int field) {
        return this._calendar.get(field);
    }

    public int getSeconds() {
        return this._time;
    }

    public Calendar getCalendar() {
        return (Calendar) this._calendar.clone();
    }

    public boolean isNight() {
        final int hour = this._calendar.get(Calendar.HOUR_OF_DAY);
        return !RangeInt.includes(hour, 6, 17); // 6:00-17:59(昼)で无ければtrue
    }

    @Override
    public String toString() {
        final SimpleDateFormat f = new SimpleDateFormat(
                "yyyy.MM.dd G 'at' HH:mm:ss z");
        f.setTimeZone(this._calendar.getTimeZone());
        return f.format(this._calendar.getTime()) + "(" + this.getSeconds()
                + ")";
    }
}
