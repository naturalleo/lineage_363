package com.lineage.server.utils;

import java.util.Random;

/**
 * 随机质取回
 * 
 * @author daien
 * 
 */
public class Dice {

    private static final Random _rnd = new Random();

    private final int _faces;// 基础值

    /**
     * 随机质取回
     * 
     * @param faces
     *            基础值
     */
    public Dice(final int faces) {
        this._faces = faces;
    }

    /**
     * 基础值
     * 
     * @return
     */
    public int getFaces() {
        return this._faces;
    }

    /**
     * 单次随机质
     * 
     * @return
     */
    public int roll() {
        return _rnd.nextInt(this._faces) + 1;
    }

    /**
     * 多次随机质
     * 
     * @param count
     *            计算次数
     * @return 多次随机质总合
     */
    public int roll(final int count) {
        int n = 0;
        for (int i = 0; i < count; i++) {
            n += this.roll();
        }
        return n;
    }
}
