package taskmanager.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import taskmanager.ListChangedSubscriber;
import taskmanager.Manager;
import taskmanager.NotificationSubscriber;
import taskmanager.exceptions.ItemNotFoundException;
import taskmanager.exceptions.NameTaskException;
import taskmanager.requests.*;
import taskmanager.task.Task;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ServerThread extends Thread implements ListChangedSubscriber, NotificationSubscriber {
    /**
     * сокет для общения
     */
    private Socket clientSocket;
    /**
     * поток чтения из сокета
     */
    private DataInputStream inputStream;
    /**
     * поток записи в сокет
     */
    private DataOutputStream outputStream;
    private boolean isProcessed=false;

    private ObjectMapper objectMapper;

    /**
     * управление журналом задач
     */
    private Manager journalTask;

    /**
     * журнал задач
     */
    private LinkedList<Task> listItem;

    private Thread serverNotifyThread;

    @Override
    public void taskDeleted(int index) {
        for (ServerThread st : ServerStarter.clientList) {
            st.send("DeleteItem", index);
        }
    }

    @Override
    public void taskAdded(Task task) {
        for (ServerThread st : ServerStarter.clientList) {
            st.send("AddItem", task);
        }
    }

    @Override
    public void taskUpdated(int index, Task task) {
        for (ServerThread st : ServerStarter.clientList) {
            st.send("UpdateItem" + index, task);
        }
    }

    @Override
    public void newJournalTask(List<Task> taskList) {

    }

    @Override
    public void notifyTask(int index) {

    }

    public void send(String command, Object data) {
        Request request = new Request(command, data);
        try {
            objectMapper.writeValue((DataOutput) outputStream, request);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    enum Message {
        JOURNAL_TASK("JournalTask"),
        ADD_ITEM("AddItem"),
        DELETE_ITEM("DeleteItem"),
        UPDATE_ITEM("UpdateItem"),
        SIZE_JOURNAL("SizeJournalTask"),
        GET_ITEM("GetItem"),
        NOTIFY("Notify");

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
    public ServerThread(Socket clientSocket, Manager journalTask) throws IOException {
        this.clientSocket = clientSocket;
        this.journalTask = journalTask;
        inputStream = new DataInputStream(clientSocket.getInputStream());
        outputStream = new DataOutputStream(clientSocket.getOutputStream());
        start();
    }

    /**
     * Обработка запросов клиента
     */
    @Override
    public void run() {
        System.out.println("Серверная нить запущена.");
        listItem = getItems();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        //TODO оба этих варианта подписки скорее всего нужно будет объединить в один (тот, в котором идет подписка на конкретные изменения журнала)
        journalTask.subscribe(this); //подписка ПРОСТО на изменение списка задач для дальнейшего перерасчета времени засыпания
                                                //потока оповещений
        journalTask.notificationSubscribe(this); //подписка на конкретные изменения списка задач (добавление/удаление/
                                                                    //редактирование) для рассылки реквестов для всех клиентов
        try {
            Runnable run = new ServerNotifyThread(journalTask);
            serverNotifyThread = new Thread(run);
            serverNotifyThread.start();
            Request journalTaskRequest = new Request("JournalTask", listItem);
            objectMapper.writeValue((DataOutput) outputStream, journalTaskRequest);
            while (true) {
                Request inputRequest = null;
                try {
                    inputRequest = objectMapper.readValue((DataInput) inputStream, Request.class);
                    System.out.println(inputRequest.getCommand());
                } catch (IOException ex) {
                    finalWork();
                    System.out.println("Серверная нить закрыта.");
                    return;
                }
                for (Message msg : Message.values()) {
                    if (inputRequest.getCommand().contains(msg.message)) {
                        switch (msg) {
                            case ADD_ITEM:
                                System.out.println("Запрос принят: добавить задачу.");
                                Task newTask = objectMapper.convertValue(inputRequest.getData(), Task.class);
                                try {
                                    journalTask.addItem(newTask);
                                } catch (NameTaskException | IOException ex) {
                                    ex.printStackTrace();
                                }
                                listItem = getItems();
                                break;
                            case DELETE_ITEM:
                                System.out.println("Запрос принят: удалить задачу.");
                                try {
                                    journalTask.deleteItem((int) inputRequest.getData());
                                } catch (ItemNotFoundException | IOException ex) {
                                    ex.printStackTrace();
                                }
                                listItem = getItems();
                                break;
                            case UPDATE_ITEM:
                                System.out.println("Запрос принят: редактировать задачу.");
                                StringBuilder sbIndex = new StringBuilder();
                                for (int i = 10; i < inputRequest.getCommand().toCharArray().length; i++) {
                                    sbIndex.append(inputRequest.getCommand().toCharArray()[i]);
                                }
                                int index = Integer.parseInt(sbIndex.toString());
                                Task newItem = objectMapper.convertValue(inputRequest.getData(), Task.class);
                                try {
                                    journalTask.updateItem(index, newItem);
                                } catch (ItemNotFoundException | NameTaskException | IOException ex) {
                                    ex.printStackTrace();
                                }
                                listItem = getItems();
                                break;
//                            case SIZE_JOURNAL:
//                                System.out.println("Запрос принят: размер журнала.");
//                                Request reply_sj = new Request("SizeJournalTask", journalTask.getSize());
//                                try {
//                                    objectMapper.writeValue((DataOutput) outputStream, reply_sj);
//                                } catch (IOException ex) {
//                                    ex.printStackTrace();
//                                }
//                                break;
//                            case GET_ITEM:
//                                System.out.println("Запрос принят: получить задачу.");
//                                Task item = null;
//                                try {
//                                    item = getItem((int) inputRequest.getData());
//                                } catch (ItemNotFoundException ex) {
//                                    ex.printStackTrace();
//                                }
//                                Request reply_gt = new Request("GetItem", item);
//                                objectMapper.writeValue((DataOutput) outputStream, reply_gt);
//                                break;
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
        for (int i = 0; i < journalTask.getSize(); i++) {
            try {
                listTask.addLast((Task) journalTask.getItem(i));
            } catch (ItemNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        return listTask;
    }

//    /**
//     * Получение задачи из журнала по индексу
//     *
//     * @param index индекс задачи в журнале задач
//     * @return задача, соответствующая введенному индексу
//     * @throws ItemNotFoundException Неверное значение индекса
//     */
//    public Task getItem(int index) throws ItemNotFoundException {
//        return listItem.get(index);
//    }

//    public void startWork() throws IOException {
//
//    }

    /**
     * Завершение работы серверной нити
     */
    public void finalWork() {
        try {
            ServerStarter.clientList.remove(this);
            clientSocket.close();
            journalTask.finalWork();
            inputStream.close();
            outputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


//    /**
//     * Проверка имени на уникальность
//     *
//     * @param name Имя задачи для проверки
//     * @throws NameTaskException Задача с таким именем уже существует
//     * @throws IOException       Ошибка потоков
//     */
//    public void checkUniqueName(String name) throws NameTaskException, IOException {
//        for (Task task : listItem) {
//            if (task.getName().equals(name)) throw new NameTaskException("Задача с таким именем уже существует.");
//        }
//    }

    @Override
    public void listChanged() {
        serverNotifyThread.interrupt();
    }
}