package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

// Lab 7 (A4): tests for the heap
class MyHeapTest {
    private IHeap heap;

    @BeforeEach
    void setUp() {
        heap = new MyHeap();
    }

    @Test
    void allocateReturnsTheAddressesOneTwoThree() {
        assertEquals(1, heap.allocate(new IntValue(20)));
        assertEquals(2, heap.allocate(new BoolValue(true)));
        assertEquals(3, heap.allocate(new RefValue(1, new IntType())));
    }

    @Test
    void getReturnsTheAllocatedValue() throws ADTException {
        int a = heap.allocate(new IntValue(20));
        assertEquals(new IntValue(20), heap.get(a));
    }

    @Test
    void getOfAMissingAddressThrows() {
        assertThrows(ADTException.class, () -> heap.get(1));
        assertThrows(ADTException.class, () -> heap.get(0));    // 0 is null, never allocated
    }

    @Test
    void updateChangesTheValue() throws ADTException {
        int a = heap.allocate(new IntValue(20));
        heap.update(a, new IntValue(30));
        assertEquals(new IntValue(30), heap.get(a));
    }

    @Test
    void updateOfAMissingAddressThrows() {
        assertThrows(ADTException.class, () -> heap.update(5, new IntValue(1)));
    }

    @Test
    void isDefinedIsTrueOnlyForAllocatedAddresses() {
        heap.allocate(new IntValue(20));
        assertTrue(heap.isDefined(1));
        assertFalse(heap.isDefined(2));
        assertFalse(heap.isDefined(0));
    }

    @Test
    void getContentIsAReadOnlyCopy() {
        heap.allocate(new IntValue(20));
        Map<Integer, IValue> content = heap.getContent();
        assertEquals(Map.of(1, new IntValue(20)), content);
        assertThrows(UnsupportedOperationException.class, () -> content.put(2, new IntValue(1)));
    }

    @Test
    void setContentReplacesTheContentButAddressesAreNotReused() throws ADTException {
        heap.allocate(new IntValue(20));
        heap.allocate(new IntValue(30));
        heap.setContent(Map.of(2, new IntValue(30)));
        assertFalse(heap.isDefined(1));
        assertEquals(new IntValue(30), heap.get(2));
        assertEquals(3, heap.allocate(new IntValue(40)));      // not 1, not 2
    }

    @Test
    void toStringIsSortedByAddress() {
        heap.allocate(new IntValue(20));
        heap.allocate(new RefValue(1, new IntType()));
        assertEquals("{1 -> 20, 2 -> (1, int)}", heap.toString());
    }
}