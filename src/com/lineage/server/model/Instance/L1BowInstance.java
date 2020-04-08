package com.lineage.server.model.Instance;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_UseArrowSkill;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.world.World;

/**
 * 固定攻击器
 * 
 * @author dexc
 * 
 */
public class L1BowInstance extends L1NpcInstance {

    private static final long serialVersionUID = 1L;

    private static final Log _log = LogFactory.getLog(L1BowInstance.class);

    private int _bowid = 66;// 射出武器图形代号

    private int _time = 1000;// 射出时间间隔

    private int _dmg = 15;// 基础伤害力

    private int _out_x = 0;// 攻击点X

    private int _out_y = 0;// 攻击点Y

    private boolean _start = true;// 执行

    public L1BowInstance(final L1Npc template) {
        super(template);
    }

    public void set_info(int bowid, int h, int dmg, int time) {
        this._bowid = bowid;
        // this._h = h;
        this._dmg = dmg;
        this._time = time;
    }

    public int get_dmg() {
        return _dmg;
    }

    public void set_dmg(int dmg) {
        this._dmg = dmg;
    }

    public int get_time() {
        return _time;
    }

    public void set_time(int time) {
        this._time = time;
    }

    public int get_bowid() {
        return _bowid;
    }

    public void set_bowid(int bowid) {
        this._bowid = bowid;
    }

    public boolean get_start() {
        return _start;
    }

    @Override
    public void onPerceive(final L1PcInstance perceivedFrom) {
        try {
            if (_out_x == 0 && _out_y == 0) {
                set_atkLoc();
            }
            if (!_start) {
                _start = true;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void deleteMe() {
        try {
            _destroyed = true;
            World.get().removeVisibleObject(this);
            World.get().removeObject(this);
            for (final L1PcInstance pc : World.get().getRecognizePlayer(this)) {
                pc.removeKnownObject(this);
                pc.sendPackets(new S_RemoveObject(this));
            }
            removeAllKnownObjects();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 攻击目标
     */
    public void atkTrag() {
        try {
            int out_x = _out_x;
            int out_y = _out_y;
            int tgid = 0;
            L1Character tg = checkTg();
            if (tg != null) {
                tgid = tg.getId();
                switch (getHeading()) {// 武器射出方向(2:→,6:←,0:↑,4:↓)
                    case 0:// 0:↑(Y-)
                        out_y = tg.getY();
                        break;
                    case 2:// 2:→(X+)
                        out_x = tg.getX();
                        break;
                    case 4:// 4:↓(Y+)
                        out_y = tg.getY();
                        break;
                    case 6:// 6:←(X-)
                        out_x = tg.getX();
                        break;
                }

                if (tg instanceof L1PcInstance) {// PC
                    L1PcInstance trag = (L1PcInstance) tg;
                    trag.receiveDamage(null, _dmg, false, true);

                } else if (tg instanceof L1PetInstance) {// 宠物
                    L1PetInstance trag = (L1PetInstance) tg;
                    trag.receiveDamage(null, _dmg);

                } else if (tg instanceof L1SummonInstance) {// 召唤兽
                    L1SummonInstance trag = (L1SummonInstance) tg;
                    trag.receiveDamage(null, _dmg);

                } else if (tg instanceof L1MonsterInstance) {// MOB
                    L1MonsterInstance trag = (L1MonsterInstance) tg;
                    trag.receiveDamage(null, _dmg);
                }
            }

            // 攻击资讯封包
            broadcastPacketAll(new S_UseArrowSkill(this, tgid, _bowid, out_x,
                    out_y, _dmg));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 周边PC物件检查
     * 
     * @return
     */
    public boolean checkPc() {
        try {
            if (World.get().getRecognizePlayer(this).size() <= 0) {
                _start = false;
                // _bowRunnable = null;
                return false;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return true;
    }

    /**
     * 目标设置
     * 
     * @return
     */
    private L1Character checkTg() {
        ArrayList<L1Object> tgs = World.get().getVisibleObjects(this, -1);
        for (L1Object object : tgs) {
            if (object instanceof L1Character) {
                L1Character cha = (L1Character) object;
                boolean isCheck = false;
                if (cha instanceof L1PcInstance) {// PC
                    isCheck = true;
                } else if (cha instanceof L1PetInstance) {// 宠物
                    isCheck = true;
                } else if (cha instanceof L1SummonInstance) {// 召唤兽
                    isCheck = true;
                } else if (cha instanceof L1MonsterInstance) {// MOB
                    isCheck = true;
                }

                if (isCheck) {
                    switch (getHeading()) {// 武器射出方向(2:→,6:←,0:↑,4:↓)
                        case 0:// 0:↑(Y-)
                            if (object.getX() == this.getX()
                                    && (object.getY() <= this.getY() && object
                                            .getY() >= _out_y)) {
                                return cha;
                            }
                            break;
                        case 2:// 2:→(X+)
                            if ((object.getX() >= this.getX() && object.getX() <= _out_x)
                                    && object.getY() == this.getY()) {
                                return cha;
                            }
                            break;
                        case 4:// 4:↓(Y+)
                            if (object.getX() == this.getX()
                                    && (object.getY() >= this.getY() && object
                                            .getY() <= _out_y)) {
                                return cha;
                            }
                            break;
                        case 6:// 6:←(X-)
                            if ((object.getX() <= this.getX() && object.getX() >= _out_x)
                                    && object.getY() == this.getY()) {
                                return cha;
                            }
                            break;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 设定攻击点
     */
    private void set_atkLoc() {
        try {
            // System.out.println("设定攻击点:"+this.getHeading());
            boolean test = true;
            int x = this.getX();
            int y = this.getY();
            switch (this.getHeading()) {// 武器射出方向(2:→,6:←,0:↑,4:↓)
                case 0:// 0:↑(Y-)
                    while (test) {
                        final int gab = getMap().getOriginalTile(x, y--);
                        if (gab == 0) {
                            test = false;
                        }
                    }
                    break;
                case 2:// 2:→(X+)
                    while (test) {
                        final int gab = getMap().getOriginalTile(x++, y);
                        if (gab == 0) {
                            test = false;
                        }
                    }
                    break;
                case 4:// 4:↓(Y+)
                    while (test) {
                        final int gab = getMap().getOriginalTile(x, y++);
                        if (gab == 0) {
                            test = false;
                        }
                    }
                    break;
                case 6:// 6:←(X-)
                    while (test) {
                        final int gab = getMap().getOriginalTile(x--, y);
                        if (gab == 0) {
                            test = false;
                        }
                    }
                    break;
            }
            if (!test) {
                _out_x = x;
                _out_y = y;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
