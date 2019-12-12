package taskmanager.conrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import taskmanager.exceptions.NameTaskException;
import taskmanager.exceptions.ItemNotFoundException;
import taskmanager.requests.DeleteTaskRequest;
import taskmanager.requests.LoadJournalRequest;
import taskmanager.requests.NewTaskRequest;
import taskmanager.requests.Request;
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
    ObjectMapper objectMapper= new ObjectMapper();

    public ClientManager() {

    }


    @Override
    public void addItem(Object newItem) throws NameTaskException {
        ObjectMapper objectMapper= new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Task newTask = (Task) newItem;
        NewTaskRequest request = new NewTaskRequest("AddTask", newTask);
        try {
            objectMapper.writeValue(outputStream, request);
        } catch (IOException ex) {

        }
    }

    @Override
    public void deleteItem(int index)  {
        ObjectMapper objectMapper= new ObjectMapper();
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
        ObjectMapper objectMapper= new ObjectMapper();
        Request loadJournalRequest = new Request("CloseSession");
        try {
            objectMapper.writeValue(outputStream, loadJournalRequest);
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public LinkedList<Task> getTasks() {
        ObjectMapper objectMapper= new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Request loadJournalRequest = new Request("LoadTaskJournal");
        try {
            System.out.println("Отправил пакет");
            objectMapper.writeValue(outputStream, loadJournalRequest);
            Request inputLoadJournalRequest = objectMapper.readValue(inputStream, Request.class);
            System.out.println("принял пакет");
            System.out.println(inputLoadJournalRequest.getJournal().get(0).getName());
            return inputLoadJournalRequest.getJournal();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void checkUniqueName(String name) throws NameTaskException {

    }
}
