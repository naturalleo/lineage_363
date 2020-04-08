package com.lineage.data.item_etcitem.wand;

import static com.lineage.server.model.skill.L1SkillId.AWAKEN_ANTHARAS;
import static com.lineage.server.model.skill.L1SkillId.AWAKEN_FAFURION;
import static com.lineage.server.model.skill.L1SkillId.AWAKEN_VALAKAS;
import static com.lineage.server.model.skill.L1SkillId.SHAPE_CHANGE;

import java.util.Random;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.ActionCodes;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_ShowPolyList;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.world.World;

/**
 * 变身魔杖40008<br>
 * 变身魔杖(祝福)140008<br>
 * 黑暗安特的树皮40410<br>
 */
public class Poly_Magic_Wand extends ItemExecutor {

    /**
	 *
	 */
    private Poly_Magic_Wand() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Poly_Magic_Wand();
    }

    /**
     * 道具物件执行
     * 
     * @param data
     *            参数
     * @param pc
     *            执行者
     * @param item
     *            物件
     */
    @Override
    public void execute(final int[] data, final L1PcInstance pc,
            final L1ItemInstance item) {
        final int itemId = item.getItemId();
        final int spellsc_objid = data[0];
        if ((pc.getMapId() == 63) || (pc.getMapId() == 552)
                || (pc.getMapId() == 555) || (pc.getMapId() == 557)
                || (pc.getMapId() == 558) || (pc.getMapId() == 779)) { // 水中不可使用
            pc.sendPackets(new S_ServerMessage(563)); // \f1你无法在这个地方使用。

        } else {
            // 送出封包(动作)
            pc.sendPacketsX8(new S_DoActionGFX(pc.getId(),
                    ActionCodes.ACTION_Wand));

            final int chargeCount = item.getChargeCount();
            if (((chargeCount <= 0) && (itemId != 40410))
                    || (pc.getTempCharGfx() == 6034)
                    || (pc.getTempCharGfx() == 6035)) {
                // 没有任何事情发生
                pc.sendPackets(new S_ServerMessage(79));
                return;
            }

            final L1Object target = World.get().findObject(spellsc_objid);
            if (target != null) {
                final L1Character cha = (L1Character) target;
                this.polyAction(pc, cha);
                // 解除魔法技能绝对屏障
                L1BuffUtil.cancelAbsoluteBarrier(pc);

                if ((itemId == 40008) || (itemId == 140008)) {
                    item.setChargeCount(item.getChargeCount() - 1);
                    pc.getInventory().updateItem(item,
                            L1PcInventory.COL_CHARGE_COUNT);

                } else {
                    pc.getInventory().removeItem(item, 1);
                }

            } else {
                pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生
            }

        }
    }

    private void polyAction(final L1PcInstance attacker, final L1Character cha) {
        boolean isSameClan = false;
        final Random _random = new Random();
        if (cha instanceof L1PcInstance) {
            final L1PcInstance pc = (L1PcInstance) cha;
            if ((pc.getClanid() != 0)
                    && (attacker.getClanid() == pc.getClanid())) {
                isSameClan = true;
            }
        }
        if ((attacker.getId() != cha.getId()) && !isSameClan) {
            final int probability = 3 * (attacker.getLevel() - cha.getLevel())
                    + 100 - cha.getMr();
            final int rnd = _random.nextInt(100) + 1;
            if (rnd > probability) {
                return;
            }
        }

        final int[] polyArray = { 29, 945, 947, 979, 1037, 1039, 3860, 3861,
                3862, 3863, 3864, 3865, 3904, 3906, 95, 146, 2374, 2376, 2377,
                2378, 3866, 3867, 3868, 3869, 3870, 3871, 3872, 3873, 3874,
                3875, 3876 };

        final int pid = _random.nextInt(polyArray.length);
        final int polyId = polyArray[pid];

        if (cha instanceof L1PcInstance) {
            final L1PcInstance pc = (L1PcInstance) cha;
            final int awakeSkillId = pc.getAwakeSkillId();
            if ((awakeSkillId == AWAKEN_ANTHARAS)
                    || (awakeSkillId == AWAKEN_FAFURION)
                    || (awakeSkillId == AWAKEN_VALAKAS)) {
                // 目前状态中无法变身。
                pc.sendPackets(new S_ServerMessage(1384));
                return;
            }

            if (pc.getInventory().checkEquipped(20281)) {// 变形控制戒指
                pc.sendPackets(new S_ShowPolyList(pc.getId()));
                if (!pc.isShapeChange()) {
                    pc.setSummonMonster(false);
                    pc.setShapeChange(true);
                }
                // 感受到魔法保护的力量
                pc.sendPackets(new S_ServerMessage(966));

            } else {
                final L1Skills skillTemp = SkillsTable.get().getTemplate(
                        SHAPE_CHANGE);
                if (attacker.getId() == pc.getId()) {
                    L1PolyMorph.doPoly(pc, polyId, skillTemp.getBuffDuration(),
                            L1PolyMorph.MORPH_BY_ITEMMAGIC);

                } else {
                    attacker.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生
                }
            }

        } else if (cha instanceof L1MonsterInstance) {
            final L1MonsterInstance mob = (L1MonsterInstance) cha;
            if (mob.getLevel() < 50) {
                final int npcId = mob.getNpcTemplate().get_npcId();
                if ((npcId != 45338) && (npcId != 45370) && (npcId != 45456)
                        && (npcId != 45464) && (npcId != 45473)
                        && (npcId != 45488) && (npcId != 45497)
                        && (npcId != 45516) && (npcId != 45529)
                        && (npcId != 45458)) {
                    final L1Skills skillTemp = SkillsTable.get().getTemplate(
                            SHAPE_CHANGE);
                    L1PolyMorph.doPoly(mob, polyId,
                            skillTemp.getBuffDuration(),
                            L1PolyMorph.MORPH_BY_ITEMMAGIC);
                }
            }
        }
    }
}
