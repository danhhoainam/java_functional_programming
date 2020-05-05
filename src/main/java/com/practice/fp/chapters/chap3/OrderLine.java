package com.practice.fp.chapters.chap3;

public class OrderLine {

    private Product product;
    private int count;

    public OrderLine(Product product, int count) {
        this.product = product;
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public int getCount() {
        return count;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Weight getWeight() {
        return this.product.getWeight().mult(this.count);
    }

    public Price getAmount() {
        return this.product.getPrice().mult(count);
    }
}
