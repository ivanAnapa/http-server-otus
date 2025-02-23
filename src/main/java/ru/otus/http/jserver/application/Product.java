package ru.otus.http.jserver.application;

public class Product {
    private Integer id;
    private String title;

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Product() {
    }

    public Product(int id, String title) {
        this.id = id;
        this.title = title;
    }
}
