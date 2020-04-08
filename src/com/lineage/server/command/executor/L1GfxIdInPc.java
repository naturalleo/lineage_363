package com.lineage.server.command.executor;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 产生动画物件(参数:动画编号)
 * 
 * @author dexc
 * 
 */
public class L1GfxIdInPc implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1GfxIdInPc.class);

    private L1GfxIdInPc() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1GfxIdInPc();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            final StringTokenizer st = new StringTokenizer(arg);
            int gfxid = 0;// 十进制

            String next = st.nextToken();
            try {
                gfxid = Integer.parseInt(next, 10);// 十进制

            } catch (final Exception e) {
                // 261 \f1指令错误。
                pc.sendPackets(new S_ServerMessage(261));
                return;
            }

            pc.sendPacketsX8(new S_SkillSound(pc.getId(), gfxid));

        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName());
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
