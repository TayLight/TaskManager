package taskmanager;

import taskmanager.ListChangedSubscriber;

public interface ListChangedSubscription {

    void subscribeGUI(ListChangedSubscriber subscriber);

    void unsubscribe();
}
