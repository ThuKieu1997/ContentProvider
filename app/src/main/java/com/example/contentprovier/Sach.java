package com.example.contentprovier;

public class Sach {
    private int id;
    private String title;
    private String author;

    Sach (){
        this.id = 0;
        this.title = "";
        this.author = "";
    }

    public Sach(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public int getId() {
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Sach{" + "id=" + id + ", title='" + title + '\'' + ", author='" + author + '\'' + '}';
    }

}
