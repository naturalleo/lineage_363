package com.lineage.server.clientpackets;

import static com.lineage.server.model.skill.L1SkillId.STATUS_CHAT_PROHIBITED;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigRecord;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.LogChatReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ChatWhisperFrom;
import com.lineage.server.serverpackets.S_ChatWhisperTo;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

/**
 * 要求使用密语聊天频道
 * 
 * @author daien
 * 
 */
public class C_ChatWhisper extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_ChatWhisper.class);

    /*
     * public C_ChatWhisper() { }
     * 
     * public C_ChatWhisper(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            // 来源对象
            final L1PcInstance whisperFrom = client.getActiveChar();

            if (decrypt.length > 108) {
                _log.warn("人物:" + whisperFrom.getName() + "对话(密语)长度超过限制:"
                        + client.getIp().toString());
                client.set_error(client.get_error() + 1);
                return;
            }

            // 你从现在被禁止闲谈。
            if (whisperFrom.hasSkillEffect(STATUS_CHAT_PROHIBITED)) {
                whisperFrom.sendPackets(new S_ServerMessage(242));
                return;
            }

            // 等级 %0 以下无法使用密谈。
            if ((whisperFrom.getLevel() < ConfigAlt.WHISPER_CHAT_LEVEL)
                    && !whisperFrom.isGm()) {
                whisperFrom.sendPackets(new S_ServerMessage(404, String
                        .valueOf(ConfigAlt.WHISPER_CHAT_LEVEL)));
                return;
            }

            // 取回对话内容
            final String targetName = this.readS();
            final String text = this.readS();

            // 目标对象
            final L1PcInstance whisperTo = World.get().getPlayer(targetName);

            // \f1%0%d 不在线上。
            if (whisperTo == null) {
                whisperFrom.sendPackets(new S_ServerMessage(73, targetName));
                return;
            }

            // 对象是自己
            if (whisperTo.equals(whisperFrom)) {
                return;
            }

            // %0%s 断绝你的密语。
            if (whisperTo.getExcludingList().contains(whisperFrom.getName())) {
                whisperFrom.sendPackets(new S_ServerMessage(117, whisperTo
                        .getName()));
                return;
            }

            // \f1%0%d 目前关闭悄悄话。
            if (!whisperTo.isCanWhisper()) {
                whisperFrom.sendPackets(new S_ServerMessage(205, whisperTo
                        .getName()));
                return;
            }

            whisperFrom.sendPackets(new S_ChatWhisperTo(whisperTo, text));
            whisperTo.sendPackets(new S_ChatWhisperFrom(whisperFrom, text));

            if (ConfigRecord.LOGGING_CHAT_WHISPER) {
                LogChatReading.get().isTarget(whisperFrom, whisperTo, text, 9);
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
