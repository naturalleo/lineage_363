package com.lineage.server.command.executor;

import java.util.Calendar;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.server.clientpackets.AcceleratorChecker;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 封包接收速度允许范围质设置
 * 
 * @author dexc
 * 
 */
public class L1SpeedCheck implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1SpeedCheck.class);

    private L1SpeedCheck() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1SpeedCheck();
    }

    /**
     * 目前时间
     * 
     * @return
     */
    public Calendar getRealTime() {
        final TimeZone _tz = TimeZone.getTimeZone(Config.TIME_ZONE);
        final Calendar cal = Calendar.getInstance(_tz);
        return cal;
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            final double set = Double.parseDouble(arg);
            AcceleratorChecker.CHECK_STRICTNESS = set;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
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
