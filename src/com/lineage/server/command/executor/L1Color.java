package com.lineage.server.command.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ChangeName;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.thread.GeneralThreadPool;

/**
 * GM名称变色
 * 
 * @author dexc
 * 
 */
public class L1Color implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1Color.class);

    private L1Color() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Color();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            final ColorTimeController colorTime = new ColorTimeController(pc);
            GeneralThreadPool.get().execute(colorTime);

        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName());
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }

    private class ColorTimeController implements Runnable {

        private L1PcInstance _pc;

        int _mode = 0;

        public ColorTimeController(final L1PcInstance pc) {
            _pc = pc;
        }

        @Override
        public void run() {
            try {
                while (_pc.isGm()) {
                    if (_pc.getOnlineStatus() != 1) {
                        break;
                    }
                    if (_pc.getNetConnection() == null) {
                        break;
                    }
                    if (!_pc.isGm()) {
                        _pc.sendPacketsAll(new S_ChangeName(_pc.getId(), _pc
                                .getName()));
                        break;
                    }
                    _mode++;
                    if (_mode > 10) {
                        _mode = 0;
                    }

                    _pc.sendPacketsAll(new S_ChangeName(_pc.getId(), _pc
                            .getName(), _mode));

                    // 送出封包
                    _pc.sendPacketsX8(new S_SkillSound(_pc.getId(), 5288));
                    Thread.sleep(5000);
                }

            } catch (final InterruptedException e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
