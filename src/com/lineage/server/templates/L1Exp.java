package com.lineage.server.templates;

public class L1Exp {

    private int _level;

    private long _exp;

    private double _expPenalty;

    /**
     * @return 传出 _level
     */
    public int get_level() {
        return this._level;
    }

    /**
     * @param level
     *            对 _level 进行设置
     */
    public void set_level(final int level) {
        this._level = level;
    }

    /**
     * @return 传出 _exp
     */
    public long get_exp() {
        return this._exp;
    }

    /**
     * @param exp
     *            对 _exp 进行设置
     */
    public void set_exp(final long exp) {
        this._exp = exp;
    }

    /**
     * @return 传出 _expPenalty
     */
    public double get_expPenalty() {
        return this._expPenalty;
    }

    /**
     * @param penalty
     *            对 _expPenalty 进行设置
     */
    public void set_expPenalty(final double penalty) {
        this._expPenalty = penalty;
    }

}
