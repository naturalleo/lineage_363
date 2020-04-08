package com.lineage.server.serverpackets;

import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.gametime.L1GameTimeClock;

/**
 * 角色资讯
 * 
 * @author dexc
 * 
 */
public class S_OwnCharStatus extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 角色资讯
     * 
     * @param pc
     */
    public S_OwnCharStatus(final L1PcInstance pc) {
    	final L1ItemInstance _weapon = pc.getWeapon();
        int time = L1GameTimeClock.getInstance().currentTime().getSeconds();
        time = time - (time % 300);
        // _log.warning((new
        // StringBuilder()).append("送信时间:").append(i).toString());
        this.writeC(S_OPCODE_OWNCHARSTATUS);
        this.writeD(pc.getId());

        this.writeC(pc.getLevel());

        this.writeExp(pc.getExp());

        this.writeC(pc.getStr());
        this.writeC(pc.getInt());
        this.writeC(pc.getWis());
        this.writeC(pc.getDex());
        this.writeC(pc.getCon());
        this.writeC(pc.getCha());
        this.writeH(pc.getCurrentHp());
        this.writeH(pc.getMaxHp());
        this.writeH(pc.getCurrentMp());
        this.writeH(pc.getMaxMp());
        this.writeC(pc.getAc());
        this.writeD(time);
        this.writeC(pc.get_food());
        this.writeC(pc.getInventory().getWeight240());
        this.writeH(pc.getLawful());
        this.writeC(pc.getFire());
        this.writeC(pc.getWater());
        this.writeC(pc.getWind());
        this.writeC(pc.getEarth());
        this.writeD(pc.getKillMonstersNumber());
        if (_weapon != null) {
            pc.getInventory().updateItem(_weapon, L1PcInventory.COL_ENCHANTLVL);
        }
    }

    /**
     * 角色资讯 测试用
     * 
     * @param pc
     *            测试GM
     * @param str
     *            力量
     */
    public S_OwnCharStatus(final L1PcInstance pc, int str) {
        int time = L1GameTimeClock.getInstance().currentTime().getSeconds();
        time = time - (time % 300);

        this.writeC(S_OPCODE_OWNCHARSTATUS);
        this.writeD(pc.getId());

        this.writeC(pc.getLevel());

        this.writeExp(pc.getExp());

        this.writeC(str);
        this.writeC(pc.getInt());
        this.writeC(pc.getWis());
        this.writeC(pc.getDex());
        this.writeC(pc.getCon());
        this.writeC(pc.getCha());
        this.writeH(pc.getCurrentHp());
        this.writeH(pc.getMaxHp());
        this.writeH(pc.getCurrentMp());
        this.writeH(pc.getMaxMp());
        this.writeC(pc.getAc());
        this.writeD(time);
        this.writeC(pc.get_food());
        this.writeC(pc.getInventory().getWeight240());
        this.writeH(pc.getLawful());
        this.writeC(pc.getFire());
        this.writeC(pc.getWater());
        this.writeC(pc.getWind());
        this.writeC(pc.getEarth());
    }

    @Override
    public byte[] getContent() {
        if (this._byte == null) {
            this._byte = this.getBytes();
        }
        return this._byte;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
