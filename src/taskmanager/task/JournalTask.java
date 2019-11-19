package taskmanager.task;

import taskmanager.Manager;
import taskmanager.exceptions.NameTaskException;
import taskmanager.exceptions.TaskNotFoundException;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.LinkedList;

public class JournalTask implements Manager, Serializable {
    /**
     * Список задач
     */
    private LinkedList<Task> tasks;

    public JournalTask() {
        tasks = new LinkedList<>();
    }

    public Task getTask (int index){
        return tasks.get(index);
    }

    public int size (){
        return tasks.size();
    }

    public void addTask(Task newTask){
        tasks.addLast(newTask);
    }

    public void editTask(int index, LocalTime newTime) throws TaskNotFoundException {
        if (tasks.size()<index) throw new TaskNotFoundException("Неверное значение индекса.");
        tasks.get(index).setTime(newTime);
    }
    public void editTask(int index, String name) throws TaskNotFoundException {
        if (tasks.size()<index) throw new TaskNotFoundException("Неверное значение индекса.");
        tasks.get(index).setName(name);
    }
    public void editTaskDescription(int index, String description) throws TaskNotFoundException {
        if (tasks.size()<index) throw new TaskNotFoundException("Неверное значение индекса.");
        tasks.get(index).setDescription(description);
    }

    public void deleteTask(int index) throws TaskNotFoundException {
        if (tasks.size()<index) throw new TaskNotFoundException("Неверное значение индекса.");
        tasks.remove(index);
    }

    public void testTaskForName(String name) throws NameTaskException {
        for(int i = 0; i < tasks.size(); i++){
            if (tasks.get(i).getName().equals(name)) throw new NameTaskException("Задача с таким именем уже существует.");
        }
    }

    public void testTaskForIndex(int index) throws TaskNotFoundException{
        if (tasks.size() < index) throw new TaskNotFoundException("Неверное значение индекса.");
    }

}
