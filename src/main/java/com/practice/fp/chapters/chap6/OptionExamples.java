package com.practice.fp.chapters.chap6;

import com.practice.fp.commons.Function;
import com.practice.fp.commons.List;
import com.practice.fp.commons.Option;

public class OptionExamples {
    public static void main(String[] args) {
//        max().<Integer>apply(List.list(1, 3, 45, 99)).getOrElse(() -> throwRuntimeException());
    }

    static <A extends Comparable<A>> Function<List<A>, Option<A>> max() {
        return xs -> xs.isEmpty()
                ? Option.none()
                : Option.some(xs.foldLeft(xs.head(), x -> y -> x.compareTo(y) > 0 ? x : y));
    }

    static Integer throwRuntimeException() {
        throw new RuntimeException("like to throw??");
    }
}
