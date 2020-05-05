package com.practice.fp.chapters.chap4;


import com.practice.fp.commons.*;

import java.math.BigInteger;
import java.util.List;

import static com.practice.fp.collections.CollectionUtilities.*;
import static com.practice.fp.commons.TailCall.*;

public class Memoization {

    public static void main (String[] args) {
        System.out.println("fibString_ result: "
                + fibString_(list(), BigInteger.ONE, BigInteger.ZERO, BigInteger.valueOf(10)).eval());

        System.out.println("fibString with memoization: " + fibString(BigInteger.valueOf(10)));

        testMemoizerSingleParam();
        testMemoizerCurriedFunc();
        testMemoizerTupleFunc();
    }

    static String fibString(BigInteger limit) {
        List<BigInteger> listFibo = fibString_(list(), BigInteger.ONE, BigInteger.ZERO, limit).eval();
        return makeStr(listFibo, ", ");
    }

    static TailCall<List<BigInteger>> fibString_(List<BigInteger> acc, BigInteger acc1, BigInteger acc2, BigInteger limit) {
        return limit.equals(BigInteger.ZERO)
            ? ret(list(BigInteger.ZERO))
            : limit.equals(BigInteger.ONE)
                ? ret(append(acc, acc1.add(acc2)))
                : sus(() -> fibString_(append(acc, acc1.add(acc2)), acc2, acc1.add(acc2), limit.subtract(BigInteger.ONE)));
    }

    static <T> String makeStr(List<T> list, String separator) {
        return list.isEmpty()
                ? ""
                : tail(list).isEmpty()
                    ? head(list).toString()
                    : head(list) + foldLeft(tail(list), "", x -> y -> x + separator + y);
    }

    static Integer longCalculation(Integer number) {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException ex) {

        }
        return number * 10;
    }

    static Function<Integer, Integer> func1 = number -> longCalculation(number);
    static Function<Integer, Integer> memoizedFunc1 = Memoizer.memoize(func1);

    static void testMemoizerSingleParam() {
        long startTime = System.currentTimeMillis();
        System.out.println("===========================================");
        System.out.println("testMemoizerSingleParam");
        System.out.println("first calculation: " + memoizedFunc1.apply(200));
        System.out.println("time executed 1: " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        System.out.println("second calculation: " + memoizedFunc1.apply(200));
        System.out.println("time executed 2: " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        System.out.println("third calculation: " + memoizedFunc1.apply(300));
        System.out.println("time executed 3: " + (System.currentTimeMillis() - startTime));
    }

    static Function<Integer, Function<Integer, Function<Integer, Integer>>> func2 = x -> y -> z -> x + y * z;
    static Function<Integer, Function<Integer, Function<Integer, Integer>>> memoizedFunc2 =
            Memoizer.memoize(x ->
                    Memoizer.memoize(y ->
                            Memoizer.memoize(z -> longCalculation(x) + longCalculation(y) * longCalculation(z))));

    static void testMemoizerCurriedFunc() {
        long startTime = System.currentTimeMillis();
        System.out.println("===========================================");
        System.out.println("testMemoizerCurriedFunc");
        System.out.println("first calculation: " + memoizedFunc2.apply(22).apply(23).apply(24));
        System.out.println("time executed testMemoizerCurriedFunc 1: " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        System.out.println("second calculation: " + memoizedFunc2.apply(22).apply(23).apply(24));
        System.out.println("time executed testMemoizerCurriedFunc 2: " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        System.out.println("third calculation: " + memoizedFunc2.apply(22).apply(23).apply(25));
        System.out.println("time executed testMemoizerCurriedFunc 3: " + (System.currentTimeMillis() - startTime));
    }

    static Function<Tuple3<Integer, Integer, Integer>, Integer> func3 =
            tup -> longCalculation(tup._1) + longCalculation(tup._2) * longCalculation(tup._3);
    static Function<Tuple3<Integer, Integer, Integer>, Integer> memoizedFunc3 = Memoizer.memoize(func3);

    static void testMemoizerTupleFunc() {
        long startTime = System.currentTimeMillis();
        System.out.println("===========================================");
        System.out.println("testMemoizerTupleFunc");
        System.out.println("first testMemoizerTupleFunc calculation: "
                + memoizedFunc3.apply(new Tuple3<>(2, 3, 4)));
        System.out.println("time executed testMemoizerTupleFunc 1: " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        System.out.println("second testMemoizerTupleFunc calculation: "
                + memoizedFunc3.apply(new Tuple3<>(2, 3, 4)));
        System.out.println("time executed testMemoizerTupleFunc 2: " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        System.out.println("third calculation: " + memoizedFunc3.apply(new Tuple3<>(2, 3, 6)));
        System.out.println("time executed testMemoizerTupleFunc 3: " + (System.currentTimeMillis() - startTime));
    }
}
