package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 物件动作种类(长时间)
 * 
 * @author dexc
 * 
 */
public class S_CharVisualUpdate extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物件动作种类(长时间)
     * 
     * @param objid
     *            物件OBJID
     * @param weaponType
     *            武器型态代号(TYPE)
     */
    public S_CharVisualUpdate(final int objid, final int weaponType) {
        this.writeC(S_OPCODE_CHARVISUALUPDATE);
        this.writeD(objid);
        this.writeC(weaponType);
    }

    /**
     * 物件动作种类(长时间)
     * 
     * @param cha
     */
    public S_CharVisualUpdate(final L1PcInstance cha) {
        this.writeC(S_OPCODE_CHARVISUALUPDATE);
        this.writeD(cha.getId());
        this.writeC(cha.getCurrentWeapon());
    }

    @Override
    public byte[] getContent() {
        if (this._byte == null) {
            this._byte = this.getBytes();
        }
        return this._byte;
    }
}
