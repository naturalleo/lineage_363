package com.lineage.server.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PinkName;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.types.Point;

/**
 * 红名设置
 * 
 * @author DaiEn
 * 
 */
public class L1PinkName {

    public static final Log _log = LogFactory.getLog(L1PinkName.class);

    private L1PinkName() {
    }

    private static class PinkNameTimer implements Runnable {

        private L1PcInstance _attacker = null;
        private Point _point = null;
        private int _mapid = -1;

        public PinkNameTimer(final L1PcInstance attacker) {
            _attacker = attacker;
            _point = new Point(attacker.getX(), attacker.getY());
            _mapid = attacker.getMapId();
        }

        @Override
        public void run() {
            for (int i = 0; i < 180; i++) {
                try {
                    Thread.sleep(1000);

                } catch (final Exception e) {
                    _log.error(e.getLocalizedMessage(), e);
                }
                if (_mapid != _attacker.getMapId()) {
                    break;
                }
                if (!_attacker.getLocation().isInScreen(_point)) {
                    break;
                }
                // 红名者死亡中止
                if (_attacker.isDead()) {
                    break;
                }
            }
            stopPinkName();
        }

        private void stopPinkName() {
            _attacker.sendPacketsAll(new S_PinkName(_attacker.getId(), 0));
            _attacker.setPinkName(false);
        }
    }

    /**
     * 红名设置判断
     * 
     * @param tgpc
     *            被攻击者
     * @param atk
     *            攻击者
     */
    public static void onAction(final L1PcInstance tgpc, final L1Character atk) {
        // 物件为空
        if ((tgpc == null) || (atk == null)) {
            // _log.error("物件为空");
            return;
        }
        // 攻击者非人物
        if (!(atk instanceof L1PcInstance)) {
            // _log.error("攻击者非人物");
            return;
        }

        final L1PcInstance attacker = (L1PcInstance) atk;
        // 自己
        if (tgpc.getId() == attacker.getId()) {
            // _log.error("攻击者自己");
            return;
        }

        // 是决斗对象
        if (attacker.getFightId() == tgpc.getId()) {
            // _log.error("是决斗对象");
            return;
        }

        // 被攻击者在非一般区域
        if (tgpc.getZoneType() != 0) {
            // _log.error("被攻击者在非一般区域");
            return;
        }

        // 攻击者在非一般区域
        if (attacker.getZoneType() != 0) {
            // _log.error("攻击者在非一般区域");
            return;
        }

        boolean isNowWar = false;
        final int castleId = L1CastleLocation.getCastleIdByArea(tgpc);
        if (castleId != 0) {// 战争旗内
            isNowWar = ServerWarExecutor.get().isNowWar(castleId);
        }

        // 战争中
        if (isNowWar == true) {
            // _log.error("战争中");
            return;
        }

        // 被攻击者正义质小于0
//        if (tgpc.getLawful() < 0) {
//            // _log.error("被攻击者正义质小于0");
//            return;
//        }      //修改只要首先攻击人就必须粉名。无论对像是不是红名

        // 被攻击者已在红名状态
        if (tgpc.isPinkName()) {
            // _log.error("被攻击者已在红名状态");
            return;
        }

        attacker.sendPacketsAll(new S_PinkName(attacker.getId(), 180));

        if (!attacker.isPinkName()) {
            attacker.setPinkName(true);

            final PinkNameTimer pink = new PinkNameTimer(attacker);
            GeneralThreadPool.get().execute(pink);
        }
    }
}
