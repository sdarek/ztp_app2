package pl.surdel.ztp2.notification.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notifications")
public class NotificationEntity {

    @Id
    @Column(nullable = false, updatable = false)
    public UUID id;

    @Column(nullable = false, length = 1000)
    public String content;

    @Column(nullable = false)
    public String channel;

    @Column(nullable = false)
    public String recipient;

    @Column(name = "recipient_timezone", nullable = false)
    public String recipientTimezone;

    @Column(nullable = false)
    public String priority;

    @Column(nullable = false)
    public String status;

    @Column(name = "planned_send_at", nullable = false)
    public Instant plannedSendAt;

    @Column(name = "retry_count", nullable = false)
    public int retryCount;
}
