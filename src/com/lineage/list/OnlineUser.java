package com.lineage.list;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.AccountReading;
import com.lineage.server.templates.L1Account;

/**
 * 连线用户管理
 * 
 * @author dexc
 * 
 */
public class OnlineUser {

    private static final Log _log = LogFactory.getLog(OnlineUser.class);

    private static OnlineUser _instance;

    // Map<K,V>
    private final Map<String, ClientExecutor> _clientList;

    private Collection<String> _allValues;

    private Collection<ClientExecutor> _allClient;

    public static OnlineUser get() {
        if (_instance == null) {
            _instance = new OnlineUser();
        }

        return _instance;
    }

    private OnlineUser() {
        _clientList = new ConcurrentHashMap<String, ClientExecutor>();
    }

    /**
     * 增加连线用户资料
     * 
     * @param value
     *            帐号
     * @param client
     *            连线线程
     * @return
     */
    public boolean addClient(final L1Account value, final ClientExecutor client) {
        final String accountName = value.get_login();
        final ClientExecutor xclient = _clientList.get(accountName);
        if (xclient == null) {
            // 线程 帐户 交叉设置
            client.setAccount(value);
            value.set_isLoad(true);
            value.set_server_no(Config.SERVERNO);
            AccountReading.get().updateLan(accountName, true);

            _clientList.put(accountName, client);
            _log.info("帐号登入: " + value.get_login() + " 目前连线帐号: "
                    + _clientList.size());
            return true;

        } else {
            xclient.kick();
            client.kick();
            _log.error("连线列表中重复资料: " + value.get_login() + "\n");
            return false;
        }
    }

    /**
     * 用户连线中
     * 
     * @param accountName
     * @return
     */
    public boolean isLan(final String accountName) {
        final ClientExecutor client = _clientList.get(accountName);
        if (client != null) {
            return true;
        }
        return false;
    }

    /**
     * 移除连线用户资料
     * 
     * @param accountName
     */
    public void remove(final String accountName) {
        final ClientExecutor xclient = _clientList.get(accountName);
        if (xclient != null) {
            L1Account value = xclient.getAccount();
            value.set_isLoad(false);
            value.set_server_no(0);
            AccountReading.get().updateLan(accountName, false);

            _clientList.remove(accountName);
            // _log.info("\n       目前连线帐号: " + _clientList.size() + "\n");
            xclient.kick();// hjx1000 登出账号马上中断防止双重登陆账号
        }
    }

    /**
     * 取回连线用户 ClientThread 资料
     * 
     * @param accountName
     * @return 该帐户未连线 传回NULL
     */
    public ClientExecutor get(final String accountName) {
        final ClientExecutor client = _clientList.get(accountName);
        return client;
    }

    /**
     * 全部连线用户(Map)
     * 
     * @return
     */
    public Map<String, ClientExecutor> map() {
        return _clientList;
    }

    /**
     * 全部连线用户(Collection)
     * 
     * @return
     */
    public Collection<ClientExecutor> all() {
        try {
            final Collection<ClientExecutor> vs = _allClient;
            return (vs != null) ? vs : (_allClient = Collections
                    .unmodifiableCollection(_clientList.values()));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 传回全部连线中帐户
     * 
     * @return
     */
    public Collection<String> getObject() {
        final Collection<String> vs = _allValues;
        return (vs != null) ? vs : (_allValues = Collections
                .unmodifiableCollection(_clientList.keySet()));
    }

    /**
     * 是否已达最大连线数量
     * 
     * @return
     */
    public boolean isMax() {
        if (_clientList.size() >= Config.MAX_ONLINE_USERS) {
            return true;
        }
        return false;
    }

    /**
     * 连线数量
     * 
     * @return
     */
    public int size() {
        return _clientList.size();
    }

    /**
     * 中断全部用户
     * 
     * @return
     */
    public void kickAll() {
        for (String acc : _clientList.keySet()) {
            remove(acc);
        }
    }
}
