package com.example.amicool.iface;

public interface Articleiface {
    void getResultList(String mod,
                       int page,
                       String sessionID,
                       ArticleListener listener
    );

}
