package com.lineage.commons.system;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigIpCheck;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.IpReading;

/**
 * IP攻击行为检查器
 * 
 * @author loli
 */
public class IpAttackCheck {

    private static final Log _log = LogFactory.getLog(IpAttackCheck.class);

    public static final Map<ClientExecutor, String> SOCKETLIST = new ConcurrentHashMap<ClientExecutor, String>();// DaiEn
                                                                                                                 // 2012-04-25

    private static final HashMap<String, IpTemp> _ipList = new HashMap<String, IpTemp>();

    private static final HashMap<String, IpTemp> _ipCheckList = new HashMap<String, IpTemp>();

    private static IpAttackCheck _instance;

    private class IpTemp {
        long _time;
        int _count;
    }

    public static IpAttackCheck get() {
        if (_instance == null) {
            _instance = new IpAttackCheck();
        }
        return _instance;
    }

    private IpAttackCheck() {
        _ipList.clear();
        _ipCheckList.clear();
    }

    public boolean check(String key) {
        try {
            final long time = System.currentTimeMillis();
            IpTemp value = _ipList.get(key);

            if (value == null) {
                value = new IpTemp();
                value._time = time;
                value._count = 1;
                _ipList.put(key, value);

            } else {
                if (time - value._time <= ConfigIpCheck.TIMEMILLIS) {
                    value._count += 1;
                }
                value._time = time;
                if (value._count >= ConfigIpCheck.COUNT) {
                    kick(key);
                    if (ConfigIpCheck.SETDB) {
                        // 加入IP封锁
                        IpReading.get().add(key, "IP类似攻击行为");
                        return false;
                    }
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return true;
    }

    public boolean checkFirst(String key)
    {
    	final long time = System.currentTimeMillis();
        IpTemp value = _ipCheckList.get(key);

        if (value == null) {
            value = new IpTemp();
            value._time = time;
            value._count = 1;
            _ipCheckList.put(key, value);
            return true;
        } else {
            return false;
        }
    }
    public void clearFirst(String key)
    {
        _ipCheckList.remove(key);
    }

    // 中断相同IP所有连线 DaiEn 2012-04-25
    private void kick(final String key) {
        try {
            for (final ClientExecutor socket : SOCKETLIST.keySet()) {
                final String ip = SOCKETLIST.get(socket);
                if (ip != null) {
                    if (ip.equals(key)) {
                        socket.close();
                    }
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
