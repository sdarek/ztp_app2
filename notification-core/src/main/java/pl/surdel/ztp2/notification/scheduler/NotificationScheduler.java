package pl.surdel.ztp2.notification.scheduler;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import pl.surdel.ztp2.notification.application.NotificationApplicationService;
import pl.surdel.ztp2.notification.domain.model.Notification;
import pl.surdel.ztp2.notification.persistence.NotificationEntity;
import pl.surdel.ztp2.notification.persistence.NotificationMapper;
import pl.surdel.ztp2.notification.persistence.NotificationRepository;

import java.util.List;

@ApplicationScoped
public class NotificationScheduler {
    private final NotificationRepository repository;
    private final NotificationApplicationService appService;

    public NotificationScheduler(
            NotificationRepository repository,
            NotificationApplicationService appService
    ) {
        this.repository = repository;
        this.appService = appService;
    }

    @Scheduled(every = "30s")
    @Transactional
    void scheduleNotifications() {

        List<NotificationEntity> created =
                repository.findCreated();

        for (NotificationEntity entity : created) {

            Notification notification =
                    NotificationMapper.toDomain(entity);

            if (appService.canBeScheduled(notification)) {
                appService.markScheduled(notification, entity);
            }
        }
    }
}
