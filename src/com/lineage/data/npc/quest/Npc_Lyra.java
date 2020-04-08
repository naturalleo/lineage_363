package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ALv15_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 莱拉<BR>
 * 70811<BR>
 * 击退妖魔的契约(全职业15级任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Lyra extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Lyra.class);

    private Npc_Lyra() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Lyra();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            boolean isOrg = false;
            if (pc.getTempCharGfx() == 3906) {// 妖魔
                isOrg = true;
            }
            if (pc.getTempCharGfx() == 3860) {// 妖魔弓箭手
                isOrg = true;
            }
            if (pc.getTempCharGfx() == 3864) {// 妖魔斗士
                isOrg = true;
            }
            if (pc.getTempCharGfx() == 3866) {// 甘地妖魔
                isOrg = true;
            }
            if (pc.getTempCharGfx() == 3869) {// 都达玛拉妖魔
                isOrg = true;
            }
            if (pc.getTempCharGfx() == 3868) {// 阿吐巴妖魔
                isOrg = true;
            }
            if (pc.getTempCharGfx() == 2323) {// 妖魔巡守
                isOrg = true;
            }

            if (isOrg) {
                // 你们有个叫‘妖魔’的种族吧！他们已经帮你赚了这么多钱，你还来抢我们的东西！！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "lyra11"));
                return;
            }

            // 任务已经完成
            if (pc.getQuest().isEnd(ALv15_1.QUEST.get_id())) {
                // 欢迎！你是一个冒险家吗？或者是佣兵？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "lyra1"));

            } else {
                // 等级达成要求
                if (pc.getLevel() >= ALv15_1.QUEST.get_questlevel()) {
                    if (pc.getQuest().isStart(ALv15_1.QUEST.get_id())) {
                        // 如果你同意，你可以一直帮助我们直到战争结束吗？
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "lyraev3"));

                    } else {
                        // 与莱拉订定契约
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "lyraev1"));
                    }

                } else {
                    // 欢迎！你是一个冒险家吗？或者是佣兵？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "lyra1"));
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;

        if (cmd.equalsIgnoreCase("contract1")) {// 与莱拉订定契约
            // 将任务设置为启动
            QuestClass.get().startQuest(pc, ALv15_1.QUEST.get_id());

            // 如同你所见，我的村庄很穷，所以我们不能付你很多钱...
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "lyraev2"));

        } else if (cmd.equalsIgnoreCase("contract1yes")) {// 领取赏金和重新订契约
            getAdena(pc, npc);

            // 我跟你说过的酬劳在这里，拿去吧！
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "lyraev5"));

        } else if (cmd.equalsIgnoreCase("contract1no")) {// 领取赏金和终止契约
            if (pc.getQuest().isStart(ALv15_1.QUEST.get_id())) {

                getAdena(pc, npc);
                // 将任务设置为结束
                QuestClass.get().endQuest(pc, ALv15_1.QUEST.get_id());

                // 非常感谢你。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "lyraev4"));

            } else {
                getAdena(pc, npc);
                isCloseList = true;
            }
        }

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    /**
     * 给予金币<BR>
     * 阿吐巴的图腾值２００个金币<BR>
     * 那鲁加的值１００个<BR>
     * 罗孚和都达玛拉的值５０个<BR>
     * 甘地的值３０个<BR>
     * 
     * @param pc
     * @param npc
     */
    private void getAdena(L1PcInstance pc, L1NpcInstance npc) {
        long adena = 0;
        final L1ItemInstance item1 = pc.getInventory().findItemId(40131);// 甘地图腾
        if (item1 != null) {
            adena += (30 * pc.getInventory().removeItem(item1));
        }

        final L1ItemInstance item2 = pc.getInventory().findItemId(40132);// 那鲁加图腾
        if (item2 != null) {
            adena += (100 * pc.getInventory().removeItem(item2));
        }

        final L1ItemInstance item3 = pc.getInventory().findItemId(40133);// 都达玛拉图腾
        if (item3 != null) {
            adena += (50 * pc.getInventory().removeItem(item3));
        }

        final L1ItemInstance item4 = pc.getInventory().findItemId(40134);// 罗孚图腾
        if (item4 != null) {
            adena += (50 * pc.getInventory().removeItem(item4));
        }

        final L1ItemInstance item5 = pc.getInventory().findItemId(40135);// 阿吐巴图腾
        if (item5 != null) {
            adena += (200 * pc.getInventory().removeItem(item5));
        }

        if (adena > 0) {
            CreateNewItem.createNewItem(pc, 40308, adena);
        }
    }
}
