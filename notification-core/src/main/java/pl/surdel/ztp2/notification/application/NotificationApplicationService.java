package pl.surdel.ztp2.notification.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import pl.surdel.ztp2.notification.domain.model.Notification;
import pl.surdel.ztp2.notification.domain.service.NotificationService;
import pl.surdel.ztp2.notification.persistence.NotificationEntity;
import pl.surdel.ztp2.notification.persistence.NotificationMapper;
import pl.surdel.ztp2.notification.persistence.NotificationRepository;

@ApplicationScoped
public class NotificationApplicationService {
    private final NotificationRepository repository;
    private final NotificationService domainService;

    public NotificationApplicationService(
            NotificationRepository repository,
            NotificationService domainService
    ) {
        this.repository = repository;
        this.domainService = domainService;
    }

    @Transactional
    public Notification create(Notification notification) {
        domainService.create(notification);

        NotificationEntity entity = NotificationMapper.toEntity(notification);

        repository.persist(entity);

        return notification;
    }
}
