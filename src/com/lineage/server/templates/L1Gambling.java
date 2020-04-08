package com.lineage.server.templates;

/**
 * 妖精竞赛纪录缓存
 * 
 * @author dexc
 * 
 */
public class L1Gambling {

    private int _id;

    private long _adena;

    private double _rate;

    private String _gamblingno;

    private int _outcount;

    /**
     * 场次编号
     * 
     * @return the _id
     */
    public int get_id() {
        return this._id;
    }

    /**
     * 场次编号
     * 
     * @param id
     *            the _id to set
     */
    public void set_id(final int id) {
        this._id = id;
    }

    /**
     * 本场总下注金额
     * 
     * @return the _adena
     */
    public long get_adena() {
        return this._adena;
    }

    /**
     * 本场总下注金额
     * 
     * @param adena
     *            the _adena to set
     */
    public void set_adena(final long adena) {
        this._adena = adena;
    }

    /**
     * 获胜NPC赔率
     * 
     * @return the _rate
     */
    public double get_rate() {
        return this._rate;
    }

    /**
     * 获胜NPC赔率
     * 
     * @param rate
     *            the _rate to set
     */
    public void set_rate(final double rate) {
        this._rate = rate;
    }

    /**
     * 场次编号-获胜NPCID
     * 
     * @return the _gamblingno
     */
    public String get_gamblingno() {
        return this._gamblingno;
    }

    /**
     * 场次编号-获胜NPCID
     * 
     * @param gamblingno
     *            the _gamblingno to set
     */
    public void set_gamblingno(final String gamblingno) {
        this._gamblingno = gamblingno;
    }

    /**
     * 获胜下注数量
     * 
     * @return the _outcount
     */
    public int get_outcount() {
        return this._outcount;
    }

    /**
     * 获胜下注数量
     * 
     * @param outcount
     *            the _outcount to set
     */
    public void set_outcount(final int outcount) {
        this._outcount = outcount;
    }

}
