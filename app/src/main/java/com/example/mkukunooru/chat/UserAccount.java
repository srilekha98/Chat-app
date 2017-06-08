package com.example.mkukunooru.chat;

/**
 * Created by mkukunooru on 6/8/2017.
 */

public class UserAccount {

    private String username;     //email
    private String password;
    private String displayname;

    public String getUsername() {
        return username;
    }

    public void setUsername(String messageText) {
        this.username = messageText;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String messageUser) {
        this.password = messageUser;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String messageTime) {
        this.displayname = messageTime;
    }



}
