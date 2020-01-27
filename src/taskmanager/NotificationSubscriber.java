package taskmanager;

import taskmanager.requests.Request;

public interface NotificationSubscriber {
    void processNotification(Request notification);
}
