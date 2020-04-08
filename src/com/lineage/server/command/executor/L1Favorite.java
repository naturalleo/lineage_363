package com.lineage.server.command.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.command.GMCommands;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;

/**
 * 快速指令纪录(参数:set(设置)/show(目前设置))
 * 
 * @author dexc
 * 
 */
public class L1Favorite implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1Favorite.class);

    private static final Map<Integer, String> _faviCom = new HashMap<Integer, String>();

    private L1Favorite() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Favorite();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            if (!_faviCom.containsKey(pc.getId())) {
                _faviCom.put(pc.getId(), "");
            }
            String faviCom = _faviCom.get(pc.getId());
            if (arg.startsWith("set")) {
                // コマンドの登录
                final StringTokenizer st = new StringTokenizer(arg);
                st.nextToken();
                if (!st.hasMoreTokens()) {
                    pc.sendPackets(new S_SystemMessage("纪录质不能为空白"));
                    return;
                }
                final StringBuilder cmd = new StringBuilder();
                final String temp = st.nextToken(); // コマンドタイプ
                if (temp.equalsIgnoreCase(cmdName)) {
                    pc.sendPackets(new S_SystemMessage(cmdName + " 纪录质异常。"));
                    return;
                }
                cmd.append(temp + " ");
                while (st.hasMoreTokens()) {
                    cmd.append(st.nextToken() + " ");
                }
                faviCom = cmd.toString().trim();
                _faviCom.put(pc.getId(), faviCom);
                pc.sendPackets(new S_SystemMessage(faviCom + " 指令纪录完成!"));

            } else if (arg.startsWith("show")) {
                pc.sendPackets(new S_SystemMessage("目前纪录的指令: " + faviCom));

            } else if (faviCom.isEmpty()) {
                pc.sendPackets(new S_SystemMessage("目前无纪录指令!"));

            } else {
                final StringBuilder cmd = new StringBuilder();
                final StringTokenizer st = new StringTokenizer(arg);
                final StringTokenizer st2 = new StringTokenizer(faviCom);
                while (st2.hasMoreTokens()) {
                    final String temp = st2.nextToken();
                    if (temp.startsWith("%")) {
                        cmd.append(st.nextToken() + " ");
                    } else {
                        cmd.append(temp + " ");
                    }
                }
                while (st.hasMoreTokens()) {
                    cmd.append(st.nextToken() + " ");
                }
                pc.sendPackets(new S_SystemMessage(cmd + " 指令执行。"));
                GMCommands.getInstance().handleCommands(pc, cmd.toString());
            }
        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName());
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
