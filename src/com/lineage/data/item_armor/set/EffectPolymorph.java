package com.lineage.data.item_armor.set;

import static com.lineage.server.model.skill.L1SkillId.AWAKEN_ANTHARAS;
import static com.lineage.server.model.skill.L1SkillId.AWAKEN_FAFURION;
import static com.lineage.server.model.skill.L1SkillId.AWAKEN_VALAKAS;

import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 套装效果:装备者变形
 * 
 * @author daien
 * 
 */
public class EffectPolymorph implements ArmorSetEffect {

    private int _gfxId; // 变形外观编号

    /**
     * 套装效果:装备者变形
     * 
     * @param gfxId
     *            变形外观编号
     */
    public EffectPolymorph(final int gfxId) {
        this._gfxId = gfxId;
    }

    @Override
    public void giveEffect(final L1PcInstance pc) {
        final int awakeSkillId = pc.getAwakeSkillId();
        if ((awakeSkillId == AWAKEN_ANTHARAS)
                || (awakeSkillId == AWAKEN_FAFURION)
                || (awakeSkillId == AWAKEN_VALAKAS)) {
            // 1384:目前状态中无法变身
            pc.sendPackets(new S_ServerMessage(1384));
            return;
        }
        // 6080:骑马的公主 6094:骑马的王子
        if ((this._gfxId == 6080) || (this._gfxId == 6094)) {
            if (pc.get_sex() == 0) {// 男性
                this._gfxId = 6094;

            } else {
                this._gfxId = 6080;
            }
            // 检查军马头盔是否具有可用次数
            if (!this.isRemainderOfCharge(pc)) {
                return;
            }
        }
        // 执行变身
        L1PolyMorph.doPoly(pc, this._gfxId, 0, L1PolyMorph.MORPH_BY_ITEMMAGIC);
    }

    @Override
    public void cancelEffect(final L1PcInstance pc) {
        final int awakeSkillId = pc.getAwakeSkillId();
        if ((awakeSkillId == AWAKEN_ANTHARAS)
                || (awakeSkillId == AWAKEN_FAFURION)
                || (awakeSkillId == AWAKEN_VALAKAS)) {
            // 1384:目前状态中无法变身
            pc.sendPackets(new S_ServerMessage(1384));
            return;
        }
        // 6080:骑马的公主 6094:骑马的王子
        if ((this._gfxId == 6080) || (this._gfxId == 6094)) {
            if (pc.get_sex() == 0) {// 男性
                this._gfxId = 6094;

            } else {
                this._gfxId = 6080;
            }
        }
        if (pc.getTempCharGfx() != this._gfxId) {
            return;
        }
        // 解除变身
        L1PolyMorph.undoPoly(pc);
    }

    /**
     * 检查军马头盔是否具有可用次数
     * 
     * @param pc
     * @return true:有 false:没有
     */
    private boolean isRemainderOfCharge(final L1PcInstance pc) {
        // 身上携带军马头盔
        if (pc.getInventory().checkItem(20383, 1)) {// 军马头盔
            final L1ItemInstance item = pc.getInventory().findItemId(20383);
            if (item != null) {
                if (item.getChargeCount() != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int get_mode() {
        return this._gfxId;
    }
}
