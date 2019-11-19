package taskmanager;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import taskmanager.task.Task;

import java.time.LocalTime;

public class NotificationSystem {

    public void task(Task task) throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        LocalTime ctime = task.getTime();
        SimpleTrigger simpleTrigger = new SimpleTrigger(" "," ");
    }
}
