package com.lineage.server.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCPack;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.world.World;

/**
 * 战争召唤
 * 
 * @author daien
 * 
 */
public class L1WarSpawn {

    private static final Log _log = LogFactory.getLog(L1WarSpawn.class);

    private static L1WarSpawn _instance;

    // private Constructor<?> _constructor;

    public L1WarSpawn() {
    }

    public static L1WarSpawn getInstance() {
        if (_instance == null) {
            _instance = new L1WarSpawn();
        }
        return _instance;
    }

    /**
     * 召唤主塔
     * 
     * @param castleId
     *            城堡编号
     */
    public void spawnTower(final int castleId) {
        int npcId = 81111;
        if (castleId == L1CastleLocation.ADEN_CASTLE_ID) {
            npcId = 81189;
        }
        final L1Npc l1npc = NpcTable.get().getTemplate(npcId); // 取回npc资料
        int[] loc = new int[3];
        loc = L1CastleLocation.getTowerLoc(castleId);
        SpawnWarObject(l1npc, loc[0], loc[1], (short) (loc[2]));
        if (castleId == L1CastleLocation.ADEN_CASTLE_ID) {
            spawnSubTower();
        }
    }

    /**
     * 召唤子塔
     */
    private void spawnSubTower() {
        L1Npc l1npc;
        int[] loc = new int[3];
        for (int i = 1; i <= 4; i++) {
            l1npc = NpcTable.get().getTemplate(81189 + i);
            loc = L1CastleLocation.getSubTowerLoc(i);
            SpawnWarObject(l1npc, loc[0], loc[1], (short) (loc[2]));
        }
    }

    /**
     * 召唤王冠
     * 
     * @param castleId
     *            城堡编号
     */
    public void SpawnCrown(final int castleId) {
        final L1Npc l1npc = NpcTable.get().getTemplate(81125); // 王冠
        int[] loc = new int[3];
        loc = L1CastleLocation.getTowerLoc(castleId);
        SpawnWarObject(l1npc, loc[0], loc[1], (short) (loc[2]));
    }

    /**
     * 召唤 旗
     * 
     * @param castleId
     *            城堡编号
     */
    public void SpawnFlag(final int castleId) {
        final L1Npc l1npc = NpcTable.get().getTemplate(81122); // 旗
        int[] loc = new int[5];
        loc = L1CastleLocation.getWarArea(castleId);
        int x = 0;
        int y = 0;
        final int locx1 = loc[0];
        final int locx2 = loc[1];
        final int locy1 = loc[2];
        final int locy2 = loc[3];
        final short mapid = (short) loc[4];

        for (x = locx1, y = locy1; x <= locx2; x += 8) {
            SpawnWarObject(l1npc, x, y, mapid);
        }
        for (x = locx2, y = locy1; y <= locy2; y += 8) {
            SpawnWarObject(l1npc, x, y, mapid);
        }
        for (x = locx2, y = locy2; x >= locx1; x -= 8) {
            SpawnWarObject(l1npc, x, y, mapid);
        }
        for (x = locx1, y = locy2; y >= locy1; y -= 8) {
            SpawnWarObject(l1npc, x, y, mapid);
        }

        switch (castleId) {
            case 2:// 妖魔城
                break;
            case 1:// 肯特城
            case 3:// 风木城
            case 4:// 奇岩城
            case 5:// 海音城
            case 6:// 侏儒城
            case 7:// 亚丁城
            case 8:// 狄亚得要塞
                break;
        }
    }

    /**
     * 物件召唤
     * 
     * @param l1npc
     *            NPC基础资料
     * @param locx
     *            X座标
     * @param locy
     *            Y座标
     * @param mapid
     *            地图位置
     */
    private void SpawnWarObject(final L1Npc l1npc, final int locx,
            final int locy, final short mapid) {
        try {
            if (l1npc != null) {
                final L1NpcInstance npc = NpcTable.get().newNpcInstance(l1npc);

                npc.setId(IdFactoryNpc.get().nextId());
                npc.setX(locx);
                npc.setY(locy);
                npc.setHomeX(locx);
                npc.setHomeY(locy);
                npc.setHeading(0);
                npc.setMap(mapid);
                World.get().storeObject(npc);
                World.get().addVisibleObject(npc);

                for (final L1PcInstance pc : World.get().getAllPlayers()) {
                    npc.addKnownObject(pc);
                    pc.addKnownObject(npc);
                    pc.sendPacketsAll(new S_NPCPack(npc));
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private L1NpcInstance spawnWarObject(final int npcid, final int locx,
            final int locy, final int mapid, final int h) {
        try {
            final L1Npc l1npc = NpcTable.get().getTemplate(npcid);
            if (l1npc != null) {
                final L1NpcInstance npc = NpcTable.get().newNpcInstance(l1npc);

                npc.setId(IdFactoryNpc.get().nextId());
                npc.setX(locx);
                npc.setY(locy);
                npc.setHomeX(locx);
                npc.setHomeY(locy);
                npc.setHeading(h);
                npc.setMap((short) mapid);
                World.get().storeObject(npc);
                World.get().addVisibleObject(npc);

                for (final L1PcInstance pc : World.get().getAllPlayers()) {
                    npc.addKnownObject(pc);
                    pc.addKnownObject(npc);
                    pc.sendPacketsAll(new S_NPCPack(npc));
                }
                return npc;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }
}
