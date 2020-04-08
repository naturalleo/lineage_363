package com.lineage.server.serverpackets;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 魔法购买(金币)
 * 
 * @author dexc
 * 
 */
public class S_SkillBuy extends ServerBasePacket {

    private static final Log _log = LogFactory.getLog(S_SkillBuy.class);

    private byte[] _byte = null;

    /**
     * 魔法购买(金币)
     * 
     * @param pc
     *            学习者
     * @param newSkillList
     *            学习清单
     */
    public S_SkillBuy(final L1PcInstance pc,
            final ArrayList<Integer> newSkillList) {
        try {
            if (newSkillList.size() <= 0) {
                this.writeC(S_OPCODE_SKILLBUY);
                this.writeH(0x0000);

            } else {
                this.writeC(S_OPCODE_SKILLBUY);
                this.writeD(6000);
                this.writeH(newSkillList.size());
                for (final Integer integer : newSkillList) {
                    this.writeD(integer);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
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
