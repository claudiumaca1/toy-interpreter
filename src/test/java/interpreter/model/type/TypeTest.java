package interpreter.model.type;

import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
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

    // ---- Lab 7 (A4): reference types

    @Test
    void defaultValueOfARefIsTheNullAddressWithTheInnerType() {
        assertEquals(new RefValue(0, new IntType()), new RefType(new IntType()).defaultValue());
        assertEquals(new RefValue(0, new RefType(new IntType())),
                new RefType(new RefType(new IntType())).defaultValue());
    }

    @Test
    void nestedRefTypesAreComparedRecursively() {
        assertEquals(new RefType(new RefType(new IntType())), new RefType(new RefType(new IntType())));
        assertNotEquals(new RefType(new RefType(new IntType())), new RefType(new IntType()));
        assertNotEquals(new RefType(new IntType()), new RefType(new BoolType()));
        assertNotEquals(new RefType(new IntType()), new IntType());
    }

    @Test
    void refTypeIsPrintedWithParentheses() {
        assertEquals("Ref(Ref(int))", new RefType(new RefType(new IntType())).toString());
    }
}