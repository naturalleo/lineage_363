package com.lineage.server.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ListMapUtil {

    private static final Log _log = LogFactory.getLog(ListMapUtil.class);

    public static void clear(final Queue<?> queue) {
        try {
            if (queue != null) {
                queue.clear();
            }

        } catch (final Exception e) {
            _log.error("清空Queue发生异常", e);
        }
    }

    public static void clear(final Map<?, ?> map) {
        try {
            if (map != null) {
                map.clear();
            }

        } catch (final Exception e) {
            _log.error("清空Map发生异常", e);
        }
    }

    public static void clear(final ArrayList<?> list) {
        try {
            if (list != null) {
                list.clear();
            }

        } catch (final Exception e) {
            _log.error("清空ArrayList发生异常", e);
        }
    }

    public static void clear(List<?> list) {
        try {
            if (list != null) {
                list.clear();
            }

        } catch (final Exception e) {
            _log.error("清空List发生异常", e);
        }
    }

}
