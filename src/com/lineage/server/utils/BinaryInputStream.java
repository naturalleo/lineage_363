package com.lineage.server.utils;

import java.io.IOException;
import java.io.InputStream;

public class BinaryInputStream extends InputStream {

    InputStream _in;

    public BinaryInputStream(final InputStream in) {
        this._in = in;
    }

    @Override
    public long skip(final long n) throws IOException {
        return this._in.skip(n);
    }

    @Override
    public int available() throws IOException {
        return this._in.available();
    }

    @Override
    public void close() throws IOException {
        this._in.close();
    }

    @Override
    public int read() throws IOException {
        return this._in.read();
    }

    public int readByte() throws IOException {
        return this._in.read();
    }

    public int readShort() throws IOException {
        return this._in.read() | ((this._in.read() << 8) & 0xFF00);
    }

    public int readInt() throws IOException {
        return this.readShort() | ((this.readShort() << 16) & 0xFFFF0000);
    }
}
