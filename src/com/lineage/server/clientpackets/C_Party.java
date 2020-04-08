package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.L1Party;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Party;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 要求队伍名单
 * 
 * @author daien
 * 
 */
public class C_Party extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_Party.class);

    /*
     * public C_Party() { }
     * 
     * public C_Party(final byte[] abyte0, final ClientExecutor client) {
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
            if (pc.isGhost()) {
                return;
            }

            final L1Party party = pc.getParty();
            if (pc.isInParty()) {
                pc.sendPackets(new S_Party("party", pc.getId(), party
                        .getLeader().getName(), party.getMembersNameList()));

            } else {
                // 425 您并没有参加任何队伍。
                pc.sendPackets(new S_ServerMessage(425));

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
