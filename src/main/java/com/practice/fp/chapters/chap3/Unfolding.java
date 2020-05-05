package com.practice.fp.chapters.chap3;

import java.util.List;

import static com.practice.fp.collections.CollectionUtilities.*;

public class Unfolding {

    public static void main(String[] args) {
        List<Integer> unfoldedList = unfold(0, x -> x + 1, x -> x < 10);
        System.out.println("unfolding generate: " + unfoldedList);

        System.out.println("range: " + range(1, 5));

        System.out.println("rangeRecursive: " + rangeRecursive(5, 10));
    }

    static List<Integer> range(Integer start, Integer end) {
        return unfold(start, x -> x + 1, x -> x < end);
    }

    static List<Integer> rangeRecursive(Integer start, Integer end) {
        return end <= start
                ? list()
                : prepend(start, rangeRecursive(start + 1, end));
    }
}
