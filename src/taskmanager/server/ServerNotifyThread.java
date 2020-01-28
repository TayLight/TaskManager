package taskmanager.server;

import taskmanager.Manager;
import taskmanager.exceptions.ItemNotFoundException;
import taskmanager.task.Task;

import java.io.IOException;
import java.time.LocalTime;

public class ServerNotifyThread implements Runnable {
    /**
     * управление журналом задач
     */
    private Manager journalTask;

    public ServerNotifyThread(Manager journalTask) throws IOException {
        this.journalTask = journalTask;
    }

    @Override
    public void run() {
        System.out.println("Запущен поток оповещений.");
        while (true) {
            LocalTime timeNow = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute(), 0);
            for (int i = 0; i < journalTask.getSize(); i++) {
                Task task = null;
                try {
                    task = (Task) journalTask.getItem(i);
                } catch (ItemNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
                if (((task.getTime().isBefore(timeNow)) || (task.getTime().equals(timeNow))) && task.getRelevance()) {
                    for (ServerThread st : ServerStarter.clientList) {
                        st.send("Notify" + i, task);
                        task.setRelevance(false);
                    }
                }
            }
            try {
                Thread.sleep(timeToNextNotify());
            } catch (InterruptedException ex) {
                //ex.printStackTrace();
            }
        }
    }

    public int timeToNextNotify() {
        int timeNextTask = 86400; //24 часа
        int timeNow = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute(), LocalTime.now().getSecond()).toSecondOfDay();
        for (int i = 0; i < journalTask.getSize(); i++) {
            Task task = null;
            try {
                task = (Task) journalTask.getItem(i);
            } catch (ItemNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
            if (task.getRelevance() && task.getTime().toSecondOfDay() < timeNextTask) {
                timeNextTask = task.getTime().toSecondOfDay();
            }
        }
        int time = timeNextTask - timeNow;
        return time * 1000;
    }

}
