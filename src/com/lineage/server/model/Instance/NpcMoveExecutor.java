package com.lineage.server.model.Instance;

public abstract class NpcMoveExecutor {

    /**
     * 往指定面向移动1格
     * 
     * @param dir
     */
    public abstract void setDirectionMove(int dir);

    /**
     * 追踪方向返回
     * 
     * @param x
     *            目标点Ｘ
     * @param y
     *            目标点Ｙ
     * @return
     */
    public abstract int moveDirection(int x, int y);

    /**
     * 传回目标的反方向
     * 
     * @param tx
     *            目标点Ｘ
     * @param ty
     *            目标点Ｙ
     * @return
     */
    public abstract int targetReverseDirection(int tx, int ty);

    /**
     * 对于前进方向是否有障碍的确认
     * 
     * @param h
     * @return
     */
    public abstract int checkObject(final int h);

    public abstract int openDoor(final int h);

    public abstract void clear();

    // 正向移动
    protected static final byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
    protected static final byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

    // 反向移动
    protected static final byte HEADING_TABLE_XR[] = { 0, -1, -1, -1, 0, 1, 1,
            1 };
    protected static final byte HEADING_TABLE_YR[] = { 1, 1, 0, -1, -1, -1, 0,
            1 };

    // 反面向
    protected static final byte HEADING_RD[] = { 4, 5, 6, 7, 0, 1, 2, 3 };

    // 方向判断定位
    protected static final int[][] _ary1 = new int[][] {
            new int[] { 7, 1, 0, 6, 2 }, new int[] { 7, 3, 1, 0, 2 },
            new int[] { 3, 1, 0, 4, 2 }, new int[] { 5, 3, 1, 4, 2 },
            new int[] { 5, 3, 6, 4, 2 }, new int[] { 7, 5, 3, 6, 4 },
            new int[] { 7, 5, 0, 6, 4 }, new int[] { 7, 5, 1, 0, 6 }, };

    protected static final int _heading2[] = { 7, 0, 1, 2, 3, 4, 5, 6 };
    protected static final int _heading3[] = { 1, 2, 3, 4, 5, 6, 7, 0 };

}
