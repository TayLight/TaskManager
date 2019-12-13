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
import java.net.SocketException;
import java.time.LocalTime;
import java.util.LinkedList;

public class ServerThread implements Runnable, Manager<Task> {
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

    /**журнал задач
     *
     */
    LinkedList<Task> listTask;

    enum Message{
        LOAD_JOURNAL_TASK("LoadJournalTask"),
        ADD_TASK("AddTask"),
        DELETE_TASK("DeleteTask"),
        CHECK_NAME("CheckName"),
        CLOSE_SESSION("CloseSession");

        private String message;
        Message(String message){
            this.message = message;
        }
    }

    /** Конструктор, создающий поток
     * @throws IOException ошибка потоков
     */
    public ServerThread(Socket clientSocket) throws IOException  {
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
        boolean exit = false;
        try{
            while (!exit) {
                CommandRequest commandRequest = null;
                try {
                    System.out.println("Пытаюсь получить команду...");
                    commandRequest = objectMapper.readValue(inputStream, CommandRequest.class);
                    System.out.println(commandRequest.getMessage());
                } catch (IOException ex) {
                    //ex.printStackTrace();
                    clientSocket.close();
                    exit = true;
                    System.out.println("Серверная нить закрыта.");
                    return;
                }
                for(Message msg : Message.values()){
                    if(commandRequest.getMessage().equals(msg.message)){
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
                                NewTaskRequest newTaskRequest = objectMapper.readValue(inputStream, NewTaskRequest.class);
                                addItem(newTaskRequest.getData());
                                break;
                            case CHECK_NAME:
                                System.out.println("Запрос принят: проверить уникальность имени.");
                                String message_cn = "Ok";
                                NameCheckRequest nameCheckRequest = objectMapper.readValue(inputStream, NameCheckRequest.class);
                                try{
                                    checkUniqueName(nameCheckRequest.getData());
                                } catch (NameTaskException ex){
                                    message_cn = "Error";
                                } catch(IOException ex){
                                    ex.printStackTrace();
                                }
                                CommandRequest reply_cn = new CommandRequest(message_cn);
                                objectMapper.writeValue(outputStream, reply_cn);
                                break;
                            case DELETE_TASK:
                                System.out.println("Запрос принят: удалить задачу.");
                                String message_dt = "Ok";
                                DeleteTaskRequest deleteTaskRequest = objectMapper.readValue(inputStream, DeleteTaskRequest.class);
                                try{
                                    deleteItem(deleteTaskRequest.getData());
                                } catch(ItemNotFoundException ex){
                                    message_dt = "Error";
                                } catch(IOException ex){
                                    ex.printStackTrace();
                                }
                                CommandRequest reply_dt = new CommandRequest(message_dt);
                                objectMapper.writeValue(outputStream, reply_dt);
                                break;
                            case CLOSE_SESSION:
                                clientSocket.close();
                                System.out.println("Серверная нить закрыта.");
                                exit = true;
                                break;
                        }
                    }
                }
            }
        } catch (IOException ex){
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
//        objectMapper.registerModule(new JavaTimeModule()); //перенести на уровень выше?
//        try {
//            objectMapper.writeValue(outputStream, loadJournalRequest);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }

    public void addItem (Task newItem){
        listTask.addLast(newItem);
    }

    public void deleteItem(int index) throws ItemNotFoundException, IOException {
        if (index < 0 || index > listTask.size() - 1) throw new ItemNotFoundException("Неверное значение индекса.");
        listTask.remove(index);
    }

    public Task getItem(int index) throws ItemNotFoundException{
        return listTask.get(index);
    };

    public void updateItem(int index, Task item) throws ItemNotFoundException{

    }

    public int size(){
        return listTask.size();
    }

    public void startWork() throws IOException{

    }

    public void finalWork(){

    }

    public void checkUniqueName(String name) throws NameTaskException, IOException{
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
