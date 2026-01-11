package pl.surdel.ztp2.notification.messaging;

import io.smallrye.reactive.messaging.rabbitmq.OutgoingRabbitMQMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import pl.surdel.ztp2.notification.domain.model.ChannelType;

import java.util.UUID;

@ApplicationScoped
public class NotificationProducer {
    @Channel("notification-out")
    Emitter<String> emitter;

    public void send(UUID notificationId, ChannelType channel) {

        String routingKey = switch (channel) {
            case EMAIL -> "notification.email";
            case PUSH  -> "notification.push";
        };

        OutgoingRabbitMQMetadata metadata =
                OutgoingRabbitMQMetadata.builder()
                        .withRoutingKey(routingKey)
                        .build();

        emitter.send(
                Message.of(notificationId.toString())
                        .addMetadata(metadata)
        );
    }
}