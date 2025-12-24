package main.notification.messaging;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class NotificationProducer {
    @Channel("notification-out")
    Emitter<String> emitter;

    public void send(String notificationId) {
        emitter.send(notificationId);
    }
}
