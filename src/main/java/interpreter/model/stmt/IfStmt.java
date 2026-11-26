package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.BoolValue;

// rule S5
public record IfStmt(IExp cond, IStmt thenS, IStmt elseS) implements IStmt {
    // Lab 7 (A4): changed - the expression is evaluated with the heap too
    @Override
    public PrgState execute(PrgState state) throws MyException {
        if (!(cond.eval(state.getSymTable(), state.getHeap()) instanceof BoolValue(boolean c)))
            throw new StatementException("condition of if is not a boolean");
        if (c)
            state.getExeStack().push(thenS);
        else
            state.getExeStack().push(elseS);
        return state;
    }

    @Override
    public String toString() {
        return "(if " + cond + " then " + thenS + " else " + elseS + ")";
    }
}