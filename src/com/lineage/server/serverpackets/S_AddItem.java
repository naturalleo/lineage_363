package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;

/**
 * 物品增加
 * 
 * @author dexc
 * 
 */
public class S_AddItem extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物品增加
     */
    public S_AddItem(final L1ItemInstance item) {
        this.writeC(S_OPCODE_ADDITEM);
        this.writeD(item.getId());
        switch (item.getItem().getItemId()) {
            case 40318:
                writeH(166);
                break;
            case 40319:
                writeH(569);
                break;
            case 40321:
                writeH(837);
                break;
            case 49158:
                writeH(3604);
                break;
            case 49157:
                writeH(3605);
                break;
            case 49156:
                writeH(3606);
                break;
            default:
                writeH(0);
                break;
        }
        int type = item.getItem().getUseType();
        if (type < 0) {
            type = 0;
        }
        if (type == 96 || type >= 98) {
            this.writeC(26);
        } else if (type == 97) {
            this.writeC(27);
        } else {
            this.writeC(type);
        }
        if (item.getChargeCount() > 0) {
            this.writeC(item.getChargeCount());// 可用次数
        } else {
            this.writeC(0x00);// 可用次数
        }

        this.writeH(item.get_gfxid());
        this.writeC(item.getBless());
        this.writeD((int) Math.min(item.getCount(), 2000000000));
        // this.writeC((item.isIdentified()) ? 0x01 : 0x00);
        int statusX = 0;
        if (item.isIdentified())
            statusX |= 1;
        if (!item.getItem().isTradable())
            statusX |= 2;
        if (item.getItem().isCantDelete())
            statusX |= 4;
        if (item.getItem().get_safeenchant() < 0
                || item.getItem().getUseType() == -3
                || item.getItem().getUseType() == -2)
            statusX |= 8;
        if (item.getBless() >= 128) {
            statusX = 32;
            if (item.isIdentified()) {
                statusX |= 1;
                statusX |= 2;
                statusX |= 4;
                statusX |= 8;
            } else {
                statusX |= 2;
                statusX |= 4;
                statusX |= 8;
            }
        }
        this.writeC(statusX);
        this.writeS(item.getViewName());
        if (!item.isIdentified()) {
            // 未鉴定 不发送详细资讯
            this.writeC(0x00);
        } else {
            final byte[] status = item.getStatusBytes(null);
            this.writeC(status.length);
            for (final byte b : status) {
                this.writeC(b);
            }
        }
        writeC(10);
        writeH(0);
        writeD(0);
        writeD(0);
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
