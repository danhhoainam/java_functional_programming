package com.practice.fp.chapters.chap1;

import com.practice.fp.commons.Tuple;

public class DonutShop {

    public static Tuple<Donut, Payment> buyDonut(CreditCard creditCard) {
        Donut donut = new Donut();
        Payment payment = new Payment(creditCard, donut.price);
        return new Tuple<>(donut, payment);
    }
}
