package interpreter.model.stmt;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrintStmtTest extends StmtTestBase {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = freshState();
    }

    @Test
    void appendsTheValueToOut() throws MyException {
        new PrintStmt(new ValueExp(new IntValue(4))).execute(state);
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(4), state.getOut().get(0));
    }

    @Test
    void valuesAreAppendedInTheOrderTheyArePrinted() throws MyException {
        new PrintStmt(new ValueExp(new IntValue(1))).execute(state);
        new PrintStmt(new ValueExp(new IntValue(2))).execute(state);
        assertEquals(new IntValue(1), state.getOut().get(0));
        assertEquals(new IntValue(2), state.getOut().get(1));
    }

    @Test
    void errorInTheExpressionIsPropagated() {
        assertThrows(ExpressionException.class, () -> new PrintStmt(new VarExp("z")).execute(state));
    }
}
