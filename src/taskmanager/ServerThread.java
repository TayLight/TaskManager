package taskmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import taskmanager.exceptions.ItemNotFoundException;
import taskmanager.task.JournalTask;
import taskmanager.task.Task;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class ServerThread implements Runnable {
    /**сокет для общения
     *
     */
    private Socket clientSocket;
    /**поток чтения из сокета
     *
     */
    private DataInput inputStream;
    /**поток записи в сокет
     *
     */
    private DataOutput outputStream;

    /**управление журналом задач
     *
     */
    private Manager journalTask;

    /** Конструктор, создающий поток
     * @param clientSocket клиентский сокет
     * @throws IOException ошибка потоков
     */
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
            LoadJournalRequest inputLoadJournalRequest = null;
            try {
                inputLoadJournalRequest = objectMapper.readValue(inputStream, LoadJournalRequest.class);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            switch (inputLoadJournalRequest.getMessage()) {
                case "LoadTaskJournal":
                    if (!isLoaded) {
                        journalTask = new JournalTask();
                        LinkedList<Task> listTask = getList(journalTask);
                        LoadJournalRequest loadJournalRequest = new LoadJournalRequest("LoadJournalTask", listTask);
                        sendJournalTask(loadJournalRequest);
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

    /** Получение журнала задач
     * @param journalTask менеджер работы с журналом задач
     * @return возвращает журнал задач
     */
    public LinkedList<Task> getList(Manager journalTask) {
        LinkedList<Task> listTask = new LinkedList<Task>();
        for (int i = 0; i < journalTask.size(); i++) {
            try {
                listTask.addLast(journalTask.getItem(i));
            } catch (ItemNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        return listTask;
    }

    /** Отправка журнала задач клиенту
     * @param loadJournalRequest запрос, отправляемый клиенту
     */
    public void sendJournalTask(LoadJournalRequest loadJournalRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            objectMapper.writeValue(outputStream, loadJournalRequest);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
