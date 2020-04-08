package com.lineage.echo;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.commons.system.IpAttackCheck;
import com.lineage.commons.system.LanSecurityManager;
import com.lineage.config.Config;
import com.lineage.config.ConfigIpCheck;
import com.lineage.echo.encryptions.Encryption;
import com.lineage.list.OnlineUser;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Disconnect;
import com.lineage.server.templates.L1Account;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.StreamUtil;
import com.lineage.server.utils.SystemUtil;

/**
 * 客户资料处理
 * 
 * @author dexc
 * 
 */
public class ClientExecutor extends OpcodesClient implements Runnable {

    private static final Log _log = LogFactory.getLog(ClientExecutor.class);

    private Socket _csocket = null;

    private L1Account _account = null;// 连线帐户资料

    private L1PcInstance _activeChar = null;// 登入人物资料

    private StringBuilder _ip = null;// 连线IP资料

    private StringBuilder _mac = null;// MAC资料

    private int _kick = 0;

    private boolean _isStrat = true;

    private EncryptExecutor _encrypt;// 封包加密管理

    private DecryptExecutor _decrypt;// 封包解密管理

    private PacketHandlerExecutor _handler;// 资料处理者

    private Encryption _keys;

    private int _error = -1;// 错误次数

    private static final int M = 3; // 移动最大封包处理量

    private static final int O = 2;// 人物其他动作最大封包处理量

    private int _saveInventory = 0;

    private int _savePc = 0;

    public int _xorByte = (byte) 0xf0;

    public long _authdata;

    /**
     * 启用设置
     * 
     * @param socket
     * @throws IOException
     */
    public ClientExecutor(final Socket socket, final StringBuilder macStr) throws IOException {
        _csocket = socket;
        // TODO 伺服器捆绑
//        if (Config.LOGINS_TO_AUTOENTICATION) {
//            int randomNumber = (int) (Math.random() * 900000000) + 255;
//            _xorByte = randomNumber % 255 + 1;
//            _authdata = new BigInteger(Integer.toString(randomNumber)).modPow(
//                    new BigInteger(Config.RSA_KEY_E),
//                    new BigInteger(Config.RSA_KEY_N)).longValue();
//        }
		// 登陆器绑定
		if (Config.LOGINS_TO_AUTOENTICATION) {
			this._xorByte = (int) (Math.random() * 253 + 1);
			this._authdata = new BigInteger(Integer.toString(this._xorByte)).modPow(
					new BigInteger(Config.RSA_KEY_E), 
					new BigInteger(Config.RSA_KEY_N)).longValue();
		}

        _ip = new StringBuilder().append(socket.getInetAddress()
                .getHostAddress());
        _handler = new PacketHandler(this);
        _keys = new Encryption();
        _decrypt = new DecryptExecutor(this, socket.getInputStream());
        _encrypt = new EncryptExecutor(this, socket.getOutputStream());
		if (macStr != null) {
			this._mac = macStr;
		}
    }

    public void start() {

    }

    @Override
    public void run() {
        final PacketHc m = new PacketHc(this, M);
        GeneralThreadPool.get().schedule(m, 0);

        final PacketHc o = new PacketHc(this, O);
        GeneralThreadPool.get().schedule(o, 0);

        // 加入人物自动保存时间轴
        set_savePc(Config.AUTOSAVE_INTERVAL);
        // 加入背包物品自动保存时间轴
        set_saveInventory(Config.AUTOSAVE_INTERVAL_INVENTORY);

        try {
            _encrypt.satrt();// 开始处理封包输出
            _encrypt.outStart();// 把第一个封包送出去

            boolean isEcho = false;// 完成要求接收伺服器版本(防止恶意封包发送)
            boolean first = true;
            while (_isStrat) {
                byte[] decrypt = null;
                try {
                    decrypt = readPacket();

                } catch (final Exception e) {
                    break;
                }

                if (first)
                {
                    _csocket.setSoTimeout(180000);
                    first = false;
                }
                    
                if (decrypt.length > 1440) {
                    _log.warn("客户端送出长度异常封包:" + _ip.toString() + " 帐号:"
                            + (_account != null ? _account.get_login() : "未登入"));
                    LanSecurityManager.BANIPMAP.put(_ip.toString(), 100);
                    break;
                }
                if (_account != null) {
                    if (!OnlineUser.get().isLan(_account.get_login())) {
                        break;
                    }
                    if (!_account.is_isLoad()) {
                        break;
                    }
                }

                final int opcode = decrypt[0] & 0xFF;

                if (this._activeChar == null) {
                    if (opcode == C_OPCODE_CLIENTVERSION) {// 要求接收伺服器版本
                        if (ConfigIpCheck.IPCHECKPACK) {
                            LanSecurityManager.BANIPPACK.remove(_ip.toString());
                        }
                        isEcho = true;
                    }

                    if (isEcho) {
                        this._handler.handlePacket(decrypt);
                    }
                    continue;
                }

                if (!isEcho) {
                    continue;
                }// */

                switch (opcode) {
                    case C_OPCODE_QUITGAME:// 要求离开游戏
                    case C_OPCODE_CHANGECHAR: // 要求切换角色
                    case C_OPCODE_DROPITEM: // 要求丢弃物品(丢弃置地面)
                    case C_OPCODE_DELETEINVENTORYITEM: // 要求删除物品
                        _handler.handlePacket(decrypt);
                        break;

                    case C_OPCODE_MOVECHAR: // 人物移动封包处理
                        m.requestWork(decrypt);
                        break;

                    default: // 其他封包处理
                        o.requestWork(decrypt);
                        break;
                }
            }

            // 垃圾回收
            // finalize();

        } catch (Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

            // } catch (Throwable e) {
            // _log.error(e.getLocalizedMessage(), e);*/

        } finally {
            if (ConfigIpCheck.IPCHECK) {
                IpAttackCheck.SOCKETLIST.remove(this);
            }
            // 移出人物自动保存时间轴
            set_savePc(-1);
            // 移出背包物品自动保存时间轴
            set_saveInventory(-1);

            // 关闭IO
            close();
        }
        return;
    }

