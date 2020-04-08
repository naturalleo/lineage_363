package com.lineage.data.item_etcitem.shop;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 密码修改卷轴
 * 
 * 
 * DELETE FROM `etcitem` WHERE `item_id`='49538'; INSERT INTO `etcitem` VALUES
 * ('49538', '密码修改卷轴', 'shop.ResetPassword', '密码修改卷轴', 'other', 'normal',
 * 'paper', '0', '205', '3963', '0', '1', '0', '0', '0', '0', '0', '1', '0',
 * '0', '0', '0', '0', '0', '0');
 */
public class ResetPassword extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(ResetPassword.class);

    public static final Random _random = new Random();

    /**
	 *
	 */
    private ResetPassword() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new ResetPassword();
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
            // 例外状况:物件为空
            if (item == null) {
                return;
            }
            // 例外状况:人物为空
            if (pc == null) {
                return;
            }

            pc.repass(1);

            //pc.getInventory().removeItem(item, 1);

            pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "y_pass_01",
                    new String[] { "请输入您的旧密码" }));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
