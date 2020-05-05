package com.practice.fp.chapters.chap4;


import com.practice.fp.commons.Function;
import com.practice.fp.commons.TailCall;

import java.util.List;
import static com.practice.fp.collections.CollectionUtilities.*;

public class TCE {

    public static void main(String[] args) {

        System.out.println("add normally: " + add(12, 13));

        //System.out.println("addTCE value: " + addTCE(1, 20000000).eval());

        //System.out.println("addTCE function: " + addTCE.apply(100).apply(100000000).eval());

        System.out.println("sum without tail recursive: " + sum(list(1, 2, 3, 4)));

        System.out.println("sum tail recursive: " + sumTail(list(1, 2, 3, 4), 0));
    }

    static int add(int x, int y) {
        return y == 0
                ? x
                : add(++x, --y);
    }

    static TailCall<Integer> addTCE(int x, int y) {
        return y == 0
                ? TailCall.ret(x)
                : TailCall.sus(() -> addTCE(x + 1, y - 1));
    }

    static Function<Integer, Function<Integer, TailCall<Integer>>> addTCE =
            x -> y -> y == 0
                    ? TailCall.ret(x)
                    : TailCall.sus(() -> TCE.addTCE.apply(x + 1).apply(y - 1));

    static Integer sum(List<Integer> list) {
        return list.isEmpty()
                ? 0
                : head(list) + sum(tail(list));
    }

    static Integer sumTail(List<Integer> list, Integer acc) {
        return list.isEmpty()
                ? acc
                : sumTail(tail(list), acc + head(list));
    }
}
