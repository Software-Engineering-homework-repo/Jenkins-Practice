package manager;

import java.util.HashSet;
import java.util.Set;

public class StudentManager {
    private final Set<String> students = new HashSet<>();

    public void addStudent(String name) {
        if (students.contains(name)) {
            throw new IllegalArgumentException("Duplicate student: " + name);
        }
        students.add(name);
    }

    public void removeStudent(String name) {
        if (!students.contains(name)) {
            throw new IllegalArgumentException("Student not found: " + name);
        }
        students.remove(name);
    }

    public boolean hasStudent(String name) {
        return students.contains(name);
    }

    public int size() {
        return students.size();
    }
}
