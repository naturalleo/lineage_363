package com.lineage.server.model.map;

import java.io.Serializable;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.types.Point;

/**
 * Map位置资讯取回
 */
public abstract class L1Map implements Cloneable, Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static L1NullMap _nullMap = new L1NullMap();

    protected L1Map() {
    }
    @Override
    public L1Map clone() {
        try {
            return (L1Map) super.clone();
        } catch (final CloneNotSupportedException e) {
            throw new Error("CloneNotSupportedException was thrown");
        }
    }
    
    /**
     * 取得Base地图ID.
     */
    public abstract int getBaseMapId();
    
    /**
     * 传回地图编号
     * 
     * @return 地图编号
     */
    public abstract int getId();

    /**
     * 起点X
     * 
     * @return
     */
    public abstract int getX();

    /**
     * 起点Y
     * 
     * @return
     */
    public abstract int getY();

    /**
     * 地图宽度(终点X - 起点X)
     * 
     * @return
     */
    public abstract int getWidth();

    /**
     * 地图高度(终点Y - 起点Y)
     * 
     * @return
     */
    public abstract int getHeight();

    /**
     * 指定された座标の值を返す。
     * 
     * 推奖されていません。このメソッドは、既存コードとの互换性の为に提供されています。
     * L1Mapの利用者は通常、マップにどのような值が格纳されているかを知る必要はありません。
     * また、格纳されている值に依存するようなコードを书くべきではありません。 デバッグ等の特殊な场合に限り、このメソッドを利用できます。
     * 
     * @param x
     *            座标のX值
     * @param y
     *            座标のY值
     * @return 指定された座标の值
     */
    public abstract int getTile(int x, int y);

    /**
     * 指定された座标の值を返す。
     * 
     * 推奖されていません。このメソッドは、既存コードとの互换性の为に提供されています。
     * L1Mapの利用者は通常、マップにどのような值が格纳されているかを知る必要はありません。
     * また、格纳されている值に依存するようなコードを书くべきではありません。 デバッグ等の特殊な场合に限り、このメソッドを利用できます。
     * 
     * @param x
     *            座标X值
     * @param y
     *            座标Y值
     * @return 指定座标的值 0 : 无法通过 15: 一般区域 16: 装饰物件 31: 安全区域 47: 战斗区域
     * 
     * 
     */
    public abstract int getOriginalTile(int x, int y);

    /**
     * 传回座标是否在地图指定可用位置
     * 
     * @param pt
     *            座标Point
     * @return 位置可用传回 true
     */
    public abstract boolean isInMap(Point pt);

    /**
     * 传回座标是否在地图指定可用位置
     * 
     * @param x
     *            座标X值
     * @param y
     *            座标Y值
     * @return 位置可用传回 true
     */
    public abstract boolean isInMap(int x, int y);

    /**
     * 指定座标通行可能
     * 
     * @param pt
     *            座标Point
     * @param cha
     * @return true:可以通过 false:不能通过
     */
    public abstract boolean isPassable(Point pt, final L1Character cha);

    /**
     * 指定座标通行可能
     * 
     * @param x
     *            座标X值
     * @param y
     *            座标Y值
     * @param cha
     * @return true:可以通过 false:不能通过
     */
    public abstract boolean isPassable(int x, int y, final L1Character cha);

    /**
     * 指定座标heading方向通行可能
     * 
     * @param pt
     *            座标Point
     * @param heading
     * @param cha
     * @return true:可以通过 false:不能通过
     */
    public abstract boolean isPassable(Point pt, int heading,
            final L1Character cha);

    /**
     * 指定座标heading方向通行可能
     * 
     * @param x
     *            座标X值
     * @param y
     *            座标Y值
     * @param heading
     * @param cha
     * @return true:可以通过 false:不能通过
     */
    public abstract boolean isPassable(int x, int y, int heading,
            final L1Character cha);

    /**
     * 指定座标heading方向通行可能
     * 
     * @param x
     *            座标X值
     * @param y
     *            座标Y值
     * @param heading
     * @return
     */
    public abstract boolean isPassableDna(final int x, final int y,
            final int heading);

    /**
     * 该座标门的判断
     * 
     * @param x
     * @param y
     * @param heading
     * @param npc
     * @return
     */
    public abstract boolean isDoorPassable(final int x, final int y,
            final int heading, final L1NpcInstance npc);

    /**
     * 设定座标障碍宣告
     * 
     * @param pt
     *            座标Point
     * @param isPassable
     *            true:可以通过 false:不能通过
     */
    public abstract void setPassable(Point pt, boolean isPassable);

    /**
     * 设定座标障碍宣告
     * 
     * @param x
     *            座标X值
     * @param y
     *            座标Y值
     * @param isPassable
     *            true:可以通过 false:不能通过
     * @param door
     *            0:门／ 1:门＼ 2:一般
     */
    public abstract void setPassable(int x, int y, boolean isPassable, int door);

    /**
     * 指定座标位置是安全区域。
     * 
     * @param pt
     * @return 安全区域返回true
     */
    public abstract boolean isSafetyZone(Point pt);

    /**
     * 指定座标位置是安全区域。
     * 
     * @param x
     *            座标X值
     * @param y
     *            座标Y值
     * @return 安全区域返回true
     */
    public abstract boolean isSafetyZone(int x, int y);

    /**
     * 指定座标位置是战斗区域。
     * 
     * @param pt
     *            座标资料
     * @return 战斗区域返回true
     */
    public abstract boolean isCombatZone(Point pt);

    /**
     * 指定座标位置是战斗区域。
     * 
     * @param x
     *            座标X值
     * @param y
     *            座标Y值
     * @return 战斗区域返回true
     */
    public abstract boolean isCombatZone(int x, int y);

    /**
     * 指定座标位置是一般区域。
     * 
     * @param pt
     *            座标资料
     * @return 一般区域返回true
     */
    public abstract boolean isNormalZone(Point pt);

    /**
     * 指定座标位置是一般区域。
     * 
     * @param x
     *            座标X值
     * @param y
     *            座标Y值
     * @return 一般区域返回true
     */
    public abstract boolean isNormalZone(int x, int y);

    /**
     * 指定座标远程攻击是否能通过。
     * 
     * @param pt
     *            资料
     * @return 远程攻击能通过返回true
     */
    public abstract boolean isArrowPassable(Point pt);

    /**
     * 指定座标远程攻击是否能通过。
     * 
     * @param x
     *            座标X值
     * @param y
     *            座标Y值
     * @return 远程攻击能通过返回true
     */
    public abstract boolean isArrowPassable(int x, int y);

    /**
     * 指定座标远程攻击是否能通过。
     * 
     * @param pt
     *            座标资料
     * @param heading
     *            方向
     * @return 远程攻击能通过返回true
     */
    public abstract boolean isArrowPassable(Point pt, int heading);

    /**
     * 指定座标远程攻击是否能通过。
     * 
     * @param x
     *            座标X值
     * @param y
     *            座标Y值
     * @param heading
     *            方向
     * @return 远程攻击能通过返回true
     */
    public abstract boolean isArrowPassable(int x, int y, int heading);

    /**
     * 水中
     * 
     * @return 地图在水中 返回true
     */
    public abstract boolean isUnderwater();

    /**
     * 记忆座标
     * 
     * @return 可使用记忆座标 返回true
     */
    public abstract boolean isMarkable();

    /**
     * 传送
     * 
     * @return 可使用传送 返回true
     */
    public abstract boolean isTeleportable();

    /**
     * 回卷
     * 
     * @return 可使用回卷 返回true
     */
    public abstract boolean isEscapable();

    /**
     * 复活可能
     * 
     * @return 可复活
     */
    public abstract boolean isUseResurrection();
    
    /**
     * 设置地图ID.
     */
    public abstract void setId(int mapId);

    /**
     * 魔杖使用
     * 
     * @return 可使用魔杖
     */
    public abstract boolean isUsePainwand();

    /**
     * 死亡逞罚
     * 
     * @return 具有死亡逞罚
     */
    public abstract boolean isEnabledDeathPenalty();

    /**
     * 携带宠物
     * 
     * @return 可携带宠物 返回true
     */
    public abstract boolean isTakePets();

    /**
     * 呼叫宠物
     * 
     * @return 可呼叫宠物 返回true
     */
    public abstract boolean isRecallPets();

    /**
     * 使用物品
     * 
     * @return 可使用物品 返回true
     */
    public abstract boolean isUsableItem();

    /**
     * 使用技能
     * 
     * @return 可使用技能 返回true
     */
    public abstract boolean isUsableSkill();

    /**
     * 指定された座标が钓りゾーンであるかを返す。
     * 
     * @param x
     *            座标のX值
     * @param y
     *            座标のY值
     * @return 钓りゾーンであればtrue
     */
    public abstract boolean isFishingZone(int x, int y);

    /**
     * 指定作标位置是门
     * 
     * @param x
     *            座标X值
     * @param y
     *            座标Y值
     * @return 门返回true
     */
    public abstract int isExistDoor(int x, int y);

    public static L1Map newNull() {
        return _nullMap;
    }

    /**
     * 指定されたptのタイルの文字列表现を返す。
     */
    public abstract String toString(Point pt);

    /**
     * このマップがnullであるかを返す。
     * 
     * @return nullであれば、true
     */
    public boolean isNull() {
        return false;
    }
}

