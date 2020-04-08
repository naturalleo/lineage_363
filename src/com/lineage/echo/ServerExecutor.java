package com.lineage.echo;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.commons.system.IpAttackCheck;
import com.lineage.commons.system.LanSecurityManager;
import com.lineage.config.Config;
import com.lineage.config.ConfigIpCheck;
import com.lineage.list.OnlineUser;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.StreamUtil;

/**
 * 端口监听
 * 
 * @author dexc
 * 
 */
public class ServerExecutor extends Thread {

    private static final Log _log = LogFactory.getLog(ServerExecutor.class);

    // 服务器监听套接
    private ServerSocket _server;

    private int _port = 0;

    private static final String _t1 = "\n\r--------------------------------------------------";

    private static final String _t2 = "\n\r--------------------------------------------------";

    /**
     * 端口监听
     */
    public ServerExecutor(final int port) {
        try {
            _port = port;

            if (!"*".equals(Config.GAME_SERVER_HOST_NAME)) {
                final InetAddress inetaddress = InetAddress
                        .getByName(Config.GAME_SERVER_HOST_NAME);
                _server = new ServerSocket(_port, 50, inetaddress);

            } else {
                _server = new ServerSocket(_port);
            }

        } catch (final SocketTimeoutException e) {
            _log.fatal("连线超时:(" + _port + ")", e);

        } catch (final IOException e) {
            _log.fatal("IP位置加载错误 或 端口位置已被占用:(" + _port + ")", e);

        } finally {
            _log.info("[D] " + getClass().getSimpleName() + " 开始监听服务端口:("
                    + _port + ")");
        }
    }

    /**
     * 启动监听
     * 
     * @throws IOException
     */
    public void stsrtEcho() throws IOException {
        GeneralThreadPool.get().execute(this);
    }

    @Override
    public void run() {
        try {
            while (_server != null) {
                Socket socket = null;
                try {
                    socket = _server.accept();

                    if (socket != null) {
                        // 性能偏好
                        // connectionTime - 表达短连接时间的相对重要性的 int
                        // latency - 表达低延迟的相对重要性的 int
                        // bandwidth - 表达高带宽的相对重要性的 int
                        // socket.setPerformancePreferences(1, 0, 0);
                        // socket.setSoTimeout(120000);// 用户端2分钟无反应中断

//                        final String ipaddr = socket.getInetAddress()
//                                .getHostAddress();
//                        if (ConfigIpCheck.IPCHECKPACK) {
//                            LanSecurityManager.BANIPPACK.put(ipaddr, 300);
//                        }
//                        // log4j
//                        final String info = _t1 + "\n       客户端 连线游戏伺服器 服务端口:("
//                                + _port + ")" + "\n       " + ipaddr + _t2;
//
//                        _log.info(info);
//
//                        // 计时器
//                        final ClientExecutor client = new ClientExecutor(socket);
//                        if (ConfigIpCheck.IPCHECK) {
//                            IpAttackCheck.SOCKETLIST.put(client, ipaddr);
//                        }
//                        GeneralThreadPool.get().execute(client);
						try {
							// 使用線程模式進行處理，防止1個accept產生錯誤后卡死
						    final SocketHandler sh = new SocketHandler(socket, this._port);
						    sh.startHandler();
						} catch (final Exception e) {
							// 拋出的異常不包括IO異常，給予放行
						}
                    }

                } catch (final SecurityException e) {
                    // _log.warn(e.getLocalizedMessage());
                }
            }

        } catch (IOException e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            final String lanInfo = "[D] " + getClass().getSimpleName()
                    + " 服务器核心关闭监听端口(" + _port + ")";

            _log.warn(lanInfo);
        }
    }

	/**
	 * socket處理線程<BR>
	 * socket性能偏好<BR>
	 * connectionTime - 表达短连接时间的相对重要性的 int
	 * latency - 表达低延迟的相对重要性的 int
	 * bandwidth - 表达高带宽的相对重要性的 int
	 * socket.setPerformancePreferences(1, 0, 0);
	 * socket.setSoTimeout(120000); // 用戶端2分鐘無反應中斷
	 * @author Tom
	 * 
	 * */
	private class SocketHandler implements Runnable {
		
		private Socket _socket = null;
		
		/**
		 * 初始化
		 * @param socket
		 * @param port
		 * */
		public SocketHandler(final Socket socket, final int port) {
			this._socket = socket;
		}
		
		/**
		 * 開始處理線程<BR>
		 * 運行過程中的IO錯誤會被全程捕捉<BR>
		 * 拋出線程錯誤必須調用時捕獲處理<BR>
		 * */
		public void startHandler() {
			GeneralThreadPool.get().execute(this);
		}
		
		@Override
		public void run() {
			try {
				final String ipAddr = this._socket.getInetAddress().getHostAddress(); // 獲取IP地址
				// LanSecurityManager.LOADMAP.put(ipaddr, 15); // 客戶端 連線遊戲伺服器 加入禁用位置
				String info = null; // 連線資訊初始化
				// 獲取MAC地址
				StringBuilder macStr = null;
		        
		        if (ipAddr != null && LanSecurityManager.BANIPMAP.containsKey(ipAddr)) {
					_log.warn(new StringBuilder().append("禁止登入位置(IP封锁): host=").append(ipAddr).toString());
					StreamUtil.close(this._socket);
//				} else if (macStr != null && LanSecurityManager.BANIPMAP.containsKey(macStr)) {
//					_log.warn(new StringBuilder().append("禁止登入位置(MAC封锁): mac=").append(ipAddr).toString());
//					StreamUtil.close(this._socket);	
				} else {
					_log.info("本核心由www.linsf.com哥布林天堂信息發佈網提供");
					this._socket.setSoTimeout(180000); // socket超時解除
					final ClientExecutor client = new ClientExecutor(this._socket, macStr);
					if (ConfigIpCheck.IPCHECK) {
						IpAttackCheck.SOCKETLIST.put(client, ipAddr);
					}
					GeneralThreadPool.get().execute(client);
				}
			} catch (final IOException ioe) {
				StreamUtil.close(this._socket);
			}
		}
	}
    
    /**
     * 停止监听
     */
    public void stopEcho() {
        try {
            if (_server != null) {
                StreamUtil.close(_server);
                StreamUtil.interrupt(this);
                _server = null;
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {

        }
    }
}
