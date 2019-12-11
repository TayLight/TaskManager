package taskmanager;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.LinkedList;
@JsonAutoDetect
public class DeleteTaskRequest implements Request<Integer> {
    @JsonDeserialize(as = Integer.class)
    Integer index;

    String message;

    public DeleteTaskRequest(Integer index, String message) {
        this.index = index;
        this.message = message;
    }

    @Override
    public Integer getData() {
        return 0;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
