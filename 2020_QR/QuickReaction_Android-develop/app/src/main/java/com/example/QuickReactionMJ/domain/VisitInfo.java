package com.example.QuickReactionMJ.domain;

import java.time.LocalDateTime;

public class VisitInfo {

    private Long id;
    private User user;
    private Spot spot;
    private LocalDateTime localDateTime;

    public VisitInfo(User user, Spot spot, LocalDateTime localDateTime) {
        this.user = user;
        this.spot = spot;
        this.localDateTime = localDateTime;
    }
}
