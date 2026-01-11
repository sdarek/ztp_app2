package pl.surdel.ztp2.notification.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import pl.surdel.ztp2.notification.domain.model.Notification;
import pl.surdel.ztp2.notification.domain.service.NotificationService;
import pl.surdel.ztp2.notification.persistence.*;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class NotificationApplicationService {
    private final NotificationRepository repository;
    private final NotificationService domainService;

    public NotificationApplicationService(NotificationRepository repository, NotificationService domainService) {
        this.repository = repository;
        this.domainService = domainService;
    }

    @Transactional
    public Notification create(Notification notification) {
        NotificationEntity entity = NotificationMapper.toEntity(notification);
        repository.persist(entity);
        return notification;
    }

    public Optional<Notification> findById(UUID id) {
        return repository.findOptionalById(id).map(NotificationMapper::toDomain);
    }

    @Transactional
    public void cancel(UUID id) {
        NotificationEntity entity = repository.findOptionalById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found"));
        Notification notification = NotificationMapper.toDomain(entity);
        domainService.cancel(notification);
        entity.status = notification.getStatus().name();
    }

    public boolean canBeScheduled(Notification notification) {
        return domainService.canBeSentNow(notification);
    }

    @Transactional
    public void markScheduled(Notification notification, NotificationEntity entity) {
        domainService.markScheduled(notification);
        entity.status = notification.getStatus().name();
    }

    @Transactional
    public void markSending(Notification notification, NotificationEntity entity) {
        domainService.markSending(notification);
        entity.status = notification.getStatus().name();
    }

    @Transactional
    public void forceSend(UUID id) {
        NotificationEntity entity = repository.findOptionalById(id)
                .orElseThrow(() -> new jakarta.ws.rs.NotFoundException("Notification not found"));

        Notification notification = NotificationMapper.toDomain(entity);

        domainService.forceSend(notification);

        entity.status = notification.getStatus().name();
        entity.plannedSendAt = java.time.Instant.now();
    }
}