package interpreter.controller;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.*;
import interpreter.model.stmt.*;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    @TempDir
    Path dir;                                    // Lab 6 (A3): a fresh folder for the log files
    private static PrgState newState(IStmt program) {
        return PrgState.of(program);   // Lab 5 (A3): PrgState.of instead of new PrgState(...)
    }

    // Lab 6 (A3): the repository now needs a log file, it is written in the temporary folder
    private PrgState run(IStmt program) throws MyException {
        PrgState state = newState(program);
        new Controller(new Repository(state, dir.resolve("log.txt").toString()), false).allStep();
        return state;
    }

    private static IExp num(int n) {
        return new ValueExp(new IntValue(n));
    }

    private static IStmt comp(IStmt... stmts) {
        IStmt result = stmts[stmts.length - 1];
        for (int i = stmts.length - 2; i >= 0; i--)
            result = new CompStmt(stmts[i], result);
        return result;
    }

    // ---- the three example programs of the lab

    @Test
    void example1PrintsTwo() throws MyException {
        PrgState state = run(Examples.EX1);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(2), state.getOut().get(0));
    }

    @Test
    void example2PrintsEighteen() throws MyException {
        PrgState state = run(Examples.EX2);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(new IntValue(18), state.getOut().get(0));
    }

    @Test
    void example3PrintsTwo() throws MyException {
        PrgState state = run(Examples.EX3);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(new IntValue(2), state.getOut().get(0));
    }

    @Test
    void example1FinalSymbolTableHoldsV() throws MyException {
        PrgState state = run(Examples.EX1);
        assertEquals(new IntValue(2), state.getSymTable().lookup("v"));
    }

    // ---- oneStep

    @Test
    void oneStepExecutesExactlyOneStatement() throws MyException {
        PrgState state = newState(Examples.EX1);
        Controller ctrl = new Controller(new Repository(state, dir.resolve("log.txt").toString()), false);

        ctrl.oneStep(state); // S1: Stmt1; Stmt2 -> {int v | v = 2; print(v)}
        assertFalse(state.getSymTable().isDefined("v"));

        ctrl.oneStep(state); // S3: int v
        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));
    }

    @Test
    void oneStepOnEmptyStackThrows() throws MyException {
        PrgState state = newState(new NopStmt());
        Controller ctrl = new Controller(new Repository(state, dir.resolve("log.txt").toString()), false);
        ctrl.oneStep(state); // runs the nop, the stack is now empty

        var ex = assertThrows(StatementException.class, () -> ctrl.oneStep(state));
        assertEquals("execution stack is empty", ex.getMessage());
    }

    // ---- Seminar 4, exercise B2: error programs

    @Test
    void b2aDivisionByZero() {
        // int x; x = 10 / (2 - 2); print(x)
        IStmt p = comp(new VarDeclStmt("x", new IntType()),
                new AssignStmt("x", new ArithExp(ArithOp.DIV, num(10),
                        new ArithExp(ArithOp.SUB, num(2), num(2)))),
                new PrintStmt(new VarExp("x")));
        var ex = assertThrows(ExpressionException.class, () -> run(p));
        assertEquals("division by zero", ex.getMessage());
    }

    @Test
    void b2bVariableDeclaredTwice() {
        // int x; bool x
        IStmt p = comp(new VarDeclStmt("x", new IntType()), new VarDeclStmt("x", new BoolType()));
        var ex = assertThrows(StatementException.class, () -> run(p));
        assertEquals("variable x is already declared", ex.getMessage());
    }

    @Test
    void b2cAssignTypeMismatch() {
        // int x; x = true
        IStmt p = comp(new VarDeclStmt("x", new IntType()),
                new AssignStmt("x", new ValueExp(new BoolValue(true))));
        assertThrows(StatementException.class, () -> run(p));
    }

    @Test
    void b2dIfConditionNotBoolean() {
        // int x; if x then print(1) else print(0)
        IStmt p = comp(new VarDeclStmt("x", new IntType()),
                new IfStmt(new VarExp("x"), new PrintStmt(num(1)), new PrintStmt(num(0))));
        var ex = assertThrows(StatementException.class, () -> run(p));
        assertEquals("condition of if is not a boolean", ex.getMessage());
    }

    @Test
    void b2eAssignToUndeclaredVariable() {
        // y = 3; print(y)
        IStmt p = comp(new AssignStmt("y", num(3)), new PrintStmt(new VarExp("y")));
        var ex = assertThrows(StatementException.class, () -> run(p));
        assertEquals("variable y was not declared", ex.getMessage());
    }

    @Test
    void b2fLogicOperandNotBoolean() {
        // bool b; b = 1 and true
        IStmt p = comp(new VarDeclStmt("b", new BoolType()),
                new AssignStmt("b", new LogicExp(LogicOp.AND, num(1), new ValueExp(new BoolValue(true)))));
        var ex = assertThrows(ExpressionException.class, () -> run(p));
        assertEquals("first operand is not a boolean", ex.getMessage());
    }

    // ---- Seminar 4, exercise B3

    @Test
    void b3ProgramPrintsSeven() throws MyException {
        // int a; int b; bool c; a = 5; b = a * 2 - 3; c = true or false; if c then print(b) else print(a)
        IStmt p = comp(
                new VarDeclStmt("a", new IntType()),
                new VarDeclStmt("b", new IntType()),
                new VarDeclStmt("c", new BoolType()),
                new AssignStmt("a", num(5)),
                new AssignStmt("b", new ArithExp(ArithOp.SUB,
                        new ArithExp(ArithOp.MUL, new VarExp("a"), num(2)), num(3))),
                new AssignStmt("c", new LogicExp(LogicOp.OR,
                        new ValueExp(new BoolValue(true)), new ValueExp(new BoolValue(false)))),
                new IfStmt(new VarExp("c"), new PrintStmt(new VarExp("b")), new PrintStmt(new VarExp("a"))));
        PrgState state = run(p);
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(7), state.getOut().get(0));
    }
    // ---- Lab 6 (A3): Ex4 (files), Ex5 (relational expression) and the log file

    @Test
    void example4ReadsBothNumbersAndClosesTheFile() throws Exception {
        Path in = dir.resolve("test.in");
        Files.writeString(in, "15\n50\n");
        PrgState state = run(Examples.fileExample(in.toString()));
        assertEquals(2, state.getOut().size());
        assertEquals(new IntValue(15), state.getOut().get(0));
        assertEquals(new IntValue(50), state.getOut().get(1));
        assertTrue(state.getFileTable().toMap().isEmpty());
    }

    @Test
    void example4WithAnEmptyFilePrintsZeroZero() throws Exception {
        Path in = dir.resolve("empty.in");
        Files.writeString(in, "");
        PrgState state = run(Examples.fileExample(in.toString()));
        assertEquals(new IntValue(0), state.getOut().get(0));
        assertEquals(new IntValue(0), state.getOut().get(1));
    }

    @Test
    void example4WithAMissingFileThrows() {
        IStmt p = Examples.fileExample(dir.resolve("missing.in").toString());
        assertThrows(StatementException.class, () -> run(p));
    }

    @Test
    void example5Prints5() throws MyException {
        PrgState state = run(Examples.EX5);
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(5), state.getOut().get(0));
    }

    @Test
    void logFileContainsTheFourSectionsInOrder() throws Exception {
        run(Examples.EX1);
        String log = Files.readString(dir.resolve("log.txt"));
        int a = log.indexOf("ExeStack:"), b = log.indexOf("SymTable:"),
                c = log.indexOf("Out:"), d = log.indexOf("FileTable:");
        assertTrue(0 <= a && a < b && b < c && c < d);
    }

    @Test
    void logFileHoldsTheInitialStateAndTheStateAfterEveryStep() throws Exception {
        run(Examples.EX1);          // ex1 has 5 steps: 1 composition, 1 composition, 3 statements
        String log = Files.readString(dir.resolve("log.txt"));
        int sections = log.split("ExeStack:", -1).length - 1;
        assertEquals(6, sections);  // the initial state + 5 steps
        assertTrue(log.contains("v --> 2"));
    }

    @Test
    void logFileShowsTheOpenFile() throws Exception {
        Path in = dir.resolve("test.in");
        Files.writeString(in, "15\n50\n");
        run(Examples.fileExample(in.toString()));
        String log = Files.readString(dir.resolve("log.txt"));
        assertTrue(log.contains(in.toString()));
    }
}
