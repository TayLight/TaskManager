package taskmanager.requests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonAutoDetect
public class DeleteTaskRequest implements RequestInterface<Integer> {
    @JsonDeserialize(as = Integer.class)
    Integer data;

    String message;

    public DeleteTaskRequest(Integer index, String message) {
        this.data = index;
        this.message = message;
    }

    public DeleteTaskRequest(){}

    @Override
    public Integer getData() {
        return data;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
