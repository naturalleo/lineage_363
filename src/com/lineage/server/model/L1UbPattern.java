package com.lineage.server.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class L1UbPattern {

    private boolean _isFrozen = false;

    private Map<Integer, ArrayList<L1UbSpawn>> _groups = new HashMap<Integer, ArrayList<L1UbSpawn>>();

    public void addSpawn(final int groupNumber, final L1UbSpawn spawn) {
        if (this._isFrozen) {
            return;
        }

        ArrayList<L1UbSpawn> spawnList = this._groups.get(groupNumber);
        if (spawnList == null) {
            spawnList = new ArrayList<L1UbSpawn>();
            this._groups.put(groupNumber, spawnList);
        }

        spawnList.add(spawn);
    }

    public void freeze() {
        if (this._isFrozen) {
            return;
        }

        for (final ArrayList<L1UbSpawn> spawnList : this._groups.values()) {
            Collections.sort(spawnList);
        }

        this._isFrozen = true;
    }

    public boolean isFrozen() {
        return this._isFrozen;
    }

    public ArrayList<L1UbSpawn> getSpawnList(final int groupNumber) {
        if (!this._isFrozen) {
            return null;
        }

        return this._groups.get(groupNumber);
    }
}
