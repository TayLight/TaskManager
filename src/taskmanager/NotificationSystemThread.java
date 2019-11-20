package taskmanager;

import taskmanager.task.Task;

import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class NotificationSystemThread extends Thread {
    Task taskNotify;
    boolean alarm=false;

    public NotificationSystemThread(Task task) {
        taskNotify=task;
    }

    @Override
    public void run() {
        LocalTime localTime = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute(), 0);
        while(taskNotify.getTime().getHour() != localTime.getHour() || taskNotify.getTime().getMinute() != localTime.getMinute()){
            try {
                sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            localTime = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute(), 0);
        }
        System.out.println("Внимание!\n" + taskNotify.getName()+"\n"+ taskNotify.getDescription());
    }
}
