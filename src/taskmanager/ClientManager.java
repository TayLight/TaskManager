package taskmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import taskmanager.exceptions.NameTaskException;
import taskmanager.exceptions.ItemNotFoundException;
import taskmanager.requests.*;
import taskmanager.task.Task;

import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Properties;

public class ClientManager implements Manager<Task> {
    private Socket socket;
    private DataOutput outputStream;
    private DataInput inputStream;
    private LinkedList<Task> journalTask;
    ObjectMapper objectMapper;
    public static final String PATH_TO_PROPERTIES ="./serverConnection.properties";

    public ClientManager() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void addItem(Task newItem) throws NameTaskException {
        objectMapper.registerModule(new JavaTimeModule());
        Task newTask =  newItem;
        NewTaskRequest request = new NewTaskRequest("AddTask", newTask);
        try {
            objectMapper.writeValue(outputStream, request);
        } catch (IOException ex) {

        }
    }

    @Override
    public void deleteItem(int index) throws IOException {
        DeleteTaskRequest deleteTaskRequest = new DeleteTaskRequest(index, "DeleteTask");
        objectMapper.writeValue(outputStream, deleteTaskRequest);
    }

    @Override
    public Task getItem(int index) throws ItemNotFoundException {
        return journalTask.get(index);
    }

    @Override
    public void editTask(int index, LocalTime newTime) throws ItemNotFoundException {

    }

    @Override
    public void editTask(int index, String text) throws ItemNotFoundException {

    }

    @Override
    public void editTaskDescription(int index, String description) throws ItemNotFoundException {

    }

    @Override
    public Task updateItem(int index) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void startWork() throws IOException {
        FileInputStream fileInputStream;
        Properties prop = new Properties();
        fileInputStream = new FileInputStream(PATH_TO_PROPERTIES);
        prop.load(fileInputStream);
        int port = Integer.parseInt(prop.getProperty("server1.port"));
        String host = prop.getProperty("login");
        socket = new Socket(host, port);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void finalWork() {
        Request loadJournalRequest = new Request("CloseSession");
        try {
            objectMapper.writeValue(outputStream, loadJournalRequest);
            socket.close();
        } catch (IOException ignored) {

        }
    }

    @Override
    public LinkedList<Task> getItems() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        Request loadJournalRequest = new Request("LoadTaskJournal");
            objectMapper.writeValue(outputStream, loadJournalRequest);
            Request inputLoadJournalRequest = objectMapper.readValue(inputStream, Request.class);
            System.out.println(inputLoadJournalRequest.getJournal().get(0).getName());
            return inputLoadJournalRequest.getJournal();
    }

    @Override
    public void checkUniqueName(String name) throws NameTaskException, IOException {
        NameCheckRequest nameCheckRequest = new NameCheckRequest("CheckName", name);
        objectMapper.writeValue(outputStream, nameCheckRequest);
        Request request = objectMapper.readValue(inputStream, Request.class);
        if (request.getRequest().equals("Error")) throw new NameTaskException("Неверное имя");
    }
}
