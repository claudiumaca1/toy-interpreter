package interpreter.model.exp;

import interpreter.exceptions.MyException;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValueExpTest {
    @Test
    void evalReturnsTheConstant() throws MyException {
        IExp e = new ValueExp(new IntValue(7));
        // Lab 7 (A4): eval also gets the heap
        assertEquals(new IntValue(7), e.eval(new MyDictionary<>(), new MyHeap()));
    }
}