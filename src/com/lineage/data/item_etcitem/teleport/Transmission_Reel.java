package com.lineage.data.item_etcitem.teleport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * <font color=#00800>傲慢之塔传送符(11F)40289</font><BR>
 * Sealed TOI Teleport Charm, 11F<BR>
 * <font color=#00800>傲慢之塔传送符(21F)40290</font><BR>
 * Sealed TOI Teleport Charm, 21F<BR>
 * <font color=#00800>傲慢之塔传送符(31F)40291</font><BR>
 * Sealed TOI Teleport Charm, 31F<BR>
 * <font color=#00800>傲慢之塔传送符(41F)40292</font><BR>
 * Sealed TOI Teleport Charm, 41F<BR>
 * <font color=#00800>傲慢之塔传送符(51F)40293</font><BR>
 * Sealed TOI Teleport Charm, 51F<BR>
 * <font color=#00800>傲慢之塔传送符(61F)40294</font><BR>
 * Sealed TOI Teleport Charm, 61F<BR>
 * <font color=#00800>傲慢之塔传送符(71F)40295</font><BR>
 * Sealed TOI Teleport Charm, 71F<BR>
 * <font color=#00800>傲慢之塔传送符(81F)40296</font><BR>
 * Sealed TOI Teleport Charm, 81F<BR>
 * <font color=#00800>傲慢之塔传送符(91F)40297</font><BR>
 * Sealed TOI Teleport Charm, 91F<BR>
 * 
 * @see 取得传送前往指令楼层的传送符
 * @author dexc
 * 
 */
public class Transmission_Reel extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(Transmission_Reel.class);

    /**
	 *
	 */
    private Transmission_Reel() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Transmission_Reel();
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
            boolean isTeleport = false;

            if ((pc.getX() >= 33914) && (pc.getX() <= 33976)
                    && (pc.getY() >= 33329) && (pc.getY() <= 33375)
                    && (pc.getMapId() == 4)) {
                isTeleport = true;

            } else {
                pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
                // 解除传送锁定
                pc.sendPackets(new S_Paralysis(
                        S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
            }

            int locx = 0;
            int locy = 0;
            short mapid = 0;

            final String nameId = item.getName();
            if (nameId.equals("$2400")) {// 傲慢之塔传送符(11F)
                locx = 32631;
                locy = 32935;
                mapid = 111;

            } else if (nameId.equals("$2678")) {// 傲慢之塔传送符(51F)
                locx = 32669;
                locy = 32814;
                mapid = 151;

            } else if (nameId.equals("$2401")) {// 傲慢之塔传送符(21F)
                locx = 32631;
                locy = 32935;
                mapid = 121;

            } else if (nameId.equals("$2679")) {// 傲慢之塔传送符(61F)
                locx = 32669;
                locy = 32814;
                mapid = 161;

            } else if (nameId.equals("$2402")) {// 傲慢之塔传送符(31F)
                locx = 32631;
                locy = 32935;
                mapid = 131;

            } else if (nameId.equals("$2680")) {// 傲慢之塔传送符(71F)
                locx = 32669;
                locy = 32814;
                mapid = 171;

            } else if (nameId.equals("$2403")) {// 傲慢之塔传送符(41F)
                locx = 32631;
                locy = 32935;
                mapid = 141;

            } else if (nameId.equals("$2681")) {// 傲慢之塔传送符(81F)
                locx = 32669;
                locy = 32814;
                mapid = 181;

            } else if (nameId.equals("$2682")) {// 傲慢之塔传送符(91F)
                locx = 32669;
                locy = 32814;
                mapid = 191;
            }

            if (isTeleport) {
                L1Teleport.teleport(pc, locx, locy, mapid, 5, true);

            } else {
                pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
                // 解除传送锁定
                pc.sendPackets(new S_Paralysis(
                        S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
