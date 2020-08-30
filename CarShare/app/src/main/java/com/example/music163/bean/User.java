package com.example.music163.bean;

public class User {
    /**
     * uid : 14
     * username : test1234
     * password : 16d7a4fca7442dda3ad93c9a726597e4
     * session : 091dd6ab084cc8b2afad
     * avatar : 20190305/1b81791d2dfaebe010665c4ad848d1da.png
     * nickname : hahahah
     * mobilphone : 15088893307
     */

    private int uid;
    private String username;
    private String password;
    private String session;
    private String avatar;
    private String nickname;
    private String mobilphone;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobilphone() {
        return mobilphone;
    }

    public void setMobilphone(String mobilphone) {
        this.mobilphone = mobilphone;
    }
}
