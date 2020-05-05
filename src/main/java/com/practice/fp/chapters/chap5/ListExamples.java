package com.practice.fp.chapters.chap5;

import com.practice.fp.commons.List;
import com.practice.fp.commons.TailCall;

import static com.practice.fp.commons.TailCall.*;
import static com.practice.fp.commons.List.*;

public class ListExamples {

    public static void main(String[] args) {
        List<Integer> list1 = List.list(1, 2, 4, 6, 11, 21);
        List<Integer> list2 = List.list(10, 299);

        System.out.println("list head(): " + list1.head());
        System.out.println("list tail(): " + list1.tail());
        System.out.println("list isEmpty(): " + list1.isEmpty());
        System.out.println("list cons(): " + list1.cons(33));

        System.out.println("sum all items: " + sum(list1));

        System.out.println("length of the list by foldRight: " + List.foldRight(list1, 0, x -> y -> y + 1));

        System.out.println("sum stack-safe foldLeft: " + list1.foldLeft(0, x -> y -> x + y));
        System.out.println("product stack-safe foldLeft: " + list1.foldLeft(1.0, x -> y -> x * y));

        System.out.println("fold right stack-safe: " + list1.foldRight("", x -> y -> x + ", " + y));
        System.out.println("fold left stack-safe: " + list1.foldLeft("", x -> y -> x + ", " + y));

        System.out.println("concat stack-safe: " + concat(list1, list2));

        System.out.println("reverse via foldRight: " + list1.reverse());
        System.out.println("reverse via foldLeft: " + reverseViaFoldLeft(list2));

        List<List<Integer>> list3 = list(list(1, 3, 4), list2, list1);
        System.out.println("flatten list of list: " + flatten(list3));

        System.out.println("map list: " + list1.map(x -> x * 3));
        System.out.println("filter list: " + list1.filter(x -> x < 20));

        System.out.println("flatMap a list: " + list(1,2,3).flatMap(x -> list(x, -x, x + 1)));
        System.out.println("filter via flatMap: " + filterViaFlatMap(list1, x -> x < 10));

        System.out.println("length high perf: " + list1.length());
    }

    static Integer sum(List<Integer> list) {
        return sum_(0, list).eval();
    }

     static TailCall<Integer> sum_(Integer acc, List<Integer> list) {
        return list.isEmpty()
                ? TailCall.ret(acc)
                : TailCall.sus(() -> sum_(acc + list.head(), list.tail()));
    }

    static Double product(List<Double> list) {
        return product_(1.0, list).eval();
    }

    static TailCall<Double> product_(Double acc, List<Double> list) {
        return list.isEmpty()
                ? ret(acc)
                : sus(() -> product_(acc * list.head(), list.tail()));
    }
}
