package com.lineage.server.templates;

import com.lineage.server.datatables.NpcTable;
import com.lineage.server.utils.RangeInt;

public class L1PetType {

    private final int _baseNpcId;

    private final L1Npc _baseNpcTemplate;

    private final String _name;

    private final int _itemIdForTaming;

    private final RangeInt _hpUpRange;

    private final RangeInt _mpUpRange;

    private final int _npcIdForEvolving;

    private final int _msgIds[];

    private final int _defyMsgId;

    public L1PetType(final int baseNpcId, final String name,
            final int itemIdForTaming, final RangeInt hpUpRange,
            final RangeInt mpUpRange, final int npcIdForEvolving,
            final int msgIds[], final int defyMsgId) {
        this._baseNpcId = baseNpcId;
        this._baseNpcTemplate = NpcTable.get().getTemplate(baseNpcId);
        this._name = name;
        this._itemIdForTaming = itemIdForTaming;
        this._hpUpRange = hpUpRange;
        this._mpUpRange = mpUpRange;
        this._npcIdForEvolving = npcIdForEvolving;
        this._msgIds = msgIds;
        this._defyMsgId = defyMsgId;
    }

    public int getBaseNpcId() {
        return this._baseNpcId;
    }

    public L1Npc getBaseNpcTemplate() {
        return this._baseNpcTemplate;
    }

    public String getName() {
        return this._name;
    }

    public int getItemIdForTaming() {
        return this._itemIdForTaming;
    }

    public boolean canTame() {
        return this._itemIdForTaming != 0;
    }

    public RangeInt getHpUpRange() {
        return this._hpUpRange;
    }

    public RangeInt getMpUpRange() {
        return this._mpUpRange;
    }

    /**
     * 进化后ID
     * 
     * @return
     */
    public int getNpcIdForEvolving() {
        return this._npcIdForEvolving;
    }

    /**
     * 可以进化
     * 
     * @return
     */
    public boolean canEvolve() {
        return this._npcIdForEvolving != 0;
    }

    public int getMessageId(final int num) {
        if (num == 0) {
            return 0;
        }
        return this._msgIds[num - 1];
    }

    public static int getMessageNumber(final int level) {
        if (50 <= level) {
            return 5;
        }
        if (48 <= level) {
            return 4;
        }
        if (36 <= level) {
            return 3;
        }
        if (24 <= level) {
            return 2;
        }
        if (12 <= level) {
            return 1;
        }
        return 0;
    }

    public int getDefyMessageId() {
        return this._defyMsgId;
    }

}
