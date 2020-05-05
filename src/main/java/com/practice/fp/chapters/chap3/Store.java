package com.practice.fp.chapters.chap3;

import static com.practice.fp.collections.CollectionUtilities.*;

import java.util.List;

public class Store {

    public static void main(String[] args) {
        Product toothPaste = new Product("Tooth paste", Price.price(1.5), Weight.weight(0.5));
        Product toothBrush = new Product("Tooth brush", Price.price(3.5), Weight.weight(0.3));
        List<OrderLine> orders = list(
                new OrderLine(toothPaste, 2),
                new OrderLine(toothBrush, 3));

        Price price = foldLeft(orders, Price.ZERO, Price.sum);
        Weight weight = foldLeft(orders, Weight.ZERO, Weight.sum);

        System.out.println("total price: " + price.toString());
        System.out.println("total weight: " + weight.toString());
    }
}
