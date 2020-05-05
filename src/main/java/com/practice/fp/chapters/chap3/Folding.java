package com.practice.fp.chapters.chap3;

import com.practice.fp.commons.Effect;
import com.practice.fp.commons.Executable;
import com.practice.fp.commons.Function;

import java.util.List;

import static com.practice.fp.collections.CollectionUtilities.*;

public class Folding {

    public static void main(String[] args) {
        List<Integer> numbers = list(1, 2, 3, 4, 5);

        //
        Integer sum = fold(numbers, 0, x -> y -> x + y);
        Integer mult = fold(numbers, 1, x -> y -> x * y);

        System.out.println("fold sum: " + sum);
        System.out.println("fold multiply: " + mult);

        // foldLeft example:
        Function<String, Function<Integer, String>> concatFoldLeft = x -> y -> addStringInteger(x, y);
        // foldLeft expectation: (((((0 + 1) + 2) + 3) + 4) + 5)
        String foldedLeftList = foldLeft(numbers, "0", concatFoldLeft);
        System.out.println("foldedLeftList: " + foldedLeftList);

        // foldRight example:
        Function<Integer, Function<String, String>> concatFoldRight = x -> y -> addIntegerString(x, y);
        // foldRight expectation: (1 + (2 + (3 + (4 + (5 + 0)))))
        String foldedRightList = foldRight(numbers, "0", concatFoldRight);
        System.out.println("foldedRightList: " + foldedRightList);

        // foldRightRecursive example:
        Function<Integer, Function<String, String>> concatFoldRightRecursive = x -> y -> addIntegerString(x, y);
        // foldRightRecursive expectation: (1 + (2 + (3 + (4 + (5 + 0)))))
        String foldRightRecursiveList = foldRightRecursive(numbers, "0", concatFoldRightRecursive);
        System.out.println("foldRightRecursiveList: " + foldRightRecursiveList);

        // reverse
        System.out.println("reverse list: " + reverse(numbers));
        System.out.println("reverseFoldLeft list: " + reverseFoldLeft(numbers));

        // mapping
        System.out.println("map: " + map(numbers, x -> x * 10));
        System.out.println("map fold left: " + mapViaFoldLeft(numbers, x -> x + 5));
        System.out.println("map fold right: " + mapViaFoldRight(numbers, x -> x + 10));

        // compose map
        taxExample();
    }

    static void taxExample() {
        Function<Double, Double> addTax = x -> x * 0.9;
        Function<Double, Double> addShipping = x -> x * 0.5;
        List<Double> prices = list(12.4d, 17.5d, 19d);

        System.out.println("prices: " + map(prices, addTax.andThen(addShipping)));
        forEach(prices, printPriceWith2Decimals);

        // identity: () -> {}
        // compose printers: (((() -> {} + () -> print(12.40)) + () -> print(17.50)) + () -> print(19.00))
        Executable printWithExecutable =
                foldLeft(prices, () -> {}, e -> d -> composeExecutable.apply(e).apply(() -> printPriceWith2Decimals.apply(d)));
        printWithExecutable.exec();
    }

    static Effect<Double> printPriceWith2Decimals = x -> {
        System.out.printf("%.2f", x);
        System.out.println("");
    };

    static Function<Executable, Function<Executable, Executable>> composeExecutable =
            x -> y -> () -> {
                x.exec();
                y.exec();
            };

    static Integer fold(List<Integer> list, Integer identity, Function<Integer, Function<Integer, Integer>> f) {
        Integer result = identity;

        for (Integer item : list) {
            result = f.apply(result).apply(item);
        }

        return result;
    }

    static String addStringInteger(String s, Integer i) {
        return "(" + s + " + " + i + ")";
    }

    static String addIntegerString(Integer i, String s) {
        return "(" + i + " + " + s + ")";
    }
}
