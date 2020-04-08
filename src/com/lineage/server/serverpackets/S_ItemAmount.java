package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;

/**
 * 更新物品使用状态(背包)-可用次数
 * 
 * @author DaiEn
 * 
 */
public class S_ItemAmount extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 更新物品使用状态(背包)-可用次数
     * 
     * @param item
     */
    public S_ItemAmount(final L1ItemInstance item, int test) {
        if (item == null) {
            return;
        }

        this.buildPacket(item);
    }

    private void buildPacket(final L1ItemInstance item) {
        this.writeC(S_OPCODE_ITEMAMOUNT);
        this.writeD(item.getId());
        this.writeS(item.getViewName());

        // 定义数量显示
        /*
         * int count = 0;
         * 
         * if (item.getItem().getMaxChargeCount() > 0) { count =
         * item.getChargeCount();
         * 
         * } else { count = (int) Math.min(item.getCount(), 2000000000); }
         */

        // 定义数量显示
        int count = (int) Math.min(item.getCount(), 2000000000);
        // 数量
        this.writeD(count);

        // 可用数量
        // this.writeD(Math.min(item.getChargeCount(), 2000000000));
        // this.writeC(0x00);
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
