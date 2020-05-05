package com.practice.fp.chapters.chap6;

import com.practice.fp.commons.Option;

import java.util.concurrent.ConcurrentHashMap;

public class Map<T, U> {

    private final ConcurrentHashMap<T, U> map = new ConcurrentHashMap<>();

    public static <T, U> Map<T, U> empty() {
        return new Map<>();
    }

    public static <T, U> Map<T, U> add(Map<T, U> m, T t, U u) {
        m.map.put(t, u);
        return m;
    }

    public Option<U> get(final T t) {
        return this.map.containsKey(t)
                ? Option.some(this.map.get(t))
                : Option.none();
    }

    public Map<T, U> put(T t, U u) {
        return add(this, t, u);
    }

    public Map<T, U> removeKey(T t) {
        this.map.remove(t);
        return this;
    }
}
