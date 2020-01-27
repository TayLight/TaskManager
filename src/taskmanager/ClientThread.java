package taskmanager;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import taskmanager.requests.Request;
import taskmanager.task.Task;

import java.io.*;
import java.util.List;

public class ClientThread extends Thread implements NotificationSubscription {
    private InputStream inputStream;
    private ObjectMapper objectMapper;
    private NotificationSubscriber subscriber;


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

    public ClientThread(InputStream inputStream) {
        this.inputStream = inputStream;
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void run() {
        System.out.println("Начинаем получать сообщения с сервера:");
        Request request;
        try {
            while (true) {
                request = objectMapper.readValue((DataInput)inputStream, Request.class);
                for (Message msg: Message.values()){
                    if(request.getCommand().equals(msg)){
                        switch (msg){
                            case ADD_ITEM:
                                subscriber.taskAdded((Task) request.getData());
                                break;
                            case DELETE_ITEM:
                                subscriber.taskDeleted((Integer) request.getData());
                                break;
                            case UPDATE_ITEM:
                                StringBuilder sbIndex = new StringBuilder();
                                for (int i = 10; i < request.getCommand().toCharArray().length; i++) {
                                    sbIndex.append(request.getCommand().toCharArray()[i]);
                                }
                                int index = Integer.parseInt(sbIndex.toString());
                                subscriber.taskUpdated(index, (Task) request.getData());
                                break;
                            case JOURNAL_TASK:
                                subscriber.newJournalTask((List<Task>) request.getData());
                                break;
                            case NOTIFY:
                                subscriber.notifyTask((Integer) request.getData());
                                break;
                        }
                    }
                }
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void subscribe(NotificationSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void unsubscribe() {
        this.subscriber=null;
    }
}
