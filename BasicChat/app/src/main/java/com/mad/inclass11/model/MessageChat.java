package com.mad.inclass11.model;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sanket on 11/14/2016.
 */
public class MessageChat {

    private String userName;
    private String message;
    private String time;
    private ArrayList<MessageChat> comments;
    private String messageType;
    private String imageUrl;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<MessageChat> getComments() {
        return comments;
    }

    public void setComments(ArrayList<MessageChat> comments) {
        this.comments = comments;
    }
}
