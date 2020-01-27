package taskmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import taskmanager.exceptions.ItemNotFoundException;
import taskmanager.exceptions.NameTaskException;
import taskmanager.requests.Request;
import taskmanager.task.Task;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Properties;

/**
 * Класс для взаимодействия клиента с сервером
 */
public class ClientManager extends AbstractListModel<Task> implements Manager<Task>, NotificationSubscriber, ListChangedSubscription {
    private ListChangedSubscriber subscriber;
    private List<Task> tasks;
    /**
     * Клиентский сокет
     */
    private Socket socket;
    /**
     * Выходной поток клиента
     */
    private DataOutput outputStream;
    /**
     * Входной поток клиента
     */
    private DataInput inputStream;
    /**
     * Маппер для преобразования JSON
     */
    private ObjectMapper objectMapper;
    /**
     * Путь до конфигурационного файла
     */
    public static final String PATH_TO_PROPERTIES = "./serverConnection.properties";


    public ClientManager() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void addItem(Task newItem) throws NameTaskException, IOException {
        checkUniqueName(newItem.getName());
        Request addItemRequest = new Request("AddItem", newItem);
        objectMapper.writeValue(outputStream, addItemRequest);
    }

    @Override
    public void deleteItem(int index) throws IOException, ItemNotFoundException {
        Request deleteItemRequest = new Request("DeleteItem", index);
        objectMapper.writeValue(outputStream, deleteItemRequest);
        deleteItemRequest = objectMapper.readValue(inputStream, Request.class);
        if (deleteItemRequest.getCommand().equals("Error")) throw new ItemNotFoundException("Неверный индекс");
    }

    @Override
    public Task getItem(int index) {
        return null;
    }

    @Override
    public void updateItem(int index, Task item) throws IOException, NameTaskException {
        checkUniqueName(item.getName());
        Request updateItemRequest = new Request("UpdateItem" + index, item);
        objectMapper.writeValue(outputStream, updateItemRequest);
    }

    @Override
    public void startWork() throws IOException {
        FileInputStream fileInputStream;
        Properties prop = new Properties();
        fileInputStream = new FileInputStream(PATH_TO_PROPERTIES);
        prop.load(fileInputStream);
        int port;
        String host;
        String[] hosts = new String[prop.size() / 2];
        int[] ports = new int[prop.size() / 2];
        for (int i = 0; i < prop.size() / 2; i++) {
            String linkHost = "server" +
                    (i + 1) +
                    ".host";
            hosts[i] = prop.getProperty(linkHost);
            String linkPort = "server" +
                    (i + 1) +
                    ".port";
            ports[i] = Integer.parseInt(prop.getProperty(linkPort));
        }
        int tryConnection = 0;
        while (tryConnection != hosts.length) {
            try {
                host = hosts[tryConnection];
                port = ports[tryConnection];
                socket = new Socket(host, port);
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
                ClientThread clientThread = new ClientThread((InputStream) inputStream);
                clientThread.start();
                break;
            } catch (IOException e) {
                tryConnection++;
            }
        }
        if (tryConnection == hosts.length) throw new IOException();
    }

    @Override
    public void finalWork() throws IOException {
        socket.close();
    }

    @Override
    public List<Task> getItems() {
        return null;
    }


    @Override
    public void checkUniqueName(String name) throws NameTaskException {
        for (Task task : tasks) if (task.getName().equals(name)) throw new NameTaskException("Такое имя уже есть");
    }

    @Override
    public void subscribe(ListChangedSubscriber subscriber) {

    }

    @Override
    public void subscribeGUI(ListChangedSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void unsubscribe() {
        subscriber = null;
    }

    /**
     * @return возвращает размер журнала задач
     */
    @Override
    public int getSize() {
        if (tasks != null)
            return tasks.size();
        else return 0;
    }

    /**
     * @param index индекс необходимого параметра
     * @return возвращает задачу, с нужным индексом
     */
    @Override
    public Task getElementAt(int index) {
        return tasks.get(index);
    }


    @Override
    public void taskDeleted(int index) {
        tasks.remove(index);
        subscriber.listChanged();
    }

    @Override
    public void taskAdded(Task task) {
        tasks.add(task);
        subscriber.listChanged();
    }

    @Override
    public void taskUpdated(int index, Task task) {
        tasks.set(index, task);
        subscriber.listChanged();
    }

    @Override
    public void newJournalTask(List<Task> taskList) {
        tasks = taskList;
        subscriber.listChanged();
    }

    @Override
    public void notifyTask(int index) {
        tasks.get(index).setRelevance(false);
        subscriber.listChanged();
    }
}
