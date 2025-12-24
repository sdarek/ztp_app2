package pl.surdel.ztp2.notification.domain.dto;

import java.util.UUID;

public class StatusUpdate {
    public UUID id;
    public String status;

    public StatusUpdate() {}
    public StatusUpdate(UUID id, String status) {
        this.id = id;
        this.status = status;
    }
}