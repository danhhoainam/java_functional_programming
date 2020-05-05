package com.practice.fp.commons;

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
     *
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

    public static <A, B> Function<Option<A>, Option<B>> lift(Function<A, B> f) {
        return a -> a.map(f);
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
    }

    public static <A> Option<A> some(A a) {
        return new Some<>(a);
    }

    @SuppressWarnings("unchecked")
    public static <A> Option<A> none() {
        return new None<>();
    }
}
