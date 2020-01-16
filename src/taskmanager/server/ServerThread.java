package taskmanager.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import taskmanager.Manager;
import taskmanager.exceptions.ItemNotFoundException;
import taskmanager.exceptions.NameTaskException;
import taskmanager.requests.*;
import taskmanager.task.Task;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class ServerThread implements Runnable{
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

    /**
     * управление журналом задач
     */
    private Manager journalTask;

    /**
     * журнал задач
     */
    LinkedList<Task> listItem;

    enum Message {
        LOAD_JOURNAL_TASK("LoadJournalTask"),
        ADD_ITEM("AddItem"),
        DELETE_ITEM("DeleteItem"),
        UPDATE_ITEM("UpdateItem"),
        SIZE_JOURNAL("SizeJournalTask"),
        GET_ITEM("GetItem");

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
    }

    /**
     * Обработка запросов клиента
     */
    @Override
    public void run() {
        System.out.println("Серверная нить запущена.");
        listItem = getItems();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            while (true) {
                Request inputRequest = null;
                try {
                    System.out.println("Пытаюсь получить команду...");
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
                            case LOAD_JOURNAL_TASK: //нужен ли он вообще теперь?
                                System.out.println("Запрос принят: отправить журнал задач.");
                                LoadJournalRequest loadJournalRequest = new LoadJournalRequest(listItem, "LoadJournalTask");
                                try {
                                    objectMapper.writeValue((DataOutput) outputStream, loadJournalRequest);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            case ADD_ITEM:
                                System.out.println("Запрос принят: добавить задачу.");
                                String message_ai = "Ok";
                                Task newTask = objectMapper.convertValue(inputRequest.getData(), Task.class);
                                try {
                                    checkUniqueName(newTask.getName());
                                    journalTask.addItem(newTask);
                                } catch (NameTaskException ex) {
                                    message_ai = "Error";
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                Request reply_at = new Request(message_ai, null);
                                objectMapper.writeValue((DataOutput) outputStream, reply_at);
                                listItem = getItems(); //обновление представления списка задач
                                break;
                            case DELETE_ITEM:
                                System.out.println("Запрос принят: удалить задачу.");
                                String message_di = "Ok";
                                try {
                                    journalTask.deleteItem((int)inputRequest.getData());
                                } catch (ItemNotFoundException ex) {
                                    message_di = "Error";
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                Request reply_dt = new Request(message_di, null);
                                objectMapper.writeValue((DataOutput) outputStream, reply_dt);
                                listItem = getItems(); //обновление представления списка задач
                                break;
                            case UPDATE_ITEM:
                                System.out.println("Запрос принят: редактировать задачу.");
                                String message_ui = "Ok";
                                char chIndex = inputRequest.getCommand().toCharArray()[10];
                                int index = Integer.parseInt(String.valueOf(chIndex));
                                Task newItem = objectMapper.convertValue(inputRequest.getData(), Task.class);
                                try{
                                    journalTask.updateItem(index, newItem);
                                } catch (ItemNotFoundException ex){
                                    message_ui = "Error";
                                } catch (IOException ex){
                                    ex.printStackTrace();
                                }
                                Request reply_ui = new Request(message_ui, null);
                                objectMapper.writeValue((DataOutput) outputStream, reply_ui);
                                listItem = getItems(); //обновление представления списка задач
                                break;
                            case SIZE_JOURNAL:
                                System.out.println("Запрос принят: размер журнала.");
                                Request reply_sj = new Request("SizeJournalTask", journalTask.size());
                                try {
                                    objectMapper.writeValue((DataOutput) outputStream, reply_sj);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            case GET_ITEM:
                                System.out.println("Запрос принят: получить задачу.");
                                Task item = null;
                                try{
                                    item = getItem((int)inputRequest.getData());
                                } catch (ItemNotFoundException ex) {
                                    ex.printStackTrace();
                                }
                                Request reply_gt =  new Request("GetItem", item);
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

    /** Получение журнала задач
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
//        try {
//            objectMapper.writeValue(outputStream, loadJournalRequest);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }

//    /** Добавление новой задачи в журнал
//     * @param newItem Новая задача
//     */
//    public void addItem(Task newItem) {
//        listItem.addLast(newItem);
//    }
//
//    /** Удаление задачи из журнала
//     * @param index Индекс удаляемой задачи
//     * @throws ItemNotFoundException Неверное значение индекса
//     * @throws IOException Ошибка потоков
//     */
//    public void deleteItem(int index) throws ItemNotFoundException, IOException {
//        if (index < 0 || index > listItem.size() - 1) throw new ItemNotFoundException("Неверное значение индекса.");
//        listItem.remove(index);
//    }

    /** Получение задачи из журнала по индексу
     * @param index индекс задачи в журнале задач
     * @return задача, соответствующая введенному индексу
     * @throws ItemNotFoundException Неверное значение индекса
     */
    public Task getItem(int index) throws ItemNotFoundException {
        return listItem.get(index);
    }


//    public void updateItem(int index, Task item) throws ItemNotFoundException {
//
//    }

//    public int size() {
//        return listTask.size();
//    }

    public void startWork() throws IOException {

    }

    /**
     * Завершение работы серверной нити
     */
    public void finalWork() {
        try{
            clientSocket.close();
            journalTask.finalWork();
            inputStream.close();
            outputStream.close();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }


    /** Проверка имени на уникальность
     * @param name Имя задачи для проверки
     * @throws NameTaskException Задача с таким именем уже существует
     * @throws IOException Ошибка потоков
     */
    public void checkUniqueName(String name) throws NameTaskException, IOException {
        for (Task task : listItem) {
            if (task.getName().equals(name)) throw new NameTaskException("Задача с таким именем уже существует.");
        }
    }

//    public void editTask(int index, LocalTime newTime) throws ItemNotFoundException{}
//
//    public void editTask(int index, String text) throws ItemNotFoundException{}
//
//    public void editTaskDescription(int index, String description) throws ItemNotFoundException{}
}
