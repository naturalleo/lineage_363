package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 更新物品使用状态(背包)-数量/状态
 * 
 * @author dexc
 * 
 */
public class S_ItemStatus extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 更新物品使用状态(背包)-数量/状态
     */
    public S_ItemStatus(final L1ItemInstance item, final L1PcInstance owner) {
        if (item == null) {
            return;
        }
        this.buildPacket(item, owner);
    }

    private void buildPacket(final L1ItemInstance item, final L1PcInstance owner) {
        this.writeC(S_OPCODE_ITEMAMOUNT);
        this.writeD(item.getId());
        this.writeS(item.getViewName());

        // 定义数量显示
        int count = (int) Math.min(item.getCount(), 2000000000);
        // 数量
        this.writeD(count);

        // this.writeC(0x00);
        if (!item.isIdentified()) {
            // 未鉴定 不发送详细资料
            this.writeC(0x00);

        } else {
            final byte[] status = item.getStatusBytes(owner);
            this.writeC(status.length);
            for (final byte b : status) {
                this.writeC(b);
            }
        }
    }

    /**
     * 更新物品使用状态(背包)-数量(交易专用)
     */
    public S_ItemStatus(final L1ItemInstance item, final long count) {
        this.writeC(S_OPCODE_ITEMAMOUNT);
        this.writeD(item.getId());
        this.writeS(item.getNumberedViewName(count));

        // 定义数量显示
        int out_count = (int) Math.min(count, 2000000000);
        // 数量
        this.writeD(out_count);

        if (!item.isIdentified()) {
            // 未鉴定 不发送详细资料
            this.writeC(0x00);

        } else {
            final byte[] status = item.getStatusBytes(null);
            this.writeC(status.length);
            for (final byte b : status) {
                this.writeC(b);
            }
        }
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
