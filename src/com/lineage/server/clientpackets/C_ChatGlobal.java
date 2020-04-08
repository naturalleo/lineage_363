package com.lineage.server.clientpackets;

import static com.lineage.server.model.skill.L1SkillId.AREA_OF_SILENCE;
import static com.lineage.server.model.skill.L1SkillId.SILENCE;
import static com.lineage.server.model.skill.L1SkillId.STATUS_CHAT_PROHIBITED;
import static com.lineage.server.model.skill.L1SkillId.STATUS_POISON_SILENCE;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigOther;
import com.lineage.config.ConfigRecord;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.LogChatReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ChatGlobal;
import com.lineage.server.serverpackets.S_ChatTransaction;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.world.World;

/**
 * 要求使用广播聊天频道
 * 
 * @author daien
 * 
 */
public class C_ChatGlobal extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_ChatGlobal.class);

    /*
     * public C_ChatGlobal() { }
     * 
     * public C_ChatGlobal(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final L1PcInstance pc = client.getActiveChar();

            if (decrypt.length > 108) {
                _log.warn("人物:" + pc.getName() + "对话(广播)长度超过限制:"
                        + client.getIp().toString());
                client.set_error(client.get_error() + 1);
                return;
            }

            boolean isStop = false;// 停止输出

            boolean errMessage = false;// 异常讯息

            // 中毒状态
            if (pc.hasSkillEffect(SILENCE)) {
                if (!pc.isGm()) {
                    isStop = true;
                }
            }

            // 中毒状态
            if (pc.hasSkillEffect(AREA_OF_SILENCE)) {
                if (!pc.isGm()) {
                    isStop = true;
                }
            }

            // 中毒状态
            if (pc.hasSkillEffect(STATUS_POISON_SILENCE)) {
                if (!pc.isGm()) {
                    isStop = true;
                }
            }

            // 你从现在被禁止闲谈。
            if (pc.hasSkillEffect(STATUS_CHAT_PROHIBITED)) {
                isStop = true;
                errMessage = true;
            }

            if (isStop) {
                if (errMessage) {
                    pc.sendPackets(new S_ServerMessage(242));
                }
                return;
            }

            if (!pc.isGm()) {
                // 等级 %0 以下的角色无法使用公频或买卖频道。
                if (pc.getLevel() < ConfigAlt.GLOBAL_CHAT_LEVEL) {
                    pc.sendPackets(new S_ServerMessage(195, String
                            .valueOf(ConfigAlt.GLOBAL_CHAT_LEVEL)));
                    return;
                }

                // 管理者有非常重要的事项公告，请见谅。
                if (!World.get().isWorldChatElabled()) {
                    pc.sendPackets(new S_ServerMessage(510));
                    return;
                }
            }

            if (ConfigOther.SET_GLOBAL_TIME > 0 && !pc.isGm()) {
                final Calendar cal = Calendar.getInstance();
                final long time = cal.getTimeInMillis() / 1000;// 换算为秒
                if (time - pc.get_global_time() < ConfigOther.SET_GLOBAL_TIME) {
                    return;
                }
                pc.set_global_time(time);
            }

            // 取回对话内容
            final int chatType = this.readC();
            final String chatText = this.readS();

            switch (chatType) {
                case 0x03: // 广播频道
                    chatType_3(pc, chatText);
                    break;

                case 0x0c: // 交易频道
                    chatType_12(pc, chatText);
                    break;
            }

            if (!pc.isGm()) {
                pc.checkChatInterval();
            }

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    /**
     * 交易频道($)
     * 
     * @param pc
     * @param chatText
     */
    private void chatType_12(L1PcInstance pc, String chatText) {
        S_ChatTransaction packet = new S_ChatTransaction(pc, chatText);
        String name = pc.getName();

        for (final L1PcInstance listner : World.get().getAllPlayers()) {
            // 拒绝接收该人物讯息
            if (listner.getExcludingList().contains(name)) {
                continue;
            }
            // 拒绝接收广播频道
            if (!listner.isShowTradeChat()) {
                continue;
            }
            listner.sendPackets(packet);
        }

        if (ConfigRecord.LOGGING_CHAT_BUSINESS) {
            LogChatReading.get().noTarget(pc, chatText, 12);
        }
    }

    /**
     * 广播频道(&)
     * 
     * @param pc
     * @param chatText
     */
    private void chatType_3(L1PcInstance pc, String chatText) {
        S_ChatGlobal packet = new S_ChatGlobal(pc, chatText);
        if (pc.isGm()) {
            World.get().broadcastPacketToAll(packet);
            return;
        }
        String name = pc.getName();

        if (!pc.isGm()) {
            // 广播扣除金币或是饱食度(0:饱食度 其他:指定道具编号)
            // 广播扣除质(set_global设置0:扣除饱食度量 set_global设置其他:扣除指定道具数量)
            switch (ConfigOther.SET_GLOBAL) {
                case 0: // 饱食度
                    if (pc.get_food() >= 6) {
                        pc.set_food(pc.get_food()
                                - ConfigOther.SET_GLOBAL_COUNT);
                        pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc
                                .get_food()));

                    } else {
                        // 你太过于饥饿以致于无法谈话。
                        pc.sendPackets(new S_ServerMessage(462));
                        return;
                    }
                    break;

                default: // 指定道具
                    final L1ItemInstance item = pc.getInventory().checkItemX(
                            ConfigOther.SET_GLOBAL,
                            ConfigOther.SET_GLOBAL_COUNT);
                    if (item != null) {
                        pc.getInventory().removeItem(item,
                                ConfigOther.SET_GLOBAL_COUNT);// 删除指定道具

                    } else {
                        // 找回物品
                        final L1Item itemtmp = ItemTable.get().getTemplate(
                                ConfigOther.SET_GLOBAL);
                        pc.sendPackets(new S_ServerMessage(337, itemtmp
                                .getNameId()));
                        return;
                    }
                    break;
            }
        }

        for (final L1PcInstance listner : World.get().getAllPlayers()) {
            // 拒绝接收该人物讯息
            if (listner.getExcludingList().contains(name)) {
                continue;
            }
            // 拒绝接收广播频道
            if (!listner.isShowWorldChat()) {
                continue;
            }
            listner.sendPackets(packet);
        }

        /*
         * try { // ConfigDescs.get(2) = 服务器名称 String text = null; if
         * (pc.isGm()) { text = "[******] " + chatText; } else { text = "<" +
         * Config.SERVERNAME + ">" + "[" + pc.getName() + "] " + chatText; }
         * TServer.get().outServer(text.getBytes("utf-8"));
         * 
         * } catch (UnsupportedEncodingException e) {
         * _log.error(e.getLocalizedMessage(), e); }
         */

        if (ConfigRecord.LOGGING_CHAT_WORLD) {
            LogChatReading.get().noTarget(pc, chatText, 3);
        }
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
