package com.lineage.server.command.executor;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

/**
 * 显示人物附加属性(参数:人物名称) XXX 待加入显示背包
 * 
 * @author dexc
 * 
 */
public class L1Describe implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1Describe.class);

    private L1Describe() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Describe();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            if (pc == null) {
                _log.warn("系统命令执行: " + cmdName + " " + arg + " 显示人物附加属性。");
            }

            final ArrayList<String> msg = new ArrayList<String>();

            L1PcInstance target = World.get().getPlayer(arg);

            if (pc == null) {
                if (target == null) {
                    _log.error("指令异常: 指定人物不在线上，这个命令必须输入正确人物名称才能执行。");
                    return;
                }

            } else {
                if (target == null) {
                    target = pc;
                }
            }

            msg.add("-- 显示资讯人物:[" + target.getName() + "]--");
            msg.add("等级:[" + target.getLevel() + "]    抗魔:[" + target.getMr() + "%]");
            msg.add("maxHP:[" + target.getMaxHp() + "]   maxHP:[" + target.getMaxMp()+"]");
            msg.add("防御:[" + target.getAc() + "]    魔攻:[" + target.getSp()+"]");
            msg.add("力量:[" + target.getStr() + "]   敏捷:[" + target.getDex() + "]   智力:[" + target.getInt()+"]");
            msg.add("体质:[" + target.getCon() + "]   精神:[" + target.getWis() + "]   魅力:[" + target.getCha()+"]");
            msg.add("有好度:[" + target.getKarma() + "]   正义值:[" + target.getLawful()+"]");
            msg.add("背包物品数量:[" + target.getInventory().getSize() + "]  已吃万能药:[" + target.getElixirStats()+ "]");
            final int hpr = target.getHpr()
                    + target.getInventory().hpRegenPerTick();
            final int mpr = target.getMpr()
                    + target.getInventory().mpRegenPerTick();
            msg.add("伤害附加:[+" + target.getDmgup() + "]   命中附加:[+" + target.getHitup()+"]");
            msg.add("HP额外回复:[" + hpr + "]  MP额外回复:[" + mpr+"]");
            if (pc == null) {
                String items = "";
                for (final L1ItemInstance item : target.getInventory()
                        .getItems()) {
                    items += "[" + item.getNumberedName(item.getCount(), false)
                            + "]";
                }
                msg.add(items);
            }

            for (final String info : msg) {
                if (pc == null) {
                    _log.info(info);

                } else {
                    pc.sendPackets(new S_ServerMessage(166, info));
                }
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
