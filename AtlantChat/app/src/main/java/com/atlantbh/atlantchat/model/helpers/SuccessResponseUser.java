package com.atlantbh.atlantchat.model.helpers;

import com.atlantbh.atlantchat.model.realm.User;

/**
 * Created by Faruk on 23/06/16.
 */
public class SuccessResponseUser {
    private boolean success;
    private String message;
    private User data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
