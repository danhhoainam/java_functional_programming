package com.practice.fp.collections;

import com.practice.fp.commons.Effect;
import com.practice.fp.commons.Function;
import com.practice.fp.commons.TailCall;

import java.util.*;

import static com.practice.fp.commons.TailCall.*;

/**
 * class to support normal java Collections in functional way
 */
public class CollectionUtilities {

    /**
     * @param <T>
     * @return
     */
    public static <T> List<T> list() {
        return Collections.emptyList();
    }

    /**
     * @param t
     * @param <T>
     * @return
     */
    public static <T> List<T> list(T t) {
        return Collections.singletonList(t);
    }

    /**
     * @param ts
     * @param <T>
     * @return
     */
    public static <T> List<T> list(List<T> ts) {
        return Collections.unmodifiableList(new ArrayList<>(ts));
    }

    /**
     * @param t
     * @param <T>
     * @return
     */
    @SafeVarargs
    public static <T> List<T> list(T... t) {
        return Collections.unmodifiableList(Arrays.asList(Arrays.copyOf(t, t.length)));
    }

    /**
     * @param list
     * @param <T>
     * @return
     */
    public static <T> T head(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalStateException("head of empty list");
        }
        return list.get(0);
    }

    /**
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> copy(List<T> list) {
        return new ArrayList<>(list);
    }

    /**
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> tail(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalStateException("tail of empty list");
        }
        List<T> copiedList = copy(list);
        copiedList.remove(0);
        return Collections.unmodifiableList(copiedList);
    }

    /**
     * @param list
     * @param t
     * @param <T>
     * @return
     */
    public static <T> List<T> append(List<T> list, T t) {
        List<T> newList = copy(list);
        newList.add(t);
        return Collections.unmodifiableList(newList);
    }

    /**
     * @param list
     * @param f
     * @param <T>
     * @param <U>
     * @return
     */
    public static <T, U> List<U> map(List<T> list, Function<T, U> f) {
        List<U> newList = new ArrayList<>();
        for (T item : list) {
            newList.add(f.apply(item));
        }

        return newList;
    }

    public static <T, U> U foldLeft(List<T> list, U identity, Function<U, Function<T, U>> f) {
        U result = identity;

        for (T item : list) {
            result = f.apply(result).apply(item);
        }

        return result;
    }

    public static <T, U> U foldLeftRecursive(List<T> list, U identity, Function<U, Function<T, U>> f) {
        return foldLeftRecursive_(list, identity, f).eval();
    }

    private static <T, U> TailCall<U> foldLeftRecursive_(List<T> list, U identity, Function<U, Function<T, U>> f) {
        return list.isEmpty()
                ? ret(identity)
                : sus(() -> foldLeftRecursive_(tail(list), f.apply(identity).apply(head(list)), f));
    }

    public static <T, U> U foldRight(List<T> list, U identity, Function<T, Function<U, U>> f) {
        U result = identity;

        for (int i = list.size(); i > 0; i--) {
            result = f.apply(list.get(i - 1)).apply(result);
        }

        return result;
    }

    public static <T, U> U foldRightRecursive(List<T> list, U identity, Function<T, Function<U, U>> f) {
        return foldRightRecursive_(reverse(list), identity, f).eval();
    }

    private static <T, U> TailCall<U> foldRightRecursive_(List<T> list, U identity, Function<T, Function<U, U>> f) {
        return list.isEmpty()
                ? ret(identity)
                : sus(() -> foldRightRecursive_(tail(list), f.apply(head(list)).apply(identity), f));
    }

    public static <T> List<T> reverse(List<T> list) {
        List<T> result = new ArrayList<>();

        for (int i = list.size() - 1; i >= 0; i--) {
            result.add(list.get(i));
        }

        return Collections.unmodifiableList(result);
    }

    /**
     *
     * @param t
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> prepend(T t, List<T> list) {
        return foldLeft(list, list(t), x -> y -> append(x, y));
    }

    /**
     * get item from left side of original list
     * prepend item to new empty list
     * performance is slow because of traversing through list several times
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> reverseFoldLeft(List<T> list) {
        return foldLeft(list, list(), x -> y -> prepend(y, x));
    }

    /**
     * append the transformed item to new empty list
     * traverse from the left of the list
     * @param list
     * @param f
     * @param <T>
     * @param <U>
     * @return
     */
    public static <T, U> List<U> mapViaFoldLeft(List<T> list, Function<T, U> f) {
        return foldLeft(list, list(), x -> y -> append(x, f.apply(y)));
    }

    /**
     * prepend the transformed item to new empty list
     * @param list
     * @param f
     * @param <T>
     * @param <U>
     * @return
     */
    public static <T, U> List<U> mapViaFoldRight(List<T> list, Function<T, U> f) {
        return foldRight(list, list(), x -> y -> prepend(f.apply(x), y));
    }

    /**
     *
     * @param collection
     * @param e
     * @param <T>
     */
    public static <T> void forEach(Collection<T> collection, Effect<T> e) {
        for (T t : collection) e.apply(t);
    }

    /**
     *
     * @param seed
     * @param f
     * @param p
     * @param <T>
     * @return
     */
    public static <T> List<T> unfold(T seed, Function<T, T> f, Function<T, Boolean> p) {
        List<T> result = new ArrayList<>();
        T temp = seed;

        while (p.apply(temp)) {
            result = append(result, temp);
            temp = f.apply(temp);
        }

        return Collections.unmodifiableList(result);
    }

    public static List<Integer> range(int start, int end) {
        return unfold(start, x -> x + 1, x -> x < end);
    }

    public static <T> List<T> iterate(T seed, Function<T, T> f, int n) {
        List<T> result = new ArrayList<>();
        T temp = seed;
        for (int i = 0; i < n; i++) {
            result.add(temp);
            temp = f.apply(temp);
        }
        return result;
    }

    public static <T> List<T> iterate(T seed, Function<T, T> f, Function<T, Boolean> p) {
        List<T> result = new ArrayList<>();
        T temp = seed;
        while (p.apply(temp)) {
            result.add(temp);
            temp = f.apply(temp);
        }
        return result;
    }

    static <T> Function<T, T> composeAllViaFoldLeft(List<Function<T, T>> list) {
        return x -> foldLeftRecursive(reverse(list), x, a -> b -> b.apply(a));
    }

    static <T> Function<T, T> composeAllViaFoldRight(List<Function<T, T>> list) {
        return x -> foldRightRecursive(list, x, a -> b -> a.apply(b));
    }

    static <T> Function<T, T> andThenAllViaFoldLeft(List<Function<T, T>> list) {
        return x -> foldLeftRecursive(list, x, a -> b -> b.apply(a));
    }

    static <T> Function<T, T> andThenAllViaFoldRight(List<Function<T, T>> list) {
        return x -> foldRightRecursive(reverse(list), x, a -> b -> a.apply(b));
    }
}