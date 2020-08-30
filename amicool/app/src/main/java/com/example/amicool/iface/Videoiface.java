package com.example.amicool.iface;

public interface Videoiface {
    void getResultList(String mod,
                       int page,
                       String sessionID,
                       VideoListener listener
    );

}
