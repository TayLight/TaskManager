package taskmanager.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import taskmanager.Manager;
import taskmanager.exceptions.ItemNotFoundException;
import taskmanager.requests.Request;
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
            Request inputLoadJournalRequest = null;
            try {
                System.out.println("Пытаюсь получить команду");
                inputLoadJournalRequest = objectMapper.readValue(inputStream, Request.class);
                System.out.println(inputLoadJournalRequest.getRequest());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            switch (inputLoadJournalRequest.getRequest()) {
                case "LoadTaskJournal":
                    if (!isLoaded) {
                        System.out.println("Принял запрос на отправку");
                        journalTask = new JournalTask();
                        LinkedList<Task> listTask = getList(journalTask);
                        Request loadJournalRequest = new Request("LoadJournalTask", listTask);
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
    public void sendJournalTask(Request loadJournalRequest) {
        System.out.println(loadJournalRequest.getJournal().get(0));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            objectMapper.writeValue(outputStream, loadJournalRequest);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
