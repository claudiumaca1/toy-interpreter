package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

// Lab 7 (A4), rule E6: rH(exp) - reads the heap
public record ReadHeapExp(IExp exp) implements IExp {
    // Lab 7 (A4): exp must evaluate to a reference (a, T); the result is the value stored in
    // the heap at address a. Errors: exp is not a reference, a is not in the heap.
    @Override
    public IValue eval(IDictionary<String, IValue> symTable, IHeap heap) throws MyException {
        if (!(exp.eval(symTable, heap) instanceof RefValue ref))
            throw new ExpressionException("rH: " + exp + " is not a reference");
        if (!heap.isDefined(ref.address()))
            throw new ExpressionException("rH: address " + ref.address() + " is not in the heap");
        return heap.get(ref.address());
    }

    // Lab 7 (A4): printed as rH(exp)
    @Override
    public String toString() {
        return "rH(" + exp + ")";
    }
}