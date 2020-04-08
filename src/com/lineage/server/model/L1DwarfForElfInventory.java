package com.lineage.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.DwarfForElfReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;

/**
 * 精灵仓库资料
 * 
 * @author dexc
 * 
 */
public class L1DwarfForElfInventory extends L1Inventory {

    private static final Log _log = LogFactory
            .getLog(L1DwarfForElfInventory.class);

    private static final long serialVersionUID = 1L;

    private final L1PcInstance _owner;

    public L1DwarfForElfInventory(final L1PcInstance owner) {
        this._owner = owner;
    }

    /**
     * 传回该精灵仓库资料
     */
//    @Override
//    public void loadItems() {
//        try {
//            final CopyOnWriteArrayList<L1ItemInstance> items = DwarfForElfReading
//                    .get().loadItems(this._owner.getAccountName());
//
//            if (items != null) {
//                _items = items;
//            }
//
//        } catch (final Exception e) {
//            _log.error(e.getLocalizedMessage(), e);
//        }
//    }
    /**
     * 进入游戏读取妖精仓库 hjx1000
     */
	@Override
	public void loadItems() {
		//新增仓库小退刷新物品
		       Connection con = null;
		        PreparedStatement pstm = null;
		        ResultSet rs = null;
		        try {
		            con = DatabaseFactory.get().getConnection();
		            pstm = con
		                    .prepareStatement("SELECT * FROM character_elf_warehouse WHERE account_name = ?");
		            pstm.setString(1, this._owner.getAccountName());

		            rs = pstm.executeQuery();

		            while (rs.next()) {
		                final L1ItemInstance item = new L1ItemInstance();
		                final int objectId = rs.getInt("id");
		                item.setId(objectId);
		                final L1Item itemTemplate = ItemTable.get()
		                        .getTemplate(rs.getInt("item_id"));
		                item.setItem(itemTemplate);
		                 item.setCount(rs.getInt("count"));
		                 item.setEquipped(false);
		                 item.setEnchantLevel(rs.getInt("enchantlvl"));
		                 item.setIdentified(rs.getInt("is_id") != 0 ? true : false);
		                 item.set_durability(rs.getInt("durability"));
		                 item.setChargeCount(rs.getInt("charge_count"));
		                 item.setRemainingTime(rs.getInt("remaining_time"));
		                 item.setLastUsed(rs.getTimestamp("last_used"));
		                 item.setBless(rs.getInt("bless"));
		                item.setAttrEnchantKind(rs.getInt("attr_enchant_kind"));
		                item.setAttrEnchantLevel(rs.getInt("attr_enchant_level"));
						item.setGamNo(rs.getString("gamno"));
		                this._items.add(item);
		                World.get().storeObject(item);
		            }

		        } catch (final SQLException e) {
					_log.error(e.getLocalizedMessage(), e);
		        } finally {
		            SQLUtil.close(rs);
		            SQLUtil.close(pstm);
		            SQLUtil.close(con);
		        }
		}
		//新增仓库小退刷新物品
    /**
     * 加入精灵仓库数据
     */
    @Override
    public void insertItem(final L1ItemInstance item) {
        if (item.getCount() <= 0) {
            return;
        }
        try {
            DwarfForElfReading.get().insertItem(this._owner.getAccountName(),
                    item);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 精灵仓库资料更新(物品数量)
     */
    @Override
    public void updateItem(final L1ItemInstance item) {
//        try {
//            DwarfForElfReading.get().updateItem(item);
//
//        } catch (final Exception e) {
//            _log.error(e.getLocalizedMessage(), e);
//        }
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("UPDATE character_elf_warehouse SET count = ? WHERE id = ?");
            pstm.setLong(1, item.getCount());
            pstm.setInt(2, item.getId());
            pstm.execute();
        } catch (final SQLException e) {
        	_log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /**
     * 精灵仓库物品资料删除
     */
    @Override
    public void deleteItem(final L1ItemInstance item) {
//        try {
//            _items.remove(item);
//            DwarfForElfReading.get().deleteItem(this._owner.getAccountName(),
//                    item);
//            World.get().removeObject(item);
//
//        } catch (final Exception e) {
//            _log.error(e.getLocalizedMessage(), e);
//        }
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("DELETE FROM character_elf_warehouse WHERE id = ?");
            pstm.setInt(1, item.getId());
            pstm.execute();
        } catch (final SQLException e) {
        	_log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }

        this._items.remove(this._items.indexOf(item));
    }
}
