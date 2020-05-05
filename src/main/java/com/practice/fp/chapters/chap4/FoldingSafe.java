package com.practice.fp.chapters.chap4;

import com.practice.fp.commons.TailCall;

import java.util.List;

import static com.practice.fp.commons.TailCall.*;

import static com.practice.fp.collections.CollectionUtilities.*;

public class FoldingSafe {

    public static void main(String[] args) {
        System.out.println("fold left sum stack-safe: "
                + foldLeftRecursive(list(1, 3, 4, 5), 0, x -> y -> x + y));

        System.out.println("range stack-safe: " + range(1, 10));
    }

    static List<Integer> range(Integer start, Integer end) {
        return range_(list(), start, end).eval();
    }

    static TailCall<List<Integer>> range_(List<Integer> acc, Integer start, Integer end) {
        return end <= start
                ? ret(acc)
                : sus(() -> range_(append(acc, start), start + 1, end));
    }
}
