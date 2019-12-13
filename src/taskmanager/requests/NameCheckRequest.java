package taskmanager.requests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class NameCheckRequest implements RequestInterface<String> {

    String message;

    String data;


    public NameCheckRequest(String message, String name) {
        this.message = message;
        this.data = name;
    }

    public NameCheckRequest(){}

    @Override
    public String getData() {
        return data;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
