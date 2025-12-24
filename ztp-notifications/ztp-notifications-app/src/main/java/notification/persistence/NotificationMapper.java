package main.notification.persistence;

import pl.surdel.ztp2.notification.domain.model.ChannelType;
import pl.surdel.ztp2.notification.domain.model.Notification;
import pl.surdel.ztp2.notification.domain.model.NotificationStatus;
import pl.surdel.ztp2.notification.domain.model.Priority;

import java.time.ZoneId;

public class NotificationMapper {
    public static NotificationEntity toEntity(Notification notification) {
        NotificationEntity e = new NotificationEntity();
        e.id = notification.getId();
        e.content = notification.getContent();
        e.channel = notification.getChannel().name();
        e.recipient = notification.getRecipient();
        e.recipientTimezone = notification.getRecipientTimezone().getId();
        e.priority = notification.getPriority().name();
        e.status = notification.getStatus().name();
        e.plannedSendAt = notification.getPlannedSendAt();
        e.retryCount = notification.getRetryCount();
        return e;
    }

    public static Notification toDomain(NotificationEntity e) {
        return Notification.restore(
                e.id,
                e.content,
                ChannelType.valueOf(e.channel),
                e.recipient,
                ZoneId.of(e.recipientTimezone),
                Priority.valueOf(e.priority),
                e.plannedSendAt,
                NotificationStatus.valueOf(e.status),
                e.retryCount
        );
    }
}
