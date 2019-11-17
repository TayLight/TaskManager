package taskmanager;

import taskmanager.task.Task;

public interface TaskChangeSubscriber {

    void taskDeleted(Task task);
}
