package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.CharacterConfigReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Config;

/**
 * 要求纪录快速键
 * 
 * @author daien
 * 
 */
public class C_CharcterConfig extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_CharcterConfig.class);

    /*
     * public C_CharcterConfig() { }
     * 
     * public C_CharcterConfig(final byte[] abyte0, final ClientExecutor client)
     * { super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final L1PcInstance pc = client.getActiveChar();
            // System.out.println(pc);
            if (pc == null) {
                return;
            }
            final int objid = pc.getId();

            final int length = this.readD() - 3;
            final byte data[] = this.readByte();

            final L1Config config = CharacterConfigReading.get().get(objid);

            // 新建
            if (config == null) {
                CharacterConfigReading.get().storeCharacterConfig(objid,
                        length, data);

                // 更新
            } else {
                CharacterConfigReading.get().updateCharacterConfig(objid,
                        length, data);
            }

            /*
             * if (Config.CHARACTER_CONFIG_IN_SERVER_SIDE) { final L1PcInstance
             * pc = client.getActiveChar();
             * 
             * final int length = this.readD() - 3; final byte data[] =
             * this.readByte();
             * 
             * final int objid = pc.getId();
             * 
             * final L1Config config = CharacterConfigReading.get().get(objid);
             * 
             * // 新建 if (config == null) {
             * CharacterConfigReading.get().storeCharacterConfig(objid, length,
             * data);
             * 
             * // 更新 } else {
             * CharacterConfigReading.get().updateCharacterConfig(objid, length,
             * data); } }
             */

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
