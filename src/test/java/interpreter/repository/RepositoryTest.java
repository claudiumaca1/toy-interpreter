package interpreter.repository;

import interpreter.model.PrgState;
import interpreter.model.stmt.NopStmt;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

class RepositoryTest {
    @Test
    void getCrtPrgReturnsTheStoredState() {
        PrgState state = PrgState.of(new NopStmt());   // Lab 5 (A3): PrgState.of
        IRepository repo = new Repository(state);
        assertSame(state, repo.getCrtPrg());
    }
}