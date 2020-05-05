package com.practice.fp.commons;

public interface Result<T> {

    void bind(Effect<T> success, Effect<String> failure);

    static <T> Result<T> success(T value) {
        return new Success(value);
    }

    static <T> Result<T> failure(String message) {
        return new Failure(message);
    }

    class Success<T> implements Result<T> {

        private final T value;

        public Success(T value) {
            this.value = value;
        }

        @Override
        public void bind(Effect<T> success, Effect<String> failure) {
            success.apply(value);
        }
    }

    class Failure<T> implements Result<T> {

        private final String errorMessage;

        public Failure(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        @Override
        public void bind(Effect<T> success, Effect<String> failure) {
            failure.apply(errorMessage);
        }
    }
}
