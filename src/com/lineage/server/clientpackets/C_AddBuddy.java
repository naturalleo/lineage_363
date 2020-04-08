package com.lineage.server.clientpackets;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.lock.BuddyReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1BuddyTmp;

/**
 * 要求新增好友
 * 
 * @author dexc
 * 
 */
public class C_AddBuddy extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_AddBuddy.class);

    /*
     * public C_AddBuddy() { }
     * 
     * public C_AddBuddy(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            read(decrypt);

            final String charName = this.readS().toLowerCase();

            final L1PcInstance pc = client.getActiveChar();

            // 取回该人物好友清单
            final ArrayList<L1BuddyTmp> list = BuddyReading.get().userBuddy(
                    pc.getId());

            if (list != null) {
                if (charName.equalsIgnoreCase(pc.getName())) {
                    return;
                }

                for (final L1BuddyTmp buddyTmp : list) {
                    if (charName.equalsIgnoreCase(buddyTmp.get_buddy_name())) {
                        return;
                    }
                }
            }

            int objid = CharObjidTable.get().charObjid(charName);
            if (objid != 0) {
                String name = CharObjidTable.get().isChar(objid);
                BuddyReading.get().addBuddy(pc.getId(), objid, name);
                return;
            }

            // 没有叫%0的人。
            pc.sendPackets(new S_ServerMessage(109, charName));

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            over();
        }
    }

    @Override
    public String getType() {
        return getClass().getSimpleName();
    }
}
