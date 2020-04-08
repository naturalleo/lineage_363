package com.lineage.server.serverpackets;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.lineage.config.ConfigCharSetting;
import com.lineage.config.ConfigOther;
import com.lineage.config.ConfigRate;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.timecontroller.server.ServerRestartTimer;

/**
 * 伺服器讯息(行数/行数,附加字串)
 * 
 * @author hjx1000
 * 
 */
public class S_WhoStationery extends ServerBasePacket {
	
	private static final String _S_WhoStationery = "[S] _S_WhoStationery";

    private byte[] _byte = null;

    /**
     * 伺服器讯息(行数/行数,附加字串)
     * 
     * @param cha
     */
    /**
     * 玩家输入WHO显示信息为布告栏(讯息阅读)模式
     * 
     * @param pc
     *            查询的玩家
     */
    public S_WhoStationery(final L1PcInstance pc) {

        final String nowDate = new SimpleDateFormat(
                "yyyy/MM/dd HH:mm:ss").format(new Date());
        final double EXP = ConfigRate.RATE_XP * ConfigOther.RATE_XP_WHO;
        final double RWL = ConfigRate.RATE_WEIGHT_LIMIT;
        final double RDI = ConfigRate.RATE_DROP_ITEMS;
        final double RDA = ConfigRate.RATE_DROP_ADENA;
        final double RLA = ConfigRate.RATE_LA;
        final double RKA = ConfigRate.RATE_KARMA;
        final int RKC = pc.get_PKcount();
        //final int time = L1GameReStart.getWillRestartTime();
        final int P_HP = ConfigCharSetting.PRINCE_MAX_HP;
        final int P_MP = ConfigCharSetting.PRINCE_MAX_MP;
        final int K_HP = ConfigCharSetting.KNIGHT_MAX_HP;
        final int K_MP = ConfigCharSetting.KNIGHT_MAX_MP;
        final int E_HP = ConfigCharSetting.ELF_MAX_HP;
        final int E_MP = ConfigCharSetting.ELF_MAX_MP;
        final int W_HP = ConfigCharSetting.WIZARD_MAX_HP;
        final int W_MP = ConfigCharSetting.WIZARD_MAX_MP;
        final int D_HP = ConfigCharSetting.DARKELF_MAX_HP;
        final int D_MP = ConfigCharSetting.DARKELF_MAX_MP;
        final int R_HP = ConfigCharSetting.DRAGONKNIGHT_MAX_HP;
        final int R_MP = ConfigCharSetting.DRAGONKNIGHT_MAX_MP;
        final int I_HP = ConfigCharSetting.ILLUSIONIST_MAX_HP;
        final int I_MP = ConfigCharSetting.ILLUSIONIST_MAX_MP;

        final String S_WhoCharinfo = "经验倍率:" + EXP + " 倍\r\n" + "负重倍率:" + RWL
                + " 倍\r\n" + "掉宝倍率:" + RDI + " 倍\r\n" + "金币倍率:" + RDA
                + " 倍\r\n" + "正义倍率:" + RLA + " 倍\r\n" + "友好倍率:" + RKA
                + " 倍\r\n" + "总PK次数:" + RKC + " 次\r\n" + "重启时间:"
                + ServerRestartTimer.get_restartTime() + "\r\n" + 
//                "王族maxHP:" + P_HP + "王族maxMP:" + P_MP + "\r\n" + 
//                "骑士maxHP:" + K_HP + "骑士maxMP:" + K_MP + "\r\n" + 
//                "妖精maxHP:" + E_HP + "妖精maxMP:" + E_MP + "\r\n" + 
//                "法师maxHP:" + W_HP + "法师maxMP:" + W_MP + "\r\n" + 
//                "黑妖maxHP:" + D_HP + "黑妖maxMP:" + D_MP + "\r\n" + 
//                "龙骑maxHP:" + R_HP + "龙骑maxMP:" + R_MP + "\r\n" + 
//                "幻术maxHP:" + I_HP + "幻术maxMP:" + I_MP + "\r\n" +
                "万能药最多可使用10瓶\r\n" + "角色属性最大值包括万能药: 35" + "\r\n" +
                "点卡剩余时间:" + pc.getNetConnection().getAccount().get_card_fee()
                + "分钟" + "\r\n" + "当前时间:" + nowDate;

        // 当前的 年、月、日 (范例:12/01/10)
        final SimpleDateFormat setDateFormat = new SimpleDateFormat("yy/MM/dd");
        this.writeC(S_OPCODE_BOARDREAD);
        this.writeD(0x00);
        this.writeS("Lineage"); // 作者
        this.writeS("系统讯息"); // 标题
        this.writeS(setDateFormat.format(Calendar.getInstance().getTime())); // 讨论编号
        this.writeS(S_WhoCharinfo); // 显示查询信息
    }

    @Override
    public byte[] getContent() {
        if (_byte == null) {
        	this._byte = this._bao.toByteArray();
        }
        return _byte;
    }

    @Override
    public String getType() {
    	return _S_WhoStationery;
    }
}
