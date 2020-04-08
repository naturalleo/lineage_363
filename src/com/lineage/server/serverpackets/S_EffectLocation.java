package com.lineage.server.serverpackets;

import com.lineage.server.model.L1Location;
import com.lineage.server.types.Point;

/**
 * 产生动画(地点)
 * 
 * @author dexc
 * 
 */
public class S_EffectLocation extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 产生动画(地点)
     * 
     * @param pt
     *            - Point
     * @param gfxId
     *            - 动画编号
     */
    public S_EffectLocation(final Point pt, final int gfxId) {
        this(pt.getX(), pt.getY(), gfxId);
    }

    /**
     * 产生动画(地点)
     * 
     * @param loc
     *            - 座标资料
     * @param gfxId
     *            - 动画编号
     */
    public S_EffectLocation(final L1Location loc, final int gfxId) {
        this(loc.getX(), loc.getY(), gfxId);
    }

    /**
     * 产生动画(地点)
     * 
     * @param x
     *            - 座标资料X
     * @param y
     *            - 座标资料Y
     * @param gfxId
     *            - 动画编号
     */
    public S_EffectLocation(final int x, final int y, final int gfxId) {
        // 0000: 52 8a 82 2b 80 b1 18 20 R..+...
        this.writeC(S_OPCODE_EFFECTLOCATION);
        this.writeH(x);
        this.writeH(y);
        this.writeH(gfxId);
    }

    /**
     * 测试用
     * 
     * @param opid
     *            封包编号
     * @param loc
     */
    public S_EffectLocation(final int opid, final L1Location loc) {
        this.writeC(opid);
        this.writeH(loc.getX());
        this.writeH(loc.getY());
        this.writeH(4842);
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
