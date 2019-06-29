package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudentsPerCourse {
   
    private Course course;
    private List<Student> students;

    public StudentsPerCourse() {
    }

    public StudentsPerCourse(Course course) {
        this.course = course;
        students = new ArrayList();
    }

    public StudentsPerCourse(Course course, List<Student> studentsPerCourse) {
        this.course = course;
        this.students = studentsPerCourse;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
    
    public void addStudentToList(Student s) {
        
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.course);
        hash = 47 * hash + Objects.hashCode(this.students);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StudentsPerCourse other = (StudentsPerCourse) obj;
        if (!Objects.equals(this.course, other.course)) {
            return false;
        }
        if (!Objects.equals(this.students, other.students)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "StudentsPerCourse{" + "course=" + course + ", studentsPerCourse=" + students + '}';
    }
    
}
