package pl.surdel.ztp2.notification.worker;

import io.micrometer.core.instrument.MeterRegistry;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import pl.surdel.ztp2.notification.domain.ShippingSimulator;
import pl.surdel.ztp2.notification.domain.dto.StatusUpdate;
import java.util.UUID;

@ApplicationScoped
public class EmailWorker {
    private final ShippingSimulator simulator;
    private final MeterRegistry registry;

    // Emiter do wysyłania statusu zwrotnego do RabbitMQ [6]
    @Channel("status-updates-out")
    Emitter<StatusUpdate> statusEmitter;

    public EmailWorker(ShippingSimulator simulator, MeterRegistry registry) {
        this.simulator = simulator;
        this.registry = registry;
    }

    @Incoming("email-in")
    @Blocking // Przetwarzanie blokujące ze względu na symulator [3]
    public void consume(String notificationId) {
        UUID id = UUID.fromString(notificationId);

        // Logika symulacji (30% szans na błąd zgodnie ze źródłem [7])
        boolean success = simulator.ship(id);

        // Wysłanie informacji zwrotnej [5, 8]
        String status = success ? "SENT" : "FAILED";
        statusEmitter.send(new StatusUpdate(id, status));

        if (success) {
            registry.counter("notifications_sent_total", "channel", "email").increment();
        }
    }
}