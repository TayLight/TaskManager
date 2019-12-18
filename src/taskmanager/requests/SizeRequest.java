package taskmanager.requests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonAutoDetect
public class SizeRequest implements RequestInterface<Integer> {
    @JsonDeserialize(as = Integer.class)
    Integer data;

    String message;

    public SizeRequest(Integer size, String message) {
        this.data = size;
        this.message = message;
    }

    public SizeRequest() {
    }

    @Override
    public Integer getData() {
        return data;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
