package com.lineage.server.model.monitor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.world.World;

/**
 * L1PcInstanceの定期处理、监视处理等を行う为の共通的な处理を实装した抽象クラス
 * 
 * 各タスク处理は{@link #run()}ではなく{@link #execTask(L1PcInstance)}にて实装する。
 * PCがログアウトするなどしてサーバ上に存在しなくなった场合、run()メソッドでは即座にリターンする。
 * その场合、タスクが定期实行スケジューリングされていたら、ログアウト处理等でスケジューリングを停止する必要がある。
 * 停止しなければタスクは止まらず、永远に定期实行されることになる。 定期实行でなく单発アクションの场合はそのような制御は不要。
 * 
 * L1PcInstanceの参照を直接持つことは望ましくない。
 * 
 * @author frefre
 * 
 */
public abstract class L1PcMonitor implements Runnable {

    /** モニター对象L1PcInstanceのオブジェクトID */
    protected int _id;

    /**
     * 指定されたパラメータでL1PcInstanceに对するモニターを作成する。
     * 
     * @param oId
     *            {@link L1PcInstance#getId()}で取得できるオブジェクトID
     */
    public L1PcMonitor(final int oId) {
        this._id = oId;
    }

    @Override
    public final void run() {
        final L1PcInstance pc = (L1PcInstance) World.get().findObject(this._id);
        if ((pc == null) || (pc.getNetConnection() == null)) {
            return;
        }
        this.execTask(pc);
    }

    /**
     * タスク实行时の处理
     * 
     * @param pc
     *            モニター对象のPC
     */
    public abstract void execTask(L1PcInstance pc);
}
