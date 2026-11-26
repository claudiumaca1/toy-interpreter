package interpreter.model.exp;

import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.value.IValue;

public record ValueExp(IValue value) implements IExp {
    // Lab 7 (A4): changed - new parameter heap (not used, a constant needs nothing)
    @Override
    public IValue eval(IDictionary<String, IValue> symTable, IHeap heap) {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}