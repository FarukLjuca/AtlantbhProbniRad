package com.example.faruk.learningcontentproviders.Classes;

/**
 * Created by Faruk on 07/06/16.
 */
public class MyMessage {
    private String author;
    private String text;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MyMessage(String author, String text) {
        this.author = author;
        this.text = text;
    }
}
