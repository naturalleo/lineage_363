package com.lineage.server.clientpackets;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.echo.encryptions.PacketPrint;

/**
 * 未处理封包
 * 
 * @author dexc
 * 
 */
public class C_Unkonwn extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_Unkonwn.class);

    /*
     * public C_Unkonwn() { }
     * 
     * public C_Unkonwn(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            // this.read(decrypt);

            _log.info("未处理封包: " + (decrypt[0] & 0xff) + " ("
                    + this.getNow_YMDHMS() + " 核心管理者纪录用!)");
            _log.info(PacketPrint.get().printData(decrypt, decrypt.length));

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

    /**
     * <font color=#00800>取得系统时间</font>
     * 
     * @return 传出标准时间格式 yyyy/MM/dd HH:mm:ss
     */
    private final String getNow_YMDHMS() {
        final String nowDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(new Date());
        return nowDate;
    }
}
