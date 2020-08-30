package com.example.amicool.iface;

public interface Registeriface {
    void getRegisterResult(String username,
                           String pass,
                           String tel,
                           String roleid,
                           String email,
                           RegisterListener registerListener);
}