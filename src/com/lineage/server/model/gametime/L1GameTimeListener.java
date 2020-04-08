package com.lineage.server.model.gametime;

/**
 * <p>
 * アデン时间の变化を受け取るためのリスナーインターフェース。
 * </p>
 * <p>
 * アデン时间の变化を监视すべきクラスは、このインターフェースに含まれているすべてのメソッドを定义してこのインターフェースを实装するか、
 * 关连するメソッドだけをオーバーライドしてabstractクラスL1GameTimeAdapterを扩张する。
 * </p>
 * <p>
 * そのようなクラスから作成されたリスナーオブジェクトは、
 * L1GameTimeClockのaddListenerメソッドを使用してL1GameTimeClockに登录される。
 * アデン时间变化の通知は、月日时分がそれぞれ变わったときに行われる。
 * </p>
 * <p>
 * これらのメソッドは、L1GameTimeClockのスレッド上で动作する。
 * これらのメソッドの处理に时间がかかった场合、他のリスナーへの通知が迟れる可能性がある。
 * 完了までに时间を要する处理や、スレッドをブロックするメソッドの呼び出しが含まれる处理を行う场合は、内部で新たにスレッドを作成して处理を行うべきである。
 * </p>
 * 
 */
public interface L1GameTimeListener {

    /**
     * アデン时间で月が变わったときに呼び出される。
     * 
     * @param time
     *            最新のアデン时间
     */
    public void onMonthChanged(L1GameTime time);

    /**
     * アデン时间で日が变わったときに呼び出される。
     * 
     * @param time
     *            最新のアデン时间
     */
    public void onDayChanged(L1GameTime time);

    /**
     * アデン时间で时间が变わったときに呼び出される。
     * 
     * @param time
     *            最新のアデン时间
     */
    public void onHourChanged(L1GameTime time);

    /**
     * アデン时间で分が变わったときに呼び出される。
     * 
     * @param time
     *            最新のアデン时间
     */
    public void onMinuteChanged(L1GameTime time);
}
