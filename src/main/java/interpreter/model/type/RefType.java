package interpreter.model.type;

import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

// Lab 7 (A4): the reference type "Ref inner", e.g. Ref int or Ref Ref int. It is recursive:
// inner can be any type, another RefType included. Because it is a record, equals compares
// the inner types recursively: Ref(Ref(int)) equals Ref(Ref(int)), but not Ref(int).
public record RefType(IType inner) implements IType {
    // Lab 7 (A4): a Ref T variable starts with (0, T); the address 0 plays the role of null
    @Override
    public IValue defaultValue() {
        return new RefValue(0, inner);
    }

    // Lab 7 (A4): printed as Ref(int), Ref(Ref(int)), ...
    @Override
    public String toString() {
        return "Ref(" + inner + ")";
    }
}