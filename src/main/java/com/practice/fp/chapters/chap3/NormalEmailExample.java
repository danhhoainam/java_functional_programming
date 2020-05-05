package com.practice.fp.chapters.chap3;

import java.util.regex.Pattern;

public class NormalEmailExample {

    static final Pattern emailRegex = Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$");

    static void testEmail(String email) {
        if (emailRegex.matcher(email).matches()) {
            sendVerificationEmail(email);
        } else {
            logError("email " + email + " is invalid");
        }
    }

    static void sendVerificationEmail(String email) {
        System.out.println("Send verification email to: " + email);
    }

    static void logError(String error) {
        System.out.println("Error message: " + error);
    }

    public static void main(String[] args) {
        testEmail("dhn@gmail.com");
        testEmail("dhn11@gmail.comsssss");
    }
}
