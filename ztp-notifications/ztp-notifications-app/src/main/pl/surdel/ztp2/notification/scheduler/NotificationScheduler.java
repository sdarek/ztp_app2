package main.pl.surdel.ztp2.notification.scheduler;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import pl.surdel.ztp2.notification.application.NotificationApplicationService;
import pl.surdel.ztp2.notification.domain.model.Notification;
import pl.surdel.ztp2.notification.domain.model.NotificationStatus;
import pl.surdel.ztp2.notification.messaging.NotificationProducer;
import pl.surdel.ztp2.notification.persistence.NotificationEntity;
import pl.surdel.ztp2.notification.persistence.NotificationMapper;
import pl.surdel.ztp2.notification.persistence.NotificationRepository;

import java.util.List;

@ApplicationScoped
public class NotificationScheduler {
    private final NotificationRepository repository;
    private final NotificationApplicationService appService;
    private final NotificationProducer producer;

    public NotificationScheduler(
            NotificationRepository repository,
            NotificationApplicationService appService,
            NotificationProducer producer
    ) {
        this.repository = repository;
        this.appService = appService;
        this.producer = producer;
    }

    @Scheduled(every = "20s")
    @Transactional
    void scheduleNotifications() {

        List<NotificationEntity> toDispatch =
                repository.findReadyForDispatch();

        for (NotificationEntity entity : toDispatch) {

            Notification notification = NotificationMapper.toDomain(entity);

            if (appService.canBeScheduled(notification)) {
                System.err.println("### SCHEDULER SENDING TO RABBIT: " + notification.getId());
                if (notification.getStatus() == NotificationStatus.CREATED) {
                    appService.markScheduled(notification, entity);
                }
                producer.send(notification.getId().toString());
            }
        }
    }
}
