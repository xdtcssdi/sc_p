package com.weilai.keke.entity;

public class SignEntity {

    /**
     * success : 1
     * error :
     * className : 大学生就业指导
     * classAddress : A5-210
     * teacherName : 郑俊生
     * sign_time : 25
     * sign_date : 2018-08-10
     * is_sign : true
     */

    private String success;
    private String error;
    private String className;
    private String classAddress;
    private String teacherName;
    private String sign_time;
    private String sign_date;
    private boolean is_sign;
    private String need_sign_time;
    private String mac;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public boolean isIs_sign() {
        return is_sign;
    }

    public String getNeed_sign_time() {
        return need_sign_time;
    }

    public void setNeed_sign_time(String need_sign_time) {
        this.need_sign_time = need_sign_time;
    }

    public void setIs_sign(boolean is_sign) {
        this.is_sign = is_sign;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassAddress() {
        return classAddress;
    }

    public void setClassAddress(String classAddress) {
        this.classAddress = classAddress;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getSign_time() {
        return sign_time;
    }

    public void setSign_time(String sign_time) {
        this.sign_time = sign_time;
    }

    public String getSign_date() {
        return sign_date;
    }

    public void setSign_date(String sign_date) {
        this.sign_date = sign_date;
    }

    @Override
    public String toString() {
        return "SignEntity{" +
                "success='" + success + '\'' +
                ", error='" + error + '\'' +
                ", className='" + className + '\'' +
                ", classAddress='" + classAddress + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", sign_time='" + sign_time + '\'' +
                ", sign_date='" + sign_date + '\'' +
                ", is_sign=" + is_sign +
                ", need_sign_time='" + need_sign_time + '\'' +
                '}';
    }
}
