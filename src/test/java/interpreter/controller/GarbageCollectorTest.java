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
}