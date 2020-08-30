package com.weilai.keke.entity;

import java.io.Serializable;

public class UserInfo implements Serializable{


    /**
     * username : xdtcssdi
     * stuNo : 16110100704
     * nickname : null
     * telephone : null
     * avatar : 2018/08/TIM%E6%88%AA%E5%9B%BE20180808091658.png
     * is_staff : true
     */

    private String username;
    private String stuNo;
    private String nickname;
    private String telephone;
    private String avatar;
    private String email;
    private boolean is_staff;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isIs_staff() {
        return is_staff;
    }

    public void setIs_staff(boolean is_staff) {
        this.is_staff = is_staff;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "username='" + username + '\'' +
                ", stuNo='" + stuNo + '\'' +
                ", nickname='" + nickname + '\'' +
                ", telephone='" + telephone + '\'' +
                ", avatar='" + avatar + '\'' +
                ", email='" + email + '\'' +
                ", is_staff=" + is_staff +
                '}';
    }
}