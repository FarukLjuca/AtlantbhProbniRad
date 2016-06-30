package com.atlantbh.atlantchat.classes.helpers;

/**
 * Created by Faruk on 20/06/16.
 *
 * It is generic because data can be different for every type of request
 * For user registration, it is ID, for get User details it is User object...
 */
public class SuccessResponse {
    private boolean success;
    private String message;

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
}
