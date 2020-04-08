package com.lineage.server.utils.collections;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Maps {

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<K, V>();
    }

    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<K, V>();
    }
}
