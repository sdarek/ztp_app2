package pl.surdel.ztp2.notification.worker;

import io.micrometer.core.instrument.MeterRegistry;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import pl.surdel.ztp2.notification.domain.ShippingSimulator;
import java.util.UUID;

@ApplicationScoped
public class EmailWorker {
    private final ShippingSimulator simulator;
    private final MeterRegistry registry;

    public EmailWorker(ShippingSimulator simulator, MeterRegistry registry) {
        this.simulator = simulator;
        this.registry = registry;
    }

    @Incoming("email-in")
    @Blocking // Przetwarzanie po jednej wiadomości naraz [8]
    public void process(String notificationId) {
        UUID id = UUID.fromString(notificationId);
        System.out.println("[EMAIL-WORKER] Processing: " + id);

        boolean success = simulator.simulate(id); // Wykorzystanie wspólnego symulatora [3]

        if (success) {
            registry.counter("notifications_sent_total", "channel", "email").increment();
            System.out.println("[EMAIL-WORKER] Sent successfully: " + id);
        } else {
            System.err.println("[EMAIL-WORKER] Failed to send: " + id);
        }
    }
}