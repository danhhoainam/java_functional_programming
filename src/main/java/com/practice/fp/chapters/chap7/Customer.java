package com.practice.fp.chapters.chap7;

import com.practice.fp.commons.Result;

public class Customer {

    private final String firstName;
    private final String lastName;
    private final Result<String> email;

    public Customer(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = Result.success(email);
    }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = Result.empty();
    }

    public Result<String> getEmail() {
        return email;
    }
}
