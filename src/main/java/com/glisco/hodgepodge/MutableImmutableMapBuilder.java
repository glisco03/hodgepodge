package com.glisco.hodgepodge;

import com.google.common.collect.ImmutableMap;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

public class MutableImmutableMapBuilder<K, V> extends ImmutableMap.Builder<K, V> {

    private final ConcurrentMap<K, V> container = new ConcurrentHashMap<>();

    @Override
    public ImmutableMap.Builder<K, V> put(K key, V value) {
        container.put(key, value);
        return this;
    }

    public V get(K key) {
        return container.get(key);
    }

    public V remove(K key) {
        return container.remove(key);
    }

    public boolean containsKey(K key) {
        return container.containsKey(key);
    }

    public void forEach(BiConsumer<K, V> action) {
        container.forEach(action);
    }

    @Override
    public ImmutableMap<K, V> build() {
        final var builder = ImmutableMap.<K, V>builder();
        builder.putAll(container);
        return builder.build();
    }
}
