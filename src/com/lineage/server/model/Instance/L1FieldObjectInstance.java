package com.lineage.server.model.Instance;

import static com.lineage.server.model.skill.L1SkillId.CANCELLATION;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.L1HauntedHouse;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_NPCPack_F;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.world.World;

/**
 * 对象:景观 控制项
 * 
 * @author dexc
 * 
 */
public class L1FieldObjectInstance extends L1NpcInstance {

    private static final long serialVersionUID = 1L;

    private static final Log _log = LogFactory
            .getLog(L1FieldObjectInstance.class);

    public L1FieldObjectInstance(final L1Npc template) {
        super(template);
    }

    /**
     * TODO 接触资讯
     */
    @Override
    public void onPerceive(final L1PcInstance perceivedFrom) {
        try {
            perceivedFrom.addKnownObject(this);
            perceivedFrom.sendPackets(new S_NPCPack_F(this));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void onAction(final L1PcInstance pc) {
        try {
            if (this.getNpcTemplate().get_npcId() == 81171) { // おばけ屋敷のゴールの炎
                if (L1HauntedHouse.getInstance().getHauntedHouseStatus() == L1HauntedHouse.STATUS_PLAYING) {
                    final int winnersCount = L1HauntedHouse.getInstance()
                            .getWinnersCount();
                    final int goalCount = L1HauntedHouse.getInstance()
                            .getGoalCount();
                    if (winnersCount == goalCount + 1) {
                        final L1ItemInstance item = ItemTable.get().createItem(
                                41308); // 勇者のパンプキン袋
                        final long count = 1;
                        if (item != null) {
                            if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
                                item.setCount(count);
                                pc.getInventory().storeItem(item);
                                pc.sendPackets(new S_ServerMessage(403, item
                                        .getLogName())); // %0を手に入れました。
                            }
                        }
                        L1HauntedHouse.getInstance().endHauntedHouse();

                    } else if (winnersCount > goalCount + 1) {
                        L1HauntedHouse.getInstance()
                                .setGoalCount(goalCount + 1);
                        L1HauntedHouse.getInstance().removeMember(pc);
                        final L1ItemInstance item = ItemTable.get().createItem(
                                41308); // 勇者のパンプキン袋
                        final long count = 1;
                        if (item != null) {
                            if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
                                item.setCount(count);
                                pc.getInventory().storeItem(item);
                                pc.sendPackets(new S_ServerMessage(403, item
                                        .getLogName())); // %0を手に入れました。
                            }
                        }

                        final L1SkillUse l1skilluse = new L1SkillUse();
                        l1skilluse.handleCommands(pc, CANCELLATION, pc.getId(),
                                pc.getX(), pc.getY(), 0, L1SkillUse.TYPE_LOGIN);

                        L1Teleport.teleport(pc, 32624, 32813, (short) 4, 5,
                                true);
                    }
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void deleteMe() {
        try {
            this._destroyed = true;
            if (this.getInventory() != null) {
                this.getInventory().clearItems();
            }
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
}
