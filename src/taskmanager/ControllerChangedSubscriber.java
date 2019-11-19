package taskmanager;

import taskmanager.task.Task;

public interface ControllerChangedSubscriber {

    void taskDeleted(Task task);

    void taskAdded(Task task);

    void taskEdited(Task task);
}
