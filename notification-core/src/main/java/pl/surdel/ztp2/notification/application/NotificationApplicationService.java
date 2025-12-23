package pl.surdel.ztp2.notification.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import pl.surdel.ztp2.notification.domain.model.Notification;
import pl.surdel.ztp2.notification.domain.service.NotificationService;
import pl.surdel.ztp2.notification.persistence.NotificationEntity;
import pl.surdel.ztp2.notification.persistence.NotificationMapper;
import pl.surdel.ztp2.notification.persistence.NotificationRepository;

import java.util.Optional;
import java.util.UUID;

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

    public Optional<Notification> findById(UUID id) {
        return repository.findOptionalById(id)
                .map(NotificationMapper::toDomain);
    }

    @Transactional
    public void cancel(UUID id) {

        NotificationEntity entity = repository.findOptionalById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found"));

        // domena pracuje na kopii
        Notification notification = NotificationMapper.toDomain(entity);
        domainService.cancel(notification);

        // PRZEPISUJEMY STAN DO ZARZĄDZANEJ ENCJI
        entity.status = notification.getStatus().name();
        entity.retryCount = notification.getRetryCount();
    }
}
