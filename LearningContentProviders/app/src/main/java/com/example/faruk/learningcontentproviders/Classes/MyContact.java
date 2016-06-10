package com.example.faruk.learningcontentproviders.Classes;

/**
 * Created by Faruk on 07/06/16.
 */
public class MyContact {
    private int id;
    private String displayName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public MyContact(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }
}
