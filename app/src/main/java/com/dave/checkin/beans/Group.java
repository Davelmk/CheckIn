package com.dave.checkin.beans;

import java.util.List;

public class Group {
    private String title;
    private String owner;
    private String time;
    private String description;
    private int numOfMember;
    private List list;
    private int backgroundColor;

    public Group(String title, String owner, int backgroundColor) {
        this.title = title;
        this.owner = owner;
        this.backgroundColor = backgroundColor;
        this.time="";
        this.numOfMember=0;
        this.list=null;
    }

    public Group(String title, String owner, String time, String description, int numOfMember, List list, int backgroundColor) {
        this.title = title;
        this.owner = owner;
        this.time = time;
        this.description = description;
        this.numOfMember = numOfMember;
        this.list = list;
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

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
