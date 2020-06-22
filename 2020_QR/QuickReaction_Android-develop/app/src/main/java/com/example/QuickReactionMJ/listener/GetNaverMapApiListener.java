package com.example.QuickReactionMJ.listener;

import com.example.QuickReactionMJ.get.GetNaverMapApiResult;

public interface GetNaverMapApiListener {
    void onSucOrFailEvent(boolean b);
    void onVisitInfoReceiveEvent(GetNaverMapApiResult list);
}
