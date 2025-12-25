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

    private final Set<UUID> processedIds = Collections.synchronizedSet(new HashSet<>());

    public boolean ship(UUID notificationId) {
        if (processedIds.contains(notificationId)) {
            LOG.infof("Notification %s already delivered (Idempotency check).", notificationId);
            return true;
        }

        if (random.nextDouble() < 0.3) {
            LOG.errorf("Simulated failure for notification %s", notificationId);
            return false;
        }

        LOG.infof("Successfully shipped notification %s", notificationId);
        processedIds.add(notificationId);
        return true;
    }
}