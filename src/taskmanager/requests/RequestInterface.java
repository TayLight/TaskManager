package taskmanager.requests;

public interface RequestInterface<T> {

    T getData();

    String getMessage();
}
