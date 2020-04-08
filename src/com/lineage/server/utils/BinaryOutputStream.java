package com.lineage.server.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.lineage.config.Config;

public class BinaryOutputStream extends OutputStream {

    private static final String CLIENT_LANGUAGE_CODE = Config.CLIENT_LANGUAGE_CODE;

    private final ByteArrayOutputStream _bao = new ByteArrayOutputStream();

    public BinaryOutputStream() {
    }

    @Override
    public void write(final int b) throws IOException {
        this._bao.write(b);
    }

    public void writeD(final int value) {
        this._bao.write(value & 0xff);
        this._bao.write(value >> 8 & 0xff);
        this._bao.write(value >> 16 & 0xff);
        this._bao.write(value >> 24 & 0xff);
    }

    public void writeH(final int value) {
        this._bao.write(value & 0xff);
        this._bao.write(value >> 8 & 0xff);
    }

    public void writeC(final int value) {
        this._bao.write(value & 0xff);
    }

    public void writeP(final int value) {
        this._bao.write(value);
    }

    public void writeL(final long value) {
        this._bao.write((int) (value & 0xff));
    }

    public void writeF(final double org) {
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

    public void writeS(final String text) {
        try {
            if (text != null) {
                this._bao.write(text.getBytes(CLIENT_LANGUAGE_CODE));
            }
        } catch (final Exception e) {
        }

        this._bao.write(0x00);
    }

    public void writeByte(final byte[] text) {
        try {
            if (text != null) {
                this._bao.write(text);
            }
        } catch (final Exception e) {
        }
    }

    public int getLength() {
        return this._bao.size() + 2;
    }

    public byte[] getBytes() {
        return this._bao.toByteArray();
    }
}
