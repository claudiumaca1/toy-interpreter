package interpreter.model.value;

import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.type.StringType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueTest {
    @Test
    void intValueHasIntType() {
        assertEquals(new IntType(), new IntValue(3).getType());
    }

    @Test
    void boolValueHasBoolType() {
        assertEquals(new BoolType(), new BoolValue(true).getType());
    }

    @Test
    void valuesWithSameContentAreEqual() {
        assertEquals(new IntValue(3), new IntValue(3));
        assertEquals(new BoolValue(true), new BoolValue(true));
    }

    @Test
    void valuesWithDifferentContentAreNotEqual() {
        assertNotEquals(new IntValue(3), new IntValue(4));
        assertNotEquals(new BoolValue(true), new BoolValue(false));
    }

    // ---- Lab 5 (A3): string values

    @Test
    void stringValueHasStringType() {
        assertEquals(new StringType(), new StringValue("a").getType());
    }

    @Test
    void equalStringValuesAreEqualAndHaveTheSameHashCode() {
        assertEquals(new StringValue("test.in"), new StringValue("test.in"));
        assertEquals(new StringValue("test.in").hashCode(), new StringValue("test.in").hashCode());
        assertNotEquals(new StringValue("a"), new StringValue("b"));
    }

    @Test
    void stringValueIsPrintedBetweenQuotes() {
        assertEquals("\"abc\"", new StringValue("abc").toString());
    }
}
