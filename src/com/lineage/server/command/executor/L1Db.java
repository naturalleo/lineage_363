package com.lineage.server.command.executor;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigRate;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

/**
 * 变更经验直倍率(参数:倍率)
 * 
 * @author dexc
 * 
 */
public class L1Db implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1Db.class);

    private L1Db() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Db();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            final StringTokenizer st = new StringTokenizer(arg);
            final int rateXp = Integer.parseInt(st.nextToken(), 10);

            String msgid = null;

            if (pc == null) {
                _log.warn("系统命令执行: " + cmdName + " 变更经验值倍率" + arg);
            }

            if ((int) ConfigRate.RATE_XP == rateXp) {
                if (pc == null) {
                    _log.warn("目前经验倍率已经是: " + rateXp);

                } else {
                    pc.sendPackets(new S_ServerMessage(166, "目前经验倍率已经是:"
                            + rateXp));
                }
                return;

            } else if (ConfigRate.RATE_XP < rateXp) {
                ConfigRate.RATE_XP = rateXp;
                msgid = "\\fY伺服器变更经验值为" + ConfigRate.RATE_XP + "倍，大家请把握时间升级！";

            } else if (ConfigRate.RATE_XP > rateXp) {
                ConfigRate.RATE_XP = rateXp;
                msgid = "\\fY伺服器变更经验值为" + ConfigRate.RATE_XP + "倍，祝大家游戏愉快！";
            }

            if (msgid != null) {
                World.get().broadcastPacketToAll(new S_ServerMessage(msgid));
            }

            if (pc == null) {
                _log.warn("目前经验倍率变更为: " + rateXp);
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
