package com.lineage.server.timecontroller.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.config.Config;
import com.lineage.server.Shutdown;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;

/**
 * 自动重启
 * 
 * @author dexc
 * 
 */
public class ServerRestartTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(ServerRestartTimer.class);

    private ScheduledFuture<?> _timer;

    private static final ArrayList<Calendar> _restartList = new ArrayList<Calendar>();

    private static Calendar _restart = null;

    private static String _string = "yyyy/MM/dd HH:mm:ss";

    private static String _startTime = null;

    private static String _restartTime = null;

    /**
     * 重新启动时间
     * 
     * @return
     */
    public static String get_restartTime() {
        return _restartTime;
    }

    /**
     * 启动时间
     * 
     * @return
     */
    public static String get_startTime() {
        return _startTime;
    }

    /**
     * 距离关机小逾10分钟
     * 
     * @return
     */
    public static boolean isRtartTime() {
        if (_restart == null) {
            return false;
        }
        return (_restart.getTimeInMillis() - System.currentTimeMillis()) <= (10 * 60 * 1000);
    }

    private static Calendar timestampToCalendar() {
        final TimeZone _tz = TimeZone.getTimeZone(Config.TIME_ZONE);
        final Calendar cal = Calendar.getInstance(_tz);

        return cal;
    }

    public void start() {
        if (Config.AUTORESTART == null) {
            return;
        }

        final Calendar cals = timestampToCalendar();

        if (_startTime == null) {
            final String nowDate = new SimpleDateFormat(_string).format(cals
                    .getTime());
            _startTime = nowDate;
        }

        if (Config.AUTORESTART != null) {
            final String HH = new SimpleDateFormat("HH").format(cals.getTime());
            int HHi = Integer.parseInt(HH);
            final String mm = new SimpleDateFormat("mm").format(cals.getTime());
            int mmi = Integer.parseInt(mm);

            for (String hm : Config.AUTORESTART) {
                String[] hm_b = hm.split(":");
                String hh_b = hm_b[0];
                String mm_b = hm_b[1];

                int newHH = Integer.parseInt(hh_b);
                int newMM = Integer.parseInt(mm_b);

                final Calendar cal = timestampToCalendar();

                int xh = -1;
                
                int addDAY = 3;// 暂定隔3天重启一次.
                
                int xhh = newHH - HHi;
                if (xhh > 0) {
                    xh = xhh;

                } else {
                    xh = (24 - HHi) + newHH;
                    addDAY --;
                }

                int xm = newMM - mmi;

                cal.add(Calendar.HOUR, xh);
                cal.add(Calendar.MINUTE, xm);
                cal.add(Calendar.DAY_OF_MONTH, addDAY);//hjx1000 暂定三天重启一次.
                _restartList.add(cal);
            }

            for (Calendar tmpCal : _restartList) {
                if (_restart == null) {
                    _restart = tmpCal;

                } else {
                    boolean re = tmpCal.before(_restart);
                    if (re) {
                        _restart = tmpCal;
                    }
                }
            }

        }

        final String restartTime = new SimpleDateFormat(_string)
                .format(_restart.getTime());
        _restartTime = restartTime;

        _log.warn("\n\r--------------------------------------------------"
                + "\n\r       开机完成时间为:" + _startTime + "\n\r       设置关机时间为:"
                + _restartTime
                + "\n\r--------------------------------------------------");

        final int timeMillis = 60 * 1000;// 1分钟
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            startCommand();

        } catch (final Exception e) {
            _log.error("自动重启时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final ServerRestartTimer restartTimer = new ServerRestartTimer();
            restartTimer.start();
        }
    }

    private void startCommand() {
        if (Config.AUTORESTART != null) {
            final Calendar cals = Calendar.getInstance();
            cals.setTimeInMillis(System.currentTimeMillis());
            if (_restart.before(cals)) {
                Shutdown.getInstance().startShutdown(null, 300, true);
            }
        }
        if ((GetNowTime.GetNowHour() == 6) && (GetNowTime.GetNowMinute() == 0)
                /*&& (GetNowTime.GetNowSecond() == 0)*/) { //hjx1000
            update();
        }
    }

    /**
     * 更新奇岩地监时间(每天早上6:00).
     */
    public static void update() {
        // System.out.println("删除地监时间");
        for (final L1PcInstance pc : World.get().getAllPlayers()) {
            pc.setRocksPrisonTime(0);
            // System.out.println("删除角色时间");
        }
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("UPDATE characters SET RocksPrisonTime=?");
            pstm.setInt(1, 0);
            pstm.execute();
        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}
