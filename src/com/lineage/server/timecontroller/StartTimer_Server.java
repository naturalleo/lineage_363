package com.lineage.server.timecontroller;

import com.lineage.config.ConfigAlt;
import com.lineage.server.timecontroller.server.ServerAuctionTimer;
import com.lineage.server.timecontroller.server.ServerDeleteItemTimer;
import com.lineage.server.timecontroller.server.ServerElementalStoneTimer;
import com.lineage.server.timecontroller.server.ServerHomeTownTime;
import com.lineage.server.timecontroller.server.ServerHouseTaxTimer;
import com.lineage.server.timecontroller.server.ServerItemUserTimer;
import com.lineage.server.timecontroller.server.ServerLightTimer;
import com.lineage.server.timecontroller.server.ServerRestartTimer;
import com.lineage.server.timecontroller.server.ServerUseMapTimer;
import com.lineage.server.timecontroller.server.ServerWarTimer;
import com.lineage.server.timecontroller.server.ServerTrapTimer;

/**
 * 服务器专用时间轴 初始化启动
 * 
 * @author dexc
 * 
 */
public class StartTimer_Server {

    public void start() throws InterruptedException {
        // 陷阱召唤处理时间轴
        final ServerTrapTimer trapTimer = new ServerTrapTimer();
        trapTimer.start();
        Thread.sleep(50);// 延迟

        // 村庄系统
        ServerHomeTownTime.getInstance();
        Thread.sleep(50);// 延迟

        // 城战计时轴
        final ServerWarTimer warTimer = new ServerWarTimer();
        warTimer.start();
        Thread.sleep(50);// 延迟

        // 拍卖公告栏 更新计时器
        final ServerAuctionTimer auctionTimeController = new ServerAuctionTimer();
        auctionTimeController.start();
        Thread.sleep(50);// 延迟

        // 血盟小屋税收计时器
        final ServerHouseTaxTimer houseTaxTimeController = new ServerHouseTaxTimer();
        houseTaxTimeController.start();
        Thread.sleep(50);// 延迟

        // 灯光照明计时器
        final ServerLightTimer lightTimeController = new ServerLightTimer();
        lightTimeController.start();
        Thread.sleep(50);// 延迟

        // 元素石生成 计时器
        if (ConfigAlt.ELEMENTAL_STONE_AMOUNT > 0) {
            final ServerElementalStoneTimer elementalStoneGenerator = new ServerElementalStoneTimer();
            elementalStoneGenerator.start();
            Thread.sleep(50);// 延迟
        }

        // 地面物品清除
        if (ConfigAlt.ALT_ITEM_DELETION_TIME > 0) {
            final ServerDeleteItemTimer deleteitem = new ServerDeleteItemTimer();
            deleteitem.start();
            Thread.sleep(50);// 延迟
        }

        // 自动重启计时器
        final ServerRestartTimer autoRestart = new ServerRestartTimer();
        autoRestart.start();
        Thread.sleep(50);// 延迟

        // 计时地图时间轴
        final ServerUseMapTimer useMapTimer = new ServerUseMapTimer();
        useMapTimer.start();
        Thread.sleep(50);// 延迟

        // 物品使用期限计时时间轴异常重启
        final ServerItemUserTimer userTimer = new ServerItemUserTimer();
        userTimer.start();
    }
}
