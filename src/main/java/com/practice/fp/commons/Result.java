package com.practice.fp.commons;

import java.io.Serializable;

public abstract class Result<T> implements Serializable {

    private Result() {}

    @SuppressWarnings("rawtypes")
    private static Result empty = new Empty();

    // get value for Success case, default value for Failure or Empty
    public abstract T getOrElse(final T defaultValue);

    // lazy load with Supplier
    public abstract T getOrElse(final Supplier<T> defaultValue);

    //
    public abstract <U> Result<U> map(Function<T, U> f);

    //
    public abstract <U> Result<U> flatMap(Function<T, Result<U>> f);

    // Define a mapFailure method that takes a String as its argument
    // and transforms a Failure into another Failure
    // using the string as its error message.
    // If the Result is Empty or Success, this method should do nothing.
    public abstract Result<T> mapFailure(String s);

    // To apply effect on success values
    public abstract void forEach(Effect<T> ef);

    // To throw exception on failure and do something on empty
    public abstract void forEachOrThrow(Effect<T> ef);

    // The more general use case when applying an effect to Result is applying the effect if it’s a Success,
    // and handling the exception in some way if it’s a Failure
    // The typical use case for this method is as follows (using a hypothetical Logger type with a log method)
    public abstract Result<RuntimeException> forEachOrException(Effect<T> ef);

    public Result<T> orElse(Supplier<Result<T>> defaultValue) {
        return map(x -> this).getOrElse(defaultValue);
    }

    /**
     * a method filter taking a condition that’s represented by a function from T to Boolean, and returning a Result<T>,
     * which will be a Success or a Failure depending on whether the condition holds for the wrapped value
     * @param f condition function
     * @return filtered results
     */
    public Result<T> filter(Function<T, Boolean> f) {
        return flatMap(x -> f.apply(x) ? this : failure("Condition not matched"));
    }

    /**
     * a method filter taking a condition that’s represented by a function from T to Boolean, and returning a Result<T>,
     * which will be a Success or a Failure depending on whether the condition holds for the wrapped value
     * @param f condition function
     * @return filtered results
     */
    public Result<T> filter(Function<T, Boolean> f, String message) {
        return flatMap(x -> f.apply(x) ? this : failure(message));
    }

    /**
     * Define an exists method that takes a function from T to Boolean
     * and returns true if the wrapped value matches the condition,
     * or false otherwise
     * @param f condition function
     * @return True or False
     */
    public boolean exists(Function<T, Boolean> f) {
        return map(f).getOrElse(false);
    }

    private static class Empty<T> extends Result<T> {

        public Empty() {
            super();
        }

        @Override
        public T getOrElse(T defaultValue) {
            return defaultValue;
        }

        @Override
        public T getOrElse(Supplier<T> defaultValue) {
            return defaultValue.get();
        }

        @Override
        public <U> Result<U> map(Function<T, U> f) {
            return empty();
        }

        @Override
        public <U> Result<U> flatMap(Function<T, Result<U>> f) {
            return empty();
        }

        @Override
        public Result<T> mapFailure(String s) {
            return this;
        }

        @Override
        public void forEach(Effect<T> ef) {
            // Empty. Do nothing.
        }

        @Override
        public void forEachOrThrow(Effect<T> ef) {
            throw new RuntimeException("Empty value");
        }

        @Override
        public Result<RuntimeException> forEachOrException(Effect<T> ef) {
            return empty();
        }

        @Override
        public String toString() {
            return "Empty()";
        }
    }

    private static class Failure<T> extends Empty<T> {

        private final RuntimeException exception;

        private Failure(String message) {
            super();
            this.exception = new IllegalStateException(message);
        }

        private Failure(RuntimeException exception) {
            super();
            this.exception = exception;
        }

        private Failure(Exception exception) {
            super();
            this.exception = new IllegalStateException(exception.getMessage(), exception);
        }

        @Override
        public String toString() {
            return String.format("Failure(%s)", exception.getMessage());
        }

        @Override
        public T getOrElse(T defaultValue) {
            return defaultValue;
        }

        @Override
        public T getOrElse(Supplier<T> defaultValue) {
            return defaultValue.get();
        }

        @Override
        public <U> Result<U> map(Function<T, U> f) {
            return failure(this.exception);
        }

        @Override
        public <U> Result<U> flatMap(Function<T, Result<U>> f) {
            return failure(this.exception);
        }

        @Override
        public Result<T> mapFailure(String s) {
            return failure(new IllegalStateException(s, exception));
        }

