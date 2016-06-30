package com.atlantbh.atlantchat.classes.realm;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by Faruk on 20/06/16.
 */
@RealmClass
public class Message extends RealmObject {
    private long userId;
    private String message;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message() {}

    public Message(long userId, String message) {
        this.userId = userId;
        this.message = message;
    }
}
