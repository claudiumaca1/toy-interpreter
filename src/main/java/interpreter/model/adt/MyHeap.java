package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import interpreter.model.value.IValue;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;

// Lab 7 (A4): the heap, implemented with a HashMap and the next free address
public class MyHeap implements IHeap {
    private Map<Integer, IValue> heap = new HashMap<>();
    private int nextFree = 1;                       // address 0 means null

    // Lab 7 (A4): stores the value at the next free address; addresses are never reused,
    // so they stay unique even after the garbage collector removes locations
    @Override
    public int allocate(IValue value) {
        int address = nextFree++;
        heap.put(address, value);
        return address;
    }

    // Lab 7 (A4): the value stored at address; a missing address throws ADTException
    @Override
    public IValue get(int address) throws ADTException {
        if (!heap.containsKey(address))
            throw new ADTException("address " + address + " is not in the heap");
        return heap.get(address);
    }

    // Lab 7 (A4): changes the value stored at address; a missing address throws ADTException
    @Override
    public void update(int address, IValue value) throws ADTException {
        if (!heap.containsKey(address))
            throw new ADTException("address " + address + " is not in the heap");
        heap.put(address, value);
    }

    // Lab 7 (A4): true if the address is in the heap
    @Override
    public boolean isDefined(int address) {
        return heap.containsKey(address);
    }

    // Lab 7 (A4): a read-only copy of the content
    @Override
    public Map<Integer, IValue> getContent() {
        return Map.copyOf(heap);
    }

    // Lab 7 (A4): replaces the content with a (modifiable) copy of the given map; nextFree is
    // not changed, so a new allocation never reuses an address
    @Override
    public void setContent(Map<Integer, IValue> content) {
        heap = new HashMap<>(content);
    }

    // Lab 7 (A4): the content sorted by address, e.g. {1 -> 20, 2 -> (1, int)}
    @Override
    public String toString() {
        var sj = new StringJoiner(", ", "{", "}");
        new TreeMap<>(heap).forEach((a, v) -> sj.add(a + " -> " + v));
        return sj.toString();
    }
}