package main.notification.api.dto;

import java.time.Instant;
import java.util.UUID;

public class NotificationStatusResponse {
    public UUID id;
    public String status;
    public Instant plannedSendAt;
}
