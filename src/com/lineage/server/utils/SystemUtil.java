package com.lineage.server.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.nio.ByteBuffer;

import org.apache.commons.logging.Log;

public class SystemUtil {

    /**
     * Convert data from given ByteBuffer to hex
     * 
     * @param data
     * @return hex
     */
    public static String toHex(final ByteBuffer data) {
        final StringBuilder result = new StringBuilder();
        int counter = 0;
        int b;
        while (data.hasRemaining()) {
            if (counter % 16 == 0) {
                result.append(String.format("%04X: ", counter));
            }

            b = data.get() & 0xff;
            result.append(String.format("%02X ", b));

            counter++;
            if (counter % 16 == 0) {
                result.append("  ");
                toText(data, result, 16);
                result.append("\n");
            }
        }
        final int rest = counter % 16;
        if (rest > 0) {
            for (int i = 0; i < 17 - rest; i++) {
                result.append("   ");
            }
            toText(data, result, rest);
        }
        return result.toString();
    }

    /**
     * Gets last <tt>cnt</tt> read bytes from the <tt>data</tt> buffer and puts
     * into <tt>result</tt> buffer in special format:
     * <ul>
     * <li>if byte represents char from partition 0x1F to 0x80 (which are normal
     * ascii chars) then it's put into buffer as it is</li>
     * <li>otherwise dot is put into buffer</li>
     * </ul>
     * 
     * @param data
     * @param result
     * @param cnt
     */
    private static void toText(final ByteBuffer data,
            final StringBuilder result, final int cnt) {
        int charPos = data.position() - cnt;
        for (int a = 0; a < cnt; a++) {
            final int c = data.get(charPos++);
            if ((c > 0x1f) && (c < 0x80)) {
                result.append((char) c);
            } else {
                result.append('.');
            }
        }
    }

    public static long getUsedMemoryMB() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime()
                .freeMemory()) / 1024L / 1024L;
    }

    /**
     * Prings memory usage both for heap and non-heap memory.
     */
    public static void printMemoryUsage(final Log log) {
        // 返回的内存使用量中已使用内存量为活动对象和尚未回收的垃圾对象（如果有）所占用内存的总量。
        final MemoryUsage hm = ManagementFactory.getMemoryMXBean()
                .getHeapMemoryUsage();
        // 返回 Java 虚拟机使用的非堆内存的当前使用量。
        final MemoryUsage nhm = ManagementFactory.getMemoryMXBean()
                .getNonHeapMemoryUsage();

        final String s1 = (hm.getUsed() / 1048576) + "/"
                + (hm.getMax() / 1048576) + "mb";
        final String s2 = (nhm.getUsed() / 1048576) + "/"
                + (nhm.getMax() / 1048576) + "mb";

        if (log != null) {
            log.info("已分配内存使用量: " + s1);
            log.info("非分配内存使用量: " + s2);

        }
    }
}
