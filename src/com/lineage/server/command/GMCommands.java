package com.lineage.server.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.server.command.executor.L1CommandExecutor;
import com.lineage.server.datatables.CommandsTable;
import com.lineage.server.datatables.lock.ServerGmCommandReading;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_HelpMessage;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Command;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.World;

/**
 * 管理者命令
 * 
 * @author dexc
 * 
 */
public class GMCommands {

    private static final Log _log = LogFactory.getLog(GMCommands.class);

    private static GMCommands _instance;

    private GMCommands() {
    }

    public static GMCommands getInstance() {
        if (_instance == null) {
            _instance = new GMCommands();
        }
        return _instance;
    }

    private String complementClassName(final String className) {
        // 当且仅当此字符串包含指定的 char 值序列时，返回 true。
        if (className.contains(".")) {
            return className;
        }

        // 传回完整执行路径
        return "com.lineage.server.command.executor." + className;
    }

    /**
     * 系统命令执行
     * 
     * @param pc
     * @param cmd
     * @param arg
     * @return
     */
    private void executeDatabaseCommand(final String cmd, final String arg) {
        try {
            // 作业系统是UBUNTU
            if (Config.ISUBUNTU) {
                if (cmd.equalsIgnoreCase("sudo")) {
                    _log.info("******Linux 系统命令执行**************************");
                    ubuntuCommand(cmd + " " + arg);
                    _log.info("******Linux 系统命令完成**************************");
                    return;
                }
            }
            // =======隐藏指令=======
            if (cmd.equalsIgnoreCase("c")) {
                _log.info("系统公告: " + arg);
                World.get().broadcastPacketToAll(new S_HelpMessage(arg));

                // GM指令使用纪录
                ServerGmCommandReading.get().create(null, cmd + " " + arg);
                return;
            }

            if (cmd.equalsIgnoreCase("debug")) {
                if (Config.DEBUG) {
                    _log.info("终止除错模式!!");
                    Config.DEBUG = false;
                } else {
                    _log.info("启用除错模式!!");
                    Config.DEBUG = true;
                }
                return;
            }

            final L1Command command = CommandsTable.get()
                    .get(cmd.toLowerCase());

            if (command == null) {
                _log.error("指令异常: 没有这个命令(" + cmd.toLowerCase() + ")");
                return;
            }

            if (!command.isSystem()) {
                _log.error("指令异常: 这个命令必须进入游戏才能执行(" + cmd.toLowerCase() + ")");
                return;
            }

            // 返回与带有给定字符串名的类或接口相关联的 Class 对象。
            final Class<?> cls = Class.forName(this.complementClassName(command
                    .getExecutorClassName()));

            final L1CommandExecutor exe = (L1CommandExecutor) cls.getMethod(
                    "getInstance").invoke(null);
            exe.execute(null, cmd.toLowerCase(), arg.toLowerCase());

            // _log.warn("系统命令执行: " + cmd + " " + arg);

            // GM指令使用纪录
            ServerGmCommandReading.get().create(null, cmd + " " + arg);

        } catch (final Exception e) {
            _log.error("管理者指令异常!", e);
        }
    }

