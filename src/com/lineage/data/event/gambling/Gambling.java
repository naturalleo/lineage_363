package com.lineage.data.event.gambling;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 比赛控制数据
 * 
 * @author dexc
 * 
 */
public class Gambling extends GamblingConfig {

    // 冠军参赛者
    private GamblingNpc _onenpc;

    // private int _no;

    private long _adena;

    private long _upadena;

    public int WIN;

    private final Map<Integer, GamblingNpc> _npcidMap = new HashMap<Integer, GamblingNpc>();

    private Random _random = new Random();

    /*
     * public Gambling(int no) { _no = no; }
     */

    /**
     * 清除纪录
     */
    public void clear() {
        _npcidMap.clear();
        this._onenpc = null;
        this._adena = 0;
    }

    /**
     * 传回本场编号
     * 
     * @return
     */
    /*
     * public int no() { return _no; }
     */

    /**
     * 传回本场总下注金额
     * 
     * @return
     */
    public long get_allAdena() {
        return this._adena;
    }

    /**
     * 建立参赛NPC
     * 
     * @param previous
     */
    public void set_gmaNpc(final long previous) {
        if (GamblingConfig.ISGFX) {// 赛狗外型
            GamblingConfig.ISGFX = false;

        } else {// 食人妖宝宝外型
            GamblingConfig.ISGFX = true;
        }

        WIN = _random.nextInt(5);

        int i = 0;
        while (_npcidMap.size() < 5) {
            final int npcid = NPCID[_random.nextInt(NPCID.length)];
            if (_npcidMap.get(new Integer(npcid)) == null) {
                final GamblingNpc gamnpc = new GamblingNpc(this);
                gamnpc.showNpc(npcid, i++);
                _npcidMap.put(new Integer(npcid), gamnpc);
            }
        }
        // 加入累积金
        this._upadena = previous;
    }

    /**
     * 目前下注金额
     * 
     * @return
     */
    public long get_allRate() {
        long adena = 0;
        for (final GamblingNpc gamblingNpc : _npcidMap.values()) {
            adena += gamblingNpc.get_adena();
        }
        return adena + this._upadena;
    }

    /**
     * 计算赔率
     */
    public void set_allRate() {
        long adena = this._upadena;
        // 计算全部NPC累积押注
        for (final GamblingNpc gamblingNpc : _npcidMap.values()) {
            adena += gamblingNpc.get_adena();
        }

        this._adena = adena;
        // System.out.println("总收入: "+_adena);
        /*
         * adena *= GamblingSet.ODDS; //System.out.println("预计陪出: "+adena);
         * 
         * if (adena <= 0) { adena = _random.nextInt(20000000) + 9500000; } for
         * (final GamblingNpc gamblingNpc : _npcidMap.values()) {
         * this.set_npcRate(gamblingNpc, adena); }
         */
    }

    /**
     * 计算每个NPC该获取的赔率
     * 
     * @param npc
     * @param adena
     */
    /*
     * private void set_npcRate(final GamblingNpc npc, final long adena) { //
     * 每注金额 final int gamadena = GamblingSet.GAMADENA; // 卖出张数 double count =
     * npc.get_adena() / gamadena; if (count == 0.0) { count =
     * _random.nextInt(30) + 5; }
     * 
     * //System.out.println("卖出张数: "+count); // 该NPC获胜每注可领取金额 final double ex =
     * adena / count; //System.out.println("该NPC获胜每注可领取金额: "+ex); // 赔率 double
     * rate = ex / gamadena;
     * 
     * // 设置最高赔率限制条件 if (rate > 150D) { double ratex = rate - 150.0; if (ratex >
     * 100D) { ratex = 100D; } if (ratex <= 1D) { ratex = 1D; } final double x =
     * _random.nextInt((int) ratex);
     * 
     * rate = 150D + x; } //System.out.println("赔率: "+rate); npc.set_rate(rate);
     * }
     */

    /**
     * 传回全部参赛者
     * 
     * @return
     */
    public Map<Integer, GamblingNpc> get_allNpc() {
        return _npcidMap;
    }

    /**
     * 传回冠军
     * 
     * @return
     */
    public GamblingNpc get_oneNpc() {
        return this._onenpc;
    }

    /**
     * 设置冠军
     * 
     * @param npc
     */
    public void set_oneNpc(final GamblingNpc onenpc) {
        this._onenpc = onenpc;
    }

    /**
     * 启动线程
     */
    public void startGam() {
        long minadena [] = {0,0,0,0,0};
        for (final GamblingNpc gamblingNpc : _npcidMap.values()) {
            gamblingNpc.getStart();
            minadena [gamblingNpc.get_xId()] += gamblingNpc.get_adena();
        }
        long min = minadena[0];
        int index = 0;
        for (int i = 1; i < minadena.length; i++) { 
            if(min > minadena[i]){   
                min = minadena[i];   
                index = i;  
            }
        }
        System.out.println("原来的WIN==:" + WIN);
        if (get_allRate() > 0 && _random.nextInt(100) + 1 > 70) {
            WIN = index;
            System.out.println("必输==:" + WIN);
        }
    }

    /**
     * 删除参赛者
     */
    public void delAllNpc() {
        for (final GamblingNpc gamblingNpc : _npcidMap.values()) {
            gamblingNpc.delNpc();
        }
    }
}
