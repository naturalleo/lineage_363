package com.lineage.server.command.executor;

import static com.lineage.server.model.skill.L1SkillId.STATUS_CHAT_PROHIBITED;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

/**
 * 禁言(参数:人物名称 - 分钟)
 * 
 * @author dexc
 * 
 */
public class L1ChatNG implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1ChatNG.class);

    private L1ChatNG() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1ChatNG();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            final StringTokenizer st = new StringTokenizer(arg);
            final String name = st.nextToken();
            final int time = Integer.parseInt(st.nextToken());

            final L1PcInstance tg = World.get().getPlayer(name);

            if (tg != null) {
                tg.setSkillEffect(STATUS_CHAT_PROHIBITED, time * 60 * 1000);
                tg.sendPackets(new S_PacketBox(S_PacketBox.ICON_CHATBAN,
                        time * 60));
                tg.sendPackets(new S_ServerMessage(286, String.valueOf(time)));
                pc.sendPackets(new S_ServerMessage(287, name));

            } else {
                // \f1%0%d 不在线上。
                pc.sendPackets(new S_ServerMessage(73, name));
            }

        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName());
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
