package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

// Lab 7 (A4), rule S12: wH(v, exp) - writes the heap
public record WriteHeapStmt(String varName, IExp exp) implements IStmt {
    // Lab 7 (A4): (a, T) = the value of v; evaluates exp and stores the value at address a of
    // the heap (the SymTable does not change). Errors: v not declared, v is not a reference,
    // a is not in the heap, the type of the value is not T.
    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        var heap = state.getHeap();
        if (!symTable.isDefined(varName))
            throw new StatementException("wH: " + varName + " is not declared");
        if (!(symTable.lookup(varName) instanceof RefValue ref))
            throw new StatementException("wH: " + varName + " is not a reference");
        if (!heap.isDefined(ref.address()))
            throw new StatementException("wH: address " + ref.address() + " is not in the heap");
        IValue value = exp.eval(symTable, heap);
        if (!value.getType().equals(ref.locationType()))
            throw new StatementException("wH: " + exp + " has type " + value.getType()
                    + ", expected " + ref.locationType());
        heap.update(ref.address(), value);
        return state;
    }

    // Lab 7 (A4): printed as wH(v, exp)
    @Override
    public String toString() {
        return "wH(" + varName + ", " + exp + ")";
    }
}