    /**
     * 关闭连线线程
     * 
     * @throws IOException
     */
    public void close() {
        try {
            String mac = null;
            if (_mac != null) {
                mac = _mac.toString();
            }

            if (_csocket == null) {
                return;
            }

            _kick = 0;

            if (_account != null) {
                OnlineUser.get().remove(_account.get_login());
            }

            if (_activeChar != null) {
                quitGame();
            }

            String ipAddr = _ip.toString();
            String account = null;

            if (_kick < 1) {
                if (_account != null) {
                    account = _account.get_login();
                }
            }

            _decrypt.stop();
            _encrypt.stop();

            StreamUtil.close(_csocket);

            if (ConfigIpCheck.ISONEIP) {
                LanSecurityManager.ONEIPMAP.remove(ipAddr);
            }

            _handler = null;
            _mac = null;// MAC资料
            _ip = null;// 连线IP资料
            _activeChar = null;// 登入人物资料
            _account = null;// 连线帐户资料

            _decrypt = null;
            _encrypt = null;
            _csocket = null;
            _keys = null;

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder
                    .append("\n--------------------------------------------------");
            stringBuilder.append("\n       客户端 离线: (");
            if (account != null) {
                stringBuilder.append(account + " ");
            }
            if (mac != null) {
                stringBuilder.append(" " + mac + " / ");
            }
            stringBuilder.append(ipAddr + ") 完成连线中断!!");
            stringBuilder
                    .append("\n--------------------------------------------------");
            _log.info(stringBuilder.toString());
            SystemUtil.printMemoryUsage(_log);

            // System.gc();

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 传回帐号暂存资料
     * 
     * @return
     */
    public L1Account getAccount() {
        return _account;
    }

    /**
     * 设置登入帐号
     * 
     * @param account
     */
    public void setAccount(final L1Account account) {
        _account = account;
    }

    /**
     * 传回登入帐号
     * 
     * @return
     */
    public String getAccountName() {
        if (_account == null) {
            return null;
        }
        return _account.get_login();
    }

    /**
     * 传回目前登入人物
     * 
     * @return
     */
    public L1PcInstance getActiveChar() {
        return _activeChar;
    }

    /**
     * 设置目前登入人物
     * 
     * @param pc
     */
    public void setActiveChar(final L1PcInstance pc) {
        _activeChar = pc;
    }

    /**
     * 传回IP位置
     * 
     * @return
     */
    public StringBuilder getIp() {
        return _ip;
    }

    /**
     * 传回MAC位置
     * 
     * @return
     */
    public StringBuilder getMac() {
        return _mac;
    }

    /**
     * 设置MAC位置
     * 
     * @param mac
     * @return true:允许登入 false:禁止登入
     */
    public boolean setMac(final StringBuilder mac) {
        _mac = mac;
        return true;
    }

    /**
     * 传回 Socket
     * 
     * @return
     */
    public Socket get_socket() {
        return _csocket;
    }

    /**
     * 中断连线
     * 
     * @return
     */
    public boolean kick() {
        try {
            _encrypt.encrypt(new S_Disconnect());
        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);
        }
        quitGame();

        _kick = 1;
        _isStrat = false;
        close();
        return true;
    }

    /**
     * 人物离开游戏的处理
     * 
     * @param pc
     */
    public void quitGame() {
        try {
            if (_activeChar == null) {
                return;
            }
            synchronized (_activeChar) {
                QuitGame.quitGame(_activeChar);
                _activeChar = null;
            }

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 封包解密
     * 
     * @return
     * @throws Exception
     */
    private byte[] readPacket() {
        try {
            byte[] data = null;
            data = _decrypt.decrypt();
            return data;

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 传回封包加密解密管理接口
     */
    public EncryptExecutor out() {
        return _encrypt;
    }

    /**
     * 加密与解密金钥
     */
    public void set_keys(Encryption keys) {
        // System.out.println("加密与解密金钥:"+keys);
        _keys = keys;
    }

    /**
     * 传回加密与解密金钥
     * 
     * @return the _keys
     */
    public Encryption get_keys() {
        return _keys;
    }

    /**
     * 传回错误次数
     * 
     * @return
     */
    public int get_error() {
        return _error;
    }

    /**
     * 设置错误次数
     * 
     * @param error
     */
    public void set_error(final int error) {
        _error = error;
        if (error >= 2) {
            kick();
        }
    }

    /**
     * 设置自动存档背包物件时间
     * 
     * @param _saveInventory
     */
    public void set_saveInventory(final int saveInventory) {
        _saveInventory = saveInventory;
    }

    /**
     * 传回自动存档背包物件时间
     * 
     * @return
     */
    public int get_saveInventory() {
        return _saveInventory;
    }

    /**
     * 设置自动存档人物资料时间
     * 
     * @param _saveInventory
     */
    public void set_savePc(final int savePc) {
        _savePc = savePc;
    }

    /**
     * 传回自动存档人物资料时间
     * 
     * @return
     */
    public int get_savePc() {
        return _savePc;
    }
}
