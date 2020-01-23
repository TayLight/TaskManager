package taskmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import taskmanager.exceptions.ItemNotFoundException;
import taskmanager.exceptions.NameTaskException;
import taskmanager.requests.*;
import taskmanager.task.Task;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Properties;

/**
 * Класс для взаимодействия клиента с сервером
 */
public class ClientManager extends AbstractListModel<Task> implements Manager<Task> {

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
        Request addItemRequest = new Request("AddItem", newItem);
        objectMapper.writeValue(outputStream, addItemRequest);
        addItemRequest = objectMapper.readValue(inputStream, Request.class);
        if (addItemRequest.getCommand().equals("Error")) throw new NameTaskException("Неверное имя");
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
    public void updateItem(int index, Task item) throws IOException, ItemNotFoundException {
        Request updateItemRequest = new Request("UpdateItem" + index, item);
        objectMapper.writeValue(outputStream, updateItemRequest);
        updateItemRequest = objectMapper.readValue(inputStream, Request.class);
        if (updateItemRequest.getCommand().equals("Error")) throw new ItemNotFoundException("Неверный индекс");

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
    public void checkUniqueName(String name) {

    }

    /**
     * @return возвращает размер журнала задач
     */
    @Override
    public int getSize() {
        try {
            Request getSizeRequest = new Request("SizeJournalTask", null);
            objectMapper.writeValue(outputStream, getSizeRequest);
            getSizeRequest = objectMapper.readValue(inputStream, Request.class);
            return (int) getSizeRequest.getData();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }

    /**
     * @param index индекс необходимого параметра
     * @return возвращает задачу, с нужным индексом
     */
    @Override
    public Task getElementAt(int index) {
        try {
            Request getItemRequest = new Request("GetItem", index);
            objectMapper.writeValue(outputStream, getItemRequest);
            getItemRequest = objectMapper.readValue(inputStream, Request.class);
            return objectMapper.convertValue(getItemRequest.getData(), Task.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
