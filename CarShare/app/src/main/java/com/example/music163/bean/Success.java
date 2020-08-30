package com.example.music163.bean;

public class Success {
    /**
     * error : null
     * success : 1
     */

    private String error;
    private int success;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "Success{" +
                "error='" + error + '\'' +
                ", success=" + success +
                '}';
    }
}
