package taskmanager.exceptions;

/**
 * Повтор имени задачи
 */
public class NameTaskException extends Exception {

    /**
     * Повтор имени задачи
     *
     * @param message Сообщение об ошибке
     */
    public NameTaskException(String message) {
        super(message);
    }
}