/**
 * 何もしないMap。
 */
class L1NullMap extends L1Map {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public L1NullMap() {
    }
    
    @Override
    public L1NullMap clone() {
        return (L1NullMap) super.clone();
    }
    
    @Override
    public int getBaseMapId() {
        return 0;
    }
    
    @Override
    public int getId() {
        return 0;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getTile(final int x, final int y) {
        return 0;
    }

    @Override
    public int getOriginalTile(final int x, final int y) {
        return 0;
    }

    @Override
    public boolean isInMap(final int x, final int y) {
        return false;
    }

    @Override
    public boolean isInMap(final Point pt) {
        return false;
    }

    @Override
    public boolean isPassable(final int x, final int y, final L1Character cha) {
        return false;
    }

    @Override
    public boolean isPassable(final Point pt, final L1Character cha) {
        return false;
    }

    @Override
    public boolean isPassable(final int x, final int y, final int heading,
            final L1Character cha) {
        return false;
    }

    @Override
    public boolean isPassableDna(final int x, final int y, final int heading) {
        return false;
    }

    @Override
    public boolean isPassable(final Point pt, final int heading,
            final L1Character cha) {
        return false;
    }

    @Override
    public boolean isDoorPassable(final int x, final int y, final int heading,
            final L1NpcInstance npc) {
        return false;
    }

    @Override
    public void setPassable(final int x, final int y, final boolean isPassable,
            final int door) {
    }

    @Override
    public void setPassable(final Point pt, final boolean isPassable) {
    }

    @Override
    public boolean isSafetyZone(final int x, final int y) {
        return false;
    }

    @Override
    public boolean isSafetyZone(final Point pt) {
        return false;
    }

    @Override
    public boolean isCombatZone(final int x, final int y) {
        return false;
    }

    @Override
    public boolean isCombatZone(final Point pt) {
        return false;
    }

    @Override
    public boolean isNormalZone(final int x, final int y) {
        return false;
    }

    @Override
    public boolean isNormalZone(final Point pt) {
        return false;
    }

    @Override
    public boolean isArrowPassable(final int x, final int y) {
        return false;
    }

    @Override
    public boolean isArrowPassable(final Point pt) {
        return false;
    }

    @Override
    public boolean isArrowPassable(final int x, final int y, final int heading) {
        return false;
    }

    @Override
    public boolean isArrowPassable(final Point pt, final int heading) {
        return false;
    }

    @Override
    public boolean isUnderwater() {
        return false;
    }

    @Override
    public boolean isMarkable() {
        return false;
    }

    @Override
    public boolean isTeleportable() {
        return false;
    }

    @Override
    public boolean isEscapable() {
        return false;
    }

    @Override
    public boolean isUseResurrection() {
        return false;
    }
    
    @Override
    public void setId(final int mapId) {

    }

    @Override
    public boolean isUsePainwand() {
        return false;
    }

    @Override
    public boolean isEnabledDeathPenalty() {
        return false;
    }

    @Override
    public boolean isTakePets() {
        return false;
    }

    @Override
    public boolean isRecallPets() {
        return false;
    }

    @Override
    public boolean isUsableItem() {
        return false;
    }

    @Override
    public boolean isUsableSkill() {
        return false;
    }

    @Override
    public boolean isFishingZone(final int x, final int y) {
        return false;
    }

    /*
     * public void set_door(int x, int y, boolean door) { }
     */

    @Override
    public int isExistDoor(final int x, final int y) {
        return 0x03;
    }

    @Override
    public String toString(final Point pt) {
        return "null";
    }

    @Override
    public boolean isNull() {
        return true;
    }
}
