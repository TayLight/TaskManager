package taskmanager.exceptions;

/**Задача не найдена
 */
public class ItemNotFoundException extends Exception {

    /**Задача не найдена
     * @param message Сообщение об ошибке
     */
    public ItemNotFoundException(String message) {
        super(message);
    }
}
