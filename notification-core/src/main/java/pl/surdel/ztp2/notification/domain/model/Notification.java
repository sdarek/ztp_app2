package pl.surdel.ztp2.notification.domain.model;

import lombok.Getter;
import pl.surdel.ztp2.notification.domain.policy.StatusTransitionPolicy;

import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

@Getter
public class Notification {
    private final UUID id;
    private final String content;
    private final ChannelType channel;
    private final String recipient;
    private final ZoneId recipientTimezone;
    private final Priority priority;

    private NotificationStatus status;
    private final Instant plannedSendAt;
    private int retryCount;

    public Notification(
            UUID id,
            String content,
            ChannelType channel,
            String recipient,
            ZoneId recipientTimezone,
            Priority priority,
            Instant plannedSendAt
    ) {
        this.id = id;
        this.content = content;
        this.channel = channel;
        this.recipient = recipient;
        this.recipientTimezone = recipientTimezone;
        this.priority = priority;
        this.plannedSendAt = plannedSendAt;
        this.status = NotificationStatus.CREATED;
        this.retryCount = 0;
    }

    public void changeStatus(NotificationStatus newStatus, StatusTransitionPolicy policy) {
        if (!policy.canTransition(this.status, newStatus)) {
            throw new IllegalStateException(
                    "Illegal status transition from " + this.status + " to " + newStatus
            );
        }
        this.status = newStatus;
    }

    public boolean isTerminal() {
        return status == NotificationStatus.SENT || status == NotificationStatus.FAILED || status == NotificationStatus.CANCELED;
    }
}
