package com.lineage.server.clientpackets;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigOther;
import com.lineage.config.ConfigRate;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_WhoAmount;
import com.lineage.server.serverpackets.S_WhoCharinfo;
import com.lineage.server.serverpackets.S_WhoStationery;
import com.lineage.server.timecontroller.server.ServerRestartTimer;
import com.lineage.server.world.World;

/**
 * 要求查询玩家
 * 
 * @author daien
 * 
 */
public class C_Who extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_Who.class);

    /*
     * public C_Who() { }
     * 
     * public C_Who(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final String s = this.readS();
            final L1PcInstance pc = client.getActiveChar();
            //自动挂机启动 hjx1000
//            if (s.equalsIgnoreCase("挂机")) {
//            	if (!pc.isActived()) {
//            		pc.startAI();
//            		pc.sendPackets(new S_ServerMessage("\\aD挂机已经启动，若想结束请输入 /who off"));
//            	}
//            	return;
//            }
//            if (s.equalsIgnoreCase("off")) {
//            	if (pc.isActived()) {
//            		pc.setActived(false);
//            		pc.sendPackets(new S_ServerMessage("\\aD自动挂机已经结束。"));
//            	}
//            	return;
//            }
          //自动挂机启动 hjx1000
            //开关显示伤害 hjx1000
//            if (s.equalsIgnoreCase("显示伤害")) {
//            	if (!pc.showDmg()) {
//            		pc.setshowDmg(true);
//            		pc.sendPackets(new S_ServerMessage("\\aD攻击显示伤害功能已打开"));
//            		pc.sendPackets(new S_ServerMessage("\\aD若想关闭请再次输入：  /who 显示伤害 "));
//            	} else {
//            		pc.setshowDmg(false);
//            		pc.sendPackets(new S_ServerMessage("\\aD攻击显示伤害功能已关闭。"));
//            		pc.sendPackets(new S_ServerMessage("\\aD若想开启请再次输入：  /who 显示伤害 "));
//            	}
//            	return;
//            }
            L1PcInstance find = World.get().getPlayer(s);


            if (find != null) { //显示对方装备开启若想开启删掉下面的注释 hjx1000
            	final List<L1ItemInstance> itemsx = new CopyOnWriteArrayList<L1ItemInstance>();
            	final List<L1ItemInstance> items = find.getInventory().getItems();
            	for (L1ItemInstance item : items) {
                    if (!item.isEquipped()) {// 使用中物件
                        continue;
                    }
                    itemsx.add(item);
            	}
                final S_WhoCharinfo whoChar = new S_WhoCharinfo(itemsx);
                pc.sendPackets(whoChar);
                //itemsx.clear();

            } else {
                final int count = World.get().getAllPlayers().size();
                final String amount = String
                        .valueOf((int) (count * ConfigAlt.ALT_WHO_COUNT));

                // \f1【目前线上有: %0 人 】
                pc.sendPackets(new S_ServerMessage("\\fV目前线上有: " + amount));// hjx1000
//                pc.sendPackets(new S_ServerMessage("\\aD目前线上有: " + "很多互撸娃"));

                if (ConfigAlt.ALT_WHO_COMMANDX) {
                    final String nowDate = new SimpleDateFormat(
                            "yyyy/MM/dd HH:mm:ss").format(new Date());
                    switch (ConfigAlt.ALT_WHO_TYPE) {
                        case 0:// 对话视窗显示
                            pc.sendPackets(new S_ServerMessage("\\fV启动时间: "
                                    + String.valueOf(ServerRestartTimer
                                            .get_startTime())));
                            pc.sendPackets(new S_ServerMessage(
                                    "\\fV经验倍率: "
                                            + (ConfigRate.RATE_XP * ConfigOther.RATE_XP_WHO)));
                            pc.sendPackets(new S_ServerMessage("\\fV金钱倍率: "
                                    + ConfigRate.RATE_DROP_ADENA));
                            pc.sendPackets(new S_ServerMessage("\\fV冲武倍率: "
                                    + ConfigRate.ENCHANT_CHANCE_WEAPON));
                            pc.sendPackets(new S_ServerMessage("\\fV冲防倍率: "
                                    + ConfigRate.ENCHANT_CHANCE_ARMOR));
                            pc.sendPackets(new S_ServerMessage("\\fV现实时间: "
                                    + nowDate));
                            pc.sendPackets(new S_ServerMessage("\\fV重启时间: "
                                    + ServerRestartTimer.get_restartTime()));
                            break;

                        case 1:// 视窗显示
//                            final String[] info = new String[] {
//                                    Config.SERVERNAME,// 伺服器资讯:
//                                    String.valueOf((ConfigRate.RATE_XP * ConfigOther.RATE_XP_WHO)),// 经验
//                                    String.valueOf(ConfigRate.RATE_DROP_ITEMS),// 掉宝
//                                    String.valueOf(ConfigRate.RATE_DROP_ADENA),// 金币
//                                    String.valueOf(ConfigRate.RATE_LA),// 正义
//                                    String.valueOf(ConfigRate.RATE_WEIGHT_LIMIT),// 负重
//                                    String.valueOf(ConfigRate.ENCHANT_CHANCE_WEAPON),// 武器
//                                    String.valueOf(ConfigRate.ENCHANT_CHANCE_ARMOR),// 防具
//
//                                    String.valueOf(ConfigAlt.POWER),// 手点上限
//                                    String.valueOf(ConfigAlt.POWERMEDICINE),// 单项万能药上限
//                                    String.valueOf(ConfigAlt.MEDICINE),// 总和万能药瓶数
//                                    nowDate,// 目前时间
//                                    ServerRestartTimer.get_restartTime(), // 重启时间
//                                    String.valueOf(pc.getNetConnection().getAccount().get_card_fee())//点卡时间 hjx1000
//                            };
//                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
//                                    "y_who", info));
                            final S_WhoAmount s_whoamount = new S_WhoAmount(amount);
                        	pc.sendPackets(new S_WhoStationery(pc)); // 布告栏(讯息阅读)模式讯息
                        	pc.sendPackets(s_whoamount);
                            break;
                    }
                }
            }

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
