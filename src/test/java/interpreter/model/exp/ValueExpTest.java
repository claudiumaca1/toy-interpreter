package interpreter.model.exp;

import interpreter.exceptions.MyException;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValueExpTest {
    @Test
    void evalReturnsTheConstant() throws MyException {
        IExp e = new ValueExp(new IntValue(7));
        assertEquals(new IntValue(7), e.eval(new MyDictionary<>()));
    }
}
