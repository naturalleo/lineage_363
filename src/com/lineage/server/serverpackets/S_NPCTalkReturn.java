package com.lineage.server.serverpackets;

import java.util.List;

import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.L1NpcTalkData;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.npc.L1NpcHtml;
import com.lineage.server.templates.L1Item;

/**
 * NPC对话视窗
 * 
 * @author dexc
 * 
 */
public class S_NPCTalkReturn extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 可交换物件清单
     * 
     * @param objid
     * @param htmlid
     * @param pc
     * @param list
     */
    public S_NPCTalkReturn(int objid, String htmlid, L1PcInstance pc,
            List<Integer> list) {
        this.writeC(S_OPCODE_SHOWHTML);
        this.writeD(objid);
        this.writeS(htmlid);
        this.writeH(0x01);
        this.writeH(0x0b); // 11 数量
        int t = 0;
        for (final Integer v : list) {
            t++;
            L1Item datum = ItemTable.get().getTemplate(v);
            pc.get_otherList().add_sitemList2(t, v);
            this.writeS(datum.getNameId());
        }
        if (list.size() < 11) {
            int x = 11 - list.size();
            for (int i = 0; i < x; i++) {
                this.writeS(" ");
            }
        }
    }

    /**
     * NPC对话视窗
     * 
     * @param objid
     * @param htmlid
     * @param data
     *            List
     * @param pc
     */
    public S_NPCTalkReturn(int objid, String htmlid, List<L1ItemInstance> list,
            L1PcInstance pc) {
        this.writeC(S_OPCODE_SHOWHTML);
        this.writeD(objid);
        this.writeS(htmlid);
        this.writeH(0x01);
        this.writeH(0x0b); // 数量
        int t = 0;
        for (final L1ItemInstance datum : list) {
            t++;
            pc.get_otherList().add_sitemList(t, datum);
            this.writeS(datum.getViewName());
        }
        if (list.size() < 11) {
            int x = 11 - list.size();
            for (int i = 0; i < x; i++) {
                this.writeS(" ");
            }
        }
    }

    /**
     * NPC对话视窗
     * 
     * @param npc
     * @param objid
     * @param action
     * @param data
     */
    public S_NPCTalkReturn(final L1NpcTalkData npc, final int objid,
            final int action, final String[] data) {

        String htmlid = "";

        if (action == 1) {
            htmlid = npc.getNormalAction();

        } else if (action == 2) {
            htmlid = npc.getCaoticAction();

        } else {
            throw new IllegalArgumentException();
        }

        this.buildPacket(objid, htmlid, data);
    }

    /**
     * NPC对话视窗
     * 
     * @param npc
     * @param objid
     * @param action
     */
    public S_NPCTalkReturn(final L1NpcTalkData npc, final int objid,
            final int action) {
        this(npc, objid, action, null);
    }

    /**
     * NPC对话视窗
     * 
     * @param objid
     * @param htmlid
     * @param data
     */
    public S_NPCTalkReturn(final int objid, final String htmlid,
            final String[] data) {
        this.buildPacket(objid, htmlid, data);
    }

    /**
     * NPC对话视窗
     * 
     * @param objid
     * @param htmlid
     */
    public S_NPCTalkReturn(final int objid, final String htmlid) {
        this.buildPacket(objid, htmlid, null);
    }

    /**
     * NPC对话视窗
     * 
     * @param objid
     * @param html
     */
    public S_NPCTalkReturn(final int objid, final L1NpcHtml html) {
        this.buildPacket(objid, html.getName(), html.getArgs());
    }

    private void buildPacket(final int objid, final String htmlid,
            final String[] data) {
        this.writeC(S_OPCODE_SHOWHTML);
        this.writeD(objid);
        this.writeS(htmlid);
        if ((data != null) && (1 <= data.length)) {
            this.writeH(0x01); // 不明バイト 分かる人居たら修正愿います
            this.writeH(data.length); // 数量
            for (final String datum : data) {
                this.writeS(datum);
            }

        } else {
            this.writeH(0x00);
            this.writeH(0x00);
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
