package com.practice.fp.chapters.chap6;

import com.practice.fp.commons.Function;
import com.practice.fp.commons.List;
import com.practice.fp.commons.Option;

public class OptionExamples {
    public static void main(String[] args) {
//        max().<Integer>apply(List.list(1, 3, 45, 99)).getOrElse(() -> throwRuntimeException());\

        testSequenceOption();
    }

    static <A extends Comparable<A>> Function<List<A>, Option<A>> max() {
        return xs -> xs.isEmpty()
                ? Option.none()
                : Option.some(xs.foldLeft(xs.head(), x -> y -> x.compareTo(y) > 0 ? x : y));
    }

    static Integer throwRuntimeException() {
        throw new RuntimeException("like to throw??");
    }

    static void testSequenceOption() {
        Function<Integer, Function<String, Integer>> parseWithRadix =
                radix -> string -> Integer.parseInt(string, radix);
        Function<String, Option<Integer>> parse16 =
                Option.hlift(parseWithRadix.apply(16));
        List<String> list = List.list("4", "5", "6", "7", "8", "9", "12");
        Option<List<Integer>> result = Option.sequence(list.map(parse16));

        System.out.println(result);
    }
}
