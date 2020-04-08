package com.lineage.server.command.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.thread.GeneralThreadPool;

/**
 * 杀死画面中 NPC
 * 
 * @author dexc
 * 
 */
public class L1KillNpc implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1KillNpc.class);

    private L1KillNpc() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1KillNpc();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            if (pc.is_isKill()) {
                pc.sendPackets(new S_ServerMessage(166, "Kill Npc : Off"));
                pc.set_isKill(false);

            } else {
                pc.sendPackets(new S_ServerMessage(166, "Kill Npc : On"));
                pc.set_isKill(true);
                Kill kill = new Kill(pc);
                GeneralThreadPool.get().execute(kill);

            }

        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName());
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }

    private class Kill implements Runnable {

        private final L1PcInstance _pc;

        private Kill(final L1PcInstance pc) {
            _pc = pc;
        }

        @Override
        public void run() {
            try {
                while (_pc.is_isKill()) {
                    Thread.sleep(1000);
                    for (final L1Object obj : _pc.getKnownObjects()) {
                        if (obj instanceof L1MonsterInstance) {
                            L1MonsterInstance mob = (L1MonsterInstance) obj;
                            int hp = mob.getMaxHp() + 1000;
                            mob.receiveDamage(_pc, hp);
                        }
                    }
                }

            } catch (InterruptedException e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
