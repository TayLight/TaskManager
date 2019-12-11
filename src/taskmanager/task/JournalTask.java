package taskmanager.task;

import taskmanager.Manager;
import taskmanager.TaskChangedSubscriber;
import taskmanager.exceptions.NameTaskException;
import taskmanager.exceptions.ItemNotFoundException;

import java.io.*;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Журнал задач
 * Здесь хранится список задач для оповещения пользователя
 */
public class JournalTask implements Manager, Serializable {
    /**
     * Адрес журнала задач на компьютере пользователя
     */
    private String pathToJournalTask = "./JournalTask.txt";
    /**
     * Список задач
     */
    private LinkedList<Task> tasks;
    /**
     * Подписчик на обновления
     */
    private TaskChangedSubscriber subscriber = null;

    /**
     * Конструктор, создающий журнал задач
     */
    public JournalTask() {
        File fileJournalTask = new File(pathToJournalTask);
        if (fileJournalTask.exists()) {
            if (fileJournalTask.length() != 0) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(pathToJournalTask);
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    tasks = (LinkedList<Task>) objectInputStream.readObject();

                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            } else tasks = new LinkedList<>();
        } else {
            tasks = new LinkedList<>();
            try {
                fileJournalTask.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Task getItem(int index) throws ItemNotFoundException {
        checkIndexOnBound(index);
        return tasks.get(index);
    }

    /**
     * Метод, возвращающий размер журнала задача
     *
     * @return Возвращает размером журнала задач
     */
    public int size() {
        return tasks.size();
    }

    @Override
    public void startWork() throws IOException {

    }

    @Override
    public void finalWork() {

    }

    public void addTask(Task newTask) {
        tasks.addLast(newTask);
//        subscriber.taskAdded(newTask);
    }

    public void editTask(int index, LocalTime newTime) throws ItemNotFoundException {
        checkIndexOnBound(index);
        tasks.get(index).setTime(newTime);
        subscriber.taskEdited(tasks.get(index));
    }

    public void editTask(int index, String name) throws ItemNotFoundException {
        checkIndexOnBound(index);
        tasks.get(index).setName(name);
        subscriber.taskEdited(tasks.get(index));
    }

    public void editTaskDescription(int index, String description) throws ItemNotFoundException {
        checkIndexOnBound(index);
        tasks.get(index).setDescription(description);
        subscriber.taskEdited(tasks.get(index));
    }

    @Override
    public void addItem(Object newItem) throws NameTaskException {

    }

    public void deleteItem(int index) throws ItemNotFoundException {
        checkIndexOnBound(index);
        Task tempTask = tasks.get(index);
        tasks.remove(index);
        subscriber.taskDeleted(tempTask);
    }

    /**
     * Метод, сериализации и сохранения журнала задач в файл
     */
    public void saveJournalTask() {
        if (tasks.size() != 0) {
            try (ObjectOutputStream out2 = new ObjectOutputStream(new FileOutputStream(pathToJournalTask))) {
                out2.writeObject(tasks);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileWriter fstream1 = new FileWriter(pathToJournalTask);
                BufferedWriter out1 = new BufferedWriter(fstream1);
                out1.write("");
                out1.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Task> getTasks() {
        return null;
    }


    /**
     * Метод проверки на уникальность имени
     *
     * @param name Имя задачи для проверки
     * @throws NameTaskException Задача с таким именем уже есть
     */
    public void checkUniqueName(String name) throws NameTaskException {
        for (Task task : tasks) {
            if (task.getName().equals(name)) throw new NameTaskException("Задача с таким именем уже существует.");
        }
    }

    /**
     * Метод проверки значения индекса
     *
     * @param index Индекс проверяемой задачи
     * @throws ItemNotFoundException Задачи с таким индексом не существует
     */
    public void checkIndexOnBound(int index) throws ItemNotFoundException {
        if (index < 0 || index > tasks.size() - 1) throw new ItemNotFoundException("Неверное значение индекса.");
    }


    /**
     * Метод подписки на обновления
     *
     * @param subscriber Новый подписчик
     */
    public void subscribe(TaskChangedSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    /**
     * Метод отписки от обновлений
     */
    public void unsubscribe() {
        this.subscriber = null;
    }
}
