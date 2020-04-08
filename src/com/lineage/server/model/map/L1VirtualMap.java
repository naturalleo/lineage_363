/**
 *                            License
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS  
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). 
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW.  
 * ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS LICENSE OR  
 * COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND  
 * AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE  
 * MAY BE CONSIDERED TO BE A CONTRACT,
 * THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED 
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */
package com.lineage.server.model.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class <code>L1VirtualMap</code> 虚拟地图.
 * 
 * @author jrwz
 * @version 2012-7-13上午08:14:45
 * @see com.lineage.server.model.map
 * @since JDK1.7
 */
public final class L1VirtualMap {
    /** 当前ID. */
    private int curId;
    /** 监测. */
    private final Object monitor = new Object();
    /** 虚拟地图起始编号. */
    private static final short FIRST_ID = 10001;
    /** 虚拟地图结束编号. */
    private static final short LAST_ID = 12000;
    /** 类的实例. */
    private static L1VirtualMap instance;

    /**
     * 当实例为空时才新建一个(多线程下的单例模式).
     * 
     * @return 实例
     */
    public static synchronized L1VirtualMap getInstance() {
        if (instance == null) {
            instance = new L1VirtualMap();
        }
        return instance;
    }

    /** 虚拟地图集合列表. */
    private final List<Integer> virtualMapList = new ArrayList<Integer>();

    /** . */
    L1VirtualMap() {
        this.curId = FIRST_ID;
    }

    /**
     * 增加新的虚拟地图.
     * 
     * @param mapId
     *            准备被虚拟的地图ID（原本的地图）
     * @return 生成的虚拟地图ID
     */
    public int addVirtualMap(final int mapId) {
        final L1Map virtualMap = L1WorldMap.get().getMap((short) mapId).clone();
        int nextId = this.nextId();
        while (this.virtualMapList.contains(nextId)) {
            nextId = this.nextId();
        }
        virtualMap.setId(nextId);
        L1WorldMap.get().addMap(virtualMap);
        this.virtualMapList.add(virtualMap.getId());
        return virtualMap.getId();
    }

    /**
     * 取得虚拟地图数量.
     * 
     * @return 集合中的地图总数
     */
    public int getNumOfVirtualMaps() {
        return this.virtualMapList.size();
    }

    /**
     * 取得集合中的虚拟地图.
     * 
     * @return 集合列表
     */
    public List<Integer> getVirtualMap() {
        return this.virtualMapList;
    }

    /**
     * 下一个地图ID.
     * 
     * @return 当前地图编号+1
     */
    public int nextId() {
        synchronized (this.monitor) {
            if (this.curId > LAST_ID) {
                this.curId = FIRST_ID;
            }
            return this.curId++;
        }
    }

    /**
     * 删除已有的虚拟地图.
     * 
     * @param mapId
     *            要删除的虚拟地图ID
     * @return true 成功 false 失败
     */
    public boolean removeVirtualMap(final int mapId) {
        if (!this.virtualMapList.contains(mapId)) {
            return false;
        }
        for (final Iterator<Integer> i = this.virtualMapList.listIterator(); i
                .hasNext();) {
            final int key = i.next(); // 调用下一个元素
            if (key == mapId) {
                i.remove(); // 删除元素
                break;
            }
        }
        L1WorldMap.get().removeMap(mapId);
        return true;
    }
}
