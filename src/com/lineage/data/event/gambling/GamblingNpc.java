package com.lineage.data.event.gambling;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.serverpackets.S_MoveCharPacket;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.types.Point;
import com.lineage.server.utils.L1SpawnUtil;

/**
 * 参赛者移动控制
 * 
 * @author dexc
 * 
 */
public class GamblingNpc implements Runnable {

    private static final Log _log = LogFactory.getLog(GamblingNpc.class);

    // 比赛控制数据
    private Gambling _gambling;

    // 参赛者
    private L1NpcInstance _npc;

    // 比赛结速
    private boolean _isOver = false;

    private Point[] _tgLoc;

    // 跑道
    private int _xId;

    // 标点进度
    private int _sId = 1;

    // 移动格数
    // private int _move = 0;

    // 押注累积金额
    private long _adena;

    // 赔率
    private double _rate;

    private Point _loc;

    private Random _random = new Random();

    private int _randomTime;

    public GamblingNpc(final Gambling gambling) {
        _gambling = gambling;
    }

    /**
     * 传回比赛控制数据
     */
    public Gambling get_gambling() {
        return _gambling;
    }

    /**
     * 设置该NPC赔率
     */
    public void set_rate(final double rate) {
        _rate = rate;
    }

    /**
     * 传回该NPC赔率
     * 
     * @return
     */
    public double get_rate() {
        return _rate;
    }

    /**
     * 设置该NPC累积押注金额
     * 
     * @param npc
     */
    public void add_adena(final long adena) {
        _adena += adena;
    }

    /**
     * 传回该NPC押注累积金额
     * 
     * @param npc
     */
    public long get_adena() {
        return _adena;
    }

    /**
     * 删除NPC
     * 
     * @param npc
     */
    public void delNpc() {
        _npc.deleteMe();
    }

    /**
     * 传回NPC
     * 
     * @param npc
     * @return
     */
    public L1NpcInstance get_npc() {
        return _npc;
    }

    /**
     * 传回NPC跑道
     * 
     * @param npc
     * @return
     */
    public int get_xId() {
        return _xId;
    }

    /**
     * 产生NPC
     * 
     * @param npcid
     * @param i
     */
    public void showNpc(final int npcid, final int i) {
        _xId = i;

        final L1Npc npc = NpcTable.get().getTemplate(npcid);
        if (npc != null) {
            _tgLoc = GamblingConfig.TGLOC[i];
            final int x = _tgLoc[0].getX();
            final int y = _tgLoc[0].getY();
            final short mapid = 4;
            final int heading = 6;

            int[] gfxids;
            if (GamblingConfig.ISGFX) {// 赛狗外型
                gfxids = GamblingConfig.GFX[i];

            } else {// 食人妖宝宝外型
                gfxids = GamblingConfig.GFXD[i];
            }

            final int gfxid = gfxids[_random.nextInt(gfxids.length)];

            _npc = L1SpawnUtil.spawn(npcid, x, y, mapid, heading, gfxid);
            // System.out.println("比赛NPC编号: "+npcid + " 排序:" + i);
        }
    }

    /**
     * 启动
     */
    public void getStart() {
        GeneralThreadPool.get().schedule(this, 10);
    }

    @Override
    public void run() {
        try {
            _loc = _tgLoc[1];
            while (!_isOver) {
                if (_xId == _gambling.WIN) {
                    _randomTime = 150;
                } else {
                    _randomTime = 190;
                }               

                int ss = 190;
                if (_random.nextInt(100) < 25) {
                    ss = 150;
                }
                final int speed = ss + _random.nextInt(_randomTime);

                Thread.sleep(speed); // 休息时间?
                this.actionStart();
            }

        } catch (final InterruptedException e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private static final byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };

    private static final byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

    /**
     * 往指定面向移动1格(无障碍设置)
     */
    private void setDirectionMove(final int heading) {
        // System.out.println("往指定面向移动1格: "+_npc.getNpcId() + " 面向:" + dir);
        if (heading >= 0) {
            int locx = _npc.getX();
            int locy = _npc.getY();
            locx += HEADING_TABLE_X[heading];
            locy += HEADING_TABLE_Y[heading];
            _npc.setHeading(heading);

            _npc.setX(locx);
            _npc.setY(locy);
            _npc.broadcastPacketAll(new S_MoveCharPacket(_npc));
            // this._move++;
        }
    }

    private static final int[][] xx = new int[5][1];

    private void actionStart() {
        final int x = _loc.getX();
        final int y = _loc.getY();
        try {
            // 取回行进方向
            final int dir = _npc.targetDirection(x, y);
            this.setDirectionMove(dir);
            if (_npc.getLocation().getTileLineDistance(_loc) < 2) {
                _loc = _tgLoc[_sId];
                _sId++;
            }

        } catch (final Exception e) {
            if (_gambling.get_oneNpc() == null) {
                _gambling.set_oneNpc(this);

                xx[_xId][0] += 1;
                // System.out.println("获胜跑道: "+_xId + "(" + xx[_xId][0] + ")" +
                // GamblingTimeController.get_gamblingNo()+ "场");
            }

            // 取回行进方向
            final int dir = _npc.targetDirection(x, y);
            setDirectionMove(dir);
            // System.out.println("移动停止: "+_npc.getNpcId());
            _isOver = true;
        }
    }
}
