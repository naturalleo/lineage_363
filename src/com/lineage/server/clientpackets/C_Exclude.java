package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.L1ExcludingList;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 要求使用拒绝名单(开启指定人物讯息)
 * 
 * @author daien
 * 
 */
public class C_Exclude extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_Exclude.class);

    /*
     * public C_Exclude() { }
     * 
     * public C_Exclude(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final String name = this.readS();
            if (name.isEmpty()) {
                return;
            }

            final L1PcInstance pc = client.getActiveChar();

            final L1ExcludingList exList = pc.getExcludingList();
            if (exList.isFull()) {
                // 472 被拒绝的玩家太多。
                pc.sendPackets(new S_ServerMessage(472));
                return;
            }

            if (exList.contains(name)) {
                final String temp = exList.remove(name);
                pc.sendPackets(new S_PacketBox(S_PacketBox.REM_EXCLUDE, temp));

            } else {
                exList.add(name);
                pc.sendPackets(new S_PacketBox(S_PacketBox.ADD_EXCLUDE, name));
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
