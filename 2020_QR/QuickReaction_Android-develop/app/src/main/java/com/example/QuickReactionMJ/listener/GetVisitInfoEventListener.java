package com.example.QuickReactionMJ.listener;

import com.example.QuickReactionMJ.get.GetVisitInfoResult;

import java.util.List;

public interface GetVisitInfoEventListener {
    void onSucOrFailEvent(boolean b);
    void onVisitInfoReceiveEvent(List<GetVisitInfoResult> list);
}
