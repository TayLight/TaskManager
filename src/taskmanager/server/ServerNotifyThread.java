package taskmanager.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import taskmanager.Manager;
import taskmanager.exceptions.ItemNotFoundException;
import taskmanager.requests.Request;
import taskmanager.task.Task;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;

public class ServerNotifyThread implements Runnable {
    /**
     * сокет для общения
     */
    private Socket clientSocket;
    /**
     * поток записи в сокет
     */
    private DataOutputStream outputStream;

    /**
     * управление журналом задач
     */
    private Manager journalTask;

    public ServerNotifyThread(Socket clientSocket, Manager journalTask) throws IOException {
        this.clientSocket = clientSocket;
        this.journalTask = journalTask;
    }

    @Override
    public void run() {
        System.out.println("Запущен поток оповещений.");
        try {
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            while (true) {
                LocalTime timeNow = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute(), 0);
                for (int i = 0; i < journalTask.size(); i++) {
                    Task task = null;
                    try {
                        task = (Task) journalTask.getItem(i);
                    } catch (ItemNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    }
                    if (((task.getTime().isBefore(timeNow)) || (task.getTime().equals(timeNow))) && task.getRelevance()) {
                        Request notifyRequest = new Request("Notify", task);
                        objectMapper.writeValue((DataOutput) outputStream, notifyRequest);
                        task.setRelevance(false);
                    }
                }
                try {
                    Thread.sleep(timeToNextNotify());
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException ex) {
            System.out.println("Работа системы оповещений остановлена.");
        }

    }

    public int timeToNextNotify() {
        int timeNextTask = 86400; //24 часа
        int timeNow = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute(), LocalTime.now().getSecond()).toSecondOfDay();
        for (int i = 0; i < journalTask.size(); i++) {
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
