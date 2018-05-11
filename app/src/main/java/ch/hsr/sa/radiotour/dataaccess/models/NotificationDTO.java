package ch.hsr.sa.radiotour.dataaccess.models;

import com.google.gson.annotations.Expose;

public class NotificationDTO {
    @Expose
    private String message;
    @Expose
    private NotificationType typeState;

    public NotificationDTO(String msg, NotificationType type) {
        this.message = msg;
        this.typeState = type;
    }
}
