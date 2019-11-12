package taskmanager.task;

import java.io.Serializable;
import java.time.LocalTime;

public class Task implements Serializable {
    private String name;
    private String description;
    private LocalTime time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalTime getTime ()
    {
        return time;
    }

    public Task(String name, LocalTime time) {
        this.name = name;
        this.time = time;
    }

    public Task(String name, String description, LocalTime time) {
        this.name = name;
        this.time = time;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        StringBuffer task = new StringBuffer();
        task.append("Задача: ");
        task.append(name);
        task.append("\n");
        task.append("Описание:");
        task.append(description);
        task.append("\n");
        return task.toString();
    }
}
