package com.practice.fp.chapters.chap2;

import com.practice.fp.commons.Function;

public class Factorial {

    public static Integer factorial(int n) {
        return n == 0 ? 1 : n * factorial(n - 1);
    }

    public static Function<Integer, Integer> factorial = n -> n <= 1 ? n : n * Factorial.factorial.apply(n - 1);
}
