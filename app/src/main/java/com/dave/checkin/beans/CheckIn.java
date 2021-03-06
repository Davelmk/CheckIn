package com.dave.checkin.beans;

import com.dave.checkin.utils.Utils;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class CheckIn extends BmobObject{
    private String title;
    private String owner;
    private String ownerName;
    private String description;
    private String position;
    private String num;
    private List<String> signList;
    private transient String time;
    private transient String id;
    private transient int backgroundColor;

    public CheckIn() {
    }

    public CheckIn(String title, String owner) {
        this.title = title;
        this.owner = owner;
        this.time = "暂无时间";
        this.position = "暂无定位";
        this.num = 0 + "";
        this.description = "暂无描述";
        this.backgroundColor = Utils.getColor();
        this.signList=null;
    }

    public CheckIn(String title, String owner, String num,String ownerName) {
        this.title = title;
        this.owner = owner;
        this.ownerName = ownerName;
        this.num = num;
        this.time = "暂无时间";
        this.position = "暂无定位";
        this.description = "暂无描述";
        this.backgroundColor = Utils.getColor();
        this.signList=null;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public List<String> getSignList() {
        return signList;
    }

    public void setSignList(List<String> signList) {
        this.signList = signList;
    }
}