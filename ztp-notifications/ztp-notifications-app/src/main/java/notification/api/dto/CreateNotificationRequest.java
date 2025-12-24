package main.notification.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class CreateNotificationRequest {
    @NotBlank
    public String content;
    @NotBlank
    public String channel;
    @NotBlank
    public String recipient;
    @NotBlank
    public String recipientTimezone;
    @NotBlank
    public String priority;
    @NotNull
    public Instant plannedSendAt;
}
