package com.lineage.data.item_etcitem.quest;

import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 49312 时空之瓮
 */
public class TimeBox extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(TimeBox.class);

    /**
	 *
	 */
    private TimeBox() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new TimeBox();
    }

    /**
     * 道具物件执行
     * 
     * @param data
     *            参数
     * @param pc
     *            执行者
     * @param item
     *            物件
     */
    @Override
    public void execute(final int[] data, final L1PcInstance pc,
            final L1ItemInstance item) {
        try {
            // 取得道具(时空之玉 * 2)
            CreateNewItem.createNewItem(pc, 49313, 2);
            // 设置延迟使用机制
            final Timestamp ts = new Timestamp(System.currentTimeMillis());
            item.setLastUsed(ts);
            pc.getInventory().updateItem(item, L1PcInventory.COL_DELAY_EFFECT);
            pc.getInventory().saveItem(item, L1PcInventory.COL_DELAY_EFFECT);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
