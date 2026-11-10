package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;

// helper shared by the statement tests (it has no tests itself)
abstract class StmtTestBase {
    // a state with an empty stack, an empty symbol table and an empty Out
    static PrgState freshState() throws MyException {
        PrgState state = PrgState.of(new NopStmt());   // Lab 5 (A3): PrgState.of
        state.getExeStack().pop(); // remove the nop pushed by the constructor
        return state;
    }
}