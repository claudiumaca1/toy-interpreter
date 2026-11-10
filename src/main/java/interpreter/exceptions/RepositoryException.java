package interpreter.exceptions;

// Lab 6 (A3): errors of the repository, e.g. the log file cannot be written
public class RepositoryException extends MyException {
    public RepositoryException(String message) {
        super(message);
    }
}