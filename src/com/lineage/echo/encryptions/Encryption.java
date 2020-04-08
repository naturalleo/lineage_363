package com.lineage.echo.encryptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.types.UChar8;
import com.lineage.server.types.ULong32;

/**
 * 加密解密模组
 * 
 * @author daien
 * 
 */
public class Encryption {

    private static final Log _log = LogFactory.getLog(Encryption.class);

    private Keys _keys = null;

    /**
     * 设置加密解密模组
     * 
     * @param clientID
     * @param seed
     * @throws ClientIdExistsException
     */
    public void initKeys(final long seed) throws Exception {
        try {
            final Keys keys = new Keys();

            final long key[] = { seed, 0x930FD7E2L };

            Blowfish.getSeeds(key);

            keys.EKEY[0] = keys.DKEY[0] = key[0];
            keys.EKEY[1] = keys.DKEY[1] = key[1];

            _keys = keys;
            // System.out.println(_keys.DKEY[0]+" / "+_keys.DKEY[1]);
            // System.out.println(_keys.EKEY[0]+" / "+_keys.EKEY[1]);
        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 加密
     * 
     * @param buf
     *            char[]
     * @param currentKeys
     *            LineageKeys
     * @return
     * @throws NoEncryptionKeysSelectedException
     */
    public char[] encrypt(final char[] buf) throws Exception {
        try {
            if (_keys == null) {
                throw new Exception();
            }
            // System.out.println("加密");
            final long mask = ULong32.fromArray(buf);

            _encrypt(buf);

            _keys.EKEY[0] ^= mask;
            _keys.EKEY[1] = ULong32.add(_keys.EKEY[1], 0x287EFFC3L);

            return buf;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 加密处理
     * 
     * @param buf
     * @param currentKeys
     * @return
     */
    private char[] _encrypt(final char[] buf) {
        try {
            final int size = buf.length;
            final char[] ek = UChar8.fromArray(_keys.EKEY);

            buf[0] ^= ek[0];

            for (int i = 1; i < size; i++) {
                buf[i] ^= (buf[i - 1] ^ ek[i & 7]);
            }

            buf[3] = (char) (buf[3] ^ ek[2]);
            buf[2] = (char) (buf[2] ^ buf[3] ^ ek[3]);
            buf[1] = (char) (buf[1] ^ buf[2] ^ ek[4]);
            buf[0] = (char) (buf[0] ^ buf[1] ^ ek[5]);

            return buf;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 解密
     * 
     * @param buf
     *            byte[]
     * @param length
     * @param currentKeys
     * @return
     * @throws NoEncryptionKeysSelectedException
     */
    public byte[] decrypt(final byte[] buf) throws Exception {
        try {
            if (_keys == null) {
                throw new Exception();
            }
            // System.out.println("解密");
            _decrypt(buf);

            final long mask = ULong32.fromArray(buf);

            _keys.DKEY[0] ^= mask;
            _keys.DKEY[1] = ULong32.add(_keys.DKEY[1], 0x287EFFC3L);

            return buf;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 解密处理
     * 
     * @param buf
     * @param size
     * @param currentKeys
     * @return
     */
    private byte[] _decrypt(final byte[] buf) {
        try {
            final char[] dk = UChar8.fromArray(_keys.DKEY);

            final byte b3 = buf[3];
            buf[3] ^= dk[2];

            final byte b2 = buf[2];
            buf[2] ^= (b3 ^ dk[3]);

            final byte b1 = buf[1];
            buf[1] ^= (b2 ^ dk[4]);

            byte k = (byte) (buf[0] ^ b1 ^ dk[5]);
            buf[0] = (byte) (k ^ dk[0]);

            for (int i = 1; i < buf.length; i++) {
                final byte t = buf[i];
                buf[i] ^= (dk[i & 7] ^ k);
                k = t;
            }

            return buf;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }
}
