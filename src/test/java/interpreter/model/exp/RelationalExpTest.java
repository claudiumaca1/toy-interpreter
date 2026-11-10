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

// Lab 5 (A3): tests for RelationalExp (rule E5)
class RelationalExpTest {
    private IDictionary<String, IValue> symTable;

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
    }

    private static IExp num(int n) {
        return new ValueExp(new IntValue(n));
    }

    // each operator once with a true and once with a false result
    @ParameterizedTest
    @CsvSource({
            "LT, 2, 3, true",  "LT, 3, 3, false",
            "LE, 3, 3, true",  "LE, 4, 3, false",
            "EQ, 3, 3, true",  "EQ, 2, 3, false",
            "NE, 2, 3, true",  "NE, 3, 3, false",
            "GT, 4, 3, true",  "GT, 3, 3, false",
            "GE, 3, 3, true",  "GE, 2, 3, false"
    })
    void operatorsGiveTheExpectedResult(RelOp op, int a, int b, boolean expected) throws MyException {
        IExp e = new RelationalExp(op, num(a), num(b));
        assertEquals(new BoolValue(expected), e.eval(symTable));
    }

    @Test
    void firstOperandNotAnIntegerThrows() {
        IExp e = new RelationalExp(RelOp.LT, new ValueExp(new BoolValue(true)), num(1));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("first operand is not an integer", ex.getMessage());
    }

    @Test
    void secondOperandNotAnIntegerThrows() {
        IExp e = new RelationalExp(RelOp.LT, num(1), new ValueExp(new BoolValue(true)));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("second operand is not an integer", ex.getMessage());
    }

    @Test
    void operandsAreEvaluatedFromTheSymbolTable() throws MyException {
        symTable.put("a", new IntValue(5));
        IExp e = new RelationalExp(RelOp.GT, new VarExp("a"), num(1));
        assertEquals(new BoolValue(true), e.eval(symTable));
    }

    @Test
    void toStringIsInfixForm() {
        assertEquals("(a < 3)", new RelationalExp(RelOp.LT, new VarExp("a"), num(3)).toString());
    }
}
