package main.pl.surdel.ztp2.notification.api.dto;

import java.time.Instant;
import java.util.UUID;

public class NotificationResponse {
    public UUID id;
    public String status;
    public Instant plannedSendAt;
}
