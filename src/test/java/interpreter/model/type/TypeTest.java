package interpreter.model.type;

import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypeTest {
    @Test
    void defaultValueOfIntIsZero() {
        assertEquals(new IntValue(0), new IntType().defaultValue());
    }

    @Test
    void defaultValueOfBoolIsFalse() {
        assertEquals(new BoolValue(false), new BoolType().defaultValue());
    }

    @Test
    void sameTypesAreEqual() {
        assertEquals(new IntType(), new IntType());
        assertEquals(new BoolType(), new BoolType());
    }

    @Test
    void differentTypesAreNotEqual() {
        assertNotEquals(new IntType(), new BoolType());
    }

    // ---- Lab 5 (A3): string type

    @Test
    void defaultValueOfStringIsTheEmptyString() {
        assertEquals(new StringValue(""), new StringType().defaultValue());
    }

    @Test
    void stringTypeIsNotEqualToIntType() {
        assertEquals(new StringType(), new StringType());
        assertNotEquals(new StringType(), new IntType());
    }
}
