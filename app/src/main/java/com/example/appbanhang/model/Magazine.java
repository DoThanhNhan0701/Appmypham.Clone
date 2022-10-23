package com.example.appbanhang.model;

import java.io.Serializable;

public class Magazine implements Serializable {
    private int id;
    private String name_magazine;
    private String images_magazine;
    private String description_magazine;

    public Magazine() {
    }

    public Magazine(int id, String name_magazine, String images_magazine, String description_magazine) {
        this.id = id;
        this.name_magazine = name_magazine;
        this.images_magazine = images_magazine;
        this.description_magazine = description_magazine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_magazine() {
        return name_magazine;
    }

    public void setName_magazine(String name_magazine) {
        this.name_magazine = name_magazine;
    }

    public String getImages_magazine() {
        return images_magazine;
    }

    public void setImages_magazine(String images_magazine) {
        this.images_magazine = images_magazine;
    }

    public String getDescription_magazine() {
        return description_magazine;
    }

    public void setDescription_magazine(String description_magazine) {
        this.description_magazine = description_magazine;
    }
}
