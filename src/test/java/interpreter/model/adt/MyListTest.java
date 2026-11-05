package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyListTest {
    private IList<String> list;

    @BeforeEach
    void setUp() {
        list = new MyList<>();
    }

    @Test
    void newListHasSizeZero() {
        assertEquals(0, list.size());
    }

    @Test
    void addIncreasesSizeAndKeepsOrder() throws ADTException {
        list.add("x");
        list.add("y");
        assertEquals(2, list.size());
        assertEquals("x", list.get(0));
        assertEquals("y", list.get(1));
    }

    @Test
    void getWithIndexTooBigThrows() {
        list.add("x");
        assertThrows(ADTException.class, () -> list.get(1));
    }

    @Test
    void getWithNegativeIndexThrows() {
        list.add("x");
        assertThrows(ADTException.class, () -> list.get(-1));
    }
}
