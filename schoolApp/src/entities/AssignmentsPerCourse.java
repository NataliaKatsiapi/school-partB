package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AssignmentsPerCourse {
    
    private Course course;
    private List<Assignment> assignments;

    public AssignmentsPerCourse() {
    }

    public AssignmentsPerCourse(Course course) {
        this.course = course;
        assignments = new ArrayList();
    }

    public AssignmentsPerCourse(Course course, List<Assignment> assignments) {
        this.course = course;
        this.assignments = assignments;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.course);
        hash = 59 * hash + Objects.hashCode(this.assignments);
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
        final AssignmentsPerCourse other = (AssignmentsPerCourse) obj;
        if (!Objects.equals(this.course, other.course)) {
            return false;
        }
        if (!Objects.equals(this.assignments, other.assignments)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AssignmentsPerCourse{" + "course=" + course + ", assignments=" + assignments + '}';
    }
           
}
