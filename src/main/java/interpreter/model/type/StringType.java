package interpreter.model.type;

import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;

// Lab 5 (A3): the new type "string"; a string variable starts with the value ""
public record StringType() implements IType {
    @Override
    public IValue defaultValue() {
        return new StringValue("");
    }

    @Override
    public String toString() {
        return "string";
    }
}