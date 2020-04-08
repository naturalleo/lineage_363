package com.lineage.server.serverpackets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.config.ConfigBad;

/**
 * 
 * @author dexc
 * 
 */
public abstract class ServerBasePacket extends OpcodesServer {

    private static final Log _log = LogFactory.getLog(ServerBasePacket.class);

    private static final String CLIENT_LANGUAGE_CODE = Config.CLIENT_LANGUAGE_CODE;

    protected ByteArrayOutputStream _bao = new ByteArrayOutputStream();

    /**
     * boolean
     * 
     * @param b
     * @return
     */
    protected Object writeBoolean(final boolean b) {
        this._bao.write(b ? 0x01 : 0x00);

        return null;
    }

    protected void writeD(final int value) {
        this._bao.write(value & 0xff);
        this._bao.write(value >> 8 & 0xff);
        this._bao.write(value >> 16 & 0xff);
        this._bao.write(value >> 24 & 0xff);
    }

    protected void writeH(final int value) {
        this._bao.write(value & 0xff);
        this._bao.write(value >> 8 & 0xff);
    }

    protected void writeC(final int value) {
        this._bao.write(value & 0xff);
    }

    protected void writeP(final int value) {
        this._bao.write(value);
    }

    protected void writeL(final long value) {
        this._bao.write((int) (value & 0xff));
    }

    protected void writeF(final double org) {
        final long value = Double.doubleToRawLongBits(org);
        this._bao.write((int) (value & 0xff));
        this._bao.write((int) (value >> 8 & 0xff));
        this._bao.write((int) (value >> 16 & 0xff));
        this._bao.write((int) (value >> 24 & 0xff));
        this._bao.write((int) (value >> 32 & 0xff));
        this._bao.write((int) (value >> 40 & 0xff));
        this._bao.write((int) (value >> 48 & 0xff));
        this._bao.write((int) (value >> 56 & 0xff));
    }

    protected void writeExp(final long value) {
        this._bao.write((int) (value & 0xff));
        this._bao.write((int) (value >> 8 & 0xff));
        this._bao.write((int) (value >> 16 & 0xff));
        this._bao.write((int) (value >> 24 & 0xff));

        /*
         * this._bao.write((int) (value & 0xff)); this._bao.write((int) (value
         * >> 8 & 0xff)); this._bao.write((int) (value >> 16 & 0xff));
         * this._bao.write((int) (value >> 24 & 0xff)); this._bao.write((int)
         * (value >> 32 & 0xff)); this._bao.write((int) (value >> 40 & 0xff));
         * this._bao.write((int) (value >> 48 & 0xff)); this._bao.write((int)
         * (value >> 56 & 0xff));
         */
    }

    protected void writeS(final String text) {
        try {
            if (text != null) {
                String chtext = text;
                for (final Iterator<String> iter = ConfigBad.BAD_TEXT_LIST
                        .iterator(); iter.hasNext();) {
                    final String bad = iter.next();
                    final int index = chtext.indexOf(bad);
                    if (index != -1) {
                        chtext = text.substring(0, index);
                        chtext += text.substring(index + bad.length());
                    }
                }
                this._bao.write(chtext.getBytes(CLIENT_LANGUAGE_CODE));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }

        this._bao.write(0x00);
    }

    protected void writeByte(final byte[] text) {
        try {
            if (text != null) {
                this._bao.write(text);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        this._bao.write(0x00);
    }

    // private static Random _random = new Random();

    /**
     * 不足8组 补满8组BYTE
     * 
     * @return
     */
    protected byte[] getBytes() {

        final int padding = this._bao.size() % 8;
        if (padding != 0) {
            for (int i = padding; i < 8; i++) {
                this.writeC(0x00);
            }
        }

        return this._bao.toByteArray();
    }

    public abstract byte[] getContent() throws IOException;

    public String getType() {
        return this.getClass().getSimpleName();
    }
}
