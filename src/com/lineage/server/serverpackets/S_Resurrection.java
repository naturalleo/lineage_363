package com.lineage.server.serverpackets;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 物件复活
 * 
 * @author dexc
 * 
 */
public class S_Resurrection extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物件复活
     * 
     * @param target
     *            被复活的人物
     * @param use
     *            使用复活的人物
     * @param type
     */
    public S_Resurrection(final L1PcInstance target, final L1Character use,
            final int type) {
        this.writeC(S_OPCODE_RESURRECTION);
        this.writeD(target.getId());// 被复活的对象
        this.writeC(type);
        this.writeD(use.getId());// 使用复活的人物
        this.writeD(target.getClassId());
    }

    /**
     * 物件复活
     * 
     * @param target
     *            被复活的对象
     * @param use
     *            使用复活的对象
     * @param type
     */
    public S_Resurrection(final L1Character target, final L1Character use,
            final int type) {
        this.writeC(S_OPCODE_RESURRECTION);
        this.writeD(target.getId());// 被复活的对象
        this.writeC(type);
        this.writeD(use.getId());// 使用复活的人物
        this.writeD(target.getGfxId());
    }

    /**
     * 物件复活(测试封包用)
     * 
     * @param target
     * @param opid
     * @param type
     */
    public S_Resurrection(final L1PcInstance target, final int opid,
            final int type) {
        this.writeC(opid);
        this.writeD(target.getId());// 被复活的对象
        this.writeC(type);
        this.writeD(target.getId());// 使用复活的人物
        this.writeD(target.getClassId());
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
