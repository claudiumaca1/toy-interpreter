package interpreter.repository;

import interpreter.exceptions.RepositoryException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.stmt.NopStmt;
import interpreter.model.stmt.PrintStmt;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryTest {
    @TempDir
    Path dir;                                           // Lab 6 (A3): folder for the log file

    @Test
    void getCrtPrgReturnsTheStoredState() {
        PrgState state = PrgState.of(new NopStmt());   // Lab 5 (A3): PrgState.of
        IRepository repo = new Repository(state, dir.resolve("log.txt").toString());
        assertSame(state, repo.getCrtPrg());
    }

    // ---- Lab 6 (A3): the log file

    @Test
    void logAppendsTheStateEveryTime() throws Exception {
        Path log = dir.resolve("log.txt");
        IRepository repo = new Repository(PrgState.of(new NopStmt()), log.toString());
        repo.logPrgStateExec();
        repo.logPrgStateExec();
        String text = Files.readString(log);
        assertEquals(2, text.split("ExeStack:", -1).length - 1);
    }

    @Test
    void logHasOneStackElementPerLineWithTheTopFirst() throws Exception {
        Path log = dir.resolve("log.txt");
        PrgState state = PrgState.of(new NopStmt());
        state.getExeStack().push(new PrintStmt(new ValueExp(new IntValue(1))));
        new Repository(state, log.toString()).logPrgStateExec();
        String text = Files.readString(log);
        assertTrue(text.startsWith("ExeStack:\nprint(1)\nnop\nSymTable:\n"));
    }

    @Test
    void logShowsTheSymbolTableAndTheOut() throws Exception {
        Path log = dir.resolve("log.txt");
        PrgState state = PrgState.of(new NopStmt());
        state.getSymTable().put("a", new IntValue(5));
        state.getOut().add(new IntValue(7));
        new Repository(state, log.toString()).logPrgStateExec();
        String text = Files.readString(log);
        assertTrue(text.contains("SymTable:\na --> 5\nOut:\n7\nFileTable:\n"));
    }

    @Test
    void logFileThatCannotBeWrittenThrowsRepositoryException() {
        String badPath = dir.resolve("no_such_folder").resolve("log.txt").toString();
        IRepository repo = new Repository(PrgState.of(new NopStmt()), badPath);
        assertThrows(RepositoryException.class, repo::logPrgStateExec);
    }
}