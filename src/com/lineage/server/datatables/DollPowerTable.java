package com.lineage.server.datatables;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.doll.L1DollExecutor;
import com.lineage.server.templates.L1Doll;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 娃娃能力资料
 * 
 * @author dexc
 * 
 */
public class DollPowerTable {

    private static final Log _log = LogFactory.getLog(DollPowerTable.class);

    private static DollPowerTable _instance;

    // 全部娃娃能力设置
    private static final HashMap<Integer, L1Doll> _powerMap = new HashMap<Integer, L1Doll>();

    // 全部娃娃能力设置
    private static final HashMap<Integer, L1DollExecutor> _classList = new HashMap<Integer, L1DollExecutor>();

    // 初始检查用
    private static final ArrayList<String> _checkList = new ArrayList<String>();
    
    private static final HashMap<Integer, String> _powerMsg = new HashMap<Integer, String>();

    public static DollPowerTable get() {
        if (_instance == null) {
            _instance = new DollPowerTable();
        }
        return _instance;
    }

    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = DatabaseFactory.get().getConnection();

            pstm = con.prepareStatement("SELECT * FROM `etcitem_doll_power`");

            rs = pstm.executeQuery();

            while (rs.next()) {
                final int id = rs.getInt("id");
                final String classname = rs.getString("classname");
                final int type1 = rs.getInt("type1");
                final int type2 = rs.getInt("type2");
                final int type3 = rs.getInt("type3");
                final String note = rs.getString("note");
                String ch = classname + "=" + type1 + "=" + type2 + "=" + type3;
                if (_checkList.lastIndexOf(ch) == -1) {
                    _checkList.add(ch);
                    addList(id, classname, type1, type2, type3);
                    _powerMsg.put(id, note);
                } else {
                    _log.error("娃娃能力设置重复:id=" + id + " type1=" + type1
                            + " type2=" + type2 + " type3=" + type3);
                }
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);

            _checkList.clear();
        }
        _log.info("载入娃娃能力资料数量: " + _classList.size() + "(" + timer.get()
                + "ms)");
        setDollType();
    }

    /**
     * 设置娃娃能力资料
     */
    private void setDollType() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = DatabaseFactory.get().getConnection();

            pstm = con.prepareStatement("SELECT * FROM `etcitem_doll_type`");

            rs = pstm.executeQuery();

            while (rs.next()) {
                final int itemid = rs.getInt("itemid");
                final String powers = rs.getString("powers")
                        .replaceAll(" ", "");// 取代空白
                final String need = rs.getString("need").replaceAll(" ", "");// 取代空白
                final String count = rs.getString("count").replaceAll(" ", "");// 取代空白
                final int time = rs.getInt("time");
                final int gfxid = rs.getInt("gfxid");
                final String nameid = rs.getString("nameid");

                boolean iserr = false;
                final ArrayList<L1DollExecutor> powerList = new ArrayList<L1DollExecutor>();
                int[] powlists = null;
                if (powers != null) {
                    if (!powers.equals("")) {
                        final String[] set1 = powers.split(",");
						powlists = new int[set1.length];
						for (int i = 0; i < set1.length; i++) {
							int powlist = Integer.parseInt(set1[i]);
							powlists[i] = powlist;
						}
                        for (final String string : set1) {
                            L1DollExecutor e = _classList.get(Integer
                                    .parseInt(string));
                            if (e != null) {
                                powerList.add(e);

                            } else {
                                _log.error("娃娃能力取回错误-没有这个编号:" + string);
                                iserr = true;
                            }
                        }
                    }
                }
                int[] needs = null;
                if (need != null) {
                    if (!need.equals("")) {
                        final String[] set2 = need.split(",");
                        needs = new int[set2.length];
                        for (int i = 0; i < set2.length; i++) {
                            int itemid_n = Integer.parseInt(set2[i]);
                            // 找回物品资讯
                            final L1Item temp = ItemTable.get().getTemplate(
                                    itemid_n);
                            if (temp == null) {
                                _log.error("物品资讯取回错误-没有这个编号:" + itemid_n);
                                iserr = true;
                            }
                            needs[i] = itemid_n;
                        }
                    }
                }
                int[] counts = null;
                if (count != null) {
                    if (!count.equals("")) {
                        final String[] set3 = count.split(",");
                        counts = new int[set3.length];
                        if (set3.length != needs.length) {
                            _log.error("物品资讯对应错误-长度不吻合: itemid:" + itemid);
                            iserr = true;
                        }

                        for (int i = 0; i < set3.length; i++) {
                            int count_n = Integer.parseInt(set3[i]);
                            counts[i] = count_n;
                        }
                    }
                }

                if (!iserr) {
                    L1Doll doll_power = new L1Doll();
                    doll_power.set_itemid(itemid);
                    doll_power.setPowerList(powerList);
                    doll_power.set_powlist(powlists);
                    doll_power.set_need(needs);
                    doll_power.set_counts(counts);
                    doll_power.set_time(time);
                    doll_power.set_gfxid(gfxid);
                    doll_power.set_nameid(nameid);

                    _powerMap.put(itemid, doll_power);
                }
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);

            _classList.clear();
        }
        _log.info("载入娃娃能力资料数量: " + _classList.size() + "(" + timer.get()
                + "ms)");
    }

    /**
     * 加入CLASS清单
     * 
     * @param powerid
     * @param className
     * @param int1
     * @param int2
     * @param int3
     */
    private void addList(final int powerid, final String className, int int1,
            int int2, int int3) {
        if (className.equals("0")) {
            return;
        }
        try {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("com.lineage.server.model.doll.");
            stringBuilder.append(className);

            final Class<?> cls = Class.forName(stringBuilder.toString());
            final L1DollExecutor exe = (L1DollExecutor) cls.getMethod("get")
                    .invoke(null);
            exe.set_power(int1, int2, int3);

            _classList.put(new Integer(powerid), exe);

        } catch (final ClassNotFoundException e) {
            String error = "发生[娃娃能力档案]错误, 检查档案是否存在:" + className + " 娃娃能力编号:"
                    + powerid;
            _log.error(error);

        } catch (final IllegalArgumentException e) {
            _log.error(e.getLocalizedMessage(), e);

        } catch (final IllegalAccessException e) {
            _log.error(e.getLocalizedMessage(), e);

        } catch (final InvocationTargetException e) {
            _log.error(e.getLocalizedMessage(), e);

        } catch (final SecurityException e) {
            _log.error(e.getLocalizedMessage(), e);

        } catch (final NoSuchMethodException e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 传回娃娃能力设置
     * 
     * @param key
     * @return
     */
    public L1Doll get_type(int key) {
        return _powerMap.get(key);
    }

    /**
     * 传回娃娃能力设置
     * 
     * @return
     */
    public HashMap<Integer, L1Doll> map() {
        return _powerMap;
    }
    
	/**
	 * 传回娃娃显示数据库上的note属性
	 * @param key
	 * @return
	 */
	public String get_powMsg(int key) {
		return  _powerMsg.get(key);
	}
}
