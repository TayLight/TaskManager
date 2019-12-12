package taskmanager;

import taskmanager.exceptions.ItemNotFoundException;
import taskmanager.task.Task;

import java.awt.*;
import java.time.LocalTime;

/**
 * Система оповещения пользователя
 * В указаное время система выводит пользователю сообщение, заранее заданное пользователем
 */
public class NotificationSystemThread implements Runnable {
    /**
     * Журнал задач
     */
    private Manager journalTask;

    /**
     * Конструктор, принимающий журнал задач на выполнение
     *
     * @param journalTask Журнал задач, чьи сообщение будет выводиться пользователю
     */
    public NotificationSystemThread(Manager journalTask) {
        this.journalTask = journalTask;
    }

    /**
     * Метод для запуска системы оповещения
     */
    @Override
    public void run() {
        while (true) {
            LocalTime timeNow = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute(), 0);
            for (int i = 0; i < journalTask.size(); i++) {
                Task task = null;
                try {
                    task = journalTask.getItem(i);
                } catch (ItemNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
                try {
                    if ((task.getTime().isBefore(timeNow)) && task.getRelevance()) {
                        message(task.getName(), task.getDescription() + "\n" + task.getTime().toString());
                        task.setRelevance(false);
                        break;
                    } else if ((task.getTime().equals(timeNow)) && task.getRelevance()) {
                        message(task.getName(), task.getDescription());
                        task.setRelevance(false);
                        break;
                    }
                } catch (NullPointerException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Метод, для оповещения пользователя в необходимое время
     *
     * @param name        Имя задачи
     * @param description Описание задачи
     */
    private void message(String name, String description) {
        if (SystemTray.isSupported()) {
            SystemTray systemTray = SystemTray.getSystemTray();
            java.awt.Image image = Toolkit.getDefaultToolkit().getImage("./src/taskmanager/images/tray.png");
            TrayIcon trayIcon = new TrayIcon(image);
            try {
                systemTray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
            trayIcon.displayMessage(name, description, TrayIcon.MessageType.INFO);
            systemTray.remove(trayIcon); //причина незавершающейся программы
        }
    }
}
