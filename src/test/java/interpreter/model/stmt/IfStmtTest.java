package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IfStmtTest extends StmtTestBase {
    private PrgState state;
    private final IStmt thenS = new PrintStmt(new ValueExp(new IntValue(1)));
    private final IStmt elseS = new PrintStmt(new ValueExp(new IntValue(0)));

    @BeforeEach
    void setUp() throws MyException {
        state = freshState();
    }

    @Test
    void trueConditionPushesTheThenBranch() throws MyException {
        new IfStmt(new ValueExp(new BoolValue(true)), thenS, elseS).execute(state);
        assertSame(thenS, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void falseConditionPushesTheElseBranch() throws MyException {
        new IfStmt(new ValueExp(new BoolValue(false)), thenS, elseS).execute(state);
        assertSame(elseS, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void nonBooleanConditionThrows() {
        var ex = assertThrows(StatementException.class,
                () -> new IfStmt(new ValueExp(new IntValue(1)), thenS, elseS).execute(state));
        assertEquals("condition of if is not a boolean", ex.getMessage());
    }
}
