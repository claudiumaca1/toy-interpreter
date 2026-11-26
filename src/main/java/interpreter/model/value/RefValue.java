package interpreter.model.value;

import interpreter.model.type.IType;
import interpreter.model.type.RefType;

// Lab 7 (A4): a reference value (address, locationType) - the address of a heap location and
// the type of the value stored there. (1, int) has type Ref int, (10, Ref int) has type
// Ref Ref int. The address 0 is invalid (null).
public record RefValue(int address, IType locationType) implements IValue {
    // Lab 7 (A4): the type of a reference to locationType, i.e. Ref(locationType)
    @Override
    public IType getType() {
        return new RefType(locationType);
    }

    // Lab 7 (A4): printed as the pair (address, locationType), e.g. (1, int)
    @Override
    public String toString() {
        return "(" + address + ", " + locationType + ")";
    }
}