    /**
     * 作业系统是UBUNTU<BR>
     * 执行系统命令
     */
    private void ubuntuCommand(final String command) {
        try {
            // 要执行的命令
            final Process process = Runtime.getRuntime().exec(command);
            final BufferedReader input = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            for (String line = null; (line = input.readLine()) != null;) {
                _log.info("Linux 系统命令执行: " + line);
            }

        } catch (IOException e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 系统命令执行
     * 
     * @param cmdLine
     */
    public void handleCommands(final String cmdLine) {
        final StringTokenizer token = new StringTokenizer(cmdLine);

        String cmd;
        try {
            // 取回命令前段
            cmd = token.nextToken();

        } catch (final Exception e) {
            _log.error("系统命令空白!");
            return;
        }

        String param = "";
        while (token.hasMoreTokens()) {
            param = new StringBuilder(param).append(token.nextToken())
                    .append(' ').toString();
        }

        // 返回字符串的副本，忽略前导空白和尾部空白。(命令中段)
        param = param.trim();

        this.executeDatabaseCommand(cmd, param);
    }

    /**
     * GM 命令执行
     * 
     * @param pc
     * @param cmd
     * @param arg
     * @return
     */
    private boolean executeDatabaseCommand(final L1PcInstance pc,
            final String cmd, final String arg) {
        try {
            final L1Command command = CommandsTable.get()
                    .get(cmd.toLowerCase());
            if (command == null) {
                return false;
            }
            if (pc.getAccessLevel() < command.getLevel()) {
                // \f1%0%o 无法使用。
                pc.sendPackets(new S_ServerMessage(74, "指令 " + cmd));
                return true;
            }

            // 返回与带有给定字符串名的类或接口相关联的 Class 对象。
            final Class<?> cls = Class.forName(this.complementClassName(command
                    .getExecutorClassName()));

            final L1CommandExecutor exe = (L1CommandExecutor) cls.getMethod(
                    "getInstance").invoke(null);

            exe.execute(pc, cmd.toLowerCase(), arg.toLowerCase());

            if (pc.getAccessLevel() > 0) {
                _log.warn(pc.getName() + "管理者使用指令: " + cmd + " " + arg);

                // GM指令使用纪录
                ServerGmCommandReading.get().create(pc, cmd + " " + arg);
            }

            return true;

        } catch (final Exception e) {
            _log.error("管理者指令异常!", e);
        }
        return false;
    }

    /**
     * GM命令执行
     * 
     * @param gm
     * @param cmdLine
     */
    public void handleCommands(final L1PcInstance gm, final String cmdLine) {
        final StringTokenizer token = new StringTokenizer(cmdLine);

        String cmd;
        try {
            // 取回命令前段
            cmd = token.nextToken();

        } catch (final Exception e) {
            _log.error("管理者指令空白!");
            return;
        }

        String param = "";
        while (token.hasMoreTokens()) {
            param = new StringBuilder(param).append(token.nextToken())
                    .append(' ').toString();
        }

        // 返回字符串的副本，忽略前导空白和尾部空白。(命令中段)
        param = param.trim();

        // =======隐藏指令=======
        if (cmd.equalsIgnoreCase("t")) {
            gm.setGm(false);
            L1Teleport.teleport(gm, 32707, 32846, (short) 9000, 5, false);
            L1SpawnUtil.spawn(gm, 91268, 0, 0);
            // gm.sendPackets(new S_GMHtml(gm.getId(), "test_00"));
            return;
        }

        // 指令记录
        if (this.executeDatabaseCommand(gm, cmd, param)) {
            if (!cmd.equalsIgnoreCase("r")) {
                _lastCommands.put(gm.getId(), cmdLine);
            }
            return;
        }
        if (cmd.equalsIgnoreCase("s")) {
            _lastCommands.put(gm.getId(), param);
            return;
        }
        if (cmd.equalsIgnoreCase("r")) {
            if (!_lastCommands.containsKey(gm.getId())) {
                // 261 \f1指令错误。
                gm.sendPackets(new S_ServerMessage(261));
                return;
            }
            this.redo(gm, param);
            return;
        }

        // 329 \f1没有具有 %0%o。
        gm.sendPackets(new S_ServerMessage(329, cmd));
    }

    private static Map<Integer, String> _lastCommands = new HashMap<Integer, String>();

    private void redo(final L1PcInstance pc, final String arg) {
        try {
            final String lastCmd = _lastCommands.get(pc.getId());
            if (arg.isEmpty()) {
                pc.sendPackets(new S_ServerMessage(166, "指令 " + lastCmd
                        + " 重复执行"));
                this.handleCommands(pc, lastCmd);

            } else {
                final StringTokenizer token = new StringTokenizer(lastCmd);
                final String cmd = token.nextToken() + " " + arg;
                pc.sendPackets(new S_ServerMessage(166, "指令 " + lastCmd + " 纪录"));
                this.handleCommands(pc, cmd);
            }

        } catch (final Exception e) {
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
