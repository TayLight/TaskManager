package taskmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import taskmanager.exceptions.NameTaskException;
import taskmanager.exceptions.ItemNotFoundException;
import taskmanager.task.Task;

import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

public class ClientManager implements Manager<Task> {
    private Socket socket;
    private DataOutput outputStream;
    private DataInput inputStream;
    private LinkedList<Task> journalTask;
    ObjectMapper objectMapper;

    public ClientManager() {
        objectMapper = new ObjectMapper();
    }


    @Override
    public void addItem(Object newItem) throws NameTaskException {
        objectMapper.registerModule(new JavaTimeModule());
        Task newTask = (Task) newItem;
        NewTaskRequest request = new NewTaskRequest("AddTask", newTask);
        try {
            objectMapper.writeValue(outputStream, request);
        } catch (IOException ex) {

        }
    }

    @Override
    public void deleteItem(int index) throws ItemNotFoundException {
        DeleteTaskRequest deleteTaskRequest = new DeleteTaskRequest(index, "DeleteTask");
        try {
            objectMapper.writeValue(outputStream, deleteTaskRequest);
        } catch (IOException ex) {

        }
    }

    @Override
    public Task getItem(int index) throws ItemNotFoundException {
        return null;
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
    public int size() {
        return 0;
    }

    @Override
    public void startWork() throws IOException {
        socket = new Socket("localhost", 1024);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void finalWork() {
        LoadJournalRequest loadJournalRequest = new LoadJournalRequest("CloseSession");
        try {
            objectMapper.writeValue(outputStream, loadJournalRequest);
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<Task> getTasks() {
        objectMapper.registerModule(new JavaTimeModule());
        LoadJournalRequest loadJournalRequest = new LoadJournalRequest("LoadTaskJournal");
        try {
            objectMapper.writeValue(outputStream, loadJournalRequest);
            LoadJournalRequest inputLoadJournalRequest = objectMapper.readValue(inputStream, LoadJournalRequest.class);
            return inputLoadJournalRequest.getData();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void checkUniqueName(String name) throws NameTaskException {

    }
}
