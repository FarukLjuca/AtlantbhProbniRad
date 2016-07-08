package com.atlantbh.atlantchat.model;

import com.atlantbh.atlantchat.model.realm.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faruk on 20/06/16.
 *
 * Class for one session of user
 *
 * In order to save memory, we will not get all users all the time, but only those who are actively chatting with us
 */
public class Session {
    private static long userId;
    private static List<User> users;

    public static long getUserId() {
        return userId;
    }

    public static void setUserId(long userId) {
        Session.userId = userId;
    }

    public static List<User> getUsers() {
        if (users == null) {
            users = new ArrayList<>();
        }
        return users;
    }

    public static void setUsers(List<User> users) {
        Session.users = users;
    }
}
