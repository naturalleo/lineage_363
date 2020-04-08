package com.lineage.server.serverpackets;

import java.text.SimpleDateFormat;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 创造角色
 * 
 * @author dexc
 * 
 */
public class S_NewCharPacket extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 创造角色
     * 
     * @param pc
     */
    public S_NewCharPacket(final L1PcInstance pc) {
        this.buildPacket(pc);
    }

    private void buildPacket(final L1PcInstance pc) {
        this.writeC(S_OPCODE_NEWCHARPACK);
        this.writeS(pc.getName());
        this.writeS("");
        this.writeC(pc.getType());
        this.writeC(pc.get_sex());
        this.writeH(pc.getLawful());
        this.writeH(pc.getMaxHp());
        this.writeH(pc.getMaxMp());
        this.writeC(pc.getAc());
        this.writeC(pc.getLevel());
        this.writeC(pc.getStr());
        this.writeC(pc.getDex());
        this.writeC(pc.getCon());
        this.writeC(pc.getWis());
        this.writeC(pc.getCha());
        this.writeC(pc.getInt());

        // 大于0为GM权限
        this.writeC(0x00);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int time = Integer.parseInt(sdf.format(System.currentTimeMillis())
                .replace("-", ""));

        String times = Integer.toHexString(time);
        if (times.length() < 8) {
            times = "0" + times;
        }
        // cb a5 31 01 131a295
        this.writeC(Integer.decode("0x" + times.substring(6, 8)));
        this.writeC(Integer.decode("0x" + times.substring(4, 6)));
        this.writeC(Integer.decode("0x" + times.substring(2, 4)));
        this.writeC(Integer.decode("0x" + times.substring(0, 2)));

        // 解决发现外挂中断连线 by aplus
        int checkcode = pc.getLevel() ^ pc.getStr() ^ pc.getDex() ^ pc.getCon()
                ^ pc.getWis() ^ pc.getCha() ^ pc.getInt();
        this.writeC(checkcode & 0xFF);

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
