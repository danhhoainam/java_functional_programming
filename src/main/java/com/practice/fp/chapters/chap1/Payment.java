package com.practice.fp.chapters.chap1;

public class Payment {

    public final CreditCard creditCard;
    public final int amount;

    public Payment(CreditCard creditCard, int amount) {
        this.creditCard = creditCard;
        this.amount = amount;
    }

    public static Payment combine(Payment payment1, Payment payment2) {
        if (payment1.creditCard.equals(payment2.creditCard)) {
            return new Payment(payment1.creditCard, payment1.amount + payment2.amount);
        } else {
            throw new IllegalStateException("Can't combine payments to different cards");
        }
    }

    public Payment combine(Payment payment) {
        if (this.creditCard.equals(payment.creditCard)) {
            return new Payment(this.creditCard, this.amount + payment.amount);
        } else {
            throw new IllegalStateException("Can't combine payments to different cards");
        }
    }
}
