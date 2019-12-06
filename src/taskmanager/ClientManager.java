package taskmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import taskmanager.exceptions.NameTaskException;
import taskmanager.exceptions.TaskNotFoundException;
import taskmanager.task.Task;

import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.util.LinkedList;

public class ClientManager implements Manager {
    private Socket socket;
    private DataOutput outputStream;
    private DataInput inputStream;
    private LinkedList<Task> journalTask;

    public ClientManager() {
        try {
            socket = new Socket("localhost", 1024);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            journalTask = loadTaskJournal();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LinkedList<Task> getJournalTask() {
        return journalTask;
    }

    @Override
    public void addTask(Task newTask) throws NameTaskException {

    }

    @Override
    public void deleteTask(int index) throws TaskNotFoundException {

    }

    @Override
    public Task getTask(int index) throws TaskNotFoundException {
        return null;
    }

    @Override
    public void editTask(int index, LocalTime newTime) throws TaskNotFoundException {

    }

    @Override
    public void editTask(int index, String text) throws TaskNotFoundException {

    }

    @Override
    public void editTaskDescription(int index, String description) throws TaskNotFoundException {

    }

    @Override
    public int size() {
        return 0;
    }


    @Override
    public void saveJournalTask() {

    }

    @Override
    public LinkedList<Task> loadTaskJournal() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Request request = new Request("LoadTaskJournal");
        try {
            objectMapper.writeValue(outputStream, request);
            Request inputRequest = objectMapper.readValue(inputStream, Request.class);
            return inputRequest.getJournal();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closeSession() {
        ObjectMapper objectMapper = new ObjectMapper();
        Request request = new Request("CloseSession");
        try {
            objectMapper.writeValue(outputStream, request);
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void checkUniqueName(String name) throws NameTaskException {

    }

    @Override
    public void checkIndexOnBound(int index) throws TaskNotFoundException {

    }

}
