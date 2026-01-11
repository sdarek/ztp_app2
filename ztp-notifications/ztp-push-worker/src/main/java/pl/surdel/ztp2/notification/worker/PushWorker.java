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
public class PushWorker {
    private final ShippingSimulator simulator;
    private final MeterRegistry registry;

    @Channel("status-updates-out")
    Emitter<StatusUpdate> statusEmitter;

    public PushWorker(ShippingSimulator simulator, MeterRegistry registry) {
        this.simulator = simulator;
        this.registry = registry;
    }

    @Incoming("push-in")
    @Blocking
    public void process(String notificationId) {
        UUID id = UUID.fromString(notificationId);

        boolean success = simulator.ship(id);

        String status = success ? "SENT" : "FAILED";
        statusEmitter.send(new StatusUpdate(id, status));

        if (success) {
            registry.counter("notifications_sent_total", "channel", "push").increment();
        }
    }
}