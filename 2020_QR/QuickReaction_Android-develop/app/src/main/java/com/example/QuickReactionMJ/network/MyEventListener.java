package com.example.QuickReactionMJ.network;

public interface MyEventListener {
    void onMyEvent(boolean b);
    void onTokenReceiveEvent(boolean b, String Token, String Role, Long id);


}
