package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StudentManagerTest {

    private StudentManager sm;

    @BeforeEach
    void setUp() {
        sm = new StudentManager();
    }

    @Test
    void addStudent() {
        sm.add("Alice");
        assertTrue(sm.contains("Alice"));
        assertEquals(1, sm.size());
    }

    @Test
    void removeStudent() {
        sm.add("Bob");
        sm.remove("Bob");
        assertFalse(sm.contains("Bob"));
        assertEquals(0, sm.size());
    }

    @Test
    void duplicateAddThrows() {
        sm.add("Carol");
        assertThrows(IllegalArgumentException.class, () -> sm.add("Carol"));
    }

    @Test
    void removeMissingThrows() {
        assertThrows(IllegalArgumentException.class, () -> sm.remove("Ghost"));
    }

    @Test
    void multipleAddSize() {
        sm.add("A");
        sm.add("B");
        sm.add("C");
        assertEquals(3, sm.size());
    }
}
