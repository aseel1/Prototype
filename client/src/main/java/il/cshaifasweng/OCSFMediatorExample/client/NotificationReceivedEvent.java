package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.Notification;
public class NotificationReceivedEvent {

    private Notification newNotification;

    public NotificationReceivedEvent(Notification newNotification){this.newNotification= newNotification;}
    public Notification getNewNotification() {
        return newNotification;
    }

    public void setNewNotification(Notification newNotification) {
        this.newNotification = newNotification;
    }
}
