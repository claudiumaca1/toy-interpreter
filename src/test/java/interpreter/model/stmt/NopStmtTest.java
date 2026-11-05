package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NopStmtTest extends StmtTestBase {
    @Test
    void doesNotChangeTheState() throws MyException {
        PrgState state = freshState();
        new NopStmt().execute(state);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals("{}", state.getSymTable().toString());
        assertEquals(0, state.getOut().size());
    }
}
