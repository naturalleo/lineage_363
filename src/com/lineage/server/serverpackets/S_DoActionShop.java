package com.lineage.server.serverpackets;

import com.lineage.server.ActionCodes;

/**
 * 物件动作种类(短时间)-个人商店
 * 
 * @author dexc
 * 
 */
public class S_DoActionShop extends ServerBasePacket {

    /**
     * PC使用
     * 
     * @param object
     * @param message
     */
    public S_DoActionShop(final int object, final byte[] message) {
        this.writeC(S_OPCODE_DOACTIONGFX);
        this.writeD(object);
        this.writeC(ActionCodes.ACTION_Shop);// 动作编号
        this.writeByte(message);// 文字内容
    }

    /**
     * 虚拟人物使用
     * 
     * @param object
     * @param message
     */
    public S_DoActionShop(final int object, final String message) {
        this.writeC(S_OPCODE_DOACTIONGFX);
        this.writeD(object);
        this.writeC(ActionCodes.ACTION_Shop);// 动作编号
        this.writeS(message);// 文字内容
    }

    @Override
    public byte[] getContent() {
        return this.getBytes();
    }
}
