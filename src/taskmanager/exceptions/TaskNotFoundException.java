package taskmanager.exceptions;

/**Задача не найдена
 */
public class TaskNotFoundException extends Exception {

    /**Задача не найдена
     * @param message Сообщение об ошибке
     */
    public TaskNotFoundException(String message) {
        super(message);
    }
}
