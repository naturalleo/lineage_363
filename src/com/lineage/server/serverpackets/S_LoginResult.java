package com.lineage.server.serverpackets;

/**
 * 登入状态
 * 
 * @author dexc
 * 
 */
public class S_LoginResult extends ServerBasePacket {

    /** 登入伺服器成功 ( 无讯息 ) */
    public static final int REASON_LOGIN_OK = 0x0000;

    /**
     * 无法登入的原因如下： 1.您的帐号密码输入错误。 2.帐号受晶片卡保护但未使用晶片卡登入。 若仍有疑问请洽客服中心02-8024-2002
     */
    public static final int REASON_ACCESS_FAILED = 0x0008;

    /**
     * 已经使用中。
     */
    public static final int REASON_ACCOUNT_IN_USE = 0x0016;

    /**
     * 
     * 您的帐号目前无法使用，请至GASH会员中心 ‘大事纪’查询原因。GASH会员中心网址： http://tw。gamania。com/
     * 
     */
    public static final int EVENT_CANT_USE = 0x001a;

    /**
     * 您无法顺利登入，可能原因如下： 使用期间结束了。请在GASH会员中心 (http://tw。gamania。com/)延长使用时间 。
     */
    public static final int EVENT_CANT_LOGIN = 0x001c;

    /**
     * 用户注册介面
     */
    public static final int EVENT_REGISTER_ACCOUNTS = 0x002f;

    /**
     * 无法顺利取得连线
     */
    public static final int EVENT_LAN_ERROR = 0x0027;

    /**
     * 您的电子邮件是无效的。请再输入电子邮件的正确的地址。
     */
    public static final int EVENT_MAIL_ERROR = 0x000b;

    /**
     * 已经有同样的帐号。请重新输入。
     */
    public static final int REASON_ACCOUNT_ALREADY_EXISTS = 0x0007;

    /**
     * 国家名称是无效的。请重新输入国家名称。
     */
    public static final int EVENT_C_ERROR = 0x0011;

    /**
     * 回到登入画面
     */
    public static final int EVENT_RE_LOGIN = 0x0004;

    /**
     * 您的天堂点数已经使用完毕，请至开卡中心 确认剩余点数并转移点数至天堂游戏中。
     */
    public static final int EVENT_CLOCK_ERROR = 0x0032;

    /**
     * 这个角色名称是不合法的。
     */
    public static final int EVENT_NAME_ERROR = 0x0034;

    /**
     * 您无法在此变更密码，请上天堂网页开卡中心变更。
     */
    public static final int EVENT_PASS_ERROR = 0x0035;

    /**
     * 您输入的密码错误。再次确认您所输入的密码是否正确，或电洽游戏橘子客服中心(02)8024-2002。
     */
    public static final int EVENT_PASS_CHECK = 0x000a;

    /**
     * 帐号不存在
     */
    public static final int EVENT_ERROR_USER = 0x009b;

    /**
     * 帐号或密码错误
     */
    public static final int EVENT_ERROR_PASS = 0x0095;

    // public static int REASON_SYSTEM_ERROR = 0x01;

    private byte[] _byte = null;

    /**
     * 登入状态
     * 
     * @param reason
     */
    public S_LoginResult(final int reason) {
        this.buildPacket(reason);
    }

    private void buildPacket(final int reason) {
        // 0000: 76 08 00 00 00 00 00 00 00 00 00 00 00 00 90 c8
        // v...............
        this.writeC(S_OPCODE_LOGINRESULT);
        this.writeH(reason);

        this.writeD(0x00000000);
        this.writeD(0x00000000);
        this.writeD(0x00000000);
    }

    @Override
    public byte[] getContent() {
        if (this._byte == null) {
            this._byte = this.getBytes();
        }
        return this._byte;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
