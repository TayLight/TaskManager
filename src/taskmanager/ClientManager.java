package taskmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import taskmanager.exceptions.NameTaskException;
import taskmanager.exceptions.ItemNotFoundException;
import taskmanager.requests.*;
import taskmanager.task.Task;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
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
        messageToServer("AddTask");
        objectMapper.registerModule(new JavaTimeModule());
        Task newTask = newItem;
        NewTaskRequest request = new NewTaskRequest("AddTask", newTask);
        try {
            objectMapper.writeValue(outputStream, request);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void messageToServer(String message){
        CommandRequest commandRequest = new CommandRequest(message);
        try{
            objectMapper.writeValue(outputStream, commandRequest);
        } catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteItem(int index) throws IOException, ItemNotFoundException {
        messageToServer("DeleteTask");
        DeleteTaskRequest deleteTaskRequest = new DeleteTaskRequest(index, "DeleteTask");
        objectMapper.writeValue(outputStream, deleteTaskRequest);
        CommandRequest commandRequest = objectMapper.readValue(inputStream, CommandRequest.class);
        if (commandRequest.getMessage().equals("Error")) throw new ItemNotFoundException("Неверный индекс");
    }

    @Override
    public Task getItem(int index) throws ItemNotFoundException {
        return journalTask.get(index);
    }

//    @Override
//    public void editTask(int index, LocalTime newTime) throws ItemNotFoundException {
//
//    }
//
//    @Override
//    public void editTask(int index, String text) throws ItemNotFoundException {
//
//    }
//
//    @Override
//    public void editTaskDescription(int index, String description) throws ItemNotFoundException {
//
//    }

    @Override
    public void updateItem(int index, Task item) {

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
    public List<Task> getItems() throws IOException {
        messageToServer("LoadJournalTask");
        objectMapper.registerModule(new JavaTimeModule());
        LoadJournalRequest loadJournalRequest = objectMapper.readValue(inputStream, LoadJournalRequest.class);
        return loadJournalRequest.getData();
    }

    @Override
    public void checkUniqueName(String name) throws NameTaskException, IOException {
        messageToServer("CheckName");
        NameCheckRequest nameCheckRequest = new NameCheckRequest("CheckName", name);
        objectMapper.writeValue(outputStream, nameCheckRequest);
        CommandRequest commandRequest = objectMapper.readValue(inputStream, CommandRequest.class);
        if (commandRequest.getMessage().equals("Error")) throw new NameTaskException("Неверное имя");
    }
}
