package com.lineage.server.command.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.NpcSpawnTable;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;

/**
 * 设置定点NPC(参数:NPC编号)
 * 
 * @author dexc
 * 
 */
public class L1NpcSet implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1NpcSet.class);

    private L1NpcSet() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1NpcSet();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            int npcid = Integer.parseInt(arg);

            // 取回NPC资料
            final L1NpcInstance npc = NpcTable.get().newNpcInstance(npcid);
            if (npc == null) {
                pc.sendPackets(new S_SystemMessage("找不到该npc。"));
                return;
            }

            npc.setId(IdFactoryNpc.get().nextId());
            npc.setMap(pc.getMap());

            npc.setX(pc.getX());
            npc.setY(pc.getY());

            npc.setHomeX(npc.getX());
            npc.setHomeY(npc.getY());

            npc.setHeading(pc.getHeading());

            // 设置副本编号 TODO
            npc.set_showId(pc.get_showId());

            World.get().storeObject(npc);
            World.get().addVisibleObject(npc);

            npc.turnOnOffLight();

            NpcSpawnTable.get().storeSpawn(pc, npc.getNpcTemplate());

        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName());
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
