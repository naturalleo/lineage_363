package com.lineage.echo.encryptions;

/**
 * 封包资料解密(封包监控用)
 * 
 * @author DarkNight
 * 
 */
public class PacketPrint {

    private static PacketPrint _data;

    public static PacketPrint get() {
        if (_data == null) {
            _data = new PacketPrint();
        }
        return _data;
    }

    /**
     * <font color=#0000ff>印出封包</font> 目的:<BR>
     * 用于检查客户端传出的封包资料<BR>
     * 
     * @param data
     * @param len
     * @return
     */
    public String printData(final byte[] data, final int len) {

        final StringBuffer result = new StringBuffer();

        int counter = 0;

        for (int i = 0; i < len; i++) {

            if (counter % 16 == 0) {
                result.append(this.fillHex(i, 4) + ": ");
            }

            result.append(this.fillHex(data[i] & 0xff, 2) + " ");
            counter++;

            if (counter == 16) {
                result.append("   ");

                int charpoint = i - 15;
                for (int a = 0; a < 16; a++) {
                    final int t1 = data[charpoint++];

                    if ((t1 > 0x1f) && (t1 < 0x80)) {
                        result.append((char) t1);
                    } else {
                        result.append('.');
                    }
                }

                result.append("\n");
                counter = 0;
            }
        }

        final int rest = data.length % 16;

        if (rest > 0) {

            for (int i = 0; i < 17 - rest; i++) {
                result.append("   ");
            }

            int charpoint = data.length - rest;

            for (int a = 0; a < rest; a++) {

                final int t1 = data[charpoint++];

                if ((t1 > 0x1f) && (t1 < 0x80)) {
                    result.append((char) t1);
                } else {
                    result.append('.');
                }
            }

            result.append("\n");
        }

        return result.toString();
    }

    /**
     * <font color=#0000ff>将数字转成 16 进位</font>
     * 
     * @param data
     * @param digits
     * @return
     */
    private String fillHex(final int data, final int digits) {

        String number = Integer.toHexString(data);

        for (int i = number.length(); i < digits; i++) {
            number = "0" + number;
        }

        return number;
    }
}
