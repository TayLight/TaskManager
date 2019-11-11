package taskmanager.task;

import java.io.Serializable;
import java.sql.Time;

public class Task implements Serializable {
    private String name;
    private String description;
    private Time time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Time getTime ()
    {
        return time;
    }

    public Task(String name, Time time) {
        this.name = name;
        this.time=time;
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
