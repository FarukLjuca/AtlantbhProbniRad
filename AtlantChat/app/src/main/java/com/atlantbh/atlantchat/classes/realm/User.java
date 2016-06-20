package com.atlantbh.atlantchat.classes.realm;

import io.realm.RealmObject;

/**
 * Created by Faruk on 20/06/16.
 */
public class User extends RealmObject{
    private long id;
    private String name;
    private String email;
    private String password;
    private String imageUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User () {}

    public User(long id, String name, String email, String password, String imageUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
    }
}
