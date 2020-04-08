package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigOther;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CharTitle;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;

/**
 * 要求角色建立封号
 * 
 * @author daien
 * 
 */
public class C_Title extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_Title.class);

    /*
     * public C_Title() { }
     * 
     * public C_Title(final byte[] abyte0, final ClientExecutor client) {
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
            final String charName = this.readS();

            final StringBuilder title = new StringBuilder();
            title.append(this.readS());
            // final String title = this.readS();

            if (charName.isEmpty() || title.length() <= 0) {
                // \f1请以如下的格式输入。: "/title \f0角色名称 角色封号\f1"
                pc.sendPackets(new S_ServerMessage(196));
                return;
            }

            final L1PcInstance target = World.get().getPlayer(charName);
            if (target == null) {
                return;
            }

            // 有师傅 尚未 毕业
            /*
             * if (target.get_other().get_apprentice_objid() != 0) { if
             * (!target.get_other().get_award()) { return; } }
             */

            if (pc.isGm()) {
                this.changeTitle(target, title);
                return;
            }

            if (this.isClanLeader(pc)) { // 血盟主
                if (pc.getId() == target.getId()) { // 自分
                    if (pc.getLevel() < 10) {
                        // \f1加入血盟之后等级10以上才可使用封号。
                        pc.sendPackets(new S_ServerMessage(197));
                        return;
                    }
                    this.changeTitle(pc, title);

                } else { // 他人
                    if (pc.getClanid() != target.getClanid()) {
                        // \f1除了王族以外的人，不能够授与头衔给其他人。
                        pc.sendPackets(new S_ServerMessage(199));
                        return;
                    }
                    if (target.getLevel() < 10) {
                        // \f1%0的等级还不到10级，因此无法给封号。
                        pc.sendPackets(new S_ServerMessage(202, charName));
                        return;
                    }
                    this.changeTitle(target, title);
                    final L1Clan clan = WorldClan.get().getClan(
                            pc.getClanname());
                    if (clan != null) {
                        for (final L1PcInstance clanPc : clan
                                .getOnlineClanMember()) {
                            // \f1%0%s 赋予%1 '%2'的封号。
                            clanPc.sendPackets(new S_ServerMessage(203, pc
                                    .getName(), charName, title.toString()));
                        }
                    }
                }
            } else {
                if (pc.getId() == target.getId()) { // 自分
                    if (!ConfigOther.CLANTITLE) {
                        if (pc.getClanid() != 0) {
                            // \f1王子或公主才可给血盟员封号。
                            pc.sendPackets(new S_ServerMessage(198));
                            return;
                        }
                    }
                    if (target.getLevel() < 40) {
                        // \f1若等级40以上，没有加入血盟也可拥有封号。
                        pc.sendPackets(new S_ServerMessage(200));
                        return;
                    }
                    changeTitle(pc, title);

                } else { // 他人
                    if (pc.isCrown()) { // 连合に所属した君主
                        if (pc.getClanid() == target.getClanid()) {
                            // \f1%0%d不是你的血盟成员。
                            pc.sendPackets(new S_ServerMessage(201, pc
                                    .getClanname()));
                            return;
                        }
                    }
                }
            }

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    /**
     * 执行封号变更
     * 
     * @param pc
     * @param title
     */
    private void changeTitle(final L1PcInstance pc, final StringBuilder title) {
        final int objectId = pc.getId();
        pc.setTitle(title.toString());
        pc.sendPacketsAll(new S_CharTitle(objectId, title));
        try {
            pc.save();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private boolean isClanLeader(final L1PcInstance pc) {
        boolean isClanLeader = false;
        if (pc.getClanid() != 0) { // クラン所属
            final L1Clan clan = WorldClan.get().getClan(pc.getClanname());
            if (clan != null) {
                if (pc.isCrown() && (pc.getId() == clan.getLeaderId())) { // 君主、かつ、血盟主
                    isClanLeader = true;
                }
            }
        }
        return isClanLeader;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
