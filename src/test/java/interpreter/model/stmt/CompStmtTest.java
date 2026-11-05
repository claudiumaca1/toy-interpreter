package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompStmtTest extends StmtTestBase {
    @Test
    void pushesSecondThenFirstSoFirstIsOnTop() throws MyException {
        PrgState state = freshState();
        IStmt first = new PrintStmt(new ValueExp(new IntValue(1)));
        IStmt second = new NopStmt();

        new CompStmt(first, second).execute(state);

        assertSame(first, state.getExeStack().pop());
        assertSame(second, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }
}
