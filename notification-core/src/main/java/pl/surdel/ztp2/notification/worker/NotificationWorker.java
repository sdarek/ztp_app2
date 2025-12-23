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

        NotificationEntity entity = appService.getEntity(notificationId);

        // SENDING
        entity.status = NotificationStatus.SENDING.name();

        boolean success = random.nextInt(10) < 7;

        if (success) {
            entity.status = NotificationStatus.SENT.name();
        } else {
            entity.retryCount++;

            if (entity.retryCount >= 3) {
                entity.status = NotificationStatus.FAILED.name();
            } else {
                entity.status = NotificationStatus.SCHEDULED.name();
            }
        }
    }
}
