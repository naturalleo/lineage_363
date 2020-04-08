package com.lineage.server.command.executor;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigRate;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

/**
 * 变更掉宝倍率(参数:倍率)
 * 
 * @author hjx1000
 * 
 */
public class L1Dp implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1Dp.class);

    private L1Dp() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Dp();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            final StringTokenizer st = new StringTokenizer(arg);
            final int rateDp = Integer.parseInt(st.nextToken(), 10);

            String msgid = null;

            if (pc == null) {
                _log.warn("系统命令执行: " + cmdName + " 变更掉宝倍率" + arg);
            }

            if ((int) ConfigRate.RATE_DROP_ITEMS == rateDp) {
                if (pc == null) {
                    _log.warn("目前掉宝倍率已经是: " + rateDp);

                } else {
                    pc.sendPackets(new S_ServerMessage(166, "目前掉宝倍率已经是:"
                            + rateDp));
                }
                return;

            } else if (ConfigRate.RATE_DROP_ITEMS < rateDp) {
                ConfigRate.RATE_DROP_ITEMS = rateDp;
                msgid = "\\fY伺服器变更掉宝率为" + ConfigRate.RATE_DROP_ITEMS + "倍，大家请把握时间打宝！";

            } else if (ConfigRate.RATE_DROP_ITEMS > rateDp) {
                ConfigRate.RATE_DROP_ITEMS = rateDp;
                msgid = "\\fY伺服器变更掉宝率为" + ConfigRate.RATE_DROP_ITEMS + "倍，祝大家游戏愉快！";
            }

            if (msgid != null) {
                World.get().broadcastPacketToAll(new S_ServerMessage(msgid));
            }

            if (pc == null) {
                _log.warn("目前掉宝倍率变更为: " + rateDp);
            }

        } catch (final Exception e) {
            if (pc == null) {
                _log.error("错误的命令格式: " + this.getClass().getSimpleName());

            } else {
                _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                        + " 执行的GM:" + pc.getName());
                // 261 \f1指令错误。
                pc.sendPackets(new S_ServerMessage(261));
            }
        }
    }
}
