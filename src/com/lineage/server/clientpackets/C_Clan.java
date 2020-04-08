package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.ClanReading;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Emblem;

/**
 * 要求更新盟辉
 * 
 * @author DaiEn
 * 
 */
public class C_Clan extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_Clan.class);

    /*
     * public C_Clan() { }
     * 
     * public C_Clan(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final L1PcInstance pc = client.getActiveChar();

            if (pc.isGhost()) { // 鬼魂模式
                return;
            }

            if (pc.isDead()) { // 死亡
                return;
            }

            if (pc.isTeleport()) { // 传送中
                return;
            }

            final int clanId = this.readD();

            final L1Clan clan = ClanReading.get().getTemplate(clanId);

            if (clan == null) {
                return;
            }

//            final L1EmblemIcon emblemIcon = ClanEmblemReading.get().get(
//                    clan.getClanId());
//            //System.out.println("盟标===:" + emblemIcon);
//            if (emblemIcon != null) {
//                pc.sendPackets(new S_Emblem(emblemIcon));
//            }
            //修正盟辉显示不正常 hjx1000
            pc.sendPackets(new S_Emblem(clan.getClanId()));

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
