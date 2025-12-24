package main.notification.api.dto;

import java.time.Instant;
import java.util.UUID;

public class NotificationResponse {
    public UUID id;
    public String status;
    public Instant plannedSendAt;
}
