package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;

/**
 * 要求座标异常重整
 * 
 * @author dexc
 * 
 */
public class C_UnLock extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_UnLock.class);

    /*
     * public C_UnLock() { }
     * 
     * public C_UnLock(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            read(decrypt);
            // int i = 0;
//            int type = readC();
//            // System.out.println("要求座标异常重整:"+type);
//            final L1PcInstance pc = client.getActiveChar();
//            if (type == 127) {
//                final int oleLocx = pc.getX();
//                final int oleLocy = pc.getY();
//                // 设置原始座标
//                pc.setOleLocX(oleLocx);
//                pc.setOleLocY(oleLocy);
//                // 送出座标异常
//                pc.sendPackets(new S_Lock());
//
//            } else {
//                pc.sendPackets(new S_Paralysis(
//                        S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
//                pc.setTeleportX(pc.getOleLocX());
//                pc.setTeleportY(pc.getOleLocY());
//                pc.setTeleportMapId(pc.getMapId());
//                pc.setTeleportHeading(pc.getHeading());
//                // 传送回原始座标
//                System.out.println("===传送回原始座标c===");
//                Teleportation.teleportation(pc);
//            }

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
