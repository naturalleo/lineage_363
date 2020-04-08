package com.lineage.server.command.executor;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_Weather;
import com.lineage.server.world.World;

/**
 * 游戏天气控制(参数:控制代号) 1~3 雪控制 17~19 雨控制
 * 
 * @author dexc
 * 
 */
public class L1ChangeWeather implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1ChangeWeather.class);

    private L1ChangeWeather() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1ChangeWeather();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            if (pc == null) {
                _log.warn("系统命令执行: " + cmdName + " " + arg + " 游戏天气控制。");
                _log.info("说明: 0 终止气候。");
                _log.info("说明: 1~3 雪控制。");
                _log.info("说明: 17~19 雨控制");
            }
            final StringTokenizer tok = new StringTokenizer(arg);
            final int weather = Integer.parseInt(tok.nextToken());
            World.get().setWeather(weather);
            World.get().broadcastPacketToAll(new S_Weather(weather));

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
