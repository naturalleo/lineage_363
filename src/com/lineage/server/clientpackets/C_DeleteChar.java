package com.lineage.server.clientpackets;

import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_DeleteCharOK;
import com.lineage.server.world.WorldClan;

/**
 * 要求角色删除
 * 
 * @author daien
 * 
 */
public class C_DeleteChar extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_DeleteChar.class);

    /*
     * public C_DeleteChar() { }
     * 
     * public C_DeleteChar(final byte[] abyte0, final ClientExecutor client) {
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

            try {
                final L1PcInstance pc = CharacterTable.get().restoreCharacter(
                        name);
                if ((pc != null)
                        && (pc.getLevel() >= ConfigAlt.DELETE_CHARACTER_AFTER_LV)
                        && ConfigAlt.DELETE_CHARACTER_AFTER_7DAYS) {

                    if (pc.getType() < 32) {
                        if (pc.isCrown()) {
                            pc.setType(32);

                        } else if (pc.isKnight()) {
                            pc.setType(33);

                        } else if (pc.isElf()) {
                            pc.setType(34);

                        } else if (pc.isWizard()) {
                            pc.setType(35);

                        } else if (pc.isDarkelf()) {
                            pc.setType(36);

                        } else if (pc.isDragonKnight()) {
                            pc.setType(37);

                        } else if (pc.isIllusionist()) {
                            pc.setType(38);
                        }

                        final Timestamp deleteTime = new Timestamp(
                                System.currentTimeMillis() + 604800000); // 7日后
                        pc.setDeleteTime(deleteTime);
                        pc.save(); // 资料存档

                    } else {
                        if (pc.isCrown()) {
                            pc.setType(0);

                        } else if (pc.isKnight()) {
                            pc.setType(1);

                        } else if (pc.isElf()) {
                            pc.setType(2);

                        } else if (pc.isWizard()) {
                            pc.setType(3);

                        } else if (pc.isDarkelf()) {
                            pc.setType(4);

                        } else if (pc.isDragonKnight()) {
                            pc.setType(5);

                        } else if (pc.isIllusionist()) {
                            pc.setType(6);
                        }

                        pc.setDeleteTime(null);
                        pc.save(); // 资料存档
                    }
                    client.out().encrypt(
                            new S_DeleteCharOK(
                                    S_DeleteCharOK.DELETE_CHAR_AFTER_7DAYS));
                    return;
                }

                if (pc != null) {
                    final L1Clan clan = WorldClan.get().getClan(
                            pc.getClanname());
                    if (clan != null) {
                        clan.delMemberName(name);
                    }
                }
                // 已创人物数量
                int countCharacters = client.getAccount().get_countCharacters();
                client.getAccount().set_countCharacters(countCharacters - 1);

                // 移出已用名称清单
                CharObjidTable.get().charRemove(name);
                // 删除人物
                CharacterTable.get().deleteCharacter(client.getAccountName(),
                        name);

            } catch (final Exception e) {
                client.close();
                return;
            }
            client.out().encrypt(
                    new S_DeleteCharOK(S_DeleteCharOK.DELETE_CHAR_NOW));

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
