package com.dave.checkin.beans;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class UserCheckin extends BmobObject {
    private String user;
    private List<String> checkin;

    public UserCheckin(String user, List<String> checkin) {
        this.user = user;
        this.checkin = checkin;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<String> getCheckin() {
        return checkin;
    }

    public void setCheckin(List<String> checkin) {
        this.checkin = checkin;
    }
}
