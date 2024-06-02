package il.cshaifasweng.OCSFMediatorExample.client;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
// Shared EventBus manager class
public class EventBusManager {
    private static final EventBus eventBus = new EventBus();

    public static EventBus getEventBus() {
        return eventBus;
    }
}

