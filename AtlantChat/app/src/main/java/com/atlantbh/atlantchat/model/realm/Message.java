package com.atlantbh.atlantchat.model.realm;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by Faruk on 20/06/16.
 */
@RealmClass
public class Message extends RealmObject {
    private long userId;
    private long owner;
    private String message;
    private Date time;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getOwner() {
        return owner;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Message(long id, long owner, String messageText, Date time) {
        this.userId = id;
        this.owner = owner;
        this.message = messageText;
        this.time = time;
    }

    public Message() {

    }
}
