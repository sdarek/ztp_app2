package main.notification.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class NotificationRepository implements PanacheRepository<NotificationEntity> {
    public NotificationEntity findById(UUID id) {
        return find("id", id).firstResult();
    }

    public Optional<NotificationEntity> findOptionalById(UUID id) {
        return find("id", id).firstResultOptional();
    }

    public List<NotificationEntity> findCreated() {
        return list("status", "CREATED");
    }

    public List<NotificationEntity> findReadyForDispatch() {
        return list("status in ?1", List.of("CREATED", "SCHEDULED"));
    }
}
