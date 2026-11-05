package interpreter.model.stmt;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssignStmtTest extends StmtTestBase {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = freshState();
    }

    @Test
    void assignsTheValueOfTheExpression() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state);
        new AssignStmt("x", new ValueExp(new IntValue(5))).execute(state);
        assertEquals(new IntValue(5), state.getSymTable().lookup("x"));
    }

    @Test
    void assignmentToUndeclaredVariableThrows() {
        var ex = assertThrows(StatementException.class,
                () -> new AssignStmt("y", new ValueExp(new IntValue(3))).execute(state));
        assertEquals("variable y was not declared", ex.getMessage());
    }

    @Test
    void typeMismatchThrows() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state);
        var ex = assertThrows(StatementException.class,
                () -> new AssignStmt("x", new ValueExp(new BoolValue(true))).execute(state));
        assertEquals("declared type of variable x and type of the assigned expression do not match",
                ex.getMessage());
    }

    @Test
    void errorInTheExpressionIsPropagated() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state);
        assertThrows(ExpressionException.class,
                () -> new AssignStmt("x", new VarExp("unknown")).execute(state));
    }
}
