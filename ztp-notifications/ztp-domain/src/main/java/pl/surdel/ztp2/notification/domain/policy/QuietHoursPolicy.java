package pl.surdel.ztp2.notification.domain.policy;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@ApplicationScoped
public class QuietHoursPolicy {
    private static final LocalTime QUIET_HOURS_START = LocalTime.of(23, 59);
    private static final LocalTime QUIET_HOURS_END = LocalTime.of(0, 1);

    public boolean isWithinQuietHours(Instant plannedSendAt, ZoneId recipientZone) {
        ZonedDateTime recipientTime = plannedSendAt.atZone(recipientZone);
        LocalTime localTime = recipientTime.toLocalTime();
        return localTime.isAfter(QUIET_HOURS_START) || localTime.isBefore(QUIET_HOURS_END);
    }
}