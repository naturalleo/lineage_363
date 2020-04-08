package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.SHOCK_STUN;

import java.util.Random;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1GuardInstance;
import com.lineage.server.model.Instance.L1GuardianInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.utils.L1SpawnUtil;

/**
 * 冲击之晕
 * 
 * @author dexc
 * 
 */
public class SHOCK_STUN extends SkillMode {

    public SHOCK_STUN() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = magic.calcMagicDamage(SHOCK_STUN);
		final Random random = new Random();
        final int[] stunTimeArray = { 1000, 2000, 3000, 4000, 5000 };
        int lv = 0;
        if (srcpc.getLevel() < cha.getLevel()) {
        	lv = Math.min(3, cha.getLevel() - srcpc.getLevel());
        }
        final int rnd = random.nextInt(stunTimeArray.length - lv);//修改冲晕时间 hjx1000
        final int shock = stunTimeArray[rnd];

		// 取回目標是否已被施展衝暈
		if (cha.hasSkillEffect(SHOCK_STUN)) {
			//shock += cha.getSkillEffectTimeSec(SHOCK_STUN);// 累計時間
			return dmg; //修改冲晕不累计时间 hjx1000
		}		

//		if (shock > 6000) {// 最大衝暈時間6秒
//			shock = 6000;
//		}
		
		cha.setSkillEffect(SHOCK_STUN, shock);
		// 騎士技能(衝擊之暈)
		L1SpawnUtil.spawnEffect(81162, shock, cha.getX(), cha.getY(), cha.getMapId(), cha, 0);

        if (cha instanceof L1PcInstance) {
            final L1PcInstance pc = (L1PcInstance) cha;
            pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));

        } else if ((cha instanceof L1MonsterInstance)
                || (cha instanceof L1SummonInstance)
                || (cha instanceof L1PetInstance)) {
            final L1NpcInstance tgnpc = (L1NpcInstance) cha;
            tgnpc.setParalyzed(true);
        }

        return dmg;
    }

    @Override
    public int start(final L1NpcInstance npc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = magic.calcMagicDamage(SHOCK_STUN);
		final Random random = new Random();
        final int[] stunTimeArray = { 1000, 2000, 3000, 4000, 5000, 6000 };
        final int rnd = random.nextInt(stunTimeArray.length);//修改冲晕时间 hjx1000
        int shock = stunTimeArray[rnd];
		
		// 取回目標是否已被施展衝暈
		if (/*(cha instanceof L1PcInstance) &&*/ cha.hasSkillEffect(SHOCK_STUN)) {
			//shock += cha.getSkillEffectTimeSec(SHOCK_STUN);// 累計時間
			return dmg; //修改冲晕不累计时间 hjx1000
		}

//		if (shock > 6000) {// 最大衝暈時間6秒
//			shock = 6000;
//		}
		
		cha.setSkillEffect(SHOCK_STUN, shock);
		// 騎士技能(衝擊之暈)
		L1SpawnUtil.spawnEffect(81162, shock, cha.getX(), cha.getY(), cha.getMapId(), cha, 0);

        if (cha instanceof L1PcInstance) {
            final L1PcInstance pc = (L1PcInstance) cha;
            pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));

        } else if ((cha instanceof L1MonsterInstance)
                || (cha instanceof L1SummonInstance)
                || (cha instanceof L1GuardianInstance)
                || (cha instanceof L1GuardInstance)
                || (cha instanceof L1PetInstance)) {
            final L1NpcInstance tgnpc = (L1NpcInstance) cha;
            tgnpc.setParalyzed(true);
        }

        return dmg;
    }

    @Override
    public void start(final L1PcInstance srcpc, final Object obj)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void stop(final L1Character cha) throws Exception {
        if (cha instanceof L1PcInstance) {
            final L1PcInstance pc = (L1PcInstance) cha;
            pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, false));

        } else if ((cha instanceof L1MonsterInstance)
                || (cha instanceof L1SummonInstance)
                || (cha instanceof L1GuardianInstance)
                || (cha instanceof L1GuardInstance)
                || (cha instanceof L1PetInstance)) {
            final L1NpcInstance npc = (L1NpcInstance) cha;
            npc.setParalyzed(false);
        }
    }
}
