package Sql;

public class LastRowReachedException extends RuntimeException {
    public LastRowReachedException(String message) {
        super(message);
    }
}
