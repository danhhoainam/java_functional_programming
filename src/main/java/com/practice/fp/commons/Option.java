package com.practice.fp.commons;

import java.util.Objects;

public abstract class Option<A> {

    private Option() {}

    @SuppressWarnings("rawtypes")
    private static Option none = new None();

    // return value if exist, throw if not exist
    public abstract A getOrThrow();
    // return value if exist, else then use lazy supplier
    public abstract A getOrElse(Supplier<A> supplier);
    // convert Option A to Option B
    public abstract <B> Option<B> map(Function<A, B> f);

    /**
     * method to compose Option
     * @param f
     * @param <B>
     * @return
     */
    public <B> Option<B> flatMap(Function<A, Option<B>> f) {
        return map(f).getOrElse(() -> new None<>());
    }

    /**
     * x -> this = A -> Option A
     * @param defaultValue
     * @return
     */
    public Option<A> orElse(Supplier<Option<A>> defaultValue) {
        return map(x -> this).getOrElse(defaultValue);
    }

    /**
     * check if Option match condition or not
     * match: return itself
     * not match: return none
     * @param f condition function like predicate
     * @return itself or none
     */
    public Option<A> filter(Function<A, Boolean> f) {
        return flatMap(x -> f.apply(x)
                ? this
                : none());
    }

    /**
     * compose 2 option into a new one
     * @param a
     * @param b
     * @param f
     * @param <A>
     * @param <B>
     * @param <C>
     * @return
     */
    public static <A, B, C> Option<C> map2(Option<A> a,
                                           Option<B> b,
                                           Function<A, Function<B, C>> f) {
        return a.flatMap(ax -> b.map(bx -> f.apply(ax).apply(bx)));
    }

    /**
     *
     * @param list
     * @param <A>
     * @return
     */
    public static <A> Option<List<A>> sequence(List<Option<A>> list) {
        return list.foldRight(some(List.list()), x -> y -> map2(x, y, a -> b -> b.cons(a)));
    }

    /**
     *
     * @param list
     * @param f
     * @param <A>
     * @param <B>
     * @return
     */
    public static <A, B> Option<List<B>> traverse(List<A> list, Function<A, Option<B>> f) {
        return list.foldRight(some(List.list()), x -> y -> map2(f.apply(x), y, a -> b -> b.cons(a)));
    }

    public static <A> Option<List<A>> sequenceByTraverse(List<Option<A>> list) {
        return traverse(list, x -> x);
    }

    public static <A, B> Function<Option<A>, Option<B>> lift(Function<A, B> f) {
        return a -> a.map(f);
    }

    public static <A, B> Function<A, Option<B>> hlift(Function<A, B> f) {
        return x -> {
            try {
                return Option.some(x).map(f);
            } catch (Exception e) {
                return Option.none();
            }
        };
    }

    private static class None<A> extends Option<A> {
        private None() {}

        @Override
        public A getOrThrow() {
            throw new IllegalStateException("get called on None");
        }

        @Override
        public A getOrElse(Supplier<A> supplier) {
            return supplier.get();
        }

        @Override
        public <B> Option<B> map(Function<A, B> f) {
            return none();
        }

        @Override
        public String toString() {
            return "None";
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof None;
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    private static class Some<A> extends Option<A> {
        private final A value;

        private Some(A value) {
            this.value = value;
        }

        @Override
        public A getOrThrow() {
            return this.value;
        }

        @Override
        public A getOrElse(Supplier<A> supplier) {
            return this.value;
        }

        @Override
        public <B> Option<B> map(Function<A, B> f) {
            return new Some<>(f.apply(this.value));
        }

        @Override
        public String toString() {
            return String.format("Some{%s}", this.value);
        }

        @Override
        public boolean equals(Object o) {
            return (this == o || o instanceof Some)
                    && this.value.equals(((Some<?>) o).value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }
    }

    public static <A> Option<A> some(A a) {
        return new Some<>(a);
    }

    @SuppressWarnings("unchecked")
    public static <A> Option<A> none() {
        return new None<>();
    }
}
