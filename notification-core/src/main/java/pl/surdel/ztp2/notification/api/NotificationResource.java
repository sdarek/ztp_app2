package pl.surdel.ztp2.notification.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.surdel.ztp2.notification.api.dto.CreateNotificationRequest;
import pl.surdel.ztp2.notification.api.dto.NotificationResponse;
import pl.surdel.ztp2.notification.application.NotificationApplicationService;
import pl.surdel.ztp2.notification.domain.model.ChannelType;
import pl.surdel.ztp2.notification.domain.model.Notification;
import pl.surdel.ztp2.notification.domain.model.Priority;

import java.time.ZoneId;
import java.util.UUID;

@Path("/notifications")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NotificationResource {
    private final NotificationApplicationService appService;

    public NotificationResource(NotificationApplicationService appService) {
        this.appService = appService;
    }

    @POST
    public Response create(CreateNotificationRequest req) {

        Notification notification = new Notification(
                UUID.randomUUID(),
                req.content,
                ChannelType.valueOf(req.channel),
                req.recipient,
                ZoneId.of(req.recipientTimezone),
                Priority.valueOf(req.priority),
                req.plannedSendAt
        );

        Notification created = appService.create(notification);

        NotificationResponse resp = new NotificationResponse();
        resp.id = created.getId();
        resp.status = created.getStatus().name();
        resp.plannedSendAt = created.getPlannedSendAt();

        return Response.status(Response.Status.CREATED)
                .entity(resp)
                .build();
    }
}
