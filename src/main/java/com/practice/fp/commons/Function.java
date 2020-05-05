package com.practice.fp.commons;

public interface Function<T, U> {

    U apply(T t);

    /**
     * compose VT and TU into VU
     * TU.compose(VT) -> implement VT first, then implement TU
     * @param f
     * @param <V>
     * @return
     */
    default <V> Function<V, U> compose(Function<V, T> f) {
        return x -> apply(f.apply(x));
    }

    /**
     * compose TU and UV into TV
     * TU.andThen(UV) -> implement TU first, and then implement UV
     * @param f
     * @param <V>
     * @return
     */
    default <V> Function<T, V> andThen(Function<U, V> f) {
        return x -> f.apply(apply(x));
    }

    /**
     *
     * @param <T>
     * @return
     */
    static <T> Function<T, T> identity() {
        return t -> t;
    }

    /**
     * implement g function before f function
     * @param f
     * @param g
     * @param <T>
     * @param <U>
     * @param <V>
     * @return
     */
    static <T, U, V> Function<V, U> compose(Function<T, U> f,
                                            Function<V, T> g) {
        return x -> f.apply(g.apply(x));
    }

    /**
     * implement f function before g function
     * @param f
     * @param g
     * @param <T>
     * @param <U>
     * @param <V>
     * @return
     */
    static <T, U, V> Function<T, V> andThen(Function<T, U> f,
                                            Function<U, V> g) {
        return x -> g.apply(f.apply(x));
    }

    /**
     * compose functions with curry style
     * TU -> UV -> TV
     * implement TU first, then UV
     * @param <T>
     * @param <U>
     * @param <V>
     * @return
     */
    static <T, U, V> Function<Function<T, U>,
            Function<Function<U, V>,
                    Function<T, V>>> compose() {
        return x -> y -> y.compose(x);
    }

    /**
     * compose functions with curry style
     * TU -> UV -> TV
     * implement VT first, then TU
     * @param <T>
     * @param <U>
     * @param <V>
     * @return
     */
    static <T, U, V> Function<Function<T, U>,
            Function<Function<V, T>,
                    Function<V, U>>> andThen() {
        return x -> y -> y.andThen(x);
    }

    /**
     * TU -> UV -> TV
     * @param <T>
     * @param <U>
     * @param <V>
     * @return
     */
    static <T, U, V> Function<Function<T, U>,
            Function<Function<U, V>,
                    Function<T, V>>> higherAndThen() {
        return x -> y -> z -> y.apply(x.apply(z));
    }

    /**
     * UV -> TU -> TV
     * @param <T>
     * @param <U>
     * @param <V>
     * @return
     */
    static <T, U, V> Function<Function<U, V>,
            Function<Function<T, U>,
                    Function<T, V>>> higherCompose() {
        return x -> y -> z -> x.apply(y.apply(z));
    }

    /**
     * function to swap the T and U param
     * @param f
     * @param <T>
     * @param <U>
     * @param <V>
     * @return
     */
    static <T, U, V> Function<U, Function<T, V>> reverseArgs(Function<T, Function<U, V>> f) {
        return u -> t -> f.apply(t).apply(u);
    }
}
