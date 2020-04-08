package com.lineage.data.item_etcitem.magicreel;

import java.util.Random;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.ActionCodes;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

/**
 * 49335 火之徽章
 */
public class FireBadge extends ItemExecutor {

    private final Random _random = new Random();

    /**
	 *
	 */
    private FireBadge() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new FireBadge();
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
        if (pc == null) {
            return;
        }
        if (item == null) {
            return;
        }

        // 解除魔法技能绝对屏障
        L1BuffUtil.cancelAbsoluteBarrier(pc);

        if (pc.isInvisble()) {// 隐身状态
            pc.delInvis(); // 解除隐身状态
        }

        // 送出动作封包
        pc.sendPacketsAll(new S_DoActionGFX(pc.getId(),
                ActionCodes.ACTION_SkillBuff));

        // 可用次数
        final int chargeCount = item.getChargeCount();

        if (chargeCount <= 0) {
            if (pc.getInventory().removeItem(item, 1) == 1) {
                // 154：\f1这个卷轴散开了。
                // 79 没有任何事情发生
                pc.sendPackets(new S_ServerMessage(154));
            }

        } else {
            // 建立新次数(可用次数-1)
            item.setChargeCount(item.getChargeCount() - 1);
            // 更新可用次数
            pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);

            int x = pc.getX();
            int y = pc.getY();
            int mapId = pc.getMapId();

            pc.sendPacketsXR(new S_EffectLocation(new L1Location(x + 1, y + 1,
                    mapId), 1819), 7);
            pc.sendPacketsXR(new S_EffectLocation(new L1Location(x - 1, y + 1,
                    mapId), 1819), 7);
            pc.sendPacketsXR(new S_EffectLocation(new L1Location(x - 1, y - 1,
                    mapId), 1819), 7);
            pc.sendPacketsXR(new S_EffectLocation(new L1Location(x + 1, y - 1,
                    mapId), 1819), 7);

            L1PcInstance tgpc = (L1PcInstance) pc.getNowTarget();
            if (tgpc != null) {
                if (!tgpc.isSafetyZone()) {
                    // 被攻击者受伤(伤害为160-220)
                    double dmg = (_random.nextInt(60) + 160) * 1.0D;
                    tgpc.receiveDamage(pc, dmg, false, false);
                    // 周边对象血盟成员伤害
                    for (L1PcInstance tgClanPc : World.get().getVisiblePlayer(
                            tgpc, 4)) {
                        if (tgClanPc.getClanid() == tgpc.getClanid()) {
                            if (!tgpc.isSafetyZone()) {
                                // 被攻击者受伤
                                tgClanPc.receiveDamage(pc, (dmg * 0.8), false,
                                        false);
                                // 受伤动作
                                tgClanPc.broadcastPacketX8(new S_DoActionGFX(
                                        tgClanPc.getId(),
                                        ActionCodes.ACTION_Damage));
                            }
                        }
                    }
                }
            }

            // 周边对象MOB伤害
            for (L1Object object : World.get().getVisibleObjects(pc, 5)) {
                if (object instanceof L1MonsterInstance) {
                    L1MonsterInstance mob = (L1MonsterInstance) object;
                    // 被攻击者受伤(伤害为160-220)
                    int dmg = _random.nextInt(60) + 160;
                    // 被攻击者受伤
                    mob.receiveDamage(pc, dmg);
                    // 受伤动作
                    mob.broadcastPacketX8(new S_DoActionGFX(mob.getId(),
                            ActionCodes.ACTION_Damage));
                }
            }
        }
    }
}
