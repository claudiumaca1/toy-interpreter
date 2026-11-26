package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import interpreter.model.value.IValue;

import java.util.Map;

// Lab 7 (A4): the heap - a map from addresses (int, starting at 1) to values. Unlike a plain
// dictionary, the heap itself chooses the address of every new value, so addresses are unique.
public interface IHeap {
    // Lab 7 (A4): stores value at a new (free) address and returns that address
    int allocate(IValue value);

    // Lab 7 (A4): the value at the given address; a missing address throws ADTException
    IValue get(int address) throws ADTException;

    // Lab 7 (A4): replaces the value at the given address; a missing address throws ADTException
    void update(int address, IValue value) throws ADTException;

    // Lab 7 (A4): true if the address is in the heap
    boolean isDefined(int address);

    // Lab 7 (A4): a read-only copy of the content (used by the log file and the garbage collector)
    Map<Integer, IValue> getContent();

    // Lab 7 (A4): replaces the whole content (used by the garbage collector)
    void setContent(Map<Integer, IValue> content);
}