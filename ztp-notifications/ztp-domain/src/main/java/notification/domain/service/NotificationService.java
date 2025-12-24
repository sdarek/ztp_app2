package notification.domain.service;

import jakarta.enterprise.context.ApplicationScoped;
import pl.surdel.ztp2.notification.domain.model.Notification;
import pl.surdel.ztp2.notification.domain.model.NotificationStatus;
import pl.surdel.ztp2.notification.domain.policy.QuietHoursPolicy;
import pl.surdel.ztp2.notification.domain.policy.StatusTransitionPolicy;

@ApplicationScoped
public class NotificationService {
    private final StatusTransitionPolicy statusPolicy;
    private final QuietHoursPolicy quietHoursPolicy;

    public NotificationService(StatusTransitionPolicy statusPolicy, QuietHoursPolicy quietHoursPolicy) {
        this.statusPolicy = statusPolicy;
        this.quietHoursPolicy = quietHoursPolicy;
    }

    public Notification create(Notification notification) {
        return notification;
    }

    public boolean canBeSentNow(Notification notification) {
        return !quietHoursPolicy.isWithinQuietHours(
                notification.getPlannedSendAt(),
                notification.getRecipientTimezone()
        );
    }

    public void markScheduled(Notification notification) {
        notification.changeStatus(
                NotificationStatus.SCHEDULED,
                statusPolicy
        );
    }

    public void markSending(Notification notification) {
        notification.changeStatus(
                NotificationStatus.SENDING,
                statusPolicy
        );
    }

    public void markSent(Notification notification) {
        notification.changeStatus(
                NotificationStatus.SENT,
                statusPolicy
        );
    }

    public void markFailed(Notification notification) {
        notification.changeStatus(
                NotificationStatus.FAILED,
                statusPolicy
        );
    }

    public void cancel(Notification notification) {
        notification.changeStatus(
                NotificationStatus.CANCELED,
                statusPolicy
        );
    }
}
