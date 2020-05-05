package com.practice.fp.chapters.chap3;

import com.practice.fp.commons.Function;

public class Weight {

    private final double value;
    public static final Weight ZERO = new Weight(0.0);

    private Weight(double value) {
        this.value = value;
    }

    public static Weight weight(double value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Weight must be greater than 0");
        } else {
            return new Weight(value);
        }
    }

    public double getValue() {
        return value;
    }

    public Weight add(Weight that) {
        return new Weight(this.value + that.value);
    }

    public Weight mult(int count) {
        return new Weight(this.value * count);
    }

    @Override
    public String toString() {
        return Double.toString(this.value);
    }

    public static Function<Weight, Function<OrderLine, Weight>> sum = x -> y -> x.add(y.getWeight());
}
