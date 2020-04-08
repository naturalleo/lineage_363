package com.lineage.server.model.skill;

import com.lineage.server.model.L1Character;

/**
 * 技能攻击目标状态
 * 
 * @author daien
 * 
 */
public class TargetStatus {

    private L1Character _target = null;

    private boolean _isAction = false;

    private boolean _isSendStatus = false;

    private boolean _isCalc = true;

    public TargetStatus(final L1Character cha) {
        this._target = cha;
    }

    public TargetStatus(final L1Character cha, final boolean flg) {
        this._target = cha;
        this._isCalc = flg;
    }

    /**
     * 传回目标
     * 
     * @return
     */
    public L1Character getTarget() {
        return this._target;
    }

    /**
     * 是否命中
     * 
     * @return
     */
    public boolean isCalc() {
        // System.out.println("是否命中:" + _isCalc);
        return this._isCalc;
    }

    /**
     * 设置为未命中
     * 
     * @param flg
     */
    public void isCalc(final boolean flg) {
        // System.out.println("设置为未命中:" + flg);
        this._isCalc = flg;
    }

    public void isAction(final boolean flg) {
        this._isAction = flg;
    }

    public boolean isAction() {
        return this._isAction;
    }

    public void isSendStatus(final boolean flg) {
        this._isSendStatus = flg;
    }

    public boolean isSendStatus() {
        return this._isSendStatus;
    }
}
