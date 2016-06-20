package com.atlantbh.atlantchat.classes.realm;

import java.util.Date;

/**
 * Created by Faruk on 20/06/16.
 */
public class Message {
    private String message;
    private int userId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Message () {}

    public Message(String message, int userId) {
        this.message = message;
        this.userId = userId;
    }
}
