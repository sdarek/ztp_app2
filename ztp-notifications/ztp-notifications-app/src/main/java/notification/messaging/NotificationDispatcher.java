package main.notification.messaging;

import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import pl.surdel.ztp2.notification.worker.NotificationWorker;
import java.util.UUID;

@ApplicationScoped
public class NotificationDispatcher {
    private final NotificationWorker worker;

    public NotificationDispatcher(NotificationWorker worker) {
        this.worker = worker;
    }

    @Incoming("notification-in")
    @Blocking
    public void consume(String notificationId) {
        System.err.println("### DISPATCHER RECEIVED ### " + notificationId);
        worker.process(UUID.fromString(notificationId));
    }
}