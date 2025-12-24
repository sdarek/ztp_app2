package pl.surdel.ztp2.notification.domain.policy;

import jakarta.enterprise.context.ApplicationScoped;
import pl.surdel.ztp2.notification.domain.model.NotificationStatus;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class StatusTransitionPolicy {
    private static final Map<NotificationStatus, Set<NotificationStatus>> ALLOWED_TRANSITIONS = Map.of(
            NotificationStatus.CREATED, EnumSet.of(NotificationStatus.SCHEDULED, NotificationStatus.CANCELED),
            NotificationStatus.SCHEDULED, EnumSet.of(NotificationStatus.SENDING, NotificationStatus.CANCELED),
            NotificationStatus.SENDING, EnumSet.of(NotificationStatus.SENT, NotificationStatus.FAILED),
            NotificationStatus.FAILED, EnumSet.noneOf(NotificationStatus.class),
            NotificationStatus.SENT, EnumSet.noneOf(NotificationStatus.class),
            NotificationStatus.CANCELED, EnumSet.noneOf(NotificationStatus.class)
    );

    public boolean canTransition(NotificationStatus from, NotificationStatus to) {
        return ALLOWED_TRANSITIONS.getOrDefault(from, Set.of()).contains(to);
    }
}