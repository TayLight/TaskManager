package taskmanager;

import taskmanager.task.Task;

import java.io.Serializable;
import java.time.LocalTime;

/**Система оповещения пользователя
 * В указаное время система выводит пользователю сообщение, заранее заданное пользователем
 */
public class NotificationSystemThread extends Thread implements Serializable {
    /**
     * Выводимая пользователю задача
     */
    private Task taskNotify;
    /**
     * Флаг удаления задачи
     */
    private boolean taskDeleted=false;

    /**Конструктор, принмиаемый задачу на выполнение
     * @param task Задача, чье сообщение будет выводиться пользователю
     */
    public NotificationSystemThread(Task task) {
        taskNotify=task;
    }

    /**Метод для запуска метода оповещения
     * Поток, создаваемый методом, будет ждать пока время указанное пользователем задачи не совпадет
     * со временем системы. Если время не наступило, поток уснет на минуту. При совпадении времени времени
     * системы со временем задачи, пользователю будет выведено сообщение
     */
    @Override
    public void run() {
        LocalTime localTime = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute(), 0);
        while(taskNotify.getTime().getHour() != localTime.getHour()
                || taskNotify.getTime().getMinute() != localTime.getMinute()){
            try {
                sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            localTime = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute(), 0);
        }
        if(taskDeleted) return;
        else System.out.println("\n" + taskNotify.getName()+"\n"+ taskNotify.getDescription());
    }

    /** Метод изменяющий флаг удаления на true
     * @param taskDeleted True- если задача удалена
     */
    public void setTaskDeleted(boolean taskDeleted) {
        this.taskDeleted = taskDeleted;
    }
}
