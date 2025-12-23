package pl.surdel.ztp2.notification.worker;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import pl.surdel.ztp2.notification.application.NotificationApplicationService;
import pl.surdel.ztp2.notification.domain.model.Notification;
import pl.surdel.ztp2.notification.domain.model.NotificationStatus;
import pl.surdel.ztp2.notification.persistence.NotificationEntity;

import java.util.Random;
import java.util.UUID;

@ApplicationScoped
public class NotificationWorker {
    private final NotificationApplicationService appService;
    private final Random random = new Random();

    public NotificationWorker(NotificationApplicationService appService) {
        this.appService = appService;
    }

    @Transactional
    public void process(UUID notificationId) {
        Notification notification = appService.getRequired(notificationId);

        try {
            appService.markSending(notification);

            boolean success = random.nextInt(10) < 7; // 70% szans na sukces

            if (success) {
                appService.markSent(notification);
            } else {
                appService.markFailed(notification);
            }
        } catch (Exception e) {
            appService.markFailed(notification);
        }
    }
}
