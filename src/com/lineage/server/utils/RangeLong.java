package com.lineage.server.utils;

public class RangeLong {

    /**
     * 格式化数字串(1000 => 1,000)
     * 
     * @param count
     * @return
     */
    public static StringBuilder scount(final long count) {
        final String xcount = String.valueOf(count);
        final StringBuilder newCount = new StringBuilder();
        newCount.append(xcount);
        int x = xcount.length();
        int index = xcount.length() / 3;
        if ((x % 3) == 0) {
            index -= 1;
        }

        for (int i = 0; i < index; i++) {
            x -= 3;
            newCount.insert(x, ",");
        }
        return newCount;
    }
}
