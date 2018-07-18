package com.dave.checkin.beans;

public class CheckIn {
    private String title;
    private String owner;
    private String time;
    private String description;
    private int numOfMember;
    private int backgroundColor;
    public CheckIn(String title, String owner, String time,int backgroundColor) {
        this.title = title;
        this.owner = owner;
        this.time = time;
        this.backgroundColor = backgroundColor;
        this.numOfMember=0;
        this.description="暂无描述";
    }

    public CheckIn(String title, String owner, String time,
                        String description, int numOfMember, int backgroundColor) {
        this.title = title;
        this.owner = owner;
        this.time = time;
        this.description = description;
        this.numOfMember = numOfMember;
        this.backgroundColor = backgroundColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumOfMember() {
        return numOfMember;
    }

    public void setNumOfMember(int numOfMember) {
        this.numOfMember = numOfMember;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
