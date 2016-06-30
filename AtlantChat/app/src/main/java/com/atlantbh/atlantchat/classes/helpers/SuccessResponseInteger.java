package com.atlantbh.atlantchat.classes.helpers;

/**
 * Created by Faruk on 23/06/16.
 */
public class SuccessResponseInteger {
    private boolean success;
    private String message;
    private int data;

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

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
