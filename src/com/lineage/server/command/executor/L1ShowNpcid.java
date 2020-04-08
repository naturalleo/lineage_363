package com.lineage.server.command.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.Instance.L1TrapInstance;
import com.lineage.server.serverpackets.S_Chat;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 显示物件ID
 * 
 * @author dexc DELETE FROM `commands` WHERE `name`='id'; INSERT INTO `commands`
 *         VALUES ('id', '200', 'L1ShowNpcid', '显示物件资讯', '0');
 * 
 */
public class L1ShowNpcid implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1ShowNpcid.class);

    private L1ShowNpcid() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1ShowNpcid();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            for (final L1Object object : pc.getKnownObjects()) {
                if (object instanceof L1ItemInstance) {
                    final L1ItemInstance tg = (L1ItemInstance) object;
                    pc.sendPackets(new S_Chat(tg, "ItemId:" + tg.getItemId(), 0));
                    continue;
                }

                if (object instanceof L1PcInstance) {
                    final L1PcInstance tg = (L1PcInstance) object;
                    pc.sendPackets(new S_Chat(tg, "Objid:" + tg.getId(), 0));
                    continue;
                }
                // 陷阱
                if (object instanceof L1TrapInstance) {
                    final L1TrapInstance tg = (L1TrapInstance) object;
                    pc.sendPackets(new S_Chat(object, "XY:" + tg.getX() + "/"
                            + tg.getY(), 0));
                    continue;
                }
                // 宠物
                if (object instanceof L1PetInstance) {
                    // final L1PetInstance tg = (L1PetInstance) object;
                    pc.sendPackets(new S_Chat(object, "tg: Pet", 0));
                    continue;
                }
                // 召唤兽
                if (object instanceof L1SummonInstance) {
                    pc.sendPackets(new S_Chat(object, "tg: Summon", 0));
                    continue;
                }
                // 魔法娃娃
                if (object instanceof L1DollInstance) {
                    final L1DollInstance tg = (L1DollInstance) object;
                    pc.sendPackets(new S_Chat(object, "Over Time:"
                            + tg.get_time(), 0));
                    continue;
                }
                // Effect(技能物件)
                if (object instanceof L1EffectInstance) {
                    // final L1EffectInstance tg = (L1EffectInstance) object;
                    pc.sendPackets(new S_Chat(object, "tg: Effect", 0));
                    continue;
                }
                // MOB
                if (object instanceof L1MonsterInstance) {
                    final L1MonsterInstance tg = (L1MonsterInstance) object;
                    pc.sendPackets(new S_Chat(object, "NpcId:" + tg.getNpcId(),
                            0));
                    continue;
                }
                // NPC
                if (object instanceof L1NpcInstance) {
                    final L1NpcInstance tg = (L1NpcInstance) object;
                    pc.sendPackets(new S_Chat(object, "NpcId:" + tg.getNpcId(),
                            0));
                    continue;
                }
            }

        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName());
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
