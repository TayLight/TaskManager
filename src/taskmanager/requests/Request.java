package taskmanager.requests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class Request {
    private String command;
    private Object data;

    public String getCommand(){
        return command;
    }

    public Object getData(){
        return data;
    }

    public Request(){ }

    public Request(String command, Object data){
        this.command = command;
        this.data = data;
    }

}