        @Override
        public void forEachOrThrow(Effect<T> ef) {
            throw exception;
        }

        @Override
        public Result<RuntimeException> forEachOrException(Effect<T> ef) {
            return success(exception);
        }
    }

    private static class Success<T> extends Result<T> {

        private final T value;

        private Success(T val) {
            this.value = val;
        }

        @Override
        public String toString() {
            return String.format("Success(%s)", value.toString());
        }

        @Override
        public T getOrElse(T defaultValue) {
            return value;
        }

        @Override
        public T getOrElse(Supplier<T> defaultValue) {
            return value;
        }

        @Override
        public <U> Result<U> map(Function<T, U> f) {
            return success(f.apply(this.value));
        }

        @Override
        public <U> Result<U> flatMap(Function<T, Result<U>> f) {
            return f.apply(this.value);
        }

        @Override
        public Result<T> mapFailure(String s) {
            return this;
        }

        @Override
        public void forEach(Effect<T> ef) {
            ef.apply(value);
        }

        @Override
        public void forEachOrThrow(Effect<T> ef) {
            ef.apply(value);
        }

        @Override
        public Result<RuntimeException> forEachOrException(Effect<T> ef) {
            ef.apply(value);
            return empty();
        }
    }

    public static <T> Result<T> failure(String message) {
        return new Failure<>(message);
    }

    public static <T> Result<T> failure(RuntimeException ex) {
        return new Failure<>(ex);
    }

    public static <T> Result<T> failure(Exception ex) {
        return new Failure<>(ex);
    }

    public static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    public static <T> Result<T> of(T value) {
        return of(value, "Null value");
    }

    public static <T> Result<T> of(T value, String message) {
        return value != null
                ? success(value)
                : failure(message);
    }

    public static <T> Result<T> of(Function<T, Boolean> predicate, T value) {
        try {
            return predicate.apply(value)
                    ? success(value)
                    : empty();
        } catch (Exception e) {
            String error = String.format("Exception while evaluating predicate: %s", value);
            return failure(new IllegalStateException(error, e));
        }
    }

    public static <T> Result<T> of(Function<T, Boolean> predicate, T value, String message) {
        try {
            return predicate.apply(value)
                    ? success(value)
                    : failure(message);
        } catch (Exception e) {
            String error = String.format("Exception while evaluating predicate: %s",
                    String.format(message, value));
            return failure(new IllegalStateException(error, e));
        }
    }

    /**
     * lift a function from A to B
     * to a function from Result A to Result B
     * @param f function from A to B
     * @param <A> A
     * @param <B> B
     * @return function Result A to Result B
     */
    public static <A, B> Function<Result<A>, Result<B>> lift(final Function<A, B> f) {
        return a -> {
            try {
                return a.map(f);
            } catch (Exception ex) {
                return failure(ex);
            }
        };
    }

    /**
     * lift a function from A to B to C
     * to a function from Result A to Result B to Result C
     * @param f function from A to B to C
     * @param <A> A
     * @param <B> B
     * @param <C> C
     * @return function from Result A to Result B to Result C
     */
    public static <A, B, C> Function<Result<A>, Function<Result<B>,
            Result<C>>> lift2(Function<A, Function<B, C>> f) {
        return a -> b -> a.map(f).flatMap(b::map);
    }

    /**
     * lift a function from A to B to C to D
     * to a function from Result A to Result B to Result C to Result D
     * @param f function from A to B to C to D
     * @param <A> A
     * @param <B> B
     * @param <C> C
     * @param <D> D
     * @return function from Result A to Result B to Result C to Result D
     */
    public static <A, B, C, D> Function<Result<A>,
            Function<Result<B>, Function<Result<C>,
                    Result<D>>>> lift3(Function<A, Function<B, Function<C, D>>> f) {
        return a -> b -> c -> a.map(f).flatMap(b::map).flatMap(c::map);
    }

    /**
     * compose Result A and Result B
     * and transform them through function A -> B -> C
     * @param a Result A
     * @param b Result B
     * @param f function A -> B -> C
     * @param <A> A
     * @param <B> B
     * @param <C> C
     * @return Result of C
     */
    public static <A, B, C> Result<C> map2(Result<A> a, Result<B> b, Function<A, Function<B, C>> f) {
//        return a.flatMap(ax -> b.map(bx -> f.apply(ax).apply(bx)));
        return lift2(f).apply(a).apply(b);
    }

    @SuppressWarnings("uncheked")
    public static <T> Result<T> empty() {
        return empty;
    }
}
