package com.dave.checkin.beans;

import com.dave.checkin.utils.Utils;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class Group extends BmobObject{
    private String title;
    private String owner;
    private String num;
    private String description;
    private List<String> member;
    private List<String> checkin;
    private transient String ownerName;
    private transient String time;
    private transient String id;
    private transient int backgroundColor;

    public Group() {
    }

    public Group(String title, String owner) {
        this.title = title;
        this.owner = owner;
        this.ownerName = "Dave";
        this.num=0+"";
        this.description="暂无描述";
        this.member=null;
        this.checkin=null;
        this.backgroundColor=Utils.getColor();
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getMember() {
        return member;
    }

    public void setMember(List<String> member) {
        this.member = member;
    }

    public List<String> getCheckin() {
        return checkin;
    }

    public void setCheckin(List<String> checkin) {
        this.checkin = checkin;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
