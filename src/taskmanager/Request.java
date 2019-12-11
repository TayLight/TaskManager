package taskmanager;

public interface Request<T> {

    T getData();

    String getMessage();
}
