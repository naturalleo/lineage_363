package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Pledge;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.WorldClan;

/**
 * 要求查询血盟成员
 * 
 * @author daien
 * 
 */
public class C_Pledge extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_Pledge.class);

    /*
     * public C_Pledge() { }
     * 
     * public C_Pledge(final byte[] abyte0, final ClientExecutor client) {
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

            if (pc.getClanid() > 0) {
                final L1Clan clan = WorldClan.get().getClan(pc.getClanname());
                // 盟主
                if (pc.isCrown() && (pc.getId() == clan.getLeaderId())) {
                    // System.out.println("盟主查询");
                    pc.sendPackets(new S_Pledge(pc.getId(), clan.getClanName(),
                            clan.getOnlineMembersFPWithRank(), clan
                                    .getAllMembersFPWithRank()));

                    // 成员
                } else {
                    // System.out.println("成员查询");
                    pc.sendPackets(new S_Pledge(pc.getId(), clan.getClanName(),
                            clan.getOnlineMembersFP()));
                }

            } else {
                // 1064 不属于血盟。
                pc.sendPackets(new S_ServerMessage(1064));
            }

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
