package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyDictionaryTest {
    private IDictionary<String, Integer> dict;

    @BeforeEach
    void setUp() {
        dict = new MyDictionary<>();
    }

    @Test
    void putThenLookupReturnsValue() throws ADTException {
        dict.put("a", 5);
        assertEquals(5, dict.lookup("a"));
    }

    @Test
    void isDefinedIsTrueOnlyForExistingKeys() {
        dict.put("a", 5);
        assertTrue(dict.isDefined("a"));
        assertFalse(dict.isDefined("b"));
    }

    @Test
    void updateChangesTheValue() throws ADTException {
        dict.put("a", 5);
        dict.update("a", 7);
        assertEquals(7, dict.lookup("a"));
    }

    @Test
    void lookupOfMissingKeyThrows() {
        assertThrows(ADTException.class, () -> dict.lookup("missing"));
    }

    @Test
    void updateOfMissingKeyThrows() {
        assertThrows(ADTException.class, () -> dict.update("missing", 1));
    }

    // ---- Lab 5 (A3): remove and toMap

    @Test
    void removeDeletesTheKey() throws ADTException {
        dict.put("a", 5);
        dict.remove("a");
        assertFalse(dict.isDefined("a"));
    }

    @Test
    void removeOfMissingKeyThrows() {
        assertThrows(ADTException.class, () -> dict.remove("missing"));
    }

    @Test
    void toMapReturnsACopyOfTheContent() {
        dict.put("a", 5);
        assertEquals(java.util.Map.of("a", 5), dict.toMap());
        assertThrows(UnsupportedOperationException.class, () -> dict.toMap().put("b", 1));
    }
}
