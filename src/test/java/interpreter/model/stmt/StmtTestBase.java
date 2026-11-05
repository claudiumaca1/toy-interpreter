package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;

// helper shared by the statement tests (it has no tests itself)
abstract class StmtTestBase {
    // a state with an empty stack, an empty symbol table and an empty Out
    static PrgState freshState() throws MyException {
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
        state.getExeStack().pop(); // remove the nop pushed by the constructor
        return state;
    }
}
