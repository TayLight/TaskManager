package taskmanager.task;

import taskmanager.ControllerChangedSubscriber;
import taskmanager.Manager;
import taskmanager.TaskChangeSubscriber;
import taskmanager.exceptions.NameTaskException;
import taskmanager.exceptions.TaskNotFoundException;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.LinkedList;

/** Журнал задач
 * Здесь хранится список задач для оповещения пользователя
 */
public class JournalTask implements Manager, Serializable, TaskChangeSubscriber {
    /**Список задач
     */
    private LinkedList<Task> tasks;
    /**Подписчик на обновления
     */
    private ControllerChangedSubscriber subscriber = null;

    public JournalTask() {
        tasks = new LinkedList<>();
    }

    /** Метод получения задачи по индексу
     * @param index индекс необходимой задачи
     * @return возвращает модель под индексом index
     */
    public Task getTask (int index){
        return tasks.get(index);
    }

    /**Метод, возвращающий размер журнала задача
     * @return Возвращает размером журнала задач
     */
    public int size (){
        return tasks.size();
    }

    /** Метод добавления задачи в журнал задач
     * @param newTask Новая задача
     */
    public void addTask(Task newTask){
        tasks.addLast(newTask);
        newTask.subscribe(this);
        taskAdded(newTask);
    }

    /**Метод редактирования у задачи времени
     * @param index   Индекс редактируемой задачи
     * @param newTime Новое устанавливаемое время
     * @throws TaskNotFoundException задача не найдена
     */
    public void editTask(int index, LocalTime newTime) throws TaskNotFoundException {
        testTaskForIndex(index);
        tasks.get(index).setTime(newTime);
        taskChanged(tasks.get(index));
    }
    /**Метод редактирования у задачи имени
     * @param index   Индекс редактируемой задачи
     * @param name Новое устанавливаемое имя задачи
     * @throws TaskNotFoundException задача не найдена
     */
    public void editTask(int index, String name) throws TaskNotFoundException {
        testTaskForIndex(index);
        tasks.get(index).setName(name);
        taskChanged(tasks.get(index));
    }
    /**Метод редактирования у задачи описания
     * @param index   Индекс редактируемой задачи
     * @param description Новое устанавливаемое описание
     * @throws TaskNotFoundException задача не найдена
     */
    public void editTaskDescription(int index, String description) throws TaskNotFoundException {
        testTaskForIndex(index);
        tasks.get(index).setDescription(description);
        taskChanged(tasks.get(index));
    }

    /** Метод удаления задачи
     * @param index Индекс удаляемой задачи
     * @throws TaskNotFoundException задача не найдена
     */
    public void deleteTask(int index) throws TaskNotFoundException {
        testTaskForIndex(index);
        tasks.get(index).unsubscribe();
        Task tempTask = tasks.get(index);
        tasks.remove(index);
        tempTask.getNotify().setTaskDeleted(true);
        taskDeleted(tempTask);
    }

    /** Метод проверки на уникальность имени
     * @param name Имя задачи для проверки
     * @throws NameTaskException Задача с таким именем уже есть
     */
    public void testTaskForName(String name) throws NameTaskException {
        for (Task task : tasks) {
            if (task.getName().equals(name)) throw new NameTaskException("Задача с таким именем уже существует.");
        }
    }

    /** Метод проверки значения индекса
     * @param index
     * @throws TaskNotFoundException
     */
    public void testTaskForIndex(int index) throws TaskNotFoundException{
        if (tasks.size() < index) throw new TaskNotFoundException("Неверное значение индекса.");
    }

    /** Метод оповещения подписчиков об изменении модели
     * @param task  измененная модель
     */
    @Override
    public void taskChanged(Task task) {
        subscriber.taskEdited(task);
    }
    /** Метод оповещения подписчиков об добавлении модели
     * @param task  добавленная модель
     */
    private void taskAdded(Task task)
    {
        subscriber.taskAdded(task);
    }
    /** Метод оповещения подписчиков об удалении модели
     * @param task  удаленная модель
     */
    private void taskDeleted(Task task)
    {
        subscriber.taskDeleted(task);
    }

    /** Метод подписки на обновления
     * @param subscriber Новый подписчик
     */
    public void subscribe(ControllerChangedSubscriber subscriber)  {
        this.subscriber = subscriber;
    }

    /**Метод отписки от обновлений
     */
    public void unsubscribe()
    {
        this.subscriber=null;
    }
}
