package com.practice.fp.commons;

public abstract class TailCall<T> {

    // prevent extension
    private TailCall() {}

    public abstract TailCall<T> resume();
    public abstract T eval();
    public abstract boolean isSuspend();

    public static <T> Return<T> ret(T t) {
        return new Return<>(t);
    }

    public static <T> Suspend<T> sus(Supplier<TailCall<T>> s) {
        return new Suspend<>(s);
    }

    private static class Return<T> extends TailCall<T> {
        private final T t;

        public Return(T t) {
            this.t = t;
        }

        @Override
        public TailCall<T> resume() {
            throw new RuntimeException("Return has no resume");
        }

        @Override
        public T eval() {
            return t;
        }

        @Override
        public boolean isSuspend() {
            return false;
        }
    }

    private static class Suspend<T> extends TailCall<T> {
        private final Supplier<TailCall<T>> resume;

        public Suspend(Supplier<TailCall<T>> resume) {
            this.resume = resume;
        }

        @Override
        public TailCall<T> resume() {
            return resume.get();
        }

        @Override
        public T eval() {
            TailCall<T> tailCall = resume.get();
            while (tailCall.isSuspend()) {
                tailCall = tailCall.resume();
            }
            return tailCall.eval();
        }

        @Override
        public boolean isSuspend() {
            return true;
        }
    }
}
