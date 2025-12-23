package pl.surdel.ztp2.notification.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class NotificationRepository implements PanacheRepository<NotificationEntity> {
    public NotificationEntity findById(UUID id) {
        return find("id", id).firstResult();
    }
}
