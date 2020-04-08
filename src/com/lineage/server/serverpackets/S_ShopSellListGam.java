package com.lineage.server.serverpackets;

import java.util.Map;

import com.lineage.data.event.GamblingSet;
import com.lineage.data.event.gambling.Gambling;
import com.lineage.data.event.gambling.GamblingNpc;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Item;
import com.lineage.server.timecontroller.event.GamblingTime;

/**
 * 买食人妖精竞赛票
 * 
 * @author dexc
 * 
 */
public class S_ShopSellListGam extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 买食人妖精竞赛票
     * 
     * @param npc
     */
    public S_ShopSellListGam(final L1PcInstance pc, final L1NpcInstance npc) {
        this.writeC(S_OPCODE_SHOWSHOPBUYLIST);
        this.writeD(npc.getId());

        final Gambling gambling = GamblingTime.get_gambling();
        final Map<Integer, GamblingNpc> list = gambling.get_allNpc();

        if (list.size() <= 0) {
            this.writeH(0x0000);
            return;
        }

        this.writeH(list.size());

        // 食人妖精竞赛票
        final L1Item item = ItemTable.get().getTemplate(40309);

        int i = 0;
        for (final GamblingNpc gamblingNpc : list.values()) {
            i++;
            pc.get_otherList().add_gamList(gamblingNpc, i);

            this.writeD(i);
            this.writeH(item.getGfxId());
            this.writeD(GamblingSet.GAMADENA);

            final int no = GamblingTime.get_gamblingNo();
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(gamblingNpc.get_npc().getNameId());
            stringBuilder.append(" [" + no + "-"
                    + gamblingNpc.get_npc().getNpcId() + "]");

            this.writeS(stringBuilder.toString());
            this.writeC(0x00);
        }

        this.writeH(0x0007); // 0x0000:无显示 0x0001:珍珠 0x0007:金币 0x17d4:天宝
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
