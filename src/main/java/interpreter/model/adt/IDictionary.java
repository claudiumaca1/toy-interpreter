package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import java.util.Map;

public interface IDictionary<K, V> {
    boolean isDefined(K key);

    V lookup(K key) throws ADTException;

    void put(K key, V value);

    void update(K key, V value) throws ADTException;

    // Lab 5 (A3): removes the key (used by closeRFile); a missing key throws ADTException
    void remove(K key) throws ADTException;

    // Lab 5 (A3): a read-only copy of the content (used by the log file and the tests)
    Map<K, V> toMap();
}
