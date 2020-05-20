package com.practice.fp.chapters.chap3;

import com.practice.fp.commons.Case;
import com.practice.fp.commons.Effect;
import com.practice.fp.commons.Function;
import com.practice.fp.collections.Result;

import java.util.regex.Pattern;

public class FunctionalEmailExample {

    static final Pattern emailPattern = Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$");

    static Effect<String> success = s -> System.out.println("Send verification email to: " + s);

    static Effect<String> failure = s -> System.out.println("Error message: " + s);

    static Function<String, Result<String>> emailChecker = email -> Case.match(
            Case.matchCase(() -> Result.success(email)),
            Case.matchCase(() -> email == null, () -> Result.failure("email must not be null")),
            Case.matchCase(() -> email.trim().length() == 0, () -> Result.failure("email must not be empty")),
            Case.matchCase(() -> !emailPattern.matcher(email).matches(), () -> Result.failure("email " + email + " is invalid"))
    );

    public static void main(String[] args) {
        emailChecker.apply(null).bind(success, failure);
        emailChecker.apply("").bind(success, failure);
        emailChecker.apply("dhn@gmail.com").bind(success, failure);
        emailChecker.apply("dhn").bind(success, failure);
    }
}
