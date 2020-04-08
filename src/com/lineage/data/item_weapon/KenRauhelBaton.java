package com.lineage.data.item_weapon;

import static com.lineage.server.model.skill.L1SkillId.I_LV30;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>274 反王肯恩的权杖</font><BR>
 * 
 */
public class KenRauhelBaton extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(KenRauhelBaton.class);

    /**
	 *
	 */
    private KenRauhelBaton() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new KenRauhelBaton();
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
            switch (data[0]) {
                case 0:// 解除装备
                    if (pc.hasSkillEffect(I_LV30)) {
                        pc.removeSkillEffect(I_LV30);
                    }
                    break;

                case 1:// 装备
                    pc.setSkillEffect(I_LV30, -1);
                    break;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
