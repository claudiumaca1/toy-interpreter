package interpreter.controller;

import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// Lab 7 (A4): the garbage collector - removes the heap locations the program can no longer
// reach. It works on plain Java maps (not on PrgState), so it can be tested on its own.
public final class GarbageCollector {
    private GarbageCollector() {}

    /** Lab 7 (A4): the addresses that occur in the RefValues of the SymTable. */
    public static Set<Integer> getAddrFromSymTable(Collection<IValue> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).address())
                .collect(Collectors.toSet());
    }

    /** Lab 7 (A4): UNSAFE: keeps only the heap entries referred to directly from the SymTable. */
    public static Map<Integer, IValue> unsafeGarbageCollector(Set<Integer> symTableAddr,
                                                              Map<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Lab 8 (A4): the addresses reachable from the roots (the addresses of the SymTable),
     * directly or through the heap: whenever a reachable location contains a RefValue, its
     * address is reachable too. Only addresses that are in the heap are returned (0 = null
     * is never there). The computation stops because every address is visited at most once
     * and the heap is finite (so it would terminate even if the heap contained a cycle).
     */
    public static Set<Integer> reachableAddresses(Set<Integer> roots, Map<Integer, IValue> heap) {
        Set<Integer> visited = new HashSet<>();
        Deque<Integer> toVisit = new ArrayDeque<>(roots);      // the work list
        while (!toVisit.isEmpty()) {
            int address = toVisit.pop();
            if (!heap.containsKey(address) || !visited.add(address))
                continue;                                       // not in the heap, or seen already
            if (heap.get(address) instanceof RefValue ref)
                toVisit.push(ref.address());
        }
        return visited;
    }

    /**
     * Lab 8 (A4): SAFE: keeps the heap entries reachable from the SymTable, directly or
     * through other heap locations (e.g. a -> 2 -> 1 in Ex9).
     */
    public static Map<Integer, IValue> safeGarbageCollector(Set<Integer> symTableAddr,
                                                            Map<Integer, IValue> heap) {
        Set<Integer> reachable = reachableAddresses(symTableAddr, heap);
        return heap.entrySet().stream()
                .filter(e -> reachable.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}