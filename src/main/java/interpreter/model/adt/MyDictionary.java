package interpreter.model.adt;

import interpreter.exceptions.ADTException;

import java.util.HashMap;
import java.util.Map;

public class MyDictionary<K, V> implements IDictionary<K, V> {
    private final Map<K, V> map = new HashMap<>();

    @Override
    public boolean isDefined(K key) {
        return map.containsKey(key);
    }

    @Override
    public V lookup(K key) throws ADTException {
        if (!map.containsKey(key))
            throw new ADTException("key " + key + " is not in the dictionary");
        return map.get(key);
    }

    @Override
    public void put(K key, V value) {
        map.put(key, value);
    }

    @Override
    public void update(K key, V value) throws ADTException {
        if (!map.containsKey(key))
            throw new ADTException("key " + key + " is not in the dictionary");
        map.put(key, value);
    }

    @Override
    public String toString() {
        return map.toString();
    }

    // Lab 5 (A3): removes the key from the dictionary
    @Override
    public void remove(K key) throws ADTException {
        if (!map.containsKey(key))
            throw new ADTException("key " + key + " is not in the dictionary");
        map.remove(key);
    }

    // Lab 5 (A3): returns a read-only copy of the dictionary
    @Override
    public Map<K, V> toMap() {
        return Map.copyOf(map);
    }
}
