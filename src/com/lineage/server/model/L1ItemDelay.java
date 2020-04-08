package com.lineage.server.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1EtcItem;
import com.lineage.server.thread.GeneralThreadPool;

/**
 * 物件使用延迟
 * 
 * @author daien
 * 
 */
public class L1ItemDelay {

    private static final Log _log = LogFactory.getLog(L1ItemDelay.class);

    /**
     * 500:武器禁止使用
     */
    public static final int WEAPON = 500;// 500:武器禁止使用

    /**
     * 501:防具禁止使用
     */
    public static final int ARMOR = 501;// 501:防具禁止使用

    /**
     * 502:道具禁止使用
     */
    public static final int ITEM = 502;// 502:道具禁止使用

    /**
     * 503:变身禁止使用
     */
    public static final int POLY = 503;// 503:变身禁止使用

    private L1ItemDelay() {
    }

    /**
     * 建立物件使用延迟计时器
     * 
     * @author daien
     * 
     */
    static class ItemDelayTimer implements Runnable {

        private int _delayId;

        private int _delayTime;

        private L1Character _cha;

        public ItemDelayTimer(final L1Character cha, final int id,
                final int time) {
            this._cha = cha;
            this._delayId = id;
            this._delayTime = time;
        }

        @Override
        public void run() {
            this.stopDelayTimer(this._delayId);
        }

        public int get_delayTime() {
            return _delayTime;
        }

        /**
         * 停止该物件使用延迟
         * 
         * @param delayId
         */
        public void stopDelayTimer(final int delayId) {
            this._cha.removeItemDelay(delayId);
        }
    }

    /**
     * 建立物件使用延迟
     * 
     * @param pc
     *            执行人物
     * @param delayId
     *            延迟编号<BR>
     *            500:武器禁止使用<BR>
     *            501:防具禁止使用<BR>
     *            502:道具禁止使用<BR>
     *            503:变身禁止使用<BR>
     *            504:禁止移动<BR>
     * 
     * @param delayTime
     *            延迟毫秒
     */
    public static void onItemUse(final L1PcInstance pc, int delayId,
            int delayTime) {
        try {
            if ((delayId != 0) && (delayTime != 0)) {
                final ItemDelayTimer timer = new ItemDelayTimer(pc, delayId,
                        delayTime);

                pc.addItemDelay(delayId, timer);
                GeneralThreadPool.get().schedule(timer, delayTime);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 建立物件使用延迟
     * 
     * @param client
     *            执行连线端
     * @param item
     *            物件
     */
    public static void onItemUse(final ClientExecutor client,
            final L1ItemInstance item) {
        try {
            final L1PcInstance pc = client.getActiveChar();
            if (pc != null) {
                onItemUse(pc, item);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 建立物件使用延迟
     * 
     * @param pc
     *            执行人物
     * @param item
     *            物件
     */
    public static void onItemUse(final L1PcInstance pc,
            final L1ItemInstance item) {
        try {
            int delayId = 0;
            int delayTime = 0;
            switch (item.getItem().getType2()) {
                case 0:
                    // 种别：道具
                    delayId = ((L1EtcItem) item.getItem()).get_delayid();
                    delayTime = ((L1EtcItem) item.getItem()).get_delaytime();
                    break;

                case 1:
                    // 种别：武器
                    return;

                case 2:
                    // 种别：防具
                    switch (item.getItemId()) {
                        case 20077: // 隐身斗篷
                        case 120077: // 隐身斗篷
                        case 20062: // 炎魔的血光斗篷
                            // 装备使用中 并且 非隐身状态
                            if (item.isEquipped() && !pc.isInvisble()) {
                                pc.beginInvisTimer();
                            }
                            break;

                        default:// 其他道具
                            return;
                    }
                    break;
            }

            if ((delayId != 0) && (delayTime != 0)) {
                final ItemDelayTimer timer = new ItemDelayTimer(pc, delayId,
                        delayTime);

                pc.addItemDelay(delayId, timer);
                GeneralThreadPool.get().schedule(timer, delayTime);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
