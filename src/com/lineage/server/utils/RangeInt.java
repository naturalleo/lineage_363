package com.lineage.server.utils;

import java.util.Random;

/**
 * <p>
 * 最低值lowと最大值highによって围まれた、数值の范围を指定するクラス。
 * </p>
 * <p>
 * <b>このクラスは同期化されない。</b> 复数のスレッドが同时にこのクラスのインスタンスにアクセスし、
 * 1つ以上のスレッドが范围を变更する场合、外部的な同期化が必要である。
 * </p>
 */
public class RangeInt {

    private static final Random _rnd = new Random();

    private int _low;

    private int _high;

    public RangeInt(final int low, final int high) {
        this._low = low;
        this._high = high;
    }

    public RangeInt(final RangeInt range) {
        this(range._low, range._high);
    }

    /**
     * 数值iが、范围内にあるかを返す。
     * 
     * @param i
     *            数值
     * @return 范围内であればtrue
     */
    public boolean includes(final int i) {
        return (this._low <= i) && (i <= this._high);
    }

    public static boolean includes(final int i, final int low, final int high) {
        return (low <= i) && (i <= high);
    }

    /**
     * 数值iを、この范围内に丸める。
     * 
     * @param i
     *            数值
     * @return 丸められた值
     */
    public int ensure(final int i) {
        int r = i;
        r = (this._low <= r) ? r : this._low;
        r = (r <= this._high) ? r : this._high;
        return r;
    }

    /**
     * 
     * @param n
     * @param low
     * @param high
     * @return
     */
    public static int ensure(final int n, final int low, final int high) {
        int r = n;
        r = (low <= r) ? r : low;
        r = (r <= high) ? r : high;
        return r;
    }

    /**
     * この范围内からランダムな值を生成する。
     * 
     * @return 范围内のランダムな值
     */
    public int randomValue() {
        return _rnd.nextInt(this.getWidth() + 1) + this._low;
    }

    public int getLow() {
        return this._low;
    }

    public int getHigh() {
        return this._high;
    }

    public int getWidth() {
        return this._high - this._low;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof RangeInt)) {
            return false;
        }
        final RangeInt range = (RangeInt) obj;
        return (this._low == range._low) && (this._high == range._high);
    }

    @Override
    public String toString() {
        return "low=" + this._low + ", high=" + this._high;
    }
}
