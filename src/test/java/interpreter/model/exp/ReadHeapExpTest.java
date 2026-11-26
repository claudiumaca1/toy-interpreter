package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Lab 7 (A4): tests for rH (rule E6)
class ReadHeapExpTest {
    private IDictionary<String, IValue> symTable;
    private IHeap heap;

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
        heap = new MyHeap();
    }

    @Test
    void readsTheValueAtTheAddress() throws MyException {
        int a = heap.allocate(new IntValue(20));
        symTable.put("v", new RefValue(a, new IntType()));
        assertEquals(new IntValue(20), new ReadHeapExp(new VarExp("v")).eval(symTable, heap));
    }

    @Test
    void readsThroughTwoReferences() throws MyException {
        heap.allocate(new IntValue(20));                          // 1 -> 20
        heap.allocate(new RefValue(1, new IntType()));            // 2 -> (1, int)
        symTable.put("a", new RefValue(2, new RefType(new IntType())));
        IExp e = new ReadHeapExp(new ReadHeapExp(new VarExp("a")));
        assertEquals(new IntValue(20), e.eval(symTable, heap));
    }

    @Test
    void aNonReferenceExpressionThrows() {
        IExp e = new ReadHeapExp(new ValueExp(new IntValue(1)));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable, heap));
        assertEquals("rH: 1 is not a reference", ex.getMessage());
    }

    @Test
    void anAddressNotInTheHeapThrows() {
        symTable.put("v", new RefValue(0, new IntType()));      // the default value: null
        IExp e = new ReadHeapExp(new VarExp("v"));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable, heap));
        assertEquals("rH: address 0 is not in the heap", ex.getMessage());
    }

    @Test
    void toStringIsRH() {
        assertEquals("rH(v)", new ReadHeapExp(new VarExp("v")).toString());
    }
}