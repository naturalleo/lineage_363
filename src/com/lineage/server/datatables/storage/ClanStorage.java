package com.lineage.server.datatables.storage;

import java.util.Map;

import com.lineage.server.model.L1Clan;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 血盟资料
 * 
 * @author dexc
 * 
 */
public interface ClanStorage {

    /**
     * 预先加载血盟资料
     */
    public void load();

    /**
     * 加入虚拟血盟
     * 
     * @param integer
     * @param l1Clan
     */
    public void addDeClan(Integer integer, L1Clan l1Clan);

    /**
     * 建立血盟资料
     * 
     * @param player
     * @param clan_name
     * @return
     */
    public L1Clan createClan(final L1PcInstance player, final String clan_name);

    /**
     * 更新血盟资料
     * 
     * @param clan
     */
    public void updateClan(final L1Clan clan);

    /**
     * 删除血盟资料
     * 
     * @param clan_name
     */
    public void deleteClan(final String clan_name);

    /**
     * 指定血盟资料
     * 
     * @param clan_id
     * @return
     */
    public L1Clan getTemplate(final int clan_id);

    /**
     * 全部血盟资料
     * 
     * @return
     */
    public Map<Integer, L1Clan> get_clans();
    
    /**
     * 血盟UI hjx1000
     * @param paramL1Clan
     */
    public abstract void updateClanOnlineMaxUser(L1Clan paramL1Clan);//血盟UI hjx1000

}
