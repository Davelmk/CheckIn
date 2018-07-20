package com.dave.checkin.beans;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class User extends BmobObject{
    private String username;
    private String account;
    private String password;
    private boolean isAdmin;
    private List<String> checkInList;

    public User() {
    }

    public User(String username) {
        this.username = username;
        this.account = "";
        this.password = "";
        this.isAdmin = false;
        this.checkInList=null;
    }

    public User(String username, String account, String password, boolean isAdmin) {
        this.username = username;
        this.account = account;
        this.password = password;
        this.isAdmin = isAdmin;
        this.checkInList=null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public List<String> getCheckInList() {
        return checkInList;
    }

    public void setCheckInList(List<String> checkInList) {
        this.checkInList = checkInList;
    }
}
