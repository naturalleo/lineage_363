package com.lineage.server;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.echo.ServerExecutor;
import com.lineage.server.thread.GeneralThreadPool;

/**
 * 监听端口控制器<br>
 * 监听端口启动重置作业
 * 
 * @author dexc
 * 
 */
public class EchoServerTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(EchoServerTimer.class);

    private static EchoServerTimer _instance;

    private static final Map<Integer, ServerExecutor> _echoList = new HashMap<Integer, ServerExecutor>();

    public static EchoServerTimer get() {
        if (_instance == null) {
            _instance = new EchoServerTimer();
        }
        return _instance;
    }

    /**
     * 启动监听
     */
    public void start() {
        try {
            if (_echoList.isEmpty()) {
                startEcho();
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            if (Config.RESTART_LOGIN > 0) {
                _log.warn("监听端口重置作业启动 间隔时间:" + Config.RESTART_LOGIN + "分钟。");
                final int timeMillis = Config.RESTART_LOGIN * 60 * 1000;
                GeneralThreadPool.get().aiScheduleAtFixedRate(this, timeMillis,
                        timeMillis);
            }
        }
    }

    @Override
    public void run() {
        try {
            _log.warn("监听端口重置作业!");
            try {
                stopEcho();

                startEcho();

            } catch (final Exception e) {
                _log.error("重新启动端口作业失败!!", e);
            }

            // System.gc();

        } catch (final Exception e) {
            _log.error("监听端口重置作业失败!!", e);

        } finally {
            _log.warn("监听端口重置作业完成!!");
        }
    }

    /**
     * 重新启动
     */
    public void reStart() {
        try {
            if (Shutdown.SHUTDOWN) {
                return;
            }
            if (!_echoList.isEmpty()) {
                stopEcho();

                Thread.sleep(2000);

                startEcho();

            } else {
                _log.error("监听端口重置作业失败(目前无任何监听线程)!!");
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 启用全部监听
     */
    public void startEcho() {
        try {
            final String[] portList = Config.GAME_SERVER_PORT.split("-");
            for (String ports : portList) {
                int key = Integer.parseInt(ports);
                ServerExecutor echoServer = new ServerExecutor(key);
                if (echoServer != null) {
                    _echoList.put(new Integer(key), echoServer);
                    echoServer.stsrtEcho();
                }

                Thread.sleep(100);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 停止全部监听
     */
    public void stopEcho() {
        try {
            if (!_echoList.isEmpty()) {
                for (Integer key : _echoList.keySet()) {
                    ServerExecutor echoServer = _echoList.get(key);
                    if (echoServer != null) {
                        echoServer.stopEcho();
                    }

                    Thread.sleep(100);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 指定端口是否已在列表中
     * 
     * @param port
     * @return
     */
    public boolean isPort(final int key) {
        try {
            return _echoList.get(new Integer(key)) != null;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    /**
     * 关闭指定监听端口
     * 
     * @param port
     */
    public void stopPort(final int key) {
        try {
            final ServerExecutor echoServer = _echoList.get(new Integer(key));
            if (echoServer != null) {
                echoServer.stopEcho();
                _echoList.remove(new Integer(key));

            } else {
                _log.warn("关闭指定监听端口 作业失败:该端口未在作用中!");
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 启用指定监听端口
     * 
     * @param port
     */
    public void startPort(final int key) {
        try {
            ServerExecutor echoServer = _echoList.get(new Integer(key));
            if (echoServer == null) {
                echoServer = new ServerExecutor(key);
                _echoList.put(new Integer(key), echoServer);
                echoServer.stsrtEcho();

            } else {
                _log.warn("启用指定监听端口 作业失败:该端口已在作用中!");
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
