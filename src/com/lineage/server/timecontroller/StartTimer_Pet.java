package com.lineage.server.timecontroller;

import com.lineage.server.timecontroller.pet.*;

/**
 * PET专用时间轴 初始化启动
 * 
 * @author dexc
 * 
 */
public class StartTimer_Pet {

    public void start() throws InterruptedException {

        // Pet HP自然回复时间轴异
        final PetHprTimer petHprTimer = new PetHprTimer();
        petHprTimer.start();
        Thread.sleep(50);// 延迟

        // Pet MP自然回复时间轴
        final PetMprTimer petMprTimer = new PetMprTimer();
        petMprTimer.start();
        Thread.sleep(50);// 延迟

        // Summon HP自然回复时间轴
        final SummonHprTimer summonHprTimer = new SummonHprTimer();
        summonHprTimer.start();
        Thread.sleep(50);// 延迟

        // Summon MP自然回复时间轴
        final SummonMprTimer summonMprTimer = new SummonMprTimer();
        summonMprTimer.start();
        Thread.sleep(50);// 延迟

        // 召唤兽处理时间轴
        final SummonTimer summon_Timer = new SummonTimer();
        summon_Timer.start();
        Thread.sleep(50);// 延迟

        // 魔法娃娃处理时间轴
        final DollTimer dollTimer = new DollTimer();
        dollTimer.start();

        final DollHprTimer dollHpTimer = new DollHprTimer();
        dollHpTimer.start();

        final DollMprTimer dollMpTimer = new DollMprTimer();
        dollMpTimer.start();

        final DollGetTimer dollGetTimer = new DollGetTimer();
        dollGetTimer.start();

        final DollAidTimer dollAidTimer = new DollAidTimer();
        dollAidTimer.start();
    }
}
