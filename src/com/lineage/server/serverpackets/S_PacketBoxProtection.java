package com.lineage.server.serverpackets;

/**
 * 战斗特化
 * 
 * @author LoLi
 * 
 */
public class S_PacketBoxProtection extends ServerBasePacket {

    private byte[] _byte = null;

    // 正义的守护 Lv.1 善恶值 10,000 ~ 19,999 (防御：-2 / 魔防+3)
    public static final int JUSTICE_L1 = 0;

    // 正义的守护 Lv.2 善恶值 20,000 ~ 29,999 (防御：-4 / 魔防+6)
    public static final int JUSTICE_L2 = 1;

    // 正义的守护 Lv.3 善恶值 30,000 ~ 32,767 (防御：-6 / 魔防+9)
    public static final int JUSTICE_L3 = 2;

    // 邪恶的守护 Lv.1 善恶值 -10,000 ~ -19,999 (近/远距离攻击力+1 / 魔攻+1)
    public static final int EVIL_L1 = 3;

    // 邪恶的守护 Lv.2 善恶值 -20,000 ~ -29,999 (近/远距离攻击力+3 / 魔攻+2)
    public static final int EVIL_L2 = 4;

    // 邪恶的守护 Lv.3 善恶值 -30,000 ~ -32,767 (近/远距离攻击力+5 / 魔攻+3)
    public static final int EVIL_L3 = 5;

    // 遭遇的守护 20级以下角色 被超过10级以上的玩家攻击而死亡时，不会失去经验值，也不会掉落物品
    public static final int ENCOUNTER = 6;

    /**
     * 战斗特化
     * 
     * @param model
     * @param exp
     *            0:启用 1:关闭
     */
    public S_PacketBoxProtection(int model, final int type) {
        this.writeC(S_OPCODE_PACKETBOX);
        writeC(0x72);
        writeD(model);
        writeD(type);
    }

    @Override
    public byte[] getContent() {
        if (_byte == null) {
            _byte = getBytes();
        }
        return _byte;
    }

    @Override
    public String getType() {
        return getClass().getSimpleName();
    }
}
