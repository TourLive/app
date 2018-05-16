package ch.hsr.sa.radiotour.dataaccess.models;

import com.google.gson.annotations.Expose;

public class NotificationDTO {
    @Expose
    private String message;
    @Expose
    private NotificationType typeState;
    @Expose
    private String id;

    public NotificationDTO(String msg, NotificationType type, String id) {
        this.message = msg;
        this.typeState = type;
        this.id = id;
    }
}
