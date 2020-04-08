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
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_UseArrowSkill;
import com.lineage.server.timecontroller.pc.HardDelay;
import com.lineage.server.utils.CheckUtil;
import com.lineage.server.world.World;

/**
 * 要求角色攻击(远距离)
 * 
 * @author dexc
 * 
 */
public class C_AttackBow extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_AttackBow.class);

    /*
     * public C_AttackBow() { }
     * 
     * public C_AttackBow(final byte[] abyte0, final ClientExecutor client) {
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
                return;
            }

            if (pc.isDead()) { // 死亡
                return;
            }

            if (pc.isTeleport()) { // 传送中
                return;
            }

            if (pc.isPrivateShop()) { // 商店村模式
                return;
            }

            if (pc.getInventory().getWeight240() >= 197) { // 重量过重
                // 110 \f1当负重过重的时候，无法战斗。
                pc.sendPackets(new S_ServerMessage(110));
                return;
            }
            
            if (pc.isActived()) { //PC挂机中无法攻击。。 hjx1000
            	return;
            }
            
//            if (!pc.hasSkillEffect(Card_Fee)) { //收费限制 hjx1000
//            	pc.sendPackets(new S_SystemMessage("点卡不足而无法攻击，请您及时冲值点卡。"));
//            	return;
//            }
            if (!pc.isHardDelay()) { //动作延时 hjx1000
            	HardDelay.onHardUse(pc, 400);
            }

            final int result = pc.speed_Attack().checkInterval(
                    AcceleratorChecker.ACT_TYPE.ATTACK);
            if (result == AcceleratorChecker.R_DISCONNECTED) {
                _log.error("要求角色攻击:速度异常(" + pc.getName() + ")");
            }// */

            if (pc.isInvisble()) { // 隐身状态
                return;
            }

            if (pc.isInvisDelay()) { // 隐身延迟
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
            int targetX = 0;
            int targetY = 0;

            try {
                // 攻击点资讯
                targetId = this.readD();
                locx = this.readH();
                locy = this.readH();

            } catch (final Exception e) {
                return;
            }

            if (locx == 0) {
                return;
            }
            if (locy == 0) {
                return;
            }

            targetX = locx;
            targetY = locy;

            final L1Object target = World.get().findObject(targetId);

            if (target instanceof L1Character) {
                if (target.getMapId() != pc.getMapId()) { // 攻击位置异常
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
                // 设置面向
                pc.setHeading(pc.targetDirection(locx, locy));
                // 取回使用武器
                final L1ItemInstance weapon = pc.getWeapon();

                if (weapon != null) {
                    // 武器类型
                    final int weaponType = weapon.getItem().getType1();

                    switch (weaponType) {
                        case 20:// 弓
                            final L1ItemInstance arrow = pc.getInventory()
                                    .getArrow();
                            if (arrow != null) { // 具有箭
                                this.arrowAction(pc, arrow, 66, targetX,
                                        targetY);

                            } else {
                                if (weapon.getName().equals("$1821")) {// 沙哈之弓
                                    this.arrowAction(pc, null, 2349, targetX,
                                            targetY);

                                } else {
                                    this.nullAction(pc);
                                }
                            }
                            break;

                        case 62:// 铁手甲
                            final L1ItemInstance sting = pc.getInventory()
                                    .getSting();
                            if (sting != null) { // 具有飞刀
                                this.arrowAction(pc, sting, 2989, targetX,
                                        targetY);

                            } else {
                                this.nullAction(pc);
                            }
                            break;
                    }
                }
            }

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    /**
     * 空攻击(具有消耗道具)
     * 
     * @param pc
     *            执行者
     * @param item
     *            消耗道具
     * @param gfx
     *            动画
     * @param targetX
     *            目标X
     * @param targetY目标Y
     */
    private void arrowAction(L1PcInstance pc, L1ItemInstance item, int gfx,
            int targetX, int targetY) {
        pc.sendPacketsAll(new S_UseArrowSkill(pc, gfx, targetX, targetY));

        if (item != null) {
            pc.getInventory().removeItem(item, 1);
        }
    }

    /**
     * 空攻击(无消耗道具)
     * 
     * @param pc
     *            执行者
     */
    private void nullAction(L1PcInstance pc) {
        int aid = 1;
        // 外型编号改变动作
        if (pc.getTempCharGfx() == 3860) {
            aid = 21;
        }

        pc.sendPacketsAll(new S_ChangeHeading(pc));
        // 送出封包(动作)
        pc.sendPacketsAll(new S_DoActionGFX(pc.getId(), aid));
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
