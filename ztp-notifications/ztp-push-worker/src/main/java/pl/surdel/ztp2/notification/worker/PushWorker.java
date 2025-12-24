package pl.surdel.ztp2.notification.worker;

import io.micrometer.core.instrument.MeterRegistry;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import pl.surdel.ztp2.notification.domain.ShippingSimulator;
import java.util.UUID;

@ApplicationScoped
public class PushWorker {
    private final ShippingSimulator simulator;
    private final MeterRegistry registry;

    public PushWorker(ShippingSimulator simulator, MeterRegistry registry) {
        this.simulator = simulator;
        this.registry = registry;
    }

    @Incoming("push-in")
    @Blocking
    public void process(String notificationId) {
        UUID id = UUID.fromString(notificationId);
        System.out.println("[PUSH-WORKER] Processing: " + id);

        boolean success = simulator.ship(id);

        if (success) {
            registry.counter("notifications_sent_total", "channel", "push").increment();
            System.out.println("[PUSH-WORKER] Sent successfully: " + id);
        } else {
            System.err.println("[PUSH-WORKER] Failed to send: " + id);
        }
    }
}