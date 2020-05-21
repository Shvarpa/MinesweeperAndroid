package com.android.minesweeper;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class CollectionUtils {

    public interface Predicate<T> {
        boolean apply(T t);
    }

    public static <K, V> Map<K, V> filter(@NonNull Map<K, V> collection, @NonNull Predicate<Map.Entry<K, V>> predicate) {
        Map<K, V> result = new HashMap<>();
        for (Map.Entry<K, V> entry : collection.entrySet())
            if (predicate.apply(entry))
                result.put(entry.getKey(), entry.getValue());
        return result;
    }

    public static <K, V> void forEach(@NonNull Map<K, V> collection, @NonNull Predicate<Map.Entry<K, V>> predicate) {
        for (Map.Entry<K, V> entry : collection.entrySet())
            predicate.apply(entry);
    }
}
