package com.practice.fp.chapters.chap7;

import com.practice.fp.commons.Either;
import com.practice.fp.commons.Function;
import com.practice.fp.commons.List;

public class EitherTest {

    public static void main(String[] args) {
        Function<List<Integer>, Either<String, Integer>> findMax = EitherTest.max();
        System.out.println("find max: " + findMax.apply(List.list(1, 3, 4, 2, 99)));
        System.out.println("find max: " + findMax.apply(List.list()));
    }

    static <A extends Comparable<A>> Function<List<A>, Either<String, A>> max() {
        return xs -> xs.isEmpty()
                ? Either.left("max called on an empty list")
                : Either.right(xs.foldLeft(xs.head(), x -> y -> x.compareTo(y) > 0 ? x : y));
    }
}
