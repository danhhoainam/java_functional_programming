package com.practice.fp.commons;

@FunctionalInterface
public interface Effect<T> {
    void apply(T t);
}
