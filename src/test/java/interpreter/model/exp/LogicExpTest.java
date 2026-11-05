package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class LogicExpTest {
    private IDictionary<String, IValue> symTable;

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
    }

    private static IExp bool(boolean b) {
        return new ValueExp(new BoolValue(b));
    }

    @ParameterizedTest
    @CsvSource({
            "AND, true, true, true",
            "AND, true, false, false",
            "AND, false, true, false",
            "AND, false, false, false",
            "OR, true, true, true",
            "OR, true, false, true",
            "OR, false, true, true",
            "OR, false, false, false"
    })
    void truthTable(LogicOp op, boolean a, boolean b, boolean expected) throws MyException {
        IExp e = new LogicExp(op, bool(a), bool(b));
        assertEquals(new BoolValue(expected), e.eval(symTable));
    }

    @Test
    void firstOperandNotBooleanThrows() {
        IExp e = new LogicExp(LogicOp.AND, new ValueExp(new IntValue(1)), bool(true));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("first operand is not a boolean", ex.getMessage());
    }

    @Test
    void secondOperandNotBooleanThrows() {
        IExp e = new LogicExp(LogicOp.OR, bool(true), new ValueExp(new IntValue(1)));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("second operand is not a boolean", ex.getMessage());
    }
}
