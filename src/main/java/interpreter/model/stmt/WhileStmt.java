package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.BoolValue;

// Lab 7 (A4), rule S13: while (cond) body
public record WhileStmt(IExp cond, IStmt body) implements IStmt {
    // Lab 7 (A4): no Java loop - if cond is true, the while is pushed back on the stack and
    // the body on top of it (so the body runs first, then the condition is checked again);
    // if cond is false nothing is pushed and the loop ends. A non-boolean cond is an error.
    @Override
    public PrgState execute(PrgState state) throws MyException {
        if (!(cond.eval(state.getSymTable(), state.getHeap()) instanceof BoolValue(boolean c)))
            throw new StatementException("condition " + cond + " is not a boolean");
        if (c) {
            state.getExeStack().push(this);
            state.getExeStack().push(body);
        }
        return state;
    }

    // Lab 7 (A4): printed as (while (cond) body)
    @Override
    public String toString() {
        return "(while (" + cond + ") " + body + ")";
    }
}