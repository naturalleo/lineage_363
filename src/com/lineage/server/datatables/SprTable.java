package com.lineage.server.datatables;

import static com.lineage.server.ActionCodes.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 图形影格资料
 * 
 * @author dexc
 * 
 */
public class SprTable {

    private static final Log _log = LogFactory.getLog(SprTable.class);

    private static class Spr {
        private final Map<Integer, Integer> _moveSpeed = new HashMap<Integer, Integer>();

        private final Map<Integer, Integer> _attackSpeed = new HashMap<Integer, Integer>();

        private int _nodirSpellSpeed = 0;

        private int _dirSpellSpeed = 0;

        private int _dirSpellSpeed30 = 0;

        private int _dmg = 0;
    }

    private static final Map<Integer, Spr> _dataMap = new HashMap<Integer, Spr>();

    private static SprTable _instance;

    public static SprTable get() {
        if (_instance == null) {
            _instance = new SprTable();
        }
        return _instance;
    }

    /**
     * spr_action
     */
    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Spr spr = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `spr_action`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                final int key = rs.getInt("spr_id");
                if (!_dataMap.containsKey(key)) {
                    spr = new Spr();
                    _dataMap.put(key, spr);
                } else {
                    spr = _dataMap.get(key);
                }

                final int actid = rs.getInt("act_id");
                int frameCount = rs.getInt("framecount");
                if (frameCount < 0) {
                    frameCount = 0;
                }
                int frameRate = rs.getInt("framerate");
                if (frameRate < 0) {
                    frameRate = 0;
                }
                final int speed = this.calcActionSpeed(frameCount, frameRate);

                switch (actid) {
                    case ACTION_Walk:
                    case ACTION_SwordWalk:
                    case ACTION_AxeWalk:
                    case ACTION_BowWalk:
                    case ACTION_SpearWalk:
                    case ACTION_StaffWalk:
                    case ACTION_DaggerWalk:
                    case ACTION_TwoHandSwordWalk:
                    case ACTION_EdoryuWalk:
                    case ACTION_ClawWalk:
                    case ACTION_ThrowingKnifeWalk:
                        spr._moveSpeed.put(actid, speed);
                        break;

                    case ACTION_Damage:
                        spr._dmg = speed;
                        break;

                    case ACTION_SkillAttack:
                        spr._dirSpellSpeed = speed;
                        break;

                    case ACTION_SkillBuff:
                        spr._nodirSpellSpeed = speed;
                        break;

                    case ACTION_AltAttack:
                        spr._dirSpellSpeed30 = speed;
                        break;

                    case ACTION_Attack:
                    case ACTION_SwordAttack:
                    case ACTION_AxeAttack:
                    case ACTION_BowAttack:
                    case ACTION_SpearAttack:
                    case ACTION_StaffAttack:
                    case ACTION_DaggerAttack:
                    case ACTION_TwoHandSwordAttack:
                    case ACTION_EdoryuAttack:
                    case ACTION_ClawAttack:
                    case ACTION_ThrowingKnifeAttack:
                        spr._attackSpeed.put(actid, speed);
                        break;

                    default:
                        break;
                }
            }
        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入图形影格资料数量: " + _dataMap.size() + "(" + timer.get() + "ms)");
    }

    /**
     * フレーム数とフレームレートからアクションの合计时间(ms)を计算して返す。
     */
    private int calcActionSpeed(final int frameCount, final int frameRate) {
        return (int) (frameCount * 40 * (24D / frameRate));
    }

    /**
     * 传回攻击速度
     * 
     * @param sprid
     * @param actid
     * @return 指定されたsprの攻击速度(ms)
     */
    public int getAttackSpeed(final int sprid, final int actid) {
        if (_dataMap.containsKey(sprid)) {
            if (_dataMap.get(sprid)._attackSpeed.containsKey(actid)) {
                return _dataMap.get(sprid)._attackSpeed.get(actid);

            } else if (actid == ACTION_Attack) {
                return 0;

            } else {
                return _dataMap.get(sprid)._attackSpeed.get(ACTION_Attack);
            }
        }
        return 0;
    }

    /**
     * 传回移动速度
     * 
     * @param sprid
     * @param actid
     * @return
     */
    public int getMoveSpeed(final int sprid, final int actid) {
        if (_dataMap.containsKey(sprid)) {
            if (_dataMap.get(sprid)._moveSpeed.containsKey(actid)) {
                return _dataMap.get(sprid)._moveSpeed.get(actid);

            } else if (actid == ACTION_Walk) {
                return 0;

            } else {
                return _dataMap.get(sprid)._moveSpeed.get(ACTION_Walk);
            }
        }
        return 0;
    }

    /**
     * 有方向技能速度
     * 
     * @param sprid
     * @return
     */
    public int getDirSpellSpeed(final int sprid) {
        if (_dataMap.containsKey(sprid)) {
            return _dataMap.get(sprid)._dirSpellSpeed;
        }
        return 0;
    }

    /**
     * 无方向技能速度
     * 
     * @param sprid
     * @return
     */
    public int getNodirSpellSpeed(final int sprid) {
        if (_dataMap.containsKey(sprid)) {
            return _dataMap.get(sprid)._nodirSpellSpeed;
        }
        return 0;
    }

    /**
     * NPC30动作技能速度
     * 
     * @param sprid
     * @return
     */
    public int getDirSpellSpeed30(final int sprid) {
        if (_dataMap.containsKey(sprid)) {
            return _dataMap.get(sprid)._dirSpellSpeed30;
        }
        return 0;
    }

    /**
     * 受伤动作速度
     * 
     * @param sprid
     * @return
     */
    public int getDmg(final int sprid) {
        if (_dataMap.containsKey(sprid)) {
            return _dataMap.get(sprid)._dmg;
        }
        return 0;
    }

    public long spr_move_speed(final int tempCharGfx) {
        return 200;
    }

    public long spr_attack_speed(final int tempCharGfx) {
        return 200;
    }

    public long spr_skill_speed(final int tempCharGfx) {
        return 200;
    }
}
