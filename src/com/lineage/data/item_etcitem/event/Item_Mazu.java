package com.lineage.data.item_etcitem.event;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 妈祖祝福<BR>
 * 
 * DELETE FROM `etcitem` WHERE `item_id`='49532'; INSERT INTO `etcitem` VALUES
 * (49532, '虔诚祝福', 'event.Item_Mazu', '虔诚祝福', 'other', 'normal', 'gemstone', 0,
 * 2563, 3963, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0);
 * 
 * @author loli
 * 
 */
public class Item_Mazu extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(Item_Mazu.class);

    private Item_Mazu() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Item_Mazu();
    }

    @Override
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        try {
            // 例外状况:物件为空
            if (item == null) {
                return;
            }
            // 例外状况:人物为空
            if (pc == null) {
                return;
            }
            if (pc.is_mazu()) {
                final Calendar cal = Calendar.getInstance();
                long h_time = cal.getTimeInMillis() / 1000;// 换算为秒
                long o_time = h_time - pc.get_mazu_time();
                if (o_time <= 2400) {// 2400秒 = 40分钟
                    pc.sendPackets(new S_ServerMessage("\\fV妈祖祝福效果时间尚有"
                            + o_time + "秒"));
                    return;
                }
            }

            pc.getInventory().removeItem(item, 1);

            pc.set_mazu(true);
            // 妈祖祝福
            pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7321));

            final Calendar cal = Calendar.getInstance();
            long h_time = cal.getTimeInMillis() / 1000;// 换算为秒
            pc.set_mazu_time(h_time);// 纪录时间

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
