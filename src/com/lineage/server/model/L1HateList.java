package com.lineage.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 攻击目标清单
 * 
 * @author daien
 * 
 */
public class L1HateList {

    private final Map<L1Character, Integer> _hateMap;

    /**
     * 攻击目标清单
     * 
     * @param hateMap
     */
    private L1HateList(final Map<L1Character, Integer> hateMap) {
        this._hateMap = hateMap;
    }

    /**
     * 攻击目标清单
     */
    public L1HateList() {
        this._hateMap = new HashMap<L1Character, Integer>();
    }

    /**
     * 将指定的值与此映射中的指定键关联（可选操作）。<BR>
     * 如果此映射以前包含一个该键的映射关系，<BR>
     * 则用指定值替换旧值（当且仅当 m.containsKey(k) 返回 true 时，<BR>
     * 才能说映射 m 包含键 k 的映射关系）<BR>
     * 
     * @param cha
     *            攻击目标
     * @param hate
     */
    public synchronized void add(final L1Character cha, final int hate) {
        if (cha == null) {
            return;
        }
        Integer h = this._hateMap.get(cha);
        if (h != null) {
            this._hateMap.put(cha, h + hate);

        } else {
            this._hateMap.put(cha, hate);
        }
    }

    /**
     * 攻击目标清单中
     * 
     * @param cha
     * @return true:是 false:否
     */
    public synchronized boolean isHate(final L1Character cha) {
        if (this._hateMap.get(cha) != null) {
            return true;
        }
        return false;
    }

    /**
     * 返回指定键所映射的值；如果此映射不包含该键的映射关系，则返回 null。<BR>
     * <BR>
     * 更确切地讲，如果此映射包含满足 (key==null ? k==null : key.equals(k)) 的键 k 到值 v 的映射关系，<BR>
     * 则此方法返回 v；否则返回 null。（最多只能有一个这样的映射关系）。<BR>
     * <BR>
     * 如果此映射允许 null 值，则返回 null 值并不一定 表示该映射不包含该键的映射关系；<BR>
     * 也可能该映射将该键显示地映射到 null。使用 containsKey 操作可区分这两种情况。<BR>
     * 
     * @param cha
     * @return
     */
    public synchronized int get(final L1Character cha) {
        return this._hateMap.get(cha);
    }

    /**
     * 如果此映射包含指定键的映射关系，则返回 true。<BR>
     * 更确切地讲，当且仅当此映射包含针对满足 (key==null ? k==null : key.equals(k)) 的键 k 的映射关系时，<BR>
     * 返回 true。（最多只能有一个这样的映射关系）。<BR>
     * 
     * @param cha
     * @return
     */
    public synchronized boolean containsKey(final L1Character cha) {
        return this._hateMap.containsKey(cha);
    }

    /**
     * 如果存在一个键的映射关系，则将其从此映射中移除（可选操作）。<BR>
     * 更确切地讲，如果此映射包含从满足 (key==null ? k==null :key.equals(k)) 的键 k 到值 v 的映射关系，<BR>
     * 则移除该映射关系。（该映射最多只能包含一个这样的映射关系。）<BR>
     * <BR>
     * 返回此映射中以前关联该键的值，如果此映射不包含该键的映射关系，则返回 null。<BR>
     * <BR>
     * 如果此映射允许 null 值，则返回 null 值并不一定 表示该映射不包含该键的映射关系；也可能该映射将该键显示地映射到 null。<BR>
     * <BR>
     * 调用返回后，此映射将不再包含指定键的映射关系。<BR>
     * 
     * @param cha
     */
    public synchronized void remove(final L1Character cha) {
        this._hateMap.remove(cha);
    }

    /**
     * 从此映射中移除所有映射关系（可选操作）。此调用返回后，该映射将为空。
     */
    public synchronized void clear() {
        this._hateMap.clear();
    }

    /**
     * 如果此映射未包含键-值映射关系，则返回 true。
     * 
     * @return
     */
    public synchronized boolean isEmpty() {
        return this._hateMap.isEmpty();
    }

    public synchronized L1Character getMaxHateCharacter() {
        L1Character cha = null;
        int hate = Integer.MIN_VALUE;

        for (final Map.Entry<L1Character, Integer> e : this._hateMap.entrySet()) {
            if (hate < e.getValue()) {
                cha = e.getKey();
                hate = e.getValue();
            }
        }
        return cha;
    }

    public synchronized void removeInvalidCharacter(final L1NpcInstance npc) {
        final ArrayList<L1Character> invalidChars = new ArrayList<L1Character>();
        for (final L1Character cha : this._hateMap.keySet()) {
            if ((cha == null) || cha.isDead() || !npc.knownsObject(cha)) {
                invalidChars.add(cha);
            }
        }

        for (final L1Character cha : invalidChars) {
            this._hateMap.remove(cha);
        }
    }

