package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VarDeclStmtTest extends StmtTestBase {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = freshState();
    }

    @Test
    void intVariableGetsDefaultZero() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));
    }

    @Test
    void boolVariableGetsDefaultFalse() throws MyException {
        new VarDeclStmt("b", new BoolType()).execute(state);
        assertEquals(new BoolValue(false), state.getSymTable().lookup("b"));
    }

    @Test
    void declaringTheSameVariableTwiceThrows() throws MyException {
        IStmt decl = new VarDeclStmt("x", new IntType());
        decl.execute(state);
        var ex = assertThrows(StatementException.class, () -> decl.execute(state));
        assertEquals("variable x is already declared", ex.getMessage());
    }

    // ---- Lab 7 (A4): rule S3 gives a Ref T variable the default value (0, T)

    @Test
    void refVariableGetsDefaultNullReference() throws MyException {
        new VarDeclStmt("v", new RefType(new IntType())).execute(state);
        assertEquals(new RefValue(0, new IntType()), state.getSymTable().lookup("v"));
    }

    @Test
    void refRefVariableGetsDefaultNullReferenceToARef() throws MyException {
        new VarDeclStmt("a", new RefType(new RefType(new IntType()))).execute(state);
        assertEquals(new RefValue(0, new RefType(new IntType())), state.getSymTable().lookup("a"));
    }
}