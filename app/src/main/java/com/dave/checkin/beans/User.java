package com.dave.checkin.beans;

import cn.bmob.v3.BmobObject;

public class User extends BmobObject{
    private String username;
    private String account;
    private String password;
    private boolean isAdmin;

    public User(String username) {
        this.username = username;
        this.account = "";
        this.password = "";
        this.isAdmin = false;
    }

    public User(String username, String account, String password, boolean isAdmin) {
        this.username = username;
        this.account = account;
        this.password = password;
        this.isAdmin = isAdmin;
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
}