    public synchronized int getTotalHate() {
        int totalHate = 0;
        for (final int hate : this._hateMap.values()) {
            totalHate += hate;
        }
        return totalHate;
    }

    public synchronized int getTotalLawfulHate() {
        int totalHate = 0;
        for (final Map.Entry<L1Character, Integer> e : this._hateMap.entrySet()) {
            if (e.getKey() instanceof L1PcInstance) {
                totalHate += e.getValue();
            }
        }
        return totalHate;
    }

    public synchronized int getPartyHate(final L1Party party) {
        int partyHate = 0;

        for (final Map.Entry<L1Character, Integer> e : this._hateMap.entrySet()) {
            L1PcInstance pc = null;
            if (e.getKey() instanceof L1PcInstance) {
                pc = (L1PcInstance) e.getKey();
            }
            if (e.getKey() instanceof L1NpcInstance) {
                final L1Character cha = ((L1NpcInstance) e.getKey())
                        .getMaster();
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance) cha;
                }
            }

            if ((pc != null) && party.isMember(pc)) {
                partyHate += e.getValue();
            }
        }
        return partyHate;
    }

    public synchronized int getPartyLawfulHate(final L1Party party) {
        int partyHate = 0;

        for (final Map.Entry<L1Character, Integer> e : this._hateMap.entrySet()) {
            L1PcInstance pc = null;
            if (e.getKey() instanceof L1PcInstance) {
                pc = (L1PcInstance) e.getKey();
            }

            if ((pc != null) && party.isMember(pc)) {
                partyHate += e.getValue();
            }
        }
        return partyHate;
    }

    /**
     * 构造一个映射关系与指定 Map 相同的新 HashMap。<BR>
     * 所创建的 HashMap 具有默认加载因子 (0.75) 和足以容纳指定 Map 中映射关系的初始容量。<BR>
     * 
     * @return
     */
    public synchronized L1HateList copy() {
        return new L1HateList(new HashMap<L1Character, Integer>(this._hateMap));
    }

    /**
     * 返回此映射中包含的映射关系的 Set 视图。<BR>
     * 该 set 受映射支持，<BR>
     * 所以对映射的更改可在此 set 中反映出来，反之亦然。<BR>
     * 如果对该 set 进行迭代的同时修改了映射（通过迭代器自己的 remove 操作，<BR>
     * 或者通过对迭代器返回的映射项执行 setValue 操作除外），<BR>
     * 则迭代结果是不确定的。set 支持元素移除，<BR>
     * 通过 Iterator.remove、Set.remove、removeAll、retainAll 和 clear
     * 操作可从映射中移除相应的映射关系。<BR>
     * 它不支持 add 或 addAll 操作。<BR>
     * 
     * @return
     */
    public synchronized Set<Entry<L1Character, Integer>> entrySet() {
        return this._hateMap.entrySet();
    }

    /**
     * 返回此映射中包含的键的 Set 视图。<BR>
     * 该 set 受映射支持，<BR>
     * 所以对映射的更改可在此 set 中反映出来，反之亦然。<BR>
     * 如果对该 set 进行迭代的同时修改了映射（通过迭代器自己的 remove 操作除外），<BR>
     * 则迭代结果是不确定的。set 支持元素移除，<BR>
     * 通过 Iterator.remove、Set.remove、removeAll、retainAll 和 clear
     * 操作可从映射中移除相应的映射关系。<BR>
     * 它不支持 add 或 addAll 操作。<BR>
     * 
     * @return
     */
    public synchronized ArrayList<L1Character> toTargetArrayList() {
        return new ArrayList<L1Character>(this._hateMap.keySet());
    }

    /**
     * 返回此映射中包含的值的 Collection 视图。<BR>
     * 该 collection 受映射支持，<BR>
     * 所以对映射的更改可在此 collection 中反映出来，反之亦然。<BR>
     * 如果对该 collection 进行迭代的同时修改了映射（通过迭代器自己的 remove 操作除外），<BR>
     * 则迭代结果是不确定的。collection 支持元素移除，<BR>
     * 通过 Iterator.remove、Collection.remove、removeAll、retainAll 和 clear
     * 操作可从映射中移除相应的映射关系。<BR>
     * 它不支持 add 或 addAll 操作。<BR>
     * 
     * @return
     */
    public synchronized ArrayList<Integer> toHateArrayList() {
        return new ArrayList<Integer>(this._hateMap.values());
    }
}
