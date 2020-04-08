package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.DungeonTable;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 要求座标传送
 * 
 * @author daien
 * 
 */
public class C_EnterPortal extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_EnterPortal.class);

    /*
     * public C_EnterPortal() { }
     * 
     * public C_EnterPortal(final byte[] abyte0, final ClientExecutor client) {
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

            final int locx = this.readH();
            final int locy = this.readH();

            if (pc.isTeleport()) {
                return;
            }
            // 执行座标移动
            DungeonTable.get().dg(locx, locy, pc.getMap().getId(), pc);

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
