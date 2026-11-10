package interpreter.model.value;

import interpreter.model.type.IType;
import interpreter.model.type.StringType;

// Lab 5 (A3): a string value, e.g. "test.in"; it is a record, so equals and hashCode
// compare the text (needed because it is the key of the FileTable)
public record StringValue(String val) implements IValue {
    @Override
    public IType getType() {
        return new StringType();
    }

    @Override
    public String toString() {
        return "\"" + val + "\"";
    }
}