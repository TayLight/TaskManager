package taskmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import taskmanager.exceptions.TaskNotFoundException;
import taskmanager.task.JournalTask;
import taskmanager.task.Task;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class ServerThread implements Runnable {
    private Socket clientSocket; //сокет для общения
    private DataInput inputStream; // поток чтения из сокета
    private DataOutput outputStream; // поток записи в сокет
    private Manager journalTask;

    public ServerThread(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        inputStream = new DataInputStream(clientSocket.getInputStream());
        outputStream = new DataOutputStream(clientSocket.getOutputStream());
    }

    @Override
    public void run() {
        System.out.println("Server thread is started.");
        boolean isLoaded = false, isClosed = false;
        while (!isClosed) {
            ObjectMapper objectMapper = new ObjectMapper();
            Request inputRequest = null;
            try {
                inputRequest = objectMapper.readValue(inputStream, Request.class);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            switch (inputRequest.getRequest()) {
                case "LoadTaskJournal":
                    if (!isLoaded) {
                        journalTask = new JournalTask();
                        LinkedList<Task> listTask = getList(journalTask);
                        Request request = new Request("LoadJournalTask", listTask);
                        sendJournalTask(request);
                        isLoaded = true;
                    }
                    break;
                case "CloseSession":
                    try {
                        clientSocket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    System.out.println("Server thread is closed.");
                    isClosed = true;
                    break;
            }
        }

    }

    public LinkedList<Task> getList(Manager journalTask) {
        LinkedList<Task> listTask = new LinkedList<Task>();
        for (int i = 0; i < journalTask.size(); i++) {
            try {
                listTask.addLast(journalTask.getTask(i));
            } catch (TaskNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        return listTask;
    }

    public void sendJournalTask(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            objectMapper.writeValue(outputStream, request);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
