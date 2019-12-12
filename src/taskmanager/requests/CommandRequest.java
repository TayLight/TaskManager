package taskmanager.requests;

public class CommandRequest implements RequestInterface {

    String message;

    public CommandRequest(String message) {
        this.message = message;
    }

    @Override
    public Object getData() {
        return null;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
