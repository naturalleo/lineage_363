package com.lineage.data.item_etcitem.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.data.quest.DragonKnightLv50_1;
import com.lineage.data.quest.IllusionistLv50_1;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.world.WorldQuest;

/**
 * 49202 时空裂痕邪念碎片
 */
public class ThoughtPieceTime extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(ThoughtPieceTime.class);

    /**
	 *
	 */
    private ThoughtPieceTime() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new ThoughtPieceTime();
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
        if (pc.isDragonKnight()) {// 龙骑士
            if (pc.getQuest().isStart(DragonKnightLv50_1.QUEST.get_id())) {
                pc.getInventory().removeItem(item, 1);// 移除道具
                staraQuest(pc, DragonKnightLv50_1.QUEST.get_id(),
                        DragonKnightLv50_1.MAPID);

            } else {
                // 内容显示
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "cot_ep1st"));
            }

        } else if (pc.isIllusionist()) {// 幻术师
            if (pc.getQuest().isStart(IllusionistLv50_1.QUEST.get_id())) {
                pc.getInventory().removeItem(item, 1);// 移除道具
                staraQuest(pc, IllusionistLv50_1.QUEST.get_id(),
                        IllusionistLv50_1.MAPID);

            } else {
                // 内容显示
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "cot_ep1st"));
            }

        } else {
            // 内容显示
            pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "cot_ep1st"));
        }
    }

    /**
     * 进入副本执行任务
     * 
     * @param pc
     * @param questid
     *            任务编号
     * @param questid
     *            任务地图编号
     * @return
     */
    private void staraQuest(L1PcInstance pc, final int questid, final int mapid) {
        try {
            // 取回新的任务副本编号
            final int showId = WorldQuest.get().nextId();

            // 进入人数限制
            int users = QuestMapTable.get().getTemplate(mapid);
            if (users == -1) {// 无限制
                users = Byte.MAX_VALUE;// 设置为127
            }

            // 加入副本执行成员
            final L1QuestUser quest = WorldQuest.get().put(showId, mapid,
                    questid, pc);

            if (quest == null) {
                _log.error("副本设置过程发生异常!!");
                // 关闭对话窗
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }

            // 取回进入时间限制
            final Integer time = QuestMapTable.get().getTime(mapid);
            if (time != null) {
                quest.set_time(time.intValue());
            }

            // 设置副本参加编号(已经在WorldQuest加入编号)
            // pc.set_showId(showId);
            // 传送任务执行者
            L1Teleport.teleport(pc, 32729, 32831, (short) mapid, 2, true);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
