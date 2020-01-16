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

public class ClientManager extends AbstractListModel<Task> implements Manager<Task> {
    private Socket socket;
    private DataOutput outputStream;
    private DataInput inputStream;
    private boolean isConnection = false;
    private int[] ports;
    private String[] hosts;
    private ObjectMapper objectMapper;
    public static final String PATH_TO_PROPERTIES = "./serverConnection.properties";

    public ClientManager() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void addItem(Task newItem) throws NameTaskException, IOException {
        Request addTaskRequest = new Request("AddTask", newItem);
        objectMapper.writeValue(outputStream, addTaskRequest);
        addTaskRequest = objectMapper.readValue(inputStream, Request.class);
        if (addTaskRequest.getCommand().equals("Error")) throw new NameTaskException("Неверное имя");
    }

    public void messageToServer(String message) { //нужно будет удалить
        CommandRequest commandRequest = new CommandRequest(message);
        try {
            objectMapper.writeValue(outputStream, commandRequest);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteItem(int index) throws IOException, ItemNotFoundException {
        Request deleteTaskRequest = new Request("DeleteTask", index);
        objectMapper.writeValue(outputStream, deleteTaskRequest);
        deleteTaskRequest = objectMapper.readValue(inputStream, Request.class);
        if (deleteTaskRequest.getCommand().equals("Error")) throw new ItemNotFoundException("Неверный индекс");
    }

    @Override
    public Task getItem(int index) {
        return null;
    }

    @Override
    public void updateItem(int index, Task item) throws IOException {
        messageToServer("AddTask");
        EditTaskRequest editTaskRequest = new EditTaskRequest("EditTask" + index, item);
        objectMapper.writeValue(outputStream, editTaskRequest);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void startWork() throws IOException {
        ConnectionFrame connectionFrame = new ConnectionFrame();
        FileInputStream fileInputStream;
        Properties prop = new Properties();
        fileInputStream = new FileInputStream(PATH_TO_PROPERTIES);
        prop.load(fileInputStream);
        int port;
        String host;
        hosts = new String[prop.size() / 2];
        ports = new int[prop.size() / 2];
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
                connectionFrame.tryConnectionTo(tryConnection + 1);
                host = hosts[tryConnection];
                port = ports[tryConnection];
                socket = new Socket(host, port);
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
                connectionFrame.setVisible(false);
                break;
            } catch (IOException e) {
                tryConnection++;
            }
        }
        connectionFrame.setVisible(false);
        if (tryConnection == hosts.length) throw new IOException();
    }

    @Override
    public void finalWork() {
//        RequestOld loadJournalRequest = new RequestOld("CloseSession");
//        try {
//            objectMapper.writeValue(outputStream, loadJournalRequest);
//            socket.close();
//        } catch (IOException ignored) {
//
//        }
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
//        messageToServer("CheckName");
//        NameCheckRequest nameCheckRequest = new NameCheckRequest("CheckName", name);
//        objectMapper.writeValue(outputStream, nameCheckRequest);
//        CommandRequest commandRequest = objectMapper.readValue(inputStream, CommandRequest.class);
//        if (commandRequest.getMessage().equals("Error")) throw new NameTaskException("Неверное имя");
    }

    @Override
    public int getSize() {
        try {
            Request getSizeRequest = new Request("SizeJournalTask", null);
            objectMapper.writeValue(outputStream, getSizeRequest);
            System.out.println("Запрашиваю размер");
            getSizeRequest = objectMapper.readValue(inputStream, Request.class);
            return (int) getSizeRequest.getData();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }

    @Override
    public Task getElementAt(int index) {
        try {
            Request getTaskRequest = new Request("GetTask", index);
            objectMapper.writeValue(outputStream, getTaskRequest);
            System.out.println("Запрашиваю элемент " + index);
            getTaskRequest = objectMapper.readValue(inputStream, Request.class);
            Task task = objectMapper.convertValue(getTaskRequest.getData(), Task.class);
            return task;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
