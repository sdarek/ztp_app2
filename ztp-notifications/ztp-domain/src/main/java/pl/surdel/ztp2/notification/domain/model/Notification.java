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

    public Notification(UUID id, String content, ChannelType channel, String recipient,
                        ZoneId recipientTimezone, Priority priority, Instant plannedSendAt) {
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

    public static Notification restore(UUID id, String content, ChannelType channel, String recipient,
                                       ZoneId recipientTimezone, Priority priority, Instant plannedSendAt,
                                       NotificationStatus status, int retryCount) {
        Notification n = new Notification(id, content, channel, recipient, recipientTimezone, priority, plannedSendAt);
        n.status = status;
        n.retryCount = retryCount;
        return n;
    }

    public void changeStatus(NotificationStatus newStatus, StatusTransitionPolicy policy) {
        if (!policy.canTransition(this.status, newStatus)) {
            throw new IllegalStateException("Illegal status transition from " + this.status + " to " + newStatus);
        }
        this.status = newStatus;
    }

    public void markSending() { this.status = NotificationStatus.SENDING; }
    public void markSent() { this.status = NotificationStatus.SENT; }
    public void markFailed() { this.status = NotificationStatus.FAILED; }
    public void markScheduled() { this.status = NotificationStatus.SCHEDULED; }
    public void incrementRetry() { this.retryCount++; }
}