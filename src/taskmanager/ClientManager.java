package taskmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import netscape.javascript.JSObject;
import taskmanager.Manager;
import taskmanager.TaskChangedSubscriber;
import taskmanager.exceptions.NameTaskException;
import taskmanager.exceptions.TaskNotFoundException;
import taskmanager.task.Task;
import com.fasterxml.jackson.databind.json.*;
import com.fasterxml.jackson.core.json.*;
import com.fasterxml.jackson.core.*;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
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
            loadJournalTask();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void deleteTaskByNotify(int index) {

    }

    @Override
    public void saveJournalTask() {
        ObjectMapper objectMapper = new ObjectMapper();
        Request request = new Request("SaveJournalTask",journalTask);
        try {
            objectMapper.writeValue(outputStream, request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LinkedList<Task> loadJournalTask() {
        ObjectMapper objectMapper = new ObjectMapper();
        Request request = new Request("LoadTaskJournal");
        try {
            objectMapper.writeValue(outputStream, request);
            Request inputRequest = objectMapper.readValue(inputStream, Request.class);
            journalTask = inputRequest.getJournal();
            return journalTask;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void checkUniqueName(String name) throws NameTaskException {

    }

    @Override
    public void checkIndexOnBound(int index) throws TaskNotFoundException {

    }

}
