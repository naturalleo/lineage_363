package com.lineage.server.timecontroller.server;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.config.ConfigAlt;
import com.lineage.server.datatables.lock.AuctionBoardReading;
import com.lineage.server.datatables.lock.ClanReading;
import com.lineage.server.datatables.lock.HouseReading;
import com.lineage.server.model.L1Clan;
import com.lineage.server.templates.L1AuctionBoardTmp;
import com.lineage.server.templates.L1House;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldClan;

/**
 * 血盟小屋税收时间轴
 * 
 * @author dexc
 * 
 */
public class ServerHouseTaxTimer extends TimerTask {

    private static final Log _log = LogFactory
            .getLog(ServerHouseTaxTimer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 600 * 1000;// 10分钟
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            checkTaxDeadline();

        } catch (final Exception e) {
            _log.error("血盟小屋税收时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final ServerHouseTaxTimer houseTaxTimer = new ServerHouseTaxTimer();
            houseTaxTimer.start();
        }
    }

    private static Calendar getRealTime() {
        final TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
        final Calendar cal = Calendar.getInstance(tz);
        return cal;
    }

    private static void checkTaxDeadline() {
        try {
            final Collection<L1House> houseList = HouseReading.get()
                    .getHouseTableList().values();
            if (!houseList.isEmpty()) {
                for (final L1House house : houseList) {
                    if (!house.isOnSale()) { // 竞卖中のアジトはチェックしない
                        if (house.getTaxDeadline().before(getRealTime())) {
                            sellHouse(house);
                        }
                    }
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private static void sellHouse(final L1House house) {
        final L1AuctionBoardTmp board = new L1AuctionBoardTmp();
        if (board != null) {
            // 竞卖揭示板に新规书き迂み
            final int houseId = house.getHouseId();
            board.setHouseId(houseId);
            board.setHouseName(house.getHouseName());
            board.setHouseArea(house.getHouseArea());
            final TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
            final Calendar cal = Calendar.getInstance(tz);
            cal.add(Calendar.DATE, 5); // 5日后
            cal.set(Calendar.MINUTE, 0); // 分、秒は切り舍て
            cal.set(Calendar.SECOND, 0);
            board.setDeadline(cal);
            board.setPrice(100000);
            board.setLocation(house.getLocation());
            board.setOldOwner("");
            board.setOldOwnerId(0);
            board.setBidder("");
            board.setBidderId(0);
            AuctionBoardReading.get().insertAuctionBoard(board);
            house.setOnSale(true); // 竞卖中に设定
            house.setPurchaseBasement(true); // 地下アジト未购入に设定
            cal.add(Calendar.DATE, ConfigAlt.HOUSE_TAX_INTERVAL);
            house.setTaxDeadline(cal);

            HouseReading.get().updateHouse(house); // DBに书き迂み
            // 以前の所有者のアジトを消す
            final Collection<L1Clan> allClans = WorldClan.get().getAllClans();
            for (final Iterator<L1Clan> iter = allClans.iterator(); iter
                    .hasNext();) {
                final L1Clan clan = iter.next();
                if (clan.getHouseId() == houseId) {
                    clan.setHouseId(0);
                    ClanReading.get().updateClan(clan);
                }
            }
        }
    }
}
