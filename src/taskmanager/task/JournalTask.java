import java.util.LinkedList;

public class JournalTask implements Serializable, Manager {
    private LinkedList<Task> tasks;

    public JournalTask() {
        tasks = new LinkedList<>();
    }

    public void addTask(Task newTask) {
        tasks.addLast(newTask);
    }

    public void editTask(int index, Time newTime) throws TaskNotFoundException {
        if (tasks.size()<index)throw new TaskNotFoundException("Неверное значение индекса");
        tasks.get(index).setTime(newTime);
    }
    public void editTask(int index, String text) throws TaskNotFoundException {
        if (tasks.size()<index)throw new TaskNotFoundException("Неверное значение индекса");
        tasks.get(index).setName(text);
    }
    public void editTask(int index, String text, Time newTime) throws TaskNotFoundException {
        if (tasks.size()<index)throw new TaskNotFoundException("Неверное значение индекса");
        tasks.get(index).setTime(newTime);
        tasks.get(index).setName(text);
    }

    public void deleteTask(int index) throws TaskNotFoundException {
        if (tasks.size()<index)throw new TaskNotFoundException("Неверное значение индекса");
        tasks.remove(index);
    }
}
