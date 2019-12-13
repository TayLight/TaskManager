package taskmanager.requests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonAutoDetect
public class CommandRequest implements RequestInterface {

    String message;

    public CommandRequest(){}

    public CommandRequest(String message) {
        this.message = message;
    }

    @JsonIgnore
    @Override
    public Object getData() {
        return null;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
