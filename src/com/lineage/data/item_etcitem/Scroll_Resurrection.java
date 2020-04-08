package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1TowerInstance;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;

/**
 * <font color=#00800>复活卷轴</font><BR>
 * Scroll of Resurrection<BR>
 * <font color=#00800>复活卷轴(祝福)</font><BR>
 * Scroll of Resurrection<BR>
 * <font color=#00800>复活与永生之誓约书</font><BR>
 * Contract of Resurrection and Eternal Life<BR>
 * 
 * @author dexc
 * 
 */
public class Scroll_Resurrection extends ItemExecutor {

    /**
	 *
	 */
    private Scroll_Resurrection() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Scroll_Resurrection();
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
        // 对象OBJID
        final int targObjId = data[0];

        // 取得目标资料
        final L1Character target = (L1Character) World.get().findObject(
                targObjId);

        // 例外状况:物件为空
        if (target == null) {
            return;
        }
        // 例外状况:物件是自己
        if (target.getId() == pc.getId()) {
            return;
        }
        // 例外状况:物件没有死亡
        if ((target.getCurrentHp() > 0) && !target.isDead()) {
            return;
        }

        // 删除道具
        pc.getInventory().removeItem(item, 1);

        // 目标是死亡的
        if (target.isDead()) {
            if (target instanceof L1PcInstance) {
                final L1PcInstance targetPc = (L1PcInstance) target;

                if (World.get().getVisiblePlayer(targetPc, 0).size() > 0) {
                    for (final L1PcInstance visiblePc : World.get()
                            .getVisiblePlayer(targetPc, 0)) {
                        if (!visiblePc.isDead()) {
                            // 592 复活失败，因为这个位置已被占据
                            pc.sendPackets(new S_ServerMessage(592));
                            return;
                        }
                    }
                }
                //城战不能复活 == hjx1000
                boolean isNowWar = false;
                final int castleId = L1CastleLocation.getCastleIdByArea(pc);
                if (castleId > 0) {
                    isNowWar = ServerWarExecutor.get().isNowWar(castleId);
                }
                if (isNowWar) {
                	return;
                }
              //城战不能复活 end  ==hjx1000
                if (pc.getMap().isUseResurrection()) {
                    targetPc.setTempID(pc.getId());
                    if (item.getItem().getBless() != 0) {
                        // 321 是否要复活？ (Y/N)
                        targetPc.sendPackets(new S_Message_YN(321));
                    } else {
                        // 322 是否要复活？ (Y/N)
                        targetPc.sendPackets(new S_Message_YN(322));
                    }
                } else {
                    return;
                }

            } else if (target instanceof L1NpcInstance) {
                if (!(target instanceof L1TowerInstance)) {
                    final L1NpcInstance npc = (L1NpcInstance) target;
                    // 不允许复活
                    if (npc.getNpcTemplate().isCantResurrect()) {
                        return;
                    }
                    //城战不能复活 == hjx1000
                    boolean isNowWar = false;
                    final int castleId = L1CastleLocation.getCastleIdByArea(npc);
                    if (castleId > 0) {
                        isNowWar = ServerWarExecutor.get().isNowWar(castleId);
                    }
                    if (isNowWar) {
                    	return;
                    }
                  //城战不能复活 end  ==hjx1000
                    if ((npc instanceof L1PetInstance)
                            && (World.get().getVisiblePlayer(npc, 0).size() > 0)) {
                        for (final L1PcInstance visiblePc : World.get()
                                .getVisiblePlayer(npc, 0)) {
                            if (!visiblePc.isDead()) {
                                // 592 复活失败，因为这个位置已被占据
                                pc.sendPackets(new S_ServerMessage(592));
                                return;
                            }
                        }
                    }
                    npc.resurrect(npc.getMaxHp() / 4);
                    npc.setResurrect(true);
                    npc.setDead(false);
                }
            }
        }
    }
}
