package com.practice.fp.chapters.chap2;

import com.practice.fp.commons.Function;

public class FunctionTests {

    public static void main(String[] args) {
        // apply
        Function<Integer, Integer> plus10 = num -> num + 10;
        System.out.println("plus10 apply test: " + plus10.apply(100));

        // self compose
        Function<Integer, Integer> multiply3 = num -> num * 3;
        System.out.println("compose plus10 and multiply3: " + plus10.compose(multiply3).apply(5));

        // self andThen
        System.out.println("andThen plus10 and multiply3: " + plus10.andThen(multiply3).apply(5));

        // identity: f(x) = x

        // compose 2 functions
        Function<Integer, Integer> mult3ThenPlus10 = Function.compose(plus10, multiply3);
        System.out.println("mult3ThenPlus10: " + mult3ThenPlus10.apply(50));

        // andThen 2 functions
        Function<Integer, Integer> plus10ThenMult3 = Function.andThen(plus10, multiply3);
        System.out.println("plus10ThenMult3: " + plus10ThenMult3.apply(50));

        // compose curry
        System.out.println("compose curry (30 + 10) * 3: " +
                Function.<Integer, Integer, Integer>compose().apply(plus10).apply(multiply3).apply(30));

        // andThen curry
        System.out.println("andThen curry (30 * 3) + 10: " +
                Function.<Integer, Integer, Integer>andThen().apply(plus10).apply(multiply3).apply(30));

        // higher compose curry
        System.out.println("higher compose curry (30 * 3) + 10: " +
                Function.<Integer, Integer, Integer>higherCompose().apply(plus10).apply(multiply3).apply(30));

        // higher andThen curry
        System.out.println("higher andThen curry (30 + 10) * 3: " +
                Function.<Integer, Integer, Integer>higherAndThen().apply(plus10).apply(multiply3).apply(30));

        // reverse args
        Function<Double, Function<Double, Double>> funcToReverse = x -> y -> x * x * 10 - y;
        System.out.println("funcToReverse: 2 * 2 * 10 - 10 " + funcToReverse.apply(2D).apply(10D));
        System.out.println("reverse funcToReverse: 10 * 10 * 10 - 2 " +
                Function.reverseArgs(funcToReverse).apply(2D).apply(10D));
    }
}
