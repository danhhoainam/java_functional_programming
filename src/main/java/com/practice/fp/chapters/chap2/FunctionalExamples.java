package com.practice.fp.chapters.chap2;

import com.practice.fp.commons.Function;
import com.practice.fp.commons.Tuple;

public class FunctionalExamples {

    public static void main(String[] args) {
        Function<Integer, Integer> triple = x -> x * 3;

        Function<Integer, Integer> square = x -> x * x;

        Function<Integer, Integer> doubl = x -> x * 2;

        Function<Integer, Integer> plus2 = x -> x + 2;

        int result = triple.apply(square.apply(doubl.apply(3)));
        System.out.println(result);

        System.out.println(compose(triple, doubl).apply(2));

        System.out.println("add 2 int: " + add.apply(10).apply(100));
        System.out.println("multiply 2 int: " + mult.apply(10).apply(100));

        System.out.println("compose 2 functions: " + compose.apply(plus2).apply(square).apply(10));
        System.out.println("compose 2 functions: " + compose.apply(square).apply(plus2).apply(10));

        System.out.println("higher compose function: "
                + Function.<Integer, Integer, Integer>higherCompose().apply(triple).apply(plus2).apply(3));
        System.out.println("higher and then function: "
                + Function.<Integer, Integer, Integer>higherAndThen().apply(triple).apply(plus2).apply(3));

        System.out.println("curried func: " + func().apply("f").apply("e").apply("g").apply("a"));

        System.out.println("curried tuple: " +
                curryTuple((Tuple<Integer, Integer> tuple) -> tuple._1 * tuple._2).apply(10).apply(5));

        Function<Double, Function<Double, Double>> funcToReverse = x -> y -> x * x * 10 - y;
        System.out.println("funcToReverse: " + funcToReverse.apply(2D).apply(10D));
        System.out.println("reverse funcToReverse: " + Function.reverseArgs(funcToReverse).apply(2D).apply(10D));

        System.out.println("factorial of 10: " + Factorial.factorial(10));
        System.out.println("factorial of 5: " + Factorial.factorial.apply(5));
    }

    /**
     * this function will cause error of stack overflow
     * because it will use stack memory
     * the number of operation ~7500
     * to prevent this, we will use object to store function in heap memory instead
     * @param f1
     * @param f2
     * @return
     */
    private static Function<Integer, Integer> compose(Function<Integer, Integer> f1, Function<Integer, Integer> f2) {
        return arg -> f1.apply(f2.apply(arg));
    }

    private static Function<Function<Integer, Integer>,
                            Function<Function<Integer, Integer>,
                                    Function<Integer, Integer>>> compose = x -> y -> z -> x.apply(y.apply(z));

    private static Function<Integer, Function<Integer, Integer>> add = x -> y -> x + y;

    private static BinaryOperator mult = x -> y -> x * y;

    private static <A, B, C, D> String func(A a, B b, C c, D d) {
        return String.format("%s, %s, %s, %s", a, b, c, d);
    }

    static <A, B, C, D> Function<A, Function<B, Function<C, Function<D, String>>>> func() {
        return a -> b -> c -> d -> String.format("%s, %s, %s, %s", a, b, c, d);
    }

    static <A, B, C> Function<A, Function<B, C>> curryTuple(Function<Tuple<A, B>, C> f) {
        return a -> b -> f.apply(new Tuple<>(a, b));
    }
}
