package interpreter.model.stmt;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

// Lab 5 (A3): tests for openRFile, readFile and closeRFile (rules S8-S10); the input files
// are written in a @TempDir, so nothing depends on files of the project folder
class FileStmtTest {
    @TempDir
    Path dir;                                   // a new empty folder for every test

    private PrgState state;
    private ValueExp file;                      // the expression "<dir>/numbers.in"
    private StringValue fileName;

    @BeforeEach
    void setUp() throws Exception {
        Path f = dir.resolve("numbers.in");
        Files.writeString(f, "7\n8\n");
        fileName = new StringValue(f.toString());
        file = new ValueExp(fileName);
        state = PrgState.of(new NopStmt());
        new VarDeclStmt("x", new IntType()).execute(state);
    }

    // ---- openRFile (S8)

    @Test
    void openAddsTheFileToTheFileTable() throws MyException {
        new OpenRFileStmt(file).execute(state);
        assertTrue(state.getFileTable().isDefined(fileName));
    }

    @Test
    void openWithANonStringExpressionThrows() {
        var stmt = new OpenRFileStmt(new ValueExp(new IntValue(1)));
        assertThrows(StatementException.class, () -> stmt.execute(state));
    }

    @Test
    void openingTheSameFileTwiceThrows() throws MyException {
        new OpenRFileStmt(file).execute(state);
        assertThrows(StatementException.class, () -> new OpenRFileStmt(file).execute(state));
    }

    @Test
    void openingAMissingFileThrows() {
        var missing = new ValueExp(new StringValue(dir.resolve("missing.in").toString()));
        assertThrows(StatementException.class, () -> new OpenRFileStmt(missing).execute(state));
    }

    @Test
    void errorInTheExpressionOfOpenIsPropagated() {
        assertThrows(ExpressionException.class, () -> new OpenRFileStmt(new VarExp("zzz")).execute(state));
    }

    // ---- readFile (S9)

    @Test
    void readGivesTheNumbersInOrderThenZeroAtTheEnd() throws MyException {
        new OpenRFileStmt(file).execute(state);
        new ReadFileStmt(file, "x").execute(state);
        assertEquals(new IntValue(7), state.getSymTable().lookup("x"));
        new ReadFileStmt(file, "x").execute(state);
        assertEquals(new IntValue(8), state.getSymTable().lookup("x"));
        new ReadFileStmt(file, "x").execute(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));
    }

    @Test
    void readIntoAVariableThatIsNotDeclaredThrows() throws MyException {
        new OpenRFileStmt(file).execute(state);
        assertThrows(StatementException.class, () -> new ReadFileStmt(file, "nope").execute(state));
    }

    @Test
    void readIntoAVariableThatIsNotIntThrows() throws MyException {
        new OpenRFileStmt(file).execute(state);
        new VarDeclStmt("b", new BoolType()).execute(state);
        assertThrows(StatementException.class, () -> new ReadFileStmt(file, "b").execute(state));
    }

    @Test
    void readFromAFileThatIsNotOpenThrows() {
        assertThrows(StatementException.class, () -> new ReadFileStmt(file, "x").execute(state));
    }

    @Test
    void readWithANonStringExpressionThrows() {
        var stmt = new ReadFileStmt(new ValueExp(new IntValue(1)), "x");
        assertThrows(StatementException.class, () -> stmt.execute(state));
    }

    @Test
    void readOfALineThatIsNotAnIntegerThrows() throws Exception {
        Path bad = dir.resolve("bad.in");
        Files.writeString(bad, "abc\n");
        var badFile = new ValueExp(new StringValue(bad.toString()));
        new OpenRFileStmt(badFile).execute(state);
        var ex = assertThrows(StatementException.class, () -> new ReadFileStmt(badFile, "x").execute(state));
        assertTrue(ex.getMessage().contains("not an integer"));
    }

    @Test
    void readFromAnEmptyFileGivesZero() throws Exception {
        Path empty = dir.resolve("empty.in");
        Files.writeString(empty, "");
        var emptyFile = new ValueExp(new StringValue(empty.toString()));
        new OpenRFileStmt(emptyFile).execute(state);
        new ReadFileStmt(emptyFile, "x").execute(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));
    }

    // ---- closeRFile (S10)

    @Test
    void closeRemovesTheFileFromTheFileTable() throws MyException {
        new OpenRFileStmt(file).execute(state);
        new CloseRFileStmt(file).execute(state);
        assertFalse(state.getFileTable().isDefined(fileName));
    }

    @Test
    void closingAFileThatIsNotOpenThrows() {
        assertThrows(StatementException.class, () -> new CloseRFileStmt(file).execute(state));
    }

    @Test
    void closeWithANonStringExpressionThrows() {
        var stmt = new CloseRFileStmt(new ValueExp(new IntValue(1)));
        assertThrows(StatementException.class, () -> stmt.execute(state));
    }

    @Test
    void readAfterCloseThrows() throws MyException {
        new OpenRFileStmt(file).execute(state);
        new CloseRFileStmt(file).execute(state);
        assertThrows(StatementException.class, () -> new ReadFileStmt(file, "x").execute(state));
    }

    // ---- toString (infix form)

    @Test
    void toStringsAreInTheUsualForm() {
        assertEquals("openRFile(f)", new OpenRFileStmt(new VarExp("f")).toString());
        assertEquals("readFile(f, x)", new ReadFileStmt(new VarExp("f"), "x").toString());
        assertEquals("closeRFile(f)", new CloseRFileStmt(new VarExp("f")).toString());
    }
}
