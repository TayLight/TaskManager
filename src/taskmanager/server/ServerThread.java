package taskmanager.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import taskmanager.Manager;
import taskmanager.exceptions.ItemNotFoundException;
import taskmanager.exceptions.NameTaskException;
import taskmanager.requests.*;
import taskmanager.task.JournalTask;
import taskmanager.task.Task;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class ServerThread implements Runnable, Manager<Task> {
    /**
     * сокет для общения
     */
    private Socket clientSocket;
    /**
     * поток чтения из сокета
     */
    private DataInput inputStream;
    /**
     * поток записи в сокет
     */
    private DataOutput outputStream;

    /**
     * управление журналом задач
     */
    private Manager journalTask;

    /**
     * журнал задач
     */
    LinkedList<Task> listTask;

    enum Message {
        LOAD_JOURNAL_TASK("LoadJournalTask"),
        ADD_TASK("AddTask"),
        DELETE_TASK("DeleteTask"),
        CHECK_NAME("CheckName"),
        GET_SIZE("SizeJournalTask"),
        GET_TASK("GetTask");

        private String message;

        Message(String message) {
            this.message = message;
        }
    }

    /**
     * Конструктор, создающий поток
     *
     * @throws IOException ошибка потоков
     */
    public ServerThread(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        inputStream = new DataInputStream(clientSocket.getInputStream());
        outputStream = new DataOutputStream(clientSocket.getOutputStream());
    }

    @Override
    public void run() {
        System.out.println("Серверная нить запущена.");
        journalTask = new JournalTask<Task>();
        listTask = getItems();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        boolean exit = false;
        try {
            while (!exit) {
                Request inputRequest = null;
                try {
                    System.out.println("Пытаюсь получить команду...");
                    inputRequest = objectMapper.readValue(inputStream, Request.class);
                    System.out.println(inputRequest.getCommand());
                } catch (IOException ex) {
                    //ex.printStackTrace();
                    clientSocket.close();
                    journalTask.finalWork();
                    exit = true;
                    System.out.println("Серверная нить закрыта.");
                    return;
                }
                for (Message msg : Message.values()) {
                    if (inputRequest.getCommand().equals(msg.message)) {
                        switch (msg) {
                            case LOAD_JOURNAL_TASK:
                                System.out.println("Запрос принят: отправить журнал задач.");
                                LoadJournalRequest loadJournalRequest = new LoadJournalRequest(listTask, "LoadJournalTask");
                                objectMapper.registerModule(new JavaTimeModule());
                                try {
                                    objectMapper.writeValue(outputStream, loadJournalRequest);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            case ADD_TASK:
                                System.out.println("Запрос принят: добавить задачу.");
                                String message_at = "Ok";
                                Task newTask = objectMapper.convertValue(inputRequest.getData(), Task.class);
                                try {
                                    checkUniqueName(newTask.getName());
                                    journalTask.addItem(newTask);
                                } catch (NameTaskException ex) {
                                    message_at = "Error";
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                Request reply_at = new Request(message_at, null);
                                objectMapper.writeValue((DataOutput) outputStream, reply_at);
                                listTask = getItems();
                                break;
//                            case CHECK_NAME:
//                                System.out.println("Запрос принят: проверить уникальность имени.");
//                                String message_cn = "Ok";
//                                NameCheckRequest nameCheckRequest = objectMapper.readValue(inputStream, NameCheckRequest.class);
//                                try {
//                                    checkUniqueName(nameCheckRequest.getData());
//                                } catch (NameTaskException ex) {
//                                    message_cn = "Error";
//                                } catch (IOException ex) {
//                                    ex.printStackTrace();
//                                }
//                                CommandRequest reply_cn = new CommandRequest(message_cn);
//                                objectMapper.writeValue(outputStream, reply_cn);
//                                break;
                            case DELETE_TASK:
                                System.out.println("Запрос принят: удалить задачу.");
                                String message_dt = "Ok";
                                try {
                                    journalTask.deleteItem((int)inputRequest.getData());
                                } catch (ItemNotFoundException ex) {
                                    message_dt = "Error";
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                Request reply_dt = new Request(message_dt, null);
                                objectMapper.writeValue(outputStream, reply_dt);
                                listTask = getItems();
                                break;
                            case GET_SIZE:
                                System.out.println("Запрос принят: размер журнала.");
                                Request reply_gs = new Request("SizeJournalTask", journalTask.size());
                                try {
                                    objectMapper.writeValue((DataOutput) outputStream, reply_gs);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            case GET_TASK:
                                System.out.println("Запрос принят: получить задачу.");
                                Task task = null;
                                try{
                                    task = getItem((int)inputRequest.getData());
                                } catch (ItemNotFoundException ex) {
                                    ex.printStackTrace();
                                }
                                Request reply_gt =  new Request("GetTask", task);
                                objectMapper.writeValue((DataOutput) outputStream, reply_gt);
                                break;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Получение журнала задач
     *
     * @return возвращает журнал задач
     */
    public LinkedList<Task> getItems() {
        LinkedList<Task> listTask = new LinkedList<Task>();
        for (int i = 0; i < journalTask.size(); i++) {
            try {
                listTask.addLast((Task) journalTask.getItem(i));
            } catch (ItemNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        return listTask;
    }

//    /** Отправка журнала задач клиенту
//     * @param loadJournalRequest запрос, отправляемый клиенту
//     * @param objectMapper экземпляр objectMapper
//     */
//    public void sendJournalTask(Request loadJournalRequest, ObjectMapper objectMapper) {
//        objectMapper.registerModule(new JavaTimeModule()); //перенести на уровень выше?
//        try {
//            objectMapper.writeValue(outputStream, loadJournalRequest);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }

    public void addItem(Task newItem) {
        listTask.addLast(newItem);
    }

    public void deleteItem(int index) throws ItemNotFoundException, IOException {
        if (index < 0 || index > listTask.size() - 1) throw new ItemNotFoundException("Неверное значение индекса.");
        listTask.remove(index);
    }

    public Task getItem(int index) throws ItemNotFoundException {
        return listTask.get(index);
    }


    public void updateItem(int index, Task item) throws ItemNotFoundException {

    }

    public int size() {
        return listTask.size();
    }

    public void startWork() throws IOException {

    }

    public void finalWork() {

    }

    public void checkUniqueName(String name) throws NameTaskException, IOException {
        for (Task task : listTask) {
            if (task.getName().equals(name)) throw new NameTaskException("Задача с таким именем уже существует.");
        }
    }

//    public void editTask(int index, LocalTime newTime) throws ItemNotFoundException{}
//
//    public void editTask(int index, String text) throws ItemNotFoundException{}
//
//    public void editTaskDescription(int index, String description) throws ItemNotFoundException{}
}
