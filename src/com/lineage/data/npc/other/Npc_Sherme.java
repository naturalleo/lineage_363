package com.lineage.data.npc.other;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_ItemCount;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 道具制作^米米<BR>
 * 70938
 * 
 * @author dexc
 * 
 */
public class Npc_Sherme extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Sherme.class);

    /**
	 *
	 */
    private Npc_Sherme() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Sherme();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            // 嗯..原来你也是屠龙者..
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "sherme2"));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        if (cmd.equalsIgnoreCase("a")) {// 制作地龙之魔眼
            int[] items = new int[] { 42514, 40308 };
            int[] counts = new int[] { 1, 100000 };
            int[] gitems = new int[] { 42518 };
            int[] gcounts = new int[] { 1 };
            // 可制作数量
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount == 1) {
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, 1, gcounts);// 给予
                isCloseList = true;

            } else if (xcount > 1) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, "a1"));

            } else if (xcount < 1) {
                isCloseList = true;
            }

        } else if (cmd.equalsIgnoreCase("a1")) {
            int[] items = new int[] { 42514, 40308 };
            int[] counts = new int[] { 1, 100000 };
            int[] gitems = new int[] { 42518 };
            int[] gcounts = new int[] { 1 };
            // 可制作数量
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount >= amount) {
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, amount, gcounts);// 给予
            }
            isCloseList = true;

        } else if (cmd.equalsIgnoreCase("b")) {// 制作水龙之魔眼
            int[] items = new int[] { 42515, 40308 };
            int[] counts = new int[] { 1, 100000 };
            int[] gitems = new int[] { 42519 };
            int[] gcounts = new int[] { 1 };
            // 可制作数量
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount == 1) {
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, 1, gcounts);// 给予
                isCloseList = true;

            } else if (xcount > 1) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, "b1"));

            } else if (xcount < 1) {
                isCloseList = true;
            }

        } else if (cmd.equalsIgnoreCase("b1")) {
            int[] items = new int[] { 42515, 40308 };
            int[] counts = new int[] { 1, 100000 };
            int[] gitems = new int[] { 42519 };
            int[] gcounts = new int[] { 1 };
            // 可制作数量
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount >= amount) {
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, amount, gcounts);// 给予
            }
            isCloseList = true;

        } else if (cmd.equalsIgnoreCase("c")) {// 制作火龙之魔眼
            int[] items = new int[] { 42517, 40308 };
            int[] counts = new int[] { 1, 100000 };
            int[] gitems = new int[] { 42521 };
            int[] gcounts = new int[] { 1 };
            // 可制作数量
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount == 1) {
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, 1, gcounts);// 给予
                isCloseList = true;

            } else if (xcount > 1) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, "c1"));

            } else if (xcount < 1) {
                isCloseList = true;
            }

        } else if (cmd.equalsIgnoreCase("c1")) {
            int[] items = new int[] { 42517, 40308 };
            int[] counts = new int[] { 1, 100000 };
            int[] gitems = new int[] { 42521 };
            int[] gcounts = new int[] { 1 };
            // 可制作数量
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount >= amount) {
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, amount, gcounts);// 给予
            }
            isCloseList = true;

        } else if (cmd.equalsIgnoreCase("d")) {// 制作风龙之魔眼
            int[] items = new int[] { 42516, 40308 };
            int[] counts = new int[] { 1, 100000 };
            int[] gitems = new int[] { 42520 };
            int[] gcounts = new int[] { 1 };
            // 可制作数量
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount == 1) {
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, 1, gcounts);// 给予
                isCloseList = true;

            } else if (xcount > 1) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, "d1"));

            } else if (xcount < 1) {
                isCloseList = true;
            }

        } else if (cmd.equalsIgnoreCase("d1")) {
            int[] items = new int[] { 42516, 40308 };
            int[] counts = new int[] { 1, 100000 };
            int[] gitems = new int[] { 42520 };
            int[] gcounts = new int[] { 1 };
            // 可制作数量
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount >= amount) {
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, amount, gcounts);// 给予
            }
            isCloseList = true;

        } else if (cmd.equalsIgnoreCase("e")) {// 制作诞生之魔眼
            int[] items = new int[] { 42525, 42526, 40308 };
            int[] counts = new int[] { 1, 1, 200000 };
            int[] gitems = new int[] { 42522 };
            int[] gcounts = new int[] { 1 };
            // 可制作数量
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount == 1) {
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, 1, gcounts);// 给予
                isCloseList = true;

            } else if (xcount > 1) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, "e1"));

            } else if (xcount < 1) {
                isCloseList = true;
            }

        } else if (cmd.equalsIgnoreCase("e1")) {
            int[] items = new int[] { 42525, 42526, 40308 };
            int[] counts = new int[] { 1, 1, 200000 };
            int[] gitems = new int[] { 42522 };
            int[] gcounts = new int[] { 1 };
            // 可制作数量
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount >= amount) {
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, amount, gcounts);// 给予
            }
            isCloseList = true;

        } else if (cmd.equalsIgnoreCase("f")) {// 制作形象之魔眼
            int[] items = new int[] { 42522, 42527, 40308 };
            int[] counts = new int[] { 1, 1, 200000 };
            int[] gitems = new int[] { 42523 };
            int[] gcounts = new int[] { 1 };
            // 可制作数量
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount == 1) {
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, 1, gcounts);// 给予
                isCloseList = true;

            } else if (xcount > 1) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, "f1"));

            } else if (xcount < 1) {
                isCloseList = true;
            }

        } else if (cmd.equalsIgnoreCase("f1")) {
            int[] items = new int[] { 42522, 42527, 40308 };
            int[] counts = new int[] { 1, 1, 200000 };
            int[] gitems = new int[] { 42523 };
            int[] gcounts = new int[] { 1 };
            // 可制作数量
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount >= amount) {
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, amount, gcounts);// 给予
            }
            isCloseList = true;

        } else if (cmd.equalsIgnoreCase("g")) {// 制作生命之魔眼
            int[] items = new int[] { 42523, 42528, 40308 };
            int[] counts = new int[] { 1, 1, 200000 };
            int[] gitems = new int[] { 42524 };
            int[] gcounts = new int[] { 1 };
            // 可制作数量
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount == 1) {
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, 1, gcounts);// 给予
                isCloseList = true;

            } else if (xcount > 1) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, "g1"));

            } else if (xcount < 1) {
                isCloseList = true;
            }

        } else if (cmd.equalsIgnoreCase("g1")) {
            int[] items = new int[] { 42523, 42528, 40308 };
            int[] counts = new int[] { 1, 1, 200000 };
            int[] gitems = new int[] { 42524 };
            int[] gcounts = new int[] { 1 };
            // 可制作数量
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount >= amount) {
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, amount, gcounts);// 给予
            }
            isCloseList = true;
        }

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
