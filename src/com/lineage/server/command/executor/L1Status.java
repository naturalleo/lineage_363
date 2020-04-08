package com.lineage.server.command.executor;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Karma;
import com.lineage.server.serverpackets.S_Lawful;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;

/**
 * 重置指定人物属性(参数:对象/属性(参考说明)/变更质)
 * 
 * @author dexc
 * 
 */
public class L1Status implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1Status.class);

    private static final int _max_int = 20000;

    private L1Status() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Status();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            if (pc == null) {
                _log.warn("系统命令执行: " + cmdName + " " + arg + " 重置指定人物属性。");
            }
            final StringTokenizer st = new StringTokenizer(arg);
            final String char_name = st.nextToken();
            final String param = st.nextToken();
            int value = Integer.parseInt(st.nextToken());

            final String e1 = "指令异常: 指定人物不在线上，这个命令必须输入正确人物名称才能执行。";
            L1PcInstance target = null;

            if (char_name.equalsIgnoreCase("me")) {
                if (pc == null) {
                    _log.error(e1);
                    return;
                }
                target = pc;

            } else {
                target = World.get().getPlayer(char_name);
            }

            if (target == null) {
                if (pc == null) {
                    _log.error(e1);
                    return;
                }
                // 73:\f1%0%d 不在线上。
                pc.sendPackets(new S_ServerMessage(73, char_name));
                return;
            }

            // -- not use DB --
            if (param.equalsIgnoreCase("AC")) {
                target.addAc((byte) (value - target.getAc()));

            } else if (param.equalsIgnoreCase("MR")) {
                target.addMr((short) (value - target.getMr()));

            } else if (param.equalsIgnoreCase("HIT")) {
                target.addHitup((short) (value - target.getHitup()));

            } else if (param.equalsIgnoreCase("DMG")) {
                target.addDmgup((short) (value - target.getDmgup()));
                // -- use DB --

            } else {
                if (param.equalsIgnoreCase("HP")) {
                    if (value > _max_int) {
                        value = _max_int;
                    }
                    int maxHP = value - target.getBaseMaxHp();
                    if (target.getBaseMaxHp() + maxHP > _max_int) {
                        maxHP = _max_int - target.getBaseMaxHp();
                    }
                    target.addBaseMaxHp((short) maxHP);
                    target.setCurrentHpDirect(target.getMaxHp());

                } else if (param.equalsIgnoreCase("MP")) {
                    if (value > _max_int) {
                        value = _max_int;
                    }
                    int maxMP = value - target.getBaseMaxMp();
                    if (target.getBaseMaxMp() + maxMP > _max_int) {
                        maxMP = _max_int - target.getBaseMaxMp();
                    }
                    // int maxMP = RangeInt.ensure((short) (value -
                    // target.getBaseMaxMp()), 1, 30000);
                    target.addBaseMaxMp((short) maxMP);
                    target.setCurrentMpDirect(target.getMaxMp());

                } else if (param.equalsIgnoreCase("LAWFUL")
                        || param.equalsIgnoreCase("L")) {
                    target.setLawful(value);
                    target.sendPacketsAll(new S_Lawful(target));

                } else if (param.equalsIgnoreCase("KARMA")
                        || param.equalsIgnoreCase("K")) {
                    target.setKarma(value);
                    target.sendPacketsAll(new S_Karma(target));

                } else if (param.equalsIgnoreCase("GM")) {
                    if (value > 200) {
                        value = 200;
                    }
                    target.setAccessLevel((short) value);
                    target.sendPackets(new S_SystemMessage("取得GM权限"));// 4:取得GM权限

                } else if (param.equalsIgnoreCase("STR")) {
                    target.addBaseStr((byte) (value - target.getBaseStr()));

                } else if (param.equalsIgnoreCase("CON")) {
                    target.addBaseCon((byte) (value - target.getBaseCon()));

                } else if (param.equalsIgnoreCase("DEX")) {
                    target.addBaseDex((byte) (value - target.getBaseDex()));

                } else if (param.equalsIgnoreCase("INT")) {
                    target.addBaseInt((byte) (value - target.getBaseInt()));

                } else if (param.equalsIgnoreCase("WIS")) {
                    target.addBaseWis((byte) (value - target.getBaseWis()));

                } else if (param.equalsIgnoreCase("CHA")) {
                    target.addBaseCha((byte) (value - target.getBaseCha()));

                } else {
                    // final String e2 = "指令异常: 指令 " + param + " 不明确";
                    // 5:指令异常: 指令 %s 不明确
                    final String e2 = "指令异常: 指令 " + param + " 不明确";
                    if (pc == null) {
                        _log.error(e2);
                        return;
                    }
                    pc.sendPackets(new S_SystemMessage(e2));
                    return;
                }
                target.save(); // 资料存档
            }
            target.sendPackets(new S_OwnCharStatus(target));

            // final String ok = target.getName() + " 的" + param + " 属性 " +
            // value + " 变更完成。";
            // 6:%s 的 %s 属性%i 变更完成。
            final String ok = target.getName() + "的" + param + "属性" + value
                    + "变更完成";
            if (pc == null) {
                _log.info(ok);
                return;
            }
            pc.sendPackets(new S_SystemMessage(ok));

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
