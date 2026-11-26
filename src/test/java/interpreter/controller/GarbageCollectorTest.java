package interpreter.controller;

import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// Lab 7 (A4): tests for the garbage collector; it works on plain Java maps, so no program
// has to be run
class GarbageCollectorTest {
    // the state of Ex9 after new(v, 30): v -> (3, int), a -> (2, Ref(int))
    private final List<IValue> symTable = List.of(
            new RefValue(3, new IntType()), new RefValue(2, new RefType(new IntType())));
    private final Map<Integer, IValue> heap = Map.of(
            1, new IntValue(20), 2, new RefValue(1, new IntType()),
            3, new IntValue(30), 4, new IntValue(7));

    @Test
    void getAddrFromSymTableKeepsOnlyTheAddressesOfReferences() {
        List<IValue> values = List.of(new IntValue(5), new BoolValue(true),
                new RefValue(3, new IntType()), new RefValue(2, new RefType(new IntType())));
        assertEquals(Set.of(2, 3), GarbageCollector.getAddrFromSymTable(values));
    }

    @Test
    void unsafeCollectorRemovesTheUnreachableLocations() {
        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(symTable);
        Map<Integer, IValue> kept = GarbageCollector.unsafeGarbageCollector(roots, heap);
        assertFalse(kept.containsKey(4));
        assertEquals(new IntValue(30), kept.get(3));
    }

    @Test
    void unsafeCollectorAlsoRemovesALocationReachableOnlyThroughTheHeap() {
        // this is why it is unsafe: 1 is still reachable through a -> 2 -> 1
        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(symTable);
        Map<Integer, IValue> kept = GarbageCollector.unsafeGarbageCollector(roots, heap);
        assertEquals(Set.of(2, 3), kept.keySet());
    }

    // ---- Lab 8 (A4): the safe collector

    @Test
    void keepsTheLocationsReachableThroughTheHeap() {
        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(symTable);
        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(roots, heap);
        assertEquals(Set.of(1, 2, 3), kept.keySet());          // 4 is garbage, 1 is not
        assertEquals(new IntValue(20), kept.get(1));
    }

    @Test
    void reachableAddressesFollowsAChainOfReferences() {
        // 3 -> (2, Ref(int)), 2 -> (1, int), 1 -> 5; 4 is not reachable
        Map<Integer, IValue> h = Map.of(
                1, new IntValue(5), 2, new RefValue(1, new IntType()),
                3, new RefValue(2, new RefType(new IntType())), 4, new IntValue(9));
        assertEquals(Set.of(1, 2, 3), GarbageCollector.reachableAddresses(Set.of(3), h));
    }

    @Test
    void reachableAddressesIgnoresNullAndMissingAddresses() {
        Map<Integer, IValue> h = Map.of(1, new IntValue(5), 2, new RefValue(7, new IntType()));
        assertEquals(Set.of(2), GarbageCollector.reachableAddresses(Set.of(0, 2), h));
    }

    @Test
    void reachableAddressesStopsOnACycle() {
        // not possible in the toy language (the types differ), but the collector still stops
        Map<Integer, IValue> h = Map.of(
                1, new RefValue(2, new IntType()), 2, new RefValue(1, new IntType()));
        assertEquals(Set.of(1, 2), GarbageCollector.reachableAddresses(Set.of(1), h));
    }

    @Test
    void anEmptySymTableMakesTheWholeHeapGarbage() {
        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(Set.of(), heap);
        assertTrue(kept.isEmpty());
    }

    // Lab 8, exercise 2: a -> (3, Ref(int)), c -> (0, int);
    // heap 1 -> 7, 2 -> 8, 3 -> (2, int), 4 -> (1, int), 5 -> (4, Ref(int))
    @Test
    void lab8Exercise2() {
        List<IValue> st = List.of(new RefValue(3, new RefType(new IntType())),
                new RefValue(0, new IntType()));
        Map<Integer, IValue> h = Map.of(
                1, new IntValue(7), 2, new IntValue(8), 3, new RefValue(2, new IntType()),
                4, new RefValue(1, new IntType()), 5, new RefValue(4, new RefType(new IntType())));
        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(st);
        assertEquals(Set.of(2, 3), GarbageCollector.reachableAddresses(roots, h));
        assertEquals(Set.of(3), GarbageCollector.unsafeGarbageCollector(roots, h).keySet());
        assertEquals(Set.of(2, 3), GarbageCollector.safeGarbageCollector(roots, h).keySet());
    }
}