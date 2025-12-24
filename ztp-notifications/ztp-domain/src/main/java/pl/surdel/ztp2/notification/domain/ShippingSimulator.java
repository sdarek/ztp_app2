package pl.surdel.ztp2.notification.domain;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class ShippingSimulator {
    private final Random random = new Random();
    // Pamięć identyfikatorów dla zapewnienia idempotentności (wymaganie 4.0)
    private final Set<UUID> processedIds = Collections.synchronizedSet(new HashSet<>());

    public boolean simulate(UUID notificationId) {
        if (processedIds.contains(notificationId)) {
            return true; // Już wysłano wcześniej
        }

        boolean success = random.nextInt(10) < 7; // 70% szans na sukces
        if (success) {
            processedIds.add(notificationId);
        }
        return success;
    }
}