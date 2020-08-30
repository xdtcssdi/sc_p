package com.example.amicool.iface;

public interface Attentioniface {
    void focus(String mod, int idolid,
                       String SessionID, AttentionListener listener);

    void exists(String mod, int idolid,  String sessionID,AttentionListener listener);

    void unfocus(String mod,int idolid, String sessionID,AttentionListener listener);

}
