package pl.surdel.ztp2.notification.api;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.surdel.ztp2.notification.api.dto.CreateNotificationRequest;
import pl.surdel.ztp2.notification.api.dto.NotificationResponse;
import pl.surdel.ztp2.notification.api.dto.NotificationStatusResponse;
import pl.surdel.ztp2.notification.application.NotificationApplicationService;
import pl.surdel.ztp2.notification.domain.model.ChannelType;
import pl.surdel.ztp2.notification.domain.model.Notification;
import pl.surdel.ztp2.notification.domain.model.Priority;

import java.time.ZoneId;
import java.util.UUID;

import static pl.surdel.ztp2.notification.common.EnumParser.parse;

@Path("/notifications")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NotificationResource {
    private final NotificationApplicationService appService;

    public NotificationResource(NotificationApplicationService appService) {
        this.appService = appService;
    }

    @POST
    public Response create(@Valid CreateNotificationRequest req) {

        Notification notification = new Notification(
                UUID.randomUUID(),
                req.content,
                parse(ChannelType.class, req.channel, "channel"),
                req.recipient,
                ZoneId.of(req.recipientTimezone),
                parse(Priority.class, req.priority, "priority"),
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

    @GET
    @Path("/{id}")
    public NotificationStatusResponse getById(@PathParam("id") UUID id) {

        Notification notification = appService.findById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found"));

        NotificationStatusResponse resp = new NotificationStatusResponse();
        resp.id = notification.getId();
        resp.status = notification.getStatus().name();
        resp.plannedSendAt = notification.getPlannedSendAt();

        return resp;
    }

    @POST
    @Path("/{id}/cancel")
    public Response cancel(@PathParam("id") UUID id) {
        appService.cancel(id);
        return Response.noContent().build();
    }
}
