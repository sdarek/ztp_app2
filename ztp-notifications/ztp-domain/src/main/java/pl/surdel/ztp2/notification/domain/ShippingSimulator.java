package pl.surdel.ztp2.notification.domain;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class ShippingSimulator {
    private static final Logger LOG = Logger.getLogger(ShippingSimulator.class);
    private final Random random = new Random();

    // Pamięć identyfikatorów dla zapewnienia idempotentności (wymaganie 4.0 - Źródło 55)
    private final Set<UUID> processedIds = Collections.synchronizedSet(new HashSet<>());

    /**
     * Symuluje wysyłkę powiadomienia.
     * @return true jeśli wysyłka się powiodła, false w przypadku błędu.
     */
    public boolean ship(UUID notificationId) {
        // 1. Sprawdzenie idempotentności (Źródło 54-55)
        if (processedIds.contains(notificationId)) {
            LOG.infof("Notification %s already delivered (Idempotency check).", notificationId);
            return true;
        }

        // 2. Symulacja 30% szans na błąd (Źródło 50)
        if (random.nextDouble() < 0.3) {
            LOG.errorf("Simulated failure for notification %s", notificationId);
            return false;
        }

        // 3. Symulacja udanej wysyłki - zapis do logu (Źródło 50)
        LOG.infof("Successfully shipped notification %s", notificationId);
        processedIds.add(notificationId);
        return true;
    }
}