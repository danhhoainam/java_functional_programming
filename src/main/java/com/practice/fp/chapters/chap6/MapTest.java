package com.practice.fp.chapters.chap6;

import com.practice.fp.commons.Function;
import com.practice.fp.commons.List;
import com.practice.fp.commons.Option;

import static com.practice.fp.commons.List.*;

public class MapTest {

    public static void main(String[] args) {

        Map<String, Student> studentMap = new Map<String, Student>()
                .put("nam1", new Student("nam1", "nguyen", "dhn1@gmail.com"))
                .put("nam2", new Student("nam2", "nguyen"))
                .put("nam3", new Student("nam3", "nguyen", "dhn3@gmail.com"));

        Option<String> nam1 = studentMap.get("nam1").flatMap(student -> student.getEmail());
        Option<String> nam2 = studentMap.get("nam2").flatMap(student -> student.getEmail());
        Option<String> nam3 = studentMap.get("nam4").flatMap(student -> student.getEmail());

        System.out.println(nam1.getOrElse(() -> "no data"));
        System.out.println(nam2.getOrElse(() -> "no data"));
        System.out.println(nam3.getOrElse(() -> "no data"));

        System.out.println("variance: " + variance.apply(list(1D, 6D, 19D, 30D)));
        System.out.println("variance: " + variance.apply(list(1D, 1D, 1D, 1D)));

        Function<Option<String>, Option<String>> upperOption = Option.lift(x -> x.toUpperCase());
        System.out.println("lift: " + upperOption.apply(Option.some("aafas34324gi")));
    }

    static Function<List<Double>, Option<Double>> variance =
            numbers -> MapTest.mean.apply(numbers)
                    .flatMap(m -> MapTest.mean.apply(numbers.map(x -> Math.pow(x - m, 2))));

    static Function<List<Double>, Double> sum = numbers -> numbers.foldLeft(0D, x -> y -> x + y);

    static Function<List<Double>, Option<Double>> mean =
            numbers -> numbers.isEmpty()
                    ? Option.none()
                    : Option.some(sum.apply(numbers) / numbers.length());

    static class Student {
        private final String firstName;
        private final String lastName;
        private final Option<String> email;

        Student(String firstName, String lastName, String email) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = Option.some(email);
        }

        public Student(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = Option.none();
        }

        public Option<String> getEmail() {
            return email;
        }
    }
}
