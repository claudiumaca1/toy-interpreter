package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

// Lab 7 (A4), rule S11: new(v, exp) - heap allocation
public record NewStmt(String varName, IExp exp) implements IStmt {
    // Lab 7 (A4): evaluates exp, stores the value at a new address a of the heap and sets
    // v -> (a, T), where (_, T) is the old value of v. Errors: v not declared, v is not a
    // reference, the type of the value is not T.
    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        if (!symTable.isDefined(varName))
            throw new StatementException("new: " + varName + " is not declared");
        if (!(symTable.lookup(varName) instanceof RefValue ref))
            throw new StatementException("new: " + varName + " is not a reference");
        IValue value = exp.eval(symTable, state.getHeap());
        if (!value.getType().equals(ref.locationType()))
            throw new StatementException("new: " + exp + " has type " + value.getType()
                    + ", expected " + ref.locationType());
        int address = state.getHeap().allocate(value);
        symTable.update(varName, new RefValue(address, ref.locationType()));
        return state;
    }

    // Lab 7 (A4): printed as new(v, exp)
    @Override
    public String toString() {
        return "new(" + varName + ", " + exp + ")";
    }
}