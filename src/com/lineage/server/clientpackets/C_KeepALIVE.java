package com.lineage.server.clientpackets;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.ActionCodes;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.gametime.L1GameTimeClock;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_GameTime;

/**
 * 要求更新时间
 * 
 * @author dexc
 * 
 */
public class C_KeepALIVE extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_KeepALIVE.class);

    /*
     * public C_KeepALIVE() { }
     * 
     * public C_KeepALIVE(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            // this.read(decrypt);

            final L1PcInstance pc = client.getActiveChar();

            final int serverTime = L1GameTimeClock.getInstance().currentTime()
                    .getSeconds();
            pc.sendPackets(new S_GameTime(serverTime));

            if (pc.isPrivateShop()) {
                final int mapId = pc.getMapId();
                if ((mapId != 340) && (mapId != 350) && (mapId != 360)
                        && (mapId != 370) && (mapId != 4)) { //增加大陆可以摆商店 hjx1000
                    pc.getSellList().clear();
                    pc.getBuyList().clear();

                    pc.setPrivateShop(false);
                    pc.sendPacketsAll(new S_DoActionGFX(pc.getId(),
                            ActionCodes.ACTION_Idle));
                }
            }

            if (pc.get_mazu_time() != 0) {
                if (pc.is_mazu()) {
                    final Calendar cal = Calendar.getInstance();
                    long h_time = cal.getTimeInMillis() / 1000;// 换算为秒
                    if (h_time - pc.get_mazu_time() >= 2400) {// 2400秒 = 40分钟
                        pc.set_mazu_time(0);
                        pc.set_mazu(false);
                    }
                }
            }
            // GMCommands.getInstance().handleCommands(pc,
            // "spawn 45601 150 15");

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }

}
