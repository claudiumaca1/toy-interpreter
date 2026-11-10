package interpreter.model.adt;

import interpreter.exceptions.ADTException;

import java.util.List;

public interface IList<T> {
    void add(T v);

    T get(int index) throws ADTException;

    int size();

    // Lab 5 (A3): the elements as a list, in insertion order (used by the log file)
    List<T> toList();
}
