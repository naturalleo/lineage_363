package com.lineage.server.model;

import java.util.TimerTask;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 计时物件使用时间轴
 * 
 * @author dexc
 * 
 */
public class L1EquipmentTimer extends TimerTask {

    private final L1PcInstance _pc;// 拥有者

    private final L1ItemInstance _item;// 计时物件

    public L1EquipmentTimer(final L1PcInstance pc, final L1ItemInstance item) {
        this._pc = pc;
        this._item = item;
    }

    @Override
    public void run() {
        if ((this._item.getRemainingTime() - 1) > 0) {
            this._item.setRemainingTime(this._item.getRemainingTime() - 1);
            this._pc.getInventory().updateItem(this._item,
                    L1PcInventory.COL_REMAINING_TIME);
            if (this._pc.getOnlineStatus() == 0) {
                this.cancel();
            }

        } else {
            this._pc.getInventory().removeItem(this._item, 1);
            this.cancel();
        }
    }
}
