package taskmanager;

import taskmanager.task.Task;

public interface TaskChangeSubscriber {

    void taskChanged(Task task);
}
