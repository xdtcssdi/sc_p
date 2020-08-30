package com.example.amicool.iface;

import retrofit2.http.Path;

public interface Loginiface {
    void getLoginResult(String username,
                        String pass,
                        LoginListener loginListener);

}
