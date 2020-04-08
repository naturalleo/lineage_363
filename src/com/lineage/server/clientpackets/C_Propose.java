package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.FaceToFace;

/**
 * 要求婚姻的执行
 * 
 * @author daien
 * 
 */
public class C_Propose extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_Propose.class);

    /*
     * public C_Propose() { }
     * 
     * public C_Propose(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final int c = this.readC();

            final L1PcInstance pc = client.getActiveChar();
            if (c == 0) { // /propose（/プロポーズ）
                if (pc.isGhost()) {
                    return;
                }
                final L1PcInstance target = FaceToFace.faceToFace(pc);
                if (target != null) {
                    if (pc.getPartnerId() != 0) {
                        pc.sendPackets(new S_ServerMessage(657)); // \f1あなたはすでに结婚しています。
                        return;
                    }
                    if (target.getPartnerId() != 0) {
                        pc.sendPackets(new S_ServerMessage(658)); // \f1その相手はすでに结婚しています。
                        return;
                    }
                    if (pc.get_sex() == target.get_sex()) {
                        pc.sendPackets(new S_ServerMessage(661)); // \f1结婚相手は异性でなければなりません。
                        return;
                    }
                    if ((pc.getX() >= 33974) && (pc.getX() <= 33976)
                            && (pc.getY() >= 33362) && (pc.getY() <= 33365)
                            && (pc.getMapId() == 4) && (target.getX() >= 33974)
                            && (target.getX() <= 33976)
                            && (target.getY() >= 33362)
                            && (target.getY() <= 33365)
                            && (target.getMapId() == 4)) {
                        target.setTempID(pc.getId()); // 相手のオブジェクトIDを保存しておく
                        target.sendPackets(new S_Message_YN(654, pc.getName())); // %0%sあなたと结婚したがっています。%0と结婚しますか？（Y/N）
                    }
                }
            } else if (c == 1) { // /divorce（/离婚）
                if (pc.getPartnerId() == 0) {
                    pc.sendPackets(new S_ServerMessage(662)); // \f1あなたは结婚していません。
                    return;
                }
                pc.sendPackets(new S_Message_YN(653)); // 离婚をするとリングは消えてしまいます。离婚を望みますか？（Y/N）
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
