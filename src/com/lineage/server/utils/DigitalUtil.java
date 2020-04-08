package com.lineage.server.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 排列数量
 * 
 * @author dexc
 * 
 */
public class DigitalUtil {

    private static final Log _log = LogFactory.getLog(DigitalUtil.class);

    /**
     * 传回数组最小值
     * 
     * @param longs
     * @return
     */
    public static long returnMin(final long[] longs) {
        long i = -1;
        try {
            for (long count : longs) {
                if (i == -1) {
                    i = count;
                }
                i = Math.min(i, count);
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        }
        return i;
    }

    /**
     * 传回数组最小值
     * 
     * @param ints
     * @return
     */
    public static long returnMin(final int[] ints) {
        long i = -1;
        try {
            for (long count : ints) {
                if (i == -1) {
                    i = count;
                }
                i = Math.min(i, count);
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        }
        return i;
    }

    /**
     * 传回数组最小值
     * 
     * @param shorts
     * @return
     */
    public static long returnMin(final short[] shorts) {
        long i = -1;
        try {
            for (long count : shorts) {
                if (i == -1) {
                    i = count;
                }
                i = Math.min(i, count);
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        }
        return i;
    }

    /**
     * 传回数组最小值
     * 
     * @param bytes
     * @return
     */
    public static long returnMin(final byte[] bytes) {
        long i = -1;
        try {
            for (long count : bytes) {
                if (i == -1) {
                    i = count;
                }
                i = Math.min(i, count);
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        }
        return i;
    }

    /**
     * 传回数组最大值
     * 
     * @param longs
     * @return
     */
    public static long returnMax(final long[] longs) {
        long i = -1;
        try {
            for (long count : longs) {
                i = Math.max(i, count);
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        }
        return i;
    }

    /**
     * 传回数组最大值
     * 
     * @param ints
     * @return
     */
    public static long returnMax(final int[] ints) {
        long i = -1;
        try {
            for (long count : ints) {
                i = Math.max(i, count);
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        }
        return i;
    }

    /**
     * 传回数组最大值
     * 
     * @param shorts
     * @return
     */
    public static long returnMax(final short[] shorts) {
        long i = -1;
        try {
            for (long count : shorts) {
                i = Math.max(i, count);
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        }
        return i;
    }

    /**
     * 传回数组最大值
     * 
     * @param bytes
     * @return
     */
    public static long returnMax(final byte[] bytes) {
        long i = -1;
        try {
            for (long count : bytes) {
                i = Math.max(i, count);
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        }
        return i;
    }

}
