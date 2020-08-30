package com.weilai.keke.entity;

import java.io.Serializable;

public class LoginState implements Serializable{

    /**
     * success : 1
     * sessionId : 325325353252
     */

    private String success;
    private String sessionid;
    private String error;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "LoginState{" +
                "success='" + success + '\'' +
                ", sessionid='" + sessionid + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
