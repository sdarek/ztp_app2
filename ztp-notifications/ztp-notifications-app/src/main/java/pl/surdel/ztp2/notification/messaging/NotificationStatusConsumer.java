package pl.surdel.ztp2.notification.messaging;

import io.smallrye.common.annotation.Blocking;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import pl.surdel.ztp2.notification.domain.dto.StatusUpdate;
import pl.surdel.ztp2.notification.persistence.NotificationEntity;
import pl.surdel.ztp2.notification.persistence.NotificationRepository;

@ApplicationScoped
public class NotificationStatusConsumer {

    private final NotificationRepository repository;

    public NotificationStatusConsumer(NotificationRepository repository) {
        this.repository = repository;
    }

    @Incoming("status-updates-in")
    @Blocking
    @Transactional
    public void processStatusUpdate(JsonObject json) {

        StatusUpdate update = json.mapTo(StatusUpdate.class);

        NotificationEntity entity = repository.findById(update.id);
        if (entity == null) {
            return;
        }

        entity.status = update.status;

        if ("FAILED".equals(update.status)) {
            entity.retryCount++;
        }
    }
}
