package com.lineage.server.model.Instance;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.serverpackets.S_NPCPack_Eff;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.world.World;

/**
 * 对象:效果专用 控制项
 * 
 * @author dexc
 * 
 */
public class L1EffectInstance extends L1NpcInstance {
    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    private static final Log _log = LogFactory.getLog(L1EffectInstance.class);

    public static final int FW_DAMAGE_INTERVAL = 1650;// 火牢伤害间隔时间(毫秒)

    public static final int CUBE_INTERVAL = 500; // 幻术师技能间隔时间(毫秒)

    public static final int CUBE_TIME = 8000; // 幻术师技能效果时间8秒(毫秒)

    public static final int OTHER = 500; // 其它(毫秒)

    private L1EffectType _effectType;

    public L1EffectInstance(final L1Npc template) {
        super(template);

        // 取回NPCID
        final int npcId = this.getNpcTemplate().get_npcId();
        switch (npcId) {
            case 81157:// 法师技能(火牢)
                this._effectType = L1EffectType.isFirewall;
                break;

            case 80149:// 幻术师技能(立方：燃烧)
                this._effectType = L1EffectType.isCubeBurn;
                break;

            case 80150:// 幻术师技能(立方：地裂)
                this._effectType = L1EffectType.isCubeEruption;
                break;

            case 80151:// 幻术师技能(立方：冲击)
                this._effectType = L1EffectType.isCubeShock;
                break;

            case 80152:// 幻术师技能(立方：和谐)
                this._effectType = L1EffectType.isCubeHarmonize;
                break;

            default:
                this._effectType = L1EffectType.isOther;
                break;
        }
    }

    /**
     * 技能NPC类型
     * 
     * @return
     */
    public L1EffectType effectType() {
        return this._effectType;
    }

    /**
     * TODO 接触资讯
     */
    @Override
    public void onPerceive(final L1PcInstance perceivedFrom) {
        try {
            // 副本ID不相等 不相护显示
            if (perceivedFrom.get_showId() != this.get_showId()) {
                return;
            }
            perceivedFrom.addKnownObject(this);
            perceivedFrom.sendPackets(new S_NPCPack_Eff(this));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void onAction(final L1PcInstance pc) {

    }

    @Override
    public void deleteMe() {
        try {
            this._destroyed = true;
            if (this.getInventory() != null) {
                this.getInventory().clearItems();
            }
            this.allTargetClear();
            this._master = null;
            World.get().removeVisibleObject(this);
            World.get().removeObject(this);
            for (final L1PcInstance pc : World.get().getRecognizePlayer(this)) {
                pc.removeKnownObject(this);
                pc.sendPackets(new S_RemoveObject(this));
            }
            this.removeAllKnownObjects();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private int _skillId;// 引用技能编号

    /**
     * 设置引用技能编号
     * 
     * @param i
     */
    public void setSkillId(final int i) {
        this._skillId = i;
    }

    /**
     * 引用技能编号
     * 
     * @return
     */
    public int getSkillId() {
        return this._skillId;
    }

}
