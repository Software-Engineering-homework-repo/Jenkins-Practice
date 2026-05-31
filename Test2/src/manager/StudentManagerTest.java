package manage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

// @Order 번호 순서대로 테스트를 실행하도록 지정
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentManagerTest {

    private static StudentManager manager;

    @BeforeAll
    static void setUp() {
        manager = new StudentManager();
    }
    
    
    @Test
    @Order(1)
    void testAddStudent() {
        manager.addStudent("이호재");
        assertTrue(manager.hasStudent("이호재"));
    }

    // 2. 중복 추가 예외 처리 
    @Test
    @Order(2)
    void testAddDuplicateStudent() {
        assertThrows(IllegalArgumentException.class, () -> {
            manager.addStudent("이호재");
        });
    }

    // 3. 학생 제거
    @Test
    @Order(3)
    void testRemoveStudent() {
        manager.removeStudent("이호재");
        assertFalse(manager.hasStudent("이호재"));
    }

    // 4. 존재하지 않는 학생 제거 예외 처리
    @Test
    @Order(4)
    void testRemoveNonexistentStudent() {
        assertThrows(IllegalArgumentException.class, () -> {
            manager.removeStudent("이호재");
        });
    }
}
