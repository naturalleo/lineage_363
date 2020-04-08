package com.lineage.server.timecontroller.server;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.lock.TownReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.gametime.L1GameTime;
import com.lineage.server.model.gametime.L1GameTimeAdapter;
import com.lineage.server.model.gametime.L1GameTimeClock;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.world.World;

/**
 * 村庄系统
 * 
 * XXX 待处理优化 该时间轴尚与主游戏计时轴共用
 * 
 * @author dexc
 * 
 */
public class ServerHomeTownTime {

    private static final Log _log = LogFactory.getLog(ServerHomeTownTime.class);

    private static ServerHomeTownTime _instance;

    public static ServerHomeTownTime getInstance() {
        if (_instance == null) {
            _instance = new ServerHomeTownTime();
        }
        return _instance;
    }

    private ServerHomeTownTime() {
        startListener();
    }

    private static L1TownFixedProcListener _listener;

    private void startListener() {
        if (_listener == null) {
            _listener = new L1TownFixedProcListener();
            L1GameTimeClock.getInstance().addListener(_listener);
        }
    }

    private class L1TownFixedProcListener extends L1GameTimeAdapter {
        @Override
        public void onDayChanged(final L1GameTime time) {
            ServerHomeTownTime.this.fixedProc(time);
        }
    }

    private void fixedProc(final L1GameTime time) {
        final Calendar cal = time.getCalendar();
        final int day = cal.get(Calendar.DAY_OF_MONTH);

        if (day == 25) {
            this.monthlyProc();

        } else {
            this.dailyProc();
        }
    }

    public void dailyProc() {
        _log.info("村庄系统：日处理启动");
        TownReading.get().updateTaxRate();
        TownReading.get().updateSalesMoneyYesterday();
        TownReading.get().load();
    }

    public void monthlyProc() {
        _log.info("村庄系统：月处理启动");
        World.get().setProcessingContributionTotal(true);
        final Collection<L1PcInstance> players = World.get().getAllPlayers();
        for (final Iterator<L1PcInstance> iter = players.iterator(); iter
                .hasNext();) {
            final L1PcInstance pc = iter.next();
            try {
                pc.save();

            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }

        for (int townId = 1; townId <= 10; townId++) {
            final String leaderName = TownReading.get().totalContribution(
                    townId);
            if (leaderName != null) {
                final S_PacketBox packet = new S_PacketBox(
                        S_PacketBox.MSG_TOWN_LEADER, leaderName);
                for (final Iterator<L1PcInstance> iter = players.iterator(); iter
                        .hasNext();) {
                    final L1PcInstance pc = iter.next();
                    if (pc.getHomeTownId() == townId) {
                        pc.setContribution(0);
                        pc.sendPackets(packet);
                    }
                }
            }
        }
        TownReading.get().load();

        for (final Iterator<L1PcInstance> iter = players.iterator(); iter
                .hasNext();) {
            final L1PcInstance pc = iter.next();
            if (pc.getHomeTownId() == -1) {
                pc.setHomeTownId(0);
            }
            pc.setContribution(0);
            try {
                // 资料存档
                pc.save();

            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
        TownReading.get().clearHomeTownID();
        World.get().setProcessingContributionTotal(false);
    }
}
