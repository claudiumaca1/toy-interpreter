package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ArithExp;
import interpreter.model.exp.ArithOp;
import interpreter.model.exp.RelOp;
import interpreter.model.exp.RelationalExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Lab 7 (A4): tests for the while statement (rule S13)
class WhileStmtTest extends StmtTestBase {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = freshState();
        new VarDeclStmt("v", new IntType()).execute(state);
    }

    private static ValueExp num(int n) {
        return new ValueExp(new IntValue(n));
    }

    // while (v > 0) (print(v); v = v - 1)
    private static WhileStmt countDown() {
        return new WhileStmt(new RelationalExp(RelOp.GT, new VarExp("v"), num(0)),
                new CompStmt(new PrintStmt(new VarExp("v")),
                        new AssignStmt("v", new ArithExp(ArithOp.SUB, new VarExp("v"), num(1)))));
    }

    // runs the statements of the stack until it is empty (like Controller.allStep, no log)
    private void runAll(IStmt program) throws MyException {
        state.getExeStack().push(program);
        while (!state.getExeStack().isEmpty())
            state.getExeStack().pop().execute(state);
    }

    @Test
    void oneStepWithATrueConditionPushesTheWhileThenTheBodyOnTop() throws MyException {
        WhileStmt loop = countDown();
        state.getSymTable().update("v", new IntValue(1));
        loop.execute(state);
        assertEquals(loop.body(), state.getExeStack().pop());      // the body is on top
        assertEquals(loop, state.getExeStack().pop());             // the while under it
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void oneStepWithAFalseConditionPushesNothing() throws MyException {
        countDown().execute(state);                                // v = 0
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void aLoopThatRunsZeroTimes() throws MyException {
        runAll(countDown());                                       // v = 0
        assertEquals(0, state.getOut().size());
    }

    @Test
    void aLoopThatRunsOnce() throws MyException {
        state.getSymTable().update("v", new IntValue(1));
        runAll(countDown());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(1), state.getOut().get(0));
        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));
    }

    @Test
    void aLoopThatRunsSeveralTimes() throws MyException {
        state.getSymTable().update("v", new IntValue(4));
        runAll(countDown());
        assertEquals(4, state.getOut().size());
        for (int i = 0; i < 4; i++)
            assertEquals(new IntValue(4 - i), state.getOut().get(i));
        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));
    }

    @Test
    void whileWithANonBooleanConditionThrows() {
        var loop = new WhileStmt(new ValueExp(new IntValue(1)), new NopStmt());
        assertThrows(StatementException.class, () -> loop.execute(state));
    }

    @Test
    void whileWithAFalseConstantIsSkipped() throws MyException {
        runAll(new WhileStmt(new ValueExp(new BoolValue(false)), new PrintStmt(num(1))));
        assertEquals(0, state.getOut().size());
    }

    @Test
    void toStringIsInTheUsualForm() {
        assertEquals("(while ((v > 0)) nop)",
                new WhileStmt(new RelationalExp(RelOp.GT, new VarExp("v"), num(0)), new NopStmt()).toString());
    }
}