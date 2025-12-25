package pl.surdel.ztp2.notification.domain.service;

import jakarta.enterprise.context.ApplicationScoped;
import pl.surdel.ztp2.notification.domain.model.Notification;
import pl.surdel.ztp2.notification.domain.model.NotificationStatus;
import pl.surdel.ztp2.notification.domain.policy.QuietHoursPolicy;
import pl.surdel.ztp2.notification.domain.policy.StatusTransitionPolicy;

@ApplicationScoped
public class NotificationService {
    private static final int MAX_RETRIES = 3;
    private final StatusTransitionPolicy statusPolicy;
    private final QuietHoursPolicy quietHoursPolicy;

    public NotificationService(StatusTransitionPolicy statusPolicy, QuietHoursPolicy quietHoursPolicy) {
        this.statusPolicy = statusPolicy;
        this.quietHoursPolicy = quietHoursPolicy;
    }

    public boolean canBeSentNow(Notification notification) {
        return !quietHoursPolicy.isWithinQuietHours(notification.getPlannedSendAt(), notification.getRecipientTimezone());
    }

    public void markScheduled(Notification notification) {
        notification.changeStatus(NotificationStatus.SCHEDULED, statusPolicy);
    }

    public void markSending(Notification notification) {
        notification.changeStatus(NotificationStatus.SENDING, statusPolicy);
    }

    public void cancel(Notification notification) {
        notification.changeStatus(NotificationStatus.CANCELED, statusPolicy);
    }

    public void handleSendResult(Notification notification, boolean success) {
        if (success) {
            notification.changeStatus(NotificationStatus.SENT, statusPolicy);
            return;
        }

        // porażka
        notification.incrementRetry();

        if (notification.getRetryCount() >= MAX_RETRIES) {
            notification.changeStatus(NotificationStatus.FAILED_FINAL, statusPolicy);
        } else {
            notification.changeStatus(NotificationStatus.FAILED_RETRY, statusPolicy);
            notification.changeStatus(NotificationStatus.SCHEDULED, statusPolicy);
        }
    }

    public void forceSend(Notification notification) {
        switch (notification.getStatus()) {
            case CREATED, SCHEDULED, FAILED_RETRY -> {
                notification.changeStatus(NotificationStatus.SCHEDULED, statusPolicy);
            }
            default -> throw new IllegalStateException(
                    "Cannot force send notification with status " + notification.getStatus()
            );
        }
    }
}