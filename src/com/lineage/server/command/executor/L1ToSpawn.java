package com.lineage.server.command.executor;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.EventSpawnTable;
import com.lineage.server.datatables.NpcSpawnTable;
import com.lineage.server.datatables.QuesttSpawnTable;
import com.lineage.server.datatables.SpawnTable;
import com.lineage.server.datatables.lock.SpawnBossReading;
import com.lineage.server.model.L1Spawn;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;

/**
 * 移动往指定召唤编号的NPC所在位置(参数:召唤表编号)
 * 
 * @author dexc
 * 
 */
public class L1ToSpawn implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1ToSpawn.class);

    private L1ToSpawn() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1ToSpawn();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {

            final StringTokenizer st = new StringTokenizer(arg);
            int id = Integer.parseInt(st.nextToken());

            // NPC召唤列表中物件
            L1Spawn spawn = NpcSpawnTable.get().getTemplate(id);

            // MOB召唤列表中物件
            if (spawn == null) {
                spawn = SpawnTable.get().getTemplate(id);
            }

            // EVENT NPC/MOB召唤列表中物件
            if (spawn == null) {
                spawn = EventSpawnTable.get().getTemplate(id);
            }

            // BOSS召唤列表中物件
            if (spawn == null) {
                spawn = SpawnBossReading.get().getTemplate(id);
            }

            // QUEST NPC/MOB召唤列表中物件
            if (spawn == null) {
                spawn = QuesttSpawnTable.get().getTemplate(id);
            }

            if (spawn != null) {
                L1Teleport.teleport(pc, spawn.getTmpLocX(), spawn.getTmpLocY(),
                        spawn.getTmpMapid(), 5, false);
                pc.sendPackets(new S_SystemMessage("移动至指定召唤编号: " + id));

            } else {
                pc.sendPackets(new S_SystemMessage("没有这个编号的召唤点: " + id));
            }

        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName(), e);
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
