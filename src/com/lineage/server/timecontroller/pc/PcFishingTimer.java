package com.lineage.server.timecontroller.pc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.server.datatables.FishingTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Fishing;
import com.lineage.server.templates.L1Item;
import com.lineage.server.thread.PcOtherThreadPool;

/**
 * 钓鱼时间轴
 * 
 * @author dexc
 * 
 */
public class PcFishingTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(PcFishingTimer.class);

    private ScheduledFuture<?> _timer;

    private static final List<L1PcInstance> _fishingList = new ArrayList<L1PcInstance>();

    private static final Random _random = new Random();

    public void start() {
        final int timeMillis = 20 * 1000;// 20秒
        _timer = PcOtherThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            fishing();

        } catch (final Exception e) {
            _log.error("钓鱼时间轴异常重启", e);
            PcOtherThreadPool.get().cancel(_timer, false);
            final PcFishingTimer timerTask = new PcFishingTimer();
            timerTask.start();
        }
    }

    /**
     * 加入清单
     * 
     * @param pc
     */
    public static void addMember(final L1PcInstance pc) {
        if ((pc == null) || _fishingList.contains(pc)) {
            return;
        }
        _fishingList.add(pc);
    }

    /**
     * 移出清单
     * 
     * @param pc
     */
    public static void removeMember(final L1PcInstance pc) {
        if ((pc == null) || !_fishingList.contains(pc)) {
            return;
        }
        _fishingList.remove(pc);
    }

    private void fishing() {
        // 包含元素
        if (!_fishingList.isEmpty()) {
            for (int i = 0; i < _fishingList.size(); i++) {
                final L1PcInstance pc = _fishingList.get(i);
                // 正在钓鱼的玩家
                if (pc.isFishing()) {
                    if (pc.getMapId() != 5300) {
                        finishFishing(pc, true);
                        continue;
                    }
                    // 没有饵
                    if (!pc.getInventory().checkItem(41295, 2)) {// 饵
                        finishFishing(pc, true);
                        continue;
                    }

                    if (pc.getOnlineStatus() != 1) {
                        finishFishing(pc, true);
                        continue;
                    }

                    if (pc.getNetConnection() == null) {
                        finishFishing(pc, true);
                        continue;
                    }

                    L1Fishing temp = FishingTable.get().get_item();
                    if (temp != null) {
                        if (_random.nextInt(temp.get_randomint()) > temp
                                .get_random()) {
                            temp = null;
                        }
                        successFishing(pc, temp);
                    }
                }
            }
        }
    }

    /**
     * 结束
     * 
     * @param pc
     */
    public static void finishFishing(final L1PcInstance pc, final boolean msg) {
        pc.setFishing(false, -1, -1);
        if (msg) {
            // 钓鱼已经结束了。
            pc.sendPackets(new S_ServerMessage(1163));
        }
        pc.sendPacketsAll(new S_CharVisualUpdate(pc));
        removeMember(pc);
    }

    /**
     * 钓鱼结果
     * 
     * @param pc
     * @param temp
     */
    private void successFishing(L1PcInstance pc, L1Fishing temp) {
        if (temp == null) {
            if (!pc.getInventory().consumeItem(41295, 2)) {// 饵(删除饵)
                finishFishing(pc, true);
            }

        } else {
            final L1Item item = ItemTable.get().getTemplate(temp.get_itemid());
            if (item != null) {
                if (pc.getInventory().consumeItem(41295, temp.get_bait())) {// 饵(删除饵)
                    CreateNewItem.createNewItem(pc, temp.get_itemid(), 1);

                } else {
                    finishFishing(pc, true);
                }
            }
        }
    }
}
