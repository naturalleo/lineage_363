package com.lineage.server.timecontroller;

import com.lineage.server.timecontroller.skill.EffectCubeBurnTimer;
import com.lineage.server.timecontroller.skill.EffectCubeEruptionTimer;
import com.lineage.server.timecontroller.skill.EffectCubeHarmonizeTimer;
import com.lineage.server.timecontroller.skill.EffectCubeShockTimer;
import com.lineage.server.timecontroller.skill.EffectFirewallTimer;
import com.lineage.server.timecontroller.skill.Skill_Awake_Timer;

/**
 * SKILL专用时间轴 初始化启动
 * 
 * @author dexc
 * 
 */
public class StartTimer_Skill {

    public void start() {

        // 龙骑士觉醒技能MP自然减少处理
        final Skill_Awake_Timer awake_Timer = new Skill_Awake_Timer();
        awake_Timer.start();

        // 法师技能(火牢)
        final EffectFirewallTimer firewall = new EffectFirewallTimer();
        firewall.start();

        // 幻术师技能(立方：燃烧)
        final EffectCubeBurnTimer cubeBurn = new EffectCubeBurnTimer();
        cubeBurn.start();

        // 幻术师技能(立方：地裂)
        final EffectCubeEruptionTimer cubeEruption = new EffectCubeEruptionTimer();
        cubeEruption.start();

        // 幻术师技能(立方：冲击)
        final EffectCubeShockTimer cubeShock = new EffectCubeShockTimer();
        cubeShock.start();

        // 幻术师技能(立方：和谐)
        final EffectCubeHarmonizeTimer cubeHarmonize = new EffectCubeHarmonizeTimer();
        cubeHarmonize.start();

    }
}
