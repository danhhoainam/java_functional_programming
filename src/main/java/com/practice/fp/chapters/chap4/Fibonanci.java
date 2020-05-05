package com.practice.fp.chapters.chap4;

import com.practice.fp.commons.TailCall;

import java.math.BigInteger;

import static com.practice.fp.commons.TailCall.*;

public class Fibonanci {

    public static void main(String[] args) {
        System.out.println("fibonacci normal: " + fibonacci(10));

        System.out.println("fibonacci dual recursive: " + fib(BigInteger.valueOf(1000)));

        System.out.println("fibonacci stack safe by TailCall: " + fibSafe(BigInteger.valueOf(10000)));
    }

    public static int fibonacci(int number) {
        if (number == 0 || number == 1) {
            return number;
        }

        return fibonacci(number - 1) + fibonacci(number - 2);
    }

    public static BigInteger fib(BigInteger n) {
        return fib_(BigInteger.ONE, BigInteger.ZERO, n);
    }

    /**
     * fails at 7500 - 8000
     * @param acc1
     * @param acc2
     * @param n
     * @return
     */
    public static BigInteger fib_(BigInteger acc1, BigInteger acc2, BigInteger n) {
        if (n.equals(BigInteger.ZERO)) {
            return BigInteger.ZERO;
        } else if (n.equals(BigInteger.ONE)) {
            return acc1.add(acc2);
        } else {
            return fib_(acc2, acc1.add(acc2), n.subtract(BigInteger.ONE));
        }
    }

    public static BigInteger fibSafe(BigInteger n) {
        return fibSafe_(BigInteger.ONE, BigInteger.ZERO, n).eval();
    }

    /**
     * stack safe version
     * @param acc1
     * @param acc2
     * @param n
     * @return
     */
    public static TailCall<BigInteger> fibSafe_(BigInteger acc1, BigInteger acc2, BigInteger n) {
        if (n.equals(BigInteger.ZERO)) {
            return ret(BigInteger.ZERO);
        } else if (n.equals(BigInteger.ONE)) {
            return ret(acc1.add(acc2));
        } else {
            return sus(() -> fibSafe_(acc2, acc1.add(acc2), n.subtract(BigInteger.ONE)));
        }
    }
}
