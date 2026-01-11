package pl.surdel.ztp2.notification.messaging;

import io.smallrye.common.annotation.Blocking;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import pl.surdel.ztp2.notification.domain.dto.StatusUpdate;
import pl.surdel.ztp2.notification.domain.model.Notification;
import pl.surdel.ztp2.notification.domain.service.NotificationService;
import pl.surdel.ztp2.notification.persistence.NotificationEntity;
import pl.surdel.ztp2.notification.persistence.NotificationMapper;
import pl.surdel.ztp2.notification.persistence.NotificationRepository;

@ApplicationScoped
public class NotificationStatusConsumer {

    private final NotificationRepository repository;
    private final NotificationService domainService;

    public NotificationStatusConsumer(NotificationRepository repository, NotificationService domainService) {
        this.repository = repository;
        this.domainService = domainService;
    }

    @Incoming("status-updates-in")
    @Blocking
    @Transactional
    public void processStatusUpdate(JsonObject json) {

        StatusUpdate update = json.mapTo(StatusUpdate.class);

        NotificationEntity entity = repository.findById(update.id);
        if (entity == null) return;

        Notification notification = NotificationMapper.toDomain(entity);

        boolean success = "SENT".equals(update.status);

        domainService.handleSendResult(notification, success);

        entity.status = notification.getStatus().name();
        entity.retryCount = notification.getRetryCount();
    }
}
