package com.lineage.server.clientpackets;

import java.util.Collection;
import java.util.EnumMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigOther;
import com.lineage.server.datatables.SprTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_Disconnect;
import com.lineage.server.serverpackets.S_Lock;
import com.lineage.server.serverpackets.S_ToGmMessage;
import com.lineage.server.world.World;

/**
 * 加速器检测
 */
public class AcceleratorChecker {

    private static final Log _log = LogFactory.getLog(AcceleratorChecker.class);

    private final L1PcInstance _pc;

    private int _injusticeCount;

    private int _justiceCount;

    private static final int INJUSTICE_COUNT_LIMIT = 8;// 允许错误次数 原为10改为 8 hjx1000

    private static final int JUSTICE_COUNT_LIMIT = 4;// 必须正常次数

    // 加大的允许范围质
    public static double CHECK_STRICTNESS = ConfigOther.SPEED_TIME;

    private final EnumMap<ACT_TYPE, Long> _actTimers = new EnumMap<ACT_TYPE, Long>(
            ACT_TYPE.class);

    private final EnumMap<ACT_TYPE, Long> _checkTimers = new EnumMap<ACT_TYPE, Long>(
            ACT_TYPE.class);

    public static enum ACT_TYPE {
        MOVE, ATTACK, SPELL_DIR, SPELL_NODIR
    }

    public static final int R_OK = 0;// 正常

    public static final int R_DETECTED = 1;// 异常

    public static final int R_DISCONNECTED = 2;// 连续异常

    public AcceleratorChecker(final L1PcInstance pc) {
        this._pc = pc;
        this._injusticeCount = 0;
        this._justiceCount = 0;
        final long now = System.currentTimeMillis();
        for (final ACT_TYPE each : ACT_TYPE.values()) {
            this._actTimers.put(each, now);
            this._checkTimers.put(each, now);
        }
    }

    /**
     * 断开用户改为回弹 hjx1000
     */
    private void doDisconnect(ACT_TYPE type) {
        try {
//            final StringBuilder name = new StringBuilder();
//            name.append(this._pc.getName());
//            // 945：发现外挂程式因此强制中断游戏
//            this._pc.sendPackets(new S_Disconnect());
//
//            this._pc.getNetConnection().kick();// 中断
//            toGmKickMsg(name.toString());
        	//add hjx1000
        	_pc.setSkillEffect(L1SkillId.attack_fanlse, 8000);//给予一个状态为攻击无效作判断 hjx1000
        	switch (type) {
        	    case MOVE:
                    this._pc.sendPackets(new S_Lock(_pc));//回弹 hjx1000
                    break;
        	}
        	Thread.sleep(500);
        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
    
    /**
     * 断开用户
     */
    private void prodoDisconnect() {
        try {
          final StringBuilder name = new StringBuilder();
          name.append(this._pc.getName());
          // 945：发现外挂程式因此强制中断游戏
          this._pc.sendPackets(new S_Disconnect());

          this._pc.getNetConnection().kick();// 中断
          toGmKickMsg(name.toString());

      } catch (final Exception e) {
          _log.error(e.getLocalizedMessage(), e);
      }
    }

    /**
     * 通知GM
     */
    private void toGmErrMsg(final String name, int count) {
        try {
            if (count >= INJUSTICE_COUNT_LIMIT) { //hjx1000
                final Collection<L1PcInstance> allPc = World.get()
                        .getAllPlayers();
                for (L1PcInstance tgpc : allPc) {
                    if (tgpc.isGm()) {
                        tgpc.sendPackets(new S_ToGmMessage("人物:" + name
                                + " 速度异常!(" + count + "次)"));
                    }
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 通知GM
     */
    private void toGmKickMsg(final String name) {
        try {
            final Collection<L1PcInstance> allPc = World.get().getAllPlayers();
            for (L1PcInstance tgpc : allPc) {
                if (tgpc.isGm()) {
                    tgpc.sendPackets(new S_ToGmMessage("人物:" + name
                            + " 速度异常断线!"));
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 封包速度正常与否的检测
     * 
     * @param type
     *            检测类型
     * @return 0:正常 1:异常 2:连续异常
     */
    public int checkInterval(final ACT_TYPE type) {
        if (!ConfigOther.SPEED) {
            return R_OK;
        }
        int result = R_OK;
        try {
            final long now = System.currentTimeMillis();
            long interval = now - this._actTimers.get(type);
            final int rightInterval = this.getRightInterval(type);

            interval *= CHECK_STRICTNESS;

            if ((0 < interval) && (interval < rightInterval)) {
                this._injusticeCount++;
                toGmErrMsg(this._pc.getName(), this._injusticeCount);
                this._justiceCount = 0;
                //add防加速惩罚加速多次冻住 by hjx1000 end ==
                if (this._injusticeCount >= INJUSTICE_COUNT_LIMIT + 10) {// 此处修改为多次才进行处罚 hjx1000
                    this.prodoDisconnect();
                    return R_DISCONNECTED;
                }
                //add防加速惩罚由断线改为弹回 以及攻击无效 hjx1000
                if (this._injusticeCount >= INJUSTICE_COUNT_LIMIT) {// 允许错误次数
                    this.doDisconnect(type);
                    return R_DISCONNECTED;
                }
                result = R_DETECTED;

            } else if (interval >= rightInterval) {
                this._justiceCount++;
                if (this._justiceCount >= JUSTICE_COUNT_LIMIT) {// 连续正常 恢复计算
                    this._injusticeCount = 0;
                    this._justiceCount = 0;
                }
            }

            this._actTimers.put(type, now);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    /**
     * 正常的速度
     * 
     * @param type
     *            检测类型
     * @return 正常应该接收的速度(MS)
     */
    private int getRightInterval(final ACT_TYPE type) {
        int interval = 0;

        switch (type) {
            case ATTACK:
                interval = SprTable.get().getAttackSpeed(
                        this._pc.getTempCharGfx(),
                        this._pc.getCurrentWeapon() + 1);
                interval *= 1.05;
                break;

            case MOVE:
                interval = SprTable.get().getMoveSpeed(
                        this._pc.getTempCharGfx(), this._pc.getCurrentWeapon());
                break;

            default:
                return 0;
        }
        return intervalR(type, interval);
    }

    private int intervalR(final ACT_TYPE type, int interval) {
        try {
//        	if(this._pc.isskillHardDelay()) { //添加动作延时防止变档修改加速 hjx1000
//        		return interval * 2;
//        	}
//        	if (type.equals(ACT_TYPE.MOVE)) {
//            	if(this._pc.isHardDelay()) { //添加动作延时防止变档修改加速 hjx1000
//            		return interval * 2;
//            	}
//        	}
            if (this._pc.isHaste()) {
                interval *= 0.755;// 0.755
            }

            if (type.equals(ACT_TYPE.MOVE) && this._pc.isFastMovable()) {
                interval *= 0.755;// 0.665
            }

            if (type.equals(ACT_TYPE.MOVE) && this._pc.isFastAttackable()) {
                interval *= 0.665;// 0.775
            }

            if (this._pc.isBrave()) {
                interval *= 0.755;// 0.755
            }

            if (this._pc.isBraveX()) {
                interval *= 0.755;// 0.755
            }

            if (this._pc.isElfBrave()) {
                interval *= 0.855;// 0.855
            }

            if (type.equals(ACT_TYPE.ATTACK) && this._pc.isElfBrave()) {
                interval *= 0.9;// 0.9
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return interval;
    }
}
