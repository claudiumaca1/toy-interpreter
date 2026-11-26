package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;

// rule S4
public record PrintStmt(IExp exp) implements IStmt {
    // Lab 7 (A4): changed - the expression is evaluated with the heap too
    @Override
    public PrgState execute(PrgState state) throws MyException {
        state.getOut().add(exp.eval(state.getSymTable(), state.getHeap()));
        return state;
    }

    @Override
    public String toString() {
        return "print(" + exp + ")";
    }
}