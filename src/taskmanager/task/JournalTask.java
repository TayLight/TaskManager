package taskmanager.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import taskmanager.JournalForSave;
import taskmanager.Manager;
import taskmanager.TaskChangedSubscriber;
import taskmanager.exceptions.ItemNotFoundException;
import taskmanager.exceptions.NameTaskException;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Журнал задач
 * Здесь хранится список задач для оповещения пользователя
 */
public class JournalTask<T> implements Manager<Task>, Serializable {
    /**
     * Адрес журнала задач на компьютере пользователя
     */
    private String pathToJournalTask = "JournalTask.json";
    ObjectMapper objectMapper;
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
        try {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            startWork();
        } catch (IOException e) {
            e.printStackTrace();
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
        if(tasks==null) return 0;
        return tasks.size();
    }

    @Override
    public void startWork() throws IOException {
        try {
            File fileJournalTask = new File(pathToJournalTask);
            JournalForSave journal = objectMapper.readValue(fileJournalTask, JournalForSave.class);
            tasks = (LinkedList<Task>) journal.getJournalTask();
        } catch (FileNotFoundException e) {
            File newFile = new File(pathToJournalTask);
            newFile.createNewFile();
        } catch (MismatchedInputException e){
            tasks = new LinkedList<>();
        }
    }

    @Override
    public void finalWork() throws IOException {
        try {
            File file = new File(pathToJournalTask);
            JournalForSave journal = new JournalForSave(tasks);
            objectMapper.writeValue(new File(pathToJournalTask),journal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateItem(int index, Task item) throws ItemNotFoundException {
        checkIndexOnBound(index);
        tasks.set(index, item);
    }


    @Override
    public void addItem(Task newItem) throws NameTaskException {
        tasks.addLast(newItem);
    }

    public void deleteItem(int index) throws ItemNotFoundException {
        Task tempTask = tasks.get(index);
        tasks.remove(index);
    }

    @Override
    public List<Task> getItems() {
        return (List<Task>) tasks.clone();
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
}
