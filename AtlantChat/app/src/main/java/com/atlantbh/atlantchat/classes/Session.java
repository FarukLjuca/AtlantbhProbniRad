package com.atlantbh.atlantchat.classes;

/**
 * Created by Faruk on 20/06/16.
 */
public class Session {
    private static long userId;

    public static long getUserId() {
        return userId;
    }

    public static void setUserId(long userId) {
        Session.userId = userId;
    }
}
