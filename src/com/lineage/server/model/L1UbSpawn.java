package com.lineage.server.model;

import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.UBTable;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1TDInstance;
import com.lineage.server.serverpackets.S_NPCPack;
import com.lineage.server.world.World;

/**
 * 无线大赛
 * 
 * @author daien
 * 
 */
public class L1UbSpawn implements Comparable<L1UbSpawn> {
    private int _id;
    private int _ubId;
    private int _pattern;
    private int _group;
    private int _npcTemplateId;
    private int _amount;
    private int _spawnDelay;
    private int _sealCount;
    private String _name;

    // --------------------start getter/setter--------------------
    public int getId() {
        return this._id;
    }

    public void setId(final int id) {
        this._id = id;
    }

    public int getUbId() {
        return this._ubId;
    }

    public void setUbId(final int ubId) {
        this._ubId = ubId;
    }

    public int getPattern() {
        return this._pattern;
    }

    public void setPattern(final int pattern) {
        this._pattern = pattern;
    }

    public int getGroup() {
        return this._group;
    }

    public void setGroup(final int group) {
        this._group = group;
    }

    public int getNpcTemplateId() {
        return this._npcTemplateId;
    }

    public void setNpcTemplateId(final int npcTemplateId) {
        this._npcTemplateId = npcTemplateId;
    }

    public int getAmount() {
        return this._amount;
    }

    public void setAmount(final int amount) {
        this._amount = amount;
    }

    public int getSpawnDelay() {
        return this._spawnDelay;
    }

    public void setSpawnDelay(final int spawnDelay) {
        this._spawnDelay = spawnDelay;
    }

    public int getSealCount() {
        return this._sealCount;
    }

    public void setSealCount(final int i) {
        this._sealCount = i;
    }

    public String getName() {
        return this._name;
    }

    public void setName(final String name) {
        this._name = name;
    }

    // --------------------end getter/setter--------------------

    public void spawnOne() {
        final L1UltimateBattle ub = UBTable.getInstance().getUb(this._ubId);
        final L1Location loc = ub.getLocation().randomLocation(
                (ub.getLocX2() - ub.getLocX1()) / 2, false);
        final L1MonsterInstance mob = new L1MonsterInstance(NpcTable.get()
                .getTemplate(this.getNpcTemplateId()));

        mob.setId(IdFactoryNpc.get().nextId());
        mob.setHeading(5);
        mob.setX(loc.getX());
        mob.setHomeX(loc.getX());
        mob.setY(loc.getY());
        mob.setHomeY(loc.getY());
        mob.setMap((short) loc.getMapId());
        mob.set_storeDroped(!(3 < this.getGroup()));
        mob.setUbSealCount(this.getSealCount());
        mob.setUbId(this.getUbId());

        World.get().storeObject(mob);
        World.get().addVisibleObject(mob);

        final S_NPCPack s_npcPack = new S_NPCPack(mob);
        for (final L1PcInstance pc : World.get().getRecognizePlayer(mob)) {
            pc.addKnownObject(mob);
            mob.addKnownObject(pc);
            pc.sendPackets(s_npcPack);
        }
        // モンスターのＡＩを开始
        mob.onNpcAI();
        mob.turnOnOffLight();
        // mob.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // チャット开始
    }
    public void spawnOneTower(int where, L1NpcInstance towner) {
        final L1UltimateBattle ub = UBTable.getInstance().getUb(this._ubId);
        final L1Location loc = L1UTDSpawn.getInstance().getTower(where);
        final L1TDInstance mob = new L1TDInstance(NpcTable.get()
                .getTemplate(this.getNpcTemplateId()));

        mob.setId(IdFactoryNpc.get().nextId());
        mob.setHeading(5);
        mob.setX(loc.getX());
        mob.setHomeX(loc.getX());
        mob.setY(loc.getY());
        mob.setHomeY(loc.getY());
        mob.setMap((short) loc.getMapId());
        mob.set_storeDroped(!(3 < this.getGroup()));
        mob.setUbSealCount(this.getSealCount());
        mob.setUbId(this.getUbId());
        mob.set_ranged(2);
        mob.setHate(towner, 10);
        mob.startAI();


        World.get().storeObject(mob);
        World.get().addVisibleObject(mob);

        final S_NPCPack s_npcPack = new S_NPCPack(mob);
        for (final L1PcInstance pc : World.get().getRecognizePlayer(mob)) {
            pc.addKnownObject(mob);
            mob.addKnownObject(pc);
            pc.sendPackets(s_npcPack);
        }
        // モンスターのＡＩを开始
        mob.onNpcAI();
        mob.turnOnOffLight();
        // mob.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // チャット开始
    }

    public void spawnAll() {
        for (int i = 0; i < this.getAmount(); i++) {
            this.spawnOne();
        }
    }

    public void spawnAllTower(L1NpcInstance towner){
        for (int i = 0; i < this.getAmount(); i++) {
            this.spawnOneTower(i, towner);
        }        
    }


    @Override
    public int compareTo(final L1UbSpawn rhs) {
        // XXX - 本当はもっと严密な顺序付けがあるはずだが、必要なさそうなので后回し
        if (this.getId() < rhs.getId()) {
            return -1;
        }
        if (this.getId() > rhs.getId()) {
            return 1;
        }
        return 0;
    }

    /*
     * private static final Log _log = LogFactory.getLog(L1UbSpawn.class
     * .getName());
     */
}
