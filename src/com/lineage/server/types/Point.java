package com.lineage.server.types;

/**
 * 座标点
 * 
 * @author daien
 * 
 */
public class Point {

    protected int _x = 0;
    protected int _y = 0;

    public Point() {
    }

    public Point(final int x, final int y) {
        this._x = x;
        this._y = y;
    }

    public Point(final Point pt) {
        this._x = pt._x;
        this._y = pt._y;
    }

    /**
     * X点座标
     * 
     * @return
     */
    public int getX() {
        return this._x;
    }

    /**
     * 设置X点座标
     * 
     * @param x
     */
    public void setX(final int x) {
        this._x = x;
    }

    /**
     * Y点座标
     * 
     * @return
     */
    public int getY() {
        return this._y;
    }

    /**
     * 设置Y点座标
     * 
     * @param y
     */
    public void setY(final int y) {
        this._y = y;
    }

    /**
     * 设置座标点
     * 
     * @param pt
     *            座标点
     */
    public void set(final Point pt) {
        this._x = pt._x;
        this._y = pt._y;
    }

    /**
     * 设置座标点
     * 
     * @param x
     *            座标点X
     * @param y
     *            座标点Y
     */
    public void set(final int x, final int y) {
        this._x = x;
        this._y = y;
    }

    private static final int HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
    private static final int HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

    /**
     * 指定面向前进位置座标
     * 
     * @param heading
     *            0~7 面向
     */
    public void forward(final int heading) {
        this._x += HEADING_TABLE_X[heading];
        this._y += HEADING_TABLE_Y[heading];
    }

    /**
     * 指定面向反向前进位置座标
     * 
     * @param heading
     *            0~7 面向
     */
    public void backward(final int heading) {
        this._x -= HEADING_TABLE_X[heading];
        this._y -= HEADING_TABLE_Y[heading];
    }

    /**
     * 指定座标直线距离
     * 
     * @param pt
     * @return 距离质
     */
    public double getLineDistance(final Point pt) {
        final long diffX = pt.getX() - this.getX();
        final long diffY = pt.getY() - this.getY();
        return Math.sqrt((diffX * diffX) + (diffY * diffY));
    }

    /**
     * 指定座标直线距离(相对位置最大距离)
     * 
     * @param pt
     * @return 距离质
     */
    /*
     * public double getLineDistanceX(final Point pt) { final long diffX =
     * pt.getX() - this.getX(); final long diffY = pt.getY() - this.getY();
     * return Math.max(Math.abs(diffX), Math.abs(diffY)); }
     */

    /**
     * 指定座标直线距离
     * 
     * @param x
     * @param y
     * @return 距离质
     */
    public double getLineDistance(final int x, final int y) {
        final long diffX = x - this.getX();
        final long diffY = y - this.getY();
        return Math.sqrt((diffX * diffX) + (diffY * diffY));
    }

    /**
     * 指定座标直线距离(相对位置最大距离)
     * 
     * @param pt
     * @return 距离质
     */
    public int getTileLineDistance(final Point pt) {
        return Math.max(Math.abs(pt.getX() - this.getX()),
                Math.abs(pt.getY() - this.getY()));
    }

    /**
     * 指定座标距离(XY距离总合)
     * 
     * @param pt
     * @return 距离质
     */
    public int getTileDistance(final Point pt) {
        return Math.abs(pt.getX() - this.getX())
                + Math.abs(pt.getY() - this.getY());
    }

    /**
     * 指定座标19格范围内
     * 
     * @param pt
     * @return 指定座标在19格范围内 返回true
     */
    public boolean isInScreen(final Point pt) {
        int dist = getTileDistance(pt);
        // 3.5c可见范围再度修正
        if (dist > 19) { // 当tile距离 > 19 的时候，判定为不在画面内(false)
            return false;

        } else if (dist <= 18) { // 当tile距离 <= 18 的时候，判定为位于同一个画面内(true)
            return true;

        } else {
            // 显示区的座标系统 (18, 18)
            // 3.5c可见范围再度修正
            int dist2 = Math.abs(pt.getX() - (getX() - 18))
                    + Math.abs(pt.getY() - (getY() - 18));
            if ((19 <= dist2) && (dist2 <= 52)) {
                return true;
            }
            return false;
        }
    }

    /**
     * 是否与指定座标位置重叠
     * 
     * @param pt
     * @return TRUE是 FALSE否
     */
    public boolean isSamePoint(final Point pt) {
        return ((pt.getX() == this.getX()) && (pt.getY() == this.getY()));
    }

    @Override
    public int hashCode() {
        return 7 * this.getX() + this.getY();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Point)) {
            return false;
        }
        final Point pt = (Point) obj;
        return (this.getX() == pt.getX()) && (this.getY() == pt.getY());
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", this._x, this._y);
    }
}
