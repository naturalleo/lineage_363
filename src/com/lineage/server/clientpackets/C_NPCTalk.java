package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.NpcActionTable;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.npc.L1NpcHtml;
import com.lineage.server.model.npc.action.L1NpcAction;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.world.World;

/**
 * 要求物件对话视窗
 * 
 * @author daien
 * 
 */
public class C_NPCTalk extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_NPCTalk.class);

    /*
     * public C_NPCTalk() { }
     * 
     * public C_NPCTalk(final byte[] abyte0, final ClientExecutor client) {
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

            if (pc.isDead()) { // 死亡
                return;
            }

            if (pc.isTeleport()) { // 传送中
                return;
            }

            if (pc.isPrivateShop()) { // 商店村模式
                return;
            }

            final int objid = this.readD();

            final L1Object obj = World.get().findObject(objid);

            // 清空全部买入资料
            pc.get_otherList().clear();
            // 解除GM管理状态
            pc.get_other().set_gmHtml(null);

            if ((obj != null) && (pc != null)) {
                // 纪录对话NPC OBJID
                pc.setTempID(objid);

                if (obj instanceof L1NpcInstance) {
                    final L1NpcInstance npc = (L1NpcInstance) obj;
                    // 具有执行项
                    if (npc.TALK != null) {
                        npc.TALK.talk(pc, npc);
                        return;
                    }
                }

                final L1NpcAction action = NpcActionTable.getInstance().get(pc,
                        obj);
                if (action != null) {
                    final L1NpcHtml html = action.execute("", pc, obj,
                            new byte[0]);
                    if (html != null) {
                        pc.sendPackets(new S_NPCTalkReturn(obj.getId(), html));
                    }
                    return;
                }
                obj.onTalkAction(pc);

            } else {
                _log.error("指定的OBJID不存在: " + objid);
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
