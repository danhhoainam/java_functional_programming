package com.practice.fp.commons;

import java.io.Serializable;

public abstract class Result<T> implements Serializable {

    private Result() {}

    @SuppressWarnings("rawtypes")
    private static Result empty = new Empty();

    public abstract T getOrElse(final T defaultValue);
    public abstract T getOrElse(final Supplier<T> defaultValue);
    public abstract <U> Result<U> map(Function<T, U> f);
    public abstract <U> Result<U> flatMap(Function<T, Result<U>> f);
    public abstract Result<T> mapFailure(String s);
    public abstract void forEach(Effect<T> ef);

    public Result<T> orElse(Supplier<Result<T>> defaultValue) {
        return map(x -> this).getOrElse(defaultValue);
    }

    public Result<T> filter(Function<T, Boolean> f) {
        return flatMap(x -> f.apply(x) ? this : failure("Condition not matched"));
    }

    public Result<T> filter(Function<T, Boolean> f, String message) {
        return flatMap(x -> f.apply(x) ? this : failure(message));
    }

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
        public void forEach(Effect<T> ef) {

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

    @SuppressWarnings("uncheked")
    public static <T> Result<T> empty() {
        return empty;
    }
}
