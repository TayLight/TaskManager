package taskmanager;

import taskmanager.exceptions.TaskNotFoundException;
import taskmanager.task.JournalTask;
import taskmanager.task.Task;

import java.awt.*;
import java.io.Serializable;
import java.time.LocalTime;

/**Система оповещения пользователя
 * В указаное время система выводит пользователю сообщение, заранее заданное пользователем
 */
public class NotificationSystemThread extends Thread implements Serializable, Runnable {
    /**
     * Журнал задач
     */
    private Manager journalTask;

//    /**
//     * Флаг удаления задачи
//     */
//    private boolean taskDeleted=false;

    /**Конструктор, принимающий задачу на выполнение
     * @param journalTask Задача, чье сообщение будет выводиться пользователю
     */
    public NotificationSystemThread(Manager journalTask) {
        this.journalTask = journalTask;
    }

    /**Метод для запуска метода оповещения
     * Поток, создаваемый методом, сравнивает время задачи с текущим временем системы и
     * выводит сообщение в трей либо если время задачи уже прошло, но она еще не выполнена,
     * либо если время задачи совпадает с текущим временем системы.
     */
    @Override
    public void run() {
        boolean exit = false;
        while (!exit){
            LocalTime timeNow = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute(), 0);
            for (int i = 0; i < journalTask.size(); i++){
                Task task = null;
                try{
                    task = journalTask.getTask(i);
                }
                catch(TaskNotFoundException ex){
                    System.out.println(ex.getMessage());
                }
                try{
                    if ((task.getTime().isBefore(timeNow)) && task.getRelevance()){
                        Message(task.getName(), task.getDescription() + "\n" + task.getTime().toString());
                        task.setRelevance(false);
                        break;
                    }
                    else if ((task.getTime().equals(timeNow)) && task.getRelevance()){
                        Message(task.getName(), task.getDescription());
                        task.setRelevance(false);
                        break;
                    }
                }
                catch(NullPointerException ex){
                    System.out.println(ex.getMessage());
                }

            }
           try{
               Thread.sleep(10000);
           }
           catch (InterruptedException ex){
               Thread.currentThread().interrupt();
           }
        }
    }

    private void Message(String name, String description){
        if (SystemTray.isSupported()){
            SystemTray systemTray = SystemTray.getSystemTray();
            java.awt.Image image = Toolkit.getDefaultToolkit().getImage("./src/taskmanager/images/tray.png");
            TrayIcon trayIcon = new TrayIcon(image);
            try {
                systemTray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
            trayIcon.displayMessage(name, description, TrayIcon.MessageType.INFO);
            System.out.println("msg: " + name + "\n" + description);
        }
    }

//    /** Метод изменяющий флаг удаления на true
//     * @param taskDeleted True- если задача удалена
//     */
//    public void setTaskDeleted(boolean taskDeleted) {
//        this.taskDeleted = taskDeleted;
//    }
}
