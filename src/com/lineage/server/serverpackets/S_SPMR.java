package com.lineage.server.serverpackets;

import static com.lineage.server.model.skill.L1SkillId.STATUS_WISDOM_POTION;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 更新魔攻与魔防
 * 
 * @author dexc
 * 
 */
public class S_SPMR extends ServerBasePacket {

    private byte[] _byte = null;

    public S_SPMR(final L1PcInstance pc, final boolean isLogin) {
        this.writeC(S_OPCODE_SPMR);

        this.writeC(pc.getTrueSp()); // 魔攻
        this.writeC(pc.getBaseMr()); // 魔防
    }

    /**
     * 更新魔攻与魔防
     * 
     * @param pc
     */
    public S_SPMR(final L1PcInstance pc) {
        this.buildPacket(pc);
    }

    private void buildPacket(final L1PcInstance pc) {
        writeC(S_OPCODE_SPMR);

        int sp = pc.getSp() - pc.getTrueSp();
        //System.out.println("魔攻: " + pc.getSp() +"-"+ pc.getTrueSp() + "=" +
        //sp);
        // 慎重药水效果
        if (pc.hasSkillEffect(STATUS_WISDOM_POTION)) {
            sp -= 2;
        }
        
        int mr = pc.getTrueMr() - pc.getBaseMr();

        switch (pc.guardianEncounter()) {
            case 0:// 正义的守护 Lv.1
                mr += 3;
                break;

            case 1:// 正义的守护 Lv.2
                mr += 6;
                break;

            case 2:// 正义的守护 Lv.3
                mr += 9;
                break;

            case 3:// 邪恶的守护 Lv.1
                sp += 1;
                break;

            case 4:// 邪恶的守护 Lv.2
                sp += 2;
                break;

            case 5:// 邪恶的守护 Lv.3
                sp += 3;
                break;
        }

        writeC(sp); // 魔攻
        writeC(mr); // 魔防
    }

    /**
     * 更新魔攻与魔防 - 测试
     * 
     * @param pc
     */
    public S_SPMR() {
        writeC(S_OPCODE_SPMR);
        writeC(50); // 增加魔功
        writeC(100); // 增加魔防
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
