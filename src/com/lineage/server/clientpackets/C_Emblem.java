package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.ClanEmblemReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Emblem;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1EmblemIcon;
import com.lineage.server.world.World;

/**
 * 要求上传盟徽
 * 
 * @author daien
 * 
 */
public class C_Emblem extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_Emblem.class);

    /*
     * public C_Emblem() { }
     * 
     * public C_Emblem(final byte[] abyte0, final ClientExecutor client) {
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

            final int clan_id = pc.getClanid();
            // 人物具有血盟
            if (clan_id != 0) {
                if (pc.getClan().getLeaderId() != pc.getId()) {
                    // 219 \f1王子或公主才能上传徽章。
                    pc.sendPackets(new S_ServerMessage(219));
                    return;
                }

                final byte[] iconByte = this.readByte();
                // System.out.println(iconByte + " length:" + iconByte.length +
                // " clan_id:" + clan_id);
                L1EmblemIcon emblemIcon = ClanEmblemReading.get().get(clan_id);

                if (emblemIcon != null) {
                    // System.out.println("update");
                    emblemIcon.set_clanIcon(iconByte);
                    emblemIcon.set_update(emblemIcon.get_update() + 1);
                    ClanEmblemReading.get().updateClanIcon(emblemIcon);

                } else {
                    // System.out.println("NEW");
                    emblemIcon = ClanEmblemReading.get().storeClanIcon(clan_id,
                            iconByte);
                }
                // player.sendPackets(new S_Emblem(emblemIcon));
//                World.get().broadcastPacketToAll(new S_Emblem(emblemIcon));
                //修正盟辉显示不正确 hjx1000
                World.get().broadcastPacketToAll(new S_Emblem(pc.getClanid()));
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
