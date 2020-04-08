package com.lineage.server.serverpackets;

import java.util.ArrayList;
import java.util.List;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;

/**
 * 宠物领取清单
 * 
 * @author daien
 * 
 */
public class S_PetList extends ServerBasePacket {

    private byte[] _byte = null;

    public S_PetList(final int npcObjId, final L1PcInstance pc) {
        this.buildPacket(npcObjId, pc);
    }

    private void buildPacket(final int npcObjId, final L1PcInstance pc) {
        final List<L1ItemInstance> amuletList = new ArrayList<L1ItemInstance>();
        for (final Object itemObject : pc.getInventory().getItems()) {
            final L1ItemInstance item = (L1ItemInstance) itemObject;
            switch (item.getItem().getItemId()) {
                case 40314: // 项圈
                case 40316: // 高等宠物项圈
                    if (!this.isWithdraw(pc, item)) {
                        amuletList.add(item);
                    }
                    continue;
            }
        }
        if (amuletList.size() != 0) {
            this.writeC(S_OPCODE_SELECTLIST);
            this.writeD(0x00000046); // Price
            this.writeH(amuletList.size());
            for (final L1ItemInstance item : amuletList) {
                this.writeD(item.getId());
                this.writeC((int) Math.min(item.getCount(), 2000000000));
            }
        }
    }

    private boolean isWithdraw(final L1PcInstance pc, final L1ItemInstance item) {
        final Object[] petlist = pc.getPetList().values().toArray();
        for (final Object petObject : petlist) {
            if (petObject instanceof L1PetInstance) {
                final L1PetInstance pet = (L1PetInstance) petObject;
                if (item.getId() == pet.getItemObjId()) {
                    return true;
                }
            }
        }
        return false;
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
