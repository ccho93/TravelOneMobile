package com.app.gillime.travelone;

/**
 * Created by Charles on 11/20/16.
 */

public class Group {
    private String groupOwnerUID;
    private String title;
    private String name;
    public Group(){

    }
    public Group(String groupOwnerUID, String title, String name) {
        this.groupOwnerUID = groupOwnerUID;
        this.title = title;
        this.name = name;
    }

    public String getGroupOwnerUID() {
        return groupOwnerUID;
    }

    public void setGroupOwnerUID(String groupOwnerUID) {
        this.groupOwnerUID = groupOwnerUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

