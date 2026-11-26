package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ArithExp;
import interpreter.model.exp.ArithOp;
import interpreter.model.exp.ReadHeapExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Lab 7 (A4): tests for new (rule S11) and wH (rule S12)
class HeapStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = PrgState.of(new NopStmt());
        new VarDeclStmt("v", new RefType(new IntType())).execute(state);              // Ref int v
    }

    private static ValueExp num(int n) {
        return new ValueExp(new IntValue(n));
    }

    // ---- new (S11)

    @Test
    void newAllocatesAtAddressOneAndUpdatesTheVariable() throws MyException {
        new NewStmt("v", num(20)).execute(state);
        assertEquals(new RefValue(1, new IntType()), state.getSymTable().lookup("v"));
        assertEquals(new IntValue(20), state.getHeap().get(1));
    }

    @Test
    void newTwiceAllocatesANewAddressAndKeepsTheOldLocation() throws MyException {
        new NewStmt("v", num(20)).execute(state);
        new NewStmt("v", num(30)).execute(state);
        assertEquals(new RefValue(2, new IntType()), state.getSymTable().lookup("v"));
        assertEquals(new IntValue(20), state.getHeap().get(1));
        assertEquals(new IntValue(30), state.getHeap().get(2));
    }

    @Test
    void newOfARefRefStoresTheReference() throws MyException {
        new NewStmt("v", num(20)).execute(state);
        new VarDeclStmt("a", new RefType(new RefType(new IntType()))).execute(state);
        new NewStmt("a", new VarExp("v")).execute(state);
        assertEquals(new RefValue(2, new RefType(new IntType())), state.getSymTable().lookup("a"));
        assertEquals(new RefValue(1, new IntType()), state.getHeap().get(2));
    }

    @Test
    void newOfAnUndeclaredVariableThrows() {
        var ex = assertThrows(StatementException.class, () -> new NewStmt("x", num(1)).execute(state));
        assertEquals("new: x is not declared", ex.getMessage());
    }

    @Test
    void newOfAVariableThatIsNotAReferenceThrows() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state);
        var ex = assertThrows(StatementException.class, () -> new NewStmt("x", num(1)).execute(state));
        assertEquals("new: x is not a reference", ex.getMessage());
    }

    @Test
    void newWithAValueOfTheWrongTypeThrows() {
        var exp = new ValueExp(new BoolValue(true));
        assertThrows(StatementException.class, () -> new NewStmt("v", exp).execute(state));
        assertTrue(state.getHeap().getContent().isEmpty());        // nothing was allocated
    }

    // ---- wH (S12)

    @Test
    void writeHeapChangesTheLocation() throws MyException {
        new NewStmt("v", num(20)).execute(state);
        new WriteHeapStmt("v", num(30)).execute(state);
        assertEquals(new IntValue(30), state.getHeap().get(1));
        assertEquals(new RefValue(1, new IntType()), state.getSymTable().lookup("v"));   // unchanged
    }

    @Test
    void writeHeapCanUseTheOldValue() throws MyException {
        new NewStmt("v", num(20)).execute(state);
        new WriteHeapStmt("v", new ArithExp(ArithOp.ADD, new ReadHeapExp(new VarExp("v")), num(1)))
                .execute(state);
        assertEquals(new IntValue(21), state.getHeap().get(1));
    }

    @Test
    void writeHeapOfAnUndeclaredVariableThrows() {
        var ex = assertThrows(StatementException.class,
                () -> new WriteHeapStmt("x", num(1)).execute(state));
        assertEquals("wH: x is not declared", ex.getMessage());
    }

    @Test
    void writeHeapOfAVariableThatIsNotAReferenceThrows() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state);
        var ex = assertThrows(StatementException.class,
                () -> new WriteHeapStmt("x", num(1)).execute(state));
        assertEquals("wH: x is not a reference", ex.getMessage());
    }

    @Test
    void writeHeapToAnAddressNotInTheHeapThrows() {
        // v is still (0, int): address 0 is never in the heap
        var ex = assertThrows(StatementException.class,
                () -> new WriteHeapStmt("v", num(1)).execute(state));
        assertEquals("wH: address 0 is not in the heap", ex.getMessage());
    }

    @Test
    void writeHeapWithAValueOfTheWrongTypeThrows() throws MyException {
        new NewStmt("v", num(20)).execute(state);
        assertThrows(StatementException.class,
                () -> new WriteHeapStmt("v", new ValueExp(new BoolValue(true))).execute(state));
        assertEquals(new IntValue(20), state.getHeap().get(1));    // unchanged
    }

    // ---- toString

    @Test
    void toStringsAreInTheUsualForm() {
        assertEquals("new(v, 20)", new NewStmt("v", num(20)).toString());
        assertEquals("wH(v, 30)", new WriteHeapStmt("v", num(30)).toString());
    }
}