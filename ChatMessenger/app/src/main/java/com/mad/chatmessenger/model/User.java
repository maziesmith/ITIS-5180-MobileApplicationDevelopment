package com.mad.chatmessenger.model;

import java.util.HashMap;

/**
 * Created by Sanket on 11/18/2016.
 */

public class User {
    private String firstName;
    private String lastName;
    private String imagePath;
    private String userID;
    private String gender;
    private HashMap<String, Integer> unreadMessageInfo;

    public User() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public HashMap<String, Integer> getUnreadMessageInfo() {
        return unreadMessageInfo;
    }

    public void setUnreadMessageInfo(HashMap<String, Integer> unreadMessageInfo) {
        this.unreadMessageInfo = unreadMessageInfo;
    }
}
