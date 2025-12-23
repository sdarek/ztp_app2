package pl.surdel.ztp2.notification.api.dto;

import java.time.Instant;

public class CreateNotificationRequest {
    public String content;
    public String channel;
    public String recipient;
    public String recipientTimezone;
    public String priority;
    public Instant plannedSendAt;
}
