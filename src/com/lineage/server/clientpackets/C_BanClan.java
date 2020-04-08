package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ClanUpdate;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;

/**
 * 要求驱逐人物离开血盟
 * 
 * @author dexc
 * 
 */
public class C_BanClan extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_BanClan.class);

    /*
     * public C_BanClan() { }
     * 
     * public C_BanClan(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final String s = this.readS();

            final L1PcInstance pc = client.getActiveChar();
            final L1Clan clan = WorldClan.get().getClan(pc.getClanname());

            if (clan != null) {
                final String clanMemberName[] = clan.getAllMembers();
                int i;
                if (pc.isCrown() && (pc.getId() == clan.getLeaderId())) { // 君主、かつ、血盟主
                    for (i = 0; i < clanMemberName.length; i++) {
                        if (pc.getName().toLowerCase().equals(s.toLowerCase())) { // 君主自身
                            return;
                        }
                    }

                    final L1PcInstance tempPc = World.get().getPlayer(s);
                    if (tempPc != null) { // オンライン中
                        try {
                            if (tempPc.getClanid() == pc.getClanid()) { // 同じクラン
                                tempPc.setClanid(0);
                                tempPc.setClanname("");
                                tempPc.setClanRank(0);
                                tempPc.save(); // 资料存档
                                clan.delMemberName(tempPc.getName());
                                // 238 你被 %0 血盟驱逐了。
                                tempPc.sendPackets(new S_ServerMessage(238, pc
                                        .getClanname()));
                                // 被驱逐的血盟成员发送血盟数据更新包
                                tempPc.sendPackets(new S_ClanUpdate(tempPc
                                        .getId()));
                                // 取得在线的血盟成员 发送血盟数据更新包
                                for (L1PcInstance clanMembers : clan
                                        .getOnlineClanMember()) {
                                    clanMembers.sendPackets(new S_ClanUpdate(
                                            tempPc.getId()));// 在线上的血盟成员发送遭驱逐的血盟成员血盟数据更新
                                }
                                // 240 %0%o 被你从你的血盟驱逐了。
                                pc.sendPackets(new S_ServerMessage(240, tempPc
                                        .getName()));

                            } else {
                                // 109 没有叫%0的人。
                                pc.sendPackets(new S_ServerMessage(109, s));
                            }

                        } catch (final Exception e) {
                            _log.error(e.getLocalizedMessage(), e);
                        }

                    } else { // オフライン中
                        try {
                            final L1PcInstance restorePc = CharacterTable.get()
                                    .restoreCharacter(s);
                            if ((restorePc != null)
                                    && (restorePc.getClanid() == pc.getClanid())) { // 同じクラン
                                restorePc.setClanid(0);
                                restorePc.setClanname("");
                                restorePc.setClanRank(0);
                                restorePc.save(); // 资料存档
                                clan.delMemberName(restorePc.getName());
                                // 240 %0%o 被你从你的血盟驱逐了。
                                pc.sendPackets(new S_ServerMessage(240,
                                        restorePc.getName()));

                            } else {
                                // 109 没有叫%0的人。
                                pc.sendPackets(new S_ServerMessage(109, s));
                            }

                        } catch (final Exception e) {
                            _log.error(e.getLocalizedMessage(), e);
                        }
                    }

                } else {
                    // 518 血盟君主才可使用此命令。
                    pc.sendPackets(new S_ServerMessage(518));
                }
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
