package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import java.util.List;

public interface IStack<T> {
    void push(T v);

    T pop() throws ADTException;

    boolean isEmpty();

    // Lab 5 (A3): the elements as a list, the top of the stack first (used by the log file)
    List<T> toList();
}
