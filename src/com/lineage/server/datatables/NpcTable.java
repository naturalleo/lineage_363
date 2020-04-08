package com.lineage.server.datatables;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * NPC设置资料
 * 
 * @author dexc
 * 
 */
public class NpcTable {

    private static final Log _log = LogFactory.getLog(NpcTable.class);

    // private boolean _initialized;

    private static NpcTable _instance;

    public static int ORC = -1;

    private static final Map<Integer, L1Npc> _npcs = new HashMap<Integer, L1Npc>();

    private static final Map<String, Constructor<?>> _constructorCache = new HashMap<String, Constructor<?>>();

    private static final Map<String, Integer> _familyTypes = NpcTable
            .buildFamily();

    public static NpcTable get() {
        if (_instance == null) {
            _instance = new NpcTable();
        }
        return _instance;
    }

    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        this.loadNpcData();
        // this._initialized = true;
        _log.info("载入NPC设置资料数量: " + _npcs.size() + "(" + timer.get() + "ms)");
    }

    /**
     * 取得执行类位置
     * 
     * @param implName
     * @return
     */
    private Constructor<?> getConstructor(final String implName) {
        try {
            final String implFullName = "com.lineage.server.model.Instance."
                    + implName + "Instance";
            final Constructor<?> con = Class.forName(implFullName)
                    .getConstructors()[0];
            return con;

        } catch (final ClassNotFoundException e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 加载NPC执行类位置
     * 
     * @param implName
     */
    private void registerConstructorCache(final String implName) {
        if (implName.isEmpty() || _constructorCache.containsKey(implName)) {
            return;
        }
        _constructorCache.put(implName, this.getConstructor(implName));
    }

    private void loadNpcData() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `npc`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                final L1Npc npc = new L1Npc();
                final int npcId = rs.getInt("npcid");
                npc.set_npcId(npcId);
                npc.set_name(rs.getString("name"));
                npc.set_nameid(rs.getString("nameid"));

                final String classname = rs.getString("classname");
                npc.set_classname(classname);
                npc.setNpcExecutor(addClass(npcId, classname));

                npc.setImpl(rs.getString("impl"));
                npc.set_gfxid(rs.getInt("gfxid"));
                npc.set_level(rs.getInt("lvl"));
                npc.set_hp(rs.getInt("hp"));
                npc.set_mp(rs.getInt("mp"));
                npc.set_ac(rs.getInt("ac"));
                npc.set_str(rs.getByte("str"));
                npc.set_con(rs.getByte("con"));
                npc.set_dex(rs.getByte("dex"));
                npc.set_wis(rs.getByte("wis"));
                npc.set_int(rs.getByte("intel"));
                npc.set_mr(rs.getInt("mr"));
                npc.set_exp(rs.getInt("exp"));
                npc.set_lawful(rs.getInt("lawful"));
                npc.set_size(rs.getString("size"));
                npc.set_weakAttr(rs.getInt("weakAttr"));// NPC害怕属性
                npc.set_ranged(rs.getInt("ranged"));
                npc.setTamable(rs.getBoolean("tamable"));
                npc.set_passispeed(rs.getInt("passispeed"));
                npc.set_atkspeed(rs.getInt("atkspeed"));
                npc.setAtkMagicSpeed(rs.getInt("atk_magic_speed"));
                npc.setSubMagicSpeed(rs.getInt("sub_magic_speed"));
                npc.set_undead(rs.getInt("undead"));
                npc.set_poisonatk(rs.getInt("poison_atk"));
                npc.set_paralysisatk(rs.getInt("paralysis_atk"));
                npc.set_agro(rs.getBoolean("agro"));
                npc.set_agrososc(rs.getBoolean("agrososc"));
                npc.set_agrocoi(rs.getBoolean("agrocoi"));
                final Integer family = _familyTypes.get(rs.getString("family"));
                if (family == null) {
                    npc.set_family(0);
                } else {
                    npc.set_family(family.intValue());
                }
                final int agrofamily = rs.getInt("agrofamily");
                if ((npc.get_family() == 0) && (agrofamily == 1)) {
                    npc.set_agrofamily(0);
                } else {
                    npc.set_agrofamily(agrofamily);
                }
                npc.set_agrogfxid1(rs.getInt("agrogfxid1"));
                npc.set_agrogfxid2(rs.getInt("agrogfxid2"));
                npc.set_picupitem(rs.getBoolean("picupitem"));
                npc.set_digestitem(rs.getInt("digestitem"));
                npc.set_bravespeed(rs.getBoolean("bravespeed"));
                npc.set_hprinterval(rs.getInt("hprinterval"));
                npc.set_hpr(rs.getInt("hpr"));
                npc.set_mprinterval(rs.getInt("mprinterval"));
                npc.set_mpr(rs.getInt("mpr"));
                npc.set_teleport(rs.getBoolean("teleport"));
                npc.set_randomlevel(rs.getInt("randomlevel"));
                npc.set_randomhp(rs.getInt("randomhp"));
                npc.set_randommp(rs.getInt("randommp"));
                npc.set_randomac(rs.getInt("randomac"));
                npc.set_randomexp(rs.getInt("randomexp"));
                npc.set_randomlawful(rs.getInt("randomlawful"));
                npc.set_damagereduction(rs.getInt("damage_reduction"));
                npc.set_hard(rs.getBoolean("hard"));
                npc.set_doppel(rs.getBoolean("doppel"));
                npc.set_IsTU(rs.getBoolean("IsTU"));
                npc.set_IsErase(rs.getBoolean("IsErase"));
                npc.setBowActId(rs.getInt("bowActId"));
                npc.setKarma(rs.getInt("karma"));
                npc.setTransformId(rs.getInt("transform_id"));
                npc.setTransformGfxId(rs.getInt("transform_gfxid"));
                npc.setLightSize(rs.getInt("light_size"));
                npc.setAmountFixed(rs.getBoolean("amount_fixed"));
                npc.setChangeHead(rs.getBoolean("change_head"));
                npc.setCantResurrect(rs.getBoolean("cant_resurrect"));

                this.registerConstructorCache(npc.getImpl());

                _npcs.put(npcId, npc);
            }
        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /**
     * 加入独立执行CLASS位置
     * 
     * @param npcid
     * @param className
     * @return
     */
    private NpcExecutor addClass(final int npcid, final String className) {
        try {
            if (!className.equals("0")) {
                String newclass = className;
                String[] set = null;
                if (className.indexOf(" ") != -1) {
                    set = className.split(" ");
                    try {
                        newclass = set[0];
                    } catch (final Exception e) {
                    }
                }
                final StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("com.lineage.data.npc.");
                stringBuilder.append(newclass);

                final Class<?> cls = Class.forName(stringBuilder.toString());
                final NpcExecutor exe = (NpcExecutor) cls.getMethod("get")
                        .invoke(null);
                if (set != null) {
                    exe.set_set(set);
                }
                return exe;
            }

        } catch (final ClassNotFoundException e) {
            String error = "发生[NPC档案]错误, 检查档案是否存在:" + className + " NpcId:"
                    + npcid;
            _log.error(error);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 传回该编号NPC资料
     * 
     * @param id
     * @return
     */
    public L1Npc getTemplate(final int id) {
        return _npcs.get(id);
    }

    /**
     * 取回NPC名称
     * 
     * @param id
     * @return
     */
    public String getNpcName(final int id) {
        final L1Npc npcTemp = this.getTemplate(id);
        if (npcTemp == null) {
            _log.error("取回NPC名称错误 没有这个编号的NPC: " + id);
            return null;
        }
        return npcTemp.get_nameid();
    }

    /**
     * 依照NPCID取回新的L1NpcInstance资料
     * 
     * @param id
     *            NPCID
     * @return
     */
    public L1NpcInstance newNpcInstance(final int id) {
        try {
            final L1Npc npcTemp = this.getTemplate(id);
            if (npcTemp == null) {
                _log.error("依照NPCID取回新的L1NpcInstance资料发生异常(没有这编号的NPC): " + id);
                return null;
            }
            return this.newNpcInstance(npcTemp);

        } catch (final Exception e) {
            _log.error("NPCID:" + id + "/" + e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 依照NPC资料 取回新的L1NpcInstance资料
     * 
     * @param template
     *            NPC资料
     * @return
     */
    public L1NpcInstance newNpcInstance(final L1Npc template) {
        try {
            if (template == null) {
                _log.error("依照NPCID取回新的L1NpcInstance资料发生异常(NPC资料为空)");
                return null;
            }
            final Constructor<?> con = _constructorCache
                    .get(template.getImpl());
            return (L1NpcInstance) con.newInstance(new Object[] { template });

        } catch (final Exception e) {
            _log.error(
                    "NPCID:" + template.get_npcId() + "/"
                            + e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 建立NPC家族清单
     * 
     * @return
     */
    private static Map<String, Integer> buildFamily() {
        final Map<String, Integer> result = new HashMap<String, Integer>();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("select distinct(family) as family from npc WHERE NOT trim(family) =''");
            rs = pstm.executeQuery();
            int id = 1;
            while (rs.next()) {
                final String family = rs.getString("family");
                int oid = id++;
                if (family.equalsIgnoreCase("orc")) {
                    ORC = oid;
                }
                result.put(family, oid);
            }
        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        return result;
    }

    /**
     * 依照NPC名称传回NPCID
     * 
     * @param name
     *            依照NPC名称
     * @return
     */
    public int findNpcIdByName(final String name) {
        for (final L1Npc npc : _npcs.values()) {
            if (npc.get_name().equals(name)) {
                return npc.get_npcId();
            }
        }
        return 0;
    }

    /**
     * 依照NPC名称传回NPCID
     * 
     * @param name
     *            依照NPC名称
     * @return
     */
    public int findNpcIdByNameWithoutSpace(final String name) {
        for (final L1Npc npc : _npcs.values()) {
            if (npc.get_name().replace(" ", "").equals(name)) {
                return npc.get_npcId();
            }
        }
        return 0;
    }
}
