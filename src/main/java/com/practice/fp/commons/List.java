package com.practice.fp.commons;

import static com.practice.fp.commons.TailCall.ret;
import static com.practice.fp.commons.TailCall.sus;

public abstract class List<A> {

    private List() {}

    // get the first element
    public abstract A head();
    // get the tail list
    public abstract List<A> tail();
    // check list is empty or not
    public abstract Boolean isEmpty();
    // add an element to the head of the list
    public abstract List<A> cons(A a);
    // replace first element
    public abstract List<A> setHead(A a);
    // convert list to String
    public abstract String toString();
    // drop n first element
    public abstract List<A> drop(int n);
    // method to remove elements from the head of the List as long as a condition holds true
    public abstract List<A> dropWhile(Function<A, Boolean> f);
    // reverse the list
    public abstract List<A> reverse();
    // remove the last element from a list
    public abstract List<A> init();
    // length by memoization
    // actually, we can use foldRight to count or imperative style to count
    // but this is the best way to increase the performance
    public abstract int length();
    // foldLeft stack-safe
    public abstract <B> B foldLeft(B identity, Function<B, Function<A, B>> f);
    // foldRight stack-safe
    public abstract <B> B foldRight(B identity, Function<A, Function<B, B>> f);
    // headOption method in List<A> that will return a Result<A>
    public abstract Result<A> headOption();

    /**
     * transform a list to a list of different values
     * @param f
     * @param <B>
     * @return
     */
    public <B> List<B> map(Function<A, B> f) {
        return foldRight(list(), h -> t -> new Cons<>(f.apply(h),t));
    }

    /**
     * removes from a list the elements that don’t satisfy a given predicate
     * @param f
     * @return
     */
    public List<A> filter(Function<A, Boolean> f) {
        return foldRight(list(), h -> t -> f.apply(h) ? new Cons<>(h,t) : t);
    }

    /**
     * method that applies to each element of List<A> a function from A to List<B>, and returns a List<B>
     * example: List.list(1,2,3).flatMap(i -> List.list(i, -i)) -> list(1,-1,2,-2,3,-3)
     * @param f
     * @param <B>
     * @return
     */
    public <B> List<B> flatMap(Function<A, List<B>> f) {
        return foldRight(list(), h -> t -> concat(f.apply(h), t));
    }

    /**
     * @return returning a Result of the last element in the list
     */
    public Result<A> lastOption() {
        return foldLeft(Result.empty(), x -> y -> Result.success(y));
    }

    private static class Nil<A> extends List<A> {

        private Nil() {}

        @Override
        public A head() {
            throw new IllegalStateException("Head called for empty list");
        }

        @Override
        public List<A> tail() {
            throw new IllegalStateException("Tail called for empty list");
        }

        @Override
        public Boolean isEmpty() {
            return true;
        }

        @Override
        public List<A> cons(A a) {
            return new Cons<>(a, this);
        }

        @Override
        public List<A> setHead(A a) {
            throw new IllegalStateException("set head called for empty list");
        }

        @Override
        public String toString() {
            return "[NIL]";
        }

        @Override
        public List<A> drop(int n) {
            return this;
        }

        @Override
        public List<A> dropWhile(Function<A, Boolean> f) {
            return this;
        }

        @Override
        public List<A> reverse() {
            return this;
        }

        @Override
        public List<A> init() {
            throw new IllegalStateException("init called for empty list");
        }

        @Override
        public int length() {
            return 0;
        }

        @Override
        public <B> B foldLeft(B identity, Function<B, Function<A, B>> f) {
            return identity;
        }

        @Override
        public <B> B foldRight(B identity, Function<A, Function<B, B>> f) {
            return identity;
        }

        @Override
        public Result<A> headOption() {
            return Result.empty();
        }
    }

    private static class Cons<A> extends List<A> {

        private final A head;
        private final List<A> tail;
        private final int length;

        private Cons(A head, List<A> tail) {
            this.head = head;
            this.tail = tail;
            this.length = tail.length() + 1;
        }

        @Override
        public A head() {
            return this.head;
        }

        @Override
        public List<A> tail() {
            return this.tail;
        }

        @Override
        public Boolean isEmpty() {
            return false;
        }

        @Override
        public List<A> cons(A a) {
            return new Cons<>(a, this);
        }

        @Override
        public List<A> setHead(A a) {
            return new Cons<>(a, tail());
        }

        @Override
        public String toString() {
            return String.format("[%sNIL]", toString_(new StringBuilder(), this).eval());
        }

        private TailCall<StringBuilder> toString_(StringBuilder acc, List<A> list) {
            return list.isEmpty()
                    ? ret(acc)
                    : sus(() -> toString_(acc.append(list.head()).append(", "), list.tail()));
        }

        @Override
        public List<A> drop(int n) {
            return drop_(this, n).eval();
        }

        private TailCall<List<A>> drop_(List<A> list, int n) {
            return n <= 0 || list.isEmpty()
                    ? ret(list)
                    : sus(() -> drop_(list.tail(), n - 1));
        }

