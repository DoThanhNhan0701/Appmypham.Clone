package com.example.appbanhang.model;

public class Cart {
    private int id;
    private String name;
    private String price;
    private String images;
    private int amount_cart;

    public Cart() {
    }

    public Cart(int id, String name, String price, String images, int amount_cart) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.images = images;
        this.amount_cart = amount_cart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getAmount_cart() {
        return amount_cart;
    }

    public void setAmount_cart(int amount_cart) {
        this.amount_cart = amount_cart;
    }
}
