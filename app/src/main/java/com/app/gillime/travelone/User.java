package com.app.gillime.travelone;

/**
 * Created by Charles on 11/19/16.
 */

public class User {
    public String name;
    public String username;
    public User(){

    }
    public User(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