        @Override
        public List<A> dropWhile(Function<A, Boolean> f) {
            return dropWhile_(this, f).eval();
        }

        // list().dropWhile(f) can't work because java needs to infer type
        private TailCall<List<A>> dropWhile_(List<A> list, Function<A, Boolean> f) {
            return !list.isEmpty() && f.apply(list.head())
                    ? sus(() -> dropWhile_(list.tail(), f))
                    : ret(list);
        }

        @Override
        public List<A> reverse() {
            return reverse_(list(), this).eval();
        }

        private TailCall<List<A>> reverse_(List<A> acc, List<A> list) {
            return list.isEmpty()
                    ? ret(acc)
                    : sus(() -> reverse_(new Cons<>(list.head(), acc), list.tail()));
        }

        @Override
        public List<A> init() {
            return reverse().tail().reverse();
        }

        @Override
        public int length() {
            return this.length;
        }

        @Override
        public <B> B foldLeft(B identity, Function<B, Function<A, B>> f) {
            return foldLeft_(identity, this, f).eval();
        }

        private <B> TailCall<B> foldLeft_(B acc, List<A> list, Function<B, Function<A, B>> f) {
            return list.isEmpty()
                    ? ret(acc)
                    : sus(() -> foldLeft_(f.apply(acc).apply(list.head()), list.tail(), f));
        }

        @Override
        public <B> B foldRight(B identity, Function<A, Function<B, B>> f) {
            return foldRight_(identity, this.reverse(), f).eval();
        }

        private <B> TailCall<B> foldRight_(B acc, List<A> list, Function<A, Function<B, B>> f) {
            return list.isEmpty()
                    ? ret(acc)
                    : sus(() -> foldRight_(f.apply(list.head()).apply(acc), list.tail(), f));
        }

        @Override
        public Result<A> headOption() {
            return Result.success(head);
        }
    }

    // singleton for empty list
    @SuppressWarnings("rawtypes")
    public static final List NIL = new Nil();

    /**
     * init empty list
     * @param <A>
     * @return
     */
    @SuppressWarnings("uncheked")
    public static <A> List<A> list() {
        return NIL;
    }

    /**
     * singly linked list -> stack
     * so that we have to traverse
     * from the bottom of the list of values
     * We use imperative solution for performance here
     * @param a
     * @param <A>
     * @return
     */
    @SafeVarargs
    public static <A> List<A> list(A... a) {
        List<A> list = list();
        for (int i = a.length - 1; i >= 0; i--) {
            list = new Cons<>(a[i], list);
        }
        return list;
    }

    /**
     * static method for abstract setHead
     * @param list
     * @param a
     * @param <A>
     * @return
     */
    public static <A> List<A> setHead(List<A> list, A a) {
        return list.setHead(a);
    }

    /**
     *
     * @param list
     * @param n
     * @param <A>
     * @return
     */
    public static <A> List<A> drop(List<A> list, int n) {
        return list.drop(n);
    }

    /**
     *
     * @param list
     * @param f
     * @param <A>
     * @return
     */
    public static <A> List<A> dropWhile(List<A> list, Function<A, Boolean> f) {
        return list.dropWhile(f);
    }

    /**
     *
     * @param list1
     * @param list2
     * @param <A>
     * @return
     */
    public static <A> List<A> concat(List<A> list1, List<A> list2) {
        return foldRight(list1, list2, x -> y -> new Cons<>(x, y));
    }

    /**
     * reverse the list1
     * prepend list1 items one by one to the left of list2
     * @param list1
     * @param list2
     * @param <A>
     * @return
     */
    public static <A> List<A> concatViaFoldLeft(List<A> list1, List<A> list2) {
        return list1.reverse().foldLeft(list2, x -> y -> x.cons(y));
    }

    /**
     *
     * @param list
     * @param identity
     * @param f
     * @param <A>
     * @param <B>
     * @return
     */
    public static <A, B> B foldRight(List<A> list, B identity, Function<A, Function<B, B>> f) {
        return list.reverse().foldLeft(identity, acc -> item -> f.apply(item).apply(acc));
    }

    /**
     *
     * @param list
     * @param <A>
     * @return
     */
    public static <A> List<A> reverseViaFoldLeft(List<A> list) {
        return list.foldLeft(list(), x -> a -> x.cons(a));
    }

    /**
     * flatten list of list
     * there’s a strong relation between map, flatten, and flatMap.
     * If you map a function returning a list to a list, you get a list of lists
     * then apply flatten to get a single list containing all the elements of the enclosed lists
     * @param list
     * @param <A>
     * @return
     */
    public static <A> List<A> flatten(List<List<A>> list) {
        return list.flatMap(x -> x);
    }

    /**
     *
     * @param list
     * @param f
     * @param <A>
     * @param <B>
     * @return
     */
    public static <A, B> List<B> map(List<A> list, Function<A, B> f) {
        return list.map(f);
    }

    public static <A> List<A> filterViaFlatMap(List<A> list, Function<A, Boolean> f) {
        return list.flatMap(x -> f.apply(x) ? list(x) : list());
    }
}
