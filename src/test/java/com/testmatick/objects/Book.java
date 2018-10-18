package com.testmatick.objects;

public class Book {
    private String name;
    private String author;
    private String rating;
    private double price;
    private boolean bestSeller;

    public Book(String name, String author, double price, String rating, boolean bestSeller) {
        this.name = name;
        this.author = author;
        this.price = price;
        this.rating = rating;
        this.bestSeller = bestSeller;
    }

    public String getName() {
        return name;
    }
}
