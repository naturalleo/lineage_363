package com.lineage.server.timecontroller;

import com.lineage.server.timecontroller.npc.NpcBowTimer;
import com.lineage.server.timecontroller.npc.NpcChatTimer;
import com.lineage.server.timecontroller.npc.NpcDeadTimer;
import com.lineage.server.timecontroller.npc.NpcDeleteTimer;
import com.lineage.server.timecontroller.npc.NpcDigestItemTimer;
import com.lineage.server.timecontroller.npc.NpcHprTimer;
import com.lineage.server.timecontroller.npc.NpcMprTimer;
import com.lineage.server.timecontroller.npc.NpcRestTimer;
import com.lineage.server.timecontroller.npc.NpcSpawnBossTimer;
import com.lineage.server.timecontroller.npc.NpcWorkTimer;

/**
 * NPC专用时间轴 初始化启动
 * 
 * @author dexc
 * 
 */
public class StartTimer_Npc {

    public void start() throws InterruptedException {
        // NPC对话计时器
        final NpcChatTimer npcChatTimeController = new NpcChatTimer();
        npcChatTimeController.start();
        Thread.sleep(50);// 延迟

        // HP 回复
        final NpcHprTimer npcHprTimer = new NpcHprTimer();
        npcHprTimer.start();
        Thread.sleep(50);// 延迟

        // MP 回复
        final NpcMprTimer npcMprTimer = new NpcMprTimer();
        npcMprTimer.start();
        Thread.sleep(50);// 延迟

        // 时间性质NPC
        final NpcDeleteTimer npcDeleteTimer = new NpcDeleteTimer();
        npcDeleteTimer.start();
        Thread.sleep(50);// 延迟

        // 死亡NPC
        final NpcDeadTimer npcDeadTimer = new NpcDeadTimer();
        npcDeadTimer.start();
        Thread.sleep(50);// 延迟

        // NPC消化道具时间
        final NpcDigestItemTimer digestItemTimer = new NpcDigestItemTimer();
        digestItemTimer.start();
        Thread.sleep(50);// 延迟

        // NPC(BOSS)召唤时间时间轴
        final NpcSpawnBossTimer bossTimer = new NpcSpawnBossTimer();
        bossTimer.start();
        Thread.sleep(50);// 延迟

        // NPC动作暂停
        final NpcRestTimer restTimer = new NpcRestTimer();
        restTimer.start();
        Thread.sleep(50);// 延迟

        // NPC工作时间轴
        final NpcWorkTimer workTimer = new NpcWorkTimer();
        workTimer.start();
        Thread.sleep(50);// 延迟

        // 箭孔
        final NpcBowTimer bow = new NpcBowTimer();
        bow.start();
    }
}
