package com.lineage.server.clientpackets;

import static com.lineage.server.model.Instance.L1PcInstance.REGENSTATE_ATTACK;
import static com.lineage.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static com.lineage.server.model.skill.L1SkillId.MEDITATION;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_AttackPacketPc;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.timecontroller.pc.HardDelay;
import com.lineage.server.utils.CheckUtil;
import com.lineage.server.world.World;

/**
 * 要求角色攻击
 * 
 * @author dexc
 * 
 */
public class C_Attack extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_Attack.class);

    /*
     * public C_Attack() { }
     * 
     * public C_Attack(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final L1PcInstance pc = client.getActiveChar();

            pc.isFoeSlayer(false);

            if (pc.isGhost()) { // 鬼魂模式
                // _log.error("要求角色攻击:鬼魂模式");
                return;
            }
            if (pc.isDead()) { // 死亡
                // _log.error("要求角色攻击:死亡");
                return;
            }
            if (pc.isTeleport()) { // 传送中
                // _log.error("要求角色攻击:传送中");
                return;
            }

            if (pc.isPrivateShop()) { // 商店村模式
                // _log.error("要求角色攻击:商店村模式");
                return;
            }

            if (pc.getInventory().getWeight240() >= 197) { // 重量过重
                // 110 \f1当负重过重的时候，无法战斗。
                pc.sendPackets(new S_ServerMessage(110));
                // _log.error("要求角色攻击:重量过重");
                return;
            }
            
            if (pc.isActived()) { //PC挂机中无法攻击。。 hjx1000
            	return;
            }
            
//            if (!pc.hasSkillEffect(Card_Fee)) { //收费限制 hjx1000
//            	pc.sendPackets(new S_SystemMessage("点卡不足而无法攻击，请您及时冲值点卡。"));
//            	return;
//            }

            final int result = pc.speed_Attack().checkInterval(
                    AcceleratorChecker.ACT_TYPE.ATTACK);
            if (result == AcceleratorChecker.R_DISCONNECTED) {
                _log.error("要求角色攻击:速度异常(" + pc.getName() + ")");
            }// */

            if (pc.isInvisble()) { // 隐身状态
                // _log.error("要求角色攻击:隐身状态");
                return;
            }

            if (pc.isInvisDelay()) { // 隐身延迟
                // _log.error("要求角色攻击:隐身延迟");
                return;
            }

            // 无法攻击/使用道具/技能/回城的状态
            if (pc.isParalyzedX()) {
                return;
            }

            if (pc.get_weaknss() != 0) {
                long h_time = Calendar.getInstance().getTimeInMillis() / 1000;// 换算为秒
                if (h_time - pc.get_weaknss_t() > 16) {
                    pc.set_weaknss(0, 0);
                }
            }

            int targetId = 0;
            int locx = 0;
            int locy = 0;

            try {
                // 攻击点资讯
                targetId = this.readD();
                locx = this.readH();
                locy = this.readH();
                // _log.error("要求角色攻击:攻击点资讯取回");

            } catch (final Exception e) {
                // _log.error("要求角色攻击:攻击点资讯异常");
                return;
            }

            if (locx == 0) {
                // _log.error("要求角色攻击:locx == 0");
                return;
            }
            if (locy == 0) {
                // _log.error("要求角色攻击:locy == 0");
                return;
            }

            final L1Object target = World.get().findObject(targetId);

            if (target instanceof L1Character) {
                if (target.getMapId() != pc.getMapId()) { // 攻击位置异常
                    // _log.error("要求角色攻击:攻击位置异常target.getMapId() != pc.getMapId()");
                    return;
                }
            }

            // 检查地图使用权
            CheckUtil.isUserMap(pc);

            if (target instanceof L1NpcInstance) {
                final int hiddenStatus = ((L1NpcInstance) target)
                        .getHiddenStatus();
                if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_SINK) { // 躲藏
                    return;
                }
                if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_FLY) { // 空中
                    return;
                }
            }

            // 绝对屏障解除
            if (pc.hasSkillEffect(ABSOLUTE_BARRIER)) {
                pc.killSkillEffectTimer(ABSOLUTE_BARRIER);
                pc.startHpRegeneration();
                pc.startMpRegeneration();
            }

            pc.killSkillEffectTimer(MEDITATION);

            pc.delInvis(); // 透明状态の解除

            pc.setRegenState(REGENSTATE_ATTACK);

            if ((target != null) && !((L1Character) target).isDead()) {
                if (target instanceof L1PcInstance) {
                    L1PcInstance tg = (L1PcInstance) target;
                    pc.setNowTarget(tg);
                }
                target.onAction(pc);

            } else { // 空攻击
                pc.setHeading(pc.targetDirection(locx, locy));
                pc.sendPacketsAll(new S_AttackPacketPc(pc));
            }
            if (!pc.isHardDelay()) { //动作延时 hjx1000
            	HardDelay.onHardUse(pc, 400);
            }
        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
