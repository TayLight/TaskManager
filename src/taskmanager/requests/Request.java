package taskmanager.requests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Класс запроса, использующийся для общения клиентс-сервер
 */
@JsonAutoDetect
public class Request {
    /**
     * Сообщение-команда
     */
    private String command;
    /**
     * Данные передающиеся
     */
    private Object data;

    /**
     * @return возвращает команду
     */
    public String getCommand(){
        return command;
    }

    /**
     * @return возвращает данные
     */
    public Object getData(){
        return data;
    }

    public Request(){ }

    public Request(String command, Object data){
        this.command = command;
        this.data = data;
    }

}