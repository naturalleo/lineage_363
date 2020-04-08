package com.lineage.server.timecontroller;

import com.lineage.config.Config;
import com.lineage.config.ConfigAlt;
import com.lineage.server.timecontroller.pc.*;

/**
 * PC专用时间轴 初始化启动
 * 
 * @author dexc
 * 
 */
public class StartTimer_Pc {

    public void start() throws InterruptedException {
        // 人物资料自动保存计时器
        if (Config.AUTOSAVE_INTERVAL > 0) {
            final PcAutoSaveTimer save = new PcAutoSaveTimer();
            save.start();
        }
        // 背包物品自动保存计时器
        if (Config.AUTOSAVE_INTERVAL_INVENTORY > 0) {
            final PcAutoSaveInventoryTimer save = new PcAutoSaveInventoryTimer();
            save.start();
        }
        // 人物钓鱼计时器
        final PcFishingTimer fishingTimeController = new PcFishingTimer();
        fishingTimeController.start();
        Thread.sleep(50);// 延迟

        // PC 可见物更新处理 时间轴 XXX
        final UpdateObjectCTimer objectCTimer = new UpdateObjectCTimer();
        objectCTimer.start();
        final UpdateObjectDKTimer objectDKTimer = new UpdateObjectDKTimer();
        objectDKTimer.start();
        final UpdateObjectDTimer objectDTimer = new UpdateObjectDTimer();
        objectDTimer.start();
        final UpdateObjectETimer objectETimer = new UpdateObjectETimer();
        objectETimer.start();
        final UpdateObjectITimer objectITimer = new UpdateObjectITimer();
        objectITimer.start();
        final UpdateObjectKTimer objectKTimer = new UpdateObjectKTimer();
        objectKTimer.start();
        final UpdateObjectWTimer objectWTimer = new UpdateObjectWTimer();
        objectWTimer.start();
        Thread.sleep(50);// 延迟

        final HprTimerCrown hprCrown = new HprTimerCrown();
        hprCrown.start();
        final HprTimerDarkElf hprDarkElf = new HprTimerDarkElf();
        hprDarkElf.start();
        final HprTimerDragonKnight hprDK = new HprTimerDragonKnight();
        hprDK.start();
        final HprTimerElf hprElf = new HprTimerElf();
        hprElf.start();
        final HprTimerIllusionist hprIllusionist = new HprTimerIllusionist();
        hprIllusionist.start();
        final HprTimerKnight hprKnight = new HprTimerKnight();
        hprKnight.start();
        final HprTimerWizard hprWizard = new HprTimerWizard();
        hprWizard.start();
        Thread.sleep(50);// 延迟

        final MprTimerCrown mprCrown = new MprTimerCrown();
        mprCrown.start();
        final MprTimerDarkElf mprDarkElf = new MprTimerDarkElf();
        mprDarkElf.start();
        final MprTimerDragonKnight mprDragonKnight = new MprTimerDragonKnight();
        mprDragonKnight.start();
        final MprTimerElf mprElf = new MprTimerElf();
        mprElf.start();
        final MprTimerIllusionist mprIllusionist = new MprTimerIllusionist();
        mprIllusionist.start();
        final MprTimerKnight mprKnight = new MprTimerKnight();
        mprKnight.start();
        final MprTimerWizard mprWizard = new MprTimerWizard();
        mprWizard.start();
        Thread.sleep(50);// 延迟

        // PC EXP更新处理 时间轴
        final ExpTimer expTimer = new ExpTimer();
        expTimer.start();
        // PC Lawful更新处理 时间轴
        final LawfulTimer lawfulTimer = new LawfulTimer();
        lawfulTimer.start();
        // PC 死亡删除处理 时间轴
        final PcDeleteTimer deleteTimer = new PcDeleteTimer();
        deleteTimer.start();
        // PC 鬼魂模式处理 时间轴
        final PcGhostTimer ghostTimer = new PcGhostTimer();
        ghostTimer.start();
        // PC 解除人物卡点计时时间轴
        final UnfreezingTimer unfreezingTimer = new UnfreezingTimer();
        unfreezingTimer.start();
        //新增玩家定时执行任务
        final Players_timing playersTiming = new Players_timing();
        playersTiming.start();
        // 队伍更新时间轴
        final PartyTimer partyTimer = new PartyTimer();
        partyTimer.start();
        Thread.sleep(50);// 延迟

        if (ConfigAlt.ALT_PUNISHMENT) {
            final PcHellTimer hellTimer = new PcHellTimer();
            hellTimer.start();
        }
    }
}
