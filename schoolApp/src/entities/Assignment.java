package entities;

import daos.AssignmentDao;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Assignment {

    private int id;
    private String title;
    private String description;
    private LocalDate subDate;
    private Course course;
    private double oralMark;
    private double totalMark;

    public Assignment() {
    }

    public Assignment(String title, String description, LocalDate subDate, Course course) {
        this.title = title;
        this.description = description;
        this.subDate = subDate;
        this.course = course;
    }

    public Assignment(int id, String title, String description, LocalDate subDate, Course course) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.subDate = subDate;
        this.course = course;
    }
        public Assignment(int id, String title, String description, LocalDate subDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.subDate = subDate;
    }

    public Assignment(int id, String title, String description, LocalDate subDate, double oralMark, double totalMark) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.subDate = subDate;
        this.course = course;
        this.oralMark = oralMark;
        this.totalMark = totalMark;
    }

    public int getId() {
        return id;
    }

    public double getOralMark() {
        return oralMark;
    }

    public void setOralMark(double oralMark) {
        this.oralMark = oralMark;
    }

    public double getTotalMark() {
        return totalMark;
    }

    public void setTotalMark(double totalMark) {
        this.totalMark = totalMark;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getSubDate() {
        return subDate;
    }

    public void setSubDate(LocalDate subDate) {
        this.subDate = subDate;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public static void printAllAssignments() {
        AssignmentDao adao = new AssignmentDao();
        List<Assignment> assignments = adao.getAssignments();
        for (Assignment a : assignments) {
            System.out.println(a);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.id;
        hash = 71 * hash + Objects.hashCode(this.title);
        hash = 71 * hash + Objects.hashCode(this.description);
        hash = 71 * hash + Objects.hashCode(this.subDate);
        hash = 71 * hash + Objects.hashCode(this.course);
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
        final Assignment other = (Assignment) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.subDate, other.subDate)) {
            return false;
        }
        if (!Objects.equals(this.course, other.course)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Assignment{" + "id: " + id + ", title: " + title + ", description: " + description + ", subDate:" + subDate +
                (course != null ? " course:  id:" + course.getId() + " title: " + course.getTitle() : "")
                + (oralMark != 0 ? " oral mark: " + oralMark : "") + (totalMark != 0 ? " total mark: " + totalMark : "") + '}';
    }

}
