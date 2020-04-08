package com.lineage.server.datatables.storage;

import com.lineage.server.templates.L1EmblemIcon;

/**
 * 盟辉图档纪录
 * 
 * @author dexc
 * 
 */
public interface ClanEmblemStorage {

    /**
     * 初始化载入
     */
    public void load();

    /**
     * 传回 Clan Icon
     */
    public L1EmblemIcon get(final int clan_id);

    /**
     * 增加虚拟血盟盟辉
     * 
     * @param clan_id
     * @param icon
     */
    public void add(final int clan_id, final byte[] icon);

    /**
     * 删除盟辉资料
     * 
     * @param clan_id
     */
    public void deleteIcon(final int clan_id);

    /**
     * 新建 ICON
     */
    public L1EmblemIcon storeClanIcon(final int clan_id, final byte[] emblemicon);

    /**
     * 更新 ICON
     */
    public void updateClanIcon(final L1EmblemIcon emblemIcon);
}
